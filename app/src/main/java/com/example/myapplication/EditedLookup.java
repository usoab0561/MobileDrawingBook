package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ui.BookList.BBookList;

import java.util.ArrayList;

public class EditedLookup extends AppCompatActivity {
    TextView textView;
    DBHelper DB;
    ImageView imagemain3;
    Bitmap imageDB;
    Button next, previous;
    String book_title,username;
    static Bitmap basic_image;
    int max_page;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_lookup);
        DB = new DBHelper(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("user");
        book_title = intent.getStringExtra("bookname");
        TextView title = findViewById(R.id.toolbar_title); // 타이틀 가져오기
        next = (Button) findViewById(R.id.next_button);
        title.setText("내가 그린 책 조회하기");
        imagemain3 = (ImageView) findViewById(R.id.imagemain4);
        max_page = DB. getMaxPage2(book_title,username);
        basic_image = DB.getImageEdited2(username,book_title, 1);
        imagemain3.setImageBitmap(basic_image);
        previous = (Button) findViewById(R.id.previous_button);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page++;
                if(page < max_page+1 && page != max_page){
                    // contents 가져오기
                    basic_image = DB.getImageEdited2(username, book_title, page);
                    imagemain3.setImageBitmap(basic_image);
                    // url 가져오기
                }
                else if(page == max_page) {
                    basic_image = DB.getImageEdited2(username, book_title, page);
                    imagemain3.setImageBitmap(basic_image);
                    next.setText("완료");
                }
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
                    next.setText("다음");
                    basic_image = DB.getImageEdited2(username, book_title, page);
                    imagemain3.setImageBitmap(basic_image);
                }

            }
        });
        //imagemain3.setImageBitmap(basic_image);
    }

}