package com.example.myapplication.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.myapplication.ImageThread;
import com.example.myapplication.MainActivity2;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING =1;

    // thread setting
    // url을 image로 바꿔주는 Thread
    ImageThread imageThread = new ImageThread();

    // item list => image list, image ulr, book title의 2차원 배열
    public ArrayList<String[]> mItemList = new ArrayList<String[]>();

    // mItemList를 pre_list로
    public RecyclerViewAdapter(ArrayList<String[]> pre_list) {
        System.out.println("Adater java");
        this.mItemList = pre_list;
        System.out.println("mItemList");
        System.out.println(Arrays.deepToString(mItemList.toArray()));
    }


    // click_event interface
    public interface ItemClickListener{
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장
    private ItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 Adapter에 전달
    public void setOnItemClickListener(ItemClickListener listener){
        this.mListener = listener;
    }


    @NonNull
    @Override // ViewHolder를 새롭게 만들어야할 때 이 메서드를 호출 => View를 생성하고 초기화하지만 뷰의 contents는 채우지 않음
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override //  RecyclerView ViewHoder를 데이터와 연결
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // 변수 선언
        String title;
        String url;
        // imageView를 위한 Bitmap
        Bitmap image;
        // db와 연결
        if(holder instanceof  ItemViewHolder){

            System.out.println("onBindViewHolder_mItemList");
            System.out.println(Arrays.deepToString(mItemList.toArray()));


            // title setting
            title = mItemList.get(position)[0];
            // url setting
            url = mItemList.get(position)[1];

            System.out.println("onBindViewHolder");
            System.out.println("title"+title+"url" + url);
            image = imageThread.Image_Bitmap(url);

            // holder setting
            ((ItemViewHolder) holder).text_view.setText(title);
            ((ItemViewHolder) holder).image_view.setImageBitmap(image);

            /*
            // set click event
            public void OnItemClickListenr(View v, int po)

             */

        }else if (holder instanceof LoadingViewHolder){
            showLoadingView((LoadingViewHolder) holder, position); // 이게 왜 필요하지?
        }

    }

    @Override // 데이터 세트 크기를 가져올 때, 이 메서드 호출
    public int getItemCount() {
        /* mItemList의 행 길이 가져오기 */
        return mItemList == null ? 0: mItemList.size();

    }
    // Item 내부의 view 초기화
    private class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView text_view;
        ImageView image_view;
        // interface 참조
        ItemClickListener itemClickListener;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            text_view = (TextView) itemView.findViewById(R.id.textView6);
            image_view = (ImageView) itemView.findViewById(R.id.image_View);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // click event 정의 => 이때 Adapter의 position알아내야함
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        // 리스너 객체의 메서드 호출
                        mListener.onItemClick(view, position);
                    }
                }
            });

        }
        // interface override
        public void onItemClick(View v){
            this.itemClickListener.onItemClick(v, getAdapterPosition());
        }


    }

    // loading progress bar
    private class LoadingViewHolder extends ViewHolder {

        ProgressBar progressbar;

        public LoadingViewHolder(View view) {
            super(view);
            progressbar = itemView.findViewById(R.id.progressBar);


        }
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //
    }
}
