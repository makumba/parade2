package org.makumba.parade.tools;

import java.util.Comparator;

import org.makumba.parade.model.File;

public class FileComparator implements Comparator {

    public FileComparator() {
    }

    public int compare(Object d1, Object d2) {

        File f1 = (File) d1;
        File f2 = (File) d2;

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
