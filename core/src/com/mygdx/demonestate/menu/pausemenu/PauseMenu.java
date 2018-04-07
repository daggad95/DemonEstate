package com.mygdx.demonestate.menu.pausemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.entity.Player;
import com.mygdx.demonestate.menu.WeaponMenuItem;
import com.mygdx.demonestate.weapon.WeaponFactory;

import java.util.ArrayList;

public class PauseMenu {
    private static float FONTSCALE = 0.7f;
    private static float WIDTH = 25f;
    private static float ITEM_HEIGHT = 5f;

    private Stage stage;
    private Table table;
    private Skin skin;
    protected boolean active;
    protected List menuList;
    protected Label moneyLabel;
    private boolean choosingItem;
    private int slotType;
    private PauseMenuItem[] menuItems;

    public PauseMenu(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;
        menuItems = new PauseMenuItem[] {new ExitItem(), new MainMenuItem() };

        skin.getFont("small-font").getData().setScale(FONTSCALE);

        table = new Table();
        table.setSize(Gdx.graphics.getWidth() * (WIDTH / 100),
                Gdx.graphics.getHeight() / 4);
        table.setPosition(Gdx.graphics.getWidth() / 2
                             - table.getWidth() / 2,
                          Gdx.graphics.getHeight() / 2 - table.getHeight() / 2);
        table.setSkin(skin);
        table.background("window");

        menuList = new List(skin);
        menuList.setItems(menuItems);
        table.add(menuList);
        table.row();

        stage.addActor(table);
        active = false;
        choosingItem = false;

        table.setVisible(false);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void changeSelection(int dir) {
        int endIndex = menuItems.length - 1;

        if (dir < 0) {
            if (menuList.getSelectedIndex() == 0) {
                menuList.setSelectedIndex(endIndex);
            } else {
                menuList.setSelectedIndex(menuList.getSelectedIndex() - 1);
            }
        } else {
            if (menuList.getSelectedIndex() == endIndex) {
                menuList.setSelectedIndex(0);
            } else {
                menuList.setSelectedIndex(menuList.getSelectedIndex() + 1);
            }
        }
    }

    public void selectItem() {
        ((PauseMenuItem) menuList.getSelected()).action();
    }

    public void toggle() {
        table.setVisible(!table.isVisible());
        EntityHandler.togglePaused();
    }

    public boolean active() {
        return table.isVisible();
    }
}
