package com.abplus.dxf;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/24 20:09
 */
public class Point {

    protected double y;
    protected double x;
    protected double z;

    /**
     * 座標を指定して生成するコンストラクタ
     * @param x X座標（もしくはX成分）
     * @param y Y座標（もしくはY成分）
     * @param z Z座標（もしくはZ成分）
     */
    public Point(double x, double y, double z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * コピーコンストラクタ
     * @param p コピーもとのPoint
     */
    public Point(Point p) {
        super();
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    /**
     * X座標（もしくはX成分）を返すメソッド
     * @return  X座標（もしくはX成分）
     */
    public double getX() {
        return x;
    }

    /**
     * Y座標（もしくはY成分）を返すメソッド
     * @return  Y座標（もしくはY成分）
     */
    public double getY() {
        return y;
    }

    /**
     * Z座標（もしくはZ成分）を返すメソッド
     * @return  Z座標（もしくはZ成分）
     */
    public double getZ() {
        return z;
    }

    /**
     * ノルムを返すメソッド
     * @return  ノルム
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * 同じ方向の単位ベクトルを返すメソッド
     * @return  単位方向ベクトル
     */
    public Point unit() {
        double n = norm();
        return new Point(x / n, y / n, z / n);
    }

    /**
     * 各成分の加算したベクトルを返すメソッド
     * @param p 加算するベクトル
     * @return  加算したベクトル
     */
    public Point sum(Point p) {
        return new Point(this.x + p.x, this.y + p.y, this.z + p.z);
    }

    /**
     * 各成分の減算したベクトルを返すメソッド
     * @param p 減算するベクトル
     * @return  減算したベクトル
     */
    public Point difference(Point p) {
        return new Point(this.x - p.x, this.y - p.y, this.z - p.z);
    }

    /**
     * ドット積を返すメソッド
     * @param p 掛けるベクトル
     * @return  ドット積
     */
    public double dot_product(Point p) {
        return this.x * p.x + this.y * p.y + this.z * p.z;
    }

    /**
     * クロス積を返すメソッド
     * @param p 掛けるベクトル
     * @return  クロス積
     */
    public Point cross_product(Point p) {
        double x = this.y * p.z - this.z * p.y;
        double y = this.z * p.x - this.x * p.z;
        double z = this.x * p.y - this.y * p.x;
        return new Point(x, y, z);
    }

    public Point scale(double k) {
        return new Point(x * k, y * k, z * k);
    }

    public Point shift(double x, double y, double z) {
        return new Point(this.x + x, this.y + y, this.z + z);
    }

    public Point reverse() {
        return new Point(-x, -y, -z);
    }


    /**
     * 可変なPoint
     */
    public class MutablePoint extends Point {

        /**
         * コピーコンストラクタのみ
         * @param p コピー元のPoint
         */
        public MutablePoint(Point p) {
            super(p);
        }

        /**
         * X座標（もしくはX成分）を設定するメソッド
         * @param x X座標（もしくはX成分）
         */
        public void setX(double x) {
            this.x = x;
        }

        /**
         * Y座標（もしくはY成分）を設定するメソッド
         * @param y Y座標（もしくはY成分）
         */
        public void setY(double y) {
            this.y = y;
        }

        /**
         * Z座標（もしくはZ成分）を設定するメソッド
         * @param z Z座標（もしくはZ成分）
         */
        public void setZ(double z) {
            this.z = z;
        }
    }

    /**
     * 可変なPointとしてコピーを作成するメソッド
     * @return  コピーしたMutablePoint
     */
    public MutablePoint copyMutable() {
        return this.new MutablePoint(this);
    }
}
