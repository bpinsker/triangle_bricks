package com.bp.trianglebricks;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.strictmode.CleartextNetworkViolation;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.FileInputStream;
import java.io.InputStream;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Calendar;

import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class Util {
    private static final String TAG = "Util class";
    public static int BrickSize = 30;
    public static int FieldSide = 30;
    public static int maxX;
    public static int maxY;
    public static int colorsNum = 4;
    public static int getRndProgressStart = 1234;
    public static int rndProgress = getRndProgressStart;
    public static String LogFN = "logFile.txt";
    public static String ScoreFN = "score.txt";
    private static final int MAX_STREAMS = 100;
    public static boolean OnUndo = false;
    public static int Silent = 0;
    private static int prevColorRnd = 0;
    public static int ifDebug = 0;
    public static float ClickX = -1;
    public static float ClickY = -1;
    public static int triangleCountLine = 0;
    public static int triangleCount = 0;
    public static Triangle TriangleArr[] = new Triangle[1000];
    public static int colors[] = {Color.TRANSPARENT, Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.LTGRAY};
    public static int GridWing = 3;
    public static int GridCount = 5;
    public static int[] TargetArr = {13, 7, 14, 12, 1, 25, 17};
    public static int Targets = 4;
    public static int TargetID = 0;

    public static int[] HaveMove(int id) {
        int[] move = {0, 0, 0, 0, 0, 0};
        int[][] directions = new int[6][2];
        // left
        directions[0][0] = -1;
        directions[0][1] = 0;
        //right
        directions[1][0] = 1;
        directions[1][1] = 0;
        // left-up
        directions[2][0] = -1;
        directions[2][1] = -1;
        // right-uo
        directions[3][0] = 1;
        directions[3][1] = -1;
        // left-down
        directions[4][0] = -1;
        directions[4][1] = 1;
        // right-down
        directions[5][0] = 1;
        directions[5][1] = -1;
        boolean IfHaveMove = false;
        Triangle tr = Util.TriangleArr[id];
        //Log.d(TAG, "HaveMove: qadrant=" + tr.quadrant);
        for (int dirI = 0; dirI < 6; dirI++) {
            int dirX = directions[dirI][0];
            int dirY = directions[dirI][1];
            double X = tr.CentrX;
            double Y = tr.CentrY;
            boolean closeDirection = false;
            move[dirI] = 0;
            for (int step = 1; step < 2 * GridCount; step++) {
                if (closeDirection == true) {
                    break;
                }
                X = tr.CentrX + dirX * BrickSize * step / 2;
                Y = tr.CentrY + dirY * 1.25 * BrickSize * step / 2;
                for (int i = 0; i < triangleCount; i++) {
                    double distance = DistancePointToPoint(X, Y, TriangleArr[i].CentrX, TriangleArr[i].CentrY);
                    if (distance < BrickSize / 3) {
                        if (TriangleArr[i].quadrant == 0 && TriangleArr[i].color > 0) {
                            Log.d(TAG, "HaveMove: from id=" + id + ", to id=" + i + ", distance=" + distance + ", steps=" + step);
                            move[dirI] = step;
                            IfHaveMove = true;
                            closeDirection = true;
                        }
                    }
                    if (closeDirection == true) {
                        break;
                    }
                }
            }
        }
        if (IfHaveMove == true) {
            Log.d(TAG, "HaveMove: from id=" + id + ",moveArr=" + move);
        }
        return move;
    }

    public static double DistancePointToPoint(double X0, double Y0, double X1, double Y1) {
        return Math.sqrt(Math.pow(X0 - X1, 2) + Math.pow(Y0 - Y1, 2));
    }

    public static double DistancePointToLine(double X0, double Y0, double A, double B, double C) {
        double coef = Math.sqrt(A * A + B * B);
        double a = A / coef;
        double b = B / coef;
        double p = C / coef;
        double ret = (X0 * a + Y0 * b - p);
        return ret;
    }

    public static void ClickSound(Context context) {
        // sound
        Sound(context, R.raw.click);
    }

    public static void setClick(float X, float Y) {
        ClickX = X;
        ClickY = Y;
        Log.d("TAG", "setClick: ");
    }


    public static void ExplosionSound(Context context) {
        // sound
        Sound(context, R.raw.explosion);
    }

    public static void Sound(Context context, int resID) {
        // sound
        if (OnUndo == true) {
            return;
        }
        if (Silent == 1) {
            return;
        }
        MediaPlayer ring = MediaPlayer.create(context, resID);
        ring.start();
    }

    public static int rndColor(int i) {
        rndProgress = rndProgress + i + 1;
        rndProgress = rndProgress % 14000 + getRndProgressStart;
        double rndBase = (rndProgress) ^ 2;
        int logRnd = (int) Math.log(rndBase);
        rndBase = rndBase % (10 ^ (logRnd - 4));
        rndBase = rndBase / (10 ^ ((int) Math.log10(rndBase)));
        int color = (int) ((colorsNum + 1) * rndBase);
        color = Math.min(color, colorsNum);
//        Log.d("Utilclass:", "rndColor="+color +",colorsNum="+ colorsNum+" ,logRnd="+logRnd+" ,rndBase="+rndBase +" ,rndProgress="+rndProgress);
        return color;
    }

    public static int Msg(Context theContext, String str) {
        Toast toast = Toast.makeText(theContext, str, Toast.LENGTH_LONG);
        toast.show();
        return 0;
    }

    public static void setrndProgress(int X) {
        rndProgress = X;
    }

    public static void setSilent(int s) {
        Silent = s;
    }

    public static void setMaxX(int X) {
        maxX = X;
    }

    public static void setOnUndo(boolean onundo) {
        OnUndo = onundo;
    }

    public static void setMaxY(int Y) {
        maxY = Y;
    }

    public static void setBrickSize(int bsize) {
        BrickSize = bsize;
    }

    public static void setFieldSide(int fieldside) {
        FieldSide = fieldside;
    }


    public static void colorsNum(int cn) {
        colorsNum = cn;
    }

    public static String TimeStr() {
        Date now = new Date();
        long timestamp = now.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateStr = sdf.format(timestamp);
        Calendar calendar;
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        String time = "" + calendar.getTime();
        return dateStr + "-" + time;
    }

    public static int ScoreClearFile(Context theContext) {
        return ClearFile(theContext, ScoreFN);
    }

    public static int LogClearFile(Context theContext) {
        return ClearFile(theContext, LogFN);
    }

    public static int ClearFile(Context theContext, String FN) {
        File logFile = new File(theContext.getFilesDir() + "/", FN);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(logFile, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        try {
            bufferedWriter.write("");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int FileWrite(Context theContext, String FN, String strToWrite) {
        File file = new File(theContext.getFilesDir() + "/", FN);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        try {
            bufferedWriter.write(strToWrite);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int LogfileWrite(Context theContext, String strToWrite) {
        return FileWrite(theContext, LogFN, strToWrite);
    }

    public static int ScorefileWrite(Context theContext, String strToWrite) {
        return FileWrite(theContext, ScoreFN, strToWrite);
    }

    public static int getValue(Context theContext, String ValueName, int defaultValue) {
        int value = defaultValue;
        String Str = FileRead(theContext, ValueName + ".txt");
        if (Str.length() < 1) {
            return defaultValue;
        }
        value = Integer.valueOf(Str);
        return value;
    }

    public static String getValue(Context theContext, String ValueName, String defaultValue) {
        String value = defaultValue;
        String Str = FileRead(theContext, ValueName + ".txt");
        if (Str.length() < 1) {
            return defaultValue;
        }
        return Str;
    }

    public static void setValue(Context theContext, String ValueName, int Value) {
        ClearFile(theContext, ValueName + ".txt");
        FileWrite(theContext, ValueName + ".txt", "" + Value);
    }

    public static void setValue(Context theContext, String ValueName, String Value) {
        ClearFile(theContext, ValueName + ".txt");
        FileWrite(theContext, ValueName + ".txt", Value);
    }

    public static String FileRead(Context theContext, String FN) {
        String str = "";
        try {
            FileInputStream input = new FileInputStream(theContext.getFilesDir() + "/" + FN);
            try {
                int i = input.read();
                while (i != -1) {
                    str += (char) i;
                    i = input.read();
                }
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return str;

    }

    public static String ScorefileRead(Context theContext) {
        return FileRead(theContext, ScoreFN);
    }

    public static String LogfileRead(Context theContext) {
        return FileRead(theContext, LogFN);
    }


}
