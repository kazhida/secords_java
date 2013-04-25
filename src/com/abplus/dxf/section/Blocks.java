package com.abplus.dxf.section;

import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 9:30
 */
public class Blocks {

    public interface Callback {
        public boolean handleBeginBlock(Entity ent);
        public boolean handleBlockEntity(Entity ent);
        public boolean handleEndBlock(Entity ent);
    }

    public static final String SECTION_NAME = "BLOCKS";

    public class Block {
        private Entity head;
        private Entity tail;
        private Vector<Entity> buf = new Vector<Entity>();

        Block(Entity head) {
            super();
            this.head = head;
        }

        boolean add(Entity ent) {
            return buf.add(ent);
        }

        void setTail(Entity tail) {
            this.tail = tail;
        }

        public String name() {
            return head.name();
        }

        public Entity getHead() {
            return head;
        }

        public Entity getTail() {
            return tail;
        }

        public int size() {
            return buf.size();
        }

        public Entity entities(int idx) {
            return buf.get(idx);
        }
    }

    private Map<String, Block> map = null;

    public static final String BLOCK = "BLOCK";
    public static final String ENDBLK = "ENDBLK";

    public Blocks(Lexer lex, Callback cb) throws IOException {
        super();

        Block blk = null;
        while (!lex.fetch(0, Lexer.ENDSEC)) {
            if (lex.groupCode() == 0) {
                Entity ent = new Entity(lex);

                if (ent.asString(0).equals(BLOCK)) {
                    if (cb == null || cb.handleBeginBlock(ent) == false) {
                        blk = new Block(ent);
                    }
                } else if (ent.asString(0).equals(ENDBLK)) {
                    if (cb == null || cb.handleEndBlock(ent) == false) {
                        if (blk != null) {
                            if (map == null) map = new TreeMap<String, Block>();
                            blk.setTail(ent);
                            map.put(blk.name(), blk);
                        }
                    }
                    blk = null;
                } else {
                    if (cb == null || cb.handleBlockEntity(ent) == false) {
                        if (blk != null) blk.add(ent);
                    }
                }
            }
        }
    }

    public Block get(String name) {
        if (map == null) {
            return null;
        } else {
            return map.get(name);
        }
    }
}
