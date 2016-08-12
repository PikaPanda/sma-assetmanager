package fr.smartapps.lib;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class SMAFileUtils {

    static private String TAG = "SMAFileUtils";

    static String getFileNameOutOfPath(String path) {
        String[] splitString = path.split("/");
        return splitString[splitString.length - 1];
    }

    static public boolean deleteFileOrDirectory(File file) {
        Log.e("FileUtils", "deleteFile : " + file.getAbsolutePath());
        if(file.exists()) {
            if(!file.isDirectory()) {
                file.delete();
                return true;
            }

            File[] files = file.listFiles();

            for(File currentFile : files) {
                if(currentFile.isDirectory()) {
                    deleteFileOrDirectory(currentFile);
                }
                else {
                    currentFile.delete();
                }
            }
        }
        return file.delete();
    }

    public static void copyFileOrDirectory(String urlSource, String urlDestination) {
        File sourceLocation = new File(urlSource);
        File targetLocation = new File(urlDestination);

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {
                copyFileOrDirectory(new File(sourceLocation, children[i]).getAbsolutePath(), new File(targetLocation, children[i]).getAbsolutePath());
            }
        }
        else {
            InputStream in;
            try {
                in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
