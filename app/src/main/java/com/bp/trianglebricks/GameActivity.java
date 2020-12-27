package com.bp.trianglebricks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private ImageView TriangeleIvBTN;
    private Button mMenu;
    private Button mUndo;
    private Button mDebug;
    private TextView scoreTV;
    private Context theContext;
    public View FieldView;
    public Canvas canvas;
    public int SetupShow = 1;
    public int SquareCount = 0;
    public int GridWing = 3;
    public int GridCount = 5;
    public int ColorsNum = 2;//4; 4 is max
    int colorAddLevel = 9;
    public int maxColorNum = 5;
    public int TargetsNum = 1;
    public ImageView[] ivArr;
    public int ivArrMax = 1000;
    public int tick = 0;
    public int[][] SqTransitionArr;
    public int SqTransitionSpeed = 1;
    private Timer mTimer;
    int timerPeriod_ms = 100;
    private MyTimerTask mMyTimerTask;
    public boolean OnClickAcive = false;
    public boolean inCareDone = false;
    public int Score = 0;
    public int Level = 1;
    public Triangle[] TriangleArr;



    public String TAG = "GameActivity";

    private Paint paint = new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //start my code
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mMenu = (Button) findViewById(R.id.Menu);
        mUndo = (Button) findViewById(R.id.Undo);
        mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.ScorefileWrite(theContext, Score + "\n");
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UndoOnClickAction(1);
            }
        });
        mDebug = (Button) findViewById(R.id.debug);
        if (Util.ifDebug != 0) {
            mDebug.setWidth(2 * Util.BrickSize);
            mDebug.setHeight(2 * Util.BrickSize);
            mDebug.setX(Util.maxX - 2 * Util.BrickSize);
            mDebug.setY(Util.maxY - 2 * Util.BrickSize);
            mDebug.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Level += 1;
                    Util.setValue(getApplicationContext(), "level", Level);
                    Util.Msg(getApplicationContext(), "Game win, next level " + Level);
                    Util.ScorefileWrite(getApplicationContext(), Score + "\n");
                    Util.LogClearFile(getApplicationContext());
                    Intent intent = new Intent(GameActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            mDebug.setVisibility(View.GONE);
        }
        FieldView = new MyCanvas(getApplicationContext());
        Bitmap bitmap = Bitmap.createBitmap(1000/*width*/, 1000/*height*/, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        initFuild(canvas);

        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.setImageBitmap(bitmap);
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                PointF current = new PointF(event.getX(), event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        float CanvasX = (current.x * 1000)/view.getWidth();
                        float CanvasY = (current.y * 1000)/view.getHeight();;
                        Util.setClick(CanvasX, CanvasY);
                        Log.i(TAG, "action=ACTION_DOWN"  + " at x=" + current.x + ", y=" + current.y +", view.getX()="+view.getX() +", view.getY()="+view.getY()  +", view.getWidth()="+view.getWidth() +", view.getHeight()="+view.getHeight() +", CanvasX="+ CanvasX +", CanvasY="+ CanvasY);
                        view.invalidate();
                        FieldView.draw(canvas);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        Log.d(TAG, "ACTION_CANCEL iv");
                        break;
                    }
                }
                return true;
            }
        });

        mTimer.scheduleAtFixedRate(mMyTimerTask, 10, timerPeriod_ms);
        Level = Util.getValue(this, "level", 1);
        scoreTV = (TextView) findViewById(R.id.scoreTV);
        scoreTV.setText("Score: " + Score + "\n" + "Level: " + Level + "\n" + "Colors: " + (ColorsNum + 1));
        String logStr = Util.LogfileRead(this);
        int Silent = Util.getValue(this, "silent", 0);
        Util.setSilent(Silent);
        if (logStr.length() > 5) {
            UndoOnClickAction(0);
        } else {
            initGame();
            createSetup();
            SquareUpdSign();
        }





    }
    public void UndoOnClickAction(int backSteps) {
    }

    public void initGame() {
    }

    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1f);
    }


    public void createSetup() {
        int triangleCount = 2*(Util.FieldSide/Util.BrickSize);
        Util.triangleCountLine = triangleCount;
        Util.triangleCount = triangleCount*triangleCount;
        triangleCount = triangleCount*triangleCount;
//        Log.d(TAG, "createSetup: Util.FieldSide="+ Util.FieldSide + ", Util.BrickSize="+ Util.BrickSize + ", triangleCount="+triangleCount);
        Util.TargetID = 0;
        for (int i= 0; i<triangleCount;i++){
            Triangle tr = new Triangle((i));
            Util.TriangleArr[i] = tr;
        }

        FieldView.draw(canvas);
     }


    public void initFuild(Canvas canvas) {
        int maxX = canvas.getWidth();
        int maxY = canvas.getHeight();
        theContext = getApplicationContext();
        Util.setMaxX(maxX);
        Util.setMaxY(maxY);
        int FieldSide= Math.min(maxX, maxY);
        Util.setFieldSide(FieldSide);
        Util.GridWing = GridWing;
        Util.GridCount = GridCount;
        int BrickSize = Math.min(maxX, maxY) / (GridCount + 2 * GridWing);
        Util.setBrickSize(BrickSize);
    }

    public void UpdateTimeTask() {
        if (tick % 1000 == 0) {
            Log.d("timer", "timer=" + tick / 1000);
        }
        tick += 1;
        //SquareTransitionAction();
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    UpdateTimeTask();
                }
            });
        }
    }
    public void SquareUpdSign() {
        int quadrant = 0;
        int haveMove = -1;
        boolean GameFinished = true;
//        Log.d("TAG", "SquareUpdSign: ");
        for (int i = 0; i < Util.triangleCount; i++) {
            if(Util.TriangleArr[i].hide == true) {
                continue;
            }
            if(Util.TriangleArr[i].quadrant == 0 ){
                continue;
            }
            if(Util.TriangleArr[i].quadrant == 11 || Util.TriangleArr[i].quadrant == 22 || Util.TriangleArr[i].quadrant == 33 ){
                Util.TriangleArr[i].targetKind = 2;
                int[] move = Util.HaveMove(i);
                if(move[0] > 0){Util.TriangleArr[i].targetKind = move[0];}
                continue;
            }
        }
        FieldView.draw(canvas);
    }

}