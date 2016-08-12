package lib.smartapps.fr.smademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import fr.smartapps.lib.SMAAssetManager;
import fr.smartapps.lib.SMAAudioPlayer;
import fr.smartapps.lib.SMAAudioPlayerListener;

public class AudioActivity extends AppCompatActivity {

    public SMAAssetManager assetManager;
    public SMAAudioPlayer audioPlayer;
    public AppCompatSeekBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        // audio player
        assetManager = new SMAAssetManager(this);
        audioPlayer = assetManager.getAudioPlayer("random_music.mp3", new SMAAudioPlayerListener() {
            @Override
            public void onSongProgress(int progress, int totalProgress) {
                progressBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
                progressBar.setMax(totalProgress);
                progressBar.setProgress(progress);
            }

            @Override
            public void onSongFinish(int totalProgress) {

            }
        });

        // toggle button play / pause
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggle_button);
        if (toggleButton != null) {
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        audioPlayer.start();
                    }
                    else {
                        audioPlayer.pause();
                    }
                }
            });
        }
    }
}
