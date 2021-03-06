package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.HUD;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.weapon.WeaponFactory;
import com.mygdx.demonestate.weapon.Weapon;
import com.mygdx.demonestate.weapon.WeaponType;

import javax.xml.bind.annotation.XmlType;
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
    public static int MAX_MAIN_AMMO = 512;
    public static int MAX_SIDEARM_AMMO = 32;
    public static int MAX_HEAVY_AMMO = 128;
    
    //Default speed for player in game units/s
    public static float DEFAULT_SIZE = 1.0f;
    public static float DEFAULT_PLAYER_SPEED = 5f;
    public static int   DEFAULT_PLAYER_HEALTH = 100;
    public static float CROSSHAIR_DIST = 2f;
    public static float CROSSHAIR_RATIO = 4f;
    public static float HOP_HEIGHT = 0.1f;
    public static float HOP_RATE = 1f;

    protected static int lastId = 0;

    private Weapon[] inventory;
    private int[] ammo;
    private Weapon currentWeapon;
    private Vector2 attackDir;
    private boolean attacking;
    private Vector2 lastDir;
    private float pathGenTimer;
    private int money;
    private int score;
    private boolean flipped;
    private float hopOffset;
    private boolean hopping;
    private boolean hoppingUp;
    private boolean triggerPulled;
    private int id;
    Vector2 crosshairPos;
    Vector2 crosshairSize;
    Vector2 crosshairDir;
    private HUD hud;

    public Player(Vector2 pos, Texture spriteSheet) {
        super(pos, spriteSheet, DEFAULT_PLAYER_SPEED, DEFAULT_PLAYER_HEALTH);

        size = new Vector2(DEFAULT_SIZE, DEFAULT_SIZE);
        setHitBox(); // must be called AFTER size is set

        Weapon gun = WeaponFactory.makeWeapon(WeaponType.UZI, this);
        inventory = new Weapon[5];
        inventory [SIDEARM] = gun;
        currentWeapon= inventory[SIDEARM];

        ammo = new int[5];
        ammo [SIDEARM] = MAX_SIDEARM_AMMO;

        attackDir = new Vector2(1, 1);
        attacking = false;

        pathMap = MapHandler.genPathMap(new Vector2(pos));
        lastDir = new Vector2(0, 0);
        pathGenTimer = 0;
        crosshairSize = new Vector2(size.x / CROSSHAIR_RATIO, size.y / CROSSHAIR_RATIO);
        crosshairPos = new Vector2(pos);
        //TEMPORARY
        money = 1000;
        hopOffset = 0;
        hopping = false;
        hoppingUp = false;
        triggerPulled = false;
        id = lastId;
        lastId += 1;
        hud = new HUD(this);
        score = 42;
    }

    public boolean update() {
        if (!super.update())
            return false;

        /*if (hopping) {
            if (hoppingUp) {
                hopOffset += Gdx.graphics.getDeltaTime() * HOP_RATE;

                if (hopOffset >= HOP_HEIGHT) {
                    hoppingUp = false;
                }
            }
            else {
                hopOffset -= Gdx.graphics.getDeltaTime() * HOP_RATE;

                if (hopOffset <= 0) {
                    hopping = false;
                    hopOffset = 0;
                }
            }
        }*/

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

        currentWeapon.update(movementVector.x < 0, movementVector.x > 0);
        if (attacking) {
            attack();
        }

        if (velVector.len() > 0 & !hopping) {
            hopping = true;
            hoppingUp = true;
        }

        if (velVector.y > 0) {
            currentTexture.setRegion(SIZE_CONV, 0, SIZE_CONV, SIZE_CONV);
            flipped = true;
        }
        else if (velVector.y < 0) {
            currentTexture.setRegion(0, 0, SIZE_CONV, SIZE_CONV);
            flipped = false;
        }
        return true;
    }

    public void draw(SpriteBatch batch) {
        pos.add(0, hopOffset);

        if (flipped)
            currentWeapon.draw(batch, new Vector2(pos), new Vector2(size));

        super.draw(batch);


        if (!flipped)
            currentWeapon.draw(batch, new Vector2(pos), new Vector2(size));

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
        pos.sub(0, hopOffset);
    }

    public void setAttacking(boolean attackState) {
        if (attacking == false)
            triggerPulled = true;

        attacking = attackState;
    }

    public void setAttackDir(float x, float y) {
        attackDir.x = x;
        attackDir.y = y;
        attackDir.nor();
    }

    public void attack() {
        if (attackDir.y > 0.5) {
            flipped = true;
            currentTexture.setRegion(SIZE_CONV, 0, SIZE_CONV, SIZE_CONV);
        }
        if (attackDir.y < 0.5) {
            flipped = false;
            currentTexture.setRegion(0, 0, SIZE_CONV, SIZE_CONV);
        }

        currentWeapon.attack(new Vector2(pos.x + size.x / 2, pos.y + size.y / 2), new Vector2(attackDir), triggerPulled);
        triggerPulled = false;
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

    public int getId() {
        return id;
    }

    public HUD getHud() { return hud; }

    public float getHealth() { return health; }
    public float getMaxHealth() { return maxHealth; }
    public Weapon getCurrentWeapon() { return currentWeapon; }
    public int getAmmo(int weaponSlot) { return ammo[weaponSlot]; }
    public int getCurrentAmmo() { return ammo[currentWeapon.getSlotType()]; }
    public void setCurrentAmmo(int newAmmo) {ammo[currentWeapon.getSlotType()] = newAmmo; }
    public int getScore() { return score; }
    public Vector2 getAttackDir() {return attackDir; }
}
