package fr.smartapps.lib;

import android.content.Context;
import android.util.Log;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;

import java.io.IOException;

/**
 * Created by vchann on 12/08/2016.
 */
public class SMAObbManager {

    private String TAG = "SMAObbManager";
    protected int OBB_MAIN_VERSION = 0;
    protected int OBB_PATCH_VERSION = 0;
    protected int OBB_MAIN_FILE_SIZE = 0;
    protected int OBB_PATCH_FILE_SIZE = 0;
    protected Context context;
    static protected ZipResourceFile expansionFile = null;

    public SMAObbManager(Context context) {
        this.context = context;
    }

    public boolean isOBBDelivered() {
        String mainFilename = Helpers.getExpansionAPKFileName(context, true, OBB_MAIN_VERSION);
        String patchFilename = Helpers.getExpansionAPKFileName(context, false, OBB_PATCH_VERSION);

        if (mainFilename != null && !Helpers.doesFileExist(context, mainFilename, OBB_MAIN_FILE_SIZE, false)) {
            return false;
        }

        if (patchFilename != null && !Helpers.doesFileExist(context, patchFilename,OBB_PATCH_FILE_SIZE, false)) {
            return false;
        }
        return true;
    }

    public void initMainOBB(int version, int fileSize) {
        OBB_MAIN_VERSION = version;
        OBB_MAIN_FILE_SIZE = fileSize;
    }

    public void initPatchOBB(int version, int fileSize) {
        OBB_PATCH_VERSION = version;
        OBB_PATCH_FILE_SIZE = fileSize;
    }

    public boolean isMainOBBInitialized() {
        if (OBB_MAIN_VERSION == 0 || OBB_MAIN_FILE_SIZE == 0) {
            return false;
        }
        return true;
    }

    public boolean isPatchOBBInitialized() {
        if (OBB_PATCH_VERSION == 0 || OBB_PATCH_FILE_SIZE == 0) {
            return false;
        }
        return true;
    }

    public ZipResourceFile getExpansionFile() {
        if (expansionFile == null) {
            try {
                expansionFile = APKExpansionSupport.getAPKExpansionZipFile(context,
                        OBB_MAIN_VERSION,
                        OBB_PATCH_FILE_SIZE);
                Log.e(TAG, "Expansion File OBB found");
            } catch (IOException e) {
                Log.e(TAG, "Failed to find Expansion File OBB", e);
            }
            return expansionFile;
        }
        else {
            return expansionFile;
        }
    }
}
