package com.pinonzhyk.coinssnake;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.Nullable;

public class SceneRenderer extends View  {

    private Scene scene;
    private float pixelsPerSceneUnit;
    private Paint paint = new Paint();


    public SceneRenderer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        calculateUnitScale();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (scene != null) {
            calculateUnitScale();
        }
    }

    private void calculateUnitScale() {
        float widthScale = ((float) getWidth() / scene.getWidthUnits());
        float heightScale = ((float) getHeight() / scene.getHeightUnits());
        pixelsPerSceneUnit = Math.min(widthScale, heightScale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // view borders
        drawLineSquare(canvas, getWidth(), getHeight(), paint);
        // scene borders
        drawLineSquare(canvas,
                scene.getWidthUnits() * pixelsPerSceneUnit,
                scene.getHeightUnits() * pixelsPerSceneUnit,
                paint
        );

        for (SceneObject sceneObject : scene.getSceneObjects()) {
            canvas.save();
            canvas.translate(
                    sceneObject.getX() * pixelsPerSceneUnit,
                    sceneObject.getY() * pixelsPerSceneUnit
            );
            canvas.drawRect(
                    0,
                    0,
                    1 * pixelsPerSceneUnit,
                    1 * pixelsPerSceneUnit,
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
