package lib.smartapps.fr.smademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import fr.smartapps.lib.SMAAssetManager;
import fr.smartapps.lib.SMADrawable;
import fr.smartapps.lib.SMAStateListDrawable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        SMAAssetManager assetManager = new SMAAssetManager(this);
        SMADrawable drawable = assetManager.getDrawable("assets://image.jpg");
        imageView.setBackground(drawable.alpha(125));

        Button button = (Button) findViewById(R.id.button);
        SMAStateListDrawable stateListDrawable = assetManager.getStateListDrawable()
                .focused("#ABCDEF")
                .pressed("#ABCDEF")
                .inverse("#ABCABC");
        button.setBackground(stateListDrawable);
    }
}
