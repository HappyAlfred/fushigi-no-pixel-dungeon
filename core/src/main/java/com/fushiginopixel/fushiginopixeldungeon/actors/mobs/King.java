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
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.effects.Flare;
import com.fushiginopixel.fushiginopixeldungeon.effects.Lightning;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.ArmorKit;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.LloydsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfDisintegration;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.KingSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.UndeadSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class King extends Mob {
	
	private static final int MAX_ARMY_SIZE	= 5;
	
	{
		spriteClass = KingSprite.class;
		
		HP = HT = 1500;
		EXP = 80;
		//defenseSkill = 55;
		
		Undead.count = 0;

		properties.add(Property.BOSS);
		properties.add(Property.UNDEAD);
	}
	
	private boolean nextPedestal = true;
	
	private static final String PEDESTAL = "pedestal";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( PEDESTAL, nextPedestal );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		nextPedestal = bundle.getBoolean( PEDESTAL );
		BossHealthBar.assignBoss(this);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 35, 70 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 60;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(5, 35);
	}
	
	@Override
	protected boolean getCloser( int target ) {
		return canTryToSummon() ?
			super.getCloser( ((CityBossLevel)Dungeon.level).pedestal( nextPedestal ) ) :
			super.getCloser( target );
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return canTryToSummon() ?
			pos == ((CityBossLevel)Dungeon.level).pedestal( nextPedestal ) :
			Dungeon.level.adjacent( pos, enemy.pos );
	}
	
	private boolean canTryToSummon() {
		if (Undead.count < maxArmySize()) {
			Char ch = Actor.findChar( ((CityBossLevel)Dungeon.level).pedestal( nextPedestal ) );
			return ch == this || ch == null;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean attack( Char enemy ) {
		if (canTryToSummon() && pos == ((CityBossLevel)Dungeon.level).pedestal( nextPedestal )) {
			summon();
			return true;
		} else {
			if (Actor.findChar( ((CityBossLevel)Dungeon.level).pedestal( nextPedestal ) ) == enemy) {
				nextPedestal = !nextPedestal;
			}
			return super.attack(enemy);
		}
	}

	@Override
	public int damage(int dmg, Object src, EffectType type) {
		int damage = super.damage( dmg, src ,type );
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) lock.addTime(damage);
		return damage;
	}
	
	@Override
	public void die( Object cause, EffectType type ) {

		GameScene.bossSlain();
		//Dungeon.level.drop( new ArmorKit(), pos ).sprite.drop();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();
		
		super.die( cause, type );
		
		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.toUpgrade();
		}
		
		yell( Messages.get(this, "defeated", Dungeon.hero.givenName()) );
	}

	@Override
	public void aggro(Char ch) {
		super.aggro(ch);
		for (Mob mob : Dungeon.level.mobs){
			if (mob instanceof Undead){
				mob.aggro(ch);
			}
		}
	}

	private int maxArmySize() {
		return 1 + MAX_ARMY_SIZE * (HT - HP) / HT;
	}
	
	private void summon() {

		nextPedestal = !nextPedestal;
		
		sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );
		
		boolean[] passable = Dungeon.level.passable.clone();
		for (Char c : Actor.chars()) {
			passable[c.pos] = false;
		}
		
		int undeadsToSummon = maxArmySize() - Undead.count;

		PathFinder.buildDistanceMap( pos, passable, undeadsToSummon );
		PathFinder.distance[pos] = Integer.MAX_VALUE;
		int dist = 1;
		
	undeadLabel:
		for (int i=0; i < undeadsToSummon; i++) {
			do {
				for (int j=0; j < Dungeon.level.length(); j++) {
					if (PathFinder.distance[j] == dist) {
						
						Undead undead = new Undead();
						undead.pos = j;
						GameScene.add( undead );
						
						ScrollOfTeleportation.appear( undead, j );
						new Flare( 3, 32 ).color( 0x000000, false ).show( undead.sprite, 2f ) ;
						
						PathFinder.distance[j] = Integer.MAX_VALUE;
						
						continue undeadLabel;
					}
				}
				dist++;
			} while (dist < undeadsToSummon);
		}

		if(HP < HT / 2){

			int statueToSummon = 3 - 4 * HP/HT;
			ArrayList<Integer> statueSp = new ArrayList<Integer>();
			for (int i = 0; i < Dungeon.level.length(); i++){
				if(Dungeon.level.map[i] == Terrain.STATUE_SP){
					statueSp.add(i);
				}
			}
			ArrayList<Integer> statueToSm = new ArrayList<Integer>();
			do{
				if(statueSp.size() <= 0)break;
				int a = Random.Int(statueSp.size());
				statueToSm.add(statueSp.get(a));
				statueSp.remove(a);

			}while(statueToSm.size() < statueToSummon);
			for(int i = 0 ; i < statueToSummon ; i++) {
				for (int j : statueToSm){
					if(Dungeon.level.map[j] == Terrain.STATUE_SP){
						sprite.parent.add( new Lightning( pos, j, null ));
						Dungeon.level.destroy(j);
						Dungeon.level.terrainAdjustComplete();
						GameScene.updateMap(j);

						ShopGuardian statue = new ShopGuardian();
						statue.pos = j;
						statue.HP = statue.HT = statue.HT/2;
						GameScene.add( statue );

						ScrollOfTeleportation.appear( statue, j );
						new Flare( 3, 32 ).color( 0x000000, false ).show( statue.sprite, 2f ) ;
					}
				}
			}
		}
		
		yell( Messages.get(this, "arise") );
	}
	
	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell( Messages.get(this, "notice") );
	}
	
	{
		resistances.add( new EffectType(0,EffectType.DARK) );
	}
	
	{
		immunities.add( new EffectType(Paralysis.class) );
		immunities.add( new EffectType(Vertigo.class) );
		immunities.add( new EffectType(Blindness.class) );
		immunities.add( new EffectType(Terror.class) );
	}
	
	public static class Undead extends Mob {
		
		public static int count = 0;
		
		{
			spriteClass = UndeadSprite.class;
			
			HP = HT = 50;
			//defenseSkill = 13;
			
			EXP = 0;
			
			state = WANDERING;

			properties.add(Property.UNDEAD);
			properties.add(Property.INORGANIC);
		}
		
		@Override
		protected void onAdd() {
			count++;
			super.onAdd();
		}
		
		@Override
		protected void onRemove() {
			count--;
			super.onRemove();
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 25, 50 );
		}

		/*
		@Override
		public int attackSkill( Char target ) {
			return 20;
		}
		*/
		
		@Override
		public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
			damage = super.attackProc( weapon, enemy, damage,type );
			if (Random.Int( MAX_ARMY_SIZE ) == 0) {
				Buff.prolong( enemy, Paralysis.class, 1,new EffectType(type.attachType,0) );
			}
			
			return damage;
		}
		
		@Override
		public int damage( int dmg, Object src ,EffectType type) {
			int damage = super.damage( dmg, src ,type);
			if (src instanceof ToxicGas) {
				((ToxicGas)src).clear( pos );
			}
			return damage;
		}
		
		@Override
		public void die( Object cause, EffectType type ) {
			super.die( cause, type );
			
			if (Dungeon.level.heroFOV[pos]) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			}
		}
		
		@Override
		public int drRoll() {
			return Random.NormalIntRange(0, 5);
		}

		/*{
			immunities.add( Grim.class );
			immunities.add( Paralysis.class );
		}*/
	}
}
