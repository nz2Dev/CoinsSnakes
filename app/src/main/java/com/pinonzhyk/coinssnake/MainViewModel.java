package com.pinonzhyk.coinssnake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinonzhyk.coinssnake.game.EconomySystem;
import com.pinonzhyk.coinssnake.game.FlowerPlantSystem;
import com.pinonzhyk.coinssnake.game.Movable;
import com.pinonzhyk.coinssnake.game.SnakeSpawnSystem;
import com.pinonzhyk.coinssnake.world.World;
import com.pinonzhyk.coinssnake.world.WorldObject;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Integer> dialogData = new MutableLiveData<>();
    private MutableLiveData<World> worldData = new MutableLiveData<>();
    private String playerName;

    public MainViewModel() {
        dialogData.postValue(0);
    }

    public LiveData<World> getWorldData() {
        return worldData;
    }

    public LiveData<Integer> getDialogData() {
        return dialogData;
    }

    public void playerNameEntered(String playerNameInput) {
        this.playerName = playerNameInput.isEmpty() ? "Player1" : playerNameInput;
        createGame();
    }

    private void createGame() {
        final World gameWorld = new World(1000, 1000);
        gameWorld.addSystem(new EconomySystem(150));
        gameWorld.addSystem(new FlowerPlantSystem(50));
        gameWorld.addSystem(new SnakeSpawnSystem());
        worldData.postValue(gameWorld);
    }

}
