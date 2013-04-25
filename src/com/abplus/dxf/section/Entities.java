package com.abplus.dxf.section;

import com.abplus.dxf.Cell;
import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;

import java.io.IOException;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 10:05
 */
public class Entities {

    public interface Callback {
        public boolean handleEntity(Entity ent);
    }

    public static final String SECTION_NAME = "ENTITIES";

    private Vector<Entity> buf = null;

    public Entities(Lexer lex, Callback cb) throws IOException {
        super();

        while (!lex.fetch(0, Lexer.ENDSEC)) {
            Entity ent = new Entity(lex);

            if (cb == null || cb.handleEntity(ent) == false) {
                if (buf == null) buf = new Vector<Entity>();
                buf.add(ent);
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
