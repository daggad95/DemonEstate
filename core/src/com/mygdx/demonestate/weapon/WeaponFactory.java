package com.mygdx.demonestate.weapon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;

/**
 * Created by David on 3/15/2017.
 */
public class WeaponFactory {
    public static Weapon makeWeapon(WeaponType weaponType) {
        Weapon weapon = null;
        switch (weaponType) {
            case BASIC_PISTOL:
                weapon = makeBasicPistol();
                break;
            case HEAVY_PISTOL_1:
                weapon = makeHeavyPistol1();
                break;
        }

        return weapon;
    }

    public static Weapon makeBasicPistol() {
        float attackDelay = 0.5f;
        float reloadSpeed = 1.5f;
        float projectileMultiHit = 0.1f;
        float projectileVel = 10f;
        float spread = 10;
        float range = 10;
        int clipSize = 10;
        int damage = 10;
        Texture spriteSheet = TextureHandler.getTexture("gun");
        Texture projectileSpriteSheet = TextureHandler.getTexture("bullet");
        Vector2 size = new Vector2(0.5f, 0.5f);
        Vector2 projectileSize = new Vector2(0.1f, 0.1f);

        return new Weapon(spriteSheet, size, attackDelay, WeaponType.BASIC_PISTOL, spread, clipSize, reloadSpeed,
                damage, range, projectileSize, projectileVel, projectileSpriteSheet, projectileMultiHit);
    }

    public static Weapon makeHeavyPistol1() {
        float attackDelay = 0.5f;
        float reloadSpeed = 1.5f;
        float projectileMultiHit = 0.3f;
        float projectileVel = 10f;
        float spread = 5;
        float range = 12;
        int clipSize = 10;
        int damage = 20;
        Texture spriteSheet = TextureHandler.getTexture("gun");
        Texture projectileSpriteSheet = TextureHandler.getTexture("bullet");
        Vector2 size = new Vector2(0.5f, 0.5f);
        Vector2 projectileSize = new Vector2(0.1f, 0.1f);

        return new Weapon(spriteSheet, size, attackDelay, WeaponType.HEAVY_PISTOL_1, spread, clipSize, reloadSpeed,
                damage, range, projectileSize, projectileVel, projectileSpriteSheet, projectileMultiHit);
    }
}
