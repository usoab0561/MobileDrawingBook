package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    EditText username, name, password, repassword;
    Button btnupdate;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView title = findViewById(R.id.toolbar_title); // 타이틀 가져오기
        title.setText("프로필");
        username = (EditText)findViewById(R.id.username2);
        name = (EditText)findViewById(R.id.name2);
        password = (EditText)findViewById(R.id.password2);
        btnupdate = (Button)findViewById(R.id.btnupdate);
        repassword = (EditText)findViewById(R.id.repassword2);
        DB = new DBHelper(this);

        Intent intent = getIntent();

        String userID = intent.getStringExtra("user");
        String userPass = intent.getStringExtra("pass");

        username.setText(userID);
        password.setText(userPass);
        String nam = DB.getName(userID);
        name.setText(nam);

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = name.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if(pass.equals(repass))
                {
                    Boolean checkupdate = DB.updatedata(userID, pass, namee);
                    if(checkupdate==true)
                    {
                        Intent intent = new Intent(getApplicationContext(), com.example.myapplication.Edited_books.class);
                        startActivity(intent);
                        Toast.makeText(com.example.myapplication.ProfileActivity.this, "프로필 수정 성공", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(com.example.myapplication.ProfileActivity.this, "프로필 수정 실패", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(com.example.myapplication.ProfileActivity.this, "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
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