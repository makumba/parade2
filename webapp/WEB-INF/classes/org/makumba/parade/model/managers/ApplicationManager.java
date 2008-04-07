package org.makumba.parade.model.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.makumba.parade.model.Application;
import org.makumba.parade.init.ApplicationProperties;
import org.makumba.parade.init.ParadeProperties;
import org.makumba.parade.model.Parade;
import org.makumba.parade.model.interfaces.ParadeRefresher;
import org.makumba.parade.tools.Execute;

/**
 * Manages the applications ParaDe should know about, by building them out of the definitions of the
 * "application.properties" file and looking up all the files in their CVS repository
 * 
 * @author Manuel Gay
 * 
 */
public class ApplicationManager implements ParadeRefresher {
    
    private static Logger logger = Logger.getLogger(ApplicationManager.class);

    public void paradeRefresh(Parade p) {

        Map<String, Application> applications = new HashMap<String, Application>();

        ApplicationProperties appsProperties = new ApplicationProperties();
        Map<String, Map<String, String>> applicationDefinitions = appsProperties.getApplicationDefinitions();

        Iterator<String> i = applicationDefinitions.keySet().iterator();
        while (i.hasNext()) {
            String appName = i.next();
            Map<String, String> applicationDefinition = applicationDefinitions.get(appName);
            
            // first we do some checks
            if(applicationDefinition.get("repository").trim().length() == 0) {
                logger.error("No repository defined for application "+appName+". Please fix this so that the application is available");
                continue;
            }
            if(applicationDefinition.get("module").trim().length() == 0) {
                logger.error("No CVS module defined for application "+appName+". Please fix this so that the application is available");
                continue;
            }
            
            Application a = new Application(appName, applicationDefinition.get("repository"), applicationDefinition
                    .get("desc"), applicationDefinition.get("module"));
            applications.put(appName, a);
        }

        readApplicationCVS(applications);

        p.setApplications(applications);

    }

    // cvs -d:pserver:manu@cvs.best.eu.org:/usr/local/cvsroot rls karamba -R
    // -e if needed
    /*
     * 
     * karamba/public_html/www/images/tarvi: /footer.gif/1.1/Tue Oct 26 12:34:54 2004/-kb/ /footer.jpg/1.1/Tue Oct 26
     * 12:34:54 2004/-kb/ /header.gif/1.1/Tue Oct 26 12:34:54 2004/-kb/ /header.jpg/1.1/Tue Oct 26 12:34:54 2004/-kb/
     * /middle.gif/1.1/Tue Oct 26 12:34:54 2004/-kb/ /middle.jpg/1.1/Tue Oct 26 12:34:54 2004/-kb/
     * 
     * karamba/public_html/www/localWeb: /eventList.jsp/1.1/Tue Feb 6 10:24:48 2007// /eventListJS.jsp/1.1/Tue Feb 6
     * 10:24:55 2007// /index.jsp/1.1/Tue Feb 6 10:25:00 2007// /lbgChooser.jsp/1.2/Tue May 22 13:38:45 2007//
     * 
     * 
     */

    private void readApplicationCVS(Map<String, Application> applications) {
        
        Iterator<String> i = applications.keySet().iterator();
        
        while(i.hasNext()) {
            String name = i.next();
            
            // we skip parade
            if(name.equals("parade")) {
                continue;
            }
            
            Application a = applications.get(name);
            
            // now we are going to build the application's cvsfile list
            buildCVSlist(a);
            
            
        }
    }

    private void buildCVSlist(Application a) {
        
        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);
        
        
        // let's get the list from the CVS repository
        Vector<String> cmd = new Vector<String>();
        cmd.add("cvs");
        cmd.add("-d:"+a.getRepository());
        cmd.add("rls"); //listing from the root
        cmd.add(a.getModule());
        cmd.add("-R"); //recursive
        cmd.add("-e"); //parseable output
        
        Execute.exec(cmd, new java.io.File(ParadeProperties.getParadeBase()), out);
        

        // first let's see if everything went fine
        if(result.toString().indexOf("exit value: 1") > -1) {
            logger.error("Could not retrieve CVS list for application "+a.getName()+". Result of the operation was:\n"+result.toString());
        } else {
            
            Map<String, String> cvsfiles = new HashMap<String, String>();
            
            
            BufferedReader br = new BufferedReader(new StringReader(result.toString()));
            
            try {
                String line = "", currentDir="", fileName="", version="";
                while((line = br.readLine()) != null) {
                    
                    // this is a new directory content listing
                    if(line.endsWith(":")) {
                        currentDir = line.substring(0, line.indexOf(":"));
                        continue;
                    }
                    
                    // this is a file entry
                    if(line.startsWith("/")) {
                        line = line.substring(1);
                        fileName = line.substring(0, line.indexOf("/"));
                        line = line.substring(line.indexOf("/")+1);
                        version = line.substring(0, line.indexOf("/"));
                        cvsfiles.put(currentDir + "/" + fileName, version);
                        continue;
                    }
                    
                    // this is a directory entry, we ignore it
                    if(line.startsWith("D")) {
                        continue;
                    }
                    
                    // empty line
                    if(line.equals("")) {
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
    
}
