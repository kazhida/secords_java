package com.abplus.secords;

import android.graphics.*;
import android.telephony.CellLocation;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import com.abplus.dxf.*;
import com.abplus.dxf.Matrix;
import com.abplus.dxf.Point;
import com.abplus.dxf.section.*;

import java.io.IOException;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 12:42
 */
public class DxfParser extends ThroughParser {

    private DrawingView view;
    private final Matrix mtx0 = new Matrix();
    private Point min;
    private Point max;
    private int   pdMode = 3;
    private float pdSize = 0;

    public DxfParser(DrawingView view) {
        super();
        this.view = view;
    }

    @Override
    public void parse(Lexer lex) throws IOException {
        min = null;
        max = null;
        super.parse(lex);
    }

    @Override
    public boolean handleHeaderParam(String key, Cell val) {

        if (key.equals("$EXTMIN")) min = val.asPoint();
        if (key.equals("$EXTMAX")) max = val.asPoint();

        if (min != null && max != null) {
            float x = (float)((max.getX() + min.getX()) / 2);
            float y = (float)((max.getY() + min.getY()) / 2);
            float h = (float)((max.getY() + min.getY()) * 1.1);
            view.setModelGeometry(x, y, h);
            //  不要なのでクリア
            min = null;
            max = null;
        }

        if (key.equals("$PDMODE")) pdMode = val.asInteger();
        if (key.equals("$PDSIZE")) pdSize = (float)val.asDouble();

        //ヘッダは一応、とっておく
        return false;
    }

    @Override
    public boolean handleLayer(Entity rec) {
        String name = rec.name();
        ColorIndex col = new ColorIndex(rec.asInteger(62));
        Log.d("addLayer", name + "(" + col.index() + ":" + Integer.toHexString(col.color()) + ")");
        //  レイヤの追加
        Paint paint = DrawingView.defaultPaint(col.color());
        view.putLayer(name, new Layer(name, paint));

        return true;
    }

    @Override
    public boolean handleBeginBlock(Entity ent) {
        //  ブロックもとっておく
        return false;
    }

    @Override
    public boolean handleBlockEntity(Entity ent) {
        //  ブロックもとっておく
        return false;
    }

    @Override
    public boolean handleEndBlock(Entity ent) {
        //  ブロックもとっておく
        return false;
    }

    @Override
    public boolean handleEntity(Entity ent) {
        addEntity(mtx0, ent);
        return true;
    }

    /**
     * Entityの追加
     * @param mtx
     * @param ent
     */
    private void addEntity(Matrix mtx, Entity ent) {
        String name = ent.asString(0).intern();
        if (name == CIRCLE)     addCircle(mtx, ent);
//        if (name == DIMENSION)  addDimension(mtx, ent);
        if (name == INSERT)     addInsert(mtx, ent);
        if (name == LEADER)     addLeader(mtx, ent);
        if (name == LINE)       addLine(mtx, ent);
        if (name == LWPOLYLINE) addLwPolyline(mtx, ent);
        if (name == RAY)        addRay(mtx, ent);
        if (name == TEXT)       addText(mtx, ent);
        if (name == MTEXT)      addMText(mtx, ent);
        if (name == XLINE)      addXLine(mtx, ent);

        Log.d("addEntity", name);
    }

    private Matrix toOCS(Matrix mtx, Cell ext) {
        if (ext != null && ext.asPoint() != null) {
            Matrix m2 = new Matrix(ext.asPoint());
            return mtx.product(m2);
        } else {
            return mtx;
        }
    }

