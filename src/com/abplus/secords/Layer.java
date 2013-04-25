package com.abplus.secords;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/21 19:31
 */
public class Layer {

    private Vector<Element> elements = new Vector<Element>();
    private Paint paint;
    private boolean visible = true;
    private String name;

    public Layer(String name, Paint paint) {
        this.paint = paint;
        this.name  = name;
    }

    public boolean add(Element elem) {
        return elements.add(elem);
    }

    public void draw(Canvas canvas, RectF bound) {
        if (visible) {
            for (Element elem: elements) {
                //無駄な描画はさける
//                RectF eb = elem.bound();
//                if (eb == null || eb.intersect(bound)) {
//                    elem.draw(canvas, paint);
//                }
                elem.draw(canvas, paint);
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return paint.getColor();
    }

    public void preferredBound(RectF bound) {
        for (Element elm: elements) elm.preferredBound(bound);
    }

    public void setStrokeWidth(float w) {
        if (paint != null) paint.setStrokeWidth(w);
        for (Element elm: elements) elm.setStrokeWidth(w);
    }

    public void debugLog() {
        for (Element elm: elements) {
            RectF bound = elm.bound();
            if (bound != null) {
                Log.d("debugLog", "(" + bound.left + ", " + bound.bottom + ")-(" + bound.right + ", " + bound.top +")");
            }
        }
    }
}
