package com.rsen.github.filldrawable;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Dimitry Ivanov on 26.10.2015.
 */
abstract class CanvasClipper {

    static CanvasClipper create(@FillDrawable.From int directionFrom) {

        switch (directionFrom) {

            case FillDrawable.FROM_LEFT:
                return new LeftClipper();

            case FillDrawable.FROM_TOP:
                return new TopClipper();
            
            case FillDrawable.FROM_RIGHT:
                return new RightClipper();

            case FillDrawable.FROM_BOTTOM:
                return new BottomClipper();

            default:
                throw new IllegalStateException("Unknown `startDirection`: " + directionFrom);
        }
    }

    int w;
    int h;
    int currPivot;

    private CanvasClipper() {
    }

    void setBounds(Rect bounds) {
        w = bounds.width();
        h = bounds.height();
    }

    abstract void prepare(float progress);

    abstract void clipNormal(Canvas canvas);

    abstract void clipProgress(Canvas canvas);


    private static abstract class HorizontalClipper extends CanvasClipper {
        @Override
        void prepare(float progress) {
            currPivot = (int) (progress * w + .5F);
        }
    }

    private static abstract class VerticalClipper extends CanvasClipper {
        @Override
        void prepare(float progress) {
            currPivot = (int) (progress * h + .5F);
        }
    }


    private static class LeftClipper extends HorizontalClipper {

        @Override
        void clipNormal(Canvas canvas) {
            canvas.clipRect(currPivot, 0, w, h);
        }

        @Override
        void clipProgress(Canvas canvas) {
            canvas.clipRect(0, 0, currPivot, h);
        }
    }

    private static class TopClipper extends VerticalClipper {

        @Override
        void clipNormal(Canvas canvas) {
            canvas.clipRect(0, currPivot, w, h);
        }

        @Override
        void clipProgress(Canvas canvas) {
            canvas.clipRect(0, 0, w, currPivot);
        }
    }

    private static class RightClipper extends HorizontalClipper {

        @Override
        void clipNormal(Canvas canvas) {
            canvas.clipRect(0, 0, w - currPivot, h);
        }

        @Override
        void clipProgress(Canvas canvas) {
            canvas.clipRect(w - currPivot, 0, w, h);
        }
    }

    private static class BottomClipper extends VerticalClipper {

        @Override
        void clipNormal(Canvas canvas) {
            canvas.clipRect(0, 0, w, h - currPivot);
        }

        @Override
        void clipProgress(Canvas canvas) {
            canvas.clipRect(0, h - currPivot, w, h);
        }
    }
}
