package org.makumba.parade.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Command line program to count lines, words and characters in files or from standard input, similar to the wc utility.
 * Run like that: java WordCount FILE1 FILE2 ... or like that: java WordCount < FILENAME.
 * 
 * @author Marco Schmidt
 */
public class WordCount {
    /**
     * Count lines, words and characters in given input stream and print stream name and those numbers to standard
     * output.
     * 
     * @param name
     *            name of input source
     * @param input
     *            stream to be processed
     * @throws IOException
     *             if there were I/O errors
     */
    private static long count(String name, BufferedReader in) throws IOException {
        long numLines = 0;
        long numWords = 0;
        long numChars = 0;
        String line;
        do {
            line = in.readLine();
            if (line != null) {
                numLines++;
                numChars += line.length();
                numWords += countWords(line);
            }
        } while (line != null);
        return numChars;
    }

    /**
     * Open file, count its words, lines and characters and print them to standard output.
     * 
     * @param fileName
     *            name of file to be processed
     */
    public static int count(String fileName) {
        BufferedReader in = null;
        long chars = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            in = new BufferedReader(fileReader);
            chars = count(fileName, in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return new Long(chars).intValue();
    }

    /**
     * Count words, lines and characters of given input stream and print them to standard output.
     * 
     * @param streamName
     *            name of input stream (to print it to stdout)
     * @param input
     *            InputStream to read from
     */
    private static void count(String streamName, InputStream input) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader in = new BufferedReader(inputStreamReader);
            count(streamName, in);
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Determine the number of words in the argument line.
     * 
     * @param line
     *            String to be examined, must be non-null
     * @return number of words, 0 or higher
     */
    private static long countWords(String line) {
        long numWords = 0;
        int index = 0;
        boolean prevWhitespace = true;
        while (index < line.length()) {
            char c = line.charAt(index++);
            boolean currWhitespace = Character.isWhitespace(c);
            if (prevWhitespace && !currWhitespace) {
                numWords++;
            }
            prevWhitespace = currWhitespace;
        }
        return numWords;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            count("stdin", System.in);
        } else {
            for (String arg : args) {
                count(arg);
            }
        }
    }
}
