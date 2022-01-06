package com.example.myapplication.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;

public class SlideshowFragment extends Fragment {
    EditText username, name, password, repassword;
    Button signup;
    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        DBHelper DB;



        username = root.findViewById(R.id.username);
        name = root.findViewById(R.id.name);
        password = root.findViewById(R.id.password);
        repassword = root.findViewById(R.id.repassword);
        signup = root.findViewById(R.id.btnsignup);




        DB = new DBHelper(getActivity());

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String namee = name.getText().toString();
                String repass = repassword.getText().toString();
                if(user.equals("") || pass.equals("") || repass.equals("") || namee.equals(""))
                    Toast.makeText(getActivity(), "모든 필드에 값을 입력하시오.", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser == false){
                            Boolean insert = DB.insertData(user, pass, namee);
                            if(insert == true){
                                Toast.makeText(getActivity(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), com.example.myapplication.MainActivity1.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getActivity(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getActivity(), "이미 존재하는 회원! 로그인 페이지로 이동하세요",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return root;
    }
}