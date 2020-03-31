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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corrosion;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corruption;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.properties.GildedArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.GildedWeapon;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Door;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SlimeSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SwarmSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class Slime extends Mob {

	{
		spriteClass = SlimeSprite.class;
		
		HP = HT = 30;
		//defenseSkill = 8;

		EXP = 7;

		rareLootChance = 0;
		properties.add(Property.ACIDIC);
	}

	public int corrodeStr = 1;
	
	private static final float SPLIT_DELAY	= 1f;
	
	int generation	= 0;
	
	private static final String GENERATION	= "generation";
	private static final String PARTICAL_REC	= "particalRec";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GENERATION, generation );
		bundle.put( PARTICAL_REC, partialRec );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		generation = bundle.getInt( GENERATION );
		partialRec = bundle.getFloat( PARTICAL_REC );
		if (generation > 0) EXP = 0;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 5 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	{
		immunities.add(new EffectType(0,0,Roots.class));
	}

	private float partialRec = 0;

	@Override
	public boolean act() {

		if (Dungeon.level.water[pos] && HP < HT) {
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			partialRec += HT/100f;
			partialRec = Math.min(partialRec, HT-HP);
			while (partialRec >= 1){
				HP++;
				partialRec--;
			}
		}

		return super.act();
	}

	@Override
	public void die( Object cause, EffectType type ) {

		if(type.isExistAttachType(EffectType.MELEE) || type.isExistAttachType(EffectType.MISSILE)){
			destroy();
			((SlimeSprite)sprite).knockedFlag = true;
			sprite.die();
		}else {
			super.die(cause, type);
		}
	}

	public void corrodeEnemy(Char enemy, int damage, EffectType type){
		Buff.affect(enemy, Corrosion.class, new EffectType(type.attachType,EffectType.CORRROSION)).set(2f, 2);
		if(Random.Int(3) == 0){
			if(enemy instanceof Hero) {
				corrodeEquip((Hero) enemy, corrodeStr, 1, false);
			}else{
				Buff.prolong(enemy, Cripple.class,corrodeStr * 5, new EffectType(type.attachType,EffectType.CORRROSION));
				Buff.prolong(enemy, Weakness.class,corrodeStr * 5, new EffectType(type.attachType,EffectType.CORRROSION));
			}
		}

	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type) {
		damage = super.attackProc( weapon, enemy, damage,type );
		corrodeEnemy(enemy,damage,type);
		return damage;
	}

	public void corrodeEquip(Hero hero, int str, int count, boolean enchantment){
		//items the trap can curse if nothing else is available.
		ArrayList<Item> canCorrode = new ArrayList<>();
		KindOfWeapon weapon = hero.belongings.weapon;
		if (weapon != null && weapon instanceof Weapon) {
			if (weapon.level() >= 0 || (enchantment && ((Weapon)weapon).enchantmentCount() > 0))
				canCorrode.add(weapon);
		}

		Armor armor = hero.belongings.armor;
		if (armor != null) {
			if (armor.level() >= 0 || (enchantment && armor.glyphCount() > 0))
				canCorrode.add(armor);
		}

		Collections.shuffle(canCorrode);

		for (int i = 0; i < count; i++){
			if (!canCorrode.isEmpty()){
				corrode(hero ,canCorrode.remove(0), str, enchantment);
			}
		}
	}

	private static void corrode(Hero hero ,Item item ,int str ,boolean enchantment){
		if (item instanceof Weapon){
			Weapon w = (Weapon) item;
			if (w.enchantmentCount() > 0 && enchantment && (w.hasEnchant(GildedWeapon.class) || Random.Int(2) == 0)){
				Weapon.Enchantment en;
				if(!w.hasProperty(GildedWeapon.class) && w.hasEnchant(GildedWeapon.class)){
					en = w.getEnchantment(GildedWeapon.class);
				}else {
					en = Random.element(w.enchantment);
				}
				GLog.n( Messages.get(Slime.class, "enchantment_remove" , item.name(), en.name()) );
			}else{
				if(!w.hasEnchant(GildedWeapon.class)){
					if(w.level() < str && w.isUnique()){
						str = w.level();
					}

					/*
					if(w.level() < str && !w.isUnique()){
						w.doUnequip(hero ,false);
						GLog.n( Messages.get(Slime.class, "solute" , item.name()) );
					}else{
						w.degrade(str);
						GLog.n( Messages.get(Slime.class, "corrod" , item.name(), str) );
					}
					*/
					if(!w.isDegradeable() && !w.isUnique()){
						w.unEquip(hero);
						GLog.n( Messages.get(Slime.class, "solute" , item.name()) );
					}else{
						int lvl = w.level();
						w.degrade(str);
						GLog.n( Messages.get(Slime.class, "corrod" , item.name(), lvl - w.level()) );
					}
				}else{
					GLog.i( Messages.get(Slime.class, "corrode_failed" , item.name()) );
				}
			}
		}else if (item instanceof Armor){
			Armor a = (Armor) item;
			if (a.glyphCount() > 0 && enchantment && (a.hasGlyph(GildedArmor.class) || Random.Int(2) == 0)){
				Armor.Glyph en;
				if(!a.hasProperty(GildedArmor.class) && a.hasGlyph(GildedArmor.class)){
					en = a.getGlyph(GildedArmor.class);
				}else {
					en = Random.element(a.glyph);
				}
				GLog.n( Messages.get(Slime.class, "enchantment_remove" , item.name(), en.name()) );
			}else{
				if(!a.hasGlyph(GildedArmor.class)){
					if(a.level() < str && a.isUnique()){
						str = a.level();
					}

					if(!a.isDegradeable() && !a.isUnique()){
						a.unEquip(hero);
						GLog.n( Messages.get(Slime.class, "solute" , item.name()) );
					}else{
						int lvl = a.level();
						a.degrade(str);
						GLog.n( Messages.get(Slime.class, "corrod" , item.name(), lvl - a.level()) );
					}
				}else{
					GLog.i( Messages.get(Slime.class, "corrode_failed" , item.name()) );
				}
			}
		}
	}
	
	@Override
	public int defenseProc( Char enemy, int damage, EffectType type  ) {

		if (HP >= damage + 2 && Random.Int(4) == 0) {
			split( damage, enemy,type );
		}
		
		return super.defenseProc(enemy, damage, type);
	}

	@Override
	public int damage( int dmg, Object src, EffectType type ) {
		int damage = super.damage( dmg, src ,type );
		burstSplit(damage, src, type);
		return damage;

	}

	public void burstSplit(int dmg, Object src, EffectType type){
		if (isAlive() && type.isExistAttachType(EffectType.BURST)) {
			split( dmg, src, type, 3 );
		}
	}


	public void split( int damage, Object src,EffectType type) {
		split( damage, src, type ,1);
	}
	public void split( int damage, Object src,EffectType type ,int count) {
		{
			ArrayList<Integer> candidates = new ArrayList<>();
			boolean[] solid = Dungeon.level.solid;

			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (!solid[n] && Actor.findChar(n) == null) {
					candidates.add(n);
				}
			}
			count = Math.min(candidates.size(), count);
		}
		if (HP >= damage + count + 1 && isAlive()) {
			for(int i = 0; i < count; i++) {
				ArrayList<Integer> candidates = new ArrayList<>();
				boolean[] solid = Dungeon.level.solid;

				int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
				for (int n : neighbours) {
					if (!solid[n] && Actor.findChar(n) == null) {
						candidates.add(n);
					}
				}

				if (candidates.size() > 0) {

					Slime clone = split();
					clone.HP = (HP - damage) / (count + 1);
					clone.pos = Random.element(candidates);
					clone.state = clone.HUNTING;

					if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
						Door.enter(clone.pos);
					}

					GameScene.add(clone, SPLIT_DELAY);
					Actor.addDelayed(new Pushing(clone, pos, clone.pos), -1);
					Dungeon.level.press(clone.pos, clone, true);

					HP -= clone.HP;
				}
			}
		}
	}
	
	protected Slime split() {
		Slime clone = new Slime();
		clone.generation = generation + 1;
		clone.EXP = 0;
		if (buff( Burning.class ) != null) {
			Buff.affect( clone, Burning.class,new EffectType(0,EffectType.FIRE) ).reignite( clone );
		}
		if (buff( Poison.class ) != null) {
			Buff.affect( clone, Poison.class,new EffectType(0,EffectType.POISON) ).set(2);
		}
		if (buff(Corruption.class ) != null) {
			Buff.affect( clone, Corruption.class,new EffectType(0,EffectType.CORRROSION));
		}
		return clone;
	}
}
