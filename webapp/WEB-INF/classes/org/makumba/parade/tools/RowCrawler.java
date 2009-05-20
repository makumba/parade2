package org.makumba.parade.tools;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.makumba.aether.Aether;
import org.makumba.aether.AetherContext;
import org.makumba.parade.aether.MakumbaContextRelationComputer;
import org.makumba.parade.aether.ParadeRelationComputer;
import org.makumba.parade.init.InitServlet;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;

/**
 * Standalone class that crawls all the rows loaded in parade
 * @author Manuel Gay
 *
 */
public class RowCrawler {
    
    public static void main(String[] args) {

        Session s = InitServlet.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        
        Parade p = (Parade) s.get(Parade.class, new Long(1));
        
        // building an AetherContext to get Aether
        AetherContext ctx = new AetherContext(ParadeRelationComputer.PARADE_DATABASE_NAME, InitServlet.getSessionFactory());

        ctx.addRelationComputer(new ParadeRelationComputer());

        for (Row r : p.getRows().values()) {

            if (r.getRowname().equals("(root)")) {
                continue;
            }

            if (r.getHasMakumba() && !r.getModuleRow()) {
                MakumbaContextRelationComputer c = new MakumbaContextRelationComputer(r);
                ctx.addRelationComputer(c);

                // reset previous crawl status
                MakumbaContextRelationComputer.resetCrawlStatus(r, s);
            }
        }
        
        ParadeLogger.getParadeLogger("RowCrawler").info("INIT: Starting to crawl relations at " + new java.util.Date());
        long start = System.currentTimeMillis();
        System.out.flush();

        Aether a = Aether.getAether(ctx);
        a.computeAllRelations(false);
        ParadeLogger.getParadeLogger("RowCrawler").info("INIT: Finished crawling relations at " + new java.util.Date());
        long end = System.currentTimeMillis();

        long refresh = end - start;
        ParadeLogger.getParadeLogger("RowCrawler").info("INIT: Crawling took " + refresh + " ms");
        
        tx.commit();
        s.close();
        
    }


}
