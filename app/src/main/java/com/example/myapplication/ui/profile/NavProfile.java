package com.example.myapplication.ui.profile;

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
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.ui.profile.ProfileViewModel;

/*
SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getUserData().observe(getViewLifecycleOwner(), new Observer<String>() {
@Override
public void onChanged(String s) {
        userID += s;
        }
        });
        model.getPassData().observe(getViewLifecycleOwner(), new Observer<String>() {
@Override
public void onChanged(String s) {
        userPass += s;
        }
        });
*/


public class NavProfile extends Fragment {
    EditText username, name, password, repassword;
    Button btnupdate;
    DBHelper DB;
    String userID = "";
    String userPass = "";
    private ProfileViewModel profileViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getUserData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                username.setText(s);
            }
        });
        model.getPassData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password.setText(s);
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.activity_profile, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        /*profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        username = root.findViewById(R.id.username2);
        name = root.findViewById(R.id.name2);
        password = root.findViewById(R.id.password2);
        btnupdate = root.findViewById(R.id.btnupdate);
        repassword = root.findViewById(R.id.repassword2);
        DB = new DBHelper(getActivity());
        //Intent intent = getIntent();
        /*Bundle extra = getArguments();

        if(extra != null){
            userID += extra.getString("user");
            userPass += extra.getString("pass");
        }*/
        //String userID = intent.getStringExtra("user");
        //String userPass = intent.getStringExtra("pass");

        //username.setText(userID);
        //password.setText(userPass);
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
                        Intent intent = new Intent(getActivity(), com.example.myapplication.Edited_books.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "프로필 수정 성공", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getActivity(), "프로필 수정 실패", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return root;
    }
}
