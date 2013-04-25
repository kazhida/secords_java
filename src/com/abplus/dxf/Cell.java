package com.abplus.dxf;

import java.io.UnsupportedEncodingException;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/24 20:08
 */
public class Cell {

    enum Type {
        SHT_CELL,
        INT_CELL,
        LNG_CELL,
        HND_CELL,
        DBL_CELL,
        STR_CELL,
        PNT_CELL,
        UNKNOWN
    }

    private int grp;
    private String src = null;

    /**
     * コンストラクタ
     * @param grp
     * @param src
     */
    public Cell(int grp, String src) {
        super();
        this.grp = grp;
        this.src = src;
    }

    protected Cell(int grp) {
        super();
        this.grp = grp;
    }

    /**
     * グループコード
     * @return
     */
    final public int groupCode() {
        return grp;
    }

    /**
     * 最初に与えられた値を表す文字列
     * @return
     */
    final public String source() {
        return src;
    }

    /**
     * 16bit整数値
     * @return
     */
    public short asShort() {
        return Short.parseShort(src);
    }

    /**
     * 32bit整数値
     * @return
     */
    public int asInteger() {
        return Integer.parseInt(src);
    }

    /**
     * 64bit整数値
     * @return
     */
    public long asLong() {
        return Long.parseLong(src);
    }

    /**
     * 浮動小数点数値
     * @return
     */
    public double asDouble() {
        return Double.parseDouble(src);
    }

    /**
     * 値の文字列
     * @return
     */
    public String asString() {
        return src;
    }

    /**
     * ハンドル値
     * @return
     */
    public long asHandle() {
        return 0;
    }

    /**
     * 座標値またはベクトル値
     * @return
     */
    public Point asPoint() {
        return null;
    }

    /**
     * 整数セル
     */
    public class IntCell extends Cell {

        private int val;

        public IntCell(int grp, int src) {
            super(grp);
            val = src;
        }

        @Override
        public short asShort() {
            return (short)val;
        }

        @Override
        public int asInteger() {
            return val;
        }

        @Override
        public long asLong() {
            return (long)val;
        }

        @Override
        public double asDouble() {
            return (double)val;
        }

        @Override
        public String asString() {
            return "" + val;
        }
    }

    /**
     * 長整数セル
     */
    public class LngCell extends Cell {

        private long val;

        public LngCell(int grp, long src) {
            super(grp);
            val = src;
        }

        @Override
        public short asShort() {
            return (short)val;
        }

        @Override
        public int asInteger() {
            return (int)val;
        }

        @Override
        public long asLong() {
            return val;
        }

        @Override
        public double asDouble() {
            return (double)val;
        }

        @Override
        public String asString() {
            return "" + val;
        }

        @Override
        public long asHandle() {
            return val;
        }
    }

    /**
     * ハンドルセル
     */
    public class HndCell extends Cell {

        private long val;

        public HndCell(int grp, long src) {
            super(grp);
            val = src;
        }

        @Override
        public short asShort() {
            return (short)val;
        }

        @Override
        public int asInteger() {
            return (int)val;
        }

        @Override
        public long asLong() {
            return val;
        }

        @Override
        public double asDouble() {
            return (double)val;
        }

        @Override
        public String asString() {
            return "" + val;
        }

        @Override
        public long asHandle() {
            return val;
        }
    }

    /**
     * 実数セル
     */
    public class DblCell extends Cell {

        private double val;

        public DblCell(int grp, double src) {
            super(grp);
            val = src;
        }

        @Override
        public short asShort() {
            return (short)val;
        }

        @Override
        public int asInteger() {
            return (int)val;
        }

        @Override
        public long asLong() {
            return (long)val;
        }

        @Override
        public double asDouble() {
            return val;
        }

        @Override
        public String asString() {
            return "" + val;
        }
    }

    /**
     * 文字列セル
     */
    public class StrCell extends Cell {

        private String val = null;

        public StrCell(int grp, String src) {
            super(grp, src);
        }

        @Override
        public short asShort() {
            return 0;
        }

        @Override
        public int asInteger() {
            return 0;
        }

        @Override
        public long asLong() {
            return 0;
        }

        @Override
        public double asDouble() {
            return 0;
        }

