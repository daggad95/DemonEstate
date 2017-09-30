package com.mygdx.demonestate.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.damagebox.*;
import com.mygdx.demonestate.entity.EntityHandler;
import com.mygdx.demonestate.entity.Player;

/**
 * Created by David on 3/11/2017.
 */
public class Weapon {
    //Conversion between game units and gun size
    public static final int SIZE_CONV = 32;

    //projectile velocity variance
    public static final float PROJ_VEL_VAR = 0.1f;

    private Vector2 size;

    //draw offset from player position
    private Vector2 offset;
    private Texture spriteSheet;
    private TextureRegion currentTexture;
    private float attackDelay;
    private float attackTimer;
    private float reloadTimer;
    private float spread; //bullet spread in degrees
    private float reloadSpeed;
    private float knockback;
    private int clipSize;
    private int clip;
    private float projectileMultiHit;
    private int damage;
    private float range;
    private Vector2 projectileSize;
    private float projectileVel;
    private int projectileNum;
    private Texture projectileSpriteSheet;
    private WeaponType type;
    private boolean reloading;
    private float burn_damage;
    private float burnChance;
    private float shockChance;
    private float duration;
    private DamageBoxType projectileType;
    private boolean flipped;
    private float animationDelay;
    private float animationTimer;
    private int numFrames;
    private int currentFrame;
    private int slotType;
    private Player player;

    public Weapon(Player player, Texture spriteSheet, Vector2 size, float attackDelay, WeaponType type, float spread, int clipSize, float reloadSpeed,
                  int damage, float range, Vector2 projectileSize, float projectileVel,
                  Texture projectileSpriteSheet, float projectileMultiHit, int projectileNum,
                  Vector2 offset, float knockback, float burn_damage,
                  float burnChance, float duration, DamageBoxType projectileType,
                  float animationDelay, float shockChance, int slotType) {

        this.spriteSheet = spriteSheet;
        this.size = size;
        this.attackDelay = attackDelay;
        this.type = type;
        this.spread = spread;
        this.clipSize = clipSize;
        this.reloadSpeed = reloadSpeed;
        this.damage = damage;
        this.range = range;
        this.projectileSize = projectileSize;
        this.projectileVel = projectileVel;
        this.projectileSpriteSheet = projectileSpriteSheet;
        this.projectileMultiHit = projectileMultiHit;
        this.projectileNum = projectileNum;
        this.offset = offset;
        this.knockback = knockback;
        this.burn_damage = burn_damage;
        this.burnChance = burnChance;
        this.duration = duration;
        this.projectileType = projectileType;
        this.animationDelay = animationDelay;
        this.shockChance = shockChance;
        this.slotType = slotType;
        this.player = player;

        clip = clipSize;
        attackTimer = 0;
        reloadTimer = 0;
        reloading = false;

        currentTexture = new TextureRegion(spriteSheet,
                SIZE_CONV, SIZE_CONV);
        numFrames = spriteSheet.getWidth() / SIZE_CONV;
        animationTimer = 0;
        currentFrame = 1;
        flipped = false;
    }

    public void update(boolean movingLeft, boolean movingRight) {
        float dTime = Gdx.graphics.getDeltaTime();

        if (animationTimer > 0) {
            animationTimer -= dTime;

            if (animationTimer <= 0) {
                if (currentFrame < numFrames) {
                    currentFrame++;
                    animationTimer = animationDelay;
                }
                else {
                    currentFrame = 1;
                }
                currentTexture = new TextureRegion(spriteSheet,
                        SIZE_CONV * (currentFrame - 1), 0, SIZE_CONV, SIZE_CONV);
                if (flipped)
                    currentTexture.flip(true, false);
            }

        }

        if (attackTimer > 0) {
            attackTimer -= dTime;
        }

        /*if (movingLeft && !flipped) {
            currentTexture.flip(true, false);
            flipped = true;

            //used to re-align flipped images
            offset.x = size.x - 1f;
        }
        else if (movingRight && flipped) {
            currentTexture.flip(true, false);
            flipped = false;
            offset.x = 0;
        }*/

        if (reloadTimer > 0) {
            reloadTimer -= dTime;

            if (reloadTimer <= 0) {
                clip = clipSize;
                reloading = false;
            }
        }
    }

