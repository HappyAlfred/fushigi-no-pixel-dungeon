package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Triradius;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class KamaitachiBlade extends MeleeWeapon{


    {
        image = ItemSpriteSheet.KAMAITACHI_BLADE;

        tier = 4;
        LIMIT = 4;
        RCH = 1;

        properties = new ArrayList<Enchantment>(){
            {
                add(new Triradius());
            }
        };
    }

    @Override
    public int min(int lvl) {
        return  1 +
                lvl;
    }

    @Override
    public int max(int lvl) {
        return  19 +
                lvl*(UPGRADE_ATTACK);
    }
}