    private void applyMatrix(Path path, Matrix mtx, double elevation) {
        if (mtx != mtx0 && (! mtx.isIdentity())) {
            android.graphics.Matrix amtx = new android.graphics.Matrix();

            float[] m = new float[9];
            m[android.graphics.Matrix.MPERSP_0] = 0;
            m[android.graphics.Matrix.MPERSP_1] = 0;
            m[android.graphics.Matrix.MPERSP_2] = 1;

            m[android.graphics.Matrix.MSCALE_X] = (float)mtx.getScaleX();
            m[android.graphics.Matrix.MSCALE_Y] = (float)mtx.getScaleY();
            m[android.graphics.Matrix.MSKEW_X]  = (float)mtx.getSkewXY();
            m[android.graphics.Matrix.MSKEW_Y]  = (float)mtx.getSkewYX();
            m[android.graphics.Matrix.MTRANS_X] = (float)(mtx.getSkewXZ() * elevation + mtx.getTransX());
            m[android.graphics.Matrix.MTRANS_Y] = (float)(mtx.getSkewYZ() * elevation + mtx.getTransY());

            amtx.setValues(m);

            path.transform(amtx);
        }
    }

    private void addToLayer(Element elm, String name) {
        view.layers(name).add(elm);
    }

    //------------------------------------------------------------------------
    private static final String DIMENSION = "DIMENSION".intern();

    private void addDimension(Matrix mtx, Entity ent) {
        Cell name = ent.assoc(2);

        if (name != null) {
            mtx = toOCS(mtx, ent.assoc(210));

            double ra = ent.asDouble(53);

            mtx = mtx.product(Matrix.translateMatrix(ent.asPoint(10)));
            mtx = mtx.rotate(ra);

            Blocks.Block blk = blocks.get(name.asString());
            if (blk != null) {
                mtx = mtx.product(Matrix.translateMatrix(blk.getHead().asPoint(10).reverse()));
                int n = blk.size();
                for (int i = 0; i < n; i++) {
                    addEntity(mtx, blk.entities(i));
                }
                Log.d("addEntity", "DIMENSION");
            }
        }
    }

    //------------------------------------------------------------------------
    private static final String INSERT = "INSERT".intern();

