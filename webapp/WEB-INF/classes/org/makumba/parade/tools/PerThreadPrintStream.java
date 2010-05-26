package org.makumba.parade.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PerThreadPrintStream extends java.io.PrintStream {
    
    public PerThreadPrintStream(OutputStream o) {
        super(o);
    }

    private static Logger logger = ParadeLogger.getParadeLogger(PerThreadPrintStream.class.getName());
    
    public static DateFormat logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static Object dummy = "dummy";

    public static PrintStream oldSystemOut;

    static ThreadLocal<Boolean> enable = new ThreadLocal<Boolean>() {
        @Override
        public Boolean initialValue() {
            return true;
        }
    };

    static ThreadLocal lastEnter = new ThreadLocal() {
        @Override
        public Object initialValue() {
            return dummy;
        }
    };
    static {
        oldSystemOut = System.out;
        java.io.PrintStream singleton = new PerThreadPrintStream(System.out);
        System.setOut(singleton);
        System.setErr(singleton);
    }

    public static void setEnabled(boolean b) {
        enable.set(b);
    }

    void debug(String s) {
        byte b[] = ("\n\n\t" + s + "\n\n").getBytes();
        super.write(b, 0, b.length);
        super.flush();
    }

    @Override
    public void write(byte[] buffer, int start, int len) {

        // when enabled, we issue a log (so all the uncaught System.out.println's get to DB and File as well)
        // when disabled, we just do System.out
        try {

            if (!(Boolean) enable.get()) {
                outWrite(buffer, start, len);
                // super.write(buffer, start, len);
                return;
            }
            
            String msg = new String(buffer, start, len);

            // we issue a log so the catched text goes to the console, the db and the file
            LogRecord rec = new LogRecord(Level.INFO, msg);
            Object[] params = { LogHandler.prefix.get() };
            
            logger.getHandlers();
            
            rec.setParameters(params);

            enable.set(false);
            logger.log(rec);
            enable.set(true);
            
        } catch (Throwable e) {
            PrintWriter p = new PrintWriter(new OutputStreamWriter(out));
            e.printStackTrace(p);
            p.flush();
        }

            /*
            
            String s = TriggerFilter.prefix.get();
            byte[] b = null;
            if (s != null)
                b = (s + ": ").getBytes();
            else
                // if s is null, it means that this didn't happen through some external access to the webapp
                b = ("[Thread]  " + logDate.format(new Date()) + ": ").getBytes();
            int lastStart = start;
            if (b != null) {
                for (int i = start; i < start + len; i++) {
                    if (buffer[i] == '\n') // FIXME, use general line separator
                    // here.
                    {
                        if (lastEnter.get() != null) {
                            outWrite(b, 0, b.length);
                        }

                        else
                            lastEnter.set(dummy);
                        outWrite(buffer, lastStart, i - lastStart + 1);
                        // w.write(buffer, lastStart, i - lastStart + 1);
                        lastStart = i + 1;
                    }
                }
                if (lastStart < start + len) {
                    if (lastEnter.get() != null) {
                        outWrite(b, 0, b.length);
                    }

                    lastEnter.set(null);
                    outWrite(buffer, lastStart, start + len - lastStart);
                    // w.write(buffer, lastStart, start + len - lastStart);
                }
            } else {
                outWrite(buffer, start, len);
                // w.write(buffer, start, len);
            }
        } catch (Throwable e) {
            PrintWriter p = new PrintWriter(new OutputStreamWriter(out));
            e.printStackTrace(p);
            p.flush();
        }
        
        */
    }

    private void outWrite(byte[] buffer, int start, int len) throws IOException {
        out.write(buffer, start, len);
        out.flush();
        
    }
    
}