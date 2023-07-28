package com.pinonzhyk.coinssnake;

import android.view.Choreographer;

public class GameLoop implements Choreographer.FrameCallback {

    private Choreographer choreographer;
    private Callback callback;
    private float lastFrameTime;
    private boolean isRunning;

    private final boolean calculateFpsDebug;
    private int frameCountSinceLastReset;
    private float fpsCountingTimeMark;
    private int fpsDebug;

    public GameLoop(boolean calculateFpsDebug) {
        this.calculateFpsDebug = calculateFpsDebug;
    }

    public int getFpsDebug() {
        return fpsDebug;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Start game loop that will periodically callback via specified Callback
     * on the same thread from which this method has been called
     */
    public void start() {
        isRunning = true;
        choreographer = Choreographer.getInstance();
        choreographer.postFrameCallback(GameLoop.this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        if (isRunning) {
            final float frameTimeSec = frameTimeNanos / 1000000000f;
            final float frameTimeDelta = frameTimeSec - lastFrameTime;
            lastFrameTime = frameTimeSec;

            if (callback != null) {
                callback.onFrame(frameTimeSec, frameTimeDelta);
            }

            if (calculateFpsDebug) {
                final float fpsCountingTimeDelta = frameTimeSec - fpsCountingTimeMark;
                frameCountSinceLastReset++;
                if (frameCountSinceLastReset > 5 && frameCountSinceLastReset % 10 == 0) {
                    fpsDebug = (int) (frameCountSinceLastReset / fpsCountingTimeDelta);
                }
                if (fpsCountingTimeDelta > 1f) {
                    frameCountSinceLastReset = 0;
                    fpsCountingTimeMark = frameTimeSec;
                }
            }

            choreographer.postFrameCallback(this);
        }
    }

    public void stop() {
        isRunning = false;
    }

    public interface Callback {
        void onFrame(float timeSec, float deltaTime);
    }
}