    private void addInsert(Matrix mtx, Entity ent) {
        Cell name = ent.assoc(2);

        if (name != null) {
            mtx = toOCS(mtx, ent.assoc(210));

            double sx = ent.asDouble(41, 1);
            double sy = ent.asDouble(42, 1);
            double sz = ent.asDouble(43, 1);
            double ra = ent.asDouble(50);

            mtx = mtx.product(Matrix.translateMatrix(ent.asPoint(10)));
            mtx = mtx.product(Matrix.scaleMatrix(sx, sy, sz));
            mtx = mtx.rotate(ra);

            Blocks.Block blk = blocks.get(name.asString());
            if (blk != null) {
                mtx = mtx.product(Matrix.translateMatrix(blk.getHead().asPoint(10).reverse()));
                int n = blk.size();
                int cols = ent.asInteger(70, 1);
                int rows = ent.asInteger(71, 1);
                double cs = ent.asDouble(44);
                double rs = ent.asDouble(45);
                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        Matrix mtx2 = mtx.translate(cs * c, rs * r, 0);
                        for (int i = 0; i < n; i++) {
                            addEntity(mtx2, blk.entities(i));
                        }
                    }
                }
                Log.d("addEntity", "INSERT");
            } else {
                Log.d("addEntity", "block(" + name.asString() + ") not found.");
            }
        }
    }

    //------------------------------------------------------------------------
    private static final String CIRCLE = "CIRCLE".intern();

    private void addCircle(Matrix mtx, Entity ent) {
        mtx = toOCS(mtx, ent.assoc(210));

        Point p = ent.asPoint(10);
        double r = ent.asDouble(40);

        Path path = new Path();
        path.addCircle((float)p.getX(), (float)p.getY(), (float)r, Path.Direction.CW);

        applyMatrix(path, mtx, 0);
        addToLayer(new Element(path), ent.asString(8, "0"));
        Log.d("addEntity", "CIRCLE");
    }

    //------------------------------------------------------------------------
    private static final String LEADER = "LEADER".intern();

    private void addLeader(Matrix mtx, Entity ent) {
        mtx = toOCS(mtx, ent.assoc(210));

        Point last = null;
        Path path = new Path();
        for (Cell cell: ent) {
            if (cell.groupCode() == 10) {
                Point p = cell.asPoint();
                if (last == null) {
                    path.moveTo((float)p.getX(), (float)p.getY());
                } else {
                    path.lineTo((float)p.getX(), (float)p.getY());
                }
                last = p;
            }
        }

        int flags = ent.asInteger(70);
        if ((flags & 1) == 1) {
            path.close();
        } else if (last != null) {
            path.setLastPoint((float)last.getX(), (float)last.getY());
        }

        applyMatrix(path, mtx, ent.asDouble(38));
        addToLayer(new Element(path), ent.asString(8, "0"));
        Log.d("addEntity", "Leader");
    }

    //------------------------------------------------------------------------
    private static final String LINE = "LINE".intern();

    private void addLine(Matrix mtx, Entity ent) {
        mtx = toOCS(mtx, ent.assoc(210));

        Point p0 = ent.asPoint(10);
        Point p1 = ent.asPoint(11);

        Path path = new Path();
        path.moveTo((float)p0.getX(), (float)p0.getY());
        path.lineTo((float)p1.getX(), (float)p1.getY());

        applyMatrix(path, mtx, ent.asDouble(38));
        addToLayer(new Element(path), ent.asString(8, "0"));
        Log.d("addEntity", "LINE");
    }

    //------------------------------------------------------------------------
    private static final String LWPOLYLINE = "LWPOLYLINE".intern();

    private void addLwPolyline(Matrix mtx, Entity ent) {
        mtx = toOCS(mtx, ent.assoc(210));

        Point last = null;
        Path path = new Path();
        for (Cell cell: ent) {
            if (cell.groupCode() == 10) {
                Point p = cell.asPoint();
                if (last == null) {
                    path.moveTo((float)p.getX(), (float)p.getY());
                } else {
                    path.lineTo((float)p.getX(), (float)p.getY());
                }
                last = p;
            }
        }

        int flags = ent.asInteger(70);
        if ((flags & 1) == 1) {
            path.close();
        } else if (last != null) {
            path.setLastPoint((float)last.getX(), (float)last.getY());
        }

        applyMatrix(path, mtx, ent.asDouble(38));
        addToLayer(new Element(path), ent.asString(8, "0"));
        Log.d("addEntity", "LwPolyline");
    }

    //------------------------------------------------------------------------
    private static final String RAY = "RAY".intern();

    private void addRay(Matrix mtx, Entity ent) {
        Point p0 = ent.asPoint(10);
        Point v = ent.asPoint(11);
        v = v.scale(view.getModelHeight() * 10);    //十分な大きさ
        Point p1 = p0.sum(v);

        Path path = new Path();
        path.moveTo((float)p0.getX(), (float)p0.getY());
        path.lineTo((float) p1.getX(), (float) p1.getY());

        addToLayer(new Element(path), ent.asString(8, "0"));
        Log.d("addEntity", "Ray");
    }

    //------------------------------------------------------------------------
    private static final String TEXT = "TEXT".intern();

    private void addText(Matrix mtx, Entity ent) {
        String s = ent.asString(1);
        if (s.length() > 0) {
            mtx = toOCS(mtx, ent.assoc(210));

            Point p = mtx.apply(ent.asPoint(10));
            float r = (float)ent.asDouble(50);
            float h = (float)ent.asDouble(40);

            TextElement elm = new TextElement((float)p.getX(), (float)p.getY(), s, h, r);

            switch (ent.asInteger(72)) {
                case 0:
                    elm.setHorizontalAlignment(TextElement.Align.LEFT);
                    break;
                case 1:
                    elm.setHorizontalAlignment(TextElement.Align.CENTER);
                    break;
                case 2:
                    elm.setHorizontalAlignment(TextElement.Align.RIGHT);
                    break;
            }

            switch (ent.asInteger(73)) {
                case 0:
                    elm.setVerticalAlignment(TextElement.Position.BASE);
                    break;
                case 1:
                    elm.setVerticalAlignment(TextElement.Position.BOTTOM);
                    break;
                case 2:
                    elm.setVerticalAlignment(TextElement.Position.MIDDLE);
                    break;
                case 3:
                    elm.setVerticalAlignment(TextElement.Position.TOP);
                    break;
            }
            addToLayer(elm, ent.asString(8, "0"));
            Log.d("addEntity", "TEXT");
        }
    }

    //------------------------------------------------------------------------
    private static final String MTEXT = "MTEXT".intern();

    private void addMText(Matrix mtx, Entity ent) {
        String s = "";
        float r = 0;
        Point a = null;
        for (Cell cell: ent) {
            switch (cell.groupCode()) {
                case 1:
                case 3:
                    s = s + cell.asString();
                    break;
                case 50:
                    r = cell.asInteger();
                    a = null;
                    break;
                case 11:
                    a = cell.asPoint();
                    r = 0;
                    break;
            }
        }
        if (s.length() > 0) {
            mtx = toOCS(mtx, ent.assoc(210));

            Point p = mtx.apply(ent.asPoint(10));
            float h = (float)ent.asDouble(40);
            float w = (float)ent.asDouble(41);
            float sh = (float)ent.asDouble(44) / 100 * h;

            //for debug!
//            Path path = new Path();
//            path.moveTo((float)p.getX() + 10, (float)p.getY() + 10);
//            path.lineTo((float)p.getX() - 10, (float)p.getY() - 10);
//            path.moveTo((float)p.getX() - 10, (float)p.getY() + 10);
//            path.lineTo((float)p.getX() + 10, (float)p.getY() - 10);
//            addToLayer(new Element(path), ent.asString(8, "0"));

            if (a != null) r = (float)Math.atan2(a.getY(), a.getX());

            String[] ts = s.split("\\\\P");
            int ap = ent.asInteger(71);

            switch (ap) {
                case 4:
                case 5:
                case 6:
                    p = p.shift(0, (h + sh) * (ts.length - 1) / 2, 0);
                    break;
                case 7:
                case 8:
                case 9:
                    p = p.shift(0, (h + sh) * (ts.length - 1), 0);
                    break;
            }

            for (String t: ts) {
                TextElement elm = new TextElement((float)p.getX(), (float)p.getY(), t, h, r);
                switch (ap) {
                    case 1:
                    case 4:
                    case 7:
                        elm.setHorizontalAlignment(TextElement.Align.LEFT);
                        break;
                    case 2:
                    case 5:
                    case 8:
                        elm.setHorizontalAlignment(TextElement.Align.CENTER);
                        break;
                    case 3:
                    case 6:
                    case 9:
                        elm.setHorizontalAlignment(TextElement.Align.RIGHT);
                        break;
                }
                switch (ap) {
                    case 1:
                    case 2:
                    case 3:
                        elm.setVerticalAlignment(TextElement.Position.TOP);
                        break;
                    case 4:
                    case 5:
                    case 6:
                        elm.setVerticalAlignment(TextElement.Position.MIDDLE);
                        break;
                    case 7:
                    case 8:
                    case 9:
                        elm.setVerticalAlignment(TextElement.Position.BOTTOM);
                        break;
                }
                addToLayer(elm, ent.asString(8, "0"));
                p = p.shift(0, -(h + sh), 0);
            }
            Log.d("addEntity", "MTEXT");
        }
    }

    //------------------------------------------------------------------------
    private static final String XLINE = "XLINE".intern();

    private void addXLine(Matrix mtx, Entity ent) {
        Point p0 = ent.asPoint(10);
        Point v = ent.asPoint(11);
        v = v.scale(view.getModelHeight() * 10);    //十分な大きさ

        Point p1 = p0.sum(v);
        Point p2 = p0.sum(v.reverse());

        Path path = new Path();
        path.moveTo((float)p1.getX(), (float)p1.getY());
        path.lineTo((float)p2.getX(), (float)p2.getY());

        addToLayer(new Element(path), ent.asString(8, "0"));
        Log.d("addEntity", "XLine");
    }
}
