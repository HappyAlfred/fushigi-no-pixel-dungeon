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

package com.fushiginopixel.fushiginopixeldungeon.items.artifacts;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.InterlevelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
/*
public class RatWhistle extends Artifact {

	public static final float TIME_TO_USE = 1;

	public static final String AC_PLAY       = "PLAY";
	public static final String AC_ORDER	= "ORDER";
	
	{
		image = ItemSpriteSheet.ARTIFACT_BEACON;

		levelCap = 3;

		charge = 100;
		chargeCap = 100;

		defaultAction = AC_PLAY;
		usesTargeting = true;
	}
	
	private static final String DEPTH	= "depth";
	private static final String POS		= "pos";
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.remove(AC_EQUIP);
		actions.add( AC_PLAY );
		actions.add( AC_ORDER );
		return actions;
	}
	
	@Override
	public void execute( Char hero, String action ) {

		super.execute( hero, action );

		if (action == AC_SET || action == AC_RETURN) {
			
			if (Dungeon.bossLevel()) {
				hero.spend( RatWhistle.TIME_TO_USE );
				GLog.w( Messages.get(this, "preventing") );
				return;
			}
			
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				Char ch = Actor.findChar(hero.pos + PathFinder.NEIGHBOURS8[i]);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
					GLog.w( Messages.get(this, "creatures") );
					return;
				}
			}
		}

		if (action == AC_ZAP ){

			curUser = hero;
			int chargesToUse = Dungeon.depth > 40 ? 2 : 1;

			if (!isEquipped( hero )) {
				GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				QuickSlotButton.cancel();

			} else if (charge < chargesToUse) {
				GLog.i( Messages.get(this, "no_charge") );
				QuickSlotButton.cancel();

			} else {
				GameScene.selectCell(zapper);
			}

		} else if (action == AC_SET) {
			
			returnDepth = Dungeon.depth;
			returnPos = hero.pos;
			
			hero.spend( RatWhistle.TIME_TO_USE );
			hero.busy();
			
			hero.sprite.operate( hero.pos );
			Sample.INSTANCE.play( Assets.SND_BEACON );
			
			GLog.i( Messages.get(this, "return") );
			
		} else if (action == AC_RETURN && hero instanceof Hero) {

			if ((Statistics.amuletObtained || returnDepth == Dungeon.depth ) && !Statistics.thief) {
				if (returnDepth == Dungeon.depth) {
					ScrollOfTeleportation.appear(hero, returnPos);
					Dungeon.level.press(returnPos, hero);
					Dungeon.observe();
					GameScene.updateFog();
				} else {

					Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
					if (buff != null) buff.detach();

					InterlevelScene.mode = InterlevelScene.Mode.RETURN;
					InterlevelScene.returnDepth = returnDepth;
					InterlevelScene.returnPos = returnPos;
					Game.switchScene(InterlevelScene.class);
				}
			}else{
				hero.spend( RatWhistle.TIME_TO_USE );
				GLog.w( Messages.get(this, "preventing") );
			}
			
		}
	}

	protected CellSelector.Listener zapper = new  CellSelector.Listener() {

		@Override
		public void onSelect(Integer target) {

			if (target == null) return;

			Invisibility.dispel();
			charge -= Dungeon.depth > 40 ? 2 : 1;
			updateQuickslot();

			if (Actor.findChar(target) == curUser){
				ScrollOfTeleportation.teleportHero(curUser);
				curUser.spendAndNext(1f);
			} else {
				final Ballistica bolt = new Ballistica( curUser.pos, target, Ballistica.MAGIC_BOLT );
				final Char ch = Actor.findChar(bolt.collisionPos);

				if (ch == curUser){
					ScrollOfTeleportation.teleportHero(curUser);
					curUser.spendAndNext( 1f );
				} else {
					Sample.INSTANCE.play( Assets.SND_ZAP );
					curUser.sprite.zap(bolt.collisionPos);
					curUser.busy();

					MagicMissile.boltFromChar(curUser.sprite.parent,
							MagicMissile.BEACON,
							curUser.sprite,
							bolt.collisionPos,
							new Callback() {
								@Override
								public void call() {
									if (ch != null) {

										int count = 10;
										int pos;
										do {
											pos = Dungeon.level.randomRespawnCell();
											if (count-- <= 0) {
												break;
											}
										} while (pos == -1);

										if (pos == -1 || Dungeon.bossLevel()) {

											GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );

										} else if (ch.properties().contains(Char.Property.IMMOVABLE)) {

											GLog.w( Messages.get(RatWhistle.class, "tele_fail") );

										} else  {

											ch.pos = pos;
											if (ch instanceof Mob && ((Mob) ch).state == ((Mob) ch).HUNTING){
												((Mob) ch).state = ((Mob) ch).WANDERING;
											}
											ch.sprite.place(ch.pos);
											ch.sprite.visible = Dungeon.level.heroFOV[pos];

										}
									}
									curUser.spendAndNext(1f);
								}
							});

				}


			}

		}

		@Override
		public String prompt() {
			return Messages.get(RatWhistle.class, "prompt");
		}
	};

	@Override
	protected ArtifactBuff passiveBuff() {
		return new whistleRecharge();
	}

	@Override
	public Item upgrade() {
		if (level() == levelCap) return this;
		chargeCap ++;
		//GLog.p( Messages.get(this, "levelup") );
		return super.upgrade();
	}

	public void toUpgrade() {
		if (level() == levelCap) return;
		GLog.p( Messages.get(this, "levelup") );
		upgrade();
	}

	public class whistleRecharge extends ArtifactBuff{
		@Override
		public boolean act() {LockedFloor lock = target.buff(LockedFloor.class);
			if (charge < chargeCap && !cursed && (lock == null || lock.regenOn())) {
				partialCharge += 1/5f; //500 turns to a full charge
				if (partialCharge > 1){
					charge++;
					partialCharge--;
					if (charge == chargeCap){
						partialCharge = 0f;
						GLog.p( Messages.get(DriedRose.class, "charged") );
					}
				}
			} else if (cursed && Random.Int(100) == 0) {
				Buff.affect(target, Vertigo.class, 5f, new EffectType(0, EffectType.SPIRIT));

			}

			updateQuickslot();
			return true;
		}
	}

	private static class Free_Wandering implements Mob.AiState {

		Mob owner;
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if ( enemyInFOV ) {

				owner.enemySeen = true;

				noticeByMyself();
				alerted = true;
				state = HUNTING;
				target = enemy.pos;

			} else {

				enemySeen = false;

				int oldPos = pos;
				//always move towards the hero when wandering
				if (getCloser( target = Dungeon.hero.pos )) {
					//moves 2 tiles at a time when returning to the hero from a distance
					if (!Dungeon.level.adjacent(Dungeon.hero.pos, pos)){
						getCloser( target = Dungeon.hero.pos );
					}
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					spend( TICK );
				}

			}
			return true;
		}

	}
}
*/
