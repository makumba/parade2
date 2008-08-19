package org.makumba.parade.tools;

import java.util.Comparator;

/**
 * This comparator compares CVS revisions of the kind 1.12.<br>
 * 
 * @author Manuel Gay
 * 
 */
public class CVSRevisionComparator implements Comparator<String> {

    /**
     * Compares two CVS revisions
     * 
     * @param r0
     *            the revision string of the supposedly new revision
     * @param r1
     *            the revision string of the supposedly old revision
     * 
     * @return 0 if both are equal, -1 if r0 < r1, 1 if r0 > r1, and 2 if there's a comparison error
     */
    public int compare(String r0, String r1) {

        if (r0.equals(r1)) {
            return 0;
        }

        if (r0.equals("NONE")) {
            return -1;
        }

        // new file
        // we do a strong assumption here: we assume that r0 is the new one, and that we don't have a 1.1 moved to
        // attic...
        if (r0.equals("NONE") && r1.equals("1.1")) {
            return -1;
        }

        if (r0.equals("1.1") && r1.equals("NONE")) {
            return 1;
        }

        // file moved to attic
        if (r0.equals("NONE")) {
            return -1;
        }

        if (r1.equals("NONE")) {
            return 1;
        }

        String[] re0 = r0.split("\\.");

        int[] rev0 = new int[re0.length];

        for (int i = 0; i < re0.length; i++) {
            rev0[i] = Integer.parseInt(re0[i]);
        }

        String[] re1 = r1.split("\\.");

        int[] rev1 = new int[re1.length];

        for (int i = 0; i < re1.length; i++) {
            rev1[i] = Integer.parseInt(re1[i]);
        }

        if (!(rev0.length == rev1.length)) {
            return 2;
        }

        int i = 0;

        while (i < rev0.length) {
            if (rev0[i] < rev1[i]) {
                return -1;
            } else if (rev0[i] > rev1[i]) {
                return 1;
            } else if (rev0[i] == rev1[i]) {
                i++;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        CVSRevisionComparator c = new CVSRevisionComparator();
        System.out.println(c.compare("1.12", "1.2"));
    }

}
