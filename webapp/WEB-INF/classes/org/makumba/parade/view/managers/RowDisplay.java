package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.WebappManager;

public class RowDisplay {

    public String getView(Parade p, String context, String handler, String op, String entry, String pathURI) {

        StringWriter result = new StringWriter();
        PrintWriter out = new PrintWriter(result);

        Row r = null;
        if (context != null)
            r = (Row) p.getRows().get(context);

        if (pathURI == null)
            pathURI = "";

        if (pathURI != null) {
            try {
                pathURI = URLEncoder.encode(pathURI, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // operation to handle
        // TODO - move this somewhere else

        String opResult = "";

        if (handler != null && op != null) {

            if (handler.equals("webapp")) {
                WebappManager webappMgr = new WebappManager();

                Row entryRow = null;
                if (entry != null)
                    entryRow = (Row) p.getRows().get(entry);

                if (op.equals("servletContextStart")) {
                    opResult = webappMgr.servletContextStartRow(entryRow);
                }
                if (op.equals("servletContextStop")) {
                    opResult = webappMgr.servletContextStopRow(entryRow);
                }
                if (op.equals("servletContextReload")) {
                    opResult = webappMgr.servletContextReloadRow(entryRow);
                }
                if (op.equals("servletContextRemove")) {
                    opResult = webappMgr.servletContextRemoveRow(entryRow);
                }
                if (op.equals("servletContextInstall")) {
                    opResult = webappMgr.servletContextInstallRow(entryRow);
                }
            }
            if (handler.equals("file")) {
                FileManager fileMgr = new FileManager();

                if (op.startsWith("newFile")) {

                }

                if (op.startsWith("newDir")) {
                    Row row = (Row) p.getRows().get(context);
                    if (row == null)
                        return "Unknown context " + context;
                    String path = op.substring(op.indexOf('#') + 1);
                    opResult = fileMgr.newDir(row, path, entry);
                    if (opResult.startsWith("OK"))
                        opResult = "New directory " + entry + " created.";
                    try {
                        opResult = URLEncoder.encode(opResult, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

        // we are in the table view
        if (context == null || (context != null && handler != null)) {
            RowStoreViewManager rowstoreV = new RowStoreViewManager();
            CVSViewManager cvsV = new CVSViewManager();
            AntViewManager antV = new AntViewManager();
            WebappViewManager webappV = new WebappViewManager();
            MakumbaViewManager makV = new MakumbaViewManager();

            out.println("<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE>" + "</HEAD><BODY><CENTER>");

            if (!opResult.equals(""))
                out.println(opResult + "<br>");

            out.println("<TABLE>");

            // printing headers
            out.println("<TR bgcolor=#ddddff>");

            out.println("<TD align='center'>");
            out.println(rowstoreV.getParadeViewHeader());
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(cvsV.getParadeViewHeader());
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(antV.getParadeViewHeader());
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(webappV.getParadeViewHeader());
            out.println("</TD>");
            out.println("<TD align='center'>");
            out.println(makV.getParadeViewHeader());
            out.println("</TD>");

            out.print("</TR>");

            // printing row information
            Iterator i = p.getRows().keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();

                out.println("<TR bgcolor=#f5f5ff>");

                out.println("<TD align='center'>");
                out.println(rowstoreV.getParadeView((Row) p.getRows().get(key)));
                out.println("</TD>");
                out.println("<TD align='center'>");
                out.println(cvsV.getParadeView((Row) p.getRows().get(key)));
                out.println("</TD>");
                out.println("<TD align='center'>");
                out.println(antV.getParadeView((Row) p.getRows().get(key)));
                out.println("</TD>");
                out.println("<TD align='center'>");
                out.println(webappV.getParadeView((Row) p.getRows().get(key)));
                out.println("</TD>");
                out.println("<TD align='center'>");
                out.println(makV.getParadeView((Row) p.getRows().get(key)));
                out.println("</TD>");

                out.println("</TR>");

            }

            out.println("</TABLE></CENTER></BODY></HTML>");
        }

        return result.toString();

    }
}
