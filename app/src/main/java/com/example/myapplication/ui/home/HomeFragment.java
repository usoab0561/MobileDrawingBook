package com.example.myapplication.ui.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Edit;
import com.example.myapplication.DBHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MainActivity2;
import com.example.myapplication.R;
import com.example.myapplication.activity_user;

import com.example.myapplication.util.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.ContentValues.TAG;
import com.example.myapplication.SharedViewModel;

public class HomeFragment extends Fragment{
    Button user;

    private HomeViewModel homeViewModel;
    DBHelper DB;

    // recycleview Setting
    RecyclerView recyclerView, recyclerView2, recyclerView3;
    RecyclerViewAdapter recyclerViewAdapter, recyclerViewAdapter2, recyclerViewAdapter3;
    // db로 부터 가져온 list개수
    int data_count = 0;

    // loading boolean
    boolean isLoading = false;

    // recycle에 setting 할 list
    ArrayList<String[]> All_list = new ArrayList<String[]>();
    ArrayList<String[]> pre_list = new ArrayList<String[]>();
    ArrayList<String[]> pre_list2 = new ArrayList<String[]>();
    ArrayList<String[]> pre_list3 = new ArrayList<String[]>();

    String username, password;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getUserData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                username = s;
            }
        });
        model.getPassData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password = s;
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // fragmet_hom xml
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // view_setting ** final: 엔티티를 한번 만 할당
        final TextView textView = root.findViewById(R.id.text_home);
        recyclerView = root.findViewById(R.id.recyclerview);
        // Context가 현재 activity
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true)); // 좌우 scroll true

        recyclerView2 = root.findViewById(R.id.recyclerview2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true)); // 좌우 scroll true

        recyclerView3 = root.findViewById(R.id.recyclerview3);
        recyclerView3.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true)); // 좌우 scroll true

        /* db에서 data 불러오기
        DB = new DBHelper(getActivity());
        All_list = DB.getBookInfo(); // 전체 data 가져오기
         */

        System.out.println("현재 init_data 실행 시작");
        // data init
        init_data();

        // adapter init
        initAdapter();
        initAdapter2();
        initAdapter3();

        // adapter click event
        click_event();
        click_event2();
        click_event3();

        // adapter scroll
        initScrollListener();

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        /*
        user = (Button) root.findViewById(R.id.button4);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Edit.class);
                intent.putExtra("user",username);
                startActivity(intent);
            }
        });*/
        // return root
        return root;


    }


    // 초기의 data update
    private void init_data(){
        System.out.println("현재 init_data 실행 중");

        // db에서 data 불러오기
        DB = new DBHelper(getActivity());
        All_list = DB.getBookInfo(); // 전체 data 가져오기

        // pre_list update => 처음에 3개의 data update
        for (int i=0; i<10; i++){
            // all_list size보다 적게 update
            if(i == All_list.size()){
                return;
            }else{
                pre_list.add(All_list.get(i));
                pre_list2.add(All_list.get(i));
                pre_list3.add(All_list.get(i));
            }
            System.out.println(" pre_list");
            System.out.println(Arrays.deepToString(pre_list.toArray()));

        }
    }
    // 초기의 Adapter update
    private void initAdapter(){

        System.out.println("initAdapter");
        recyclerViewAdapter = new RecyclerViewAdapter(pre_list);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    // 초기의 Adapter update
    private void initAdapter2(){

        System.out.println("initAdapter");
        recyclerViewAdapter2 = new RecyclerViewAdapter(pre_list2);
        recyclerView2.setAdapter(recyclerViewAdapter2);

    }

    // 초기의 Adapter update
    private void initAdapter3(){

        System.out.println("initAdapter");
        recyclerViewAdapter3 = new RecyclerViewAdapter(pre_list3);
        recyclerView3.setAdapter(recyclerViewAdapter3);

    }

    // click event 발생시 event 처리
    private  void click_event(){
        // click_event가 발생할 시 동작 수현
        recyclerViewAdapter.setOnItemClickListener((v, position) -> {

            if(username != null){
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                // position은 adater에서 가져옴
                intent.putExtra("book_title",  pre_list.get(position)[0]);
                intent.putExtra("user", username);
                startActivity(intent);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그인하세요!!!");
                builder.show();
            }

        });
    }

    // click event 발생시 event 처리
    private  void click_event2(){
        // click_event가 발생할 시 동작 수현
        recyclerViewAdapter2.setOnItemClickListener((v, position) -> {

            if(username != null){
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                // position은 adater에서 가져옴
                intent.putExtra("book_title",  pre_list2.get(position)[0]);
                intent.putExtra("user", username);
                startActivity(intent);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그인하세요!!!");
                builder.show();
            }

        });
    }

    // click event 발생시 event 처리
    private  void click_event3(){
        // click_event가 발생할 시 동작 수현
        recyclerViewAdapter3.setOnItemClickListener((v, position) -> {

            if(username != null){
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                // position은 adater에서 가져옴
                intent.putExtra("book_title",  pre_list3.get(position)[0]);
                intent.putExtra("user", username);
                startActivity(intent);
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그인하세요!!!");
                builder.show();
            }

        });
    }

    private void dataMore(){

        pre_list.add(null);
        recyclerViewAdapter.notifyItemInserted(pre_list.size() - 1);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pre_list.remove(pre_list.size() -1); // 왜 다시 지우지..
                int scroll = pre_list.size();

                recyclerViewAdapter.notifyItemRemoved(scroll);
                int current_size = scroll;

                // 3개씩 늘림: 현재 size에서 하나씩 늘림
                for (int i = current_size; i < current_size +10 ; i++){
                    if( i < All_list.size()){
                        pre_list.add(All_list.get(i));
                    }
                }
                // data가 변했다는 것을 알림
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                return;
            }
        },2000);
    }

    private void dataMore2(){

        pre_list2.add(null);
        recyclerViewAdapter2.notifyItemInserted(pre_list2.size() - 1);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pre_list2.remove(pre_list2.size() -1); // 왜 다시 지우지..
                int scroll = pre_list2.size();

                recyclerViewAdapter2.notifyItemRemoved(scroll);
                int current_size = scroll;

                // 3개씩 늘림: 현재 size에서 하나씩 늘림
                for (int i = current_size; i < current_size +10 ; i++){
                    if( i < All_list.size()){
                        pre_list2.add(All_list.get(i));
                    }
                }
                // data가 변했다는 것을 알림
                recyclerViewAdapter2.notifyDataSetChanged();
                isLoading = false;
                return;
            }
        },2000);
    }


    private void dataMore3(){

        pre_list3.add(null);
        recyclerViewAdapter3.notifyItemInserted(pre_list3.size() - 1);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pre_list3.remove(pre_list3.size() -1); // 왜 다시 지우지..
                int scroll = pre_list3.size();

                recyclerViewAdapter3.notifyItemRemoved(scroll);
                int current_size = scroll;

                // 3개씩 늘림: 현재 size에서 하나씩 늘림
                for (int i = current_size; i < current_size +10 ; i++){
                    if( i < All_list.size()){
                        pre_list3.add(All_list.get(i));
                    }
                }
                // data가 변했다는 것을 알림
                recyclerViewAdapter3.notifyDataSetChanged();
                isLoading = false;
                return;
            }
        },2000);
    }

    private void initScrollListener(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: ");

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: ");
                // LinearLayoutManager을 불러옴
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //현재 loading 중이지 않다면,
                if(!isLoading){
                    // linearLayoutManager가 null이 아니고 size가 pre_list라면 더 data를 불러와야함, all_list 보다 pre_list가 더 적어야함
                    if(linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == pre_list.size() - 1 && All_list.size() > pre_list.size()){
                        dataMore();
                        isLoading = true;
                        Toast.makeText(getActivity(), "스크롤감지", Toast.LENGTH_SHORT).show();
                    }
                    else if( All_list.size() > pre_list.size()){
                        Toast.makeText(getActivity(), "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void initScrollListener2(){

        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: ");

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: ");
                // LinearLayoutManager을 불러옴
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //현재 loading 중이지 않다면,
                if(!isLoading){
                    // linearLayoutManager가 null이 아니고 size가 pre_list라면 더 data를 불러와야함, all_list 보다 pre_list가 더 적어야함
                    if(linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == pre_list2.size() - 1 && All_list.size() > pre_list2.size()){
                        dataMore2();
                        isLoading = true;
                        Toast.makeText(getActivity(), "스크롤감지", Toast.LENGTH_SHORT).show();
                    }
                    else if( All_list.size() > pre_list2.size()){
                        Toast.makeText(getActivity(), "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void initScrollListener3(){

        recyclerView3.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: ");

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: ");
                // LinearLayoutManager을 불러옴
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                //현재 loading 중이지 않다면,
                if(!isLoading){
                    // linearLayoutManager가 null이 아니고 size가 pre_list라면 더 data를 불러와야함, all_list 보다 pre_list가 더 적어야함
                    if(linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == pre_list3.size() - 1 && All_list.size() > pre_list3.size()){
                        dataMore3();
                        isLoading = true;
                        Toast.makeText(getActivity(), "스크롤감지", Toast.LENGTH_SHORT).show();
                    }
                    else if( All_list.size() > pre_list3.size()){
                        Toast.makeText(getActivity(), "마지막 페이지입니다.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }










}
