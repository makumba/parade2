package org.makumba.parade.aether;

import java.security.Timestamp;
import java.sql.Time;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.aether.Aether;
import org.makumba.aether.RelationComputationException;
import org.makumba.aether.RelationComputer;
import org.makumba.commons.NamedResources;
import org.makumba.devel.relations.RelationCrawler;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Row;
import org.makumba.providers.TransactionProvider;
import org.makumba.providers.datadefinition.makumba.RecordInfo;

/**
 * Computes relations amongst files for a context using the Makumba framework
 * 
 * @author Manuel Gay
 * 
 */
public class MakumbaContextRelationComputer implements RelationComputer {

    private static Logger logger;

    protected Row r;

    protected RelationCrawler rc;

    protected String webappPath;

    public MakumbaContextRelationComputer(Row r) {
        this.r = r;
        this.webappPath = r.getRowpath() + java.io.File.separator + r.getWebappPath();
        initRelationCrawler();
        logger = Aether.getAetherLogger(MakumbaContextRelationComputer.class.getName());

    }

    protected void initRelationCrawler() {
        this.rc = RelationCrawler.getRelationCrawler(this.webappPath, ParadeRelationComputer.PARADE_DATABASE_NAME,
                true, "file://", r.getRowname(), false);
    }

    public String getName() {
        return "MakumbaContextRelationComputer for row " + r.getRowname();
    }

    public void computeRelations(boolean updateExistingRelations) throws RelationComputationException {
        
        long start = new Date().getTime();
        logger.info("Starting crawling of row " + r.getRowname());

        // let's compute all relations using the Makumba relations crawler
        // while we crawl, we adjust the MDD provider root to the webapp root
        RecordInfo.setWebappRoot(webappPath);

        List<String> filesToCrawl = getFilesToCrawl();

        for (String path : filesToCrawl) {
            logger.fine("Crawling file " + path);
            rc.crawl(path.substring(webappPath.length()));
        }

        // we set it back to null after the crawling and clean the cache
        RecordInfo.setWebappRoot(null);
        NamedResources.cleanStaticCache(RecordInfo.infos);

        rc.writeRelationsToDb(updateExistingRelations);
        //+++
        setLastCrawledDate(filesToCrawl);
        //---
        long end = new Date().getTime();
        logger.info("Crawling of row " + r.getRowname() + " took " + (end - start) + " ms");
        
    }
    
    //+++
    private void setLastCrawledDate(String file)
    {
        List<String> filesToCrawl = new LinkedList<String>();
        filesToCrawl.add(file);
        setLastCrawledDate(filesToCrawl);
        
    }
    
    private void setLastCrawledDate(List<String> filesToCrawl)
    {
        if (filesToCrawl.size() != 0) {
            Session s = null;
            try {
                s = InitServlet.getSessionFactory().openSession();
                Transaction tx = s.beginTransaction();
                
                Query q = s.createQuery("update File set crawled = " + new Long(new Date().getTime()) + " where path in ("
                        + fileListToHQLSet(filesToCrawl) + ")");
                
                q.executeUpdate();
                tx.commit();

            } finally {
                if (s != null) {
                    s.close();
                }
            }
        }
    }
    //---
    
    private String fileListToHQLSet(List<String> files) {
        String res = "";
        Iterator<String> it = files.iterator();
        while (it.hasNext()) {
            String s = it.next();
            res += "'" + s + "'";
            if (it.hasNext()) {
                res += ", ";
            }
        }
        return res;
    }

    public void updateRelation(String filePath) throws RelationComputationException {

        if (filePath.startsWith(webappPath)) {
            String file = filePath; //+++
            
            logger.fine("Updating relations of " + filePath);
            filePath = filePath.substring(webappPath.length());
            if (!filePath.startsWith("/"))
                filePath = "/" + filePath;
            RecordInfo.setWebappRoot(webappPath);
            rc.crawl(filePath);
            RecordInfo.setWebappRoot(null);
            NamedResources.cleanStaticCache(RecordInfo.infos);
            rc.writeRelationsToDb(true);
            //+++
            setLastCrawledDate(file);
            //---
        }
    }

    // TODO refactor to use hibernate api
    protected List<String> getFilesToCrawl() {
        List<String> res = new LinkedList<String>();

        // we get only the files that are not UP_TO_DATE
        // and that were not previously crawled
        TransactionProvider tp = TransactionProvider.getInstance();

        org.makumba.Transaction t = tp.getConnectionTo(ParadeRelationComputer.PARADE_DATABASE_NAME);
        Map<String, Object> params = new HashMap<String, Object>();
        String webappRoot = r.getRowpath() + "/" + r.getWebappPath();
        params.put("webappRoot", webappRoot);
        params.put("webappRootLength", webappRoot.length());
        params.put("webappRootLike", webappRoot + "%");
        params.put("relationsDb", ParadeRelationComputer.PARADE_DATABASE_NAME);
        params.put("rowId", r.getId().intValue());
        //+++
        Vector<Dictionary<String, Object>> v = t
                .executeQuery(
                        "SELECT f.path AS path FROM File f WHERE f.path like :webappRootLike AND (f.path like '%.mdd' OR f.path like '%.jsp' OR f.path like '%.java') AND f.isDir = false AND f.row.id = :rowId  AND f.crawled < f.date",
                        params);
        //---
        
        for (Dictionary<String, Object> dictionary : v) {
            String path = (String) dictionary.get("path");
            res.add(path);
        }

        t.close();

        return res;
    }

    public void deleteRelation(String filePath) throws RelationComputationException {
        if (filePath.startsWith(webappPath)) {
            logger.fine("Deleting relations of " + filePath);
            filePath = filePath.substring(webappPath.length());
            if (filePath.startsWith("/"))
                filePath = filePath.substring(1);
            rc.deleteFileRelations(filePath);
        }
    }

    public static void resetCrawlStatus(Row r, Session s) {
        //+++
        //Query q = s.createQuery("UPDATE File f set f.crawled = false where f.row.id = :rowid");
        Query q = s.createQuery("UPDATE File f set f.crawled = 0 where f.row.id = :rowid");
        //---
        q.setParameter("rowid", r.getId());
        int u = q.executeUpdate();
        logger.fine("Reset the crawl status of " + u + " files");
    }

}
