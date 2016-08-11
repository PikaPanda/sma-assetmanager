package fr.smartapps.lib;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 *
 */
public class SMAStateListDrawable extends StateListDrawable {

    /*
    Attributes
     */
    SMAAssetManager assetManager;

    /*
    Constructor
     */
    public SMAStateListDrawable(SMAAssetManager assetManager) {
        super();
        this.assetManager = assetManager;
    }

    /*
    Enabled / Disabled
     */
    public SMAStateListDrawable enabled(Drawable drawable) {
        addState(new int[] {android.R.attr.state_enabled}, drawable);
        return this;
    }

    public SMAStateListDrawable enabled(String url) {
        if (url.startsWith("#")) {
            return enabled(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return enabled(assetManager.getDrawable(url));
        }
    }

    public SMAStateListDrawable disabled(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_enabled}, drawable);
        return this;
    }

    public SMAStateListDrawable disabled(String url) {
        if (url.startsWith("#")) {
            return disabled(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return disabled(assetManager.getDrawable(url));
        }
    }

    /*
    Focused / Unfocused
     */
    public SMAStateListDrawable focused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable focused(String url) {
        if (url.startsWith("#")) {
            return focused(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return focused(assetManager.getDrawable(url));
        }
    }

    public SMAStateListDrawable unfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable unfocused(String url) {
        if (url.startsWith("#")) {
            return unfocused(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return unfocused(assetManager.getDrawable(url));
        }
    }

    /*
    Windows focused / Windows unfocused
     */
    public SMAStateListDrawable windowFocused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_window_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable windowFocused(String url) {
        if (url.startsWith("#")) {
            return windowFocused(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return windowFocused(assetManager.getDrawable(url));
        }
    }

    public SMAStateListDrawable windowUnfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_window_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable windowUnfocused(String url) {
        if (url.startsWith("#")) {
            return windowUnfocused(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return windowUnfocused(assetManager.getDrawable(url));
        }
    }

    /*
    Selected / Unselected
     */
    public SMAStateListDrawable selected(Drawable drawable) {
        addState(new int[] {android.R.attr.state_selected}, drawable);
        return this;
    }

    public SMAStateListDrawable selected(String url) {
        if (url.startsWith("#")) {
            return selected(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return selected(assetManager.getDrawable(url));
        }
    }

    public SMAStateListDrawable unselected(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_selected}, drawable);
        return this;
    }

    public SMAStateListDrawable unselected(String url) {
        if (url.startsWith("#")) {
            return unselected(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return unselected(assetManager.getDrawable(url));
        }
    }

    /*
    Pressed / Unpressed
     */
    public SMAStateListDrawable pressed(Drawable drawable) {
        addState(new int[] {android.R.attr.state_pressed}, drawable);
        return this;
    }

    public SMAStateListDrawable pressed(String url) {
        if (url.startsWith("#")) {
            return pressed(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return pressed(assetManager.getDrawable(url));
        }
    }

    public SMAStateListDrawable unpressed(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_pressed}, drawable);
        return this;
    }

    public SMAStateListDrawable unpressed(String url) {
        if (url.startsWith("#")) {
            return unpressed(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return unpressed(assetManager.getDrawable(url));
        }
    }

    /*
    Checked / Unchecked
     */
    public SMAStateListDrawable checked(Drawable drawable) {
        addState(new int[] {android.R.attr.state_checked}, drawable);
        return this;
    }

    public SMAStateListDrawable checked(String url) {
        if (url.startsWith("#")) {
            return checked(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return checked(assetManager.getDrawable(url));
        }
    }

    public SMAStateListDrawable unchecked(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_checked}, drawable);
        return this;
    }

    public SMAStateListDrawable unchecked(String url) {
        if (url.startsWith("#")) {
            return unchecked(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return unchecked(assetManager.getDrawable(url));
        }
    }

    /*
    Default states (add it at the end because it will add all the inverse states that has not been added
     */
    public SMAStateListDrawable inverse(Drawable drawable) {
        addState(new int[] {}, drawable);
        return this;
    }

    public SMAStateListDrawable inverse(String url) {
        if (url.startsWith("#")) {
            return inverse(new ColorDrawable(Color.parseColor(url)));
        }
        else {
            return inverse(assetManager.getDrawable(url));
        }
    }
}
