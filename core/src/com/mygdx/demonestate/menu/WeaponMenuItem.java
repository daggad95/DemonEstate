package com.mygdx.demonestate.menu;
import com.mygdx.demonestate.weapon.WeaponType;
/**
 * Created by david on 8/24/17.
 */
public class WeaponMenuItem {
    public int price;
    public String name;
    public WeaponType type;
    public int slotType;

    //whether or not the player owns this weapon
    public boolean owned;

    public WeaponMenuItem(int price, String name, WeaponType type, int slotType) {
        this.price = price;
        this.name = name;
        this.type = type;
        this.slotType = slotType;
        this.owned = false;
    }

    public String toString() {
        if (!owned)
            return name + " " + price;
        else
            return name + " *";
    }
}
