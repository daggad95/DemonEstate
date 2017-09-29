package com.mygdx.demonestate.damagebox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;

/**
 * Created by david on 9/20/17.
 */
public class Rocket extends DamageBox {
    public static final float EXPLOSION_DURATION = 0.25f;
    public static final float DAMAGE_SIZE_SCALE = 0.05f;
    private Vector2 explosionSize;

    public Rocket(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                  float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                  float knockback, float burnDamage, float burnChance, float shockChance, boolean ignoreWall) {

        super(damage, range, pos, size, dir, vel, spriteSheet,
                rotation, duration, multiHitChance, knockback,
                burnDamage, burnChance, shockChance, ignoreWall);
        explosionSize = new Vector2(damage * DAMAGE_SIZE_SCALE,
                damage * DAMAGE_SIZE_SCALE);
    }

    public void applyDamage(Entity entity) {
        EntityHandler.addPDamageBox(new Explosion(damage, -1,
                new Vector2(pos).sub(new Vector2(explosionSize).scl(0.5f)),
                explosionSize, new Vector2(dir), 0, TextureHandler.getTexture("explosion"),
                0, EXPLOSION_DURATION, -1, knockback, 0, 0,
                0, ignoreWall));
        die();
    }
}
