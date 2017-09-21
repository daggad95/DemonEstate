package com.mygdx.demonestate.damagebox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.entity.Entity;

/**
 * Created by david on 9/20/17.
 */
public class Explosion extends DamageBox {
    public Explosion(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                  float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                  float knockback, float burnDamage, float burnChance) {

        super(damage, range, pos, size, dir, vel, spriteSheet,
                rotation, duration, multiHitChance, knockback, burnDamage, burnChance);
    }

    public void applyDamage(Entity entity) {
        if (!hitSet.contains(entity)) {
            entity.takeDamage(damage, knockback,
                    new Vector2(dir).rotate((float) Math.random() * 180 - 90),
                    burnDamage, burnChance);
            hitSet.add(entity);
        }
    }
}
