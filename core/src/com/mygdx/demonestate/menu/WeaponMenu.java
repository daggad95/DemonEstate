package com.mygdx.demonestate.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
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
    private static float WIDTH = 25f;
    private static float ITEM_HEIGHT = 5f;

    private Stage stage;
    private Table table;
    private Skin skin;
    protected boolean active;
    protected ArrayList<WeaponMenuItem> items;
    protected List menuList;
    protected Label moneyLabel;
    private boolean choosingItem;
    private int slotType;
    private ArrayList<WeaponMenuItem> validItems;
    private int id;

    public WeaponMenu(Stage stage, Skin skin, int id) {
        this.stage = stage;
        this.skin = skin;
        this.id = id;

        skin.getFont("small-font").getData().setScale(FONTSCALE);

        table = new Table();
        table.setSize(Gdx.graphics.getWidth() * (WIDTH / 100),
                Gdx.graphics.getHeight() / 4);
        table.setPosition(Gdx.graphics.getWidth() * (WIDTH / 100) * id,
                Gdx.graphics.getHeight() - table.getHeight());
        table.setSkin(skin);
        table.background("window");

        menuList = new List(skin);
        table.add(menuList);
        table.row();


        VerticalGroup moneyLabelGroup = new VerticalGroup();
        table.add(moneyLabelGroup);

        moneyLabel = new Label("", skin);
        moneyLabelGroup.addActor(moneyLabel);
        moneyLabelGroup.padLeft(Gdx.graphics.getWidth() * (WIDTH / 100) / 2);

        stage.addActor(table);
        active = false;
        choosingItem = false;

        loadMenuItems();
        table.setVisible(false);
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
            table.setVisible(true);
        }
        else {
            choosingItem = false;
            table.setVisible(false);
        }
    }

    public void selectItem(Player player) {
        if (choosingItem) {
            WeaponMenuItem item = (WeaponMenuItem) menuList.getSelected();
            if (player.getMoney() >= item.price && !player.hasWeapon(item.type)) {
                player.setMoney(player.getMoney() - item.price);
                player.addWeapon(item.slotType, WeaponFactory.makeWeapon(item.type, player));
                updateMenuItems(player);
                choosingItem = false;
            }
        }
        else {
            switch ((String) menuList.getSelected()) {
                case "Melee":
                    slotType = Player.MELEE;
                    break;
                case "Sidearm":
                    slotType = Player.SIDEARM;
                    break;
                case "Main Gun":
                    slotType = Player.MAIN_GUN;
                    break;
                case "Heavy":
                    slotType = Player.HEAVY;
                    break;
                case "Equipment":
                    slotType = Player.OTHER;
                    break;
            }
            choosingItem = true;
        }
        updateMenuItems(player);
    }

    public void changeSelection(int dir) {
        int endIndex;
        if (choosingItem)
            endIndex = validItems.size() - 1;
        else
            endIndex = 4; //number of slots - 1

        if (dir < 0) {
            if (menuList.getSelectedIndex() == 0 ) {
                menuList.setSelectedIndex(endIndex);
            } else {
                menuList.setSelectedIndex(menuList.getSelectedIndex() - 1);
            }
        } else {
            if (menuList.getSelectedIndex() == endIndex ) {
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
        if (choosingItem) {
            validItems = new ArrayList<WeaponMenuItem>();
            for (WeaponMenuItem item : items) {
                if (item.slotType == slotType) {
                    if (player.hasWeapon(item.type))
                        item.owned = true;
                    else
                        item.owned = false;
                    validItems.add(item);
                }
            }

            menuList.setItems(validItems.toArray());
        }
        else {
            String categories[] = {"Melee", "Sidearm",
                    "Main Gun", "Heavy", "Equipment"};
            menuList.setItems(categories);
        }

        table.setSize(Gdx.graphics.getWidth() * (WIDTH / 100),
                Gdx.graphics.getHeight() * (ITEM_HEIGHT / 100) * menuList.getItems().size);
        table.setPosition(Gdx.graphics.getWidth() * (WIDTH / 100) * id,
                Gdx.graphics.getHeight() - table.getHeight());
        moneyLabel.setText("$" + Integer.toString(player.getMoney()));
    }

    public void goBack(Player player) {
        if (choosingItem)
            choosingItem = false;
        else
            toggleActive(player);
        updateMenuItems(player);
    }
}
