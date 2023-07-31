package com.pinonzhyk.coinssnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.Collection;
import java.util.Collections;

public class GameView extends View implements GameLoop.Callback {

    private World world;
    private float pixelsPerSceneUnit;

    private final GameLoop gameLoop;
    private final Paint fpsPaint;
    private final Paint paint;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        fpsPaint = new Paint();
        fpsPaint.setTextSize(32);
        gameLoop = new GameLoop(true);
        gameLoop.setCallback(this);
    }

    public void setWorld(World world) {
        this.world = world;
        calculateUnitScale();
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        gameLoop.start();
    }

    @Override
    public void onFrame(float timeSec, float deltaTime) {
        if (world != null) {
            world.update(timeSec, deltaTime);
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        gameLoop.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (world == null) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = pixelPositionToUnit(event.getX());
            int y = pixelPositionToUnit(event.getY());
            world.handleClickInput(x, y);
        }

        return true;
    }

    private int pixelPositionToUnit(float pixelCoord) {
        return (int) (pixelCoord / pixelsPerSceneUnit);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (world != null) {
            calculateUnitScale();
        }
    }

    private void calculateUnitScale() {
        float widthScale = ((float) getWidth() / world.getBoundsWidthUnits());
        float heightScale = ((float) getHeight() / world.getBoundsHeightUnits());
        pixelsPerSceneUnit = Math.min(widthScale, heightScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("FPS: " + gameLoop.getFpsDebug(), 50, 74, fpsPaint);
        // view borders
        drawLineSquare(canvas, getWidth(), getHeight(), paint);

        if (world == null) {
            return;
        }
        // world borders
        drawLineSquare(canvas,
                world.getBoundsWidthUnits() * pixelsPerSceneUnit,
                world.getBoundsHeightUnits() * pixelsPerSceneUnit,
                paint);

        for (WorldObject worldObject : world.getObjects()) {
            canvas.save();
            canvas.translate(
                    worldObject.position.x * pixelsPerSceneUnit,
                    worldObject.position.y * pixelsPerSceneUnit
            );

            int halfSurfaceX = worldObject.inputSurfaceSize.x / 2;
            int halfSurfaceY = worldObject.inputSurfaceSize.y / 2;
            canvas.drawRect(
                    -halfSurfaceX * pixelsPerSceneUnit,
                    -halfSurfaceY * pixelsPerSceneUnit,
                    halfSurfaceX * pixelsPerSceneUnit,
                    halfSurfaceY * pixelsPerSceneUnit,
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
}
