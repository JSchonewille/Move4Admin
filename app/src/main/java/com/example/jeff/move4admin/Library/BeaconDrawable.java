package com.example.jeff.move4admin.Library;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Jeff on 18-12-2014.
 */
public class BeaconDrawable {
    private ImageView imageView;
    private int major;
    private int minor;
    private  int x;
    private  int y;

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
