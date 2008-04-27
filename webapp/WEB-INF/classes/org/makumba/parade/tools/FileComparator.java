package org.makumba.parade.tools;

import java.util.Comparator;

import org.makumba.parade.model.File;

public class FileComparator implements Comparator<File> {

    public FileComparator() {
    }

    public int compare(File f1, File f2) {

        if (f1.getIsDir() && !f2.getIsDir())
            return -1;
        if (!f1.getIsDir() && f2.getIsDir())
            return 1;

        if (f1.getIsDir() && f2.getIsDir()) {
            String s1 = f1.getName();
            String s2 = f2.getName();
            return s1.compareTo(s2);
        }

        return 0;

    }
}
