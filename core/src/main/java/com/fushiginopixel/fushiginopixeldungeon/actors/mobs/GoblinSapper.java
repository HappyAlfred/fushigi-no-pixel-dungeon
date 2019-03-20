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
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Imp;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLiquidFlame;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ElementalSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GoblinSapperSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GoblinSapper extends Mob {

	{
		spriteClass = GoblinSapperSprite.class;
		
		HP = HT = 65;
		defenseSkill = 17;
		
		EXP = 10;
		
		loot = new Bomb();
		lootChance = 0.05f;

		HUNTING = new Hunting();
	}

	protected final int DEFAULT_COUNTDOWN = 3;
	protected int countDown = -1;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 25 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 26;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

	@Override
	public void damage( int dmg, Object src,EffectType type ) {
		super.damage( dmg, src ,type );

		if (isAlive() && type.isExistAttachType(EffectType.BURST)) {
			die( src ,type );
		}
	}

	protected boolean expAct(){

		if(buff(Burning.class) != null){
			suicide();
		}

		if(countDown != -1){
			countDown --;
			if(countDown <= 0){
				die(this ,new EffectType(EffectType.BURST,0));
				return true;
			}
			spend( TICK );
			return true;
		}

		return false;

	}

	@Override
	public boolean act() {

		if(expAct()){
			return true;
		}

		return super.act();
	}

	@Override
	public void die( Object cause, EffectType type ) {

		if(cause instanceof Burning || type.isExistAttachType(EffectType.BURST)){
			selfExplode();

			destroy(this, type);
			((GoblinSapperSprite)sprite).explodeFlag = true;
			sprite.die();
		}else {
			super.die(cause, type);
		}
	}

	public void selfExplode(){

		Sample.INSTANCE.play( Assets.SND_BLAST );

		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.center( pos ).burst( BlastParticle.FACTORY, 20 );
		}
		boolean terrainAffected = false;


		for (int n : PathFinder.NEIGHBOURS9) {
			int c = pos + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 3 );
				}
				terrainAffected = Bombs.blowUp(c , 2);

				Char ch = Actor.findChar( c );
				if (ch != null && ch != this) {
					if(ch instanceof Hero && ch.HP > 1){
						ch.damage( ch.HP - 1, this ,new EffectType(EffectType.BURST,0));
					}
					else if (ch.HP > 0) {
						ch.damage( ch.HP, this,new EffectType(EffectType.BURST,0) );
					}
					if (ch == Dungeon.hero && !ch.isAlive()) {
						Dungeon.fail(getClass());
					}
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}
	}

	@Override
	public void destroying(Object src, EffectType type){
		if(src == null || !EffectType.isExistType(new EffectType(type.attachType, type.effectType, src.getClass()), new EffectType(EffectType.BURST, 0, getClass()))){
			super.destroying(src, type);
		}else{
			Dungeon.level.mobs.remove( this );
		}
	}

	protected void suicide(){
		suicide(DEFAULT_COUNTDOWN);
	}

	protected void suicide(int countDown){
		if(this.countDown == -1) {
			sprite.showStatus(CharSprite.NEGATIVE, Messages.get(this, "!!!"));
			GLog.n( Messages.get(this, "suicide",name) );
			this.countDown = countDown;
		}
	}
	private static final String COUNTDOWN = "countDown";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(COUNTDOWN, countDown);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		countDown = bundle.getInt( COUNTDOWN );
	}

	protected class Hunting extends Mob.Hunting{
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if ( enemyInFOV
					&& !isCharmedBy( enemy )
					&& canAttack( enemy )) {
				if(Random.Int(4) == 0 && countDown == -1){

					suicide();
					spend(TICK);
					return true;

				}
				else{
					return super.act(enemyInFOV, justAlerted);
				}
			}
			else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}
	
}
