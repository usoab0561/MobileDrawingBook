package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class MainActivity2 extends AppCompatActivity {

    TextView bookname, bookartist, bookinfo,too_bar;
    DBHelper DB;
    ImageThread imageThread;
    ImageView imagemain3;
    Bitmap imageDB;
    String[] str = new String[3];
    // 원본 보기 버튼
    Button original;
    Button user;

    // intent로 받은 data
    String book_title;

    /*Intent intent1 = getIntent();
    String userID = intent1.getStringExtra("user");
    String userPass = intent1.getStringExtra("pass");*/
    Intent intent1;
    String userID;
    String userPass;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // intent extra
        Intent intent = getIntent();
        book_title = intent.getStringExtra("book_title");

        // db connection
        DB = new DBHelper(this);
        str = new String[4];
        String strname = "";
        String strartist= "저자 : ";
        String strinfo ="";
        str = DB.getBookInfo2(book_title);
        strname += str[0]; //  책 제목
        strartist += str[1]; // 저자
        strinfo += str[2]; // 책 정보
        String url = str[3]; // 책의 url 정보

        // xml view setting
        too_bar = (TextView) findViewById(R.id.toolbar_title);
        bookname = (TextView) findViewById(R.id.bookname1);
        bookartist = (TextView) findViewById(R.id.bookartist1);
        bookinfo = (TextView) findViewById(R.id.bookinfo1);
        bookinfo.setMovementMethod(new ScrollingMovementMethod());
        imagemain3 = (ImageView) findViewById(R.id.imagemain3);
        original = (Button) findViewById(R.id.button2);
        user = (Button) findViewById(R.id.button);

        // image setting
        imageThread = new ImageThread();
        imageThread.ImageThread(url, imagemain3);


        intent1 = getIntent();
        userID = intent1.getStringExtra("user");
        userPass = intent1.getStringExtra("pass");

        bookname.setText(strname);
        bookartist.setText(strartist);
        bookinfo.setText(strinfo);
        too_bar.setText(strname);


        // 원본 보기 버튼 누를시 발생하는 클릭 이벤트
        original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OriginalBooks.class);
                intent.putExtra("title", str[0]);
                intent.putExtra("user", userID);
                intent.putExtra("pass", userPass);
                startActivity(intent);
            }
        });

        // 사용자 보기 버튼 누를시 발생하는 클릭 이벤트
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), activity_user.class);
                intent.putExtra("title", str[0]);
                intent.putExtra("user", userID);
                intent.putExtra("pass", userPass);
                startActivity(intent);
            }
        });

    }
    //DBHelper로 넣었음.

    public void arrowbtnClicked(View view) {

        Intent intent = new Intent(this, MainActivity3.class); // this에서 edited_books로 간다.
        intent.putExtra("user", userID);
        intent.putExtra("pass", userPass);
        startActivity(intent);
    }
    public void closebtnClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class); // this에서 edited_books로 간다.
        startActivity(intent);
    }
}