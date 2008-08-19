package org.makumba.parade.aether;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.makumba.aether.RelationComputer;
import org.makumba.db.hibernate.HibernateTransactionProvider;
import org.makumba.devel.relations.RelationCrawler;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowWebapp;
import org.makumba.parade.tools.ParadeLogger;
import org.makumba.providers.TransactionProvider;

/**
 * Computes relations between files of a Makumba-enabled CVS module
 * 
 * @author Manuel Gay
 * 
 */
public class CVSModuleRelationComputer extends MakumbaContextRelationComputer implements RelationComputer {

    private Logger logger;

    public CVSModuleRelationComputer(Row r) {
        super(r);
        this.logger = ParadeLogger.getParadeLogger(CVSModuleRelationComputer.class.getName());

    }

    @Override
    protected void initRelationCrawler() {
        rc = RelationCrawler.getRelationCrawler(webappPath, ParadeRelationComputer.PARADE_DATABASE_NAME, true,
                "cvs://", r.getApplication().getName(), false);
    }

    @Override
    protected List<String> getFilesToCrawl() {
        List<String> res = new LinkedList<String>();

        // we get only the files that are not yet crawled
        TransactionProvider tp = new TransactionProvider(new HibernateTransactionProvider());

        org.makumba.Transaction t = tp.getConnectionTo(ParadeRelationComputer.PARADE_DATABASE_NAME);
        Map<String, Object> params = new HashMap<String, Object>();
        String webappRoot = r.getRowpath() + "/" + ((RowWebapp) r.getRowdata().get("webapp")).getWebappPath();
        params.put("webappRoot", webappRoot);
        params.put("webappRootLength", webappRoot.length());
        params.put("webappRootLike", webappRoot + "%");
        params.put("relationsDb", ParadeRelationComputer.PARADE_DATABASE_NAME);
        params.put("rowId", r.getId().intValue());
        Vector<Dictionary<String, Object>> v = t
                .executeQuery(
                        "SELECT f.path AS path FROM File f WHERE f.path like :webappRootLike AND (f.path like '%.mdd' OR f.path like '%.jsp' OR f.path like '%.java') AND f.isDir = false AND f.row.id = :rowId AND f.crawled = false",
                        params);

        for (Dictionary<String, Object> dictionary : v) {
            String path = (String) dictionary.get("path");
            res.add(path);
        }

        t.close();

        return res;
    }

}
