package com.abplus.dxf.section;

import com.abplus.dxf.Cell;
import com.abplus.dxf.Lexer;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/24 20:12
 */
public class Header {

    public interface Callback {
        public boolean handleHeaderParam(String key, Cell val);
    }

    public static final String SECTION_NAME = "HEADER";

    private Map<String, Cell> params= null;

    /**
     * コンストラクタ
     * パラメータを読み込む。
     * @param lex   レキサ
     * @param cb    コールバック
     * @throws IOException
     */
    public Header(Lexer lex, Callback cb) throws IOException {
        super();

        while (!lex.fetch(0, Lexer.ENDSEC)) {
            String key = lex.rawValue();
            lex.fetch();
            Cell val = lex.get();
            if (cb == null || cb.handleHeaderParam(key, val) == false) {
                if (params == null) params = new TreeMap<String, Cell>();
                params.put(key, val);
            }
        }
    }

    /**
     * パラメータを返すメソッド
     * @param key   変数名
     * @return      変数の値
     */
    public Cell get(String key) {
        if (params == null) {
            return null;
        } else {
            return params.get(key);
        }
    }
}
