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
    private static ArrayList<WeaponMenu> weaponMenus;

    public static void init() {
        weaponMenus = new ArrayList<>();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("assets/skins/gdx-skins/" +
                "commodore64/skin/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        weaponMenus.add(new WeaponMenu(stage, skin));
    }

    public static void renderMenus() {
        for (WeaponMenu m : weaponMenus) {
            if (m.isActive()) {
                m.render();
            }
        }
    }

    public static WeaponMenu getMenu() {
        return weaponMenus.get(0);
    }
}
