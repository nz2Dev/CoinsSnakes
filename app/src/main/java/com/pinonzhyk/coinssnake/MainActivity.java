package com.pinonzhyk.coinssnake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Choreographer;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GameLoop gameLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SceneRenderer sceneRenderer = findViewById(R.id.sceneRenderer);
        final List<SceneObject> sceneObjects = Arrays.asList(
                new SceneObject(25, 25),
                new SceneObject(25, 85)
        );

        final Scene scene = new Scene(1000, 1000, sceneObjects);
        sceneRenderer.setScene(scene);

        gameLoop = new GameLoop(true);
        gameLoop.setCallback((timeSec, deltaTime) -> {
            for (SceneObject sceneObject : scene.getSceneObjects()) {
                sceneObject.update(timeSec);
            }
            sceneRenderer.setScene(scene);
            sceneRenderer.setFpsDebug(gameLoop.getFpsDebug());
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