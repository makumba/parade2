package org.makumba.parade.init;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.makumba.HibernateSFManager;
import org.makumba.aether.Aether;
import org.makumba.aether.AetherContext;
import org.makumba.parade.aether.MakumbaContextRelationComputer;
import org.makumba.parade.aether.ParadeRelationComputer;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.RowMakumba;

import freemarker.template.DefaultObjectWrapper;

public class InitServlet extends HttpServlet implements Runnable {

    private static final long serialVersionUID = 1L;
    
    public static Date startupDate = new Date();

    static Logger logger = Logger.getLogger(InitServlet.class.getName());

    private static Configuration cfg;

    private static SessionFactory sessionFactory = null;
    
    private static Aether aether;

    private static Map<String, MakumbaContextRelationComputer> rowComputers = new HashMap<String, MakumbaContextRelationComputer>();
    
    private static Long one = new Long(1);

    private Session session = null;

    private Parade p = null;

    private static freemarker.template.Configuration freemarkerCfg;
    
    public static boolean aetherEnabled = ParadeProperties.getParadeProperty("aether.enabled") != null && ParadeProperties.getParadeProperty("aether.enabled").equals("true");

    static {
        initSessionFactory();
    }
    
    private static void initSessionFactory() {
        /* Initializing Makumba-driven Hibernate */
        Vector<String> resources = new Vector<String>();
        
        try {
            //cfg = new Configuration().configure("localhost_mysql_parade.cfg.xml");
            resources.add("org/makumba/parade/model/AbstractFileData.hbm.xml");
            resources.add("org/makumba/parade/model/Parade.hbm.xml");
            resources.add("org/makumba/parade/model/Row.hbm.xml");
            resources.add("org/makumba/parade/model/AbstractRowData.hbm.xml");
            resources.add("org/makumba/parade/model/File.hbm.xml");
            resources.add("org/makumba/parade/model/Log.hbm.xml");
            resources.add("org/makumba/parade/model/ActionLog.hbm.xml");
            resources.add("org/makumba/parade/model/Application.hbm.xml");
            resources.add("org/makumba/parade/model/User.hbm.xml");
            
            if(aetherEnabled) {
                
                // Aether XMLs
                // In a standalone Aether those would be in initialised by an own Aether sessionFactory
                // but that seems a bit too resource-consuming, and we anyway need to query this model
                resources.add("org/makumba/aether/model/InitialPercolationRule.hbm.xml");
                resources.add("org/makumba/aether/model/PercolationRule.hbm.xml");
                resources.add("org/makumba/aether/model/PercolationStep.hbm.xml");
                resources.add("org/makumba/aether/model/RelationQuery.hbm.xml");
                resources.add("org/makumba/aether/model/MatchedAetherEvent.hbm.xml");
                resources.add("org/makumba/aether/model/Focus.hbm.xml");
                
            }
            
            HibernateSFManager.setExternalConfigurationResources(resources);
            SessionFactory sf = HibernateSFManager.getSF();

            SchemaUpdate schemaUpdate = new SchemaUpdate(HibernateSFManager.getConfiguredConfiguration());
            schemaUpdate.execute(true, true);

            // now it's ready to be available for other classes
            sessionFactory = sf;
        } catch (Throwable t) {
            logger.error(t);
            t.printStackTrace();
        }

        /* Initalising Freemarker */
        try {

            freemarkerCfg = new freemarker.template.Configuration();

            String templatesPath = new java.io.File(ParadeProperties.getClassesPath()
                    + "/org/makumba/parade/view/templates").getPath();

            freemarkerCfg.setDirectoryForTemplateLoading(new File(templatesPath));

            freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper());

        } catch (Throwable t) {
            logger.error(t);
            t.printStackTrace();
        }

    }

    @Override
    public void init(ServletConfig conf) throws ServletException {
        new Thread(this).start();
    }

    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("INIT: Starting Parade initialization at " + new java.util.Date());
        long start = System.currentTimeMillis();
        System.out.flush();

        /* Getting Parade, creating one if none found */

        session = sessionFactory.openSession();

        Transaction tx = session.beginTransaction();

        p = (Parade) session.get(Parade.class, new Long(1));
        if (p == null) {
            p = new Parade();
            p.setId(one);
            p.hardRefresh();
            session.save(p);
            p.performPostRefreshOperations();
            session.save(p);            
        }
        try {
            p.refreshApplicationsCache();
            p.softRefresh();
            p.addJNotifyListeners();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Hibernate.initialize(p.getRows());
        
        if(aetherEnabled) {
            
            AetherContext ctx = new AetherContext(ParadeRelationComputer.PARADE_DATABASE_NAME, getSessionFactory());
            
            // parade relation computer, generating user to row relations
            ctx.addRelationComputer(new ParadeRelationComputer());
            
            
            for(Row r : p.getRows().values()) {
                
                if(r.getRowname().equals("(root)")) {
                    continue;
                }

                if(((RowMakumba)r.getRowdata().get("makumba")).getHasMakumba() && !r.getModuleRow()) {
                    MakumbaContextRelationComputer c = new MakumbaContextRelationComputer(r); 
                    ctx.addRelationComputer(c);
                    rowComputers.put(r.getRowpath(), c);
                }
            }
            aether = Aether.getAether(ctx);
        }
        
        tx.commit();

        session.close();


        logger.info("INIT: Launching ParaDe finished at " + new java.util.Date());
        long end = System.currentTimeMillis();

        long refresh = end - start;
        logger.info("INIT: Initialisation took " + refresh + " ms");

    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static freemarker.template.Configuration getFreemarkerCfg() {
        return freemarkerCfg;
    }
    
    public static MakumbaContextRelationComputer getContextRelationComputer(String rowPath) {
        return rowComputers.get(rowPath);
    }
    
    public static Aether getAether() {
        return aether;
    }
    

}