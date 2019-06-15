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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.CorrosiveGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Belongings;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.AntiMagic;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Brimstone;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CapeOfThorns;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfEvasion;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfHaste;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfTenacity;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MirrorSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class MirrorImage extends NPC {
	
	{
		spriteClass = MirrorSprite.class;
		
		alignment = Alignment.ALLY;
		state = HUNTING;

		properties.add(Property.INORGANIC);
	}
	
	public int tier;
	
	//private int attack;
	private int attackSkill = 0;
	//private int damage;
	
	private static final String TIER	= "tier";
	private static final String ATTACK	= "attack";
	private static final String DEFENSE	= "defense";
	//private static final String DAMAGE	= "damage";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( TIER, tier );
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		//bundle.put( DAMAGE, damage );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		tier = bundle.getInt( TIER );
		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );
		//damage = bundle.getInt( DAMAGE );
	}
	
	public void duplicate( Hero hero ) {
		tier = hero.tier();
		attackSkill = hero.attackSkill;
		defenseSkill = hero.defenseSkill;
		//damage = hero.damageRoll();

		//maybe cause bug? I am afraid
		if(hero.belongings != null) {
			Bundle copy = new Bundle();
			hero.belongings.storeInBundle(copy);
			belongings.restoreFromBundle(copy);
			for (Item item : belongings.backpack.items.toArray( new Item[0])) {
				if (!item.isEquipped( this )) {
					item.detachAll( belongings.backpack );
				}
			}
		}
	}

	@Override
	public int attackSkill( Char target ) {
		KindOfWeapon wep = belongings.weapon;

		if (wep != null) {
			return (int)(attackSkill * wep.accuracyFactor( this ,target));
		} else {
			return attackSkill;
		}
	}

	@Override
	public int defenseSkill( Char enemy ) {

		float evasion = defenseSkill;

		evasion *= RingOfEvasion.evasionMultiplier( this );

		if (belongings.armor != null) {
			evasion = belongings.armor.evasionFactor(this, enemy, evasion);
		}

		return Math.round(evasion);
	}

	@Override
	public int damageRoll() {
		KindOfWeapon wep = belongings.weapon;
		int dmg;
		if (wep != null) {
			dmg = wep.damageRoll( this );
		} else {
			dmg = Random.NormalIntRange(1, 5);
		}
		if (dmg < 0) dmg = 0;

		return dmg;
	}

	@Override
	public int drRoll() {
		int dr = 0;
		if (belongings.armor != null) {
			int armDr = Random.NormalIntRange( belongings.armor.min(), belongings.armor.max());
			if (armDr > 0) dr += armDr;
		}
		if (belongings.weapon != null)  {
			int wepDr = Random.NormalIntRange( 0 , belongings.weapon.defenseFactor( this ) );
			if (wepDr > 0) dr += wepDr;
		}

		return dr;
	}


	@Override
	public float speed() {

		float speed = super.speed();

		speed *= RingOfHaste.speedMultiplier(this);

		if (belongings.armor != null) {
			speed = belongings.armor.speedFactor(this, speed);
		}

		return speed;

	}

	public boolean canCriticalAttack( Char enemy, int damage, EffectType type){
		if(belongings.weapon != null)
			return belongings.weapon.canCriticalAttack( this, enemy, damage ,type);
		else return super.canCriticalAttack(enemy, damage, type);
	}

	@Override
	public int attackProc(Char enemy, int damage, EffectType type) {
		damage = super.attackProc(enemy, damage, type);
		if (belongings.weapon != null && belongings.weapon != null) {
			damage = belongings.weapon.proc( this, enemy, damage ,type );
		}

		destroy();
		sprite.die();

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage, EffectType type ) {
		if (belongings != null && belongings.armor != null) {
			return belongings.armor.proc( enemy, this, damage ,type, Armor.EVENT_SUFFER_ATTACK);
		} else {
			return super.defenseProc(enemy, damage, type);
		}
	}

	@Override
	public void onMissed(Char enemy) {
		if (belongings != null && belongings.weapon != null)
			belongings.weapon.onMissed( this, enemy);
		return;
	}

	@Override
	public float attackDelay() {
		return belongings.weapon != null ? belongings.weapon.speedFactor( this ) : super.attackDelay();
	}

	@Override
	protected boolean canAttack(Char enemy) {
		if (belongings.weapon != null) {
			return Dungeon.level.distance(pos, enemy.pos) <= belongings.weapon.reachFactor(this);
		}
		else{
			return super.canAttack(enemy);
		}
	}

	@Override
	public int damage( int dmg, Object src ,EffectType type) {

		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this, type);
		}

		dmg = (int)Math.ceil(dmg * RingOfTenacity.damageMultiplier( this ));

		if (belongings.armor != null) {
			dmg = belongings.armor.proc( src, this, dmg, type, Armor.EVENT_BEFORE_DAMAGE );
		}

		int damage = super.damage( dmg, src ,type );

		if (belongings.armor != null && isAlive()) {
			belongings.armor.proc( src, this, dmg, type, Armor.EVENT_AFTER_DAMAGE );
		}
		return damage;
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return attack;
	}
	
	@Override
	public int damageRoll() {
		return damage;
	}
	
	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage,type );

		destroy();
		sprite.die();
		
		return damage;
	}*/
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		((MirrorSprite)s).updateArmor( tier );
		return s;
	}

	@Override
	public boolean interact() {
		
		int curPos = pos;
		
		moveSprite( pos, Dungeon.hero.pos );
		move( Dungeon.hero.pos );
		
		Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
		Dungeon.hero.move( curPos );
		
		Dungeon.hero.spend( 1 / Dungeon.hero.speed() );
		Dungeon.hero.busy();

		return true;
	}
	
	/*{
		immunities.add( ToxicGas.class );
		immunities.add( CorrosiveGas.class );
		immunities.add( Burning.class );
	}*/
}