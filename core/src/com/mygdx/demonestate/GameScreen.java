package com.mygdx.demonestate;

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
	// this is a test hello
    //width in game units that the camera can see
    public static final int VIEW_WIDTH = 32;
    
    private SpriteBatch batch;
    private OrthographicCamera camera;
    FPSLogger logger;


    public GameScreen() {
        // test
        batch = new SpriteBatch();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(VIEW_WIDTH, VIEW_WIDTH * (h/w));

        MapHandler.init();
        EntityHandler.init();
        MenuHandler.init();
        logger = new FPSLogger();
    }

    public void render(float delta) {
        positionCamera(EntityHandler.getPlayers());
        camera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        MapHandler.renderGroundLayer(camera);
        batch.begin();
        EntityHandler.update(batch);
        batch.end();
        MapHandler.renderWallLayer(camera);
        MenuHandler.renderMenus();
        logger.log();
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

        camera.position.x = centerX;
        camera.position.y = centerY;
    }
}
