package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;

import java.util.ArrayList;

public class Penetrate extends Weapon.Enchantment {

    @Override
    public boolean procBeforeAttack(Weapon weapon, Char attacker, Char defender, boolean process, EffectType type ) {

        if (process && !type.isExistAttachType(EffectType.SPLIT)){
            int maxReach = weapon.hasProperty(getClass()) ? weapon.reachFactor(attacker) : weapon.RCH;
            Ballistica penetrate = new Ballistica(attacker.pos, defender.pos, Ballistica.WONT_STOP);

            ArrayList<Char> chars = new ArrayList<>();
            for (int c : penetrate.subPath(1, maxReach)) {

                Char ch = Actor.findChar( c );
                if (ch != null && ch != defender && attacker.canAttack(defender)) {
                    chars.add( ch );
                }
            }

            for(Char target: chars){
                attacker.attack(target, new EffectType(type.attachType | EffectType.SPLIT, type.effectType), weapon);
            }

        }

        return true;
    }
}
