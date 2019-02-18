package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MagicalSleep;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Sleepy extends Weapon.Enchantment {

    private ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xCCCCCC );
    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type ) {
        // lvl 0 - 13%
        // lvl 1 - 22%
        // lvl 2 - 30%
        int level = Math.max( 0, weapon.level() );

        if (isSuccess(level)) {

            final EffectType effectType = type;
            new FlavourBuff(){
                {actPriority = VFX_PRIO;}
                public boolean act() {
                    Buff.affect(target, MagicalSleep.class,new EffectType(effectType.attachType,EffectType.SPIRIT));
                    return super.act();
                }
            }.attachTo(defender);
            defender.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 12 );

        }

        return 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    public static boolean isSuccess(int level){

        return Random.Int( level / 2 + 100 ) >= 88;
    }
}
