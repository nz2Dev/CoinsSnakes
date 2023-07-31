package com.pinonzhyk.coinssnake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinonzhyk.coinssnake.game.Movable;
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
        final WorldObject movableObj = new WorldObject(25, 25, 100, 100);
        movableObj.addLogicComponent(new Movable());
        gameWorld.instantiateWorldObject(movableObj);
        gameWorld.setClickEventsListener((receiver, x, y) -> {
            if (receiver == null) {
                gameWorld.instantiateWorldObject(new WorldObject(x, y, 50, 50));
            }
        });
        worldData.postValue(gameWorld);
    }

}
