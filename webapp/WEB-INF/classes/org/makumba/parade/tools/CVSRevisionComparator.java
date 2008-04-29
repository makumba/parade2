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
     *            the first revision string
     * @param r1
     *            the second revision string
     * @return 0 if both are equal, -1 if r0 < r1, 1 if r0 > r1, and 2 if there's a comparison error
     */
    public int compare(String r0, String r1) {

        if (r0.equals(r1)) {
            return 0;
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
