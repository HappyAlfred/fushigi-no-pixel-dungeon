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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Ooze;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Sleep;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfSelfDestruct;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BurningFistSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.LarvaSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RottingFistSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.YogSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Yog extends Mob {
	
	{
		spriteClass = YogSprite.class;
		
		HP = HT = 2000;
		
		EXP = 100;
		
		state = PASSIVE;

		properties.add(Property.BOSS);
		properties.add(Property.IMMOVABLE);
		properties.add(Property.DEMONIC);
	}
	
	public Yog() {
		super();
	}
	
	public void spawnFists() {
		RottingFist fist1 = new RottingFist();
		BurningFist fist2 = new BurningFist();
		
		do {
			fist1.pos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			fist2.pos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Dungeon.level.passable[fist1.pos] || !Dungeon.level.passable[fist2.pos] || fist1.pos == fist2.pos);
		
		GameScene.add( fist1 );
		GameScene.add( fist2 );

		notice();
	}

	@Override
	protected boolean act() {
		//heals 1 health per turn
		HP = Math.min( HT, HP+1 );

		int fist = 0;
		for (Mob mob : Dungeon.level.mobs)
			if (mob instanceof RottingFist || mob instanceof BurningFist)
				fist++;

		if(fist == 0){
			ArrayList<Integer> spawnPoints = new ArrayList<>();

			int larvaCount = 0;
			for (Mob mob : Dungeon.level.mobs)
				if (mob instanceof Larva)
					larvaCount++;

			if(larvaCount <10) {

				for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
					int p = pos + PathFinder.NEIGHBOURS8[i];
					if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
						spawnPoints.add(p);
					}
				}

				if (spawnPoints.size() > 0) {
					Larva larva = new Larva();
					larva.pos = Random.element(spawnPoints);

					GameScene.add(larva);
					Actor.addDelayed(new Pushing(larva, pos, larva.pos), -1);
				}
			}
		}

		return super.act();
	}

	@Override
	public int damage( int dmg, Object src, EffectType type ) {

		HashSet<Mob> fists = new HashSet<>();

		for (Mob mob : Dungeon.level.mobs)
			if (mob instanceof RottingFist || mob instanceof BurningFist)
				fists.add( mob );

		dmg >>= fists.size();

		int damage = super.damage(dmg, src, type);

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(damage*0.5f);

		return damage;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage, EffectType type  ) {

		ArrayList<Integer> spawnPoints = new ArrayList<>();
		
		for (int i=0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
				spawnPoints.add( p );
			}
		}
		
		if (spawnPoints.size() > 0) {
			Larva larva = new Larva();
			larva.pos = Random.element( spawnPoints );
			
			GameScene.add( larva );
			Actor.addDelayed( new Pushing( larva, pos, larva.pos ), -1 );
		}

		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof BurningFist || mob instanceof RottingFist || mob instanceof Larva) {
				mob.aggro( enemy );
			}
		}

		return super.defenseProc(enemy, damage,type);
	}
	
	@Override
	public void beckon( int cell ) {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die( Object cause, EffectType type ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof BurningFist || mob instanceof RottingFist) {
				mob.die( cause );
			}
		}
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();
		super.die( cause, type );
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell( Messages.get(this, "notice") );
	}
	
	{
		
		immunities.add( new EffectType(Grim.class) );
		immunities.add( new EffectType(Terror.class) );
		immunities.add( new EffectType(Amok.class) );
		immunities.add( new EffectType(Charm.class) );
		immunities.add( new EffectType(Sleep.class) );
		//immunities.add( Burning.class );
		immunities.add( new EffectType(ToxicGas.class) );
		immunities.add( new EffectType(ScrollOfSelfDestruct.class) );
		immunities.add( new EffectType(Vertigo.class) );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}

	public static class RottingFist extends Mob {
	
		private static final int REGENERATION	= 10;
		
		{
			spriteClass = RottingFistSprite.class;
			
			HP = HT = 900;
			//defenseSkill = 50;
			
			EXP = 0;
			
			state = WANDERING;

			properties.add(Property.BOSS);
			properties.add(Property.DEMONIC);
			properties.add(Property.ACIDIC);
		}

		/*
		@Override
		public int attackSkill( Char target ) {
			return 100;
		}
		*/
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 60, 100 );
		}
		
		@Override
		public int drRoll() {
			return Random.NormalIntRange(4, 30);
		}
		
		@Override
		public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
			damage = super.attackProc( weapon, enemy, damage,type );
			
			if (Random.Int( 3 ) == 0) {
				Buff.affect( enemy, Ooze.class,new EffectType(type.attachType,EffectType.CORRROSION) );
				enemy.sprite.burst( 0xFF000000, 5 );
			}
			
			return damage;
		}
		
		@Override
		public boolean act() {
			
			if (Dungeon.level.water[pos] && HP < HT) {
				sprite.emitter().burst( ShadowParticle.UP, 2 );
				HP += REGENERATION;
			}
			
			return super.act();
		}

		@Override
		public int damage(int dmg, Object src, EffectType type) {
			int damage = super.damage(dmg, src, type);
			LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
			if (lock != null) lock.addTime(damage*0.5f);
			return damage;
		}
		
		{
			immunities.add( new EffectType(Paralysis.class) );
			immunities.add( new EffectType(Amok.class) );
			immunities.add( new EffectType(Sleep.class) );
			immunities.add( new EffectType(Terror.class) );
			immunities.add( new EffectType(Poison.class) );
			immunities.add( new EffectType(Vertigo.class) );
		}
	}
	
	public static class BurningFist extends Mob {
		
		{
			spriteClass = BurningFistSprite.class;
			
			HP = HT = 600;
			//defenseSkill = 50;
			
			EXP = 0;
			
			state = WANDERING;

			properties.add(Property.BOSS);
			properties.add(Property.DEMONIC);
			properties.add(Property.FIERY);
		}

		/*
		@Override
		public int attackSkill( Char target ) {
			return 100;
		}
		*/
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 52, 70 );
		}
		
		@Override
		public int drRoll() {
			return Random.NormalIntRange(4, 30);
		}
		
		@Override
		protected boolean canAttack( Char enemy ) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		}
		
		@Override
		public boolean attack( Char enemy ) {
			
			if (!Dungeon.level.adjacent( pos, enemy.pos )) {
				spend( totalAttackDelay() );
				
				if (hit( this, null, enemy, true )) {
					
					int dmg =  damageRoll();
					enemy.damage( dmg, this,new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE) );
					
					enemy.sprite.bloodBurstA( sprite.center(), dmg );
					enemy.sprite.flash();
					
					if (!enemy.isAlive() && enemy == Dungeon.hero) {
						Dungeon.fail( getClass() );
						GLog.n( Messages.get(Char.class, "kill", name) );
					}
					return true;
					
				} else {
					
					enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
					return false;
				}
			} else {
				return super.attack( enemy );
			}
		}
		
		@Override
		public boolean act() {
			
			for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
				GameScene.add( Blob.seed( pos + PathFinder.NEIGHBOURS9[i], 2, Fire.class ) );
			}
			
			return super.act();
		}

		@Override
		public int damage(int dmg, Object src,EffectType type) {
			int damage = super.damage(dmg, src, type);
			LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
			if (lock != null) lock.addTime(damage*0.5f);
			return damage;
		}
		
		{
			resistances.add( new EffectType(ToxicGas.class) );
		}
		
		{
			immunities.add( new EffectType(Amok.class) );
			immunities.add( new EffectType(Sleep.class) );
			immunities.add( new EffectType(Terror.class) );
			immunities.add( new EffectType(Vertigo.class) );
		}
	}
	
	public static class Larva extends Mob {
		
		{
			spriteClass = LarvaSprite.class;
			
			HP = HT = 50;
			//defenseSkill = 40;
			
			EXP = 0;
			
			state = HUNTING;

			properties.add(Property.DEMONIC);
		}

		/*
		@Override
		public int attackSkill( Char target ) {
			return 60;
		}
		*/
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 40, 60 );
		}
		
		@Override
		public int drRoll() {
			return Random.NormalIntRange(3, 20);
		}

	}
}
