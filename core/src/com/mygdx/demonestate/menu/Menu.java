package com.mygdx.demonestate.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.demonestate.entity.Player;

import java.util.ArrayList;

/**
 * Created by david on 4/7/17.
 */
public abstract class Menu {
    private static float FONTSCALE = 0.7f;

    private Stage stage;
    private Table table;
    private Skin skin;
    protected boolean active;
    protected ArrayList<String> items;
    protected List  menuList;

    public Menu(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        skin.getFont("commodore-64").getData().setScale(FONTSCALE);

        table = new Table();
        table.setSize(Gdx.graphics.getWidth() / 5,
                Gdx.graphics.getHeight() / 4);
        table.setPosition(0, Gdx.graphics.getHeight() - table.getHeight());
        table.setSkin(skin);
        table.background("window");

        menuList = new List(skin);
        table.add(menuList);

        stage.addActor(table);
        active = false;
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
        table.setSize(Gdx.graphics.getWidth() / 4,
                Gdx.graphics.getHeight() / 4);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

    public boolean isActive() {
        return active;
    }

    public void toggleActive() {
        active = !active;

        if (active) {
            openMenu();
        }
    }

    protected abstract void openMenu();
    public abstract void selectItem(Player player);

    public void changeSelection(int dir) {
        if (dir < 0) {
            if (menuList.getSelectedIndex() == 0 ) {
                menuList.setSelectedIndex(items.size() - 1);
            } else {
                menuList.setSelectedIndex(menuList.getSelectedIndex() - 1);
            }
        } else {
            if (menuList.getSelectedIndex() == items.size() - 1 ) {
                menuList.setSelectedIndex(0);
            } else {
                menuList.setSelectedIndex(menuList.getSelectedIndex() + 1);
            }
        }

    }

    protected void updateMenuItems() {

        menuList.setItems(items.toArray());
    }
}
