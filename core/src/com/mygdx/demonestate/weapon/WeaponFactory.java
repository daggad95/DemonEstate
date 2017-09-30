package com.mygdx.demonestate.weapon;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.demonestate.TextureHandler;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.mygdx.demonestate.damagebox.DamageBox;
import com.mygdx.demonestate.damagebox.DamageBoxType;
import com.mygdx.demonestate.entity.Player;

import java.util.HashMap;

/**
 * Created by David on 3/15/2017.
 */
public class WeaponFactory {

    public static Weapon makeWeapon(WeaponType type, Player player) {
        XmlReader reader = new XmlReader();

        try {
            XmlReader.Element weaponStats = reader.parse(new FileHandle("assets/data/weapon_stats.xml"));
            String weaponName = type.toString();

            float attackDelay
                    = Float.parseFloat(weaponStats.getChildByName(weaponName).get("attackDelay"));
            float reloadSpeed
                    = Float.parseFloat(weaponStats.getChildByName(weaponName).get("reloadSpeed"));
            float projectileMultiHit
                    = Float.parseFloat(weaponStats.getChildByName(weaponName).get("projectileMultiHit"));
            float projectileVel
                    = Float.parseFloat(weaponStats.getChildByName(weaponName).get("projectileVel"));
            float spread =
                    Float.parseFloat(weaponStats.getChildByName(weaponName).get("spread"));
            float range =
                    Float.parseFloat(weaponStats.getChildByName(weaponName).get("range"));
            float knockback =
                    Float.parseFloat(weaponStats.getChildByName(weaponName).get("knockback"));
            int clipSize =
                   Integer.parseInt(weaponStats.getChildByName(weaponName).get("clipSize"));
            int damage
                    = Integer.parseInt(weaponStats.getChildByName(weaponName).get("damage"));
            int projectileNum
                    = Integer.parseInt(weaponStats.getChildByName(weaponName).get("projectileNum"));

            Texture spriteSheet
                    = TextureHandler.getTexture(weaponStats.getChildByName(weaponName).get("spriteSheet"));
            Texture projectileSpriteSheet
                    = TextureHandler.getTexture(weaponStats.getChildByName(weaponName).get("projectileSpriteSheet"));

            Vector2 size = new Vector2();
            size.x = Float.parseFloat(weaponStats.getChildByName(weaponName).getChildByName("size").get("x"));
            size.y = Float.parseFloat(weaponStats.getChildByName(weaponName).getChildByName("size").get("y"));

            Vector2 offset = new Vector2();
            offset.x = Float.parseFloat(weaponStats.getChildByName(weaponName).getChildByName("offset").get("x"));
            offset.y = Float.parseFloat(weaponStats.getChildByName(weaponName).getChildByName("offset").get("y"));

            Vector2 projectileSize = new Vector2();
            projectileSize.x = Float.parseFloat(weaponStats.getChildByName(weaponName).getChildByName("projectileSize").get("x"));
            projectileSize.y = Float.parseFloat(weaponStats.getChildByName(weaponName).getChildByName("projectileSize").get("y"));

            float burn_damage = Float.parseFloat(weaponStats.getChildByName(weaponName).get("burnDamage"));
            float burnChance = Float.parseFloat(weaponStats.getChildByName(weaponName).get("burn_chance"));
            float shockChance = Float.parseFloat(weaponStats.getChildByName(weaponName).get("shock_chance"));
            float duration = Float.parseFloat(weaponStats.getChildByName(weaponName).get("duration"));
            float animationDelay = Float.parseFloat(weaponStats.getChildByName(weaponName).get("animation_delay"));
            int slotType = Integer.parseInt(weaponStats.getChildByName(weaponName).get("slotType"));

            DamageBoxType damageBoxType = DamageBoxType.valueOf(weaponStats.
                    getChildByName(weaponName).get("projectile_type"));

            return new Weapon(player, spriteSheet, size, attackDelay, type, spread, clipSize, reloadSpeed,
                    damage, range, projectileSize, projectileVel, projectileSpriteSheet, projectileMultiHit,
                    projectileNum, offset, knockback, burn_damage, burnChance, duration, damageBoxType,
                    animationDelay, shockChance, slotType);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("weapon stats file not found");
            System.exit(0);
        }

        return null;
    }

}
