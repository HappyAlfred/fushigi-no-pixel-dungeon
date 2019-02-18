package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

public class BalanceAttack extends Weapon.Enchantment {

    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0000FF );

    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type) {
        //no proc effect, see armor.speedfactor for effect.
        return 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }
}
