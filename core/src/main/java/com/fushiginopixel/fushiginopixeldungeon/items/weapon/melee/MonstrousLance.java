package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Penetrate;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class MonstrousLance extends MeleeWeapon{

    {
        image = ItemSpriteSheet.MONSTROUS_LANCE;

        tier = 4;
        LIMIT = 3;
        RCH = 2;

        properties = new ArrayList<Weapon.Enchantment>(){
            {
                add(new Penetrate());
            }
        };
    }

    @Override
    public int min(int lvl) {
        return  2 +
                lvl;
    }

    @Override
    public int max(int lvl) {
        return  20 +
                lvl*(UPGRADE_ATTACK);
    }
}
