package com.mygdx.demonestate.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.menu.pausemenu.PauseMenu;

import java.util.ArrayList;

/**
 * Created by david on 4/7/17.
 */
public class MenuHandler {
    private static Stage stage;
    private static Skin skin;
    private static PauseMenu pauseMenu;
    private static ArrayList<WeaponMenu> weaponMenus;

    public static void init() {
        weaponMenus = new ArrayList<>();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("assets/skins/" +
                "visui/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        for (int i = 0; i < EntityHandler.getPlayers().size(); i++) {
            weaponMenus.add(new WeaponMenu(stage, skin, i));
        }

        pauseMenu = new PauseMenu(stage, skin);
    }

    public static void renderMenus() {
        for (WeaponMenu m : weaponMenus) {
            if (m.isActive()) {
                m.render();
            }
        }
        pauseMenu.render();
    }

    public static WeaponMenu getMenu(int id) {
        return weaponMenus.get(id);
    }

    public static PauseMenu getPauseMenu() { return pauseMenu; }
}
