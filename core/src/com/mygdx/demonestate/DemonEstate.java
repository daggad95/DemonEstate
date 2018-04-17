package com.mygdx.demonestate;

import com.badlogic.gdx.Game;
import com.mygdx.demonestate.entity.EntityHandler;

public class DemonEstate extends Game {
    GameScreen gameScreen;

    public void create() {
        TextureHandler.loadTextures();
        setScreen(new TitleScreen(this));
    }

}
