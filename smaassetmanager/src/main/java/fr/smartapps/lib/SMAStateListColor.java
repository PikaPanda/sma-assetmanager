package fr.smartapps.lib;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 *
 */
public class SMAStateListColor extends ColorStateList {

    /*
    Attributes
     */
    int[][] states = null;
    int[] colors = null;
    int count = 0;

    /*
    Constructor
     */
    public SMAStateListColor() {
        super(null, null);
        states = new int[count][count];
        colors = new int[count];
    }

    /*
    Enabled / Disabled
     */
    public SMAStateListColor addStateEnabled(String color) {
        count++;
        states[count][count] = new int[] { android.R.attr.state_pressed    };
        return this;
    }

    public SMAStateListColor addStateDisabled(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_enabled}, drawable);
        return this;
    }

    /*
    Focused / Unfocused
     */
    public SMAStateListColor addStateFocused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_focused}, drawable);
        return this;
    }

    public SMAStateListColor addStateUnfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_focused}, drawable);
        return this;
    }

    /*
    Windows focused / Windows unfocused
     */
    public SMAStateListColor addStateWindowFocused(Drawable drawable) {
        addState(new int[] {android.R.attr.state_window_focused}, drawable);
        return this;
    }

    public SMAStateListColor addStateWindowUnfocused(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_window_focused}, drawable);
        return this;
    }

    /*
    Selected / Unselected
     */
    public SMAStateListColor addStateSelected(Drawable drawable) {
        addState(new int[] {android.R.attr.state_selected}, drawable);
        return this;
    }

    public SMAStateListColor addStateUnselected(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_selected}, drawable);
        return this;
    }

    /*
    Pressed / Unpressed
     */
    public SMAStateListColor addStatePressed(Drawable drawable) {
        addState(new int[] {android.R.attr.state_pressed}, drawable);
        return this;
    }

    public SMAStateListColor addStateUnpressed(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_pressed}, drawable);
        return this;
    }

    /*
    Checked / Unchecked
     */
    public SMAStateListColor addStateChecked(Drawable drawable) {
        addState(new int[] {android.R.attr.state_checked}, drawable);
        return this;
    }

    public SMAStateListColor addStateUnchecked(Drawable drawable) {
        addState(new int[] {-android.R.attr.state_checked}, drawable);
        return this;
    }

    /*
    Default states (add it at the end because it will add all the inverse states that has not been added
     */
    public SMAStateListColor addStateDefault(Drawable drawable) {
        addState(new int[] {}, drawable);
        return this;
    }
}
