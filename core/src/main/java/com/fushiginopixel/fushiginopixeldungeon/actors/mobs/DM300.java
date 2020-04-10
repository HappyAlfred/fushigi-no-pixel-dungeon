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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Lightning;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ElmoParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SparkParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CapeOfThorns;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.LloydsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DM300Sprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DM300 extends Mob {
	
	{
		spriteClass = DM300Sprite.class;
		
		HP = HT = 600;
		EXP = 50;
		//defenseSkill = 28;
		
		loot = new CapeOfThorns();
		lootChance = 0.333f;

		properties.add(Property.BOSS);
		properties.add(Property.INORGANIC);
		properties.add(Property.MACHANIC);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 45 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 35;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(1, 15);
	}
	
	@Override
	public boolean act() {
		
		GameScene.add( Blob.seed( pos, 30, ToxicGas.class ) );
		
		return super.act();
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( weapon, enemy, damage, type );
		int lDamage = Random.NormalIntRange(1, 30);
		/*
		if (Dungeon.level.map[enemy.pos] == Terrain.WATER) {
			lDamage = Random.NormalIntRange(1, 90);
		}else{
			lDamage = Random.NormalIntRange(1, 30);
		}
		*/
		enemy.damage(lDamage, this, new EffectType(type.attachType, EffectType.ELETRIC));

		ArrayList<Lightning.Arc> arcs = new ArrayList<>();
		CharSprite s = enemy.sprite;
		arcs.add(new Lightning.Arc(new PointF(s.x, s.y + s.height / 2), new PointF(s.x + s.width, s.y + s.height / 2)));
		arcs.add(new Lightning.Arc(new PointF(s.x + s.width / 2, s.y), new PointF(s.x + s.width / 2, s.y + s.height)));
		s.parent.add(new Lightning(arcs, null));

		enemy.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
		enemy.sprite.flash();
		s.parent.add(new Lightning(arcs, null));

		if (enemy == Dungeon.hero) {

			Camera.main.shake( 2, 0.3f );

			if (!enemy.isAlive()) {
				Dungeon.fail( getClass() );
			}
		}

		return damage;
	}
	
	@Override
	public void move( int step ) {
		super.move( step );
		
		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {
			
			HP += Random.Int( 1, HT - HP );
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.level.heroFOV[step] && Dungeon.hero.isAlive()) {
				GLog.n( Messages.get(this, "repair") );
			}
		}
		
		int[] cells = {
			step-1, step+1, step-Dungeon.level.width(), step+Dungeon.level.width(),
			step-1-Dungeon.level.width(),
			step-1+Dungeon.level.width(),
			step+1-Dungeon.level.width(),
			step+1+Dungeon.level.width()
		};
		int cell = cells[Random.Int( cells.length )];
		
		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
			
			if (Dungeon.level.water[cell]) {
				GameScene.ripple( cell );
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Level.set( cell, Terrain.EMPTY_DECO );
				GameScene.updateMap( cell );
			}
		}

		Char ch = Actor.findChar( cell );
		if (ch != null && ch != this) {
			Buff.prolong( ch, Paralysis.class, 2,new EffectType(EffectType.MISSILE,0) );
		}
	}

	@Override
	public int damage(int dmg, Object src, EffectType type) {
		int damage = super.damage(dmg, src, type);
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !isImmune(src.getClass(),type)) lock.addTime(damage*1.5f);
		return damage;
	}

	@Override
	public void die( Object cause, EffectType type ) {
		
		super.die( cause, type );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth  ), pos ).sprite.drop();
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell( Messages.get(this, "notice") );
	}
	
	/*{
		immunities.add( ToxicGas.class );
		immunities.add( Terror.class );
	}*/

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
	}
}
