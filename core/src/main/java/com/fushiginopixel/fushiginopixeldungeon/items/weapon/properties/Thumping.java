package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

public class Thumping extends Weapon.Enchantment {

    private ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x111111 );

    public int priorityInAttack(){
        return 0;
    }

    @Override
    public float procInAttack(Weapon weapon, Char attacker, Char defender, int damage, EffectType type ) {

        if(defender instanceof Mob){
            if(defender.properties().contains(Char.Property.INORGANIC)){
                if(defender != null)
                    CellEmitter.center(defender.pos).burst(Speck.factory(Speck.STAR), 14);
                return 2f;
            }
        }
        return 1;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
