package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;


public class FirstStrike extends Weapon.Enchantment{

    private ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x111111 );

    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type ) {

        return 1;
    }


    @Override
    public float accuracyAdapt( Weapon weapon, Char attacker ,Char target, float acc ) {
        //no proc effect, see weapon.accuracyFactor for effect
        if(target.HP == target.HT){
            return 1000f;
        }
        return 1f;
    }
    /*
    @Override
    public boolean canCriticalAttack(Weapon weapon, Char attacker, Char defender, int damage, EffectType type ) {

        if(defender.HP == defender.HT) {
            return true;
        }else{
            return false;
        }
    }
    */
    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    public static float strikeFactor(Weapon weapon){
        return 1.5f;
    }


}
