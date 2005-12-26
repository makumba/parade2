package org.makumba.parade.view.managers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import org.makumba.parade.model.Parade;
import org.makumba.parade.model.Row;

public class ViewManager {
	
	public String getView(Parade p, String context) {
		
		StringWriter result = new StringWriter();
		PrintWriter out = new PrintWriter(result);
		
		//we are in the browse view
		if(context != null) {
			
			Row r = (Row) p.getRows().get(context);
			
			/*
			//we print the tree
			if(tree != null) {
				FileViewManager fileV = new FileViewManager(req);
				out.println(fileV.getTreeView(r));
			
			//we are in the file view - a test view, as files will be printed alltogether
			//for some strange reason the key of a directory spins, it returns some random file in the dir!
			} else if(file != null) {
				String fileKey = null;
				try {
					fileKey = URLDecoder.decode(file,"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(fileKey != null) {
					File f = (File) r.getFiles().get(fileKey);
					FileViewManager fileV = new FileViewManager(req);
					CVSViewManager cvsV = new CVSViewManager(req);
					
					out.println("<HTML><HEAD><TITLE>"+fileKey+" viewer</TITLE>"+
					"</HEAD><BODY><CENTER>");
					
					out.println("<table>" +
								"<tr bgcolor=#ddddff><td align='center'>"+fileV.getFileViewHeader()+"</td><td align='center'>"+cvsV.getFileViewHeader()+"</td></tr>"+
								
								"<tr bgcolor=#f5f5ff><td>"+fileV.getFileView(r,f)+"</td><td>"+cvsV.getFileView(r,f)+"</td></tr>"+
								
								"</table>");
					
					out.println("</TABLE></CENTER></BODY></HTML>");
				}
				
			
			//general browse view
			} else {
			*/
			
				out.println("<HTML><HEAD><TITLE>"+context+" browser</TITLE>"+
				"</HEAD>");
	
				out.println("<FRAMESET rows=\"30,*\">"+      
						"<FRAME name=\"header\" src=\"/servlet/header?context="+r.getRowname()+"\""+
						" marginwidth=\"1\" marginheight=\"1\">"+
						"<FRAMESET cols=\"190,*\">"+
							"<FRAME name=\"tree\" src=\"/servlet/tree?context="+r.getRowname()+"\" marginwidth=\"0\" marginheight=\"5\">"+
							"<FRAMESET rows=\"*,20%\">"+      
								"<FRAME name=\"directory\" src=\"/servlet/file?context="+r.getRowname()+"\">"+
								"<FRAME name=\"command\" src=\"/servlet/command\" marginwidth=\"1\" marginheight=\"1\">"+
							"</FRAMESET>"+
						"</FRAMESET>"+
					"</FRAMESET>");
				
				out.println("</HTML>");
			
			
		}
		
		//we are in the table view
		if(context == null) {
			RowStoreViewManager rowstoreV = new RowStoreViewManager();
			CVSViewManager cvsV = new CVSViewManager();
			AntViewManager antV = new AntViewManager();
			MakumbaViewManager makV = new MakumbaViewManager();
			
			
			out.println("<HTML><HEAD><TITLE>Welcome to ParaDe</TITLE>"+
						"</HEAD><BODY><CENTER>"+
						"<TABLE>");
			
			// printing headers
			out.print("<TR bgcolor=#ddddff>");
			
			out.print("<TD align='center'>");
			out.println(rowstoreV.getParadeViewHeader());
			out.print("</TD>");
			out.print("<TD align='center'>");
			out.println(cvsV.getParadeViewHeader());
			out.print("</TD>");
			out.print("<TD align='center'>");
			out.println(antV.getParadeViewHeader());
			out.print("</TD>");
			out.print("<TD align='center'>");
			out.println(makV.getParadeViewHeader());
			out.print("</TD>");
			
			
			out.print("</TR>");
			
			// printing row information
			Iterator i = p.getRows().keySet().iterator();
			while(i.hasNext()) {
				String key = (String) i.next();
				
				out.print("<TR bgcolor=#f5f5ff>");
				
				out.print("<TD align='center'>");
				out.println(rowstoreV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				out.print("<TD align='center'>");
				out.println(cvsV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				out.print("<TD align='center'>");
				out.println(antV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				out.print("<TD align='center'>");
				out.println(makV.getParadeView((Row) p.getRows().get(key)));
				out.print("</TD>");
				
				
				out.print("</TR>");

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