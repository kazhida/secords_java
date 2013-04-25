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
public class ThumbnailImage {

    public interface Callback {
        public boolean handleThumbnailImage(Entity obj);
    }

    public static final String SECTION_NAME = "THUMBNAILIMAGE";

    public ThumbnailImage(Lexer lex, Callback cb) throws IOException {
        super();
        lex.skipTo(0, Lexer.ENDSEC);
    }
}
