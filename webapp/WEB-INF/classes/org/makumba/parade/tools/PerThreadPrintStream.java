package org.makumba.parade.tools;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerThreadPrintStream extends java.io.PrintStream {
    public static DateFormat logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static Object dummy = "dummy";

    static ThreadLocal prefix = new ThreadLocal();

    static ThreadLocal enable = new ThreadLocal() {
        public Object initialValue() {
            return true;
        }
    };
    
    static ThreadLocal lastEnter = new ThreadLocal() {
        public Object initialValue() {
            return dummy;
        }
    };
    static {
        java.io.PrintStream singleton = new PerThreadPrintStream(System.out);
        System.setOut(singleton);
        System.setErr(singleton);
    }

    public static void set(String o) {
        prefix.set(o);
    }

    public static String get() {
        return (String) prefix.get();
    }
    
    public static void setEnabled(boolean b) {
        enable.set(b);
    }

    void debug(String s) {
        byte b[] = ("\n\n\t" + s + "\n\n").getBytes();
        super.write(b, 0, b.length);
        super.flush();
    }

    PerThreadPrintStream(OutputStream o) {
        super(o);
    }

    public void write(byte[] buffer, int start, int len){
            try {
              

        if(!(Boolean)enable.get()) {
            super.write(buffer, start, len);
            return;
        }
        String msg = new String(buffer, start, len);

        PerThreadPrintStreamLogRecord record = new PerThreadPrintStreamLogRecord();
        record.setDate(new Date());
        record.setMessage(msg);
        record.setNotThroughAccess(get() == null);
        
        enable.set(false);
        TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", record);
        enable.set(true);
        
        //PrintStream w = new PrintStream(bao);
        
        String s = get();
        byte[] b = null;
        if (s != null)
            b = (s + ": ").getBytes();
        else //if s is null, it means that this didn't happen through some external access to the webapp
            b = ("[Thread]  "+logDate.format(new Date()) + ": ").getBytes();
        int lastStart = start;
        if (b != null) {
            for (int i = start; i < start + len; i++) {
                if (buffer[i] == '\n') // FIXME, use general line separator
                // here.
                {
                    if (lastEnter.get() != null) {
                        out.write(b, 0, b.length);
                    }
                        
                    else
                        lastEnter.set(dummy);
                    out.write(buffer, lastStart, i - lastStart + 1);
                    //w.write(buffer, lastStart, i - lastStart + 1);
                    lastStart = i + 1;
                }
            }
            if (lastStart < start + len) {
                if (lastEnter.get() != null) {
                    out.write(b, 0, b.length);
                }
                    
                
                lastEnter.set(null);
                out.write(buffer, lastStart, start + len - lastStart);
                //w.write(buffer, lastStart, start + len - lastStart);
            }
        } else {
            out.write(buffer, start, len);
            //w.write(buffer, start, len);
        }
            } catch (Throwable e) {
                PrintWriter p = new PrintWriter(new OutputStreamWriter(out));
                e.printStackTrace(p);
                p.flush();
            }
    }
}