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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FetidRat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GnollTrickster;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GreatCrab;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LeatherArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.MailArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.PlateArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.SamuraiArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ScaleArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Katana;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerLevel;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GhostSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Ghost extends NPC {

	{
		spriteClass = GhostSprite.class;
		
		flying = true;
		
		state = WANDERING;

		properties.add(Property.UNDEAD);
		properties.add(Property.INORGANIC);
	}
	
	public Ghost() {
		super();

		Sample.INSTANCE.load( Assets.SND_GHOST );
	}

	@Override
	protected boolean act() {
		if (Quest.processed())
			target = Dungeon.hero.pos;
		return super.act();
	}

	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public float speed() {
		return Quest.processed() ? 2f : 0.5f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public int damage( int dmg, Object src ,EffectType type) {
		return 0;
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		Sample.INSTANCE.play( Assets.SND_GHOST );
		
		if (Quest.given) {
			if (Quest.weapon != null) {
				if (Quest.processed) {
					GameScene.show(new WndSadGhost(this, Quest.type));
				} else {
					switch (Quest.type) {
						case 1:
						default:
							GameScene.show(new WndQuest(this, Messages.get(this, "rat_2")));
							break;
						case 2:
							GameScene.show(new WndQuest(this, Messages.get(this, "gnoll_2")));
							break;
						case 3:
							GameScene.show(new WndQuest(this, Messages.get(this, "crab_2")));
							break;
					}

					int newPos = -1;
					for (int i = 0; i < 10; i++) {
						newPos = Dungeon.level.randomRespawnCell();
						if (newPos != -1) {
							break;
						}
					}
					if (newPos != -1) {

						CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.level.heroFOV[pos];
					}
				}
			}
		} else {
			Mob questBoss;
			String txt_quest;

			switch (Quest.type){
				case 1: default:
					questBoss = new FetidRat();
					txt_quest = Messages.get(this, "rat_1", Dungeon.hero.givenName()); break;
				case 2:
					questBoss = new GnollTrickster();
					txt_quest = Messages.get(this, "gnoll_1", Dungeon.hero.givenName()); break;
				case 3:
					questBoss = new GreatCrab();
					txt_quest = Messages.get(this, "crab_1", Dungeon.hero.givenName()); break;
			}

			questBoss.pos = Dungeon.level.randomRespawnCell();

			if (questBoss.pos != -1) {
				GameScene.add(questBoss);
				GameScene.show( new WndQuest( this, txt_quest ) );
				Quest.given = true;
				Notes.add( Notes.Landmark.GHOST );
			}

		}

		return false;
	}
	
	/*{
		immunities.add( Paralysis.class );
		immunities.add( Roots.class );
	}*/

	public static class Quest {
		
		private static boolean spawned;

		private static int type;

		private static boolean given;
		private static boolean processed;
		
		private static int depth;
		
		public static Weapon weapon;
		public static Armor armor;
		
		public static void reset() {
			spawned = false;
			
			weapon = null;
			armor = null;
		}
		
		private static final String NODE		= "sadGhost";
		
		private static final String SPAWNED		= "spawned";
		private static final String TYPE        = "type";
		private static final String GIVEN		= "given";
		private static final String PROCESSED	= "processed";
		private static final String DEPTH		= "depth";
		private static final String WEAPON		= "weapon";
		private static final String ARMOR		= "armor";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( DEPTH, depth );
				node.put( PROCESSED, processed);
				
				node.put( WEAPON, weapon );
				node.put( ARMOR, armor );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {
			
			Bundle node = bundle.getBundle( NODE );

			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {

				type = node.getInt(TYPE);
				given	= node.getBoolean( GIVEN );
				processed = node.getBoolean( PROCESSED );

				depth	= node.getInt( DEPTH );
				
				weapon	= (Weapon)node.get( WEAPON );
				armor	= (Armor)node.get( ARMOR );
			} else {
				reset();
			}
		}
		
		public static void spawn( SewerLevel level ) {
			if (!spawned && Dungeon.depth > 6 && Random.Int( 10 - Dungeon.depth ) == 0) {
				
				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add( ghost );
				
				spawned = true;
				//dungeon depth determines type of quest.
				//depth2=fetid rat, 3=gnoll trickster, 4=great crab
				type = Dungeon.depth-6;
				
				given = false;
				processed = false;
				depth = Dungeon.depth;

				//50%:tier2, 30%:tier3, 15%:tier4, 5%:tier5
				float itemTierRoll = Random.Float();
				/*
				int wepTier;

				if (itemTierRoll < 0.5f) {
					wepTier = 2;
					armor = new LeatherArmor();
				} else if (itemTierRoll < 0.8f) {
					wepTier = 3;
					armor = new MailArmor();
				} else if (itemTierRoll < 0.95f) {
					wepTier = 4;
					armor = new ScaleArmor();
				} else {
					wepTier = 5;
					armor = new PlateArmor();
				}
				*/

				try {
					do {
						//weapon = (Weapon) Generator.wepTiers[wepTier - 1].classes[Random.chances(Generator.wepTiers[wepTier - 1].probs)].newInstance();
						weapon = Generator.randomWeapon((Dungeon.depth / 10) + 2);
						armor = Generator.randomArmor((Dungeon.depth / 10) + 2);
					} while (!(weapon instanceof MeleeWeapon));
				} catch (Exception e){
					Fushiginopixeldungeon.reportException(e);
					weapon = new Katana();
					armor = new SamuraiArmor();
				}

				//50%:+0, 30%:+1, 15%:+2, 5%:+3
				float itemLevelRoll = Random.Float();
				int itemLevel;
				if (itemLevelRoll < 0.5f){
					itemLevel = 0;
				} else if (itemLevelRoll < 0.8f){
					itemLevel = 1;
				} else if (itemLevelRoll < 0.95f){
					itemLevel = 2;
				} else {
					itemLevel = 3;
				}
				weapon.upgrade(itemLevel);
				armor.upgrade(itemLevel);

				//10% to be enchanted
				if (Random.Int(10) == 0){
					weapon.enchant();
					armor.inscribe();
				}

			}
		}
		
		public static void process() {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.n( Messages.get(Ghost.class, "find_me") );
				Sample.INSTANCE.play( Assets.SND_GHOST );
				processed = true;
			}
		}
		
		public static void complete() {
			weapon = null;
			armor = null;
			
			Notes.remove( Notes.Landmark.GHOST );
		}

		public static boolean processed(){
			return spawned && processed;
		}
		
		public static boolean completed(){
			return processed() && weapon == null && armor == null;
		}
	}
}
