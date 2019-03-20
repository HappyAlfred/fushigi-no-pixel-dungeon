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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corruption;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Healing;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfTransfusion;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sungrass;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.FallenAngelSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FallenAngel extends Mob {
	private static final float TIME_TO_ZAP	= 1f;

	{
		spriteClass = FallenAngelSprite.class;
		
		HP = HT = 80;
		defenseSkill = 20;

		flying = true;

		EXP = 12;

		WANDERING = new Wandering();
		HUNTING = new Hunting();

		properties.add(Property.ANGEL);

		loot = null;    //see createloot.
		lootChance = 0.05f;
	}

	public Char cureTarget;
	public boolean cureInFOV = false;

    {
        immunities.add(new EffectType(Corruption.class));
    }

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 10, 15 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 30;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}

	@Override
	protected Item createLoot() {
		Item loot;
		if (Random.Int(5) > 3) {
			loot = new WandOfTransfusion().random();
			loot.cursed = false;
			loot.level(0);
			return loot;
		}
		else{
			loot = new Sungrass.Seed();
			return loot;
		}
	}

    @Override
	protected boolean doAttack( Char enemy ) {

		boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
		if(visible)
			sprite.zap( enemy.pos );
		else
			zap(enemy);
		spend( TIME_TO_ZAP );

		return !visible;
	}


	public void zap(Char enemy) {

		if (hit( this, enemy, true )) {

			int dmg = damageRoll();
			enemy.damage( dmg, this ,new EffectType(EffectType.MAGICAL_BOLT,EffectType.LIGHT));

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "light_kill") );
			}
		} else {
			enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
		}
	}

	@Override
	public boolean act() {

		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView( this, fieldOfView );
		cureTarget = chooseCureTarget();
		cureInFOV = cureTarget != null && cureTarget.isAlive() && fieldOfView[cureTarget.pos] && cureTarget.invisible <= 0;
		return super.act();
	}

	protected Char chooseCureTarget() {

		//find a new cure target if..
		boolean newTarget = false;
		//we have no enemy, or the current one is dead
		if ( cureTarget == null || !cureTarget.isAlive() || state == WANDERING || cureTarget.HP >= cureTarget.HT)
			newTarget = true;
			//We are an ally, and current enemy is another ally.
		else if ((alignment == Alignment.ALLY && cureTarget.alignment == Alignment.ENEMY) || (alignment == Alignment.ENEMY && cureTarget.alignment == Alignment.ALLY))
			newTarget = true;
		else if ((alignment == Alignment.ENEMY && cureTarget == Dungeon.hero) || (cureTarget instanceof Mob && ((Mob)cureTarget).enemy == this))
			newTarget = true;
		else if (buff( Charm.class ) != null && !isCharmedBy(cureTarget))
			newTarget = true;

		if ( newTarget ) {

			HashSet<Char> cureTargets = new HashSet<>();

			//if the mob is amoked...
			if ( buff(Charm.class) != null) {
				Char source = null;
				for (Buff b : buffs()) {
					if (b instanceof Charm) {
						source=(Char) Actor.findById(  buff( Charm.class ).object );
					}
				}
				if(source != null && source != this && fieldOfView[source.pos] && source.HP < source.HT)
					cureTargets.add(source);
				//if the mob is an ally...
			} else if ( alignment == Alignment.ALLY ) {
				//look for hostile mobs that are not passive to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ALLY
							&& mob != this
							&& fieldOfView[mob.pos]
							&& mob.HP < mob.HT
							&& mob.enemy != this)
						cureTargets.add(mob);

				//and look for the hero
				if (fieldOfView[Dungeon.hero.pos] && Dungeon.hero.HP < Dungeon.hero.HT) {
					cureTargets.add(Dungeon.hero);
				}

				//if the mob is an enemy...
			} else if (alignment == Alignment.ENEMY) {
				//look for ally mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && mob != this && fieldOfView[mob.pos] && mob.HP < mob.HT && mob.enemy != this)
						cureTargets.add(mob);

			}

			//neutral character in particular do not choose enemies.
			if (cureTargets.isEmpty()){
				return null;
			} else {
				//go after the closest potential enemy, preferring the hero if two are equidistant
				Char closest = null;
				for (Char curr : cureTargets){
					if (closest == null
							|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
							|| Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero){
						closest = curr;
					}
				}
				return closest;
			}

		} else
			return cureTarget;
	}

	public void onZapComplete() {
		zap( enemy );
		next();
	}

	public void onCureComplete() {
		cureBeam( cureTarget );
		next();
	}

	public void cure(Char cureTarget){
		spend( TICK );
		if(cureTarget == null) return;
		boolean visible = Dungeon.level.heroFOV[pos];
		if (visible) {
			((FallenAngelSprite)sprite).cure( cureTarget.pos );
		} else {
			cureBeam( cureTarget );
		}

	}

	public void cureBeam(Char cureTarget){
		int reg = Math.min( 30, cureTarget.HT - cureTarget.HP );
		if(reg > 0 && cureTarget != null && cureTarget.isAlive()){
			cureTarget.HP += reg;
			cureTarget.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
			cureTarget.sprite.showStatus(CharSprite.POSITIVE, Messages.get(Healing.class, "value", reg));
		}

	}

	protected class Wandering extends Mob.Wandering {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if(cureInFOV){
				notice();
				alerted = true;
				state = HUNTING;
				target = cureTarget.pos;
				return true;
			}
			else return super.act(enemyInFOV ,justAlerted);
		}
	}

	protected boolean canCure( Char cureTarget ) {

		return Dungeon.level.adjacent( pos, cureTarget.pos ) && cureTarget.HP < cureTarget.HT;
	}

	private class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if(cureInFOV && buff(Amok.class) == null && canCure( cureTarget )){
				cure( cureTarget );

				return true;
			} else {

				if (cureInFOV) {
					target = cureTarget.pos;
				}else if (cureTarget == null) {
					return super.act(enemyInFOV , justAlerted);
				}

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				}
				else if(enemyInFOV || enemy != null)
					return super.act(enemyInFOV , justAlerted);
				else {
					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination();
					}
					return true;
				}
			}
		}
	}
}
