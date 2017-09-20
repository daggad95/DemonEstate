package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.damagebox.DamageBox;

/**
 * Created by david on 3/25/17.
 */
public class Eyebat extends Entity {
    //Default speed for zombie in game units/s
    public static final float DEFAULT_SPEED = 1f;
    public static final int DEFAULT_HEALTH = 100;
    public static final int DEFAULT_DAMAGE = 10;
    public static final float ATTACK_RANGE = 5f;

    public static final float ATTACK_DELAY = 2f;
    public static final float ANIMATION_DELAY = 0.1f;
    public static final float ATTACK_DURATION = 1f;


    public static final String TEXTURE_NAME = "Eyeball";

    private Entity target;
    private float attackTimer;
    private float immobileTimer;
    private float animationTimer;
    private int frame;

    public Eyebat(Vector2 pos, Vector2 size) {
        super(pos, size, TextureHandler.getTexture(TEXTURE_NAME), DEFAULT_SPEED + (float) Math.random() * DEFAULT_SPEED / 2, DEFAULT_HEALTH);

        target = null;
        attackTimer = ANIMATION_DELAY;
        animationTimer = 0;
        immobileTimer  = 0;
        frame = 0;
    }

    public void update() {
        super.update();

        float dTime = Gdx.graphics.getDeltaTime();

        if (attackTimer > 0) {
            attackTimer -= dTime;
        }

        if (immobileTimer > 0) {
            immobileTimer -= dTime;
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

        if (Math.abs(pos.dst(target.getPos())) <= ATTACK_RANGE) {
            if (attackTimer <= 0) {
                attack(target);
                attackTimer += ATTACK_DELAY;
                immobileTimer += ATTACK_DURATION;
            }
        } else if (immobileTimer <= 0) {
            chase(target, dTime, true);
        }
    }

    public void die() {
        EntityHandler.killMonster(this);
    }

    //shoots laser at target
    private void attack(Entity target) {
        Polygon targetHitBox = target.getHitbox();

        Vector2 targetCenter = new Vector2(targetHitBox.getX() + targetHitBox.getOriginX(),
                targetHitBox.getY() + targetHitBox.getOriginY());
        Vector2 center = new Vector2(hitBox.getX() + hitBox.getOriginX(),
                hitBox.getY() +  hitBox.getOriginY());

        Vector2 dir = new Vector2(targetCenter).sub(center);
        Vector2 flat = new Vector2(100, 0);

        //angle between the line drawn by the texture and the direction it
        //should be turned towards.
        float rotation = (float) Math.toDegrees(Math.acos(dir.dot(flat) / (dir.len() * flat.len())));

        if (targetCenter.y < center.y) {
            rotation = -rotation;
        }

        Vector2 dbSize = new Vector2(dir.len(), size.y * 0.1f);

        EntityHandler.addMDamageBox(new DamageBox(DEFAULT_DAMAGE, -1, center, dbSize,
                new Vector2(0, 0), 0, TextureHandler.getTexture("laser"),
                rotation, ATTACK_DURATION, -1, 0, false, 1,
                0.2f));
    }

    private void animate() {
        currentTexture = new TextureRegion(spriteSheet, SIZE_CONV * (frame % 4), SIZE_CONV * (frame / 4), SIZE_CONV, SIZE_CONV);
        if (frame == 5) {
            frame = 0;
        } else {
            frame++;
        }

        if (flipped) {
            currentTexture.flip(true, false);
        }
        animationTimer = ANIMATION_DELAY;
    }
}
