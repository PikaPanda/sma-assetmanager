package fr.smartapps.lib;

/**
 * Created by vchann on 12/08/2016.
 */
public interface SMAAudioPlayerListener {

    void onSongProgress(int progress, int totalProgress);

    void onSongFinish(int totalProgress);
}
