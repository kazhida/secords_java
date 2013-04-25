package com.abplus.dxf.section;

import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;

import java.io.IOException;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/24 20:28
 */
public class Classes {

    public interface Callback {
        boolean handleClass(Entity cls);
    }

    public static final String SECTION_NAME = "CLASSES";

    private Vector<Entity> buf = null;

    /**
     * コンストラクタ
     * @param lex
     * @throws IOException
     */
    public Classes(Lexer lex, Callback cb) throws IOException {
        super();

        while (!lex.fetch(0, Lexer.ENDSEC)) {
            Entity cls = new Entity(lex);

            if (cb == null || cb.handleClass(cls) == false) {
                if (buf == null) buf = new Vector<Entity>();
                buf.add(cls);
            }
        }
    }

    public int size() {
        if (buf == null) {
            return 0;
        } else {
            return buf.size();
        }
    }

    public Entity get(int idx) {
        if (buf == null) {
            return null;
        } else if (idx < 0) {
            return null;
        } else if (idx < buf.size()) {
            return buf.get(idx);
        } else {
            return null;
        }
    }
}
