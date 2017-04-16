package com.mygdx.demonestate.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.demonestate.entity.Player;
import com.mygdx.demonestate.weapon.Weapon;
import com.mygdx.demonestate.weapon.WeaponFactory;
import com.mygdx.demonestate.weapon.WeaponType;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by david on 4/7/17.
 */
public class SideArmShopMenu extends Menu {
    private static int SELECT_SIDEARM = 0;
    private static int SHURIKAN = 0;
    private static int FIRST_UPGRADE = 1;
    private static int SECOND_UPGRADE = 2;
    private static int THIRD_UPGRADE = 3;
    private static int FOURTH_UPGRADE = 4;
    private static int BOTH_UPGRADES = 5;

    private int shurikenLevel;
    private int shurikenUpgradeState;
    private int pistolLevel;
    private int pistolUpgradeState;
    private int menuState;

    public SideArmShopMenu(Stage stage, Skin skin) {
        super(stage, skin);

        shurikenLevel = 0;
        shurikenUpgradeState = 0;
        pistolLevel = 0;
        pistolUpgradeState = 0;
        menuState = SELECT_SIDEARM;

        items = new ArrayList<String>();
    }

    public void selectItem(Player player) {
        int selection = menuList.getSelectedIndex();

        if (menuState == SELECT_SIDEARM) {
            if (selection == SHURIKAN) {
                switch (shurikenLevel) {
                    case 0:
                        buyShuriken(player);
                        break;
                    case 1:
                        selectShuriken(player);
                        break;
                    case 2:
                        selectBigShuriken(player);
                        break;
                    case 3:
                        selectHeavyShuriken(player);
                        break;
                }
            } else {
                handlePistol(player);
            }
        } else {

        }

    }

    public void openMenu() {
        items.clear();

        switch (shurikenLevel) {
            case 0:
                items.add("BUY SHURIKEN");
                break;
            case 1:
                items.add("UPGRADE SHURIKEN");
                break;
            case 2:
                items.add("UPGRADE BIG SHURIKEN");
                break;
            case 3:
                items.add("UPGRADE HEAVY SHURIKEN");
        }

        switch (pistolLevel) {
            case 0:
                items.add("BUY PISTOL");
                break;
            case 1:
                items.add("UPGRADE PISTOL");
                break;
            case 2:
                items.add("UPGRADE HEAVY PISTOL");
                break;
            case 3:
                items.add("UPGRADE REVOLVER");
        }

        updateMenuItems();
    }

    /////////////////////SHURIKEN TREE/////////////////////
    private void buyShuriken(Player player) {
        player.addWeapon(Player.SIDEARM,
                WeaponFactory.makeWeapon(WeaponType.SHURIKEN));
        shurikenLevel = 1;
        openMenu();
    }

    private void selectShuriken(Player player) {
        items.clear();

        if (!(shurikenUpgradeState == FIRST_UPGRADE
                && shurikenUpgradeState == SECOND_UPGRADE)) {
            if (shurikenUpgradeState != FIRST_UPGRADE) {
                items.add("DAMAGE");
            }

            if (shurikenUpgradeState != SECOND_UPGRADE) {
                items.add("RANGE");
            }
        } else {
            items.add("BIG SHURIKEN");
        }

        if (!(shurikenUpgradeState == THIRD_UPGRADE
                && shurikenUpgradeState == FOURTH_UPGRADE)) {
            if (shurikenUpgradeState != THIRD_UPGRADE) {
                items.add("# OF PROJECTILES");
            }

            if (shurikenUpgradeState != FOURTH_UPGRADE) {
                items.add("THROWING RATE");
            }
        } else {
            items.add("STAR SHURIKEN");
        }

        updateMenuItems();
    }

    private void selectBigShuriken(Player player) {

    }

    private void selectHeavyShuriken(Player player) {

    }

    private void upgradeShuriken(Player player) {
        
    }

    private void upgradeBigShuriken(Player player) {

    }

    private void upgradeHeavyShuriken(Player player) {

    }
    /////////////////////END SHURIKEN TREE/////////////////////

    private void handlePistol(Player player) {

    }


    private void buyPistol(Player player) {

    }
}
