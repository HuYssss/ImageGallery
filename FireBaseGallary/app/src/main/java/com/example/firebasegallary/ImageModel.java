package com.example.firebasegallary;

import android.graphics.Bitmap;

public class ImageModel {
    Bitmap bitmap;

    public ImageModel(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
