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
    public static final int SIZE_CONV = 32;

    private int damage;

    //chance to hit after the first time
    private float multiHitChance;
    private float rotation;
    private float duration;
    private float range;
    private float vel;
    private boolean explosive;
    private boolean explosion;

    //knockback in meters
    private float knockback;
    private Vector2 pos;
    private Vector2 size;
    private Vector2 dir;
    private Texture spriteSheet;
    private TextureRegion currentTexture;
    private Polygon hitBox;

    //entities that have been damaged by this box already
    private HashSet<Entity> hitSet;

    public DamageBox(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                     float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                     float knockback, boolean explosive) {
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
        this.explosive = explosive;
        explosion = false;

        hitSet = new HashSet<Entity>();


        hitBox = new Polygon(new float[]{0, 0, size.x, 0, size.x, size.y, 0, size.y});
        hitBox.setOrigin(0, 0);
        hitBox.rotate(rotation);
        hitBox.setPosition(pos.x, pos.y);

        if (spriteSheet != null) {
            float whratio = size.x / size.y;
            currentTexture = new TextureRegion(spriteSheet,
                    (int) (SIZE_CONV * whratio), SIZE_CONV);
        }
    }

    public DamageBox(int damage, float range, Vector2 pos, Vector2 size, Vector2 dir,
                     float vel, Texture spriteSheet, float rotation, float duration, float multiHitChance,
                     float knockback, boolean explosive, boolean explosion) {
        this(damage, range, pos, size, dir,
            vel, spriteSheet, rotation, duration, multiHitChance,
            knockback, explosive);
        this.explosion = explosion;

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

        if (MapHandler.wallAt(new Vector2(pos), new Vector2(size))) {
            range = 0;
        }
    }

    public void applyDamage(Entity entity) {
        if (!hitSet.contains(entity)) {
            if (explosive) {
                //creating explosion box

                //size damage conversion
                float sdc = 0.1f;

                EntityHandler.addPDamageBox(new DamageBox(damage, -1,
                        new Vector2(pos).add(-damage * sdc * 0.5f,
                                -damage * sdc * 0.5f),
                        new Vector2(damage * sdc, damage * sdc),
                        dir, 0,
                        TextureHandler.getTexture("explosion"),
                        0, 0.5f, multiHitChance, knockback,
                        false, true));
                die();
                return;
            }
            else if (explosion) {
                entity.inflictDamage(damage, knockback,
                        new Vector2(dir).rotate((float) Math.random() * 180 - 90));
            }
            else {
                entity.inflictDamage(damage, knockback, new Vector2(dir));
            }
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
                batch.draw(currentTexture, pos.x, pos.y, 0, 0, size.x, size.y, 1, 1, rotation);
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
