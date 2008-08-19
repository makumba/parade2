package org.makumba.parade.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;

public class LineNumberCounter {

    public static int countLineNumbers(File f) {

        try {
            RandomAccessFile randFile = new RandomAccessFile(f, "r");
            long lastRec = randFile.length();
            randFile.close();
            FileReader fileRead = new FileReader(f);
            LineNumberReader lineRead = new LineNumberReader(fileRead);
            lineRead.skip(lastRec);
            int countRec = lineRead.getLineNumber() - 1;
            fileRead.close();
            lineRead.close();

            return countRec;

        } catch (IOException e) {
        }

        return -1;

    }

}
