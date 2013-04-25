package com.abplus.dxf;

import com.abplus.dxf.section.*;

import java.io.IOException;

/**
 * Copyright (C) 2012 ABplus Inc. kazhida
 * All rights reserved.
 * Author:  kazhida
 * Created: 12/02/26 10:30
 */
public abstract class CustomParser extends      Parser
                                   implements   Header.Callback,
                                                Classes.Callback,
                                                Tables.Callback,
                                                Blocks.Callback,
                                                Entities.Callback,
                                                Objects.Callback,
                                                ThumbnailImage.Callback {

    @Override
    protected Header.Callback getHeaderCallback() {
        return this;
    }

    @Override
    protected Classes.Callback getClassesCallback() {
        return this;
    }

    @Override
    protected Tables.Callback getTablesCallback() {
        return this;
    }

    @Override
    protected Blocks.Callback getBlocksCallback() {
        return this;
    }

    @Override
    protected Entities.Callback getEntitiesCallback() {
        return this;
    }

    @Override
    protected Objects.Callback getObjectsCallback() {
        return this;
    }

    @Override
    protected ThumbnailImage.Callback getThumbnailImageCallback() {
        return this;
    }
}
