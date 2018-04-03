package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;

public class Skeleton extends Entity {

    // speed in m/s
    private static final float DEFAULT_SPEED = 0.5f;
    private static final int DEFAULT_HEALTH = 100;
    private static final float ATTACK_RANGE = 10.0f; // range for skele ranged attack

    private static final float WANDER_TIME = 1.0f;
    private static final float WANDER_ODDS = 0.7f;

    private static final String SKELETON_RIGHT = "SpookySkele";

    // skeleton has no attack delay

    private Entity target;
    private float animationTimer;

    public Skeleton(Vector2 pos, Vector2 size) {
        super(pos, size, TextureHandler.getTexture(SKELETON_RIGHT), DEFAULT_SPEED, DEFAULT_HEALTH);

        target = null;


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

        boolean inThrowRange = getDistanceTo(target).len() < ATTACK_RANGE;

        // skeleton is able to attack closest player
        if (inThrowRange) {

        } else {

        }



        return true;
    }
}
