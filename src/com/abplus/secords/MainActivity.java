package com.abplus.secords;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.graphics.Color;

import com.abplus.dxf.Cell;
import com.abplus.dxf.Lexer;
import com.abplus.dxf.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    private ViewController viewController;
    private DrawingView view;
    private DxfParser parser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ViewGroup frame = (ViewGroup)findViewById(R.id.base_layout);
        view = new DrawingView(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                                                        ViewGroup.LayoutParams.FILL_PARENT));
        frame.addView(view);

        viewController = new ViewController(this, view);
        view.setController(viewController);

        parser = new DxfParser(view);
        try {
            load(this.getAssets().open("sample.dxf"));
        } catch (IOException e) {
            Layer lyr = new Layer("0", DrawingView.defaultPaint(Color.WHITE));
            Element elm = new TextElement(0, 0, "Cannot open sample.dxf", 10.0f, 0.0f);
            lyr.add(elm);
            view.putLayer("0", lyr);
        }
    }

    private InputStream getStream(String filename) throws IOException {
        Uri uri = Uri.parse(filename);
        String scheme = uri.getScheme();
        String path = uri.getPath();

        if (scheme == null) {
            File f = this.getFileStreamPath(path);
            return new FileInputStream(f);
        } else if (scheme.equals("file")) {
            File f = new File(path);
            return new FileInputStream(f);
        } else if (scheme.equals("content")) {
            //  contents provider.
            return this.getContentResolver().openInputStream(uri);
        } else {
            Log.e("on load", "Unknown scheme(" + scheme + ")");
            return null;
        }
    }

    private void load(InputStream stream) throws IOException {
        Lexer lex = new Lexer(stream);
        parser.parse(lex);
        if (view.getModelHeight() == 0) {
            view.setPreferredGeometry();
        }

        view.debugLog();
    }

    private void load(String uri) throws IOException {
        Lexer lex = new Lexer(getStream(uri));
    }
}
