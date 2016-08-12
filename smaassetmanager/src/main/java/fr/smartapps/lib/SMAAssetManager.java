package fr.smartapps.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    protected SMAObbManager obbManager;

    protected final int STORAGE_TYPE_UNDEFINED = 0;
    protected final int STORAGE_TYPE_ASSETS = 1;
    protected final int STORAGE_TYPE_EXTERNAL = 2;
    protected final int STORAGE_TYPE_EXTERNAL_PRIVATE = 3;
    protected final int STORAGE_TYPE_OBB = 4;

    final static public String SUFFIX_ASSETS = "assets://";
    final static public String SUFFIX_EXTERNAL = "external://";
    final static public String SUFFIX_EXTERNAL_PRIVATE = "external_private://";
    final static public String SUFFIX_OBB = "obb://";

    /*
    Constructor
     */
    public SMAAssetManager(Context context) {
        this.context = context;
        obbManager = new SMAObbManager(context);
    }

    /*
    Protected basics methods
     */
    public int getStorageType(String url) {
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

    public String getExternalPublicStorageSuffix() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public String getExternalPrivateStorageSuffix() {
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

    public String getAssetsSuffix() {
        return "file:///android_asset/";
    }

    public String getObbSuffix() {
        return "";
    }

    public String getAbsoluteUrl(String url) {
        switch (getStorageType(url)) {

            case STORAGE_TYPE_ASSETS:
                url = url.replace(SUFFIX_ASSETS, "");
                return getAssetsSuffix() + url;

            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(SUFFIX_EXTERNAL, "");
                return getExternalPublicStorageSuffix() + url;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(SUFFIX_EXTERNAL_PRIVATE, "");
                return getExternalPrivateStorageSuffix() + url;

            case STORAGE_TYPE_OBB:
                url = url.replace(SUFFIX_OBB, "");
                return getObbSuffix() + url;

            default:
                return url;
        }
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

    public SMAAudioPlayer getAudioPlayer(String url, SMAAudioPlayerListener audioPlayerListener) {
        return new SMAAudioPlayer(context, this, url, audioPlayerListener);
    }

    /*
    Public practical basics methods
     */
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
                File file = new File(getExternalPublicStorageSuffix() + url);
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
                File privateFile = new File(getExternalPrivateStorageSuffix() + url);
                if (privateFile.exists()) {
                    try {
                        return Typeface.createFromFile(privateFile);
                    } catch (Exception e) {
                        Log.e(TAG, "Fail to get " + url + " from external private storage");
                    }
                }
                break;

            case STORAGE_TYPE_OBB:
                // work around for Typeface from OBB ...
                InputStream inputStream = getInputStream(url);
                Typeface typeFace = null;

                url = url.replace(SUFFIX_OBB, "");
                String outPath = getExternalPrivateStorageSuffix() + "/" + url;

                try
                {
                    byte[] buffer = new byte[inputStream.available()];
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

                    int l = 0;
                    while((l = inputStream.read(buffer)) > 0)
                        bos.write(buffer, 0, l);

                    bos.close();

                    typeFace = Typeface.createFromFile(outPath);

                    // clean up
                    new File(outPath).delete();
                    return typeFace;
                }
                catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from OBB storage");
                }
                break;

            default:
                url = url.replace(SUFFIX_ASSETS, "");
                try {
                    return Typeface.createFromAsset(context.getAssets(), url);
                } catch (Exception e) {
                    Log.e(TAG, "Fail to get " + url + " from default assets");
                }
                break;
        }
        return null;
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
                File file = new File(getExternalPublicStorageSuffix() + url);
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
                File privateFile = new File(getExternalPrivateStorageSuffix() + "/" + url);
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
                if (obbManager != null && obbManager.getExpansionFile() != null) {
                    return obbManager.getExpansionFile().getAssetFileDescriptor(url);
                }
                else {
                    Log.e(TAG, "ExpansionFile hasn't been initialized");
                }
                break;

            default:
                try {
                    return context.getAssets().openFd(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from default assets");
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
                File file = new File(getExternalPublicStorageSuffix() + url);
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
                File privateFile = new File(getExternalPrivateStorageSuffix() + url);
                if (privateFile.exists()) {
                    try {
                        return new FileInputStream(privateFile);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Fail to get " + url + " from external private storage");
                    }
                }
                break;

            case STORAGE_TYPE_OBB:
                url = url.replace(SUFFIX_OBB, "");
                if (obbManager != null && obbManager.getExpansionFile() != null) {
                    try {
                        return obbManager.getExpansionFile().getInputStream(url);
                    } catch (IOException e) {
                        Log.e(TAG, "Fail to get " + url + " from OBB storage");
                    }
                }
                else {
                    Log.e(TAG, "ExpansionFile hasn't been initialized");
                }
                break;

            default:
                url = url.replace(SUFFIX_ASSETS, "");
                try {
                    return context.getAssets().open(url);
                } catch (IOException e) {
                    Log.e(TAG, "Fail to get " + url + " from default assets");
                }
                break;
        }
        return null;
    }

    /*
    Public tools methods
     */
    public boolean doesFileExist(String url) {
        if (url == null) {
            return false;
        }

        switch (getStorageType(url)) {
            case STORAGE_TYPE_EXTERNAL:
                url = url.replace(SUFFIX_EXTERNAL, "");
                File externalPublicFile = new File(getExternalPublicStorageSuffix() + url);
                Log.e(TAG, externalPublicFile.getAbsolutePath() + " exist " + externalPublicFile.exists());
                return externalPublicFile.exists();

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                url = url.replace(SUFFIX_EXTERNAL_PRIVATE, "");
                File externalPrivateFile = new File(getExternalPrivateStorageSuffix() + url);
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

    public void initMainOBB(int mainVersion, int mainSize) {
        obbManager.initMainOBB(mainVersion, mainSize);
    }

    public void initPatchOBB(int patchVersion, int patchSize) {
        obbManager.initPatchOBB(patchVersion, patchSize);
    }

    /*
    File Tools : copy/create/delete file/folder TODO
     */
    public void copyFile(String urlSource, String urlDestination) {
        switch (getStorageType(urlDestination)) {
            case STORAGE_TYPE_ASSETS:
                Log.e(TAG, "You cannot copy files inside APK folders (res, assets, ...).");
                return;

            case STORAGE_TYPE_OBB:
                Log.e(TAG, "You cannot copy files inside OBB zip folder.");
                return;

            case STORAGE_TYPE_EXTERNAL:
                // continue
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                // continue
                break;

            default:
                Log.e(TAG, "Wrong url destination : " + urlDestination + " / urlDestination must start with SMAAssetManager suffixes");
                return;
        }

        switch (getStorageType(urlSource)) {
            case STORAGE_TYPE_ASSETS:
                copyFileOrDirectoryFromAssetsDir(getAbsoluteUrl(urlSource), getAbsoluteUrl(urlDestination));
                return;

            case STORAGE_TYPE_OBB:
                // TODO
                return;

            case STORAGE_TYPE_EXTERNAL:
                copyFileOrDirectoryFromExternalDir(getAbsoluteUrl(urlSource), getAbsoluteUrl(urlDestination));
                break;

            case STORAGE_TYPE_EXTERNAL_PRIVATE:
                copyFileOrDirectoryFromExternalDir(getAbsoluteUrl(urlSource), getAbsoluteUrl(urlDestination));
                break;

            default:
                Log.e(TAG, "Wrong url source : " + urlSource + " / urlSource must start with SMAAssetManager suffixes (assets://, external://, external_private://, obb://)");
                return;
        }
    }

    protected void copyFileOrDirectoryFromExternalDir(String urlSource, String urlDestination) {
        File sourceLocation = new File(urlSource);
        File targetLocation = new File(urlDestination);

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {
                copyFileOrDirectoryFromExternalDir(new File(sourceLocation, children[i]).getAbsolutePath(), new File(targetLocation, children[i]).getAbsolutePath());
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

    // http://stackoverflow.com/questions/16983989/copy-directory-from-assets-to-data-folder?answertab=active#tab-top
    protected boolean copyFileOrDirectoryFromAssetsDir(String urlSource, String urlDestination) {
        try {
            AssetManager assetManager = context.getAssets();
            String[] files = assetManager.list(urlSource);
            new File(urlDestination).mkdirs();
            boolean res = true;
            for (String file : files)
                if (file.contains(".")) {
                    res &= copyAsset(assetManager, urlSource + "/" + file, urlDestination + "/" + file);
                }
                else {
                    res &= copyFileOrDirectoryFromAssetsDir(urlSource + "/" + file, urlDestination + "/" + file);
                }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // http://stackoverflow.com/questions/16983989/copy-directory-from-assets-to-data-folder?answertab=active#tab-top
    protected boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // http://stackoverflow.com/questions/16983989/copy-directory-from-assets-to-data-folder?answertab=active#tab-top
    protected void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
