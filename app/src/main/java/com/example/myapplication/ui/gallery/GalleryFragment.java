package com.example.myapplication.ui.gallery;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.DBHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class GalleryFragment extends Fragment {
    Button btnlogin;
    EditText username, password;
    DBHelper DB;
    private GalleryViewModel galleryViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        username = root.findViewById(R.id.username1);
        password = root.findViewById(R.id.password1);
        btnlogin = root.findViewById(R.id.btnsignin1);
        DB = new DBHelper(getActivity());
        btnlogin = root.findViewById(R.id.btnsignin1);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(getActivity(), "모든 필드에 값을 입력하시오.", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass == true){
                        Toast.makeText(getActivity(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), com.example.myapplication.MainActivity3.class);
                        intent.putExtra("user", user);
                        intent.putExtra("pass", pass);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getActivity(), "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return root;
    }
}

