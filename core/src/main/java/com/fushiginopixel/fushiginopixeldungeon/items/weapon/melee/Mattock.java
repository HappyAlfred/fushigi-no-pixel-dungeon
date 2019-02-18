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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Bat;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.LeafParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.DarkGold;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.Pickaxe;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfFuror;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.HighGrass;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Mattock extends MeleeWeapon {

	public static final String AC_DIG	= "DIG";
	
	{
		image = ItemSpriteSheet.MATTOCK;

		tier = 3;
		
		defaultAction = AC_DIG;

	}

	/*@Override
	public int min(int lvl) {
		return 2;   //tier 2
	}*/

	@Override
	public int max(int lvl) {
		return  15 +
				lvl*UPGRADE_ATTACK;
	}

	/*@Override
	public int STRReq(int lvl) {
		lvl = Math.max(0, lvl);
		//strength req decreases at +1,+3,+6,+10,etc.
		return (9 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;	//tier 3
	}*/

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_DIG );
		return actions;
	}

	public void doDig( Hero hero ) {
		GameScene.selectCell( dig );
	}
	
	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );
		
		if (action.equals(AC_DIG)) {

			if (!isEquipped(hero)) {
				GLog.i(Messages.get(Weapon.class, "need_to_equip"));
				return;
			}else{
				doDig(hero);
			}
			/*
			if (Dungeon.depth < 21 || Dungeon.depth > 30) {
				GLog.w( Messages.get(this, "no_vein") );
				return;
			}
			
			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				
				final int pos = hero.pos + PathFinder.NEIGHBOURS8[i];
				if (Dungeon.level.map[pos] == Terrain.WALL_DECO) {
				
					hero.spend( TIME_TO_MINE );
					hero.busy();
					
					hero.sprite.attack( pos, new Callback() {
						
						@Override
						public void call() {

							CellEmitter.center( pos ).burst( Speck.factory( Speck.STAR ), 7 );
							Sample.INSTANCE.play( Assets.SND_EVOKE );
							
							Level.set( pos, Terrain.WALL );
							GameScene.updateMap( pos );
							
							DarkGold gold = new DarkGold();
							if (gold.doPickUp( Dungeon.hero )) {
								GLog.i( Messages.get(Dungeon.hero, "you_now_have", gold.name()) );
							} else {
								Dungeon.level.drop( gold, hero.pos ).sprite.drop();
							}
							
							hero.onOperateComplete();
						}
					} );
					
					return;
				}
			}
			
			GLog.w( Messages.get(this, "no_vein") );
			*/
		}
	}

	public void dig( final Hero user, final int dst ) {
		final Ballistica chainDig = new Ballistica(user.pos, dst, Ballistica.WONT_STOP );

		float digDelay = digDelay(user, dst);
		user.spend( digDelay );
		user.busy();

		user.sprite.attack( dst, new Callback() {
			@Override
			public void call() {

				boolean terrainAffected = false;
				for(int cell :chainDig.subPath(1 ,reachFactor(user))) {

					if(level() < 0){
						break;
					}
					if (Dungeon.level.map[cell] == Terrain.TRAP) {
						CellEmitter.get( cell ).burst( SmokeParticle.FACTORY, 4 );
						Sample.INSTANCE.play( Assets.SND_BLAST );

						Dungeon.level.destroy( cell );
						GameScene.updateMap(cell);
						terrainAffected = true;
						break;
					}else if(Dungeon.level.map[cell] == Terrain.GRASS){
						CellEmitter.get( cell ).burst( LeafParticle.LEVEL_SPECIFIC, 4 );

						Level.set(cell, Terrain.EMPTY);
						GameScene.updateMap(cell);
						break;
					}else if(Dungeon.level.map[cell] == Terrain.HIGH_GRASS){
						HighGrass.trample(Dungeon.level, cell, user);

						Level.set(cell, Terrain.EMPTY);
						GameScene.updateMap(cell);
						break;
					}else if (Dungeon.depth >= 21 && Dungeon.depth <= 30 && Dungeon.level.map[cell] == Terrain.WALL_DECO) {
						CellEmitter.center(cell).burst(Speck.factory(Speck.STAR), 7);
						Sample.INSTANCE.play(Assets.SND_EVOKE);

						Level.set(cell, Terrain.WALL);
						GameScene.updateMap(cell);

						DarkGold gold = new DarkGold();
						if (gold.doPickUp(Dungeon.hero)) {
							GLog.i(Messages.get(Dungeon.hero, "you_now_have", gold.name()));
						} else {
							Dungeon.level.drop(gold, user.pos).sprite.drop();
						}
						break;
					}
					else if(Dungeon.level.destructable[cell] && !Dungeon.canNotBlowUpLevel()){
						CellEmitter.get( cell ).burst( SmokeParticle.FACTORY, 4 );
						Sample.INSTANCE.play( Assets.SND_BLAST );

						Dungeon.level.destroy( cell );
						GameScene.updateMap(cell);
						terrainAffected = true;

					}else break;
				}
				if(terrainAffected){
					Dungeon.level.terrainAdjustComplete();
					Dungeon.observe();

					if(Random.Int(10 + level()) == 0){
						degrade();
                        updateQuickslot();
					}
				}

				if(level() < 0){
					broken(user);
				}

				user.onOperateComplete();
			}
		});

	}

	public void broken(Hero user) {
		GLog.w( Messages.get(Mattock.class, "broken") );
		if(!isUnique()){
			doUnequip( user, false );
			//detach(user.belongings.backpack);
		}
	}

	public float digDelay( Char user, int dst ){
		int encumbrance = 0;
		/*if (user instanceof Hero) {
			encumbrance = STRReq() - ((Hero)user).STR();
		}*/

		float DLY = Pickaxe.TIME_TO_DIG;
		DLY = RingOfFuror.modifyAttackDelay(DLY, user);

		return (float) (DLY * Math.pow( 1.2, encumbrance));
	}

	protected static CellSelector.Listener dig = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null && Dungeon.level.adjacent(curUser.pos, target) && (Dungeon.level.destructable[target] || Dungeon.level.map[target] == Terrain.TRAP)) {
				if(curItem.level() >= 0) {
					((Mattock) curItem).dig(curUser, target);
				}else{
					GLog.w( Messages.get(Mattock.class, "broken") );
				}
			}else{
				GLog.w( Messages.get(Mattock.class, "bad_target") );
			}

		}
		@Override
		public String prompt() {
			return Messages.get(Mattock.class, "dig_select");
		}
	};

}
