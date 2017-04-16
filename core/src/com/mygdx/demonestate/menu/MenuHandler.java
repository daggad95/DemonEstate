package com.mygdx.demonestate.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

/**
 * Created by david on 4/7/17.
 */
public class MenuHandler {
    private static Stage stage;
    private static Skin skin;
    private static ArrayList<Menu> menus;

    public static void init() {
        menus = new ArrayList<>();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("assets/skins/gdx-skins/" +
                "commodore64/skin/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        menus.add(new SideArmShopMenu(stage, skin));
    }

    public static void renderMenus() {
        for (Menu m : menus) {
            if (m.isActive()) {
                m.render();
            }
        }
    }

    public static Menu getMenu() {
        return menus.get(0);
    }
}
