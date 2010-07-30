package org.makumba.parade.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
 
public class FileOperations
{
   public static String ReadFileToString(String path)
   {
        return ReadFileToString(new File(path));
   }
   
   public static String ReadFileToString(File file)
   {
        String encoding = null; //null = platform default
        String content = null;
 
        try
        {
            content = FileUtils.readFileToString(file, encoding);
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return content;
    }

}                          