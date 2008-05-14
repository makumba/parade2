package org.makumba.parade.aether;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.makumba.aether.RelationComputationException;
import org.makumba.aether.RelationComputer;
import org.makumba.commons.NamedResources;
import org.makumba.db.hibernate.HibernateTransactionProvider;
import org.makumba.devel.relations.RelationCrawler;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.providers.TransactionProvider;
import org.makumba.providers.datadefinition.makumba.RecordInfo;

/**
 * Computes relations amongst files for a context using the Makumba framework
 * 
 * @author Manuel Gay
 * 
 */
public class MakumbaContextRelationComputer implements RelationComputer {

    private Row r;

    private RelationCrawler rc;

    private String webappPath;

    public String getName() {
        return "MakumbaContextRelationComputer for row " + r.getRowname();
    }

    public void computeRelations() throws RelationComputationException {


        // let's compute all relations using the Makumba relations crawler
        // while we crawl, we adjust the MDD provider root to the webapp root
        RecordInfo.setWebappRoot(webappPath);
        
        for (String path : getNotCrawledFiles()) {
            System.out.println("now crawling "+path.substring(webappPath.length()));
            rc.crawl(path.substring(webappPath.length()));
        }
        
        // we set it back to null after the crawling and clean the cache
        RecordInfo.setWebappRoot(null);
        NamedResources.cleanStaticCache(RecordInfo.infos);

        rc.writeRelationsToDb();
    }

    public void updateRelation(String filePath) throws RelationComputationException {
        
        if(filePath.startsWith(webappPath)) {
            filePath = filePath.substring(r.getRowpath().length());
            if(filePath.startsWith("/"))
                filePath = filePath.substring(1);
            RecordInfo.setWebappRoot(webappPath);
            rc.crawl(filePath);
            RecordInfo.setWebappRoot(null);
            NamedResources.cleanStaticCache(RecordInfo.infos);
            rc.writeRelationsToDb();
        }

    }

    public MakumbaContextRelationComputer(Row r) {
        this.r = r;
        this.webappPath = r.getRowpath() + java.io.File.separator
                + ((RowWebapp) r.getRowdata().get("webapp")).getWebappPath();
        this.rc = RelationCrawler.getRelationCrawler(this.webappPath,
                ParadeRelationComputer.PARADE_DATABASE_NAME, true, "file:/", r.getRowname());

    }

    protected List<String> getNotCrawledFiles() {
        List<String> res = new LinkedList<String>();

        // we get only the files that are not UP_TO_DATE
        // and that were not previously crawled
        TransactionProvider tp = new TransactionProvider(new HibernateTransactionProvider());

        org.makumba.Transaction t = tp.getConnectionTo(ParadeRelationComputer.PARADE_DATABASE_NAME);
        Object[] args = new Object[] { r.getRowpath(), ParadeRelationComputer.PARADE_DATABASE_NAME, r.getId() };
        Map<String, Object> params = new HashMap<String, Object>();
        String webappRoot = r.getRowpath() + "/" + ((RowWebapp)r.getRowdata().get("webapp")).getWebappPath();
        params.put("webappRoot", webappRoot);
        params.put("webappRootLength", webappRoot.length());
        params.put("webappRootLike", webappRoot + "%");
        params.put("relationsDb", ParadeRelationComputer.PARADE_DATABASE_NAME);
        params.put("rowId", r.getId().intValue());
        Vector<Dictionary<String, Object>> v = t
                .executeQuery(
                        "SELECT f.path AS path FROM File f WHERE (f.path like :webappRootLike AND f.isDir = false AND f.cvsStatus != 100 AND f.row.id = :rowId) AND f.path NOT IN (SELECT concat(:webappRoot, r.fromFile) FROM org.makumba.devel.relations.Relation r JOIN r.webapp w WHERE w.relationDatabase = :relationsDb AND w.webappRoot = :webappRoot)",
                        params);

        for (Dictionary<String, Object> dictionary : v) {
            res.add((String) dictionary.get("path"));
        }

        t.close();

        return res;
    }

    public void deleteRelation(String filePath) throws RelationComputationException {
        if(filePath.startsWith(webappPath)) {
            filePath = filePath.substring(webappPath.length());
            if(filePath.startsWith("/"))
                filePath = filePath.substring(1);
            rc.deleteFileRelations(filePath);
        }
    }
    
}
