package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    ImageView imageView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //TextView title = findViewById(R.id.toolbar_title); 타이틀 가져오기
        //title.setText("내가 그린 기린 그림은?");  title, 타이틀, 제목

        TextView textView = (TextView) findViewById(R.id.textView);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity1.class);
                startActivity(intent);

            }
        });


    }

    public void arrowbtnClicked(View view) {
        Intent intent = new Intent(this, Edited_books.class); // this에서 edited_books로 간다.
        startActivity(intent);
//        intent.putExtra("age", 20); // 추가 메세지 보내기

    }
    public void closebtnClicked(View view) {
        Intent intent = new Intent(this, Edited_books.class); // this에서 edited_books로 간다.
        startActivity(intent);
//        intent.putExtra("age", 20); // 추가 메세지 보내기
    }


}//헬로 ㅎㅎ 민혜지원호진의신 헬로헬로 ㅇㅇㅇㅇㅇㅇㅇ ㅋㅋ😛