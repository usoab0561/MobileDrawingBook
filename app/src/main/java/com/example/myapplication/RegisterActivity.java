package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText username, name, password, repassword;
    Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView title = findViewById(R.id.toolbar_title); // 타이틀 가져오기
        title.setText("회원가입");
        DBHelper DB;



        username = (EditText) findViewById(R.id.username);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);




        DB = new DBHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String namee = name.getText().toString();
                String repass = repassword.getText().toString();
                if(user.equals("") || pass.equals("") || repass.equals("") || namee.equals(""))
                    Toast.makeText(com.example.myapplication.RegisterActivity.this, "모든 필드에 값을 입력하시오.", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser == false){
                            Boolean insert = DB.insertData(user, pass, namee);
                            if(insert == true){
                                Toast.makeText(com.example.myapplication.RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(com.example.myapplication.RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(com.example.myapplication.RegisterActivity.this, "이미 존재하는 회원! 로그인 페이지로 이동하세요",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(com.example.myapplication.RegisterActivity.this, "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
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