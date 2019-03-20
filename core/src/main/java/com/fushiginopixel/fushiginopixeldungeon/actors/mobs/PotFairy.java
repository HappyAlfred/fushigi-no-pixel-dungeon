/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Honeypot;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.InventoryPot;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PatrolDogSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PotFairySprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class PotFairy extends Mob {

	{
		spriteClass = PotFairySprite.class;
		
		HP = HT = 50;
		defenseSkill = 18;
        EXP = 9;

        HUNTING = new Hunting();
	}

    protected Pot pot = null;
	protected int initSize = 1;

    public PotFairy() {
        super();
        addPot();
    }

    public void addPot(){
        do {
            pot = (Pot) Generator.random(Generator.Category.POT);
        } while (pot.cursed);

        pot.size = initSize;
    }
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 6 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}

    protected Item steal(Hero hero, EffectType type ) {

        Item item = hero.belongings.randomUnequipped();

        if (item != null && !item.isUnique() && item.level() < 1 ) {

            GLog.w( Messages.get(PotFairy.class, "suck", item.name(), pot.name()) );
            if (!item.stackable || hero.belongings.getSimilar(item) == null) {
                Dungeon.quickslot.convertToPlaceholder(item);
            }
            item.updateQuickslot();

            Item stole = item.detach( hero.belongings.backpack );
                if ( stole instanceof Honeypot.ShatteredPot)
                    ((Honeypot.ShatteredPot)item).setHolder(this);

            return stole;
        } else {
            return null;
        }
    }

    public void suck( Char enemy ) {
        this.sprite.zap(enemy.pos);
        if (enemy instanceof Hero && pot instanceof InventoryPot){
            Item item = steal((Hero) enemy, new EffectType(0, 0));
            input(item);
        }
        else{
            Buff.affect(enemy , Vertigo.class , Random.NormalIntRange(4, 8), new EffectType(0,0));
        }
    }

    public void input(Item item){
        ((InventoryPot) pot).onItemSelected(item);
    }

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

    protected class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;

            if (enemyInFOV
                    && !isCharmedBy(enemy)
                    && canAttack(enemy)) {
                if (Random.Int(4) == 0) {

                    suck(enemy);
                    spend(TICK);
                    next();
                    return true;

                } else {
                    return super.act(enemyInFOV, justAlerted);
                }
            } else {
                return super.act(enemyInFOV, justAlerted);
            }
        }
    }
}
