package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;


public class Assassination extends Weapon.Enchantment{

    private ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x111111 );

    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type) {

        return 1;
    }

    @Override
    public String desc(Weapon weapon) {
        int factor = Math.round(minFactor(weapon) * 100);
        return Messages.get(this, "desc" , factor);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    public static float minFactor(Weapon weapon){
        int lvl = Math.max(weapon.level() , 0);
        float factor = 1f - lvl/(lvl + 15f);
        factor = Math.max(factor,0.5f);
        return factor;
    }


}
