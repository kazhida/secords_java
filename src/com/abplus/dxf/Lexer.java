package com.abplus.dxf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/24 20:06
 */
public class Lexer {
    private BufferedReader reader = null;
    private int     grp;
    private String  val;
    private int     lno = 0;
    private boolean eof = false;

    private int     grpBacked;
    private String  valBacked;
    private boolean backed = false;

    /**
     * コンストラクタには、InputStreamを渡す
     * @param src	DXFファイルのInputStream
     * @throws java.io.IOException
     */
    public Lexer(InputStream src) throws IOException {
        super();
        //エンコーディングの問題はいわゆるLatin_1で読み込むことで、問題を先送りにする。
        reader = new BufferedReader(new InputStreamReader(src, "8859_1"));
    }

    /**
     * 先読みしていた場合は、最初のセルの情報もあわせて渡す
     * @param src	DXFファイルのInputStream
     * @param grp   グループコード
     * @param val   値
     * @throws IOException
     */
    Lexer(InputStream src, int grp, String val) throws IOException {
        super();
        reader = new BufferedReader(new InputStreamReader(src));
        grpBacked = grp;
        valBacked = val;
        backed = true;
    }

    /**
     * 一行読み込むメソッド
     * 行番号を更新したり、ファイル終端に達したらフラグをたてたりするので、
     * その辺りをまとめてある。
     * @return
     * @throws IOException
     */
    private String readLine() throws IOException {
        String s = reader.readLine().trim();
        lno++;
        if (s == null) {
            eof = true;
            return null;
        }
        return s;
    }

    /**
     * 次のオブジェクトを読み込む
     * 以降のプロパティは、ここで読み込んだ結果が返される
     * @return	読み込めたらtrueファイルの終端に達していたらfalse
     * @throws IOException
     */
    public int fetch() throws IOException {
        if (backed) {
            //戻されたのがあれば、それを使う
            grp = grpBacked;
            val = valBacked;
            backed = false;
            return grp;
        }
        //	こういう場合以外は、ちゃんと読み込む
        String s = readLine().trim();
        if (s == null) return 0;
        if (s.trim().length() == 0) {
            //value部分で、空行が入ってしまう処理系があるらしいので、その対策
            s = readLine();
            if (s == null) return 0;
        }
        grp = Short.parseShort(s.trim());
        val = readLine();
        lno++;
        if (val == null) {
            val = "";
            return 0;
        } else {
            return grp;
        }
    }

    /**
     * チェック機能つきfetch
     * @param grp   期待するグループコード
     * @param val   期待する値
     * @return
     * @throws IOException
     */
    public boolean fetch(int grp, String val) throws IOException {
        return fetch() == grp && this.val.equals(val);
    }

    /**
     * 直前のfetchをなかったことにする。
     * 一回のfetchに一度だけ使える。
     */
    public void unfetch() {
        grpBacked = grp;
        valBacked = val;
        backed = true;
    }

    /**
     * 読み込んだ値が指定した値と等しければ、trueを返すメソッド
     * @param val
     * @return
     */
    public boolean equals(String val) {
        return this.val.equals(val);
    }

    public void skipTo(int grp) throws IOException {
        while (fetch() != grp);
    }

    public void skipTo(int grp, String val) throws IOException {
        while (fetch() != grp && equals(val));
    }

    /*-----------------------
      *	プロパティ
      *-----------------------*/

    /**
     * いわゆるエンドオブファイル
     * @return	ファイルの終端に達していたらtrue
     */
    public boolean eof() {
        return this.eof;
    }

    /**
     * 直前のfetchで読み込んだ行番号
     * @return
     */
    public int lineNo() {
        return lno;
    }

    /**
     * グループコード
     * @return	直前のfetchで読み込んだグループコード
     */
    public int groupCode() {
        return grp;
    }

    /**
     * 文字列としての値。
     * これはファイルから読み込んだ値がそのまま返される。
     * @return	直前のfetchで読み込んだ値
     */
    public String rawValue() {
        if (eof) {
            return "EOF";
        } else {
            return val;
        }
    }

    public Cell get() throws IOException {
        Cell cell = Cell.createCell(grp, val);

        if (Cell.typeOf(cell.groupCode()).ordinal() == Cell.Type.PNT_CELL.ordinal()) {
            //  座標型の時は、次と次の次も使う
            int grp = cell.groupCode();
            double x = cell.asDouble();
            double y = 0;
            double z = 0;
            if (fetch() == grp + 10) {
                y = Double.parseDouble(val);
            } else {
                unfetch();
            }
            if (fetch() == grp + 20) {
                z = Double.parseDouble(val);
            } else {
                unfetch();
            }
            cell = cell.new PntCell(grp, x, y, z);
        }
        return cell;
    }

    public static final String ENDSEC = "ENDSEC";
}
