package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;

public class Skeleton extends Entity {
    
    // speed in m/s
    private static final float DEFAULT_SPEED = 0.5f;
    private static final int DEFAULT_HEALTH = 100;
    private static final float ATTACK_DELAY = 0.5f;
    private static final float ANIMATION_DELAY = 0.5f;
    private static final String TEXTURE_NAME = "SpookySkele";
    
    private Entity target;
    private float animationTimer;
    private float attackTimer = ATTACK_DELAY;
    
    public Skeleton(Vector2 pos, Vector2 size) {
        super(pos, size, TextureHandler.getTexture(TEXTURE_NAME), DEFAULT_SPEED, DEFAULT_HEALTH);
        
        target = null;

        
    }

    public boolean update() {
        if (!super.update()) {
            return false;
        }

        float dTime = Gdx.graphics.getDeltaTime();
        if (animationTimer > 0) {
            animationTimer -= dTime;
        } else {
            animate();
        }
        
        
        
        
        
        
        return true;
    }

    private void animate() {

    }
}
