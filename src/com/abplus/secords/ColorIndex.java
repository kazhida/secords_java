package com.abplus.secords;

import android.graphics.Color;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 14:58
 */
public class ColorIndex {

    private int idx;
    private int color;

    public ColorIndex(int idx) {
        super();
        this.idx = idx;

        switch (idx) {
            case 1:
                color = Color.RED;
                break;
            case 2:
                color = Color.YELLOW;
                break;
            case 3:
                color = Color.GREEN;
                break;
            case 4:
                color = Color.CYAN;
                break;
            case 5:
                color = Color.BLUE;
                break;
            case 6:
                color = Color.MAGENTA;
                break;
            case 7:
                color = Color.WHITE;
                break;
            case 8:
                color = Color.GRAY;
                break;
            case 9:
                color = Color.LTGRAY;
                break;
            default:
                if (idx < 250) {
                    //HSV
                    int h = idx / 10 - 1;
                    int s = idx % 10 + 1;
                    int v = s % 2;
                    s = s / 2;
                    float hsv[] = new float[3];
                    hsv[0] = h / 24.0f * 360.0f;
                    hsv[1] = (4 - s) / 10.0f;
                    hsv[2] = v;
                    color = Color.HSVToColor(hsv);
                } else if (idx < 255) {
                    //GrayScale
                    int gs = 255 - (255 - idx) * 41;
                    color = Color.rgb(gs, gs, gs);
                } else {
                    color = Color.WHITE;   //仮
                }
                break;
        }
    }

    public int index() {
        return idx;
    }

    public int color() {
        return color;
    }
}
