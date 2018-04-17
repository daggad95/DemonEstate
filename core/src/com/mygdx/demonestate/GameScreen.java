package com.mygdx.demonestate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.menu.MenuHandler;

import java.util.ArrayList;

/**
 * Created by David on 1/7/2017.
 */
public class GameScreen extends ScreenAdapter {
    //width in game units that the playerCamera can see
    public static final int VIEW_WIDTH = 32;
    
    private SpriteBatch batch;
    private OrthographicCamera playerCamera;
    private OrthographicCamera hudCamera;
    private Game game;
    FPSLogger logger;


    public GameScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        playerCamera = new OrthographicCamera(VIEW_WIDTH, VIEW_WIDTH * (h/w));
        hudCamera = new OrthographicCamera(w , h);
        hudCamera.position.x = w / 2;
        hudCamera.position.y = h / 2;

        logger = new FPSLogger();
    }

    public void render(float delta) {
        positionCamera(EntityHandler.getPlayers());
        playerCamera.update();
        hudCamera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        MapHandler.renderGroundLayer(playerCamera);

        batch.setProjectionMatrix(playerCamera.combined);
        batch.begin();
        EntityHandler.update(batch);
        batch.end();

        MapHandler.renderWallLayer(playerCamera);

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        EntityHandler.renderPlayerHUDs(batch);
        batch.end();

        MenuHandler.renderMenus();

        // logger.log();
    }

    private void positionCamera(ArrayList<Entity> players) {
        float centerX = 0;
        float centerY = 0;

        for (Entity player : players) {
            centerX += player.getPos().x;
            centerY += player.getPos().y;
        }

        centerX /= players.size();
        centerY /= players.size();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        float VIEW_HEIGHT = VIEW_WIDTH * (h/w);
        playerCamera.position.x = (float) Math.round(centerX * VIEW_WIDTH) / VIEW_WIDTH;
        playerCamera.position.y = (float) Math.round(centerY * VIEW_HEIGHT) / VIEW_HEIGHT;
    }
}
