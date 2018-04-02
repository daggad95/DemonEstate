package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.damagebox.DamageBox;

import java.util.ArrayList;

/**
 * Created by David on 3/11/2017.
 */
public class Zombie extends Entity {
    
    //Default speed for zombie in game units/s
    private static final float DEFAULT_SPEED = 0.5f;
    private static final float CHASE_SPEED = 1f;
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_DAMAGE = 10;
    private static final int DEFAULT_RANGE = 1;
    private static final float ATTACK_RANGE = 1.2f;
    private static final float CHASE_RANGE = 10f;
    private static final float ATTACK_DELAY = 0.5f;
    private static final float ANIMATION_DELAY = 0.25f;
    private static final float WANDER_TIME = 1;
    private static final float CHASE_TIME = 1;
    private static final float WANDER_ODDS = 0.7f;
    private static final String ZOMBIE_RIGHT = "Zambie_Right";
    
    private Entity target;
    private float attackTimer;
    private float animationTimer;
    private float wanderTimer;
    private float chaseTimer;
    private Vector2 wanderDir;

    public Zombie(Vector2 pos, Vector2 size) {
        super(pos, size, TextureHandler.getTexture(ZOMBIE_RIGHT), DEFAULT_SPEED + (float) Math.random() * DEFAULT_SPEED / 2, DEFAULT_HEALTH);

        target = null;
        attackTimer = ATTACK_DELAY;
        animationTimer = 0;
        wanderTimer = WANDER_TIME;
        wanderDir = new Vector2(1, 1).rotate((float)Math.random() * 360);
    }

    // return false if entity should not be updated
    public boolean update() {
        if (!super.update())
            return false;

        float dTime = Gdx.graphics.getDeltaTime();

        if (attackTimer > 0) {
            attackTimer -= dTime;
        }

        //selecting closest player as target
        for (Entity player : EntityHandler.getPlayers()) {
            if (target == null || pos.dst2(player.getPos()) < pos.dst2(target.getPos())) {
                target = player;
            }
        }

        boolean inAttackRange = Math.abs(target.getPos().add(target.getSize().scl(0.5f))
                .sub(new Vector2(pos).add(new Vector2(size).scl(0.5f))).len()) < ATTACK_RANGE;
        boolean inChaseRange = Math.abs(target.getPos().add(target.getSize().scl(0.5f))
                .sub(new Vector2(pos).add(new Vector2(size).scl(0.5f))).len()) < CHASE_RANGE;
        if (inAttackRange) {
            Vector2 dir = new Vector2(target.getPos()).sub(pos);
            if (attackTimer <= 0) {
                attack(dir);
                attackTimer += ATTACK_DELAY;
                System.out.println("attacking");
            }
        } else if (inChaseRange) {
            speed = CHASE_SPEED;
            chase(target, dTime, false);
        } else {
            speed = DEFAULT_SPEED;

            if (wanderTimer > 0) {
                while (!move(wanderDir, speed, true)) {
                    wanderDir = new Vector2(1, 1).rotate((float)Math.random() * 360);
                }
                wanderTimer -= dTime;
            } else if (chaseTimer > 0) {
                chase(target, dTime, true);
                chaseTimer -= dTime;
            } else {
                boolean wandering = Math.random() < WANDER_ODDS;

                if (wandering) {
                    wanderTimer = WANDER_TIME;
                    wanderDir = new Vector2(1, 1).rotate((float)Math.random() * 360);
                } else {
                    chaseTimer = CHASE_TIME;
                }
            }
        }
        return true;
    }

    private void attack(Vector2 dir) {
        EntityHandler.addMDamageBox( new DamageBox(DEFAULT_DAMAGE, DEFAULT_RANGE, new Vector2(pos), new Vector2(size), dir, 5,
                null, 0, -1, -1, 1, 0, 0, 0, true));
    }


}
