package com.example.marco.hw2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
class PicUtils {

    public static Bitmap decodePic(String picPath, int i) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scale = Math.max(photoW,photoH) / i;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scale;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(picPath,bmOptions);
    }

    public static Bitmap decodePic(String picPath, int widht,int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picPath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scale = 4;

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scale;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(picPath,bmOptions);
    }
}
