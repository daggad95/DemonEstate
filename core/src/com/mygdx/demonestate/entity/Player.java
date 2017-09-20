package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.weapon.WeaponFactory;
import com.mygdx.demonestate.weapon.Weapon;
import com.mygdx.demonestate.weapon.WeaponType;

import java.util.ArrayList;


/**
 * Created by David on 1/7/2017.
 */
public class Player extends Entity {
    //Weapon inventory locations
    public static int MAIN_GUN = 0;
    public static int SIDEARM = 1;
    public static int MELEE = 2;
    public static int HEAVY = 3;
    public static int OTHER = 4;
    
    //Default speed for player in game units/s
    public static float DEFAULT_PLAYER_SPEED = 5f;
    public static int   DEFAULT_PLAYER_HEALTH = 100;
    public static float CROSSHAIR_DIST = 2f;
    public static float CROSSHAIR_RATIO = 4f;

    private Weapon[] inventory;
    private Weapon currentWeapon;
    private Vector2 attackDir;
    private boolean attacking;
    private Vector2 lastDir;
    private float pathGenTimer;
    private int money;
    Vector2 crosshairPos;
    Vector2 crosshairSize;
    Vector2 crosshairDir;

    public Player(Vector2 pos, Vector2 size, Texture spriteSheet) {
        super(pos, size, spriteSheet, DEFAULT_PLAYER_SPEED, DEFAULT_PLAYER_HEALTH);

        Weapon gun = WeaponFactory.makeWeapon(WeaponType.SHOTGUN);
        inventory = new Weapon[5];
        inventory [MAIN_GUN] = gun;
        currentWeapon= inventory[MAIN_GUN];

        attackDir = new Vector2(1, 1);
        attacking = false;

        pathMap = MapHandler.genPathMap(new Vector2(pos));
        lastDir = new Vector2(0, 0);
        pathGenTimer = 0;
        crosshairSize = new Vector2(size.x / CROSSHAIR_RATIO, size.y / CROSSHAIR_RATIO);
        crosshairPos = new Vector2(pos);
        //TEMPORARY
        money = 1000;
    }

    public void update() {
        super.update();

        //cut update short if knockback
        if (knockbackDistance > 0)
            return;

        currentWeapon.update();
        if (attacking) {
            attack();
        }

        if (!movementVector.epsilonEquals(0, 0, 0.9f)) {
            lastDir= movementVector;
        }

        Vector2 velVector = movementVector.nor().scl(speed * Gdx.graphics.getDeltaTime());

        if (!MapHandler.wallAt(new Vector2(pos).add(velVector.x, 0), new Vector2(size))) {
            pos.x += movementVector.x;
        }
        if (!MapHandler.wallAt(new Vector2(pos).add(0, movementVector.y), new Vector2(size))) {
            pos.y += movementVector.y;
        }
        hitBox.setPosition(pos.x, pos.y);

        pathMap = MapHandler.genPathMap(new Vector2(pos));
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
        currentWeapon.draw(batch, new Vector2(pos));

        //crosshairs
        crosshairDir = new Vector2(attackDir).scl(0.01f);
        crosshairPos.set(pos.x + size.x / 2 + crosshairDir.x,
                pos.y + size.y / 2 + crosshairDir.y);
        while (!MapHandler.wallAt(new Vector2(crosshairPos), new Vector2(crosshairSize))
                &&  crosshairDir.len() < CROSSHAIR_DIST) {
            crosshairDir.scl(1.01f);
            crosshairPos.set(pos.x + size.x / 2 + crosshairDir.x,
                    pos.y + size.y / 2 + crosshairDir.y);
        }
        attackDir.nor();


        batch.draw(TextureHandler.getTexture("crosshair"),
                crosshairPos.x, crosshairPos.y, crosshairSize.x, crosshairSize.y);

        //reload bar;
        if (currentWeapon.isReloading()) {
            batch.draw(TextureHandler.getTexture("reloadbar"), pos.x, pos.y + size.y,
                    size.x * (currentWeapon.getReloadSpeed() - currentWeapon.getReloadTimer()) / currentWeapon.getReloadSpeed(), size.y / 10);
        }
    }

    public void setAttacking(boolean attackState) {
        attacking = attackState;
    }

    public void setAttackDir(float x, float y) {
        attackDir.x = x;
        attackDir.y = y;
        attackDir.nor();
    }

    public void attack() {
        currentWeapon.attack(new Vector2(pos.x + size.x / 2, pos.y + size.y / 2), new Vector2(attackDir));
    }
    
    public void addWeapon(int slot, Weapon weapon) {
        currentWeapon = weapon;
        inventory[slot] = weapon;
    }

    public void switchWeapon(int weaponSlot) {
        if (inventory[weaponSlot] != null)
            currentWeapon = inventory[weaponSlot];
    }

    public boolean hasWeapon(WeaponType weaponType) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                if (inventory[i].getType() == weaponType) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int newMoney) {
        money = newMoney;
    }
}
