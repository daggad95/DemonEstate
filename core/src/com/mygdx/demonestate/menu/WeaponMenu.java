package com.mygdx.demonestate.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.mygdx.demonestate.entity.Player;
import com.mygdx.demonestate.weapon.Weapon;
import com.mygdx.demonestate.weapon.WeaponFactory;
import com.mygdx.demonestate.weapon.WeaponType;

import java.util.ArrayList;

/**
 * Created by david on 4/7/17.
 */
public class WeaponMenu {
    private static float FONTSCALE = 0.7f;

    private Stage stage;
    private Table table;
    private Skin skin;
    protected boolean active;
    protected ArrayList<WeaponMenuItem> items;
    protected List menuList;

    public WeaponMenu(Stage stage, Skin skin) {
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

        loadMenuItems();
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

    public void toggleActive(Player player) {
        active = !active;

        if (active) {
            updateMenuItems(player);
            openMenu();
        }
    }

    protected void openMenu() {

    }
    public void selectItem(Player player) {
        WeaponMenuItem item = (WeaponMenuItem) menuList.getSelected();
        if (player.getMoney() >= item.price && !player.hasWeapon(item.type)) {
            player.setMoney(player.getMoney() - item.price);
            player.addWeapon(item.slotType, WeaponFactory.makeWeapon(item.type));
            updateMenuItems(player);
        }
    }

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

    private void loadMenuItems() {
        items = new ArrayList<WeaponMenuItem>();
        XmlReader reader = new XmlReader();

        try {
            XmlReader.Element weaponStats = reader.parse(new FileHandle("assets/data/weapon_stats.xml"));

            int count = weaponStats.getChildCount();
            for (int i = 0; i < count; i++) {
                int value = Integer.parseInt(weaponStats.getChild(i).get("value"));
                String name = weaponStats.getChild(i).get("name");
                WeaponType type = WeaponType.valueOf(weaponStats.getChild(i).getName());
                int slot = Integer.parseInt(weaponStats.getChild(i).get("slotType"));

                items.add(new WeaponMenuItem(value, name, type, slot));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("weapon stats file not found");
            System.exit(0);
        }
    }

    protected void updateMenuItems(Player player) {
        for (WeaponMenuItem item : items) {
            if (player.hasWeapon(item.type))
                item.owned = true;
            else
                item.owned = false;
        }

        menuList.setItems(items.toArray());
    }
}
