package com.abplus.dxf;

import android.telephony.CellLocation;

import java.io.IOException;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 9:16
 */
public class Entity implements Iterable<Cell> {

    private Vector<Cell> buf = new Vector<Cell>();

    public Entity(Lexer lex) throws IOException {
        super();

        buf.add(lex.get());
        while (lex.fetch() != 0) {
            buf.add(lex.get());
        }

        lex.unfetch();
    }

    public int size() {
        return buf.size();
    }

    public Cell get(int idx) {
        return buf.get(idx);
    }




    public Cell assoc(int grp) {
        for (Cell cell: buf) {
            if (cell.groupCode() == grp) return cell;
        }
        return null;
    }

    public String name() {
        return asString(2);
    }

    public String asString(int grp, String def) {
        Cell cell = assoc(grp);
        if (cell == null) {
            return def;
        } else {
            return cell.asString();
        }
    }

    public String asString(int grp) {
        return asString(grp, "");
    }

    public int asInteger(int grp, int def) {
        Cell cell = assoc(grp);
        if (cell == null) {
            return def;
        } else {
            return cell.asInteger();
        }
    }

    public int asInteger(int grp) {
        return asInteger(grp, 0);
    }

    public double  asDouble(int grp, double def) {
        Cell cell = assoc(grp);
        if (cell == null) {
            return def;
        } else {
            return cell.asDouble();
        }
    }

    public double  asDouble(int grp) {
        return asDouble(grp, 0.0);
    }

    public Point asPoint(int grp, Point def) {
        Cell cell = assoc(grp);
        if (cell != null) {
            return cell.asPoint();
        } else if (def != null) {
            return def;
        } else {
            return new Point(0, 0, 1);
        }
    }

    public Point asPoint(int grp) {
        return asPoint(grp, null);
    }

    /**
     * イテレータ
     * @author kazhida
     *
     */
    class Iterator implements java.util.Iterator<Cell> {

        private Entity ent;
        private int idx = 0;

        Iterator(Entity ent) {
            super();
            this.ent = ent;
        }

        public boolean hasNext() {
            return idx < ent.size();
        }

        public Cell next() {
            return ent.get(idx++);
        }

        public void remove() {
        }
    }

    public java.util.Iterator<Cell> iterator() {
        return this.new Iterator(this);
    }}
