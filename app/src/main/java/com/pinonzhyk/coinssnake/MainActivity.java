package com.pinonzhyk.coinssnake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SceneRenderer sceneRenderer = findViewById(R.id.sceneRenderer);
        final List<SceneObject> sceneObjects = Arrays.asList(
                new SceneObject(25, 25),
                new SceneObject(25, 35)
        );
        final Scene scene = new Scene(100, 100, sceneObjects);
        sceneRenderer.setScene(scene);
    }
}