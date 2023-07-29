package com.pinonzhyk.coinssnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.Collection;

public class RenderView extends View  {

    private int visibleWidthUnit;
    private int visibleHeightUnit;
    private Collection<WorldObject> renderObjects;
    private float pixelsPerSceneUnit;
    private Paint paint = new Paint();
    private InputCallback inputCallback;
    private int fpsDebug;
    private Paint fpsPaint;

    public RenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fpsPaint = new Paint();
        fpsPaint.setTextSize(32);
    }

    public void setInputCallback(InputCallback inputCallback) {
        this.inputCallback = inputCallback;
    }

    public void setVisibleBounds(int widthUnits, int heightUnits) {
        this.visibleWidthUnit = widthUnits;
        this.visibleHeightUnit = heightUnits;
        calculateUnitScale();
        invalidate();
    }

    public void renderObjects(Collection<WorldObject> renderObjects) {
        this.renderObjects = renderObjects;
        invalidate();
    }

    public void setFpsDebug(int fpsDebug) {
        this.fpsDebug = fpsDebug;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (inputCallback == null) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) ((event.getX() / getWidth()) * visibleWidthUnit);
            int y = (int) ((event.getY() / getHeight()) * visibleHeightUnit);
            inputCallback.onClick(x, y);
        }

        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (visibleWidthUnit != 0 && visibleHeightUnit != 0) {
            calculateUnitScale();
        }
    }

    private void calculateUnitScale() {
        float widthScale = ((float) getWidth() / visibleWidthUnit);
        float heightScale = ((float) getHeight() / visibleHeightUnit);
        pixelsPerSceneUnit = Math.min(widthScale, heightScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("FPS: " + fpsDebug, 50, 74, fpsPaint);

        // view borders
        drawLineSquare(canvas, getWidth(), getHeight(), paint);
        // render borders
        drawLineSquare(canvas,
                visibleWidthUnit * pixelsPerSceneUnit,
                visibleHeightUnit * pixelsPerSceneUnit,
                paint);

        for (WorldObject worldObject : renderObjects) {
            canvas.save();
            canvas.translate(
                    worldObject.position.x * pixelsPerSceneUnit,
                    worldObject.position.y * pixelsPerSceneUnit
            );
            canvas.drawRect(
                    0, 0,
                    worldObject.inputSurfaceSize.x * pixelsPerSceneUnit,
                    worldObject.inputSurfaceSize.y * pixelsPerSceneUnit,
                    paint
            );
            canvas.restore();
        }
    }

    private void drawLineSquare(Canvas canvas, float width, float height, Paint paint) {
        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(0, 0, 0, height, paint);
        canvas.drawLine(width, 0, width, height, paint);
        canvas.drawLine(0, height, width, height, paint);
    }

    public interface InputCallback {
        void onClick(int xPositionUnit, int yPositionUnit);
    }
}
