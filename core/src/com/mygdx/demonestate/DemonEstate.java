package com.mygdx.demonestate;

import com.badlogic.gdx.Game;

public class DemonEstate extends Game {
    GameScreen gameScreen;

    public void create() {
        GameScreen gameScreen = new GameScreen();

        setScreen(gameScreen);
    }

}
