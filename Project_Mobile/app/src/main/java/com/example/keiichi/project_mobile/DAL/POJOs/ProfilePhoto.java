package com.example.keiichi.project_mobile.DAL.POJOs;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.Serializable;

public class ProfilePhoto implements Serializable {

    private String id;
    private int height;
    private int width;
    private byte[] imageData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Bitmap getImage()
    {
        Bitmap ImageView = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        return ImageView;

    }
}
