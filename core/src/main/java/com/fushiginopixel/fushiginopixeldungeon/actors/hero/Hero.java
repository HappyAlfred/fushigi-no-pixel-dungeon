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

package com.fushiginopixel.fushiginopixeldungeon.actors.hero;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Bones;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.GamesInProgress;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Awareness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Barkskin;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Berserk;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bless;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Combo;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Drowsy;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Fury;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MindVision;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Momentum;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Regeneration;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.SnipersMark;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NPC;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.CheckedCell;
import com.fushiginopixel.fushiginopixeldungeon.effects.Flare;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.Surprise;
import com.fushiginopixel.fushiginopixeldungeon.items.Amulet;
import com.fushiginopixel.fushiginopixeldungeon.items.Ankh;
import com.fushiginopixel.fushiginopixeldungeon.items.Dewdrop;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap.Type;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.AntiMagic;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Brimstone;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Viscosity;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.properties.AbsorbWater;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CapeOfThorns;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.DriedRose;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.EtherealChains;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.HornOfPlenty;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.TalismanOfForesight;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.TimekeepersHourglass;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.CrystalKey;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.GoldenKey;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.IronKey;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.Key;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMight;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfStrength;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfEvasion;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfAlert;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfFuror;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfHaste;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfMight;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfTenacity;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Flail;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Chasm;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Earthroot;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.InterlevelScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.RankingsScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.SurfaceScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.HeroSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.AttackIndicator;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndAlchemy;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndMessage;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndOptions;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndResurrect;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class Hero extends Char {

	{
		actPriority = HERO_PRIO;
		
		alignment = Alignment.ALLY;
	}
	
	public static final int MAX_LEVEL = 99;

	public static final int STARTING_STR = 10;
	
	private static final float TIME_TO_REST		    = 1f;
	private static final float TIME_TO_SEARCH	    = 2f;
	private static final float HUNGER_FOR_SEARCH	= 6f;
	
	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;
	/*
	public int attackSkill = 10;
	public int defenseSkill = 5;
*/
    public int attackSkill = 100;
    public int defenseSkill = 25;

	public boolean ready = false;
	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;
	
	public boolean resting = false;
	
	//public Belongings belongings;
	
	public int STR;
	public int STRMAX;
	
	public float awareness;
	
	public int lvl = 1;
	public int exp = 0;
	
	public int HTBoost = 0;
	
	private ArrayList<Mob> visibleEnemies;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resultign in better performance
	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();
	
	public Hero() {
		super();
		name = Messages.get(this, "name");
		
		HP = HT = 20;
        STRMAX = STR = STARTING_STR;
		
		//belongings = new Belongings( this );
		
		visibleEnemies = new ArrayList<Mob>();
	}
	
	public void updateHT( boolean boostHP ){
		int curHT = HT;
		
		HT = 20 + 5*(lvl-1) + HTBoost;
		float multiplier = RingOfMight.HTMultiplier(this);
		HT = Math.round(multiplier * HT);
		
		if (boostHP){
			HP += Math.max(HT - curHT, 0);
		}
		HP = Math.min(HP, HT);
	}

	public int STR() {
		int STR = this.STR;

		STR = (buff(Weakness.class) != null) ? STR - 5 : STR;
		if(STR < 0)
			STR = 0;

		STR += RingOfMight.strengthBonus( this );

		return STR;
	}

    public int STRMAX() {
        int STRMAX = this.STRMAX;

		STRMAX = (buff(Weakness.class) != null) ? STRMAX - 5 : STRMAX;
		if(STRMAX < 0)
			STRMAX = 0;

        STRMAX += RingOfMight.strengthBonus( this );

        return STRMAX;
    }

	private static final String ATTACK		= "attackSkill";
	private static final String DEFENSE		= "defenseSkill";
	private static final String STRENGTH	= "STR";
    private static final String STRENGTHMAX	= "STRMAX";
	private static final String LEVEL		= "lvl";
	private static final String EXPERIENCE	= "exp";
	private static final String HTBOOST     = "htboost";
	
	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );
		
		heroClass.storeInBundle( bundle );
		subClass.storeInBundle( bundle );
		
		bundle.put( ATTACK, attackSkill );
		bundle.put( DEFENSE, defenseSkill );
		
		bundle.put( STRENGTH, STR );
        bundle.put( STRENGTHMAX, STRMAX );
		
		bundle.put( LEVEL, lvl );
		bundle.put( EXPERIENCE, exp );
		
		bundle.put( HTBOOST, HTBoost );

		//belongings.storeInBundle( bundle );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		heroClass = HeroClass.restoreInBundle( bundle );
		subClass = HeroSubClass.restoreInBundle( bundle );
		
		attackSkill = bundle.getInt( ATTACK );
		defenseSkill = bundle.getInt( DEFENSE );
		
		STR = bundle.getInt( STRENGTH );
        STRMAX = bundle.getInt( STRENGTHMAX );
		
		lvl = bundle.getInt( LEVEL );
		exp = bundle.getInt( EXPERIENCE );
		
		HTBoost = bundle.getInt(HTBOOST);
		
		belongings.restoreFromBundle( bundle );
	}
	
	public static void preview( GamesInProgress.Info info, Bundle bundle ) {
		info.level = bundle.getInt( LEVEL );
		info.str = bundle.getInt( STRENGTH );
		info.exp = bundle.getInt( EXPERIENCE );
		info.hp = bundle.getInt( Char.TAG_HP );
		info.ht = bundle.getInt( Char.TAG_HT );
		info.shld = bundle.getInt( Char.TAG_SHLD );
		info.heroClass = HeroClass.restoreInBundle( bundle );
		info.subClass = HeroSubClass.restoreInBundle( bundle );
		Belongings.preview( info, bundle );
	}
	
	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	public String givenName(){
		return name.equals(Messages.get(this, "name")) ? className() : name;
	}
	
	public void live() {
		Buff.affect( this, Regeneration.class );
		Buff.affect( this, Hunger.class );
	}
	
	public int tier() {
		return 0;//belongings.armor == null ? 0 : belongings.armor.tier;
	}

	
	@Override
	public int attackSkill( Char target ) {
		return attackSkill;
	}
	
	@Override
	public int defenseSkill( Char enemy ) {

		if(enemy.canSurpriseAttack(this) || enemy.invisible > 0){
			return 0;
		}
		float evasion = defenseSkill;

		return Math.round(evasion);
	}

	/*
	@Override
	public int drRoll() {
		int dr = 0;
		Barkskin bark = buff(Barkskin.class);

		if (belongings.armor != null) {
			int armDr = Random.NormalIntRange( belongings.armor.min(), belongings.armor.max());

			if (armDr > 0) dr += armDr;
		}
		if (belongings.weapon != null)  {
			int wepDr = Random.NormalIntRange( 0 , belongings.weapon.defenseFactor( this ) );

			if (wepDr > 0) dr += wepDr;
		}
		if (bark != null)               dr += Random.NormalIntRange( 0 , bark.level() );

		return dr;
	}
	*/

    public float strengthMultiplier(EffectType type){
	    float a = 0.05f;
		if(type.isExistAttachType(EffectType.MISSILE) && subClass == HeroSubClass.SNIPER){
			a *= 2;
		}

        a = 0.5f + a * STR();
        if(heroClass == HeroClass.FUURAI){
			/*
			20/220 = 0.09
			40/240 = 0.16
			60/260 = 0.23
			80/280 = 0.28
			 */
            return (a * (1 + (float)lvl / (lvl + 200)));
        }
        return a;
    }

    /*
	public float strengthMultiplier(float a){
		if(heroClass == HeroClass.FUURAI){
			return (float)((0.5 + a * STR()) * (1 + (float)lvl / (lvl + 200)));
		}
		return (float)(0.5 + a * STR());
	}
	*/

	public float strengthMultiplier(){
		return strengthMultiplier(new EffectType(EffectType.MELEE,0));
	}

	public int baseDamageMin(){
	    int min;
		int lvl = Math.max(this.lvl - 1, 0);
	    if(heroClass == HeroClass.FUURAI){
	        min = (int)(Math.pow(lvl, 2f/3) * 2 + 5);
        }else{
	        min = (int)(Math.pow(lvl, 2f/3) * 4 / 3 + 1);
        }
	    return min;
    }

    public int baseDamageMax(){
        int max;
        int lvl = Math.max(this.lvl - 1, 0);
        if(heroClass == HeroClass.FUURAI){
            max = (int)(Math.pow(lvl, 2f/3) * 5 + 5);
        }else{
            max = (int)(Math.pow(lvl, 2f/3) * 10 / 3 + 5);
        }
        return max;
    }

    /*
	@Override
	public int damageRoll() {
		KindOfWeapon wep = belongings.weapon;
		int dmg = 0;
		if (wep != null) {
			dmg += wep.damageRoll( this );
		} else {
			if(heroClass == HeroClass.FUURAI){
				dmg = Random.NormalIntRange(5 + lvl / 5 , 5 + lvl / 3);
			}else {
				dmg = Random.NormalIntRange(1, 5 Math.max(STR()-8, 1));
			}
		}
		dmg += Random.NormalIntRange(baseDamageMin(), baseDamageMax());
        dmg *= strengthMultiplier();
		if (wep != null) {
			dmg = wep.damageRoll( this ) + RingOfForce.armedDamageBonus(this);
		} else {
			dmg = RingOfForce.damageRoll(this);
		}
		if (dmg < 0) dmg = 0;
		
		Berserk berserk = buff(Berserk.class);
		if (berserk != null) dmg = berserk.damageFactor(dmg);
		
		return buff( Fury.class ) != null ? (int)(dmg * 1.5f) : dmg;
	}
	*/

    @Override
    public int damageRoll(){
        return Random.NormalIntRange(baseDamageMin(), baseDamageMax());
    }

    @Override
    public int totalDamageRoll(KindOfWeapon weapon) {
	    int dmg = super.totalDamageRoll(weapon);
        dmg *= strengthMultiplier();
        if (dmg < 0) dmg = 0;

        Berserk berserk = buff(Berserk.class);
        if (berserk != null) dmg = berserk.damageFactor(dmg);

        return buff( Fury.class ) != null ? (int)(dmg * 1.5f) : dmg;
    }
	
	@Override
	public float speed() {

		float speed = super.speed();

		/*
		speed *= RingOfHaste.speedMultiplier(this);
		
		if (belongings.armor != null) {
			speed = belongings.armor.speedFactor(this, speed);
		}
		*/
		
		Momentum momentum = buff(Momentum.class);
		if (momentum != null){
			((HeroSprite)sprite).sprint( 1f + 0.05f*momentum.stacks());
			speed *= momentum.speedMultiplier();
		}
		
		return speed;
		
	}

	/*
	//shit
	public boolean canSurpriseAttack(){
		//if (belongings.weapon == null || !(belongings.weapon instanceof Weapon))    return true;
		//if (STR() < ((Weapon)belongings.weapon).STRReq())                           return false;
		if (belongings.weapon instanceof Flail)                                     return false;

		return true;
	}
	*/

	public boolean canAttack(Char enemy){
		return super.canAttack(enemy);
	}
	/*
	public boolean canAttack(Char enemy){
		if (enemy == null || pos == enemy.pos)
			return false;

		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos))
			return true;

		KindOfWeapon wep = Dungeon.hero.belongings.weapon;

		if (wep != null && Dungeon.level.distance( pos, enemy.pos ) <= wep.reachFactor(this)){

			boolean[] passable = BArray.not(Dungeon.level.solid, null);
			for (Mob m : Dungeon.level.mobs)
				passable[m.pos] = false;

			PathFinder.buildDistanceMap(enemy.pos, passable, wep.reachFactor(this));

			return PathFinder.distance[pos] <= wep.reachFactor(this);

		} else {
			return false;
		}
	}*/

	@Override
	public void spend( float time ) {
		justMoved = false;
		TimekeepersHourglass.timeFreeze buff = buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null){
			buff.processTime(time);
		} else {
			super.spend(time);
		}
	}
	
	@Override
	public boolean act() {
		
		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;
		
		
		if (!ready) {
			//do a full observe (including fog update) if not resting.
			if (!resting || MindVision.mindvrBonus(this) > 0 || buff(Awareness.class) != null) {
				Dungeon.observe();
			} else {
				//otherwise just directly re-calculate FOV
				Dungeon.level.updateFieldOfView(this, fieldOfView);
			}
		}
		
		checkVisibleMobs();
		if (buff(FlavourBuff.class) != null) {
			BuffIndicator.refreshHero();
		}

		if (Dungeon.level.water[pos] && belongings.armor!= null && belongings.armor.hasGlyph(AbsorbWater.class)) {
			int armorLevel = belongings.armor.level();
			armorLevel = Math.max(armorLevel ,0);
			Buff.affect(this, Barkskin.class).level(armorLevel + 3);
		}
		
		if (paralysed > 0) {
			
			curAction = null;
			
			spendAndNext( TICK );
			return false;
		}
		
		if (curAction == null) {
			
			if (resting) {
				spend( TIME_TO_REST ); next();
				return false;
			}
			
			ready();
			return false;
			
		} else {
			
			resting = false;
			
			ready = false;
			
			if (curAction instanceof HeroAction.Move) {

				return actMove( (HeroAction.Move)curAction );
				
			} else
			if (curAction instanceof HeroAction.Interact) {

				return actInteract( (HeroAction.Interact)curAction );
				
			} else
			if (curAction instanceof HeroAction.Buy) {

				return actBuy( (HeroAction.Buy)curAction );
				
			}else
			if (curAction instanceof HeroAction.PickUp) {

				return actPickUp( (HeroAction.PickUp)curAction );
				
			} else
			if (curAction instanceof HeroAction.OpenChest) {

				return actOpenChest( (HeroAction.OpenChest)curAction );
				
			} else
			if (curAction instanceof HeroAction.Unlock) {

				return actUnlock((HeroAction.Unlock) curAction);
				
			} else
			if (curAction instanceof HeroAction.Descend) {

				return actDescend( (HeroAction.Descend)curAction );
				
			} else
			if (curAction instanceof HeroAction.Ascend) {

				return actAscend( (HeroAction.Ascend)curAction );
				
			} else
			if (curAction instanceof HeroAction.Attack) {

				return actAttack( (HeroAction.Attack)curAction );
				
			} else
			if (curAction instanceof HeroAction.Alchemy) {

				return actAlchemy( (HeroAction.Alchemy)curAction );
				
			}
		}
		
		return false;
	}
	
	@Override
	public void busy() {
		ready = false;
	}
	
	private void ready() {
		if (sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateState();
		
		GameScene.ready();
	}
	
	public void interrupt() {
		if (isAlive() && curAction != null &&
			((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
			(curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend))) {
			lastAction = curAction;
		}
		curAction = null;
	}
	
	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}
	
	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;
	public int justMovedPos = -1;
	
	private boolean actMove( HeroAction.Move action ) {
		int temp = pos;
		if (getCloser( action.dst )) {
			justMovedPos = temp;
			justMoved = true;
			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actInteract( HeroAction.Interact action ) {
		
		NPC npc = action.npc;

		if (Dungeon.level.adjacent( pos, npc.pos )) {
			
			ready();
			sprite.turnTo( pos, npc.pos );
			return npc.interact();
			
		} else {
			
			if (fieldOfView[npc.pos] && getCloser( npc.pos )) {

				return true;

			} else {
				ready();
				return false;
			}
			
		}
	}
	
	private boolean actBuy( HeroAction.Buy action ) {
		int dst = action.dst;
		if (pos == dst || Dungeon.level.adjacent( pos, dst )) {

			ready();
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				GameScene.show( new WndTradeItem( heap, true ) );
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy( HeroAction.Alchemy action ) {
		int dst = action.dst;
		if (Dungeon.level.distance(dst, pos) <= 1) {

			ready();
			GameScene.show(new WndAlchemy());
			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp( HeroAction.PickUp action ) {
		int dst = action.dst;
		if (pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( pos );
			if (heap != null) {
				Item item = heap.peek();
				if (item.doPickUp( this )) {
					heap.pickUp();

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof DriedRose.Petal
							|| item instanceof Key) {
						//Do Nothing
					} else {

						boolean important =
								((item instanceof ScrollOfUpgrade || item instanceof ScrollOfMagicalInfusion) && ((Scroll)item).isKnown()) ||
								((item instanceof PotionOfStrength || item instanceof PotionOfMight) && ((Potion)item).isKnown());
						if (important) {
							GLog.p( Messages.get(this, "you_now_have", item.name()) );
						} else {
							GLog.i( Messages.get(this, "you_now_have", item.name()) );
						}
					}
					
					curAction = null;
				} else {
					heap.sprite.drop();
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actOpenChest( HeroAction.OpenChest action ) {
		int dst = action.dst;
		if (Dungeon.level.adjacent( pos, dst ) || pos == dst) {
			
			Heap heap = Dungeon.level.heaps.get( dst );
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {
				
				if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.depth)) < 1)
					|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.depth)) < 1)){

						GLog.w( Messages.get(this, "locked_chest") );
						ready();
						return false;

				}
				
				switch (heap.type) {
				case TOMB:
					Sample.INSTANCE.play( Assets.SND_TOMB );
					Camera.main.shake( 1, 0.5f );
					break;
				case SKELETON:
				case REMAINS:
					break;
				default:
					Sample.INSTANCE.play( Assets.SND_UNLOCK );
				}
				
				sprite.operate( dst );
				
			} else {
				ready();
			}

			return false;

		} else if (getCloser( dst )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actUnlock( HeroAction.Unlock action ) {
		int doorCell = action.dst;
		if (Dungeon.level.adjacent( pos, doorCell )) {
			
			boolean hasKey = false;
			int door = Dungeon.level.map[doorCell];
			
			if (door == Terrain.LOCKED_DOOR
					&& Notes.keyCount(new IronKey(Dungeon.depth)) > 0) {
				
				hasKey = true;
				
			} else if (door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.depth)) > 0) {

				hasKey = true;
				
			}
			
			if (hasKey) {
				
				sprite.operate( doorCell );
				
				Sample.INSTANCE.play( Assets.SND_UNLOCK );
				
			} else {
				GLog.w( Messages.get(this, "locked_door") );
				ready();
			}

			return false;

		} else if (getCloser( doorCell )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actDescend( HeroAction.Descend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.exit) {
			
			curAction = null;

			Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
			if (buff != null) buff.detach();

			boolean descendConfirm = true;

			if(!Statistics.overed){
				GameScene.show(
						new WndOptions( Messages.get(Hero.class, "descend"),
								Messages.get(Hero.class, "enter"),
								Messages.get(Hero.class, "yes"),
								Messages.get(Hero.class, "no") ) {
							@Override
							protected void onSelect( int index ) {
								if (index == 0) {
									InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
									Game.switchScene( InterlevelScene.class );
								}
								else {

								}
							}
						}
				);
				ready();
			}
			else if(Dungeon.depth == 0){
				ready();
			}
			else{
				InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
				Game.switchScene( InterlevelScene.class );
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAscend( HeroAction.Ascend action ) {
		int stairs = action.dst;
		if (pos == stairs && pos == Dungeon.level.entrance) {
			
			if (Dungeon.depth == 0) {
				
				if (!Statistics.overed){//(belongings.getItem( Amulet.class ) == null) {
					GameScene.show( new WndMessage( Messages.get(this, "leave") ) );
					ready();
				} else {
					Dungeon.deleteGame( GamesInProgress.curSlot, true );
					Game.switchScene( RankingsScene.class );
				}
				
			} else if (Dungeon.depth == 1) {

				if (belongings.getItem( Amulet.class ) == null) {
					GameScene.show( new WndMessage( Messages.get(this, "leave") ) );
					ready();
				} else {
					Badges.silentValidateHappyEnd();
					Dungeon.win( Amulet.class );
					//Dungeon.deleteGame( GamesInProgress.curSlot, true );
					Game.switchScene( SurfaceScene.class );
				}

			}else {
				
				curAction = null;

				if(Statistics.amuletObtained) {
					Buff buff = buff(TimekeepersHourglass.timeFreeze.class);
					if (buff != null) buff.detach();

					InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
					Game.switchScene(InterlevelScene.class);
				}
				else{
					GameScene.show( new WndMessage( Messages.get(this, "ascend") ) );
					ready();
				}
			}

			return false;

		} else if (getCloser( stairs )) {

			return true;

		} else {
			ready();
			return false;
		}
	}
	
	private boolean actAttack( HeroAction.Attack action ) {

		enemy = action.target;

		if (enemy.isAlive() && canAttack( enemy ) && !isCharmedBy( enemy )) {
			
			sprite.attack( enemy.pos );

			return false;

		} else {

			if (fieldOfView[enemy.pos] && getCloser( enemy.pos )) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}
	
	public void rest( boolean fullRest ) {
		spendAndNext( TIME_TO_REST );
		if (!fullRest) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "wait") );
		}
		resting = fullRest;
	}
	
	@Override
	public int attackProc( KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
        damage = super.attackProc( weapon, enemy, damage, type);
		//KindOfWeapon wep = belongings.weapon;
			
		switch (subClass) {
		case SNIPER:
			//if (wep instanceof MissileWeapon && type.isExistAttachType(EffectType.MISSILE)) {
			if (type.isExistAttachType(EffectType.MISSILE)){
				Buff.prolong( this, SnipersMark.class, totalAttackDelay() ).object = enemy.id();
			}
			break;
		default:
		}

		
		return damage;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage, EffectType type  ) {

		if(enemy.canSurpriseAttack(this)){
			Surprise.hit(this);
		}

		/*
		if (damage > 0 && subClass == HeroSubClass.BERSERKER){
			Berserk berserk = Buff.affect(this, Berserk.class, new EffectType(0,EffectType.SPIRIT));
			berserk.damage(damage);
		}
		*/
		/*
		if (belongings.armor != null) {
			damage = belongings.armor.proc( enemy, this, damage, type, Armor.EVENT_SUFFER_ATTACK );
		}
		
		Earthroot.Armor armor = buff( Earthroot.Armor.class );
		if (armor != null) {
			damage = armor.absorb( damage );
		}
		*/
		
		return super.defenseProc(enemy, damage, type);
	}
	
	@Override
	public int damage( int dmg, Object src ,EffectType type) {
		if (buff(TimekeepersHourglass.timeStasis.class) != null || Dungeon.depth == 0)
			return 0;

		if (!(src instanceof Hunger || src instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}

		if (this.buff(Drowsy.class) != null){
			Buff.detach(this, Drowsy.class);
			GLog.w( Messages.get(this, "pain_resist") );
		}

		/*
		CapeOfThorns.Thorns thorns = buff( CapeOfThorns.Thorns.class );
		if (thorns != null) {
			dmg = thorns.proc(dmg, (src instanceof Char ? (Char)src : null),  this, type);
		}

		dmg = (int)Math.ceil(dmg * RingOfTenacity.damageMultiplier( this ));
		*/

		/*
		if (belongings.armor != null && belongings.armor.hasGlyph(Brimstone.class)
				&& EffectType.isExistType(type,Brimstone.RESISTSTYPE)) {
			dmg /= 2;
		}

		//TODO improve this when I have proper damage source logic
		if (belongings.armor != null && belongings.armor.hasGlyph(AntiMagic.class)
				&& EffectType.isExistType(type,AntiMagic.RESISTSTYPE)){
			dmg -= Random.NormalIntRange(belongings.armor.min(), belongings.armor.max())/3;
		}
		*/

		if (subClass == HeroSubClass.BERSERKER){
			int realDamage = dmg;
			Class<?> srcClass = src.getClass();
			if (isImmune( srcClass,type )) {
				realDamage = 0;
			} else {
				realDamage = Math.round( dmg * resist( srcClass,type ));
			}
			if(realDamage > 0) {
				Berserk berserk = Buff.affect(this, Berserk.class, new EffectType(0, EffectType.SPIRIT));
				berserk.damage(realDamage);
			}
		}

		int damage = super.damage( dmg, src ,type );
		return damage;
	}
	
	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[ m.pos ] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains( m )) {
					newMob = true;
				}

				if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1){
					if (target == null){
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
				}
			}
		}

		if (target != null && (QuickSlotButton.lastTarget == null ||
							!QuickSlotButton.lastTarget.isAlive() ||
							!fieldOfView[QuickSlotButton.lastTarget.pos])){
			QuickSlotButton.target(target);
		}
		
		if (newMob) {
			interrupt();
			resting = false;
		}

		visibleEnemies = visible;
	}
	
	public int visibleEnemies() {
		return visibleEnemies.size();
	}
	
	public Mob visibleEnemy( int index ) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}
	
	private boolean getCloser( final int target ) {

		if (target == pos)
			return false;

		if (rooted) {
			Camera.main.shake( 1, 1f );
			return false;
		}
		
		int step = -1;
		
		if (Dungeon.level.adjacent( pos, target )) {

			path = null;

			if (Actor.findChar( target ) == null) {
				if (Dungeon.level.pit[target] && !flying && !Dungeon.level.solid[target]) {
					if (!Chasm.jumpConfirmed){
						Chasm.heroJump(this);
						interrupt();
					} else {
						Chasm.heroFall(target);
					}
					return false;
				}
				if (Dungeon.level.passable[target] || Dungeon.level.avoid[target]) {
					step = target;
				}
			}
			
		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				//looks ahead for path validity, up to length-1 or 2.
				//Note that this is shorter than for mobs, so that mobs usually yield to the hero
				int lookAhead = (int) GameMath.gate(0, path.size()-1, 2);
				for (int i = 0; i < lookAhead; i++){
					int cell = path.get(i);
					if (!Dungeon.level.passable[cell] || (fieldOfView[cell] && Actor.findChar(cell) != null)) {
						newPath = true;
						break;
					}
				}
			}

			if (newPath) {

				int len = Dungeon.level.length();
				boolean[] p = Dungeon.level.passable;
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				path = Dungeon.findPath(this, pos, target, passable, fieldOfView);
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {
			
			float speed = speed();
			
			sprite.move(pos, step);
			move(step);

			spend( 1 / speed );
			
			search(false);
			
			if (subClass == HeroSubClass.FREERUNNER){
				Buff.affect(this, Momentum.class).gainStack();
			}

			//FIXME this is a fairly sloppy fix for a crash involving pitfall traps.
			//really there should be a way for traps to specify whether action should continue or
			//not when they are pressed.
			return InterlevelScene.mode != InterlevelScene.Mode.FALL;

		} else {

			return false;
			
		}

	}
	
	public boolean handle( int cell ) {
		
		if (cell == -1) {
			return false;
		}
		
		Char ch;
		Heap heap;
		
		if (Dungeon.level.map[cell] == Terrain.ALCHEMY && cell != pos) {
			
			curAction = new HeroAction.Alchemy( cell );
			
		} else if (fieldOfView[cell] && (ch = Actor.findChar( cell )) instanceof Mob) {

			if (ch instanceof NPC) {
				curAction = new HeroAction.Interact( (NPC)ch );
			} else {
				curAction = new HeroAction.Attack( ch );
			}

		} else if ((heap = Dungeon.level.heaps.get( cell )) != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.size() == 0 || cell == pos ||
				//...but only for standard heaps, chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

			switch (heap.type) {
			case HEAP:
				curAction = new HeroAction.PickUp( cell );
				break;
			case FOR_SALE:
				curAction = heap.size() == 1 && heap.peek().price() > 0 ?
					new HeroAction.Buy( cell ) :
					new HeroAction.PickUp( cell );
				break;
			default:
				curAction = new HeroAction.OpenChest( cell );
			}
			
		} else if (Dungeon.level.map[cell] == Terrain.LOCKED_DOOR || Dungeon.level.map[cell] == Terrain.LOCKED_EXIT) {
			
			curAction = new HeroAction.Unlock( cell );
			
		} else if (cell == Dungeon.level.exit && Dungeon.depth < Dungeon.mode.maxDepth()) {
			
			curAction = new HeroAction.Descend( cell );
			
		} else if (cell == Dungeon.level.entrance) {
			
			curAction = new HeroAction.Ascend( cell );
			
		} else  {
			
			curAction = new HeroAction.Move( cell );
			lastAction = null;
			
		}

		return true;
	}
	
	public void earnExp( int exp ) {
		
		this.exp += exp;
		float percent = exp/(float)maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if (horn != null) horn.gainCharge(percent);
		
		Berserk berserk = buff(Berserk.class);
		if (berserk != null) berserk.recover(percent);
		
		boolean levelUp = false;
		while (this.exp >= maxExp()) {
			this.exp -= maxExp();
			if (lvl < MAX_LEVEL) {
				lvl++;
				levelUp = true;

				updateHT( true );
				//attackSkill++;
				//defenseSkill++;

			} else {
				Buff.prolong(this, Bless.class, 30f);
				this.exp = 0;

				GLog.p( Messages.get(this, "level_cap"));
				Sample.INSTANCE.play( Assets.SND_LEVELUP );
			}
			
		}
		
		if (levelUp) {
			
			GLog.p( Messages.get(this, "new_level"), lvl );
			sprite.showStatus( CharSprite.POSITIVE, Messages.get(Hero.class, "level_up") );
			Sample.INSTANCE.play( Assets.SND_LEVELUP );
			
			Badges.validateLevelReached();
		}
	}
	
	public int maxExp() {
		return maxExp( lvl );
	}

	//5 + lvl * 5
	public static int maxExp( int lvl ){
		//multupler increse to 8 from 1
		return (int)((5 + lvl * 5) * (0.5f + lvl/99f * 7.5));
	}
	
	public boolean isStarving() {
		return buff(Hunger.class) != null && ((Hunger)buff( Hunger.class )).isStarving();
	}
	
	@Override
	public void add( Buff buff ) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		super.add( buff );

		if (sprite != null) {
			String msg = buff.heroMessage();
			if (msg != null){
				GLog.w(msg);
			}

			if (buff instanceof Paralysis || buff instanceof Vertigo) {
				interrupt();
			}

		}
		
		BuffIndicator.refreshHero();
	}
	
	@Override
	public void remove( Buff buff ) {
		super.remove( buff );

		BuffIndicator.refreshHero();
	}
	
	@Override
	public int stealth() {
		int stealth = super.stealth();
		
		if (belongings.armor != null){
			stealth = Math.round(belongings.armor.stealthFactor(this, stealth));
		}
		
		return stealth;
	}
	
	@Override
	public void die( Object cause, EffectType type ) {
		
		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Item item : belongings){
			if (item instanceof Ankh) {
				if (ankh == null || ((Ankh) item).isBlessed()) {
					ankh = (Ankh) item;
				}
			}
		}

		if (ankh != null && ankh.isBlessed()) {
			this.HP = HT/4;

			//ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dieing again.
			Buff.detach(this, Paralysis.class);
			spend(-cooldown());

			new Flare(8, 32).color(0xFFFF66, true).show(sprite, 2f);
			CellEmitter.get(this.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);

			ankh.detach(belongings.backpack);

			Sample.INSTANCE.play( Assets.SND_TELEPORT );
			GLog.w( Messages.get(this, "revive") );
			Statistics.ankhsUsed++;

			return;
		}
		
		Actor.fixTime();
		super.die( cause, type );

		if (ankh == null) {
			
			reallyDie( cause );
			
		} else {
			
			Dungeon.deleteGame( GamesInProgress.curSlot, false );
			GameScene.show( new WndResurrect( ankh, cause ) );
			
		}
	}

	public void dieByDoom(Object cause ){

		curAction = null;
		Actor.fixTime();
		super.die( cause, new EffectType(0,0) );
		reallyDie( cause );

		Dungeon.failByDoom( cause.getClass() );

	}
	
	public static void reallyDie( Object cause ) {
		
		int length = Dungeon.level.length();
		int[] map = Dungeon.level.map;
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;
		
		for (int i=0; i < length; i++) {
			
			int terr = map[i];
			
			if (discoverable[i]) {
				
				visited[i] = true;
				if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
					Dungeon.level.discover( i );
				}
			}
		}
		
		Bones.leave();
		
		Dungeon.observe();
		GameScene.updateFog();
				
		Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<Integer>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Dungeon.level.heaps.get( cell ) == null) {
				passable.add( cell );
			}
		}
		Collections.shuffle( passable );

		ArrayList<Item> items = new ArrayList<Item>( Dungeon.hero.belongings.backpack.items );
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element( items );
			Dungeon.level.drop( item, cell ).sprite.drop( pos );
			items.remove( item );
		}

		GameScene.gameOver();
		
		if (cause instanceof Hero.Doom) {
			((Hero.Doom)cause).onDeath();
		}
		
		Dungeon.deleteGame( GamesInProgress.curSlot, true );
	}

	//effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and concurrent modification implications if that method calls buff(Berserk.class)
	private Berserk berserk;

	@Override
	public boolean isAlive() {
		
		if (HP <= 0){
			if (berserk == null) berserk = buff(Berserk.class);
			return berserk != null && berserk.berserking();
		} else {
			berserk = null;
			return super.isAlive();
		}
	}

	@Override
	public void move( int step ) {
		super.move( step );
		
		if (!flying) {
			if (Dungeon.level.water[pos]) {
				Sample.INSTANCE.play( Assets.SND_WATER, 1, 1, Random.Float( 0.8f, 1.25f ) );
			} else {
				Sample.INSTANCE.play( Assets.SND_STEP );
			}
		}
	}
	
	@Override
	public void onAttackComplete() {
		
		AttackIndicator.target(enemy);
		
		boolean hit = attack( enemy );

		if (subClass == HeroSubClass.GLADIATOR){
			if (hit) {
				Buff.affect( this, Combo.class ).hit();
			} else {
				Combo combo = buff(Combo.class);
				if (combo != null) combo.miss();
			}
		}
		
		Invisibility.dispel();
		spend( totalAttackDelay() );

		curAction = null;

		super.onAttackComplete();
	}
	
	@Override
	public void onOperateComplete() {
		
		if (curAction instanceof HeroAction.Unlock) {

			int doorCell = ((HeroAction.Unlock)curAction).dst;
			int door = Dungeon.level.map[doorCell];

			if (door == Terrain.LOCKED_DOOR){
				Notes.remove(new IronKey(Dungeon.depth));
				Level.set( doorCell, Terrain.DOOR );
			} else {
				Notes.remove(new SkeletonKey(Dungeon.depth));
				Level.set( doorCell, Terrain.UNLOCKED_EXIT );
			}
			GameScene.updateKeyDisplay();
			
			Level.set( doorCell, door == Terrain.LOCKED_DOOR ? Terrain.DOOR : Terrain.UNLOCKED_EXIT );
			GameScene.updateMap( doorCell );
			spend( Key.TIME_TO_UNLOCK );
			
		} else if (curAction instanceof HeroAction.OpenChest) {

			Heap heap = Dungeon.level.heaps.get( ((HeroAction.OpenChest)curAction).dst );
			if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
				Sample.INSTANCE.play( Assets.SND_BONES );
			} else if (heap.type == Type.LOCKED_CHEST){
				Notes.remove(new GoldenKey(Dungeon.depth));
			} else if (heap.type == Type.CRYSTAL_CHEST){
				Notes.remove(new CrystalKey(Dungeon.depth));
			}
			GameScene.updateKeyDisplay();
			heap.open( this );
			spend( Key.TIME_TO_UNLOCK );
		}
		curAction = null;

		super.onOperateComplete();
	}
	
	public boolean search( boolean intentional ) {
		
		if (!isAlive()) return false;
		
		boolean smthFound = false;

		int distance = heroClass == HeroClass.ROGUE ? 2 : 1;
		
		int cx = pos % Dungeon.level.width();
		int cy = pos / Dungeon.level.width();
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Dungeon.level.width()) {
			bx = Dungeon.level.width() - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Dungeon.level.height()) {
			by = Dungeon.level.height() - 1;
		}

		TalismanOfForesight.Foresight foresight = buff( TalismanOfForesight.Foresight.class );
		boolean cursed = foresight != null && foresight.isCursed();
		
		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {
				
				if (fieldOfView[p] && p != pos) {
					
					if (intentional) {
						sprite.parent.addToBack( new CheckedCell( p ) );
					}
					
					if (Dungeon.level.secret[p]){
						
						float chance;
						//intentional searches always succeed
						if (intentional){
							chance = 1f;
						
						//unintentional searches always fail with a cursed talisman
						} else if (cursed) {
							chance = 0f;
							
						//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						} else if (Dungeon.level.map[p] == Terrain.SECRET_TRAP) {
							chance = 0.4f - (Dungeon.depth / 250f);
							
						//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						} else {
							chance = 0.2f - (Dungeon.depth / 100f);
						}
						
						if (Random.Float() < chance) {
						
							int oldValue = Dungeon.level.map[p];
							
							GameScene.discoverTile( p, oldValue );
							
							Dungeon.level.discover( p );
							
							ScrollOfMagicMapping.discover( p );
							
							smthFound = true;
	
							if (foresight != null && !foresight.isCursed())
								foresight.charge();
						}
					}
				}
			}
		}

		
		if (intentional) {
			sprite.showStatus( CharSprite.DEFAULT, Messages.get(this, "search") );
			sprite.operate( pos );
			if (cursed){
				GLog.n(Messages.get(this, "search_distracted"));
				buff(Hunger.class).reduceHunger(TIME_TO_SEARCH - (2*HUNGER_FOR_SEARCH));
			} else {
				buff(Hunger.class).reduceHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
			}
			spendAndNext(TIME_TO_SEARCH);
			
		}
		
		if (smthFound) {
			GLog.w( Messages.get(this, "noticed_smth") );
			Sample.INSTANCE.play( Assets.SND_SECRET );
			interrupt();
		}
		
		return smthFound;
	}
	
	public void resurrect( int resetLevel ) {
		
		HP = HT;
		Dungeon.gold = 0;
		exp = 0;
		
		belongings.resurrect( resetLevel );

		live();
	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	public static interface Doom {
		public void onDeath();
	}
}
