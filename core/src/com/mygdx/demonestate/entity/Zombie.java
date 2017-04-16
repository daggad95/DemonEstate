package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.damagebox.DamageBox;

/**
 * Created by David on 3/11/2017.
 */
public class Zombie extends Entity {
    //Default speed for zombie in game units/s
    public static final float DEFAULT_SPEED = 1f;
    public static final int DEFAULT_HEALTH = 100;
    public static final int DEFAULT_DAMAGE = 10;
    public static final int DEFAULT_RANGE = 1;
    public static final float ATTACK_DELAY = 0.5f;
    public static final float ANIMATION_DELAY = 0.25f;
    public static final String TEXTURE_NAME = "zombie";
    private Entity target;
    private float attackTimer;
    private float animationTimer;
    private int frame;

    public Zombie(Vector2 pos, Vector2 size) {
        super(pos, size, TextureHandler.getTexture(TEXTURE_NAME), DEFAULT_SPEED + (float) Math.random() * DEFAULT_SPEED / 2, DEFAULT_HEALTH);

        target = null;
        attackTimer = ANIMATION_DELAY;
        animationTimer = 0;
        frame = 0;
    }

    public void update() {
        float dTime = Gdx.graphics.getDeltaTime();

        if (attackTimer > 0) {
            attackTimer -= dTime;
        }

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

        if (EntityHandler.intersects(hitBox, target.getHitbox())) {
            Vector2 dir = new Vector2(target.getPos()).sub(pos);
            if (attackTimer <= 0) {
                attack(dir);
                attackTimer += ATTACK_DELAY;
            }
        } else {
            chase(target, dTime);
        }
    }

    public void die() {
        EntityHandler.killMonster(this);
    }

    private void attack(Vector2 dir) {
        EntityHandler.addMDamageBox( new DamageBox(DEFAULT_DAMAGE, DEFAULT_RANGE, new Vector2(pos), new Vector2(size), dir, 1,
                null, 0, -1, -1));
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
