package com.abplus.secords;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/21 20:49
 */
public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {

    public interface Controller {
        boolean onTouchEvent(MotionEvent ev);
    }

    //  viewの大きさ（表示上の大きさ）
    private float   viewHeight;
    private float   viewWidth;

    //  モデルの中心と高さの半分
    private PointF center = new PointF();
    private float  halfHeight;

    //  視線の焦点と距離
    private PointF  focus = new PointF();
    private float   distance;

    //  ベースの色とグリッドの色
    private int     baseColor;
    private Paint   gridPaint;

    //  レイヤ
    private Map<String, Layer> layerMap = new HashMap<String, Layer>();
    private Vector<Layer>      layerVec = new Vector<Layer>();

    //  コントローラ
    private Controller controller;

    /**
     * コンストラクタ
     * @param context   このビューのコンテキスト
     */
    public DrawingView(Context context) {
        super(context);
        getHolder().addCallback(this);

        gridPaint = defaultPaint(Color.GRAY);
        baseColor  = Color.BLACK;

        //とりあえずのデフォルト
        setModelGeometry(0, 0, 1024);
        focus.set(0, 0);

//        addTestData();
    }

    private void addTestData() {
        //for debug!
        putLayer("0", new Layer("0", defaultPaint(Color.WHITE)));
        putLayer("1", new Layer("1", defaultPaint(Color.CYAN)));
        layers(0).add(Element.circle(0, 0, 250));
        layers(0).add(TextElement.centerText(20, 20, "Hello CC", 20));
        layers(0).add(TextElement.topLeftText(220, 20, "Hello TL", 20));
        layers(0).add(TextElement.bottomRightText(420, 20, "Hello BR", 20));
        layers(0).add(TextElement.middleText(520, 20, "Hello MC", 20));
        layers(1).add(Element.rectangle(-700, 500, 700, -500));
        layers(1).add(Element.rectangle(-650, 450, 650, -450));
        layers(1).add(Element.rectangle(350, -300, 650, -450));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // SurfaceViewが最初に生成されたときに呼び出されるメソッド
        Log.d("SampleSurView", "surfaceCreated called.");
        holder.setFixedSize(getWidth(), getHeight());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // SurfaceViewのサイズなどが変更されたときに呼び出されるメソッド。
        Log.d("SampleSurView", "surfaceChanged called(" + width + "x" + height + ").");

        viewWidth  = width;
        viewHeight = height;

        adjustStrokeWidth();

        Canvas canvas = holder.lockCanvas();
        draw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // SurfaceViewが破棄されるときに呼び出されるメソッド
        Log.d("SampleSurView", "surfaceDestroyed");
    }

    /**
     * モデルの位置、大きさの設定
     * @param x         モデルの中心のX座標
     * @param y         モデルの中心のY座標
     * @param height    モデルの高さ
     */
    public void setModelGeometry(float x, float y, float height) {
        center.set(x, y);
        halfHeight = height / 2;
        //  視点も初期化
        setDistance(halfHeight);
        focus.set(center);
    }

    public void setPreferredGeometry() {
        RectF bound = new RectF();

        for (Layer lyr: layerVec) {
            lyr.preferredBound(bound);
        }

        float x = (bound.left + bound.right) / 2;
        float y = (bound.top + bound.bottom) / 2;
        float h = (bound.top - bound.bottom) * 1.1f;

        setModelGeometry(x, y, h);
    }

    public boolean setDistance(float d) {
        if (d > halfHeight) d = halfHeight;
        if (distance != d) {
            distance = d;
            adjustStrokeWidth();
            return true;
        } else {
            return false;
        }
    }

    private void adjustStrokeWidth() {
        if (viewHeight > 0) {
            float w = distance * 3 / viewHeight;
            for (Layer lyr: layerVec) lyr.setStrokeWidth(w);
        }
    }

    public float getModelHeight() {
        return halfHeight * 2;
    }

    public float getDistance() {
        return distance;
    }

    private RectF visibleBound() {
        RectF bound = new RectF();

        float h = distance;
        float w = h * viewWidth / viewHeight;

        bound.left   = focus.x - w;
        bound.right  = focus.x + w;
        bound.top    = focus.y + h;
        bound.bottom = focus.y - h;

        return bound;
    }

    private RectF centerBound() {
        RectF bound = new RectF();
        float h = halfHeight - distance;
        float w = h * viewWidth / viewHeight;

        bound.left   = center.x - w;
        bound.right  = center.x + w;
        bound.top    = center.y + h;
        bound.bottom = center.y - h;

        return bound;
    }

    public boolean setFocus(float x, float y) {
        RectF bound = centerBound();
        if (x < bound.left)   x = bound.left;
        if (x > bound.right)  x = bound.right;
        if (y < bound.bottom) y = bound.bottom;
        if (y > bound.top)    y = bound.top;

        boolean dirty = false;

        if (focus.x != x) {
            focus.x = x;
            dirty = true;
        }

        if(focus.y != y) {
            focus.y = y;
            dirty = true;
        }

        if (dirty) redraw();

        return dirty;
    }

    public PointF getFocus() {
        return focus;
    }

    public boolean scroll(float x, float y) {
        float f = (distance * 2) / viewHeight;
        x *= f;
        y *= f;
        if (setFocus(focus.x - x, focus.y + y)) {
            redraw();
            return true;
        } else {
            return false;
        }
    }

    public boolean zoom(float f) {
        if (setDistance(distance * f)) {
            redraw();
            return true;
        } else {
            return false;
        }
    }

    public void setBaseColor(int c) {
        baseColor = c;
    }

    public void setGrid(boolean grid) {
        if (grid) {
            gridPaint.setColor(baseColor);
        } else {
            gridPaint.setColor(Color.LTGRAY);
        }
    }

    public synchronized void setController(Controller controller) {
        this.controller = controller;
    }


    public synchronized boolean putLayer(String name, Layer layer) {
        layerMap.put(name, layer);
        return layerVec.add(layer);
    }

    public synchronized Layer layers(String name) {
        return layerMap.get(name);
    }

    public synchronized Layer layers(int idx) {
        return layerVec.get(idx);
    }

    public synchronized void clear() {
        layerMap.clear();
        layerVec.clear();
    }


    private void drawBase(Canvas canvas) {
        canvas.drawColor(baseColor);

        if (gridPaint.getColor() != baseColor) {
            for (float y = 0; y < viewHeight; y += 10) {
                canvas.drawLine(0, y, viewWidth, y, gridPaint);
            }
            for (float x = 0; x < viewWidth; x += 10) {
                canvas.drawLine(x, 0, x, viewHeight, gridPaint);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        drawBase(canvas);

        float ratio = viewHeight / (distance * 2);
        canvas.translate(viewWidth / 2, viewHeight / 2);
        canvas.scale(ratio, -ratio);
        canvas.translate(-focus.x, -focus.y);
//        canvas.translate(0, 2 * halfHeight);

        RectF bound = visibleBound();
        for (Layer lyr: layerVec) {
            lyr.draw(canvas, bound);
        }
    }

    public void redraw() {
        SurfaceHolder holder = getHolder();
        Canvas canvas = holder.lockCanvas();
        try {
            canvas.save();
            draw(canvas);
        } finally {
            canvas.restore();
            holder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (controller == null) {
            return false;
        } else {
            return controller.onTouchEvent(ev);
        }
    }

    /**
     * 色を指定すると適当なPaintを作ってくれる便利関数
     * @param color Paintの色
     * @return      指定された色で、AntiAliasの効いた幅1のSTROKEを書いてくれるPaint
     */
    static public Paint defaultPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        return paint;
    }

    public void debugLog() {

        for (String ln : layerMap.keySet()) {
            Log.d("debugLog", "Layer(map):" + ln);
        }
        for (Layer lyr: layerVec) {
            Log.d("debugLog", "Layer(vec):" + lyr.getName() + "(" + Integer.toHexString(lyr.getColor()));
        }
        for (Layer lyr: layerVec) {
            lyr.debugLog();
        }
        Log.d("debugLog", "Center:(" + center.x + ", " + center.y + ")");
        Log.d("debugLog", "HalfHeight:" + halfHeight);

        Log.d("debugLog", "Focus:(" + focus.x + ", " + focus.y + ")");
        Log.d("debugLog", "Distance:" + distance);
    }
}
