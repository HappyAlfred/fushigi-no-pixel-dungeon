package com.fushiginopixel.fushiginopixeldungeon.items.armor.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;


public class GildedArmor extends Armor.Glyph{

    private ItemSprite.Glowing GOLD = new ItemSprite.Glowing( 0xFFD700 );

    @Override
    public float proc( Armor armor, Char attacker, Char defender, int damage , EffectType type ) {

        return 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GOLD;
    }


}
