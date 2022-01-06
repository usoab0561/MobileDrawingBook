package com.example.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**

 * Canvas를 저장하거나 호출하는 클래스입니다.
 * */

public class CanvasIO {
    private final static String TAG = Canvas.class.getName();
    private final static String FILE_NAME = "draw_file.jpg";

    public static void saveBitmap(Context context, Bitmap saveFile) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            saveFile.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "Don't save canvas");
            e.printStackTrace();
        }
    }

    public static Bitmap openBitmap(Context context) {
        Bitmap result = null;
        
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            result = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (IOException e) {
            Log.e(TAG, "Don't open canvas");
            e.printStackTrace();
        }
        
        return result;
    }
}
