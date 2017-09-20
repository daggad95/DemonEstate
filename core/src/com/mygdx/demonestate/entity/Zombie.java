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
    public static final float DEFAULT_SPEED = 0.5f;
    public static final float CHASE_SPEED = 1f;
    public static final int DEFAULT_HEALTH = 100;
    public static final int DEFAULT_DAMAGE = 10;
    public static final int DEFAULT_RANGE = 1;
    public static final float ATTACK_RANGE = 1.2f;
    public static final float CHASE_RANGE = 10f;
    public static final float ATTACK_DELAY = 0.5f;
    public static final float ANIMATION_DELAY = 0.25f;
    public static final float WANDER_TIME = 1;
    public static final float CHASE_TIME = 1;
    public static final float WANDER_ODDS = 0.7f;
    public static final String TEXTURE_NAME = "zombie";
    private Entity target;
    private float attackTimer;
    private float animationTimer;
    private float wanderTimer;
    private float chaseTimer;
    private Vector2 wanderDir;
    private int frame;

    public Zombie(Vector2 pos, Vector2 size) {
        super(pos, size, TextureHandler.getTexture(TEXTURE_NAME), DEFAULT_SPEED + (float) Math.random() * DEFAULT_SPEED / 2, DEFAULT_HEALTH);

        target = null;
        attackTimer = ANIMATION_DELAY;
        animationTimer = 0;
        frame = 0;
        wanderTimer = WANDER_TIME;
        wanderDir = new Vector2(1, 1).rotate((float)Math.random() * 360);
    }

    public void update() {
        super.update();

        float dTime = Gdx.graphics.getDeltaTime();

        if (attackTimer > 0) {
            attackTimer -= dTime;
        }

        //cut update short if knockback
        if (knockbackDistance > 0)
            return;

        if (animationTimer > 0) {
            animationTimer -= dTime;
        } else {
            animate();
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
    }

    public void die() {
        EntityHandler.killMonster(this);
    }

    private void attack(Vector2 dir) {
        EntityHandler.addMDamageBox( new DamageBox(DEFAULT_DAMAGE, DEFAULT_RANGE, new Vector2(pos), new Vector2(size), dir, 5,
                null, 0, -1, -1, 1, false));
    }

    private void animate() {
        if (frame == 0) {
            currentTexture = new TextureRegion(spriteSheet, 0, SIZE_CONV, SIZE_CONV, SIZE_CONV);
            frame = 1;
        } else {
            currentTexture = new TextureRegion(spriteSheet, SIZE_CONV, SIZE_CONV);
            frame = 0;
        }

        if (flipped) {
            currentTexture.flip(true, false);
        }
        animationTimer = ANIMATION_DELAY;
    }

}
