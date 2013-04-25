package com.abplus.secords;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/21 19:58
 */
public class Element {

    private Path  path  = null;
    private Paint paint = null;
    private RectF bound = new RectF();

    /**
     * コンストラクタ
     * @param path  パス
     * @param paint ペイント
     */
    public Element(Path path, Paint paint) {
        this.path  = path;
        this.paint = paint;
        if (this.path != null) path.computeBounds(bound, true);
    }

    /**
     * Paintをもたないコンストラクタ
     * @param path  パス
     */
    public Element(Path path) {
        this.path = path;
        if (this.path != null) this.path.computeBounds(bound, true);
    }

    //properties

    protected Path path() {
        return path;
    }

    protected Paint paint() {
        return paint;
    }

    public RectF bound() {
        return bound;
    }

    /**
     * 図形の描画
     * @param canvas    描画するキャンバス
     * @param paint     描画するペイント(デフォルト)
     */
    public void draw(Canvas canvas, Paint paint) {
        if (this.paint != null) paint = this.paint;
        canvas.drawPath(path, paint);
    }

    public void preferredBound(RectF bound) {
        if (bound.left   > this.bound.left)   bound.left   = this.bound.left;
        if (bound.right  < this.bound.right)  bound.right  = this.bound.right;
        if (bound.bottom > this.bound.bottom) bound.bottom = this.bound.bottom;
        if (bound.top    < this.bound.top)    bound.top    = this.bound.top;
    }

    public void setStrokeWidth(float w) {
        if (paint != null) paint.setStrokeWidth(w);
    }

    static Element circle(float x, float y, float r, Paint paint) {
        Path path = new Path();
        path.addCircle(x, y, r, Path.Direction.CW);
        return new Element(path, paint);
    }

    static Element circle(float x, float y, float r) {
        return circle(x, y, r, null);
    }

    static Element rectangle(float left, float top, float right, float bottom, Paint paint) {
        Path path = new Path();
        path.addRect(left, top, right, bottom, Path.Direction.CW);
        return new Element(path);
    }

    static Element rectangle(float left, float top, float right, float bottom) {
        return rectangle(left, top, right, bottom, null);
    }
}
