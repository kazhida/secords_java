package com.abplus.secords;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/21 20:08
 */
public class TextElement extends Element {

    public enum Align {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum Position {
        TOP,
        MIDDLE,
        BASE,
        BOTTOM
    }

    private float  x;
    private float  y;
    private float  h;
    private String s;
    private float  r;
    private Paint     paint;
    private Align align;
    private Position position;
    private Paint.FontMetrics fm;

    public TextElement(float x, float y, String s, float h, float r, Paint paint) {
        super(null, paint);
        this.x = x;
        this.y = y;
        this.s = s;
        this.h = h;
        this.r = r;
        position = Position.MIDDLE;
        align = Align.CENTER;
    }

    public TextElement(float x, float y, String s, float h, float r) {
        super(null, null);
        this.x = x;
        this.y = y;
        this.s = s;
        this.h = h;
        this.r = r;
        position = Position.MIDDLE;
        align = Align.CENTER;
    }

    public void setHorizontalAlignment(Align align) {
        this.align = align;
    }

    public void setVerticalAlignment(Position position) {
        this.position = position;
    }

    @Override
    public RectF bound() {
        //  面倒なのでとりあえず
        return null;
    }

    @Override
    public void preferredBound(RectF bound) {
        //  面倒なので影響をあたえないことにする
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (this.paint == null) {
            //on demand copy
            this.paint = new Paint(paint);
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setStrokeWidth(1);
            this.paint.setTextSize(h);
        }
        paint = this.paint;

        if (fm == null) {
            fm = paint.getFontMetrics();
        }

        switch (align) {
            case LEFT:
                paint.setTextAlign(Paint.Align.LEFT);
                break;
            case CENTER:
                paint.setTextAlign(Paint.Align.CENTER);
                break;
            case RIGHT:
                paint.setTextAlign(Paint.Align.RIGHT);
                break;
        }

        canvas.save();
        canvas.scale(1, -1, x, y);
        if (r != 0.0f) canvas.rotate(r, x, y);

        switch (position) {
            case TOP:
                canvas.drawText(s, x, y - fm.ascent, paint);
                break;
            case MIDDLE:
                canvas.drawText(s, x, y - fm.ascent / 2, paint);
                break;
            case BASE:
                canvas.drawText(s, x, y, this.paint);
                break;
            case BOTTOM:
                canvas.drawText(s, x, y - fm.descent, paint);
                break;
        }

        canvas.restore();
        //for debug!
//        canvas.drawLine(x - 10, y - 10, x + 10, y + 10, paint);
//        canvas.drawLine(x + 10, y - 10, x - 10, y + 10, paint);
    }

    @Override
    public void setStrokeWidth(float w) {
        if (paint != null) paint.setStrokeWidth(1);
    }

    static TextElement centerText(float x, float y, String s, float h, int color) {
        return new TextElement(x, y, s, h, 0, DrawingView.defaultPaint(color));
    }

    static TextElement centerText(float x, float y, String s, float h) {
        TextElement elm = new TextElement(x, y, s, h, 0, null);
        elm.position = Position.BASE;
        elm.align = Align.CENTER;
        return elm;
    }

    static TextElement middleText(float x, float y, String s, float h) {
        TextElement elm = new TextElement(x, y, s, h, 0, null);
        elm.position = Position.MIDDLE;
        elm.align = Align.CENTER;
        return elm;
    }

    static TextElement topLeftText(float x, float y, String s, float h) {
        TextElement elm = new TextElement(x, y, s, h, 0, null);
        elm.position = Position.TOP;
        elm.align = Align.LEFT;
        return elm;
    }

    static TextElement bottomRightText(float x, float y, String s, float h) {
        TextElement elm = new TextElement(x, y, s, h, 0, null);
        elm.position = Position.BOTTOM;
        elm.align = Align.RIGHT;
        return elm;
    }


}
