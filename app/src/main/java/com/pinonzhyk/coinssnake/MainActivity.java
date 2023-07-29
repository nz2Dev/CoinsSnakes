package com.pinonzhyk.coinssnake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pinonzhyk.coinssnake.game.GameManager;
import com.pinonzhyk.coinssnake.world.World;

public class MainActivity extends AppCompatActivity {

    private GameLoop gameLoop;
    private GameManager gameManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameManager = new GameManager();
        gameManager.createGame();

        final World world = gameManager.getGameWorld();
        final RenderView renderView = findViewById(R.id.renderView);
        renderView.setVisibleBounds(world.getBoundsWidthUnits(), world.getBoundsHeightUnits());
        renderView.setInputCallback((xPositionUnit, yPositionUnit) -> {
            world.handleClickInput(xPositionUnit, yPositionUnit);
        });

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