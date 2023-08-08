package com.pinonzhyk.coinssnake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.pinonzhyk.coinssnake.game.EconomySystem;
import com.pinonzhyk.coinssnake.game.FlowerPlantSystem;
import com.pinonzhyk.coinssnake.game.GameManager;
import com.pinonzhyk.coinssnake.game.SnakeSpawnSystem;
import com.pinonzhyk.coinssnake.world.Vector2;
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
        final World gameWorld = new World(10, 10);
        gameWorld.addSystem(new GameManager());
        gameWorld.addSystem(new EconomySystem(150));
        gameWorld.addSystem(new FlowerPlantSystem(50));
        gameWorld.addSystem(new SnakeSpawnSystem());
        instantiateWalls(gameWorld);
        worldData.postValue(gameWorld);
    }

    private void instantiateWalls(final World gameWorld) {
        gameWorld.instantiateWorldObject(createHorizontalWallInBounds(-0.5f, 1, gameWorld));
        gameWorld.instantiateWorldObject(createHorizontalWallInBounds(gameWorld.getBoundsWidthUnits() + 0.5f, 1, gameWorld));
        gameWorld.instantiateWorldObject(createVerticalWallInBounds(-0.5f, 1, gameWorld));
        gameWorld.instantiateWorldObject(createVerticalWallInBounds(gameWorld.getBoundsHeightUnits() + 0.5f, 1, gameWorld));
    }

    private WorldObject createVerticalWallInBounds(float centerY, float size, World world) {
        final float x = world.getBoundsWidthUnits() / 2f;
        final float y = centerY;
        final float colliderWidth = world.getBoundsWidthUnits() + size;
        final float colliderHeight = size;
        return new WorldObject(x, y, null, new Vector2(colliderWidth, colliderHeight));
    }

    private WorldObject createHorizontalWallInBounds(float centerX, float size, World world) {
        final float x = centerX;
        final float y = world.getBoundsHeightUnits() / 2f;
        final float colliderWidth = size;
        final float colliderHeight = world.getBoundsHeightUnits() + size; // + width to cover corners
        return new WorldObject(x, y, null, new Vector2(colliderWidth, colliderHeight));
    }

}
