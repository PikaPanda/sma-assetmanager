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
    public SMAStateListDrawable addStateEnabled(Drawable drawable) {
        addState(new int[] {android.R.attr.state_enabled}, drawable);
        return this;
    }

    public SMAStateListDrawable addStateDisabled(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_enabled}, drawable);
        return this;
    }

    /*
    Focused / Unfocused
     */
    public SMAStateListDrawable addStateFocused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable addStateUnfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_focused}, drawable);
        return this;
    }

    /*
    Windows focused / Windows unfocused
     */
    public SMAStateListDrawable addStateWindowFocused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_window_focused}, drawable);
        return this;
    }

    public SMAStateListDrawable addStateWindowUnfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_window_focused}, drawable);
        return this;
    }

    /*
    Selected / Unselected
     */
    public SMAStateListDrawable addStateSelected(Drawable drawable) {
        addState(new int[] {android.R.attr.state_selected}, drawable);
        return this;
    }

    public SMAStateListDrawable addStateUnselected(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_selected}, drawable);
        return this;
    }

    /*
    Pressed / Unpressed
     */
    public SMAStateListDrawable addStatePressed(Drawable drawable) {
        addState(new int[] {android.R.attr.state_pressed}, drawable);
        return this;
    }

    public SMAStateListDrawable addStateUnpressed(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_pressed}, drawable);
        return this;
    }

    /*
    Checked / Unchecked
     */
    public SMAStateListDrawable addStateChecked(Drawable drawable) {
        addState(new int[] {android.R.attr.state_checked}, drawable);
        return this;
    }

    public SMAStateListDrawable addStateUnchecked(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_checked}, drawable);
        return this;
    }

    /*
    Default states (add it at the end because it will add all the inverse states that has not been added
     */
    public SMAStateListDrawable addComplementariesStates(Drawable drawable) {
        addState(new int[] {}, drawable);
        return this;
    }
}
