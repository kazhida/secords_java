package com.abplus.dxf;

import com.abplus.dxf.section.*;

import java.io.IOException;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 14:43
 */
public class Parser {

    protected Header header;
    protected Classes classes;
    protected Tables tables;
    protected Blocks blocks;
    protected Entities entities;
    protected Objects  objects;
    protected ThumbnailImage thumbnailImage;

    public static final String SECTION = "SECTION";

    public void parse(Lexer lex) throws IOException {
        while (lex.fetch(0, SECTION)) {
            if (lex.fetch() == 2) {
                String name = lex.rawValue();
                if (name.equals(Header.SECTION_NAME))   header   = new Header(lex, getHeaderCallback());
                if (name.equals(Classes.SECTION_NAME))  classes  = new Classes(lex, getClassesCallback());
                if (name.equals(Tables.SECTION_NAME))   tables   = new Tables(lex, getTablesCallback());
                if (name.equals(Blocks.SECTION_NAME))   blocks   = new Blocks(lex, getBlocksCallback());
                if (name.equals(Entities.SECTION_NAME)) entities = new Entities(lex, getEntitiesCallback());
                if (name.equals(Objects.SECTION_NAME))  objects  = new Objects(lex, getObjectsCallback());
                if (name.equals(ThumbnailImage.SECTION_NAME)) thumbnailImage = new ThumbnailImage(lex, getThumbnailImageCallback());
            }
        }
    }

    protected Header.Callback getHeaderCallback() {
        return null;
    }

    protected Classes.Callback getClassesCallback() {
        return null;
    }

    protected Tables.Callback getTablesCallback() {
        return null;
    }

    protected Blocks.Callback getBlocksCallback() {
        return null;
    }

    protected Entities.Callback getEntitiesCallback() {
        return null;
    }

    protected Objects.Callback getObjectsCallback() {
        return null;
    }

    protected ThumbnailImage.Callback getThumbnailImageCallback() {
        return null;
    }

    public Header getHeader() {
        return header;
    }

    public Classes getClasses() {
        return classes;
    }

    public Tables getTables() {
        return tables;
    }

    public Blocks getBlocks() {
        return blocks;
    }

    public Entities getEntities() {
        return entities;
    }

    public Objects getObjects() {
        return objects;
    }

    public ThumbnailImage getThumbnailImage() {
        return thumbnailImage;
    }
}
