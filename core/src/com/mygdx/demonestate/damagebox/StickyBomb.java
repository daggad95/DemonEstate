package com.mygdx.demonestate.damagebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;

/**
 * Created by david on 9/29/17.
 */
public class StickyBomb extends DamageBox {
    public static final float EXPLOSION_DURATION = 0.25f;
    public static final float DAMAGE_SIZE_SCALE = 0.025f;
    public static final float FUSE_TIME = 1f;
    private Vector2 explosionSize;
    private float explosionTimer;
    private Entity target;

    public StickyBomb(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                  float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                  float knockback, float burnDamage, float burnChance, float shockChance, boolean ignoreWall) {

        super(damage, range, pos, size, dir, vel, spriteSheet,
                rotation, duration, multiHitChance, knockback,
                burnDamage, burnChance, shockChance, ignoreWall);
        explosionSize = new Vector2(damage * DAMAGE_SIZE_SCALE,
                damage * DAMAGE_SIZE_SCALE);

        explosionTimer = 0;
        target = null;
    }

    public void update() {

        if (explosionTimer == 0) {
            super.update();
        }
        else {
            pos = target.getPos().add(target.getSize().scl(0.5f));
            explosionTimer -= Gdx.graphics.getDeltaTime();

            if (explosionTimer <= 0) {
                EntityHandler.addPDamageBox(new Explosion(damage, -1,
                        new Vector2(pos).sub(new Vector2(explosionSize).scl(0.5f)),
                        explosionSize, new Vector2(dir), 0, TextureHandler.getTexture("explosion"),
                        0, EXPLOSION_DURATION, -1, knockback, 0, 0,
                        0, ignoreWall));
                die();
            }
        }
    }

    public void applyDamage(Entity entity) {
        if (target == null) {
            target = entity;
            explosionTimer = FUSE_TIME;
        }

    }
}