        @Override
        public String asString() {
            /*TODO: resolve encoding*/
            val = source();
            if (val != null) {
                try {
                    val = new String(val.getBytes("8859_1"), "Shift_JIS");
                }  catch (UnsupportedEncodingException e) {
                    val = source();
                }
            }
            return val;
        }
    }

    public class PntCell extends Cell {
        private Point p;

        public PntCell(int grp, double x, double y, double z) {
            super(grp);
            p = new Point(x, y, z);
        }
        public double getX() {
            return p.x;
        }

        public double getY() {
            return p.y;
        }

        public double getZ() {
            return p.z;
        }

        @Override
        public Point asPoint() {
            return p;
        }
    }

    /**
     * セルを生成するときに使うインスタンス
     */
    static private Cell factory = new Cell(0, "");

    static public IntCell createIntCell(int grp, int src) {
        return factory.new IntCell(grp, src);
    }

    static public LngCell createLngCell(int grp, long src) {
        return factory.new LngCell(grp, src);
    }

    static public DblCell createDblCell(int grp, double src) {
        return factory.new DblCell(grp, src);
    }

    static public HndCell createHndCell(int grp, long src) {
        return factory.new HndCell(grp, src);
    }

    static public StrCell createStrCell(int grp, String src) {
        return factory.new StrCell(grp, src);
    }

    static public Cell createCell(int grp, String src) {
        Type type = typeOf(grp);
        if (type.ordinal() == Type.STR_CELL.ordinal()) return createStrCell(grp, src);
        if (type.ordinal() == Type.SHT_CELL.ordinal()) return createIntCell(grp, Integer.parseInt(src));
        if (type.ordinal() == Type.INT_CELL.ordinal()) return createIntCell(grp, Integer.parseInt(src));
        if (type.ordinal() == Type.LNG_CELL.ordinal()) return createLngCell(grp, Long.parseLong(src));
        if (type.ordinal() == Type.DBL_CELL.ordinal()) return createDblCell(grp, Double.parseDouble(src));
        if (type.ordinal() == Type.PNT_CELL.ordinal()) return createDblCell(grp, Double.parseDouble(src));  //とりあえず
        if (type.ordinal() == Type.HND_CELL.ordinal()) return createHndCell(grp, Long.parseLong(src, 16));
        return new Cell(grp, src);  //UNKNOWN
    }

