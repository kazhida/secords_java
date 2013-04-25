package com.abplus.dxf.section.table;

import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;
import com.abplus.dxf.section.Tables;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 12:13
 */
public abstract class AbstractTable {

    protected Entity head;
    protected Vector<Entity> buf = new Vector<Entity>();
    protected Map<String, Entity> map = new TreeMap<String, Entity>();

    public AbstractTable(Lexer lex, Tables.Callback cb) throws IOException {
        super();

        head = new Entity(lex);

        while (! lex.fetch(0, Tables.ENDTAB)) {
            Entity ent = new Entity(lex);
            if (cb == null || handleRecord(cb, ent) == false) {
                buf.add(ent);
                String name = ent.name();
                if (name.length() > 0) {
                    map.put(name, ent);
                }
            }
        }
    }

    protected abstract boolean handleRecord(Tables.Callback cb, Entity rec);

    public int recordCount() {
        return buf.size();
    }

    public Entity records(int idx) {
        return buf.get(idx);
    }

    public Entity records(String name) {
        return map.get(name);
    }
}
