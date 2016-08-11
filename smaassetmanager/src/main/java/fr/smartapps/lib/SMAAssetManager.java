package fr.smartapps.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 *
 */
public class SMAAssetManager {

    /*
    Attributes
     */
    private String TAG = "SMAAssetManager";
    protected Context context;
    //static public ZipResourceFile expansionFile = null;

    protected final int STORAGE_TYPE_UNDEFINED = 0;
    protected final int STORAGE_TYPE_ASSETS = 1;
    protected final int STORAGE_TYPE_EXTERNAL = 2;
    protected final int STORAGE_TYPE_EXTERNAL_PRIVATE = 3;
    protected final int STORAGE_TYPE_OBB = 4;

    public String SUFFIX_ASSETS = "assets://";
    public String SUFFIX_EXTERNAL = "external://";
    public String SUFFIX_EXTERNAL_PRIVATE = "external_private://";
    public String SUFFIX_OBB = "obb://";

    /*
    Constructor
     */
    public SMAAssetManager(Context context) {
        this.context = context;
    }

    /*
    Protected basics methods
     */
    protected int getStorageType(String url) {
        if (url == null)
            return STORAGE_TYPE_UNDEFINED;

        if (url.startsWith(SUFFIX_ASSETS))
            return STORAGE_TYPE_ASSETS;

        if (url.startsWith(SUFFIX_EXTERNAL))
            return STORAGE_TYPE_EXTERNAL;

        if (url.startsWith(SUFFIX_EXTERNAL_PRIVATE))
            return STORAGE_TYPE_EXTERNAL_PRIVATE;

        if (url.startsWith(SUFFIX_OBB))
            return STORAGE_TYPE_OBB;

        return STORAGE_TYPE_UNDEFINED;
    }

    protected String getExternalPublicStorageDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    protected String getExternalPrivateStorageDir() {
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.applicationInfo.dataDir;
        }
        catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    protected String getAssetsDir() {
        return "file:///android_asset/";
    }

    /*
    Public practical methods
     */
    public SMADrawable getDrawable(String url) {
        return new SMADrawable(getInputStream(url));
    }

    public Drawable getColorDrawable(String color) {
        if (color == null || !color.contains("#")) {
            return null;
        }
        return new ColorDrawable(Color.parseColor(color));
    }

    public String getString(String url) {
        try {
            InputStream inputStream = getInputStream(url);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } catch (IOException e) {
            Log.e(TAG, "error : " + e.getMessage());
        }
        return null;

    }

    public SMAStateListDrawable getStateListDrawable() {
        return new SMAStateListDrawable(this);
    }

    public SMAStateListColor getStateListColor() {
        return new SMAStateListColor();
    }

