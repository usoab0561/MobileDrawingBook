package com.example.myapplication.ui.BookList;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import com.example.myapplication.ImageThread;
import com.example.myapplication.DBHelper;
import com.example.myapplication.EditedLookup;
import com.example.myapplication.MainActivity1;
import com.example.myapplication.MainActivity3;
import com.example.myapplication.R;
import com.example.myapplication.SharedViewModel;
import com.example.myapplication.ui.BookList.BookViewModel;

import java.util.ArrayList;

public class BBookList extends Fragment {
    String username;
    String password;
    ArrayList<String> bookname;
    DBHelper DB;
    Button next, previous;
    TextView textView, textview2;
    Button show;
    BookViewModel BookViewModel;
    int num = 0;
    int size;

    ImageView imageView;
    ImageThread imageThread;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getUserData().observe(getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        username = s;
                        //System.out.print(username);

                    }

                });
        model.getPassData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password = s;
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //BookViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        View root = inflater.inflate(R.layout.activity_edit, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        /*profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        previous = (Button) root.findViewById(R.id.previous_button);
        show = (Button) root.findViewById(R.id.button4);
        next = (Button) root.findViewById(R.id.next_button);
        textView  = (TextView) root.findViewById(R.id.textView9);
        textview2 = (TextView) root.findViewById(R.id.textView12);
        imageView = (ImageView) root.findViewById(R.id.imagemain5);
        imageThread = new ImageThread();

        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getUserData().observe(getViewLifecycleOwner(),
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {

                        username = s;
                        textView.setText(s +" 님");
                        DB = new DBHelper(getActivity());
                        bookname = DB.getuserbook(s);
                        textview2.setText(bookname.get(num));
                        String book_title = bookname.get(num);

                        // url 가져오기
                        String url = DB.getBookInfo2(book_title)[3];
                        imageThread.ImageThread(url,imageView);

                        size = bookname.size();

                        next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                size --;
                                num ++;
                                if(size > 1) {
                                    textview2.setText(bookname.get(num));

                                    String book_title = bookname.get(num);

                                    // url 가져오기
                                    String url = DB.getBookInfo2(book_title)[3];
                                    imageThread.ImageThread(url,imageView);

                                }
                                if(size == 1) {
                                    next.setText("끝");
                                    textview2.setText(bookname.get(num));

                                    String book_title = bookname.get(num);

                                    // url 가져오기
                                    String url = DB.getBookInfo2(book_title)[3];
                                    imageThread.ImageThread(url,imageView);
                                }

                            }

                        });

                        show.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    //next.setText("다음");
                                    Intent intent = new Intent(getActivity(), EditedLookup.class);
                                    intent.putExtra("user", username);
                                    intent.putExtra("bookname", bookname.get(num));

                                    startActivity(intent);
                                }
                        });


                        previous.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                size++;
                                num--;

                                if(num >= 0){
                                    next.setText("다음");
                                    textview2.setText(bookname.get(num));

                                    String book_title = bookname.get(num);

                                    // url 가져오기
                                    String url = DB.getBookInfo2(book_title)[3];
                                    imageThread.ImageThread(url,imageView);
                                }

                            }
                        });
                    }
                    });



        return root;
    }

}
/*
show.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

        Intent intent = new Intent(getActivity(), EditedLookup.class);
        intent.putExtra("user", username);
        intent.putExtra("bookname", bookname.get(num));
        startActivity(intent);
        }

        });
        previous.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        num--;

        if (num >= 0) {
        textview2.setText(bookname.get(num));
        }

        }
        });*/