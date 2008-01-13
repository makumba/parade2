package org.makumba.parade.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class FileController {

    static Logger logger = Logger.getLogger(FileController.class.getName());

    public static void saveFile(String absoluteFilePath, String[] source) {
        
        java.io.File f= new java.io.File(absoluteFilePath);
        java.io.File d;
        String content = source[0];

        // we save
        if (f.getParent() != null) {
            d = new java.io.File(f.getParent());
            d.mkdirs();
        }
        try {
            f.createNewFile();
            
            // FIXME fishy windows line-break code. see if that doesn't cause trouble
            boolean windows = System.getProperty("line.separator").length() > 1;
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
            for (int i = 0; i < content.length(); i++) {
                if (windows || content.charAt(i) != '\r')
                    pw.print(content.charAt(i));
            }
            pw.close();
        } catch (IOException e) {
            logger.error("Error while creating file ",e);
        }
        
    }
    
}
