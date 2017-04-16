package com.mygdx.demonestate.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;

/**
 * Created by David on 1/7/2017.
 * Entities are things that are tangible and can
 * interact with the game world. Entity is abstract
 * and is used as a base model for players and npcs
 */
public abstract class Entity {
    //Conversion between game units and sprite size
    public static final int SIZE_CONV = 64;

    protected Vector2 pos;
    protected Vector2 size;
    protected Vector2 movementVector;
    protected Polygon hitBox;

    protected Texture spriteSheet;
    protected TextureRegion currentTexture;

    protected float speed;
    protected int health;
    protected Vector2[][] pathMap;
    protected boolean flipped;

    //determines which way the entity will rotate
    //when handling collision
    protected boolean rotateRight;

    public Entity() {}

    public Entity(Vector2 pos, Vector2 size, Texture spriteSheet, float speed, int health) {
        this.pos = pos;
        this.size = size;
        this.spriteSheet = spriteSheet;
        this.speed = speed;
        this.health = health;

        flipped = false;
        movementVector = new Vector2(0, 0);
        currentTexture = new TextureRegion(spriteSheet, SIZE_CONV, SIZE_CONV);

        hitBox = new Polygon(new float[]{0, 0, size.x, 0, size.x, size.y, 0, size.y});
        hitBox.setOrigin(size.x / 2, size.y / 2);
        hitBox.setPosition(pos.x, pos.y);

        if (Math.random() >= 0.5) {
            rotateRight = true;
        } else {
            rotateRight = false;
        }
    }

    public abstract void update();

    public void draw(SpriteBatch batch) {
        batch.draw(currentTexture, pos.x, pos.y, size.x, size.y);
    }

    public void setMovementVector(float x, float y) {
        movementVector.x = x;
        movementVector.y = y;
    }

    public Vector2 getPos() {
        return new Vector2(pos);
    }

    public Vector2 getSize() {
        return new Vector2(size);
    }

    public boolean dead() {
        return health <= 0;
    }

    public Polygon getHitbox() {
        return hitBox;
    }


    public void inflictDamage(int damage) {
        health -= damage;
    }

    public void die() {
        //System.out.print("DEAD!");
    }

    public Vector2[][] getPathMap() {
        return pathMap;
    }

    //will cause the entity to move towards
    //a target while avoiding walls and other
    //entities.
   protected void chase(Entity target, float dTime) {
        Vector2 dirBotLeft = MapHandler.getDirectionVector(new Vector2(pos), new Vector2(size), speed, dTime, target.getPathMap());
        Vector2 dirBotRight = MapHandler.getDirectionVector(new Vector2(pos.x + size.x, pos.y), new Vector2(size), speed, dTime, target.getPathMap());
        Vector2 dirTopLeft = MapHandler.getDirectionVector(new Vector2(pos.x, pos.y + size.y), new Vector2(size), speed, dTime, target.getPathMap());
        Vector2 dirTopRight = MapHandler.getDirectionVector(new Vector2(pos.x + size.x, pos.y + size.y), new Vector2(size), speed, dTime, target.getPathMap());

        boolean validMove = false;
        int rotation = 0;

        while (!validMove && Math.abs(rotation) < 360) {
            validMove = true;
            Vector2 velBotLeft = new Vector2(dirBotLeft).scl(speed * dTime).rotate(rotation);
            Vector2 velBotRight = new Vector2(dirBotRight).scl(speed * dTime).rotate(rotation);
            Vector2 velTopLeft = new Vector2(dirTopLeft).scl(speed * dTime).rotate(rotation);
            Vector2 velTopRight = new Vector2(dirTopRight).scl(speed * dTime).rotate(rotation);


            Vector2 posBotLeft = new Vector2(pos).add(velBotLeft);
            Vector2 posBotRight = new Vector2(pos).add(velBotRight);
            Vector2 posTopLeft = new Vector2(pos).add(velTopLeft);
            Vector2 posTopRight = new Vector2(pos).add(velTopRight);

            Vector2 newPos = pos;
            Vector2 vel = new Vector2(0, 0);
            if (!MapHandler.wallAt(posBotLeft, size) && !EntityHandler.collideMonster(this, posBotLeft)) {
                newPos = posBotLeft;
                vel = velBotLeft;
            } else if (!MapHandler.wallAt(posBotRight, size) && !EntityHandler.collideMonster(this, posBotRight)) {
                newPos = posBotRight;
                vel = velBotRight;
            } else if (!MapHandler.wallAt(posTopLeft, size) && !EntityHandler.collideMonster(this, posTopLeft)) {
                newPos = posTopLeft;
                vel = velTopLeft;
            } else if (!MapHandler.wallAt(posTopRight, size) && !EntityHandler.collideMonster(this, posTopRight)) {
                newPos = posTopRight;
                vel = velTopRight;
            } else {
                validMove = false;

                if (rotateRight) {
                    rotation++;
                } else  {
                    rotation--;
                }

                continue;
            }

            if (vel.x > 0 && flipped && rotation == 0) {
                flipped = false;
                currentTexture.flip(false, false);
            } else if (vel.x < 0 && !flipped && rotation == 0) {
                flipped = true;
                currentTexture.flip(true, false);
            }
            pos = newPos;
            hitBox.setPosition(pos.x, pos.y);
        }
    }

}
