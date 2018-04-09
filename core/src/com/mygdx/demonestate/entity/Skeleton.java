package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.damagebox.DamageBox;


public class Skeleton extends Entity {

    public static final float DEFAULT_SIZE = 1.0f; // made public for access in EntityHandler
    private static final float DEFAULT_SPEED = 1.5f; // m/s
    private static final float DEFAULT_RANGE = 10.0f;
    private static final int DEFAULT_HEALTH = 100;
    private static final int DEFAULT_DAMAGE = 25;
    private static final int DEFAULT_PROJ_SPEED = 10;

    private static final float pileTimer = 1.0f;
    private boolean isAlive = true;

    private static final String SKELETON_RIGHT = "SpookySkele";
    private static final String SKELETON_HEAD_RIGHT = "SpookySkele(HeresMyHead)_Right";
    private static final String SKELETON_HEAD_LEFT = "SpookySkele(HeresMyHead)_Left";
    private static final String SKELETON_BODY = "SpookySkele(WheresMyHead)";
    private static final String SKELETON_PILE = "SpookyBonePile";

    private Entity target;

    public Skeleton(Vector2 pos) {
        super(pos, TextureHandler.getTexture(SKELETON_RIGHT), DEFAULT_SPEED, DEFAULT_HEALTH);

        // override original deathTimer = DEATH_LINGER = 0.1f from Entity class
        deathTimer = 2;
        target = null;
        size = new Vector2(DEFAULT_SIZE, DEFAULT_SIZE);
        setHitBox(); // must be called AFTER size is set
    }

    public boolean update() {
        if (!super.update()) {
            return false;
        }

        float dTime = Gdx.graphics.getDeltaTime();

        // select closest player as current target
        for (Entity player : EntityHandler.getPlayers()) {
            if(target == null || pos.dst2(player.getPos()) < pos.dst2(target.getPos())) {
                target = player;
            }
        }

        boolean inThrowRange = getDistanceTo(target).len() < DEFAULT_RANGE;

        if(isAlive) {

            // skeleton is able to attack closest player
            if (inThrowRange) {
                attack(getDirectionTo(target));
                isAlive = false;
                health = 0;
                // headless texture should match the facing direction when head is thrown
                currentTexture = new TextureRegion(TextureHandler.getTexture(SKELETON_BODY), SIZE_CONV, SIZE_CONV);
                if(!facingRight) {
                    currentTexture.flip(true,false);
                }

            } else {
                // allowCollision = true means Skeletons can phase through each other
                chase(target, dTime, true);
            }
        }

        if(deathTimer < pileTimer) {
            currentTexture = new TextureRegion(TextureHandler.getTexture(SKELETON_PILE), SIZE_CONV, SIZE_CONV);
        }

        return true;
    }

    private void attack(Vector2 dir) {
        Texture headProjectile;
        // use left or right head texture as projectile
        if(facingRight) {
            headProjectile = TextureHandler.getTexture(SKELETON_HEAD_RIGHT);
        } else {
            headProjectile = TextureHandler.getTexture(SKELETON_HEAD_LEFT);
        }
        EntityHandler.addMDamageBox(new DamageBox(DEFAULT_DAMAGE, DEFAULT_RANGE, getPos(), getSize(), dir, DEFAULT_PROJ_SPEED,
                headProjectile, 0, -1, -1, 1, 0, 0, 0, true));
    }

}
