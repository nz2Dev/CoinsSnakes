package com.pinonzhyk.coinssnake.game;

import com.pinonzhyk.coinssnake.world.Vector2;
import com.pinonzhyk.coinssnake.world.VectorMath;

import java.util.ArrayDeque;
import java.util.Deque;

public class SegmentedPath {

    private Deque<Vector2> points;
    private Vector2[] segments;
    private float segmentDistance;
    private int segmentFractionsCount;

    public SegmentedPath(Vector2 origin) {
        points = new ArrayDeque<>();
        segments = new Vector2[0];
        points.addFirst(origin);
        segmentFractionsCount = 1;
    }
    public void setSegmentDistance(float distance) {
        this.segmentDistance = distance;
    }

    public void setSegmentsCount(int segmentsCount) {
        segments = new Vector2[segmentsCount];
    }

    public Vector2[] segmented() {
        updateSegments();
        return segments;
    }

    public Vector2 tip() {
        return points.getFirst();
    }

    private void setSegmentFraction(float segmentFraction) {
        // if the segmentFraction value is closer to the segmentDistance or greater
        // then fractions count is closer to one, or becomes sequential
        // to be so, the moveDelta must be more or around the world.bounds * fps
        // which is not the case for the most part, and so this case should not be an issue
        final int fractionsPerSegment = Math.round(segmentDistance / segmentFraction);
        segmentFractionsCount = Math.max(fractionsPerSegment, 1);
    }

    public void append(float dx, float dy) {
        // because our direction is either vertical or horizontal, this is a cheap way to get vector length
        final float stepLength = Math.max(Math.abs(dx), Math.abs(dy));
        setSegmentFraction(stepLength);

        if (points.size() > segments.length * segmentFractionsCount) {
            points.removeLast();
        }

        final Vector2 newPoint = VectorMath.add(points.getFirst(), dx, dy, new Vector2());
        points.addFirst(newPoint);
    }

    private void updateSegments() {
        int pointIndex = 0;
        int segmentIndex = 0;
        for (Vector2 point : points) {
            if (pointIndex % segmentFractionsCount == 0 && segmentIndex < segments.length) {
                segments[segmentIndex] = point;
                segmentIndex++;
            }
            pointIndex++;
        }

        // if tails is not counted to the end because there is less path points than tails
        // we use last known path points which should point at the end of the path
        // e.s should be the oldest. This is a workaround before or if we will implement
        // manual path generation to adjust the path to the amount of tails
        if (segmentIndex < segments.length - 1 && !points.isEmpty()) {
            final Vector2 lastPoint = points.getLast();
            for (; segmentIndex < segments.length; segmentIndex++) {
                segments[segmentIndex] = lastPoint;
            }
        }
    }
}
