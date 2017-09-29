package com.mygdx.demonestate.damagebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.MapHandler;
import com.mygdx.demonestate.TextureHandler;
import com.mygdx.demonestate.entity.Entity;
import com.mygdx.demonestate.entity.EntityHandler;

import java.util.HashSet;

/**
 * Created by David on 3/12/2017.
 */
public class DamageBox {
    //Conversion between game units and damagebox size
    public static final int SIZE_CONV = 64;

    protected int damage;

    //chance to hit after the first time
    protected float multiHitChance;
    protected float rotation;
    protected float duration;
    protected float range;
    protected float vel;
    protected float burnDamage;
    protected float burnChance;
    protected float shockChance;
    protected boolean explosive;
    protected boolean explosion;
    protected boolean ignoreWall;

    //knockback in meters
    protected float knockback;
    protected Vector2 pos;
    protected Vector2 size;
    protected Vector2 dir;
    protected Texture spriteSheet;
    protected TextureRegion currentTexture;
    protected Polygon hitBox;

    //entities that have been damaged by this box already
    protected HashSet<Entity> hitSet;

    public DamageBox() {}
    
    public DamageBox(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                     float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                     float knockback, float burnDamage, float burnChance, float shockChance, boolean ignoreWall) {
        this.damage = damage;
        this.range = range;
        this.pos = pos;
        this.size = size;
        this.vel = vel;
        this.dir = dir.nor();
        this.spriteSheet = spriteSheet;
        this.rotation = rotation;
        this.duration = duration;
        this.multiHitChance = multiHitChance;
        this.knockback = knockback;
        this.burnDamage = burnDamage;
        this.burnChance = burnChance;
        this.shockChance = shockChance;
        this.ignoreWall = ignoreWall;
        explosion = false;

        hitSet = new HashSet<Entity>();

        hitBox = new Polygon(new float[]{0, 0, size.x, 0, size.x, size.y, 0, size.y});
        hitBox.setOrigin(size.x / 2, size.y /2);
        hitBox.rotate(rotation);
        hitBox.setPosition(pos.x, pos.y);

        if (spriteSheet != null) {
            currentTexture = new TextureRegion(spriteSheet,
                    spriteSheet.getWidth(), spriteSheet.getHeight());
        }
    }

    public void update() {
        float dTime = Gdx.graphics.getDeltaTime();

        Vector2 moveDist = new Vector2(dir).scl(vel * dTime);

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

    public void applyDamage(Entity entity) {
        if (!hitSet.contains(entity)) {
            entity.takeDamage(damage, knockback, new Vector2(dir),
                    burnDamage, burnChance, shockChance);
            hitSet.add(entity);

            //removing projectile based on multiHitChance
            //-1 means infinite hits allowed
            if (multiHitChance != -1 && !explosion) {
                if (Math.random() <= multiHitChance) {
                    multiHitChance *= 0.5;
                    knockback = 0;
                } else {
                    die();
                }
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (currentTexture != null) {

            if (rotation == 0) {
                batch.draw(currentTexture, pos.x, pos.y, size.x, size.y);
            } else {
                batch.draw(currentTexture, pos.x, pos.y,
                        size.x /2, size.y /2,
                        size.x, size.y, 1, 1, rotation);
            }

        }
    }

    //sets box up for removal
    public void die() {
        range = 0;
        duration = 0;
    }

    public boolean dead() {
        return (range <= 0 && range != -1) || (duration <= 0 && duration != -1);
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getSize() {
        return size;
    }

    public Polygon getHitbox() {
        return hitBox;
    }
}
