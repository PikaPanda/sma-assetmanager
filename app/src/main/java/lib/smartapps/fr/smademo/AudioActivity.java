package lib.smartapps.fr.smademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.smartapps.lib.SMAAssetManager;
import fr.smartapps.lib.SMAAudioPlayer;
import fr.smartapps.lib.SMAAudioPlayerListener;

public class AudioActivity extends AppCompatActivity {

    public SMAAssetManager assetManager;
    public SMAAudioPlayer audioPlayer;
    public AppCompatSeekBar seekBar;
    public TextView textInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        // text information
        textInfoView = (TextView) findViewById(R.id.text_info);

        // seek bar
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                audioPlayer.seekTo(seekBar.getProgress());
            }
        });

        // audio player
        assetManager = new SMAAssetManager(this);
        audioPlayer = assetManager.getAudioPlayer("random_music.mp3", new SMAAudioPlayerListener() {
            @Override
            public void onSongProgress(int progress, int totalProgress) {
                if (seekBar != null) {
                    seekBar.setMax(totalProgress);
                    seekBar.setProgress(progress);
                }
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
                        textInfoView.setText("Is Playing ...");
                    }
                    else {
                        audioPlayer.pause();
                        textInfoView.setText("On Pause ...");
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.stop();
        audioPlayer.release();
    }
}
