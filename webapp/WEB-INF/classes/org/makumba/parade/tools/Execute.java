package org.makumba.parade.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;

public class Execute {

    public static int exec(Vector<String> v, File dir, PrintWriter ps) {
        String command = "";
        String[] cmd = new String[v.size()];
        String sep = "";
        // scans the arguements for spaces, surrounds with "" if some found (Windows)
        for (int i = 0; i < cmd.length; i++) {
            cmd[i] = (String) v.elementAt(i);
            command += sep;
            sep = " ";
            if (cmd[i].indexOf(' ') != -1 && !(cmd[i].startsWith("\"") && cmd[i].endsWith("\"")))
                command += "\"" + cmd[i] + "\"";
            else
                command += cmd[i];
        }
        ps.println("exec: cd " + dir);
        ps.println("exec: " + command);
        Date start = new Date();

        Process p1;
        try {
            p1 = Runtime.getRuntime().exec(cmd, null, dir);
        } catch (IOException e) {
            ps.println(e);
            return -1;
        }

        final Process p = p1;
        final PrintWriter ps1 = ps;
        new Thread(new Runnable() {
            public void run() {
                flushTo(new BufferedReader(new InputStreamReader(p.getErrorStream()), 81960), ps1);
            }
        }).start();

        flushTo(new BufferedReader(new InputStreamReader(p.getInputStream()), 81960), ps);

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            return -1;
        }
        int ret = p.exitValue();
        ps.println("exec: exit value: " + ret);
        ps.println("exec: elapsed time: " + (new Date().getTime() - start.getTime()) + " ms");
        return ret;
    }

    public static void flushTo(BufferedReader r, PrintWriter o) {
        String s;
        try {
            while ((s = r.readLine()) != null)
                o.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
