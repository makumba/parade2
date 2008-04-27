package org.makumba.parade.init;

import java.io.File;

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
import org.makumba.parade.model.Parade;

import freemarker.template.DefaultObjectWrapper;

public class InitServlet extends HttpServlet implements Runnable {

    private static final long serialVersionUID = 1L;

    static Logger logger = Logger.getLogger(InitServlet.class.getName());

    private static Configuration cfg;

    private static SessionFactory sessionFactory = null;

    private static Long one = new Long(1);

    private Session session = null;

    private Parade p = null;

    private static freemarker.template.Configuration freemarkerCfg;

    {
        /* Initializing Hibernate */
        try {
            cfg = new Configuration().configure("localhost_mysql_parade.cfg.xml");
            cfg.addResource("org/makumba/parade/model/AbstractFileData.hbm.xml");
            cfg.addResource("org/makumba/parade/model/Parade.hbm.xml");
            cfg.addResource("org/makumba/parade/model/Row.hbm.xml");
            cfg.addResource("org/makumba/parade/model/AbstractRowData.hbm.xml");
            cfg.addResource("org/makumba/parade/model/File.hbm.xml");
            cfg.addResource("org/makumba/parade/model/Log.hbm.xml");
            cfg.addResource("org/makumba/parade/model/ActionLog.hbm.xml");
            cfg.addResource("org/makumba/parade/model/Application.hbm.xml");
            cfg.addResource("org/makumba/parade/model/User.hbm.xml");

            SessionFactory sf = cfg.buildSessionFactory();

            SchemaUpdate schemaUpdate = new SchemaUpdate(cfg);
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
            p.refresh();
            session.save(p);
        }
        try {
            p.addJNotifyListeners();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        Hibernate.initialize(p.getRows());

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

}