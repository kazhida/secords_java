package com.abplus.dxf.section;

import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;

import java.io.IOException;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 10:10
 */
public class Objects {

    public interface Callback {
        public boolean handleObject(Entity obj);
    }

    public static final String SECTION_NAME = "OBJECTS";

    public Objects(Lexer lex, Callback cb) throws IOException {
        super();
        lex.skipTo(0, Lexer.ENDSEC);
    }
}
