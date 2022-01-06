package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
//import android.view.View;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class OriginalBooks extends AppCompatActivity{

    // DB 선언 && image thread 선언
    DBHelper DB;
    ImageThread imageThread;

    // xml 파일 요소 선언
    Bitmap imageDB;
    TextView textView;
    Button next, previous;
    ImageView imageView;

    // DB에서 가져온 데이터 저장 변수 선언
    String contents;
    String book_title;
    String url;
    // image url setting

    // 책의 page를 가르키는 포인터
    int page = 1;
    int max_page=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original);

        // intent로 전달한 정보 가져옴
        Intent intent = getIntent();
        book_title = intent.getStringExtra("title");
        System.out.println(intent.getStringExtra("title"));

        // toolbar_title 설정
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(book_title);
        // textView & Button 설정
        textView = (TextView) findViewById(R.id.text);
        textView.setMovementMethod(new ScrollingMovementMethod());
        imageView = (ImageView) findViewById(R.id.basic_img);
        next = (Button) findViewById(R.id.next_button);
        previous = (Button) findViewById(R.id.previous_button);

        // 1. contents 불러오기 => 초기값
        // db setting && image thread setting
        DB = new DBHelper(this);
        imageThread = new ImageThread();

        // get data from Database
        contents = DB.getBookContents(book_title, page)[0];
        url =  DB.getBookUrl(book_title, page)[0];
        imageThread.ImageThread(url,imageView);


        textView.setText(contents);
        max_page = DB. getMaxPage(book_title);
        //System.out.println(max_page);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                if(page < max_page+1 && page != max_page){
                    // contents 가져오기
                    contents = DB.getBookContents(book_title, page)[0];
                    textView.setText(contents);
                    // url 가져오기
                    url = DB.getBookUrl(book_title, page)[0];
                    imageThread.ImageThread(url,imageView );

                }
                else if(page == max_page) {
                    contents = DB.getBookContents(book_title, page)[0];
                    url =  DB.getBookUrl(book_title, page)[0];
                    imageThread.ImageThread(url,imageView);
                    textView.setText(contents);
                    next.setText("완료");
                }
                // 다 읽었으므로 다시 추천 page로
                else {
                    //System.out.println("끝");
                    Intent myintent = new Intent(getApplicationContext(), MainActivity1.class);
                    startActivity(myintent);
                    /*Intent myintent = new Intent(getApplicationContext(),  MainActivity1.class);
                    startActivity(myintent);*/
                    Intent intent1 = getIntent();
                    String userID = intent1.getStringExtra("user");
                    String userPass = intent1.getStringExtra("pass");
                    Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                    intent.putExtra("user", userID);
                    intent.putExtra("pass", userPass);
                    startActivity(intent);
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page--;

                if(page >= 1){
                    contents = DB.getBookContents(book_title, page)[0];
                    textView.setText(contents);
                    // url 가져오기
                    url = DB.getBookUrl(book_title, page)[0];
                    imageThread.ImageThread(url,imageView );
                    /*
                    task = new ImageLoadTask(url, imageView);
                    task.execute();
                     */
                }

            }
        });

    }

}
