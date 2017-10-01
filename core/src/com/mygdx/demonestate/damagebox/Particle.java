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
public class Particle extends DamageBox {
    public static final float RR = 50;
    public static final float MAX_VEL = 100;
    public static final float ACCEL = 10;
    private Entity target;


    public Particle(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                  float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                  float knockback, float burnDamage, float burnChance, float shockChance, boolean ignoreWall) {

        super(damage, range, pos, size, dir, vel, spriteSheet,
                rotation, duration, multiHitChance, knockback,
                burnDamage, burnChance, shockChance, ignoreWall);


        target = null;
        this.vel = 0;
    }

    public void update() {
        float dTime = Gdx.graphics.getDeltaTime();

        if (vel < MAX_VEL) {
            vel += ACCEL * dTime;
        }

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
            float angle = dir.angle(target.getPos().sub(pos));

            if (angle < 0) {
                dir.rotate(-RR * Gdx.graphics.getDeltaTime());
            }
            if (angle > 0) {
                dir.rotate(RR * Gdx.graphics.getDeltaTime());
            }
        }

        Vector2 moveDist = new Vector2(dir).scl(vel * Gdx.graphics.getDeltaTime());

        if (range > 0) {
            range -= moveDist.len();
        }
        if (duration > 0) {
            duration -= dTime;
        }

        pos.add(moveDist);

        hitBox.setPosition(pos.x, pos.y);

        if (!ignoreWall && MapHandler.wallAt(new Vector2(pos), new Vector2(size))) {
            range = 0;
        }
    }
}
