package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;

import com.bumptech.glide.Glide;
import com.example.myapplication.util.CanvasIO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class activity_user extends AppCompatActivity {
    TextView textView, textView2;
    ImageView imageView;
    DBHelper DB;
    String username;
    int page = 1;
    int max_page=0;
    String book_title;
    String contents;
    String content2;
    String imgurl1;
    Button next, previous;
    Animation anim;
    boolean start;
    static Bitmap basic_image;
    static final int FIXCOUNT = 1000;
    static int threadcount = 0;
    static Bitmap resize_bitmap;


    private activity_user.DrawCanvas drawCanvas;
    private FloatingActionButton fbPen;                         //펜 모드 버튼
    private FloatingActionButton fbPen_RED;                     //펜 모드 버튼
    private FloatingActionButton fbPen_ORANGE;                  //펜 모드 버튼
    private FloatingActionButton fbPen_YELLOW;                  //펜 모드 버튼
    private FloatingActionButton fbPen_GREEN;                   //펜 모드 버튼
    private FloatingActionButton fbPen_BLUE;                    //펜 모드 버튼
    private FloatingActionButton fbPen_VIOLET;                  //펜 모드 버튼
    private FloatingActionButton fbPen_PURPLE;                  //펜 모드 버튼
    private FloatingActionButton fbPen_WHITE;                   //펜 모드 버튼
    private FloatingActionButton fbPen_BLACK;                   //펜 모드 버튼

    private FloatingActionButton fbEraser;                      //지우개 모드 버튼
 // private FloatingActionButton fbSave;                        //그림 저장 버튼
    private FloatingActionButton fbOpen;                        //그림 호출 버튼
    private ConstraintLayout canvasContainer;                   //캔버스 root view

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        findId();
        canvasContainer.addView(drawCanvas);
        DB = new DBHelper(this);


        Intent intent = getIntent();
        book_title = intent.getStringExtra("title");
        System.out.println(intent.getStringExtra("title"));

        // 사용자 이름
        username = intent.getStringExtra("user");

        System.out.println(username);
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(book_title);

        textView  = (TextView) findViewById(R.id.textView4);
        textView .setMovementMethod(new ScrollingMovementMethod());
        textView2  = (TextView) findViewById(R.id.textView7);
        imageView = (ImageView) findViewById(R.id.basic_img2);
        next = (Button) findViewById(R.id.next_button);
        previous = (Button) findViewById(R.id.previous_button);

        DB = new DBHelper(this);
        if(DB.getBookContentCheck(book_title,page) == 1) {
            contents = DB.getBookContents(book_title, page)[0];
            content2= DB.getBookContents(book_title, page)[1];
        }else{
            contents = DB.getBookContents(book_title, page)[0];
            content2= " ";
        }

        if(DB.check(username,book_title,page)== 1){ // 수정된 이미지 있을때만 불러옴
            basic_image = DB.getImageEdited(username,book_title,page);
        }


        textView .setText(contents);
        textView2.setText(content2);
        max_page = DB. getMaxPage(book_title);
        setOnClickListener();

        // call thread
        BackgroundThread st[] = new BackgroundThread[max_page+1];
        for(int i = 0; i<max_page+1; i ++){
            st[i] = new BackgroundThread();
        }

        // 첫 수정 페이지가 없을때는 thread 생성
        if(DB.check(username,book_title,page) == 0){
            Thread thread = new Thread(st[page]);
            thread.start(); // Thread 실행

            try {
                // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                thread.join();

                // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // 책 내용 저장. 책 내용 있는지 check먼저하고, 있으면 새롭게 저장.
                if(DB.check(username,book_title,page)==1){ // PK막기위해 update
                    Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] img = byteArray.toByteArray();
                    DB.insertuserImage2(username, book_title,page, img);  // 여기도 str로 하기
                }else{
                    drawCanvas.invalidate();
                    Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] img = byteArray.toByteArray();
                    DB.insertuserImage(username, book_title,page, img);  // 여기도 str로 하기
                }



                page++;
                if(page < max_page+1 && page != max_page){
                    // 글쓰기 내용이있으면 채크해서 확인
                    if(DB.getBookContentCheck(book_title,page) == 1) {
                        contents = DB.getBookContents(book_title, page)[0];
                        content2= DB.getBookContents(book_title, page)[1];
                    }else{
                        contents = DB.getBookContents(book_title, page)[0];
                        content2= " ";
                    }
                    // 이미지 확인해서 있으면 username에서, 없으면 thread에서 가져오기
                    if(DB.check(username,book_title,page)== 1){
                        basic_image = DB.getImageEdited(username,book_title,page);
                    }
                    else{
                        Thread thread = new Thread(st[page]);

                        thread.start(); // Thread 실행

                        try {
                            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                            thread.join();

                            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
//            ivImage.setImageBitmap(bitmaps);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    textView.setText(contents);
                    textView2.setText(content2);
                    textView2.startAnimation(anim);

                    drawCanvas.invalidate();        // 현재 canvas 갱신
                    drawCanvas.init();              // 현재 canvas 초기화. 그림 없어지게
                }
                // 마지막 페이지입니다. 마지막 페이지 Image view setting한 후 버튼을 완료하기 버튼으로 바꾼것
                else if(page == max_page) {
                    if(DB.getBookContentCheck(book_title,page) == 1) {
                        contents = DB.getBookContents(book_title, page)[0];
                        content2= DB.getBookContents(book_title, page)[1];
                    }else{
                        contents = DB.getBookContents(book_title, page)[0];
                        content2= " ";
                    }
                    if(DB.check(username,book_title,page)== 1){
                        basic_image = DB.getImageEdited(username,book_title,page);
                    }
                    else{
                        Thread thread = new Thread(st[page]);

                        thread.start(); // Thread 실행

                        try {
                            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                            thread.join();

                            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
//            ivImage.setImageBitmap(bitmaps);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    textView.setText(contents);
                    textView2.setText(content2);
                    drawCanvas.invalidate();
                    drawCanvas.init();
                    next.setText("완료");

                }
                // 다 읽었으므로 다시 추천 page로
                else {
                    Intent intent1 = getIntent();
                    String userID = intent1.getStringExtra("user");
                    String userPass = intent1.getStringExtra("pass");
                    Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                    intent.putExtra("user", userID);
                    intent.putExtra("pass", userPass);
                    startActivity(intent);
                }
            }
        });

        anim = new AlphaAnimation(0.0f,1.0f);
        anim.setDuration(300);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(10);

        // 이전 버튼을 누를 시 발생하는 event 처리
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 책 내용 저장. 책 내용 있는지 check먼저하고, 있으면 새롭게 저장.
                if(DB.check(username,book_title,page)==1){ // PK막기위해 update
                    Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] img = byteArray.toByteArray();
                    DB.insertuserImage2(username, book_title,page, img);  // 여기도 str로 하기
                }else{
                    drawCanvas.invalidate();
                    Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
                    byte[] img = byteArray.toByteArray();
                    DB.insertuserImage(username, book_title,page, img);  // 여기도 str로 하기
                }

                page--;

                if(page >= 1){
                    if(DB.getBookContentCheck(book_title,page) == 1) {
                        contents = DB.getBookContents(book_title, page)[0];
                        content2= DB.getBookContents(book_title, page)[1];
                    }else{
                        contents = DB.getBookContents(book_title, page)[0];
                        content2= " ";
                    }
                    if(DB.check(username,book_title,page)== 1){
                        basic_image = DB.getImageEdited(username,book_title,page);
                    }
                    else{
                        Thread thread = new Thread(st[page]);

                        thread.start(); // Thread 실행

                        try {
                            // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                            // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                            thread.join();

                            // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                            // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
//            ivImage.setImageBitmap(bitmaps);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    textView.setText(contents);
                    textView2.setText(content2);
                    drawCanvas.invalidate();
                    drawCanvas.init();
                }
                if(page == 0){  // for fix cursor
                    page = 1;
                }
            }
        });

    }

    class BackgroundThread extends Thread { // 스레드에서 bitmaps를 불러온다. 그래서 표츌 해 준다.
        @Override
        public void run() {
            try {
                imgurl1 = DB.getBookContentsUrl(book_title,page)[0];
                URL url = new URL(imgurl1);

                // Web에서 이미지를 가져온 뒤
                // ImageView에 지정할 Bitmap을 만든다
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // 서버로 부터 응답 수신
                conn.connect();

                InputStream is = conn.getInputStream(); // InputStream 값 가져오기
                basic_image = BitmapFactory.decodeStream(is); // Bitmap으로 변환

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 여기서부터 그림 부분.
    public void findId() {
        canvasContainer = findViewById(R.id.lo_canvas);
        //fbPen = findViewById(R.id.fb_pen);
        fbPen_RED = findViewById(R.id.fb_pen_RED);
        fbPen_ORANGE = findViewById(R.id.fb_pen_ORANGE);
        fbPen_YELLOW = findViewById(R.id.fb_pen_YELLOW);
        fbPen_GREEN = findViewById(R.id.fb_pen_GREEN);
        fbPen_BLUE = findViewById(R.id.fb_pen_BLUE);
        fbPen_VIOLET = findViewById(R.id.fb_pen_VIOLET);
        fbPen_PURPLE = findViewById(R.id.fb_pen_PURPLE);
        fbPen_WHITE = findViewById(R.id.fb_pen_WHITE);
        fbPen_BLACK = findViewById(R.id.fb_pen_BLACK);

        fbEraser = findViewById(R.id.fb_eraser);
        //fbSave = findViewById(R.id.fb_save);
        fbOpen = findViewById(R.id.fb_open);
        drawCanvas = new activity_user.DrawCanvas(this);
    }

    /**

     * OnClickListener Setting
     */
    public void setOnClickListener() {
        /*fbPen.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN);
        });*/
        fbPen_RED.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_RED);
        });
        fbPen_ORANGE.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_ORANGE);
        });
        fbPen_YELLOW.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_YELLOW);
        });
        fbPen_GREEN.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_GREEN);
        });
        fbPen_BLUE.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_BLUE);
        });
        fbPen_VIOLET.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_VIOLET);
        });
        fbPen_PURPLE.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_PURPLE);
        });
        fbPen_WHITE.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_WHITE);
        });
        fbPen_BLACK.setOnClickListener((v)->{
            drawCanvas.changeTool(activity_user.DrawCanvas.MODE_PEN_BLACK);
        });

        fbEraser.setOnClickListener((v)->{
            drawCanvas.init();

           /* BackgroundThread st[] = new BackgroundThread[max_page+1];
            for(int i = 0; i<max_page+1; i ++){
                st[i] = new BackgroundThread();
            }

            // 첫 수정 페이지가 없을때는 thread 생성
            if(DB.check(username,book_title,page) == 0){
                Thread thread = new Thread(st[page]);
                thread.start(); // Thread 실행

                try {
                    // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                    // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                    thread.join();

                    // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                    // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

            BackgroundThread st[] = new BackgroundThread[FIXCOUNT];

            for(int i = 0; i<FIXCOUNT; i ++){
                st[i] = new BackgroundThread();
            }

            st[threadcount] = new BackgroundThread();

            Thread thread = new Thread(st[threadcount]);
            threadcount++;

            thread.start(); // Thread 실행

            try {
                // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                thread.join();

                // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
//            ivImage.setImageBitmap(bitmaps);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drawCanvas.invalidate();
            //drawCanvas.changeTool(activity_user.DrawCanvas.MODE_ERASER);
        });

  /*//      fbSave.setOnClickListener((v)->{    // 저장버튼 클릭시
*//*            drawCanvas.invalidate();
//          Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
//          CanvasIO.saveBitmap(this, saveBitmap);
            Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            saveBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] img = byteArray.toByteArray();
            boolean insert = DB.insertuserImage(username, book_title,page, img);  // 여기도 str로 하기*//*
    //    });*/

        fbOpen.setOnClickListener((v)->{    // 열기버튼 클릭시
            drawCanvas.init();
            //

            if(DB.check(username,book_title,page)== 1){
                basic_image = DB.getImageEdited(username,book_title,page);
            }
            else{
                Thread threada = new Thread();

                threada.start(); // Thread 실행

                try {
                    // 메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야한다
                    // join()를 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리게 한다
                    threada.join();

                    // 작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                    // UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지를 지정한다
//            ivImage.setImageBitmap(bitmaps);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //drawCanvas.loadDrawImage = CanvasIO.openBitmap(this);
            drawCanvas.invalidate();
        });
    }

    /**

     * Pen을 표현할 class입니다.
     */
    class Pen {
        public static final int STATE_START = 0;        //펜의 상태(움직임 시작)
        public static final int STATE_MOVE = 1;         //펜의 상태(움직이는 중)
        float x, y;                                     //펜의 좌표
        int moveStatus;                                 //현재 움직임 여부
        int color;                                      //펜 색
        int size;                                       //펜 두께

        public Pen(float x, float y, int moveStatus, int color, int size) {
            this.x = x;
            this.y = y;
            this.moveStatus = moveStatus;
            this.color = color;
            this.size = size;
        }

        /**

         * 현재 pen의 상태가 움직이는 상태인지 반환합니다.
         */
        public boolean isMove() {
            return moveStatus == STATE_MOVE;
        }
    }

    /**

     * 그림이 그려질 canvas view
     */
    class DrawCanvas extends View {
        public static final int MODE_PEN = 1;                     //모드 (펜)
        public static final int MODE_PEN_RED = 2;                     //모드 (펜)
        public static final int MODE_PEN_ORANGE = 3;                     //모드 (펜)
        public static final int MODE_PEN_YELLOW = 4;                     //모드 (펜)
        public static final int MODE_PEN_GREEN = 5;                     //모드 (펜)
        public static final int MODE_PEN_BLUE = 6;                     //모드 (펜)
        public static final int MODE_PEN_VIOLET = 7;                     //모드 (펜)
        public static final int MODE_PEN_PURPLE = 8;                     //모드 (펜)
        public static final int MODE_PEN_WHITE = 9;                     //모드 (펜)
        public static final int MODE_PEN_BLACK = 10;                     //모드 (펜)




        public static final int MODE_ERASER = 0;                  //모드 (지우개)
        final int PEN_SIZE = 7;                                   //펜 사이즈
        final int ERASER_SIZE = 30;                               //지우개 사이즈

        ArrayList<activity_user.Pen> drawCommandList;             //그리기 경로가 기록된 리스트
        Paint paint;                                              //펜
        Bitmap loadDrawImage;                                     //호출된 이전 그림
        int color;                                                //현재 펜 색상
        int size;                                                 //현재 펜 크기

        public DrawCanvas(Context context) {

            super(context);
            setBackgroundColor(Color.LTGRAY);
            Resources r = context.getResources();


            init();
        }

        public DrawCanvas(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DrawCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        /**

         * 그리기에 필요한 요소를 초기화 합니다.
         */
        private void init() {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            drawCommandList = new ArrayList<>();
            loadDrawImage = null;
            //color = Color.RED;
            color = Color.BLACK;
            size = PEN_SIZE;
        }

        /**

         * 현재까지 그린 그림을 Bitmap으로 반환합니다.
         */
        public Bitmap getCurrentCanvas() {
            Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            this.draw(canvas);
            return bitmap;
        }

        /**

         * Tool type을 (펜 or 지우개)로 변경합니다.
         * */
        private void changeTool(int toolMode) {
            if (toolMode == MODE_PEN) {
                this.color = Color.BLACK;
                size = PEN_SIZE;
            }else if (toolMode == MODE_PEN_RED){
                this.color = Color.RED;
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_ORANGE){
                this.color = Color.argb(255,255,69,0);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_YELLOW){
                this.color = Color.argb(255,255,215,0);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_GREEN){
                this.color = Color.argb(255,54,183,0);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_BLUE){
                this.color = Color.argb(255,6,17,242);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_VIOLET){
                this.color = Color.argb(255,0,0,126);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_PURPLE){
                this.color = Color.argb(255,77,0,154);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_WHITE){
                this.color = Color.WHITE;
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_BLACK){
                this.color = Color.BLACK;
                size = PEN_SIZE;
            }
            else {
                this.color = Color.WHITE;
                size = ERASER_SIZE;
            }
            paint.setColor(color);
        }


        // ㄱㄱ 사이즈 조절함
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            int width = canvasContainer.getWidth();
            int height = canvasContainer.getHeight();

            resize_bitmap = Bitmap.createScaledBitmap(basic_image, width, height, true);
            canvas.drawBitmap(resize_bitmap, 0, 0, null);

            Field field = null;
            try {
                field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            try {
                field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < drawCommandList.size(); i++) {
                activity_user.Pen p = drawCommandList.get(i);
                paint.setColor(p.color);
                paint.setStrokeWidth(p.size);

                if (p.isMove()) {
                    activity_user.Pen prevP = drawCommandList.get(i - 1);
                    canvas.drawLine(prevP.x, prevP.y, p.x, p.y, paint);
                }
            }

        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            int action = e.getAction();
            int state = action == MotionEvent.ACTION_DOWN ? activity_user.Pen.STATE_START : activity_user.Pen.STATE_MOVE;
            drawCommandList.add(new activity_user.Pen(e.getX(), e.getY(), state, color, size));
            invalidate();
            return true;
        }


    }

}