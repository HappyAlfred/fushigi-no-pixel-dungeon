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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corruption;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Slow;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.KindofMisc;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfCorruption;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.CursingTrap;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.LichSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class Lich extends Skeleton {

	private static final float TIME_TO_ZAP	= 1f;
	
	{
		spriteClass = LichSprite.class;

		HP = HT = 210;
		defenseSkill = 32;
		
		EXP = 21;

		loot = null;    //see createloot.
		lootChance = 0.05f;
	}

	/*{
		resistances.add(Grim.class);
	}*/

	{
		//immunities.add(new EffectType(Corruption.class));
		immunities.add(new EffectType(0,EffectType.DARK));
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 26, 48 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 62;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 12);
	}


	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	protected boolean doAttack( Char enemy ) {

		if (Dungeon.level.adjacent( pos, enemy.pos )) {

			return super.doAttack( enemy );

		} else {

			boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap( enemy.pos );
			} else {
				zap();
			}

			return !visible;
		}
	}

	private void zap() {
		spend( TIME_TO_ZAP );

		if (hit( this, enemy, true )) {
			int flag = enemy == Dungeon.hero ? Random.Int( 8 ):Random.Int( 4 );
			EffectType effectType = new EffectType(EffectType.MAGICAL_BOLT,0);
			switch (flag) {
				case(0) :Buff.prolong(enemy, Blindness.class, 10f,new EffectType(EffectType.MAGICAL_BOLT,EffectType.DARK));break;
				case(1) :Buff.prolong(enemy, Slow.class, 10f,new EffectType(EffectType.MAGICAL_BOLT,EffectType.SPIRIT));break;
				case(2) :Buff.prolong(enemy, Weakness.class, 10f,new EffectType(EffectType.MAGICAL_BOLT,EffectType.DARK));break;
				case(3) :{new FlavourBuff(){
								{actPriority = VFX_PRIO;}
								public boolean act() {
									Buff.affect(target, Frost.class, 10f,new EffectType(EffectType.MAGICAL_BOLT,EffectType.ICE));
									return super.act();
								}
							}.attachTo(enemy);;
							}break;

				case(4) :forgetItem((Hero)enemy , 2, false);break;
				case(5) :CursingTrap.curse((Hero)enemy , 1, false);break;
				case(6) :{
					ScrollOfTeleportation.teleportHero( (Hero)enemy);
					BArray.setFalse(Dungeon.level.visited);
					BArray.setFalse(Dungeon.level.mapped);
					GameScene.updateFog();
					Dungeon.observe();
				}break;
				case(7) :(((Hero)enemy).buff( Hunger.class )).satisfy( -Hunger.STARVING / 10 );{
					GLog.n( Messages.get(this, "hungry") );
				}break;
			}
			switch (flag){
				case(0):
				case(2):
				case(5):
				case(7):
					effectType.effectType = EffectType.DARK;break;
				case(1):
				case(4):
					effectType.effectType = EffectType.SPIRIT;break;
				case(3):
					effectType.effectType = EffectType.ICE;break;
			}

			int dmg = Random.Int( 30, 30 );
			enemy.damage( dmg, this ,effectType);

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "bolt_kill") );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}

	public void forgetItem(Hero hero, int count, boolean equiponly){

		//items the trap can curse if nothing else is available.
		ArrayList<Item> canForget = new ArrayList<>();

		if(equiponly) {
			KindOfWeapon weapon = hero.belongings.weapon;
			if (weapon != null && weapon.isIdentified()) {
				canForget.add(weapon);
			}

			Armor armor = hero.belongings.armor;
			if (armor != null && armor.isIdentified()) {
				canForget.add(armor);
			}

			KindofMisc misc1 = hero.belongings.misc1;
			if (misc1 != null && misc1.isIdentified()) {
				canForget.add(misc1);
			}

			KindofMisc misc2 = hero.belongings.misc2;
			if (misc2 != null && misc2.isIdentified()) {
				canForget.add(misc2);
			}
		}else{
			for(Item itemhad : hero.belongings) {
				if(itemhad.isIdentified()){
					canForget.add(itemhad);
				}

				if(itemhad instanceof Bag) {
					for (Item item : hero.belongings.backpack.items) {

						if(itemhad.isIdentified()){
							canForget.add(item);
						}

					}
				}
			}

		}
		Collections.shuffle(canForget);

		int numCurses = Random.Int(count) + 1;

		for (int i = 0; i < numCurses; i++){
			if (!canForget.isEmpty()){
				canForget.remove(0).forget();
			}
		}

		GLog.n( Messages.get(Lich.class, "forget") );


	}

	public void onZapComplete() {
		zap();
		next();
	}
	
	@Override
	protected Item createLoot() {
		Item loot;
		if (Random.Int(3) > 1) {
			loot = new WandOfCorruption().random();
			loot.cursed = false;
			loot.level(0);
			return loot;
		}
		else{
			loot = Generator.random(Generator.Category.WAND);
			loot.cursed = true;
			loot.level(0);
			return loot;
		}
	}

}
