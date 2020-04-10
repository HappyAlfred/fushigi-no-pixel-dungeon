package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

public class BalanceAttack extends Weapon.Enchantment {

    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }
}
