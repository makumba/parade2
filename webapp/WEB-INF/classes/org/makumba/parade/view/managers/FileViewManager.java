package org.makumba.parade.view.managers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.tools.ParadeLogger;
import org.makumba.parade.view.interfaces.TreeView;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FileViewManager implements TreeView {

    static Logger logger = ParadeLogger.getParadeLogger(FileViewManager.class.getName());

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

        List<SimpleHash> b = computeTree(r);

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
     * 
     * @param r
     *            the Row for which the tree should be computed
     * @return a List containing the tree of folders
     * 
     */
    @SuppressWarnings("unchecked")
    private List<SimpleHash> computeTree(Row r) {
        List<SimpleHash> b = new LinkedList<SimpleHash>();

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        Query q = s
                .createSQLQuery(
                        "SELECT * FROM File f JOIN Row r WHERE f.row = r.row AND f.isDir = '1' AND r.rowname = ? ORDER BY f.path ASC")
                .addScalar("PATH", Hibernate.STRING).addScalar("NAME", Hibernate.STRING);
        q.setString(0, r.getRowname());

        List<Object[]> l = q.list();

        // this vector holds the order of a directory in a given level
        // the position in the vector represents the level, the value represents the order
        Vector<Integer> levels = new Vector<Integer>();
        Vector<String> directories = new Vector<String>();
        Integer order = 0;
        Integer previousLevel = -1;

        levels.add(0, 0);

        Iterator<Object[]> i = l.iterator();
        while (i.hasNext()) {
            Object[] line = i.next();
            String path = (String) line[0];
            String name = (String) line[1];

            String simplePath = null;
            try {
                simplePath = !path.equals(r.getRowpath()) ? path.substring(path.indexOf(r.getRowpath())
                        + r.getRowpath().length() + 1) : r.getRowname();
            } catch (StringIndexOutOfBoundsException e) {
                logger.warning("Symbolic link detected while computing the tree, " + path + " of row " + r.getRowname()
                        + " links to something outside of the row");
            }

            if (simplePath == null)
                continue;

            simplePath = simplePath.replace(java.io.File.separatorChar, '/');
            if (!simplePath.equals(r.getRowname()))
                simplePath = r.getRowname() + "/" + simplePath;

            // we split the path in directory names
            StringTokenizer st = new StringTokenizer(simplePath, "/");
            int level = -1;

            // for each directory, we store its name, level and order
            while (st.hasMoreTokens()) {

                level++;
                directories.add(level, st.nextToken());

                // we reach the end of the path
                if (!st.hasMoreTokens()) {
                    // we are in a situation where the previous path and the current one are on the same level
                    // so we increment the order of the current path
                    if (level == previousLevel) {
                        order = levels.get(level) + 1;
                        levels.add(level, order);

                        // we are one level above the one of the previous path
                        // so we reset the level beneath us
                        // as well we increment the current order
                    } else if (level < previousLevel) {
                        levels.add(previousLevel, 0);
                        order = levels.get(level) + 1;
                        levels.add(level, order);
                        // we are one level beneath the previous level
                        // so we set the order to a new minimum
                    } else if (level > previousLevel) {
                        levels.add(level, 0);

                    }
                    previousLevel = level;
                }
            }

            String treeRow = "objTreeMenu"; // start a javascript line to compose a tree

            for (int j = 0; j < previousLevel + 1; j++) {
                if (levels.get(j) == -1)
                    continue;
                treeRow = treeRow + ".n[" + levels.get(j) + "]";
            }

            SimpleHash branch = new SimpleHash();
            try {
                branch.put("treeRow", treeRow);
                branch.put("fileName", name.equals("_root_") ? r.getRowname() : name);
                String nicePath = !simplePath.equals(r.getRowname()) ? simplePath
                        .substring(r.getRowname().length() + 1) : "";
                branch.put("filePath", URLEncoder.encode(nicePath, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            b.add(branch);

        }

        tx.commit();
        s.close();

        return b;
    }

}
