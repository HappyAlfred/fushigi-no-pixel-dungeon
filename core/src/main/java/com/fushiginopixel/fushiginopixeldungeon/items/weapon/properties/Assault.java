package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

public class Assault extends Weapon.Enchantment {

    private ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF00 );

    @Override
    public float proc(Weapon weapon, Char attacker, Char defender, int damage, EffectType type) {

        /*
        if(attacker instanceof Hero){
            Hero hero = (Hero)attacker;
            if(hero.justMovedPos != -1 && Dungeon.level.distance(hero.justMovedPos , defender.pos) > Dungeon.level.distance(hero.pos, defender.pos)){
                if(defender != null)
                    CellEmitter.center(defender.pos).burst(Speck.factory(Speck.STAR), 14);
                hero.justMovedPos = -1;
                return 1.5f;
            }
        }*/
        return 1;
    }

    @Override
    public boolean canCriticalAttack(Weapon weapon, Char attacker, Char defender, int damage, EffectType type) {

        if(attacker instanceof Hero){
            Hero hero = (Hero)attacker;
            if(hero.justMovedPos != -1 && Dungeon.level.distance(hero.justMovedPos , defender.pos) > Dungeon.level.distance(hero.pos, defender.pos)){
                hero.justMovedPos = -1;
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return YELLOW;
    }
}
