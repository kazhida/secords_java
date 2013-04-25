package com.abplus.secords;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/23 20:21
 */
public class ViewController implements DrawingView.Controller,
        ScaleGestureDetector.OnScaleGestureListener,
        GestureDetector.OnGestureListener {

    private DrawingView view;
    private ScaleGestureDetector scale;
    private GestureDetector fling;

    public ViewController(Context context, DrawingView view) {
        super();
        this.view = view;
        scale = new ScaleGestureDetector(context, this);
        fling = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() > 1) {
            return scale.onTouchEvent(ev);
        } else {
            return fling.onTouchEvent(ev) || scale.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float f = detector.getScaleFactor();
        if (Math.abs(f - 1.0f) < 0.01) {
            return false;
        } else {
            view.zoom(1.0f / f);
            return true;
        }
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        float f = detector.getScaleFactor();
        view.zoom(1.0f / f);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onDown (MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();
        return view.scroll(x, y);
    }

    @Override
    public void onLongPress (MotionEvent e) {

    }

    @Override
    public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return view.scroll(-distanceX, -distanceY);
    }

    @Override
    public void onShowPress (MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp (MotionEvent e) {
        return false;
    }
}
