package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Books extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        TextView title = findViewById(R.id.toolbar_title); // 타이틀 가져오기
        title.setText("책장");
    }
    public void arrowbtnClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class); // this에서 edited_books로 간다.
        startActivity(intent);
    }
    public void closebtnClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class); // this에서 edited_books로 간다.
        startActivity(intent);
    }
}