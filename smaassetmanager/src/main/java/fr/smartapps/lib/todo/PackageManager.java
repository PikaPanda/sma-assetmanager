package fr.smartapps.lib.todo;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.smartapps.commonlib.data.config.AppDescription;
import fr.smartapps.commonlib.packagemanager.listener.DownloadProgressListener;
import fr.smartapps.commonlib.packagemanager.listener.UnzipProgressListener;
import fr.smartapps.commonlib.packagemanager.worker.DownloadTask;
import fr.smartapps.commonlib.packagemanager.worker.UnzipTask;
import fr.smartapps.commonlib.utils.AssetManager;
import fr.smartapps.commonlib.utils.FileUtils;
import fr.smartapps.commonlib.utils.Utils;
import fr.smartapps.commonlib.utils.enumeration.FilePath;

/**
 * Singleton
 */
public class PackageManager {

    private String TAG = "PackageManager";
    static public String packageDirectoryName = "packages";
    static public String baseDirectory;
    static public String currentPackage;
    static private PackageManager singletonInstance = null;
    protected Context context;
    protected List<AppDescription> packagesList;
    public AssetManager assetManager;
    protected DownloadTask downloadTask;
    protected UnzipTask unzipTask;
    static public FilePath packageStorageType = FilePath.EXTERNAL_PRIVATE;

    /*
    Singleton
     */
    public static PackageManager getInstance(AssetManager assetManager) {
        if(singletonInstance == null) {
            singletonInstance = new PackageManager(assetManager);
        }
        return singletonInstance;
    }

    /*
    Constructor
     */
    protected PackageManager(AssetManager assetManager) {
        this.context = assetManager.getContext();
        switch (assetManager.getStorageType()) {
            case EXTERNAL:
                baseDirectory = assetManager.getExternalPublicStorageDir();
                break;
            case EXTERNAL_PRIVATE:
                baseDirectory = assetManager.getExternalPrivateStorageDir();
                break;
            default:
                baseDirectory = assetManager.getExternalPublicStorageDir();
                break;
        }

    }

    /*
    Return list of package inside package folder
     */
    public List<AppDescription> listPackagesAndClean() {
        List<AppDescription> result = new ArrayList<>();
        File packageDirectoryFile = new File(baseDirectory + packageDirectoryName);
        if(!packageDirectoryFile.exists())
            packageDirectoryFile.mkdirs();

        File[] files = packageDirectoryFile.listFiles();
        Log.e(TAG, "listPackagesAndClean : " + packageDirectoryFile.getPath());
        for(File file : files) {
            if(file.isDirectory()) {
                File fileDescription = new File(file.getAbsolutePath() + "/" + "app_description.json");
                if (fileDescription.exists()) {
                    Log.e(TAG, "listPackagesAndClean - package : " + file.getAbsolutePath());
                    AssetManager assetManagerExternal = new AssetManager(packageStorageType, packageDirectoryName + "/" + file.getName(), context);
                    AppDescription appDescription = (AppDescription) Utils.parseJson("app_description.json", AppDescription.class, assetManagerExternal);
                    appDescription.directory_path = packageDirectoryName + "/" + file.getName();
                    result.add(appDescription);
                } else {
                    FileUtils.deleteFile(file);
                }
            }
            else {
                FileUtils.deleteFile(file);
            }
        }
        return result;
    }

    /*
    To override
     */
    public boolean validatePackage(String packageName) {
        return true;
    }

    /*
    Delete all zip from zip folder
     */
    public void clearZipFolder() {
        File zipDirectory = new File(baseDirectory);
        if(!zipDirectory.isDirectory())
            return;

        String[] fileList = zipDirectory.list();
        for(String file : fileList) {
            if(file.endsWith(".zip")) {
                File zipFile = new File(baseDirectory + "/" + file);
                FileUtils.deleteFile(zipFile);
            }
        }
    }

    /*
    Delete package
     */
    public void removePackage(String packageName) {
        File filePath;
        if (packageName.contains("/storage/emulated/"))
            filePath = new File(packageName);
        else
            filePath = new File(baseDirectory + packageDirectoryName + "/" + packageName);
        FileUtils.deleteFile(filePath);
    }

    public void removeAllPackagesExcept(String packageName) {
        File packageDirectoryFile = new File(baseDirectory + packageDirectoryName);
        File[] files = packageDirectoryFile.listFiles();
        for(File file : files) {
            if(!file.getName().equals(packageName)) {
                removePackage(file.getName());
            }
        }
    }

    /*
    Async Task : Unzip package
     */
    public void unzipPackage(UnzipProgressListener unzipProgressListener, String zipFilePath, String packageName) {
        Log.e(TAG, "unzip path : " + baseDirectory + packageDirectoryName + "/" + packageName);
        unzipTask = new UnzipTask(context, zipFilePath, baseDirectory + packageDirectoryName + "/" + packageName);
        unzipTask.setOnUnzipProgressListener(unzipProgressListener);
        unzipTask.execute();
    }

    /*
    AsyncTask : Download package
     */
    public void downloadPackageZip(DownloadProgressListener downloadProgressListener, String url) {
        String[] splitString = url.split("/");
        currentPackage = splitString[splitString.length - 1].split("\\.")[0];
        clearZipFolder();
        downloadTask = new DownloadTask(context, url, baseDirectory + "/" + splitString[splitString.length - 1]);
        downloadTask.setOnDownloadProgressListener(downloadProgressListener);
        downloadTask.execute();
    }

    /*
    Cancel download task
     */
    public void cancelDownloadZipTask(boolean cancelDownload) {
        if (downloadTask != null)
            downloadTask.cancelDownload(cancelDownload);
    }

    /*
    cancel unzip task
     */
    public void cancelUnzipTask(boolean cancelUnzip) {
        if (unzipTask != null)
            unzipTask.cancelUnzip(cancelUnzip);
    }

    /*
    Check if device is connected to internet == iOS : checkReachability() = check wifi + 3G ?
     */
    public boolean isOnline() {
        return Utils.isOnline(context);
    }

    // addPackageAtUrl() = downloadPackageZip() + unzipPackage()
    // addPackageFromZipFile() = unzipPackage
    // importPackagesAtPath() = addPackageFromZipFile() + delete zip

}
