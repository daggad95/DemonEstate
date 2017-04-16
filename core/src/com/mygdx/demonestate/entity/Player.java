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
    
    //Default speed for player in game units/s
    public static float DEFAULT_PLAYER_SPEED = 5f;
    public static int   DEFAULT_PLAYER_HEALTH = 100;
    public static float CROSSHAIR_DIST = 3f;
    public static float CROSSHAIR_RATIO = 4f;

    private Weapon[] inventory;
    private ArrayList<Weapon> weapons;
    private Weapon currentWeapon;
    private Vector2 attackDir;
    private boolean attacking;
    private Vector2 lastDir;
    private float pathGenTimer;

    public Player(Vector2 pos, Vector2 size, Texture spriteSheet) {
        super(pos, size, spriteSheet, DEFAULT_PLAYER_SPEED, DEFAULT_PLAYER_HEALTH);

        Weapon gun = WeaponFactory.makeWeapon(WeaponType.HEAVY_PISTOL_1);

        inventory = new Weapon[5];
        inventory [SIDEARM] = gun;

        weapons = new ArrayList<Weapon>();
        weapons.add(gun);

        currentWeapon= inventory[SIDEARM];

        attackDir = new Vector2(1, 1);
        attacking = false;
        pathMap = MapHandler.genPathMap(new Vector2(pos));
        lastDir = new Vector2(0, 0);
        pathGenTimer = 0;
    }

    public void update() {
        currentWeapon.update();
        if (attacking) {
            attack();
        }

        if (!movementVector.epsilonEquals(0, 0, 0.9f)) {
            lastDir= movementVector;
        }

        Vector2 velVector = movementVector.nor().scl(speed * Gdx.graphics.getDeltaTime());
        pos.add(velVector);
        hitBox.setPosition(pos.x, pos.y);

        if (MapHandler.wallAt(new Vector2(pos), new Vector2(size))) {
            pos.sub(velVector);
        }

        pathMap = MapHandler.genPathMap(new Vector2(pos));
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
        currentWeapon.draw(batch, pos);

        //crosshairs
        batch.draw(TextureHandler.getTexture("crosshair"),
                pos.x + size.x / 2 + attackDir.x * CROSSHAIR_DIST,
                pos.y + size.y / 2 + attackDir.y * CROSSHAIR_DIST,
                size.x / CROSSHAIR_RATIO, size.y / CROSSHAIR_RATIO);

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
        inventory[slot] = weapon;
        weapons.add(weapon);
    }
}
