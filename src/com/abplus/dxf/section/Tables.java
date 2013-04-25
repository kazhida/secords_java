package com.abplus.dxf.section;

import com.abplus.dxf.Entity;
import com.abplus.dxf.Lexer;
import com.abplus.dxf.section.table.*;

import java.io.IOException;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 10:34
 */
public class Tables {

    public interface Callback {
        public boolean handleAppID(Entity rec);
        public boolean handleBlockRecord(Entity rec);
        public boolean handleDimStyle(Entity rec);
        public boolean handleLayer(Entity rec);
        public boolean handleLineType(Entity rec);
        public boolean handleStyle(Entity rec);
        public boolean handleUCS(Entity rec);
        public boolean handleView(Entity rec);
        public boolean handleViewPort(Entity rec);
    }

    public static final String SECTION_NAME = "TABLES";

    private AppID       appID       = null;
    private BlockRecord blockRecord = null;
    private DimStyle    dimStyle    = null;
    private Layer       layer       = null;
    private LineType    lineType    = null;
    private Style       style       = null;
    private UCS         ucs         = null;
    private View        view        = null;
    private ViewPort    viewPort    = null;

    public static final String ENDTAB = "ENDTAB";

    public Tables(Lexer lex, Callback cb) throws IOException {
        super();
        while (!lex.fetch(0, Lexer.ENDSEC)) {
            if (lex.fetch() == 2) {
                String name = lex.rawValue();
                if (name.equals(AppID.TABLE_NAME))       appID       = new AppID(lex, cb);
                if (name.equals(BlockRecord.TABLE_NAME)) blockRecord = new BlockRecord(lex, cb);
                if (name.equals(DimStyle.TABLE_NAME))    dimStyle    = new DimStyle(lex, cb);
                if (name.equals(Layer.TABLE_NAME))       layer       = new Layer(lex, cb);
                if (name.equals(LineType.TABLE_NAME))    lineType    = new LineType(lex, cb);
                if (name.equals(Style.TABLE_NAME))       style       = new Style(lex, cb);
                if (name.equals(UCS.TABLE_NAME))         ucs         = new UCS(lex, cb);
                if (name.equals(View.TABLE_NAME))        view        = new View(lex, cb);
                if (name.equals(ViewPort.TABLE_NAME))    viewPort    = new ViewPort(lex, cb);
            }
        }
    }

    public AppID getAppID() {
        return appID;
    }

    public BlockRecord getBlockRecord() {
        return blockRecord;
    }

    public DimStyle getDimStyle() {
        return dimStyle;
    }

    public Layer getLayer() {
        return layer;
    }

    public LineType getLineType() {
        return lineType;
    }

    public Style getStyle() {
        return style;
    }

    public UCS getUCS() {
        return ucs;
    }

    public View getView() {
        return view;
    }

    public ViewPort getViewPort() {
        return viewPort;
    }
}
