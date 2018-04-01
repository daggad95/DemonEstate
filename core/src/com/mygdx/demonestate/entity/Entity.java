package com.mygdx.demonestate.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.TextureHandler;

import java.util.ArrayList;

/**
 * Created by David on 1/7/2017.
 * Entities are things that are tangible and can
 * interact with the game world. Entity is abstract
 * and is used as a base model for players and npcs
 */
public abstract class Entity {
    //Conversion between game units and sprite size
    public static final int SIZE_CONV = 16;
    public static final float KNOCKBACK_SPEED = 10;
    public static final float BURN_DURATION = 1f;

    public static final float SHOCK_DURATION = 10f;
    public static final float SHOCK_PULSE_DELAY = 1f;
    public static final float SHOCK_STUN_DURATION = 1f;
    public static final float SHOCK_STUN_CHANCE = 0.5f;
    public static final float SHOCK_SPREAD_CHANCE = 0.1f;
    public static final float SHOCK_DAMAGE = 5;



    //how long the body lasts after death
    public static final float DEATH_LINGER = 0.1f;

    //how long the entity is red after taking damage
    public static final float DAMAGE_TIME = 0.3f;

    protected Vector2 pos;
    protected Vector2 size;
    protected Vector2 movementVector;
    protected Polygon hitBox;

    protected Texture spriteSheet;
    protected TextureRegion currentTexture;

    protected float speed;
    protected float health;
    protected float maxHealth;
    protected float knockbackDistance;
    protected Vector2 knockbackDir;
    protected Vector2[][] pathMap;
    protected boolean flipped;
    protected float deathTimer;
    protected float damageTimer;
    protected float burnTimer;
    protected float shockTimer;
    protected float shockPulseTimer;
    protected float stunTimer;
    protected float burnDamage;

    //determines which way the entity will rotate
    //when handling collision
    protected boolean rotateRight;

    public Entity() {}

    public Entity(Vector2 pos, Vector2 size, Texture spriteSheet, float speed, float health) {
        this.pos = pos;
        this.size = size;
        this.spriteSheet = spriteSheet;
        this.speed = speed;
        this.health = health;
        this.maxHealth = health;

        flipped = false;
        movementVector = new Vector2(0, 0);
        currentTexture = new TextureRegion(spriteSheet, SIZE_CONV, SIZE_CONV);

        hitBox = new Polygon(new float[]{0, 0, size.x, 0, size.x, size.y, 0, size.y});
        hitBox.setOrigin(size.x / 2, size.y / 2);
        hitBox.setPosition(pos.x, pos.y);
        deathTimer = DEATH_LINGER;

        if (Math.random() >= 0.5) {
            rotateRight = true;
        } else {
            rotateRight = false;
        }

        burnTimer = 0;
        burnDamage = 0;
        shockTimer = 0;
        shockPulseTimer = 0;
    }

