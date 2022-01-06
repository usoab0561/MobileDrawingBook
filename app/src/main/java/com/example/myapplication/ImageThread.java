package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageThread extends AppCompatActivity {

    Bitmap image;

    public void ImageThread(String url_string, ImageView imageView){

        Thread myThread = new Thread() {
            public void run() {
                try {
                    //System.out.println("thread 정의 중");
                    URL url;
                    url = new URL(url_string);

                    // http 연결
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    // 해당 사진 파일 Stream으로 받아오기
                    InputStream is = conn.getInputStream();
                    image = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        //System.out.println("thread 시작");
        myThread.start();

        try{
            myThread.join();

            imageView.setImageBitmap(image);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public Bitmap Image_Bitmap(String url_string){

        // Thread 정의
        Thread myThread = new Thread(){
            public void run(){

                try {
                    URL url;
                    System.out.println("url print");
                    System.out.println(url_string);

                    url = new URL(url_string);

                    // http 연결
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    // 해당 사진 파일 Stream으로 받아오기
                    InputStream is = conn.getInputStream();
                    image = BitmapFactory.decodeStream(is);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };

        //System.out.println("thread 시작");
        myThread.start();

        try{
            myThread.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("image return");
        return image;

    }

}
