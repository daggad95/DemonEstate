package com.mygdx.demonestate.damagebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;

import java.util.ArrayList;

/**
 * Created by david on 9/30/17.
 */
public class Aimbot extends DamageBox {
    Entity target;

    public Aimbot(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                  float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                  float knockback, float burnDamage, float burnChance, float shockChance, boolean ignoreWall) {

        super(damage, range, pos, size, dir, vel, spriteSheet,
                rotation, duration, multiHitChance, knockback,
                burnDamage, burnChance, shockChance, ignoreWall);

        ArrayList<Entity> monsters = EntityHandler.getMonsters();
        float minDistance = Integer.MAX_VALUE;
        for (Entity monster : monsters) {
            float distance = pos.dst2(monster.getPos());

            if (distance < minDistance) {
                minDistance = distance;
                target = monster;
            }
        }

        if (target != null) {
            this.dir = target.getPos().sub(pos).nor();

            Vector2 rotate = new Vector2(this.dir);
            //projectile texture rotation
            this.rotation = (float) Math.toDegrees(Math.atan(rotate.y / rotate.x));
        }
    }
}