    public AssetFileDescriptor getAssetFileDescriptor(String url) {
        if (url == null || url.equals(""))
            return null;

        switch (getStorageType(url)) {

            case STORAGE_TYPE_ASSETS:
                try {
                    url = url.replace(SUFFIX_ASSETS, "");
                    return context.getAssets().openFd(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(SUFFIX_EXTERNAL, "");
                File file = new File(getExternalPublicStorageDir() + url);
                if (file.exists())
                    try {
                        Uri path = Uri.fromFile(file);
                        return context.getContentResolver().openAssetFileDescriptor(path, "rw");
                    } catch (FileNotFoundException e) {
                        Log.e("AssetManager", "Fail to get " + url + " from external public storage");
                    }
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(SUFFIX_EXTERNAL_PRIVATE, "");
                File privateFile = new File(getExternalPrivateStorageDir() + "/" + url);
                if (privateFile.exists())
                    try {
                        Uri path = Uri.fromFile(privateFile);
                        return context.getContentResolver().openAssetFileDescriptor(path, "rw");
                    } catch (FileNotFoundException e) {
                        Log.e("AssetManager", "Fail to get " + url + " from external private storage");
                    }
                break;

            case STORAGE_TYPE_OBB:
                url = url.replace(SUFFIX_OBB, "");
                /*if(expansionFile != null)
                    return expansionFile.getAssetFileDescriptor("obb/" + url);
                else
                    Log.e(TAG, "Fail to get expansion file");
                break;*/

            default:
                try {
                    return context.getAssets().openFd(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;
        }

        return null;
    }

    public InputStream getInputStream(String url) {
        if (url == null) {
            return null;
        }

        switch (getStorageType(url)) {

            case STORAGE_TYPE_ASSETS:
                url = url.replace(SUFFIX_ASSETS, "");
                try {
                    return context.getAssets().open(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(SUFFIX_EXTERNAL, "");
                File file = new File(getExternalPublicStorageDir() + url);
                if (file.exists()) {
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Fail to get " + url + " from external public storage");
                    }
                }
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(SUFFIX_EXTERNAL_PRIVATE, "");
                File privateFile = new File(getExternalPrivateStorageDir() + url);
                if (privateFile.exists()) {
                    try {
                        return new FileInputStream(privateFile);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Fail to get " + url + " from external private storage");
                    }
                }
                break;

            case STORAGE_TYPE_OBB:
                /*url = url.replace(SUFFIX_OBB, "");
                if (expansionFile != null) {
                    try {
                        return expansionFile.getInputStream(url);
                    } catch (IOException e) {
                        Log.e(TAG, "Fail to get " + url + " from OBB storage");
                    }
                }
                else {
                    Log.e(TAG, "ExpansionFile is not defined");
                }*/
                break;

            default:
                url = url.replace(SUFFIX_ASSETS, "");
                try {
                    return context.getAssets().open(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;
        }
        return null;
    }

    public Typeface getTypeFace(String url) {
        switch (getStorageType(url)) {
            case STORAGE_TYPE_ASSETS:
                url = url.replace(SUFFIX_ASSETS, "");
                try {
                    return Typeface.createFromAsset(context.getAssets(), url);
                } catch (Exception e) {
                    Log.e(TAG, "Fail to get " + url + " from assets");
                }
                break;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(SUFFIX_EXTERNAL, "");
                File file = new File(getExternalPublicStorageDir() + url);
                if (file.exists()) {
                    try {
                        return Typeface.createFromFile(file);
                    } catch (Exception e) {
                        Log.e(TAG, "Fail to get " + url + " from external public storage");
                    }
                }
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(SUFFIX_EXTERNAL_PRIVATE, "");
                File privateFile = new File(getExternalPrivateStorageDir() + url);
                if (privateFile.exists()) {
                    try {
                        return Typeface.createFromFile(privateFile);
                    } catch (Exception e) {
                        Log.e(TAG, "Fail to get " + url + " from external private storage");
                    }
                }
                break;

            case STORAGE_TYPE_OBB:
                /*url = url.replace(SUFFIX_OBB, "");
                if (expansionFile != null) {
                    try {
                        return expansionFile.getInputStream(url);
                    } catch (IOException e) {
                        Log.e(TAG, "Fail to get " + url + " from OBB storage");
                    }
                }
                else {
                    Log.e(TAG, "ExpansionFile is not defined");
                }*/
                break;

            default:

                break;
        }
        return null;
    }

    public boolean doesFileExist(String url) {
        if (url == null) {
            return false;
        }

        switch (getStorageType(url)) {
            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(SUFFIX_EXTERNAL, "");
                File externalPublicFile = new File(getExternalPublicStorageDir() + url);
                Log.e(TAG, externalPublicFile.getAbsolutePath() + " exist " + externalPublicFile.exists());
                return externalPublicFile.exists();

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(SUFFIX_EXTERNAL_PRIVATE, "");
                File externalPrivateFile = new File(getExternalPrivateStorageDir() + url);
                Log.e(TAG, externalPrivateFile.getAbsolutePath() + " exist " + externalPrivateFile.exists());
                return externalPrivateFile.exists();

            case STORAGE_TYPE_ASSETS:
                url = url.replace(SUFFIX_ASSETS, "");
                try {
                    return Arrays.asList(context.getResources().getAssets().list("")).contains(url);
                } catch (IOException e) {
                    return false;
                }

            case STORAGE_TYPE_OBB:
                url = url.replace(SUFFIX_OBB, "");
                // ???
                return true;

            default:
                return true;
        }
    }


}