    static public Type typeOf(int grp) {
        if (grp < 0)          return Type.UNKNOWN;
        else if (grp <     5) return Type.STR_CELL;  //String (with the introduction of extended symbol names in AutoCAD 2000, the 255-character limit has been increased to 2049 single-byte characters not including the newline at the end of the line)
        else if (grp ==    5) return Type.HND_CELL;  //Entity Handle
        else if (grp <    10) return Type.STR_CELL;  //String (with the introduction of extended symbol names in AutoCAD 2000, the 255-character limit has been increased to 2049 single-byte characters not including the newline at the end of the line)
        else if (grp <    40) return Type.PNT_CELL;  //Double precision 3D point value
        else if (grp <    60) return Type.DBL_CELL;  //Double-precision floating-point value
        else if (grp <    80) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <    90) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp <   100) return Type.INT_CELL;  //32-bit integer value
        else if (grp ==  100) return Type.STR_CELL;  //String (255-character maximum; less for Unicode strings)
        else if (grp <   102) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp ==  102) return Type.STR_CELL;  //String (255-character maximum; less for Unicode strings)
        else if (grp <   105) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp ==  105) return Type.HND_CELL;  //String representing hexadecimal (hex) handle value
        else if (grp <   110) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp <   140) return Type.DBL_CELL;  //Double-precision floating-point value
        else if (grp <   150) return Type.DBL_CELL;  //Double precision scalar floating-point value
        else if (grp <   160) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp <   170) return Type.LNG_CELL;  //64-bit integer value
        else if (grp <   180) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <   210) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp <   240) return Type.PNT_CELL;  //Double-precision floating-point value
            //	たぶんここまでで、最初の世代
        else if (grp <   270) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp <   280) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <   290) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <   300) return Type.SHT_CELL;  //Boolean flag value
        else if (grp <   310) return Type.STR_CELL;  //Arbitrary text string
        else if (grp <   320) return Type.STR_CELL;  //String representing hex value of binary chunk
        else if (grp <   330) return Type.HND_CELL;  //String representing hex handle value
        else if (grp <   370) return Type.HND_CELL;  //String representing hex object IDs
        else if (grp <   380) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <   390) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <   400) return Type.HND_CELL;  //String representing hex handle value
            //	ここでも世代の断絶を感じる
        else if (grp <   410) return Type.SHT_CELL;  //16-bit integer value
        else if (grp <   420) return Type.SHT_CELL;  //String
        else if (grp <   430) return Type.INT_CELL;  //32-bit integer value
        else if (grp <   440) return Type.SHT_CELL;  //String
        else if (grp <   450) return Type.INT_CELL;  //32-bit integer value
        else if (grp <   460) return Type.LNG_CELL;  //Long
        else if (grp <   470) return Type.DBL_CELL;  //Double-precision floating-point value
        else if (grp <   480) return Type.SHT_CELL;  //String
        else if (grp <=  481) return Type.HND_CELL;  //String representing hex handle value
        else if (grp <   999) return Type.UNKNOWN;   // ???? 欠番 ????
        else if (grp ==  999) return Type.STR_CELL;  //Comment (string)
            //	ここも世代の断絶
        else if (grp <  1010) return Type.STR_CELL;  //String (same limits as indicated with 0-9 code range)
        else if (grp <  1060) return Type.DBL_CELL;  //Double-precision floating-point value
        else if (grp <  1070) return Type.SHT_CELL;  //16-bit integer value
        else if (grp == 1070) return Type.SHT_CELL;  //16-bit integer value
        else if (grp == 1071) return Type.INT_CELL;  //32-bit integer value
        else                  return Type.UNKNOWN;
    }
}
/*
Group codes by number
    -5  APP: persistent reactor chain
    -4  APP: conditional operator (used only with ssget)
    -3  APP: extended data (XDATA) sentinel (fixed)
    -2  APP: entity name reference (fixed)
    -1  APP: entity name. The name changes each time a drawing is opened. It is never saved (fixed)
     0  Text string indicating the entity type (fixed)
     1  Primary text value for an entity
     2  Name (attribute tag, block name, and so on)
   3-4  Other text or name values
     5  Entity handle; text string of up to 16 hexadecimal digits (fixed)
     6  Linetype name (fixed)
     7  Text style name (fixed)
     8  Layer name (fixed)
     9  DXF: variable name identifier (used only in HEADER section of the DXF file)
    10(20,30)  Primary point; this is the start point of a line or text entity, center of a circle, and so on DXF: X value of the primary point (followed by Y and Z value codes 20 and 30)
 11-18(21-28,31-38)  Other points APP: 3D point (list of three reals)DXF: X value of other points (followed by Y value codes 21-28 and Z value codes 31-38) APP: 3D point (list of three reals)
    38  DXF: entity's elevation if nonzero
    39  Entity's thickness if nonzero (fixed)
 40-48  Double-precision floating-point values (text height, scale factors, and so on)
    48  Linetype scale; double precision floating point scalar value; default value is defined for all entity types
    49  Repeated double-precision floating-point value. Multiple 49 groups may appear in one entity for variable-length tables (such as the dash lengths in the LTYPE table). A 7x group always appears before the first 49 group to specify the table length
 50-58  Angles (output in degrees to DXF files and radians through AutoLISP and ObjectARX applications)
    60  Entity visibility; integer value; absence or 0 indicates visibility; 1indicates invisibility
    62  Color number (fixed)
    66  "Entities follow" flag (fixed)
    67  Space—that is, model or paper space (fixed)
    68  APP: identifies whether viewport is on but fully off screen; is not active or is off
    69  APP: viewport identification number
 70-78   Integer values, such as repeat counts, flag bits, or modes
 90-99   32-bit integer values
   100 Subclass data marker (with derived class name as a string).
   102 Control string, followed by “{<arbitrary name>” or “}”.
   105  Object handle for DIMVAR symbol table entry
   110(120,130) UCS origin (appears only if code 72 is set to 1) DXF: X value; APP: 3D point
   111(121,131) UCS X-axis (appears only if code 72 is set to 1) DXF: X value; APP: 3D vector
   112(122,132) UCS Y-axis (appears only if code 72 is set to 1) DXF: X value; APP: 3D vector
140-149 Double-precision floating-point values (points, elevation, and DIMSTYLE settings, for example)
170-179 16-bit integer values, such as flag bits representing DIMSTYLE settings
   210(220,230) Extrusion direction (fixed)
 */

