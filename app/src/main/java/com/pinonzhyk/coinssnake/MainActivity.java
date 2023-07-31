package com.pinonzhyk.coinssnake;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.renderView);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getWorldData().observe(this, world -> {
            gameView.setWorld(world);
        });
        viewModel.getDialogData().observe(this, integer -> {
            showPlayerNameDialog();
        });
    }

    private void showPlayerNameDialog() {
        final EditText inputText = new EditText(this);
        new AlertDialog.Builder(this)
                .setView(inputText)
                .setTitle("Enter Player Name")
                .setPositiveButton("Create", (dialog, which) -> {
                    viewModel.playerNameEntered(inputText.getText().toString());
                })
                .create()
                .show();
    }

}