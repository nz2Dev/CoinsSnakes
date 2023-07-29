package com.pinonzhyk.coinssnake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pinonzhyk.coinssnake.game.Movable;
import com.pinonzhyk.coinssnake.world.Scene;
import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GameLoop gameLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RenderView renderView = findViewById(R.id.renderView);

        final WorldObject movableObj = new WorldObject(25, 25);
        movableObj.addLogicComponent(new Movable());

        final List<WorldObject> worldObjects = Arrays.asList(
                movableObj,
                new WorldObject(25, 85)
        );

        final World world = new World(1000, 1000, new Scene(worldObjects));
        renderView.setVisibleBounds(world.getBoundsWidthUnits(), world.getBoundsHeightUnits());

        gameLoop = new GameLoop(true);
        gameLoop.setCallback((timeSec, deltaTime) -> {
            world.update(timeSec, deltaTime);
            renderView.renderObjects(world.getObjects());
            renderView.setFpsDebug(gameLoop.getFpsDebug());
        });
        gameLoop.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameLoop.setCallback(null);
        gameLoop.stop();
    }
}