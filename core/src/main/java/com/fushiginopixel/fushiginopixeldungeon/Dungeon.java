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

package com.fushiginopixel.fushiginopixeldungeon;

import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Awareness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.DoomOfEarthquake;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Light;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MindVision;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.ShopGuardian;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Blacksmith;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Ghost;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Imp;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Wandmaker;
import com.fushiginopixel.fushiginopixeldungeon.items.Ankh;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.DriedRose;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfAlert;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.DeadEndLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.HallsBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.HallsLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.LastLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.LastShopLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.RegularLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SurfaceLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.modes.Mode;
import com.fushiginopixel.fushiginopixeldungeon.levels.modes.NormalMode;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.secret.SecretRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special.SpecialRoom;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.DungeonSeed;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndAlchemy;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndResurrect;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Dungeon {

	//enum of items which have limited spawns, records how many have spawned
	//could all be their own separate numbers, but this allows iterating, much nicer for bundling/initializing.
	public static enum LimitedDrops {
		//limited world drops
		//STRENGTH_POTIONS,
		//UPGRADE_SCROLLS,
		ARCANE_STYLI,


		//doesn't use Generator, so we have to enforce one armband drop here
		THIEVES_ARMBAND,

		//containers
		DEW_VIAL,
		VELVET_POUCH,
		SCROLL_HOLDER,
		POTION_BANDOLIER,
		MAGICAL_HOLSTER;

		public int count = 0;

		//for items which can only be dropped once, should directly access count otherwise.
		public boolean dropped(){
			return count != 0;
		}
		public void drop(){
			count = 1;
		}

		public static void reset(){
			for (LimitedDrops lim : values()){
				lim.count = 0;
			}
		}

		public static void store( Bundle bundle ){
			for (LimitedDrops lim : values()){
				bundle.put(lim.name(), lim.count);
			}
		}

		public static void restore( Bundle bundle ){
			for (LimitedDrops lim : values()){
				if (bundle.contains(lim.name())){
					lim.count = bundle.getInt(lim.name());
				} else {
					lim.count = 0;
				}
				
			}
			//saves prior to 0.6.4
			if (bundle.contains("SEED_POUCH")) {
				LimitedDrops.VELVET_POUCH.count = bundle.getInt("SEED_POUCH");
			}
			if (bundle.contains("WAND_HOLSTER")) {
				LimitedDrops.MAGICAL_HOLSTER.count = bundle.getInt("WAND_HOLSTER");
			}
		}

		//for saves prior to 0.6.1
		public static void legacyRestore( int[] counts ){
			//STRENGTH_POTIONS.count =    counts[0];
			//UPGRADE_SCROLLS.count =     counts[0];
			ARCANE_STYLI.count =        counts[0];
			THIEVES_ARMBAND.count =     counts[1];
			DEW_VIAL.count =            counts[2];
			VELVET_POUCH.count =        counts[3];
			SCROLL_HOLDER.count =       counts[4];
			POTION_BANDOLIER.count =    counts[5];
			MAGICAL_HOLSTER.count =     counts[6];
		}

	}

	public static int challenges;
	public static Mode mode;

	public static Hero hero;
	public static Level level;

	public static QuickSlot quickslot = new QuickSlot();
	
	public static int depth;
	public static int gold;
	
	public static HashSet<Integer> chapters;

	public static SparseArray<ArrayList<Item>> droppedItems;

	public static int version;

	public static long seed;
	
	public static void init() {

		version = Game.versionCode;
		challenges = SPDSettings.challenges();
        mode = SpecialMode.getMode(SPDSettings.specialMode());

		seed = DungeonSeed.randomSeed();

		Actor.clear();
		Actor.resetNextID();
		
		Random.seed( seed );

			Scroll.initLabels();
			Potion.initColors();
			Wand.initSpells();
			Pot.initShapes();
			Ring.initGems();

			SpecialRoom.initForRun();
			SecretRoom.initForRun();

		Random.seed();
		
		Statistics.reset();
		Notes.reset();

		quickslot.reset();
		QuickSlotButton.reset();
		
		depth = -1;//start at depth 0
		gold = 0;

		droppedItems = new SparseArray<ArrayList<Item>>();

		for (LimitedDrops a : LimitedDrops.values())
			a.count = 0;
		
		chapters = new HashSet<Integer>();
		
		Ghost.Quest.reset();
		Wandmaker.Quest.reset();
		Blacksmith.Quest.reset();
		Imp.Quest.reset();

		Generator.reset();
		Generator.initArtifacts();
		hero = new Hero();
		hero.live();
		
		Badges.reset();
		
		GamesInProgress.selectedClass.initHero( hero );
	}

	public static boolean isChallenged( int mask ) {
		return (challenges & mask) != 0;
	}

	public static boolean isSpecialMode( int mask ) {
		return SpecialMode.getModeValue(mode.getClass()) == mask;
	}

	//create a new level at current depth
	public static Level newLevel() {
		
		Dungeon.level = null;
		Actor.clear();
		
		//depth++; move to InterLevelScene
		if (depth > Statistics.deepestFloor) {
			Statistics.deepestFloor = depth;
			
			if (Statistics.qualifiedForNoKilling) {
				Statistics.completedWithNoKilling = true;
			} else {
				Statistics.completedWithNoKilling = false;
			}
		}

        Level level;
		/*
		if(specialMode <= 0) {
            if (depth == 0)
                level = new SurfaceLevel();
            else if (depth >= 1 && depth <= 9 && depth != 5)
                level = new SewerLevel();
            else if (depth == 5)
                level = new SewerMidBossLevel();
            else if (depth == 10)
                level = new SewerBossLevel();
            else if (depth >= 11 && depth <= 19 && depth != 15)
                level = new PrisonLevel();
            else if (depth == 15)
                level = new PrisonMidBossLevel();
            else if (depth == 20)
                level = new PrisonBossLevel();
            else if (depth >= 21 && depth <= 29 && depth != 25)
                level = new CavesLevel();
            else if (depth == 25)
                level = new CavesMidBossLevel();
            else if (depth == 30)
                level = new CavesBossLevel();
            else if (depth >= 31 && depth <= 39)
                level = new CityLevel();
            else if (depth == 40)
                level = new CityBossLevel();
            else if (depth == 41)
                level = new LastShopLevel();
            else if (depth >= 42 && depth <= 49)
                level = new HallsLevel();
            else if (depth == 50)
                level = new HallsBossLevel();
            else if (depth == 51)
                level = new LastLevel();
            else {
                level = new DeadEndLevel();
                Statistics.deepestFloor--;
            }
        }else{
		    level = mode.newLevel(depth);
        }
        */
		level = mode.newLevel(depth);

        level.create();
		
		Statistics.qualifiedForNoKilling = !bossLevel();
		
		return level;
	}
	
	public static void resetLevel() {
		
		Actor.clear();
		
		level.reset();
		switchLevel( level, level.entrance );
	}

	public static long seedCurDepth(){
		return seedForDepth(depth);
	}

	public static long seedForDepth(int depth){
		Random.seed( seed );
		for (int i = 0; i < depth; i ++)
			Random.Long(); //we don't care about these values, just need to go through them
		long result = Random.Long();
		Random.seed();
		return result;
	}
	
	public static boolean shopOnLevel() {
		return mode.isNormalMode() ? (depth % 5 == 1 && depth < 40 && depth > 1) : false;
	}
	
	public static boolean bossLevel() {
		return bossLevel( depth );
	}

	public static boolean canNotBlowUpLevel() {
		return bossLevel( depth ) || !(level instanceof RegularLevel);
	}
	
	public static boolean bossLevel( int depth ) {
		return mode.bossLevel(depth);//depth % 5 == 0/*depth == 10 || depth == 20 || depth == 30 || depth == 40 || depth == 50*/;
	}
	
	@SuppressWarnings("deprecation")
	public static void switchLevel( final Level level, int pos ) {
		
		if (pos < 0 || pos >= level.length()){
			pos = level.exit;
		}
		
		PathFinder.setMapSize(level.width(), level.height());
		
		Dungeon.level = level;
		DriedRose.restoreGhostHero( level, pos );
		Actor.init();
		
		Actor respawner = level.respawner();
		if (respawner != null) {
			Actor.addDelayed( respawner, level.respawnTime() );
		}

		hero.pos = pos;

		viewUpdate();

		hero.curAction = hero.lastAction = null;
		
		observe();
		try {
			saveAll();
		} catch (IOException e) {
			Fushiginopixeldungeon.reportException(e);
			/*This only catches IO errors. Yes, this means things can go wrong, and they can go wrong catastrophically.
			But when they do the user will get a nice 'report this issue' dialogue, and I can fix the bug.*/
		}
	}

	public static void viewUpdate(){
		Light light = hero.buff( Light.class );
		int viewAdapt = Ring.getBonus(hero, RingOfAlert.Alert.class);
		int view = light == null ? level.viewDistance + viewAdapt : Math.max( Light.DISTANCE, level.viewDistance + viewAdapt);

		hero.viewDistance = Math.min(view , 8);
	}

	public static void guardOff(Level level){
		//alarm off
		if (Statistics.thief){
			Statistics.thief = false;
			/*
			Mob[] guardian = new Mob[level.mobs.size()];
			int i = 0;
			for (Mob mob : level.mobs){
                if (mob instanceof ShopGuardian)
				    guardian[i++] = mob;
			}
			for(i = 0;i<guardian.length ;i++){
				if(guardian[i] == null) break;
				//guards off
				guardian[i].destroy();
			}
			*/
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])){
				if (mob instanceof ShopGuardian){
					if (mob.sprite != null)
						mob.sprite.killAndErase();
					mob.destroy();
				}
			}
		}
	}

	public static void setEarthquake(){
		if (!Statistics.amuletObtained && depth > 0){
			DoomOfEarthquake buff = Buff.affect(hero, DoomOfEarthquake.class);
			buff.setTime(0);
		}

		if(depth == 0){
			Buff.detach(hero,DoomOfEarthquake.class);
		}
	}

	public static void dropToChasm( Item item ) {
		int depth = Dungeon.depth + 1;
		ArrayList<Item> dropped = (ArrayList<Item>)Dungeon.droppedItems.get( depth );
		if (dropped == null) {
			Dungeon.droppedItems.put( depth, dropped = new ArrayList<Item>() );
		}
		dropped.add( item );
	}

	/*public static boolean posNeeded() {
		//2 POS each floor set
		int posLeftThisSet = 2 - (LimitedDrops.STRENGTH_POTIONS.count - (depth / 5) * 2);
		if (posLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);

		//pos drops every two floors, (numbers 1-2, and 3-4) with a 50% chance for the earlier one each time.
		int targetPOSLeft = 2 - floorThisSet/2;
		if (floorThisSet % 2 == 1 && Random.Int(2) == 0) targetPOSLeft --;

		if (targetPOSLeft < posLeftThisSet) return true;
		else return false;

	}*/

	/*
	public static boolean souNeeded() {
		int souLeftThisSet;
		//2 SOU each floor set, 1 (rounded) on forbidden runes challenge
		if (isChallenged(Challenges.NO_SCROLLS)){
			souLeftThisSet = Math.round(1.5f - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 1.5f));
		} else {
			souLeftThisSet = 3 - (LimitedDrops.UPGRADE_SCROLLS.count - (depth / 5) * 3);
		}
		if (souLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < souLeftThisSet;
	}
	*/
	
	public static boolean asNeeded() {
		//1 AS each floor set
		int asLeftThisSet = 1 - (LimitedDrops.ARCANE_STYLI.count - (depth / 5));
		if (asLeftThisSet <= 0) return false;

		int floorThisSet = (depth % 5);
		//chance is floors left / scrolls left
		return Random.Int(5 - floorThisSet) < asLeftThisSet;
	}
	
	private static final String VERSION		= "version";
	private static final String SEED		= "seed";
	private static final String CHALLENGES	= "challenges";
	private static final String SPECIALMODE	= "specialmode";
	private static final String HERO		= "hero";
	private static final String GOLD		= "gold";
	private static final String DEPTH		= "depth";
	private static final String DROPPED     = "dropped%d";
	private static final String LEVEL		= "level";
	private static final String LIMDROPS    = "limited_drops";
	private static final String CHAPTERS	= "chapters";
	private static final String QUESTS		= "quests";
	private static final String BADGES		= "badges";
	
	public static void saveGame( int save ) throws IOException {
		try {
			Bundle bundle = new Bundle();

			version = Game.versionCode;
			bundle.put( VERSION, version );
			bundle.put( SEED, seed );
			bundle.put( CHALLENGES, challenges );
			bundle.put( SPECIALMODE, mode );
			bundle.put( HERO, hero );
			bundle.put( GOLD, gold );
			bundle.put( DEPTH, depth );

			for (int d : droppedItems.keyArray()) {
				bundle.put(Messages.format(DROPPED, d), droppedItems.get(d));
			}

			quickslot.storePlaceholders( bundle );

			Bundle limDrops = new Bundle();
			LimitedDrops.store( limDrops );
			bundle.put ( LIMDROPS, limDrops );
			
			int count = 0;
			int ids[] = new int[chapters.size()];
			for (Integer id : chapters) {
				ids[count++] = id;
			}
			bundle.put( CHAPTERS, ids );
			
			Bundle quests = new Bundle();
			Ghost		.Quest.storeInBundle( quests );
			Wandmaker	.Quest.storeInBundle( quests );
			Blacksmith	.Quest.storeInBundle( quests );
			Imp			.Quest.storeInBundle( quests );
			bundle.put( QUESTS, quests );
			
			SpecialRoom.storeRoomsInBundle( bundle );
			SecretRoom.storeRoomsInBundle( bundle );

			WndAlchemy.storeInBundle( bundle );
			
			Statistics.storeInBundle( bundle );
			Notes.storeInBundle( bundle );
			Generator.storeInBundle( bundle );
			
			Scroll.save( bundle );
			Potion.save( bundle );
			Wand.save(bundle);
			Ring.save( bundle );
            Pot.save( bundle );

			Actor.storeNextID( bundle );
			
			Bundle badges = new Bundle();
			Badges.saveLocal( badges );
			bundle.put( BADGES, badges );
			
			FileUtils.bundleToFile( GamesInProgress.gameFile(save), bundle);
			
		} catch (IOException e) {
			GamesInProgress.setUnknown( save );
			Fushiginopixeldungeon.reportException(e);
		}
	}
	
	public static void saveLevel( int save ) throws IOException {
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level );
		
		FileUtils.bundleToFile(GamesInProgress.depthFile( save, depth), bundle);
	}
	
	public static void saveAll() throws IOException {
		if (hero != null && hero.isAlive()) {
			
			Actor.fixTime();
			saveGame( GamesInProgress.curSlot );
			saveLevel( GamesInProgress.curSlot );

			GamesInProgress.set( GamesInProgress.curSlot, depth, challenges, mode, hero );

		} else if (WndResurrect.instance != null) {
			
			WndResurrect.instance.hide();
			Hero.reallyDie( WndResurrect.causeOfDeath );
			
		}
	}
	
	public static void loadGame( int save ) throws IOException {
		loadGame( save, true );
	}
	
	public static void loadGame( int save, boolean fullLoad ) throws IOException {
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.gameFile( save ) );

		version = bundle.getInt( VERSION );

		seed = bundle.contains( SEED ) ? bundle.getLong( SEED ) : DungeonSeed.randomSeed();

		Actor.restoreNextID( bundle );

		quickslot.reset();
		QuickSlotButton.reset();

		Dungeon.challenges = bundle.getInt( CHALLENGES );
		Dungeon.mode = (Mode)bundle.get( SPECIALMODE );
		if(mode == null){
			mode = new NormalMode();
		}
		
		Dungeon.level = null;
		Dungeon.depth = -1;
		
		Scroll.restore( bundle );
		Potion.restore( bundle );
		Wand.restore( bundle );
		Ring.restore( bundle );
		Pot.restore( bundle );

		quickslot.restorePlaceholders( bundle );
		
		if (fullLoad) {

			//pre-0.6.1
			if( bundle.contains("limiteddrops") ){
				LimitedDrops.legacyRestore( bundle.getIntArray("limiteddrops") );
			} else {
				LimitedDrops.restore( bundle.getBundle(LIMDROPS) );
			}

			chapters = new HashSet<Integer>();
			int ids[] = bundle.getIntArray( CHAPTERS );
			if (ids != null) {
				for (int id : ids) {
					chapters.add( id );
				}
			}
			
			Bundle quests = bundle.getBundle( QUESTS );
			if (!quests.isNull()) {
				Ghost.Quest.restoreFromBundle( quests );
				Wandmaker.Quest.restoreFromBundle( quests );
				Blacksmith.Quest.restoreFromBundle( quests );
				Imp.Quest.restoreFromBundle( quests );
			} else {
				Ghost.Quest.reset();
				Wandmaker.Quest.reset();
				Blacksmith.Quest.reset();
				Imp.Quest.reset();
			}
			
			SpecialRoom.restoreRoomsFromBundle(bundle);
			SecretRoom.restoreRoomsFromBundle(bundle);
		}
		
		Bundle badges = bundle.getBundle(BADGES);
		if (!badges.isNull()) {
			Badges.loadLocal( badges );
		} else {
			Badges.reset();
		}
		
		Notes.restoreFromBundle( bundle );
		
		hero = null;
		hero = (Hero)bundle.get( HERO );

		WndAlchemy.restoreFromBundle( bundle, hero );
		
		gold = bundle.getInt( GOLD );
		depth = bundle.getInt( DEPTH );
		
		Statistics.restoreFromBundle( bundle );
		Generator.restoreFromBundle( bundle );

		droppedItems = new SparseArray<>();
		for (int i=2; i <= Statistics.deepestFloor + 1; i++) {
			ArrayList<Item> dropped = new ArrayList<Item>();
			if (bundle.contains(Messages.format( DROPPED, i )))
				for (Bundlable b : bundle.getCollection( Messages.format( DROPPED, i ) ) ) {
					dropped.add( (Item)b );
				}
			if (!dropped.isEmpty()) {
				droppedItems.put( i, dropped );
			}
		}
	}
	
	public static Level loadLevel( int save ) throws IOException {
		
		Dungeon.level = null;
		Actor.clear();
		
		Bundle bundle = FileUtils.bundleFromFile( GamesInProgress.depthFile( save, depth)) ;
		
		Level level = (Level)bundle.get( LEVEL );
		
		if (level == null){
			//throw new IOException();
			return newLevel();
		} else {
			return level;
		}
	}
	
	public static void deleteGame( int save, boolean deleteLevels ) {
		
		FileUtils.deleteFile(GamesInProgress.gameFile(save));
		
		if (deleteLevels) {
			FileUtils.deleteDir(GamesInProgress.gameFolder(save));
		}
		
		GamesInProgress.delete( save );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.depth = bundle.getInt( DEPTH );
		info.version = bundle.getInt( VERSION );
		info.challenges = bundle.getInt( CHALLENGES );
		info.mode = (Mode)bundle.get( SPECIALMODE );
		Hero.preview( info, bundle.getBundle( HERO ) );
		Statistics.preview( info, bundle );
	}
	
	public static void fail( Class cause ) {
		if (hero.belongings.getItem( Ankh.class ) == null) {
			Rankings.INSTANCE.submit( false, cause );
		}
	}

	public static void failByDoom( Class cause ) {
		Rankings.INSTANCE.submit( false, cause );
	}

	public static void escape( Class cause ) {

		hero.belongings.identify();

		Statistics.overed = true;
		Rankings.INSTANCE.submit( false, cause );
	}
	
	public static void win( Class cause ) {

		hero.belongings.identify();

		int chCount = 0;
		for (int ch : Challenges.MASKS){
			if ((challenges & ch) != 0) chCount++;
		}
		
		if (chCount != 0) {
			Badges.validateChampion(chCount);
		}

		Statistics.overed = true;
		Rankings.INSTANCE.submit( true, cause );
	}

	public static void observe(){
		observe( hero.viewDistance+1 );
	}
	
	public static void observe( int dist ) {

		if (level == null) {
			return;
		}
		
		level.updateFieldOfView(hero, level.heroFOV);

		int x = hero.pos % level.width();
		int y = hero.pos / level.width();
	
		//left, right, top, bottom
		int l = Math.max( 0, x - dist );
		int r = Math.min( x + dist, level.width() - 1 );
		int t = Math.max( 0, y - dist );
		int b = Math.min( y + dist, level.height() - 1 );
	
		int width = r - l + 1;
		int height = b - t + 1;
		
		int pos = l + t * level.width();
	
		for (int i = t; i <= b; i++) {
			BArray.or( level.visited, level.heroFOV, pos, width, level.visited );
			pos+=level.width();
		}
	
		GameScene.updateFog(l, t, width, height);


		int buffLevel = MindVision.mindvrBonus(hero);
		if (buffLevel > 0) {
			if (buffLevel >= 6) {
				for (Mob m : level.mobs.toArray(new Mob[0])) {
					BArray.or(level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited);
					BArray.or(level.visited, level.heroFOV, m.pos, 3, level.visited);
					BArray.or(level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited);
					//updates adjacent cells too
					GameScene.updateFog(m.pos, 2);
				}
			}
			else if(buffLevel >=1) {
				for (Mob m : level.mobs.toArray(new Mob[0])) {
					if (m.distance(hero) <= buffLevel* 2){
						BArray.or(level.visited, level.heroFOV, m.pos - 1 - level.width(), 3, level.visited);
						BArray.or(level.visited, level.heroFOV, m.pos, 3, level.visited);
						BArray.or(level.visited, level.heroFOV, m.pos - 1 + level.width(), 3, level.visited);
						//updates adjacent cells too
						GameScene.updateFog(m.pos, 2);
					}
				}
			}

		}
		
		if (hero.buff(Awareness.class) != null){
			for (Heap h : level.heaps.values()){
				BArray.or( level.visited, level.heroFOV, h.pos - 1 - level.width(), 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1, 3, level.visited );
				BArray.or( level.visited, level.heroFOV, h.pos - 1 + level.width(), 3, level.visited );
				GameScene.updateFog(h.pos, 2);
			}
		}

		GameScene.afterObserve();
	}

	//we store this to avoid having to re-allocate the array with each pathfind
	private static boolean[] passable;

	private static void setupPassable(){
		if (passable == null || passable.length != Dungeon.level.length())
			passable = new boolean[Dungeon.level.length()];
		else
			BArray.setFalse(passable);
	}

	public static PathFinder.Path findPath(Char ch, int from, int to, boolean pass[], boolean[] visible ) {

		setupPassable();
		System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		}
		if(ch.passWall){
			BArray.or( pass, Dungeon.level.solid, passable );
		}

		for (Char c : Actor.chars()) {
			if (visible[c.pos]) {
				passable[c.pos] = false;
			}
		}

		return PathFinder.find( from, to, passable );

	}
	
	public static int findStep(Char ch, int from, int to, boolean pass[], boolean[] visible ) {

		if (Dungeon.level.adjacent( from, to )) {
			return Actor.findChar( to ) == null && (pass[to] || Dungeon.level.avoid[to]) ? to : -1;
		}

		setupPassable();
		if (ch.flying || ch.buff( Amok.class ) != null) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		}else if(ch.passWall){
			BArray.or( pass, Dungeon.level.solid, passable );
		}
		else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}
		
		for (Char c : Actor.chars()) {
			if (visible[c.pos]) {
				passable[c.pos] = false;
			}
		}
		
		return PathFinder.getStep( from, to, passable );

	}
	
	public static int flee( Char ch, int cur, int from, boolean pass[], boolean[] visible ) {

		setupPassable();
		if (ch.flying) {
			BArray.or( pass, Dungeon.level.avoid, passable );
		}else if(ch.passWall){
			BArray.or( pass, Dungeon.level.solid, passable );
		} else {
			System.arraycopy( pass, 0, passable, 0, Dungeon.level.length() );
		}
		
		for (Char c : Actor.chars()) {
			if (visible[c.pos]) {
				passable[c.pos] = false;
			}
		}
		passable[cur] = true;
		
		return PathFinder.getStepBack( cur, from, passable );
		
	}

}
