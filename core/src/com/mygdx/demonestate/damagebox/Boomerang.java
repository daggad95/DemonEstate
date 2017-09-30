package com.mygdx.demonestate.damagebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.entity.Player;

import java.util.ArrayList;

/**
 * Created by david on 9/30/17.
 */
public class Boomerang extends DamageBox {
    //rate of acceleration
    public static final float RR = 200;
    private Vector2 target;
    private boolean starting;

    public Boomerang(Entity player, int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                     float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                     float knockback, float burnDamage, float burnChance, float shockChance, boolean ignoreWall) {

        super(damage, range, pos, size, dir, vel, spriteSheet,
                rotation, duration, multiHitChance, knockback,
                burnDamage, burnChance, shockChance, ignoreWall);


        target = player.getPos();
        starting = true;
    }

    public void update() {
       if (pos.epsilonEquals(target, 0.9f) && !starting) {
            die();
            return;
        }

        if (starting && !pos.epsilonEquals(target, 0.9f))
            starting = false;

        float dTime = Gdx.graphics.getDeltaTime();

        if (target != null) {
            float angle = dir.angle(new Vector2(target).sub(pos));

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
