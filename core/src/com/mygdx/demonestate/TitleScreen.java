package com.mygdx.demonestate;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.entity.Player;
import com.mygdx.demonestate.entity.PlayerController;
import com.mygdx.demonestate.menu.MenuHandler;

import java.util.ArrayList;

public class TitleScreen extends ScreenAdapter {
    private static final String TITLE_BG_PATH = "assets/titlescreen/title_bg.png";
    public static final int INITIAL_STATE = 0;
    public static final int CHARACTER_SELECT = 1;

    private Stage stage;
    private Table table;
    private Label pressALabel;
    private Label pressStartLabel;
    private Skin skin;
    private Texture titleBG;
    private SpriteBatch batch;
    private ArrayList<TitleController> controllers;
    private ArrayList<PlayerSelectBox> pSelectBoxes;
    private String[] pImageNames;
    private int state;
    private Game game;

    public TitleScreen(Game game) {
        this.game = game;

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        pImageNames = new String[] {};

        stage = new Stage();
        skin = new Skin(Gdx.files.internal("assets/skins/" +
                "visui/uiskin.json"));

        table = new Table();
        table.setSize(width / 1.5f, height / 2);
        table.setPosition(width / 2 - table.getWidth() / 2,
                          height / 2 - table.getHeight() / 2);
        table.setSkin(skin);
        table.setBackground("window");
        table.setVisible(false);

        controllers = new ArrayList<TitleController>();
        pSelectBoxes = new ArrayList<PlayerSelectBox>();
        for (int i = 0; i < Controllers.getControllers().size; i++) {
            Controller c = Controllers.getControllers().get(i);
            controllers.add(new TitleController(this, c, i));
            pSelectBoxes.add(new PlayerSelectBox(table, i));
        }
        table.row();

        pressStartLabel = new Label("Press (Start) to begin game.", skin);
        pressStartLabel.setVisible(false);
        table.add(pressStartLabel);

        stage.addActor(table);

        pressALabel = new Label("Press (A) to continue.", skin);
        stage.addActor(pressALabel);

        titleBG = new Texture(TITLE_BG_PATH);
        batch = new SpriteBatch();

        state = INITIAL_STATE;

        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(titleBG, 0, 0, width, height);
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void toggleTable() {
        table.setVisible(!table.isVisible());
        pressALabel.setVisible(!pressALabel.isVisible());

        if (table.isVisible())
            state = CHARACTER_SELECT;
        else
            state = INITIAL_STATE;
    }

    public void changeCharSelection(int pId, int dir) {
        pSelectBoxes.get(pId).changeSelection(dir);
    }

    public void toggleCharSelect(int pId) {
        pSelectBoxes.get(pId).toggleCharSelect();

        for (PlayerSelectBox pSelectBox : pSelectBoxes) {
            if (!pSelectBox.isImageSelected()) {
                pressStartLabel.setVisible(false);
                return;
            }
        }
        pressStartLabel.setVisible(true);
        return;
    }

    public void startGame() {
        for (PlayerSelectBox pSelectBox : pSelectBoxes) {
            if (!pSelectBox.isImageSelected())
                return;
        }

        MapHandler.init();
        EntityHandler.init();
        GameScreen gs = new GameScreen(game);
        for (PlayerSelectBox pSelectBox : pSelectBoxes) {
            pSelectBox.createPlayer();
        }

        for (TitleController controller : controllers) {
            controller.getController().removeListener(controller);
        }
        MenuHandler.init(game);
        game.setScreen(gs);
    }

    public int getState() {
        return state;
    }
}
