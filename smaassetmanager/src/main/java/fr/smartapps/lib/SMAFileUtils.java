package fr.smartapps.lib;

import android.content.Context;
import android.content.res.AssetManager;
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





    public static void copyFileOrDirectoryFromAssets(Context context, String urlSource, String urlDestination) {
        urlSource = urlSource.replace(SMAAssetManager.SUFFIX_ASSETS, "");

        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) {
            for (String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    File outFile = new File(urlDestination, filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            }
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
