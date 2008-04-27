package org.makumba.parade.model.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.model.Application;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.interfaces.ParadeManager;
import org.makumba.parade.tools.Execute;

/**
 * Manages the applications ParaDe should know about, by building them out of the definitions of the
 * "application.properties" file and looking up all the files in their CVS repository
 * 
 * @author Manuel Gay
 * 
 */
public class ApplicationManager implements ParadeManager {

    private static Logger logger = Logger.getLogger(ApplicationManager.class);

    private void buildCVSlist(Application a) {

        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        // let's get the list from the CVS repository
        Vector<String> cmd = new Vector<String>();
        cmd.add("cvs");
        cmd.add("-d" + a.getRepository());
        cmd.add("rls"); // listing from the root
        cmd.add(a.getName());
        cmd.add("-R"); // recursive
        cmd.add("-e"); // parseable output

        Execute.exec(cmd, new java.io.File(ParadeProperties.getParadeBase()), out);

        // first let's see if everything went fine
        if (result.toString().indexOf("exit value: 1") > -1) {
            logger.error("Could not retrieve CVS list for application " + a.getName()
                    + ". Result of the operation was:\n" + result.toString());
        } else {

            Map<String, String> cvsfiles = new HashMap<String, String>();

            BufferedReader br = new BufferedReader(new StringReader(result.toString()));

            try {
                String line = "", currentDir = "", fileName = "", version = "";
                while ((line = br.readLine()) != null) {

                    // this is a new directory content listing
                    if (line.endsWith(":")) {
                        currentDir = line.substring(0, line.indexOf(":"));
                        continue;
                    }

                    // this is a file entry
                    if (line.startsWith("/")) {
                        line = line.substring(1);
                        fileName = line.substring(0, line.indexOf("/"));
                        line = line.substring(line.indexOf("/") + 1);
                        version = line.substring(0, line.indexOf("/"));
                        cvsfiles.put(currentDir + "/" + fileName, version);
                        continue;
                    }

                    // this is a directory entry, we ignore it
                    if (line.startsWith("D")) {
                        continue;
                    }

                    // empty line
                    if (line.equals("")) {
                        continue;
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            a.setCvsfiles(cvsfiles);

        }

    }

    private String getTestString() {
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        out.println("karamba/public_html/website/student:");
        out.println("/getPhoto.jsp/1.3/Thu Oct  4 20:48:24 2007//");
        out.println("/helpdesk.jsp/1.1/Mon Nov 19 21:53:43 2007//");
        out.println("/news.jsp/1.3/Mon Mar 24 11:18:26 2008//");
        out.println("/welcome.jsp/1.10/Sun Jan 13 20:53:00 2008//");
        out.println("D/career////");
        out.println("D/courses////");
        out.println("D/education////");
        out.println("D/private////");
        out.println("D/register////");
        out.println("");
        out.println("karamba/public_html/website/student/career:");
        out.println("/careerEventList.jsp/1.12/Tue Nov 20 21:41:11 2007//");
        out.println("/careerEventView.jsp/1.11/Tue Nov 20 21:41:37 2007//");
        out.println("/companyOfferList.jsp/1.15/Sun Mar  2 14:52:41 2008//");
        out.println("/companyOfferView.jsp/1.13/Mon Jan 21 22:41:50 2008//");
        out.println("/companyProfileList.jsp/2.40/Sat Mar  8 20:39:41 2008//");
        out.println("/companyProfileView.jsp/2.22/Wed Dec 26 17:42:41 2007//");
        out.println("/cvTips.jsp/2.16/Fri Oct 12 08:06:05 2007//");
        out.println("/faq.jsp/2.9/Thu Oct 11 21:29:08 2007//");
        out.println("/guide.jsp/2.12/Thu Oct  4 14:06:04 2007//");
        out.println("/index.jsp/1.23/Sat Mar  8 20:39:57 2008//");
        out.println("/interviewTips.jsp/2.19/Fri Oct 12 08:34:40 2007//");
        out.println("/specialProgrammeList.jsp/1.11/Sun Mar  2 14:24:58 2008//");
        out.println("/viewOffer.jsp/1.2/Thu Mar  6 17:58:52 2008//");
        out.println("D/careerEvents////");
        out.println("D/companyOffers////");
        out.println("D/companyProfiles////");
        out.println("");
        out.println("karamba/public_html/website/student/career/careerEvents:");
        out.println("/index.jsp/1.1/Tue Nov 20 21:44:51 2007//");
        out.println("");
        out.println("karamba/public_html/website/student/career/companyOffers:");
        out.println("/index.jsp/1.1/Tue Nov 20 21:48:49 2007//");
        out.println("");
        out.println("karamba/public_html/website/student/career/companyProfiles:");
        out.println("/index.jsp/1.1/Tue Nov 20 21:47:03 2007//");

        return result.toString();
    }

    public void newRow(String name, Row r, Map<String, String> m) {

        // let's fetch the CVS module of this row
        String module = CVSManager.getCVSModule(r.getRowpath());

        if (module == null) {
            logger.warn("No module for row " + r.getRowname() + ". This means no application is set for it.");
        } else if (module.indexOf("parade") == -1) {
            Application a = r.getParade().getApplications().get(module);
            if (a == null) {
                logger.info("Registering new application " + module + " used by row " + r.getRowname());
                a = new Application(module, CVSManager.getCVSRepository(r.getRowpath()));
                buildCVSlist(a);
                a.setParade(r.getParade());
                r.getParade().getApplications().put(a.getName(), a);
            }
            r.setApplication(a);
        }
    }

}
