package com.abplus.dxf;

import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/24 20:09
 */
public class Matrix {

    /*
     * | x.x  x.y  x.z  w.x |
     * | y.x  y.y  y.z  w.y |
     * | z.x  z.y  z.z  w.z |
     * |   0    0    0    1 |
     */

    private Point x;
    private Point y;
    private Point z;
    private Point w;

    /**
     * 単位行列を作るコンストラクタ
     */
    public Matrix() {
        super();
        //	単位行列
        x = new Point(1.0f, 0.0f, 0.0f);
        y = new Point(0.0f, 1.0f, 0.0f);
        z = new Point(0.0f, 0.0f, 1.0f);
        w = new Point(0.0f, 0.0f, 0.0f);
    }

    /**
     * 各要素を指定して生成するコンストラクタ
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public Matrix(Point x, Point y, Point z, Point w) {
        super();
        this.x = new Point(x);
        this.y = new Point(y);
        this.z = new Point(z);
        this.w = new Point(w);
    }

    /**
     * 各要素を指定して生成するコンストラクタ
     * @param x
     * @param y
     * @param z
     */
    public Matrix(Point x, Point y, Point z) {
        super();
        this.x = new Point(x);
        this.y = new Point(y);
        this.z = new Point(z);
        this.w = new Point(0.0f, 0.0f, 0.0f);
    }

    /**
     * コピーコンストラクタ
     * @param src
     */
    public Matrix(Matrix src) {
        super();
        x = new Point(src.x);
        y = new Point(src.y);
        z = new Point(src.z);
        w = new Point(src.w);
    }

    /**
     * 押し出し方向を与えられたときの変換行列を作るコンストラクタ
     * @param extrusion
     */
    public Matrix(Point extrusion) {
        super();
        Point az = extrusion.unit();
        Point ax;
        if (Math.abs(az.x) < 1.0 / 64 && Math.abs(az.y) < 1.0 / 64) {
            ax = new Point(0, 1, 0).cross_product(az).unit();
        } else {
            ax = new Point(0, 0, 1).cross_product(az).unit();
        }
        Point ay = az.cross_product(ax).unit();

        x = new Point(ax.x, ay.x, az.x);
        y = new Point(ax.y, ay.y, az.y);
        z = new Point(ax.z, ay.z, az.z);
        w = new Point(0, 0, 0);
    }

    /**
     * 行列同士の積
     * @param m 右から掛ける行列
     * @return  mを右から掛けた行列
     */
    public Matrix product(Matrix m) {
        Point vx = new Point(m.x.x, m.y.x, m.z.x);
        Point vy = new Point(m.x.y, m.y.y, m.z.y);
        Point vz = new Point(m.x.z, m.y.z, m.z.z);
        Point hx = new Point(x.dot_product(vx), x.dot_product(vy), x.dot_product(vz));
        Point hy = new Point(y.dot_product(vx), y.dot_product(vy), y.dot_product(vz));
        Point hz = new Point(z.dot_product(vx), z.dot_product(vy), z.dot_product(vz));
        Point vw = new Point(x.dot_product(m.w) + w.x, y.dot_product(m.w) + w.y, z.dot_product(m.w) + w.z);
        return new Matrix(hx, hy, hz, vw);
    }

    /**
     * 伸縮
     * @param k
     * @return
     */
    public Matrix scale(float k) {
        return scale(k, k, k);
    }

    /**
     * 伸縮
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Matrix scale(double x, double y, double z) {
        Point px = new Point(   x, 0.0f, 0.0f);
        Point py = new Point(0.0f,    y, 0.0f);
        Point pz = new Point(0.0f, 0.0f,    z);
        return product(new Matrix(px, py, pz));
    }

    /**
     * 伸縮
     * @param k
     * @return
     */
    public Matrix scale(Point k) {
        return scale(k.getX(), k.getY(), k.getZ());
    }

    /**
     * 移動
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Matrix translate(double x, double y, double z) {
        Point px = new Point(1.0f, 0.0f, 0.0f);
        Point py = new Point(0.0f, 1.0f, 0.0f);
        Point pz = new Point(0.0f, 0.0f, 1.0f);
        Point pw = new Point(x, y, z);
        return product(new Matrix(px, py, pz, pw));
    }

    /**
     * 移動
     * @param d
     * @return
     */
    public Matrix translate(Point d) {
        return translate(d.getX(), d.getY(), d.getZ());
    }

    /**
     * 回転（Z軸周り）
     * @param a	回転角度(度)
     * @return
     */
    public Matrix rotate(double a) {
        double d = a * 180.0f / Math.PI;
        double c = Math.cos(d);
        double s = Math.sin(d);
        Point px = new Point(   c,   -s, 0.0f);
        Point py = new Point(   s,    c, 0.0f);
        Point pz = new Point(0.0f, 0.0f, 1.0f);
        return product(new Matrix(px, py, pz));
    }

    public Point apply(Point v) {
        double dx = x.dot_product(v) + w.x;
        double dy = y.dot_product(v) + w.y;
        double dz = z.dot_product(v) + w.z;
        return new Point(dx, dy, dz);
    }

    public Vector<Point> apply(Vector<Point> va) {
        Vector<Point> d = new Vector<Point>();
        for (Point v: va) d.add(apply(v));
        return d;
    }

    public boolean isIdentity () {
        return (x.x == 1.0f) && (x.y == 0.0f) && (x.z == 0.0f) && (w.x == 0.0f) &&
               (y.x == 0.0f) && (y.y == 1.0f) && (y.z == 0.0f) && (w.y == 0.0f) &&
               (z.x == 0.0f) && (z.y == 0.0f) && (z.z == 1.0f) && (w.z == 0.0f);

    }

    public double getScaleX() {
        return x.x;
    }

    public double getScaleY() {
        return y.y;
    }

    public double getScaleZ() {
        return z.z;
    }

    public double getSkewXY() {
        return x.y;
    }

    public double getSkewXZ() {
        return x.z;
    }

    public double getSkewYX() {
        return y.x;
    }

    public double getSkewYZ() {
        return y.z;
    }

    public double getSkewZX() {
        return z.x;
    }

    public double getSkewZY() {
        return z.y;
    }

    public double getTransX() {
        return w.x;
    }

    public double getTransY() {
        return w.y;
    }

    public double getTransZ() {
        return w.z;
    }

    public static Matrix scaleMatrix(double x, double y, double z) {
        Point px = new Point(x, 0, 0);
        Point py = new Point(0, y, 0);
        Point pz = new Point(0, 0, z);
        return new Matrix(px, py, pz);
    }

    public static Matrix translateMatrix(double x, double y, double z) {
        return translateMatrix(new Point(-x, -y, -z));
    }

    public static Matrix translateMatrix(Point pw) {
        Point px = new Point(1, 0, 0);
        Point py = new Point(0, 1, 0);
        Point pz = new Point(0, 0, 1);
        return new Matrix(px, py, pz, pw);
    }
}
