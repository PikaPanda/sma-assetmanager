package fr.smartapps.lib;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.io.InputStream;

/**
 *
 */
public class SMAStateListDrawable extends StateListDrawable {

    /*
    Constructor
     */
    public SMAStateListDrawable() {
        super();
    }

    /*
    Enabled / Disabled
     */
    public SMAStateListDrawable enabled(Drawable drawable) {
        addState(new int[] {android.R.attr.state_enabled}, drawable);
        return this;
    }

    public SMAStateListDrawable disabled(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_enabled}, drawable);
        return this;
    }

    /*
    Focused / Unfocused
     */
    public SMAStateListDrawable focused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable unfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_focused}, drawable);
        return this;
    }

    /*
    Windows focused / Windows unfocused
     */
    public SMAStateListDrawable windowFocused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_window_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable windowUnfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_window_focused}, drawable);
        return this;
    }

    /*
    Selected / Unselected
     */
    public SMAStateListDrawable selected(Drawable drawable) {
        addState(new int[] {android.R.attr.state_selected}, drawable);
        return this;
    }

    public SMAStateListDrawable unselected(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_selected}, drawable);
        return this;
    }

    /*
    Pressed / Unpressed
     */
    public SMAStateListDrawable pressed(Drawable drawable) {
        addState(new int[] {android.R.attr.state_pressed}, drawable);
        return this;
    }

    public SMAStateListDrawable unpressed(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_pressed}, drawable);
        return this;
    }

    /*
    Checked / Unchecked
     */
    public SMAStateListDrawable checked(Drawable drawable) {
        addState(new int[] {android.R.attr.state_checked}, drawable);
        return this;
    }

    public SMAStateListDrawable unchecked(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_checked}, drawable);
        return this;
    }

    /*
    Default states (add it at the end because it will add all the inverse states that has not been added
     */
    public SMAStateListDrawable inverse(Drawable drawable) {
        addState(new int[] {}, drawable);
        return this;
    }
}
