package com.mygdx.demonestate.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.damagebox.DamageBox;
import com.mygdx.demonestate.entity.EntityHandler;

/**
 * Created by David on 3/11/2017.
 */
public class Weapon {
    //Conversion between game units and gun size
    public static final int SIZE_CONV = 32;

    private Vector2 size;
    private Texture spriteSheet;
    private TextureRegion currentTexture;
    private float attackDelay;
    private float attackTimer;
    private float reloadTimer;
    private float spread; //bullet spread in degrees
    private float reloadSpeed;
    private int clipSize;
    private int clip;
    private float projectileMultiHit;
    private int damage;
    private float range;
    private Vector2 projectileSize;
    private float projectileVel;
    private Texture projectileSpriteSheet;
    private WeaponType type;
    private boolean reloading;

    public Weapon(Texture spriteSheet, Vector2 size, float attackDelay, WeaponType type, float spread, int clipSize, float reloadSpeed,
                  int damage, float range, Vector2 projectileSize, float projectileVel, Texture projectileSpriteSheet, float projectileMultiHit) {
        this.spriteSheet = spriteSheet;
        this.size = size;
        this.attackDelay = attackDelay;
        this.type = type;
        this.spread = spread;
        this.clipSize = clipSize;
        this.reloadSpeed = reloadSpeed;
        this.damage = damage;
        this.range = range;
        this.projectileSize = projectileSize;
        this.projectileVel = projectileVel;
        this.projectileSpriteSheet = projectileSpriteSheet;
        this.projectileMultiHit = projectileMultiHit;

        clip = clipSize;
        currentTexture = new TextureRegion(spriteSheet, SIZE_CONV, SIZE_CONV);
        attackTimer = 0;
        reloadTimer = 0;
        reloading = false;
    }

    public void update() {
        float dTime = Gdx.graphics.getDeltaTime();

        if (attackTimer > 0) {
            attackTimer -= dTime;
        }

        if (reloadTimer > 0) {
            reloadTimer -= dTime;

            if (reloadTimer <= 0) {
                clip = clipSize;
                reloading = false;
            }
        }
    }

    public void draw(SpriteBatch batch, Vector2 position) {
        batch.draw(currentTexture, position.x + size.x / 2, position.y + size.y / 4, size.x, size.y);
    }

    public void attack(Vector2 pos, Vector2 dir) {
        if (attackTimer <= 0 && reloadTimer <= 0) {
            if (clip > 0) {
                dir.rotate((float)Math.random() * 2 * spread - spread);

                EntityHandler.addPDamageBox(new DamageBox(damage, range, new Vector2(pos), new Vector2(projectileSize),
                        new Vector2(dir), projectileVel, projectileSpriteSheet, 0, -1, projectileMultiHit));

                attackTimer += attackDelay;
                clip--;
            } else {
                reloadTimer = reloadSpeed;
                reloading = true;
            }
        }
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getReloadTimer() {
        return reloadTimer;
    }

    public float getReloadSpeed() {
        return reloadSpeed;
    }
}
