package org.makumba.parade.view.managers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.File;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.model.managers.ServletContainer;
import org.makumba.parade.tools.DisplayFormatter;
import org.makumba.parade.view.interfaces.FileView;
import org.makumba.parade.view.interfaces.TreeView;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FileViewManager implements FileView, TreeView {
    
    static Logger logger = Logger.getLogger(FileViewManager.class.getName());
    
    // for caching tree computation
    // TODO refactor this using Events
    
    private static HashMap treeExpried = new HashMap();
    private static HashMap branches = new HashMap();
    
    public static synchronized void setTreeExpried(String n) {
        logger.info("Setting tree of row "+n+" as expired.");
        treeExpried.put(n, new Boolean(true));
    }
    
    public void setFileView(SimpleHash fileView, Row r, String path, File f) {
        
        String pathEncoded = "";
        try {
            pathEncoded = URLEncoder.encode(URLEncoder.encode(f.getPath().substring(r.getRowpath().length() + 1), "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        fileView.put("path", f.getPath().substring(r.getRowpath().length() + 1));
        fileView.put("pathEncoded", pathEncoded);
        fileView.put("name", f.getName());
        fileView.put("isDir", f.getIsDir());

        // icons
        String fl = f.getName().toLowerCase();
        String image = "unknown";

        if (fl.endsWith(".java"))
            image = "java";
        if (fl.endsWith(".mdd") || fl.endsWith(".idd"))
            image = "text";
        if (fl.endsWith(".jsp") || fl.endsWith(".properties") || fl.endsWith(".xml") || fl.endsWith(".txt")
                || fl.endsWith(".conf"))
            image = "text";
        if (fl.endsWith(".doc") || fl.endsWith(".jsp") || fl.endsWith(".html") || fl.endsWith(".htm")
                || fl.endsWith(".rtf"))
            image = "layout";
        if (fl.endsWith(".gif") || fl.endsWith(".png") || fl.endsWith(".jpg") || fl.endsWith(".jpeg"))
            image = "image";
        if (fl.endsWith(".zip") || fl.endsWith(".gz") || fl.endsWith(".tgz") || fl.endsWith(".jar"))
            image = "zip";
        if (fl.endsWith(".avi") || fl.endsWith(".mpg") || fl.endsWith(".mpeg") || fl.endsWith(".mov"))
            image = "movie";
        if (fl.endsWith(".au") || fl.endsWith(".mid") || fl.endsWith(".vaw") || fl.endsWith(".mp3"))
            image = "sound";
        
        fileView.put("image", image);

        // name
        String addr = "";
        RowWebapp webappdata = (RowWebapp) r.getRowdata().get("webapp");
        String webappPath = webappdata.getWebappPath();
        
        if (webappdata.getStatus().intValue() == ServletContainer.RUNNING
                && path.startsWith(webappPath)) {
            
            String pathURI = path.substring(path.indexOf(webappPath) + webappPath.length()).replace(
                    java.io.File.separatorChar, '/')
                    + "/";

            if (fl.endsWith(".java")) {
                String dd = pathURI + f.getName();
                dd = dd.substring(dd.indexOf("classes") + 8, dd.lastIndexOf(".")).replace('/', '.');
                addr = "/" + r.getRowname() + "/classes/" + dd;
            }
            if (fl.endsWith(".mdd") || fl.endsWith(".idd")) {
                String dd = pathURI + f.getName();
                dd = dd.substring(dd.indexOf("dataDefinitions") + 16, dd.lastIndexOf(".")).replace('/', '.');
                addr = "/" + r.getRowname() + "/dataDefinitions/" + dd;
            }
            if (fl.endsWith(".jsp") || fl.endsWith(".html") || fl.endsWith(".htm") || fl.endsWith(".txt")
                    || fl.endsWith(".gif") || fl.endsWith(".png") || fl.endsWith(".jpeg") || fl.endsWith(".jpg")
                    || fl.endsWith(".css") || fl.startsWith("readme"))
                addr = "/" + r.getRowname() + pathURI + f.getName();

            if (fl.endsWith(".jsp"))
                addr += "x";
        }
        
        fileView.put("isLinked", new Boolean(!addr.equals("")));
        fileView.put("address", addr);
       
        // time && size
        
        fileView.put("dateLong", new java.util.Date(f.getDate().longValue()).toString());
        fileView.put("dateNice", DisplayFormatter.readableTime(f.getAge().longValue()));
        fileView.put("isEmpty", f.getSize().longValue() < 0l);
        fileView.put("sizeLong", f.getSize());
        fileView.put("sizeNice", DisplayFormatter.readableBytes(f.getSize().longValue()));
    }

    public String getTreeView(Parade p, Row r) {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);
        
        /* Initalising template */
        Template temp = null;
        try {
            temp = InitServlet.getFreemarkerCfg().getTemplate("tree.ftl");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        List b = computeTree(r);

        /* Creating data model */
        SimpleHash root = new SimpleHash();
        root.put("rowName", r.getRowname());
        root.put("branches", b);
        
        /* Merge data model with template */
        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return result.toString();
    }

    /**
     * Computes the tree for a row
     * @param r the Row for which the tree should be computed
     * @return a List containing the tree of folders
     * 
     * FIXME this is a performance killer because it issues one SELECT each time it looks up a subdir
     * it should instead fetch the whole tree at once (meaning the parentdirs column) and then compute the tree
     * by an algorithm.
     * 
     */
    private List computeTree(Row r) {
        logger.info("Starting computation of tree for row "+r.getRowname()+" at " + new java.util.Date());
        long start = System.currentTimeMillis();
        
        File baseFile = (File) r.getFiles().get(r.getRowpath());
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        
        List base = baseFile.getSubdirs(s);
        
        tx.commit();
        s.close();
        
        String depth = new String("0");
        List b = null;
        
        if(branches.get(r.getRowname()) == null || (Boolean) treeExpried.get(r.getRowname()) ) {
            b = new LinkedList();
            getTreeBranch(b, base, 0, r, depth, 0);
            branches.put(r.getRowname(), b);
            treeExpried.put(r.getRowname(), new Boolean(false));
            
            long end = System.currentTimeMillis();
            long refresh = end - start;
            logger.info("Finishing tree computation for row "+r.getRowname()+" without cache at " + new java.util.Date() + ", computation took "+refresh+" ms.");
        } else {
            b = (List) branches.get(r.getRowname());
            
            long end = System.currentTimeMillis();
            long refresh = end - start;
            logger.info("Finishing tree computation for row "+r.getRowname()+" with cache at " + new java.util.Date() + ", computation took "+refresh+" ms.");
        }
        
        return b;
    }

    private void getTreeBranch(List branches, List tree, int treeLine, Row r, String depth, int level) {

        String treeRow = "";
        
        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        
        for (int i = 0; i < tree.size(); i++) {

            File currentFile = (File) tree.get(i);
            List currentTree = currentFile.getSubdirs(s);

            depth = depth + "," + i; // make last one different
            level++;
            treeLine = treeLine++;

            treeRow = "objTreeMenu"; // start a javascript line to compose a tree

            StringTokenizer st = new StringTokenizer(depth, ",");
            while (st.hasMoreTokens()) {
                treeRow = treeRow + ".n[" + st.nextToken() + "]";
            }
            
            SimpleHash branch = new SimpleHash();
            
            // converting the path to something nice
            String nicePath = (currentFile.getPath().substring(r.getRowpath().length()+1)).replace(java.io.File.separator, "/");
            
            try {
                branch.put("treeRow", treeRow);
                branch.put("fileName", currentFile.getName());
                branch.put("filePath", URLEncoder.encode(nicePath, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            branches.add(branch);

            if (level < 50 && currentTree.size() != 0)
                getTreeBranch(branches, currentTree, treeLine, r, depth, level);

            level--;
            depth = depth.substring(0, depth.lastIndexOf(','));
        }
        
        tx.commit();
        s.close();
        
    }

}
