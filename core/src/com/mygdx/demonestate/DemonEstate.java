package com.mygdx.demonestate;

import com.badlogic.gdx.Game;

public class DemonEstate extends Game {
    GameScreen gameScreen;

    public void create() {
		// this is a test
        GameScreen gameScreen = new GameScreen();

        setScreen(gameScreen);
    }

}
