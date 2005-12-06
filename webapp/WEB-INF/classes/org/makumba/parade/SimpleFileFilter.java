package org.makumba.parade;

import java.io.File;
import java.io.FileFilter;

public class SimpleFileFilter implements FileFilter {
    public boolean accept(File f) {
        String name = f.getName();
        if (name.endsWith("~") || name.endsWith(".class")
                || name.endsWith(".save"))
            return false;
        if (f.isDirectory()
                && (name.equals("CVS") || name.equals("serialized")))
            return false;
        if ((name.equals("work") || name.equals("logs")) && f.isDirectory()
                && f.getParentFile().getName().startsWith("tomcat"))
            return false;
        return true;
    }
}
