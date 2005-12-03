package org.makumba.parade.tools;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerThreadPrintStream extends java.io.PrintStream {
    static DateFormat logDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static Object dummy = "dummy";

    static ThreadLocal prefix = new ThreadLocal();

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

    void debug(String s) {
        byte b[] = ("\n\n\t" + s + "\n\n").getBytes();
        super.write(b, 0, b.length);
        super.flush();
    }

    PerThreadPrintStream(OutputStream o) {
        super(o);
    }

    public void write(byte[] buffer, int start, int len) {
        String s = get();
        byte[] b = null;
        if (s != null)
            b = (s + ": ").getBytes();
        else
            b = (logDate.format(new Date()) + ": ").getBytes();
        int lastStart = start;
        if (b != null) {
            for (int i = start; i < start + len; i++) {
                if (buffer[i] == '\n') // FIXME, use general line separator
                                        // here.
                {
                    if (lastEnter.get() != null)
                        super.write(b, 0, b.length);
                    else
                        lastEnter.set(dummy);
                    super.write(buffer, lastStart, i - lastStart + 1);
                    lastStart = i + 1;
                }
            }
            if (lastStart < start + len) {
                if (lastEnter.get() != null)
                    super.write(b, 0, b.length);
                lastEnter.set(null);
                super.write(buffer, lastStart, start + len - lastStart);
            }
        } else
            super.write(buffer, start, len);
    }

}
