package com.bp.trianglebricks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public class MyCanvas extends View {

    private static final String TAG = "BoxDrawingView";

    public MyCanvas(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    protected void MyOnDraw(Canvas canvas, int X) {

    }

     private static void DrawTriangle(Canvas canvas, int id) {
        if (Util.TriangleArr[id].hide == true) {
            return;
        }
        float ClickX = Util.ClickX;
        float ClickY = Util.ClickY;
        double Distance = Util.DistancePointToPoint(Util.TriangleArr[id].x[0], Util.TriangleArr[id].y[0], ClickX, ClickY);
        Distance += Util.DistancePointToPoint(Util.TriangleArr[id].x[1], Util.TriangleArr[id].y[1], ClickX, ClickY);
        Distance += Util.DistancePointToPoint(Util.TriangleArr[id].x[2], Util.TriangleArr[id].y[2], ClickX, ClickY);
        double Limit = 1.9 * Util.BrickSize;
        if (Distance < Limit) {
            Log.d("Clicked", "id=" + id);
            // not dwaw if click
            return;
        }


        Paint paint = new Paint();
        paint.setColor(Util.colors[Util.TriangleArr[id].color]);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        Point a = new Point((int) Util.TriangleArr[id].x[0], (int) Util.TriangleArr[id].y[0]);
        Point b = new Point((int) Util.TriangleArr[id].x[1], (int) Util.TriangleArr[id].y[1]);
        Point c = new Point((int) Util.TriangleArr[id].x[2], (int) Util.TriangleArr[id].y[2]);
//        Log.d(TAG, "onDraw: DrawTriangle on X="+Util.TriangleArr[id].X+", Y="+Util.TriangleArr[id].Y+", points:" + a.x + "," + a.y + ";" + b.x + "," + b.y + ";" + c.x + "," + c.y + "; color="+ Util.TriangleArr[id].color+                ", aID="+Util.TriangleArr[id].aID+", bID="+Util.TriangleArr[id].bID+", cID="+Util.TriangleArr[id].cID );
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();

        canvas.drawPath(path, paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        float signDim = Util.BrickSize / 5;
        float x = (float) (Util.TriangleArr[id].x[0] + Util.TriangleArr[id].x[1] + Util.TriangleArr[id].x[2]) / 3;
        float y = (float) (Util.TriangleArr[id].y[0] + Util.TriangleArr[id].y[1] + Util.TriangleArr[id].y[2]) / 3;
        if (Util.TriangleArr[id].targetKind == 1) {
            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            x = (float) Util.TriangleArr[id].CentrX;
            y = (float) Util.TriangleArr[id].CentrY;
            path.moveTo(x + signDim, y);
            path.lineTo(x, y + signDim);
            path.lineTo(x - signDim, y);
            path.lineTo(x, y - signDim);

            path.close();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
        }
        if (Util.TriangleArr[id].targetKind == 2) {
            path.reset();
            path.setFillType(Path.FillType.EVEN_ODD);
            x = (float) Util.TriangleArr[id].CentrX;
            y = (float) Util.TriangleArr[id].CentrY;
            path.moveTo(x + signDim, y + signDim);
            path.lineTo(x - signDim, y - signDim);
            path.moveTo(x - signDim, y + signDim);
            path.lineTo(x + signDim, y - signDim);

            path.close();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, paint);
         }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        float canvH = canvas.getHeight();
        float canvW = canvas.getWidth();
        canvas.drawColor(Color.WHITE);
        for (int i = 0; i < Util.triangleCount; i++) {
            DrawTriangle(canvas, i);
        }


    }


}