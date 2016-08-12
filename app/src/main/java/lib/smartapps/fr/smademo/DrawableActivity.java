package lib.smartapps.fr.smademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import fr.smartapps.lib.SMAAssetManager;

public class DrawableActivity extends AppCompatActivity {

    protected SMAAssetManager assetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        assetManager = new SMAAssetManager(this);

        // icons
        ImageView imageView1 = (ImageView) findViewById(R.id.image_1);
        ImageView imageView2 = (ImageView) findViewById(R.id.image_2);
        ImageView imageView3 = (ImageView) findViewById(R.id.image_3);

        imageView1.setBackground(assetManager.getDrawable("tinder.png"));
        imageView2.setBackground(assetManager.getDrawable("tinder.png").filter("#000000"));
        imageView3.setBackground(assetManager.getDrawable("tinder.png").alpha(100));

        // buttons
        Button buttonView = (Button) findViewById(R.id.button);
        ToggleButton toggleButtonView = (ToggleButton) findViewById(R.id.toggle_button);
        ImageButton imageButtonView = (ImageButton) findViewById(R.id.image_button);

        buttonView.setBackground(assetManager.getStateListDrawable().focused("tinder.png").pressed("tinder.png").inverse(assetManager.getDrawable("tinder.png").filter("#000000")));
        toggleButtonView.setBackground(assetManager.getStateListDrawable().checked(assetManager.getDrawable("tinder.png").alpha(100)).inverse("tinder.png"));
        imageButtonView.setBackground(assetManager.getStateListDrawable().focused("pikachu.png").pressed("pikachu.png").inverse(assetManager.getDrawable("pikachu.png").alpha(100)));


    }
}
