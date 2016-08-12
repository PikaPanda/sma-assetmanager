package fr.smartapps.lib.todo;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 */
public class FileUtils {

    static private String TAG = "FileUtils";

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
                    deleteFile(currentFile);
                }
                else {
                    currentFile.delete();
                }
            }
        }
        return file.delete();
    }

    static public boolean copyFile(String urlSource, String urlDestination) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File file = new File(urlSource);
            if (!file.exists())
                return false;

            in = new FileInputStream(file);


            out = new FileOutputStream(new File(urlDestination));
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            Log.e(TAG, String.format("Copied %s to %s", urlSource, urlDestination));
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }
}
