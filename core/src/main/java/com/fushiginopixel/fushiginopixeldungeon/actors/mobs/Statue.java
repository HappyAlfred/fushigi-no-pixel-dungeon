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
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectResistance;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon.Enchantment;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Statue extends Mob {
	
	{
		spriteClass = StatueSprite.class;

		EXP = 0;
		state = PASSIVE;
		
		properties.add(Property.INORGANIC);
	}
	
	//protected Weapon weapon = null;
	
	public Statue() {
		super();
		addWeapon();
		HP = HT = 15 + Dungeon.depth * 5;
		//defenseSkill = 4 + Dungeon.depth;
	}

	public void addWeapon(){
		Weapon weapon = null;
		do {
			weapon = (MeleeWeapon) Generator.random(Generator.Category.WEAPON);
		} while (weapon.cursed);

		weapon.enchant( Enchantment.random() );
		weapon.equip(this);
	}
	
	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos]) {
			Notes.add( Notes.Landmark.STATUE );
		}
		return super.act();
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( HT / 10, HT / 4 );
	}

	/*
	@Override
	protected boolean canAttack(Char enemy) {
		if (weapon != null) {
			return Dungeon.level.distance(pos, enemy.pos) <= weapon.reachFactor(this);
		}
		else{
			return super.canAttack(enemy);
		}
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, Dungeon.depth);
	}
	
	@Override
	public int damage( int dmg, Object src ,EffectType type) {

		if (state == PASSIVE) {
			state = HUNTING;
		}

		return super.damage( dmg, src ,type);
	}
	
	@Override
	public void beckon( int cell ) {
		// Do nothing
	}
	
	@Override
	public void die( Object cause, EffectType type ) {
		if(belongings.weapon != null) {
			KindOfWeapon w = belongings.weapon;
			w.identify();
			w.unEquip(this);
			Dungeon.level.drop(w, pos).sprite.drop();
		}
		super.die( cause, type );
	}
	
	@Override
	public void destroy(Object src, EffectType type ) {
		Notes.remove( Notes.Landmark.STATUE );
		super.destroy(src, type);
	}
	
	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {

		if (belongings.weapon != null)
			return Messages.get(this, "desc", belongings.weapon.name());
		else{
			return Messages.get(this, "desc_1");
		}
	}
	
	{
		resistances.add(new EffectResistance(new EffectType(Grim.class), 0.5f));
	}
	
}
