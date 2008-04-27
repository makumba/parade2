package org.makumba.parade.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerThreadPrintStream extends java.io.PrintStream {
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
        OutputStream bos = null;
        try {
            bos = new FileOutputStream(System.getProperty("catalina.base") + java.io.File.separator + "logs"
                    + java.io.File.separator + "catalina.out");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        oldSystemOut = System.out;
        java.io.PrintStream singleton = new PerThreadPrintStream(bos);
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

    PerThreadPrintStream(OutputStream o) {
        super(o);
    }

    @Override
    public void write(byte[] buffer, int start, int len) {

        try {

            if (!(Boolean) enable.get()) {
                outWrite(buffer, start, len);
                // super.write(buffer, start, len);
                return;
            }
            String msg = new String(buffer, start, len);

            PerThreadPrintStreamLogRecord record = new PerThreadPrintStreamLogRecord();
            record.setDate(new Date());
            record.setMessage(msg);

            enable.set(false);
            TriggerFilter.redirectToServlet("/servlet/org.makumba.parade.access.DatabaseLogServlet", record);
            enable.set(true);

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
    }

    private void outWrite(byte[] buffer, int start, int len) throws IOException {
        out.write(buffer, start, len);
        out.flush();
        oldSystemOut.write(buffer, start, len);
        oldSystemOut.flush();
    }
}