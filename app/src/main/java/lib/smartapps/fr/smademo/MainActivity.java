package lib.smartapps.fr.smademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import fr.smartapps.lib.SMAAssetManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AUDIO
        Button button1 = (Button) findViewById(R.id.button1);
        if (button1 != null) {
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, AudioActivity.class);
                    startActivity(intent);
                }
            });
        }

        // DRAWABLE
        Button button2 = (Button) findViewById(R.id.button2);
        if (button2 != null) {
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DrawableActivity.class);
                    startActivity(intent);
                }
            });
        }

        // TEXT
        Button button3 = (Button) findViewById(R.id.button3);
        if (button3 != null) {
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, TextActivity.class);
                    startActivity(intent);
                }
            });
        }

        // VIDEO
        Button button4 = (Button) findViewById(R.id.button4);
        if (button4 != null) {
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    startActivity(intent);
                }
            });
        }

        initCopy();
    }

    protected void initCopy() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                SMAAssetManager assetManager = new SMAAssetManager(getApplicationContext());
                assetManager.copyFile("assets://", "external://");
            }
        };
        task.run();
    }
}
