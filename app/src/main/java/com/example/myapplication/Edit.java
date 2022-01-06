package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.ui.home.HomeViewModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Edit extends AppCompatActivity {
    //ArrayList<String> bookname = new ArrayList<String>();
    String username;
    String bookname;
    DBHelper DB;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DB = new DBHelper(this);
        setContentView(R.layout.activity_edit);
        TextView title = findViewById(R.id.toolbar_title); // 타이틀 가져오기
        Intent intent = getIntent();
        username = intent.getStringExtra("user");
        System.out.println("이름" +username);
        textView  = (TextView) findViewById(R.id.textView9);
        //bookname = DB.getuserbook(username);
        //String name = DB.getuserbook(username);
        System.out.println("test" + bookname);
        textView.setText(bookname);
        //bookname = DB.getuserbook(username);
        title.setText("내가 그린 책 목록");



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditedLookup.class);
                intent.putExtra("user", username);
                intent.putExtra("bookname",bookname);
                startActivity(intent);
            }
        });

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