    public void draw(SpriteBatch batch, Vector2 position, Vector2 size) {
        batch.draw(currentTexture, position.x - .5f * size.x, position.y - .5f * size.y,
                size.x * 2, size.y * 2);
    }

    public void attack(Vector2 pos, Vector2 dir) {

        if (attackTimer <= 0 && reloadTimer <= 0) {
            animationTimer = animationDelay;

            if (!reloading) {
                if (dir.x > 0) {
                    flipped = false;
                }
                else if (dir.x < 0) {
                    flipped = true;
                }

                for (int i = 0; i < projectileNum; i++) {
                    createProjectile(new Vector2(pos), dir);

                    if (clip != -1)
                        clip--;
                }
                attackTimer += attackDelay;

                if (clip < 1 && clip != -1) {
                    reloadTimer = reloadSpeed;
                    reloading = true;
                }
            }
        }
    }

    public void createProjectile(Vector2 pos, Vector2 dir) {
        //rotation of direction vector for projectile
        Vector2 rotate = new Vector2(dir).rotate((float) Math.random() * 2 * spread - spread);

        //projectile texture rotation
        float rotation = (float) Math.toDegrees(Math.atan(rotate.y / rotate.x));

        if(rotate.x < 0)
            rotation = rotation + 180;

        float velVariance = ((float) Math.random() * 2 * PROJ_VEL_VAR - PROJ_VEL_VAR) * projectileVel;

        pos.sub(projectileSize.x / 2, projectileSize.y / 2);

        boolean ignoreWall = slotType == Player.MELEE;

        DamageBox projectile;
        switch (projectileType) {
            case STANDARD:
                projectile = new DamageBox(damage, range, new Vector2(pos), new Vector2(projectileSize),
                        rotate, projectileVel + velVariance, projectileSpriteSheet,
                        rotation, duration, projectileMultiHit, knockback,
                        burn_damage, burnChance, shockChance, ignoreWall);
                break;
            case ROCKET:
                projectile = new Rocket(damage, range, new Vector2(pos), new Vector2(projectileSize),
                        rotate, projectileVel + velVariance, projectileSpriteSheet,
                        rotation, duration, projectileMultiHit, knockback,
                        burn_damage, burnChance, shockChance, ignoreWall);
                break;
            case HOMING:
                projectile = new Homing(damage, range, new Vector2(pos), new Vector2(projectileSize),
                        rotate, projectileVel + velVariance, projectileSpriteSheet,
                        rotation, duration, projectileMultiHit, knockback,
                        burn_damage, burnChance, shockChance, ignoreWall);
                break;
            case STICKY_BOMB:
                projectile = new StickyBomb(damage, range, new Vector2(pos), new Vector2(projectileSize),
                        rotate, projectileVel + velVariance, projectileSpriteSheet,
                        rotation, duration, projectileMultiHit, knockback,
                        burn_damage, burnChance, shockChance, ignoreWall);
                break;
            case BOOMERANG:
                projectile = new Boomerang(player, damage, range, new Vector2(pos), new Vector2(projectileSize),
                        rotate, projectileVel + velVariance, projectileSpriteSheet,
                        rotation, duration, projectileMultiHit, knockback,
                        burn_damage, burnChance, shockChance, ignoreWall);
                break;
            default:
                projectile = null;
        }
        EntityHandler.addPDamageBox(projectile);
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getReloadTimer() {
        return reloadTimer;
    }

    public float getReloadSpeed() {
        return reloadSpeed;
    }

    public WeaponType getType() { return type; }
}
