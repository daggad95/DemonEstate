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

    //projectile velocity variance
    public static final float PROJ_VEL_VAR = 0.1f;

    private Vector2 size;

    //draw offset from player position
    private Vector2 offset;
    private Texture spriteSheet;
    private TextureRegion currentTexture;
    private float attackDelay;
    private float attackTimer;
    private float reloadTimer;
    private float spread; //bullet spread in degrees
    private float reloadSpeed;
    private float knockback;
    private int clipSize;
    private int clip;
    private float projectileMultiHit;
    private int damage;
    private float range;
    private Vector2 projectileSize;
    private float projectileVel;
    private int projectileNum;
    private Texture projectileSpriteSheet;
    private WeaponType type;
    private boolean reloading;
    private boolean explosive;
    private float burn_damage;
    private float burnChance;
    private float duration;

    public Weapon(Texture spriteSheet, Vector2 size, float attackDelay, WeaponType type, float spread, int clipSize, float reloadSpeed,
                  int damage, float range, Vector2 projectileSize, float projectileVel,
                  Texture projectileSpriteSheet, float projectileMultiHit, int projectileNum,
                  Vector2 offset, float knockback, boolean explosive, float burn_damage,
                  float burnChance, float duration) {
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
        this.projectileNum = projectileNum;
        this.offset = offset;
        this.knockback = knockback;
        this.explosive = explosive;
        this.burn_damage = burn_damage;
        this.burnChance = burnChance;
        this.duration = duration;

        clip = clipSize;
        attackTimer = 0;
        reloadTimer = 0;
        reloading = false;

        float whratio = size.x / size.y;
        currentTexture = new TextureRegion(spriteSheet,
                (int) (SIZE_CONV * whratio), SIZE_CONV);
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
        position.add(offset);
        batch.draw(currentTexture, position.x + size.x / 2, position.y + size.y / 4, size.x, size.y);
    }

    public void attack(Vector2 pos, Vector2 dir) {
        if (attackTimer <= 0 && reloadTimer <= 0) {
            if (!reloading) {
                for (int i = 0; i < projectileNum; i++) {

                    //rotation of direction vector for projectile
                    Vector2 rotate = new Vector2(dir).rotate((float) Math.random() * 2 * spread - spread);

                    //projectile texture rotation
                    float rotation = (float) Math.toDegrees(Math.atan(rotate.y / rotate.x));

                    float velVariance = ((float) Math.random() * 2 * PROJ_VEL_VAR - PROJ_VEL_VAR) * projectileVel;

                    EntityHandler.addPDamageBox(new DamageBox(damage, range, new Vector2(pos), new Vector2(projectileSize),
                            rotate, projectileVel + velVariance, projectileSpriteSheet,
                            rotation, duration, projectileMultiHit, knockback, explosive,
                            burn_damage, burnChance));

                    clip--;
                }
                attackTimer += attackDelay;

                if (clip < 1) {
                    reloadTimer = reloadSpeed;
                    reloading = true;
                }
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

    public WeaponType getType() { return type; }
}
