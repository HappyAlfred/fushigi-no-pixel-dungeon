package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;


public class GildedWeapon extends Weapon.Enchantment{

    private ItemSprite.Glowing GOLD = new ItemSprite.Glowing( 0xFFD700 );

    @Override
    public ItemSprite.Glowing glowing() {
        return GOLD;
    }


}
