package com.pinonzhyk.coinssnake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.pinonzhyk.coinssnake.world.GraphicComponent;
import com.pinonzhyk.coinssnake.world.TextComponent;
import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.Collection;
import java.util.Collections;

public class GameView extends View implements GameLoop.Callback {

    private static final boolean DEBUG_DRAW_INPUT_SURFACE = true;
    private static final boolean DEBUG_DRAW_COLLIDER_SURFACE = true;

    private World world;
    private float pixelsPerSceneUnit;

    private final GameLoop gameLoop;
    private final Paint fpsPaint;
    private final Paint paint;
    private final Paint inputSurfacePaint;
    private final Paint textComponentPaint;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        fpsPaint = new Paint();
        fpsPaint.setTextSize(32);
        textComponentPaint = new Paint();
        inputSurfacePaint = new Paint();
        inputSurfacePaint.setAlpha((int) (256 * 0.1f));
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
    public void onFrame(float timeSec) {
        if (world != null) {
            world.update(timeSec);
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
            float x = pixelPositionToUnit(event.getX());
            float y = pixelPositionToUnit(event.getY());
            world.handleClickInput(x, y);
        }

        return true;
    }

    private float pixelPositionToUnit(float pixelCoord) {
        return pixelCoord / pixelsPerSceneUnit;
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

            if (DEBUG_DRAW_INPUT_SURFACE && !worldObject.inputSurfaceSize.isZero()) {
                float halfSurfaceX = worldObject.inputSurfaceSize.x / 2f;
                float halfSurfaceY = worldObject.inputSurfaceSize.y / 2f;
                canvas.drawRect(
                        -halfSurfaceX * pixelsPerSceneUnit,
                        -halfSurfaceY * pixelsPerSceneUnit,
                        halfSurfaceX * pixelsPerSceneUnit,
                        halfSurfaceY * pixelsPerSceneUnit,
                        inputSurfacePaint
                );
            }

            if (DEBUG_DRAW_COLLIDER_SURFACE && !worldObject.colliderSurfaceSize.isZero()) {
                float halfSurfaceX = worldObject.colliderSurfaceSize.x / 2f;
                float halfSurfaceY = worldObject.colliderSurfaceSize.y / 2f;
                canvas.drawRect(
                        -halfSurfaceX * pixelsPerSceneUnit,
                        -halfSurfaceY * pixelsPerSceneUnit,
                        halfSurfaceX * pixelsPerSceneUnit,
                        halfSurfaceY * pixelsPerSceneUnit,
                        inputSurfacePaint
                );
            }

            for (WorldObject.Component component : worldObject.getComponents()) {
                if (component instanceof TextComponent) {
                    final TextComponent textComponent = ((TextComponent) component);
                    textComponentPaint.setTextSize(textComponent.textSize * pixelsPerSceneUnit);
                    canvas.drawText(textComponent.text, 0, 0, textComponentPaint);
                }
                if (component instanceof GraphicComponent) {
                    final GraphicComponent graphicComponent = ((GraphicComponent) component);
                    final float halfSize = (graphicComponent.size * pixelsPerSceneUnit) / 2f;
                    canvas.drawRect(-halfSize, -halfSize, halfSize, halfSize, paint);
                }
            }

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
