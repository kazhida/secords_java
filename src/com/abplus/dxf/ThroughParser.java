package com.abplus.dxf;

/**
 * すべてスルーするパーサ
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 14:32
 */
public class ThroughParser extends CustomParser {

    @Override
    public boolean handleHeaderParam(String key, Cell val) {
        return true;
    }

    @Override
    public boolean handleClass(Entity ent) {
        return true;
    }

    @Override
    public boolean handleAppID(Entity rec) {
        return true;
    }

    @Override
    public boolean handleBlockRecord(Entity rec) {
        return true;
    }

    @Override
    public boolean handleDimStyle(Entity rec) {
        return true;
    }

    @Override
    public boolean handleLayer(Entity rec) {
        return true;
    }

    @Override
    public boolean handleLineType(Entity rec) {
        return true;
    }

    @Override
    public boolean handleStyle(Entity rec) {
        return true;
    }

    @Override
    public boolean handleUCS(Entity rec) {
        return true;
    }

    @Override
    public boolean handleView(Entity rec) {
        return true;
    }

    @Override
    public boolean handleViewPort(Entity rec) {
        return true;
    }

    @Override
    public boolean handleBeginBlock(Entity ent) {
        return true;
    }

    @Override
    public boolean handleBlockEntity(Entity ent) {
        return true;
    }

    @Override
    public boolean handleEndBlock(Entity ent) {
        return true;
    }

    @Override
    public boolean handleEntity(Entity ent) {
        return true;
    }

    @Override
    public boolean handleObject(Entity obj) {
        return true;
    }

    @Override
    public boolean handleThumbnailImage(Entity obj) {
        return true;
    }
}