    public void update() {
        hitBox.setPosition(pos.x, pos.y);

        if (knockbackDistance > 0) {
            Vector2 velVector = knockbackDir.nor().scl((KNOCKBACK_SPEED)
                    * Gdx.graphics.getDeltaTime());
            Vector2 newPos = new Vector2(pos).add(velVector);
            ArrayList<Entity> collisions = EntityHandler.getCollisions(this);

            if (!MapHandler.wallAt(newPos, size)) {
                if (collisions != null) {
                    for (Entity e : collisions) {
                        e.takeDamage(0, knockbackDistance / 2,
                                knockbackDir, 0, 0, 0);
                    }
                }
                pos.add(velVector);

            }
            knockbackDistance -= velVector.len();
        }

        if (stunTimer > 0)
            stunTimer -= Gdx.graphics.getDeltaTime();

        if (shockTimer > 0) {
            shockTimer -= Gdx.graphics.getDeltaTime();

            if (shockPulseTimer <= 0) {
                if (Math.random() < SHOCK_STUN_CHANCE) {
                    stunTimer += SHOCK_STUN_DURATION;
                    health -= SHOCK_DAMAGE;

                    for (Entity e : EntityHandler.getCollisions(this)) {
                        if (Math.random() < SHOCK_SPREAD_CHANCE)
                            e.shockTimer += SHOCK_DURATION;
                    }
                }
                shockPulseTimer += SHOCK_PULSE_DELAY;
            }
        }

        if (shockPulseTimer > 0) {
            shockPulseTimer -= Gdx.graphics.getDeltaTime();
        }

        if (burnTimer > 0) {
            health -= burnDamage * Gdx.graphics.getDeltaTime();
            burnTimer -= Gdx.graphics.getDeltaTime();
        }

        if (damageTimer > 0)
            damageTimer -= Gdx.graphics.getDeltaTime();

        if (dead()) {
            if (deathTimer >= 0) {
                deathTimer -= Gdx.graphics.getDeltaTime();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (burnTimer > 0) {
            batch.draw(TextureHandler.getTexture("fire"),
                    pos.x, pos.y, size.x, size.y);
        }

        if (shockTimer > 0) {
            batch.draw(TextureHandler.getTexture("shock"),
                    pos.x, pos.y, size.x, size.y);
        }

        if (damageTimer > 0)
          batch.setColor(Color.RED);
        batch.draw(currentTexture, pos.x, pos.y, size.x, size.y);
        batch.setColor(Color.WHITE);
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


    public void takeDamage(int damage, float knockback, Vector2 knockbackDir,
                           float burnDamage, float burnChance, float shockChance) {

        if (damage > 0) {
            damageTimer = DAMAGE_TIME;
            health -= damage;
        }

        if (Math.random() <= shockChance && shockTimer <= 0) {
            shockTimer += SHOCK_DURATION;
        }

        if (knockbackDistance <= 0) {
            knockbackDistance += knockback;
            this.knockbackDir = knockbackDir;

            if (burnDamage > 0 && Math.random() < burnChance) {
                this.burnDamage = burnDamage;
                this.burnTimer += BURN_DURATION;
            }

        }
    }

    public void die() {
        //System.out.print("DEAD!");
    }

    public float getDeathTimer() {
        return deathTimer;
    }

    public Vector2[][] getPathMap() {
        return pathMap;
    }

    //will cause the entity to move towards
    //a target while avoiding walls and other
    //entities.
   protected void chase(Entity target, float dTime, boolean allowCollision) {

       if (!allowCollision) {
           ArrayList<Entity> collisions = EntityHandler.getCollisions(this);

           if (collisions.size() > 0) {
               Vector2 moveDir = new Vector2(pos).sub(collisions.get(0).pos);
               move(moveDir, speed, false);
               return;
           }
       }

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
           if (!MapHandler.wallAt(posBotLeft, size)) {
               newPos = posBotLeft;
               vel = velBotLeft;
           } else if (!MapHandler.wallAt(posBotRight, size)) {
               newPos = posBotRight;
               vel = velBotRight;
           } else if (!MapHandler.wallAt(posTopLeft, size)) {
               newPos = posTopLeft;
               vel = velTopLeft;
           } else if (!MapHandler.wallAt(posTopRight, size)) {
               newPos = posTopRight;
               vel = velTopRight;
           } else {
               validMove = false;

               if (rotateRight) {
                   rotation++;
               } else {
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
       }
   }

    protected boolean move(Vector2 moveDir, float moveVel, boolean allowFlip) {
        Vector2 moveVector = moveDir.nor().scl(moveVel * Gdx.graphics.getDeltaTime());
        if (!MapHandler.wallAt(new Vector2(pos).add(moveVector), new Vector2(size))) {
            pos.add(moveVector);

            if (allowFlip) {
                if (moveDir.x > 0 && flipped) {
                    flipped = false;
                    currentTexture.flip(false, false);
                } else if (moveDir.x < 0 && !flipped) {
                    flipped = true;
                    currentTexture.flip(true, false);
                }
            }
            hitBox.setPosition(pos.x, pos.y);
            return true;
        }

        return false;
    }

}
