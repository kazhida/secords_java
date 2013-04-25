package com.abplus.dxf.section.table;

import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;
import com.abplus.dxf.section.Tables;

import java.io.IOException;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 10:38
 */
public class Style extends AbstractTable {

    public static final String TABLE_NAME = "STYLE";

    public Style(Lexer lex, Tables.Callback cb) throws IOException {
        super(lex, cb);
    }

    protected boolean handleRecord(Tables.Callback cb, Entity rec) {
        return cb.handleStyle(rec);
    }
}
