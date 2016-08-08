package fr.smartapps.lib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.io.InputStream;

/**
 *
 */
public class SMADrawable extends BitmapDrawable {

    /*
    Constructor
     */
    public SMADrawable(InputStream inputStream) {
        super(inputStream);
    }

    /*
    Colorize any pixels that aren't invisible into color "color"
     */
    public SMADrawable filter(String color) {
        super.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
        return this;
    }

    /*
    Translucent value between 0 and 255
     */
    public SMADrawable alpha(int alpha) {
        this.setAlpha(alpha);
        return this;
    }

    /*
    Set density 0 to 5
     */
    public SMADrawable density(int density) {
        switch (density) {
            case 0:
                this.setTargetDensity(DisplayMetrics.DENSITY_LOW); // 120
                break;

            case 1:
                this.setTargetDensity(DisplayMetrics.DENSITY_MEDIUM); // 160
                break;

            case 2:
                this.setTargetDensity(DisplayMetrics.DENSITY_HIGH); // 240
                break;

            case 3:
                this.setTargetDensity(DisplayMetrics.DENSITY_XHIGH); // 320
                break;

            case 4:
                this.setTargetDensity(DisplayMetrics.DENSITY_XXHIGH); // 480
                break;

            case 5:
                this.setTargetDensity(DisplayMetrics.DENSITY_XXXHIGH); // 640
                break;
        }
        return this;
    }
}
