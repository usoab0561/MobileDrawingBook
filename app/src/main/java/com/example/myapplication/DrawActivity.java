package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.util.CanvasIO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * 그리기 화면입니다.
 * (그리기, 지우기, 저장, 호출)등이 가능합니다.
 */
public class DrawActivity extends AppCompatActivity {
    private DrawCanvas drawCanvas;
    private FloatingActionButton fbPen;             //펜 모드 버튼
    private FloatingActionButton fbPen_RED;             //펜 모드 버튼
    private FloatingActionButton fbPen_ORANGE;             //펜 모드 버튼
    private FloatingActionButton fbPen_YELLOW;             //펜 모드 버튼
    private FloatingActionButton fbPen_GREEN;             //펜 모드 버튼
    private FloatingActionButton fbPen_BLUE;             //펜 모드 버튼
    private FloatingActionButton fbPen_VIOLET;             //펜 모드 버튼
    private FloatingActionButton fbPen_PURPLE;             //펜 모드 버튼
    private FloatingActionButton fbPen_WHITE;             //펜 모드 버튼
    private FloatingActionButton fbPen_BLACK;             //펜 모드 버튼


    private FloatingActionButton fbEraser;          //지우개 모드 버튼
    private FloatingActionButton fbSave;            //그림 저장 버튼
    private FloatingActionButton fbOpen;            //그림 호출 버튼
    private ConstraintLayout canvasContainer;       //캔버스 root view

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        findId();
        canvasContainer.addView(drawCanvas);
        TextView title = findViewById(R.id.toolbar_title); // 타이틀 가져오기
        title.setText("테스트임당");
        setOnClickListener();
    }

    /**
     * jhChoi - 201124
     * View Id를 셋팅합니다.
     */
    private void findId() {
        canvasContainer = findViewById(R.id.lo_canvas);
        fbPen = findViewById(R.id.fb_pen);
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
        fbSave = findViewById(R.id.fb_save);
        fbOpen = findViewById(R.id.fb_open);
        drawCanvas = new DrawCanvas(this);
    }

    /**

     * OnClickListener Setting
     */
    private void setOnClickListener() {
        fbPen.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN);
        });
        fbPen_RED.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_RED);
        });
        fbPen_ORANGE.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_ORANGE);
        });
        fbPen_YELLOW.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_ORANGE);
        });
        fbPen_GREEN.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_GREEN);
        });
        fbPen_BLUE.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_BLUE);
        });
        fbPen_VIOLET.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_VIOLET);
        });
        fbPen_WHITE.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_WHITE);
        });
        fbPen_BLACK.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_PEN_BLACK);
        });

        fbEraser.setOnClickListener((v)->{
            drawCanvas.changeTool(DrawCanvas.MODE_ERASER);
        });

        fbSave.setOnClickListener((v)->{
            drawCanvas.invalidate();
            Bitmap saveBitmap = drawCanvas.getCurrentCanvas();
            CanvasIO.saveBitmap(this, saveBitmap);
        });

        fbOpen.setOnClickListener((v)->{
            drawCanvas.init();
            drawCanvas.loadDrawImage = CanvasIO.openBitmap(this);
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
        final int PEN_SIZE = 3;                                   //펜 사이즈
        final int ERASER_SIZE = 30;                               //지우개 사이즈

        ArrayList<Pen> drawCommandList;                           //그리기 경로가 기록된 리스트
        Paint paint;                                              //펜
        Bitmap loadDrawImage;                                     //호출된 이전 그림
        int color;                                                //현재 펜 색상
        int size;                                                 //현재 펜 크기

        public DrawCanvas(Context context) {
            super(context);
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
                this.color = Color.argb(255,0,0,0);
                size = PEN_SIZE;
            }
            else if (toolMode == MODE_PEN_YELLOW){
                this.color = Color.argb(255,255,255,0);
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

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            if (loadDrawImage != null) {
                canvas.drawBitmap(loadDrawImage, 0, 0, null);
            }

            for (int i = 0; i < drawCommandList.size(); i++) {
                Pen p = drawCommandList.get(i);
                paint.setColor(p.color);
                paint.setStrokeWidth(p.size);

                if (p.isMove()) {
                    Pen prevP = drawCommandList.get(i - 1);
                    canvas.drawLine(prevP.x, prevP.y, p.x, p.y, paint);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            int action = e.getAction();
            int state = action == MotionEvent.ACTION_DOWN ? Pen.STATE_START : Pen.STATE_MOVE;
            drawCommandList.add(new Pen(e.getX(), e.getY(), state, color, size));
            invalidate();
            return true;
        }
    }



}