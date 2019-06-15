package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.effects.Wound;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

public class Beheading extends Weapon.Enchantment {

    private ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xAA0000 );
    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type ) {

        if(((float)defender.HP / defender.HT) <= 0.2f && damage > 0) {
            float damFactor = Math.max(damage, defender.HT);
            //defender.damage( defender.HP, this ,new EffectType(type.attachType,0));
            Wound.hit(defender);
            return damFactor/damage;
        }
        return 1f;
    }


    @Override
    public float accuracyAdapt(Weapon weapon, Char attacker, Char defender, float acc ) {
        if(((float)defender.HP / defender.HT) <= 0.2f){
            return 1000f;
        }
        else return 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}
