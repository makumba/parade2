package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;

import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;
import org.makumba.parade.model.managers.FileManager;
import org.makumba.parade.model.managers.WebappManager;

public class ViewManager {
	
	public String getView(Parade p, String context, String handler, String op, String entry) {
		
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		Row r = null;
		if(context != null) r = (Row) p.getRows().get(context);
		
		//operation to handle
		//TODO - move this somewhere else
		
		String opResult = "";
		
		if(handler != null && op != null) {
			
			
			if(handler.equals("webapp")) {
				WebappManager webappMgr = new WebappManager();
				
				Row entryRow = null;
				if(entry != null) entryRow = (Row) p.getRows().get(entry);
				
				if(op.equals("servletContextStart")) {
					opResult=webappMgr.servletContextStartRow(entryRow);
				}
				if(op.equals("servletContextStop")) {
					opResult=webappMgr.servletContextStopRow(entryRow);
				}
				if(op.equals("servletContextReload")) {
					opResult=webappMgr.servletContextReloadRow(entryRow);
				}
				if(op.equals("servletContextRemove")) {
					opResult=webappMgr.servletContextRemoveRow(entryRow);
				}
				if(op.equals("servletContextInstall")) {
					opResult=webappMgr.servletContextInstallRow(entryRow);
				}
			}
			if(handler.equals("file")) {
				FileManager fileMgr = new FileManager();
				
				if(op.startsWith("newFile")) {
					Row row = (Row) p.getRows().get(context);
					if(row == null) return "Unknown context "+context;
					String path = op.substring(op.indexOf('#')+1);
					opResult = fileMgr.newFile(row, path, entry);
					if(opResult.startsWith("OK")) 
						opResult= "New file "+entry+ " created. " +
								"<b><a href='edit?context="+row.getRowname()+"&path="+path+"&file="+
								opResult.substring(opResult.indexOf('#')+1)+"'>Edit</a></b>";
					try {
						opResult=URLEncoder.encode(opResult,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if(op.startsWith("newDir")) {
					Row row = (Row) p.getRows().get(context);
					if(row == null) return "Unknown context "+context;
					String path = op.substring(op.indexOf('#')+1);
					opResult = fileMgr.newDir(row, path, entry);
					if(opResult.startsWith("OK")) 
						opResult= "New directory "+entry+ " created.";
					try {
						opResult=URLEncoder.encode(opResult,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
		//we are in the browse view
		if(context != null) {
			
			out.println(
"<HTML><HEAD><TITLE>"+context+" browser</TITLE>"+
"</HEAD>"+
"<FRAMESET rows=\"30,*\">"+      
"	<FRAME name=\"header\" src=\"/servlet/header?context="+r.getRowname()+"\""+
" 	marginwidth=\"1\" marginheight=\"1\">"+
"	<FRAMESET cols=\"190,*\">"+
"		<FRAME name=\"tree\" src=\"/servlet/tree?context="+r.getRowname()+"\" marginwidth=\"0\" marginheight=\"5\">"+
"		<FRAMESET rows=\"*,20%\">"+      
"			<FRAME name=\"directory\" src=\"/servlet/file?context="+r.getRowname()+"&opResult="+opResult+"\">"+
"			<FRAME name=\"command\" src=\"/servlet/command\" marginwidth=\"1\" marginheight=\"1\">"+
"		</FRAMESET>"+
"	</FRAMESET>"+
"</FRAMESET>"+
"</HTML>"
);
		
			
		}
		
		//we are in the table view
		if(context == null || (context != null && handler != null)) {
			RowStoreViewManager rowstoreV = new RowStoreViewManager();
			CVSViewManager cvsV = new CVSViewManager();
			AntViewManager antV = new AntViewManager();
			WebappViewManager webappV = new WebappViewManager();
			MakumbaViewManager makV = new MakumbaViewManager();
			
			
			out.println(
"<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE>"+
"</HEAD><BODY><CENTER>");
			
			if(!opResult.equals("")) out.println(opResult+"<br>");
			
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
			while(i.hasNext()) {
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
	
	/** Format byte size in nice format. */
    public static String readableBytes(long byteSize) {
        if (byteSize < 0l)
            return ("invalid");
        if (byteSize < 1l)
            return ("empty");

        float byteSizeF = (new java.lang.Float(byteSize)).floatValue();
        String unit = "bytes";
        float factor = 1f;
        String[] desc = { "B", "kB", "MB", "GB", "TB" };

        java.text.DecimalFormat nf = new java.text.DecimalFormat();
        nf.setMaximumFractionDigits(1);
        nf.setGroupingUsed(true);

        String value = nf.format(byteSizeF);

        int i = 0;
        while (i + 1 < desc.length
                && (value.length() > 4 || (value.length() > 3 && value
                        .indexOf('.') < 0))) {
            i++;
            factor = factor * 1024l;
            value = nf.format(byteSizeF / factor);
        }
        if (value.charAt(0) == '0' && i > 0) { // go one back if a too-big
            // scale is used
            value = nf.format(java.lang.Math.round(1024 * byteSizeF / factor));
            i--;
        }

        if (value.length() > 3 && value.indexOf('.') > 0) // sut decimals on
            // large numbers
            value = value.substring(0, value.indexOf('.'));

        unit = desc[i];
        return (value + " " + unit);
    }

    /** lentgh of time periods in nice format. */
    public static String readableTime(long milis) {
        // simplest implementation:
        // return((new Long(secs)).toString())+" seconds";
        long secs = milis / 1000l;

        if (secs < 2l)
            return ("1 second");
        if (secs == 2l)
            return ("2 seconds");

        // default:
        long value = secs; // new Long(secs);
        String unit = "seconds";

        // now try to give it a meaning:

        long[] breaks = { 31536000, 2628000, 604800, 86400, 3600, 60, 1 };
        String[] desc = { "year", "month", "week", "day", "hour", "minute",
                "second" };

        int i = 0;
        while (i <= breaks.length && secs <= (2 * breaks[i])) {
            i++;
        }
        // i=i-1;
        // long break=breaks[i];
        value = secs / breaks[i];
        unit = desc[i];
        if (value >= 2)
            unit = unit + "s";

        String retval = value + " " + unit;

        // if...

        return (retval);
    }

}
