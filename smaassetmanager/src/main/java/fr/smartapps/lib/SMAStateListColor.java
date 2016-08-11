package fr.smartapps.lib;

import android.content.res.ColorStateList;
import android.graphics.Color;

/**
 *
 */
public class SMAStateListColor {

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
        states = new int[count][count];
        colors = new int[count];
    }

    /*
    Enabled / Disabled
     */
    public SMAStateListColor enabled(String color) {
        count++;
        states[count] = new int[] {android.R.attr.state_enabled};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public SMAStateListColor disabled(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_enabled};
        colors[count] = Color.parseColor(color);
        return this;
    }

    /*
    Focused / Unfocused
     */
    public SMAStateListColor focused(String color) {
        count++;
        states[count] = new int[] {android.R.attr.state_focused};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public SMAStateListColor unfocused(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_focused};
        colors[count] = Color.parseColor(color);
        return this;
    }

    /*
    Windows focused / Windows unfocused
     */
    public SMAStateListColor windowFocused(String color) {
        count++;
        states[count] = new int[] {android.R.attr.state_window_focused};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public SMAStateListColor windowUnfocused(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_window_focused};
        colors[count] = Color.parseColor(color);
        return this;
    }

    /*
    Selected / Unselected
     */
    public SMAStateListColor selected(String color) {
        count++;
        states[count] = new int[] {android.R.attr.state_selected};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public SMAStateListColor unselected(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_selected};
        colors[count] = Color.parseColor(color);
        return this;
    }

    /*
    Pressed / Unpressed
     */
    public SMAStateListColor pressed(String color) {
        count++;
        states[count] = new int[] {android.R.attr.state_pressed};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public SMAStateListColor unpressed(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_pressed};
        colors[count] = Color.parseColor(color);
        return this;
    }

    /*
    Checked / Unchecked
     */
    public SMAStateListColor checked(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_checked};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public SMAStateListColor unchecked(String color) {
        count++;
        states[count] = new int[] {-android.R.attr.state_checked};
        colors[count] = Color.parseColor(color);
        return this;
    }

    /*
    Default states (add it at the end because it will add all the inverse states that has not been added
     */
    public SMAStateListColor inverse(String color) {
        count++;
        states[count] = new int[] {};
        colors[count] = Color.parseColor(color);
        return this;
    }

    public ColorStateList create() {
        return new ColorStateList(states, colors);
    }
}
