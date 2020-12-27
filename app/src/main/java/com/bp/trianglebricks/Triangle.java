package com.bp.trianglebricks;

import android.media.AudioTimestamp;
import android.util.Log;

public class Triangle {
    protected int ID;
    protected double aID;
    protected double bID;
    protected double cID;
    protected int targetKind; // 0 - no sign; 1- romb; 2 - "x";
    protected double X;
    protected double Y;
    protected double x[] = {0, 0, 0};
    protected double y[] = {0, 0, 0};
    protected boolean hide;
    protected int color;
    protected int quadrant;
    protected double CentrX;
    protected double CentrY;




    public Triangle(int id) {
        ID = id;
        quadrant = -1;
        int row = ((int) id / Util.triangleCountLine);
        int column = (id % Util.triangleCountLine) + 1;
        double halfH = Math.sqrt(3) * Util.BrickSize / 4;

        X = Util.BrickSize * column / 2;// - Util.BrickSize / 2;
        Y = 2 * halfH * row + halfH;
        color = Util.rndColor(id) + 1;
        x[0] = X;
        x[1] = X - Util.BrickSize / 2;
        x[2] = X + Util.BrickSize / 2;
        if (ID % 2 == 0) {
            y[0] = Y - halfH;
            y[1] = Y + halfH;
            y[2] = Y + halfH;
        } else {
            y[0] = Y + halfH;
            y[1] = Y - halfH;
            y[2] = Y - halfH;
        }
        if (row % 2 == 1) {
            x[0] -= Util.BrickSize / 2;
            x[1] -= Util.BrickSize / 2;
            x[2] -= Util.BrickSize / 2;
        }
         CentrX = (x[0] + x[1] + x[2]) / 3;
         CentrY = (y[0] + y[1] + y[2]) / 3;
//          a Line => x - y/sqr(3)=0
//          b line => x + y/sqr(3) - bricksize*GridCount = 0
//          c line +> y - h = 0
        aID = Util.DistancePointToLine(CentrX, CentrY, 1, (-1) / (Math.sqrt(3)), Util.BrickSize * (Util.GridCount - 1.1));
        bID = Util.DistancePointToLine(CentrX, CentrY, 1, 1 / (Math.sqrt(3)), Util.BrickSize * (Util.GridCount + 2.1));
        cID = Util.DistancePointToLine(CentrX, CentrY, 0, 1, Util.BrickSize * (Util.GridCount + Util.GridWing));
        double delta = Math.abs(Math.abs(aID) - Math.abs(bID)) + Math.abs(Math.abs(aID) - Math.abs(cID)) + Math.abs(Math.abs(bID) - Math.abs(cID));

        hide = true;
        //color = 0;

        if (Math.abs(aID) < 1.25 * Util.BrickSize && cID < 0 * Util.BrickSize && bID > 0.5 * Util.BrickSize) {
            hide = false;
//            color = 1;
            quadrant = 1;
        }
        if (Math.abs(aID) < 0.17 * Util.BrickSize && cID < 0 * Util.BrickSize && bID > 0.5 * Util.BrickSize) {
            hide = false;
//            color = 5;
            quadrant = 11;
        }
        if (Math.abs(bID) < 1.25 * Util.BrickSize && cID < 0 * Util.BrickSize && aID < -0.5 * Util.BrickSize) {
            hide = false;
//            color = 2;
            quadrant = 2;
        }
        if (Math.abs(bID) < 0.17 * Util.BrickSize && cID < 0 * Util.BrickSize && aID < -0.5 * Util.BrickSize) {
            hide = false;
//            color = 5;
            quadrant = 22;
        }
        if (Math.abs(cID) < 1.25 * Util.BrickSize && bID > 0.5 * Util.BrickSize && aID < -0.5 * Util.BrickSize) {
            hide = false;
//            color = 3;
            quadrant = 3;
        }
        if (Math.abs(cID) < 0.17 * Util.BrickSize && bID > 0.5 * Util.BrickSize && aID < -0.5 * Util.BrickSize) {
            hide = false;
//            color = 5;
            quadrant = 33;
        }

        if (aID < -0.5 * Util.BrickSize && bID > 0.5 * Util.BrickSize && cID < -0.25 * Util.BrickSize) {
            Util.TargetID += 1;
            targetKind = 0;
            hide = false;
            quadrant = 0;
            color = 0;
            for (int i = 0; i < Util.Targets; i++) {
                if (Util.TargetID == Util.TargetArr[i]) {
                    hide = false;
                    color = Util.rndColor(id) + 1;
                    targetKind = 1;
                    break;
                } else {

                }
            }
        }
    }
}
