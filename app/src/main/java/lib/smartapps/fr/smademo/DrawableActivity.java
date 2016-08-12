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

        if (imageView1 != null) {
            imageView1.setBackground(assetManager.getDrawable(Config.PREFIX_URL + "tinder.png"));
        }
        if (imageView2 != null) {
            imageView2.setBackground(assetManager.getDrawable(Config.PREFIX_URL + "tinder.png").filter("#000000"));
        }
        if (imageView3 != null) {
            imageView3.setBackground(assetManager.getDrawable(Config.PREFIX_URL + "tinder.png").alpha(100));
        }

        // buttons
        Button buttonView = (Button) findViewById(R.id.button);
        ToggleButton toggleButtonView = (ToggleButton) findViewById(R.id.toggle_button);
        ImageButton imageButtonView = (ImageButton) findViewById(R.id.image_button);

        if (buttonView != null) {
            buttonView.setBackground(assetManager.getStateListDrawable()
                    .focused(Config.PREFIX_URL + "tinder.png")
                    .pressed(Config.PREFIX_URL + "tinder.png")
                    .inverse(assetManager.getDrawable(Config.PREFIX_URL + "tinder.png").filter("#000000")));
        }
        if (toggleButtonView != null) {
            toggleButtonView.setBackground(assetManager.getStateListDrawable()
                    .checked(assetManager.getDrawable(Config.PREFIX_URL + "tinder.png").alpha(100))
                    .inverse(Config.PREFIX_URL + "tinder.png"));
        }
        if (imageButtonView != null) {
            imageButtonView.setBackground(assetManager.getStateListDrawable()
                    .focused(Config.PREFIX_URL + "pikachu.png")
                    .pressed(Config.PREFIX_URL + "pikachu.png")
                    .inverse(assetManager.getDrawable(Config.PREFIX_URL + "pikachu.png").alpha(100)));
        }


    }
}
