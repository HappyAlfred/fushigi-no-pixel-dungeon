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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon;

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfFuror;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Annoying;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Displacing;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Exhausting;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Fragile;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Friendly;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Retreating;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Sacrificial;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Wayward;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Blazing;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Chilling;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Dazzling;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Eldritch;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Energy;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Ghostly;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Projecting;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Shocking;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Stunning;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Vampiric;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Venomous;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Vorpal;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Assassination;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.BalanceAttack;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndEnchantmentsTab;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.Collection;

abstract public class Weapon extends KindOfWeapon {

	private static final int HITS_TO_KNOW    = 10;

	protected static final int UPGRADE_ATTACK    = 3;

	protected static final String AC_ENCHANTMENTLIST       = "ENCHANTMENTLIST";
	protected static final String AC_PROPERTYLIST       = "PROPERTYLIST";
	public float    ACC = 1f;	// Accuracy modifier
	public float	DLY	= 1f;	// Speed modifier
	public int      RCH = 1;    // Reach modifier (only applies to melee hits)
	public int 	LIMIT = 2; 		//enchantment count limit

	public enum Augment {
		SPEED   (0.7f, 0.67f),
		DAMAGE  (1.5f, 1.67f),
		NONE	(1.0f, 1.00f);

		private float damageFactor;
		private float delayFactor;

		Augment(float dmg, float dly){
			damageFactor = dmg;
			delayFactor = dly;
		}

		public int damageFactor(int dmg){
			return Math.round(dmg * damageFactor);
		}

		public float delayFactor(float dly){
			return dly * delayFactor;
		}
	}
	
	public Augment augment = Augment.NONE;

	private int hitsToKnow = HITS_TO_KNOW;
	
	public ArrayList<Enchantment>  enchantment = new ArrayList<>();

	public ArrayList<Enchantment>  properties = new ArrayList<>();

	@Override
	public int damageRoll(Char owner) {
		int damage = 0;
		if(hasEnchant(Assassination.class)){
			/*
			deals 100% toward max to max on surprise, instead of min to max., decrease with level
			level == 5,factor = 0.75
			level == 10,factor = 0.6
			level >= 15 factor = 0.5
			 */
			Char enemy = owner.enemy();
			if (enemy != null && ((enemy instanceof Mob && ((Mob) enemy).surprisedBy(owner))
					|| (!(enemy instanceof Mob) && owner.canSurpriseAttack(enemy)))) {
				float factor = Assassination.minFactor(this);

				int diff = max() - min();
				damage = Random.NormalIntRange(
						min() + Math.round(diff*factor),
						max());
			}else{
				damage = super.damageRoll(owner);
			}
			/*
			if (owner instanceof Hero) {
				Hero hero = (Hero)owner;
				Char enemy = hero.enemy();
				if (enemy != null && enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)) {
					int lvl = Math.max(level() , 0);
					float factor = Assassination.minFactor(this);

					int diff = max() - min();
					damage = Random.NormalIntRange(
							min() + Math.round(diff*factor),
							max());
				int exStr = hero.STR() - STRReq();
				if (exStr > 0) {
					damage += Random.IntRange(0, exStr);
				}
				}else{
					damage = super.damageRoll(owner);
				}
			}
			*/

		}else {
			damage = super.damageRoll(owner);
		}

		/*
		if(hasEnchant(FirstStrike.class)){
			Char enemy = null;
			if (owner instanceof Hero) {
				enemy = ((Hero) owner).enemy();
			}
			if (owner instanceof Mob) {
				enemy = ((Mob) owner).enemy();
			}
			if(enemy != null && enemy.HT >= enemy.HP){
				damage *= FirstStrike.strikeFactor(this);
			}
		}
		*/

		return damage;
	}

    @Override
    public int max(){
        int max = super.max();
        if (hasEnchant(BalanceAttack.class))
            max = (max + super.min())/2;
        return max;
    }

    @Override
    public int min(){
        int max = max();
        int min = super.min();
        if (hasEnchant(BalanceAttack.class))
            min = max;
        return min;
    }

    @Override
    public int maxUnIdentified(){
        int max = super.maxUnIdentified();
        if (hasEnchant(BalanceAttack.class))
            max = (max + super.minUnIdentified())/2;
        return max;
    }

    @Override
    public int minUnIdentified(){
        int max = maxUnIdentified();
        int min = super.minUnIdentified();
        if (hasEnchant(BalanceAttack.class))
            min = max;
        return min;
    }

	public boolean canCriticalAttack( Char attacker, Char defender, int damage, EffectType type ){
		boolean critFlag = super.canCriticalAttack( attacker, defender, damage, type);
		if (!properties.isEmpty()) {
			for(Enchantment e : properties){
				critFlag |= e.canCriticalAttack(this, attacker, defender, damage, type);
			}
		}

		if (enchantmentCount() != 0) {
			for(Enchantment e : enchantment){
				critFlag |= e.canCriticalAttack(this, attacker, defender, damage, type);
			}
		}
		return critFlag;
	}

	@Override
	public int proc( Char attacker, Char defender, int damage, EffectType type ) {

		if (!properties.isEmpty()) {
			for(Enchantment e : properties){
				damage *= e.proc(this, attacker, defender, damage, type);
			}
		}

		if (enchantmentCount() != 0) {
			for(Enchantment e : enchantment){
				damage *= e.proc(this, attacker, defender, damage, type);
			}
		}

		//Evan's code is SHIT
		if (!levelKnown && attacker instanceof Hero) {
			if (--hitsToKnow <= 0) {
				identify();
				GLog.i( Messages.get(Weapon.class, "identify") );
				Badges.validateItemLevelAquired( this );
			}
		}

		return damage;
	}

	@Override
	public void onMissed( Char attacker, Char defender) {

		for(Enchantment e : properties){
			e.onMissed(this, attacker, defender);
		}
		for(Enchantment e : enchantment){
			e.onMissed(this, attacker, defender);
		}
	}

	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String ENCHANTMENT		= "enchantment";
	private static final String AUGMENT			= "augment";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		SparseArray list = new SparseArray();
		for(Enchantment gl :enchantment){
			list.put(enchantment.indexOf(gl),gl);

		}
		bundle.put( ENCHANTMENT, list.values() );
		bundle.put( AUGMENT, augment );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		hitsToKnow = bundle.getInt( UNFAMILIRIARITY );
		Collection<Bundlable> collection = bundle.getCollection( ENCHANTMENT );
		for (Bundlable g : collection) {
			Enchantment gl = (Enchantment) g;
			enchantment.add(gl);
		}
		
		//pre-0.6.5 saves
		if (bundle.contains( "imbue" )){
			String imbue = bundle.getString( "imbue" );
			if (imbue.equals( "LIGHT" ))        augment = Augment.SPEED;
			else if (imbue.equals( "HEAVY" ))   augment = Augment.DAMAGE;
			else                                augment = Augment.NONE;
		} else {
			augment = bundle.getEnum(AUGMENT, Augment.class);
		}
	}


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (cursedKnown && enchantmentCount() != 0) actions.add(AC_ENCHANTMENTLIST);
		if (properties.size() > 0) actions.add(AC_PROPERTYLIST);
		return actions;
	}

	@Override
	public void execute(Char hero, String action) {

		super.execute(hero, action);

		if (cursedKnown && enchantmentCount() != 0 && action.equals(AC_ENCHANTMENTLIST)){
			GameScene.show(new WndEnchantmentsTab(this , null, WndEnchantmentsTab.Mode.ENCHANTMENT, Messages.get(WndEnchantmentsTab.class, "enchantment")));
		}else if(properties.size() > 0 && action.equals(AC_PROPERTYLIST)){
			GameScene.show(new WndEnchantmentsTab(this , null, WndEnchantmentsTab.Mode.PROPERTY, Messages.get(Item.class, "properties")));
		}
	}
	
	@Override
	public float accuracyFactor( Char owner ,Char target) {
		
		int encumbrance = 0;
		
		/*if( owner instanceof Hero ){
			encumbrance = STRReq() - ((Hero)owner).STR();
		}*/

		float adapt = 1f;

		for(Enchantment e : properties){
			adapt *= e.accuracyAdapt(this ,owner ,target, ACC);
			if(adapt > 1000) adapt = 1000;
		}
		for(Enchantment e : enchantment){
			adapt *= e.accuracyAdapt(this ,owner , target, ACC);
			if(adapt > 1000) adapt = 1000;
		}

		float ACC = this.ACC * adapt;

		ACC = encumbrance > 0 ? (float)(ACC / Math.pow( 1.5, encumbrance )) : ACC;

		return ACC;
	}
	
	@Override
	public float speedFactor( Char owner ) {

		int encumbrance = 0;
		/*if (owner instanceof Hero) {
			encumbrance = STRReq() - ((Hero)owner).STR();
		}*/

		float DLY = augment.delayFactor(this.DLY);

		DLY = RingOfFuror.modifyAttackDelay(DLY, owner);

		return (encumbrance > 0 ? (float)(DLY * Math.pow( 1.2, encumbrance )) : DLY);
	}

	@Override
	public int reachFactor(Char owner) {
		return hasEnchant(Projecting.class) ? RCH+1 : RCH;
	}

    @Override
    public boolean canAttack( Char attacker, Char defender){

	    if(super.canAttack(attacker, defender)){
	       return true;
        }
        if(hasEnchant(Ghostly.class)){
            if (Dungeon.level.distance( attacker.pos, defender.pos ) <= reachFactor(attacker)){
                return true;
            }
        }
        /*
        if (Dungeon.level.distance( attacker.pos, defender.pos ) <= reachFactor(attacker)){

            boolean[] passable = BArray.not(Dungeon.level.solid, null);
            for (Mob m : Dungeon.level.mobs)
                passable[m.pos] = false;

            PathFinder.buildDistanceMap(defender.pos, passable, reachFactor(attacker));

            return PathFinder.distance[attacker.pos] <= reachFactor(attacker);

        } else {
            return false;
        }
        */
        return false;
    }

	/*public int STRReq(){
		return STRReq(level());
	}

	public abstract int STRReq(int lvl);*/
	
	@Override
	public Item upgrade() {
		return upgrade(false);
	}
	
	public Item upgrade(boolean enchant ) {

		if (enchant && (!hasAllEnchant() || enchantment.size() < LIMIT)){
			enchant();
		}
		
		cursed = false;
		
		return super.upgrade();
	}

	public boolean isDegradeable() {
		int lvl = level() - 1;
		return max(lvl) >= 0 && min(lvl) >= 0;
	}
	
	@Override
	public Item random() {
		int n = 0;
		//+0: 75% (3/4)
		//+1: 20% (4/20)
		//+2: 5%  (1/20)
		/*if (Random.Int(4) == 0) {
			n++;
			if (Random.Int(5) == 0) {
				n++;
			}
		}*/

		//+0: 42.2% (19/45)
		//+1: 40% (2/5)
		//+2: 13% (2/15)
		//+3: 4%  (2/45)
		if(Random.Int(5) < 2){
			n++;
			if(Random.Int(3) == 0){
				n++;
				if(Random.Int(3) == 0){
					n++;
				}
			}
		}
		level(n);
		
		//30% chance to be cursed
		//10% chance to be enchanted
		float effectRoll = Random.Float();
		if (effectRoll < 0.1f) {
			enchant(Enchantment.randomCurse());
			cursed = true;
		} else if (effectRoll >= 0.9f){
			enchant();
		}

		return this;
	}
	
	public Weapon enchant( Enchantment ench ) {
		if (!canEnchant(null)) {
			GLog.i(Messages.get(this, "full_enchantment"));
		}
		else if(!canEnchant(ench.getClass())){
			GLog.i(Messages.get(this, "has_enchantment"));
		}
		else{
			enchantment.add(ench);
		}
		return this;
	}

	public Weapon enchant() {

		Enchantment ench = Enchantment.random();
		if(!canEnchant(null)){
			return this;
		}
		while (!canEnchant(ench.getClass())) {
			ench = Enchantment.random();
		}
		return enchant( ench );
	}

	public Item fusion(Item item) {
		super.fusion(item);
		if(item instanceof Weapon) {
			Weapon weapon = (Weapon) item;
			if (weapon.enchantmentCount() != 0 || weapon.properties.size() != 0) {
				for (Weapon.Enchantment e : weapon.properties) {
					e.curseToEnchantment();
					if (canEnchant(e.getClass()))
						enchantment.add(e);
					else if(enchantmentCount() >= LIMIT)break;
				}
				for (Weapon.Enchantment e : weapon.enchantment) {
					e.curseToEnchantment();
					if (canEnchant(e.getClass()))
						enchantment.add(e);
					else if(enchantmentCount() >= LIMIT)break;
				}
			}
		}
		return this;
	}

	public Weapon enchantmentClear() {

		if (enchantmentCount() > 0) {
			enchantment.clear();
		}
		return this;
	}

	public boolean hasAllEnchant() {
		if (enchantmentCount() != 0 || properties.size() != 0){
			Class<?>[] elist = Enchantment.getAllenchantments();
			for(int i=0 ; i < elist.length ; i++) {
				boolean isexist = false;
				for (Enchantment e : properties) {
					if (e.getClass() == elist[i])
						isexist = true;
				}
				for (Enchantment e : enchantment) {
					if (e.getClass() == elist[i])
					isexist = true;
				}
				if(!isexist){
					return false;
				}
			}
			return  true;
		}
		return false;
	}

	public int enchantmentCount(){
	    int i = 0;
	    for(Enchantment en : enchantment){
	        if(en != null){
	            i++;
            }
        }
		return i;
	}

	public Enchantment getEnchantment(Class<?extends Enchantment> type){
		for(Enchantment e : properties){
			if (e.getClass() == type)return e;
		}
		for(Enchantment e : enchantment){
			if (e.getClass() == type)return e;
		}
		return null;
	}

	public boolean hasEnchant(Class<?extends Enchantment> type) {
		for(Enchantment e : properties){
			if (e.getClass() == type)return true;
		}
		for(Enchantment e : enchantment){
			if (e.getClass() == type)return true;
		}
		return false;
	}

	public boolean hasProperty(Class<?extends Enchantment> type) {
		for(Enchantment e : properties){
			if (e.getClass() == type)return true;
		}
		return false;
	}

	public boolean canEnchant(Class<?extends Enchantment> type) {
		if(enchantmentCount() < LIMIT) {
			if (type == null) {
				if(!hasAllEnchant()) {
					return true;
				}
			}else if(!hasEnchant(type)) {
				return true;
			}
		}
		return false;
	}

	public boolean hasGoodEnchant(){
		if (enchantmentCount() != 0){
			for(Enchantment e : enchantment){
				if (!e.curse())return true;
			}
		}
		return false;
	}

	public boolean hasCurseEnchant(){
		if (enchantmentCount() != 0){
			for(Enchantment e : enchantment){
				if (e.curse())return true;
			}
		}
		return false;
	}

	public static abstract class Enchantment implements Bundlable {

		private static final Class<?>[] enchants = new Class<?>[]{
			Blazing.class, Venomous.class, Vorpal.class, Shocking.class,
			Chilling.class, Eldritch.class, Projecting.class, Dazzling.class, Energy.class, Ghostly.class,
			Grim.class, Stunning.class, Vampiric.class,};
		/*
        private static final Class<?>[] enchants = new Class<?>[]{
                Blazing.class, Venomous.class, Vorpal.class, Shocking.class,
                Chilling.class, Eldritch.class, Projecting.class, Dazzling.class, Energy.class,
                Grim.class, Stunning.class, Vampiric.class,};
		private static final float[] chances= new float[]{
			10, 10, 10, 10,
			5, 5, 5, 5, 5,
			2, 2, 2 };*/
		private static final float[] chances= new float[]{
				10, 10, 10,
				5, 5, 5, 5, 5, 5,
				5, 5, 5 };

		private static final Class<?>[] curses = new Class<?>[]{
				Annoying.class, Displacing.class, Exhausting.class, Fragile.class,
				Sacrificial.class, Wayward.class, Friendly.class, Retreating.class
		};

		public boolean canCriticalAttack( Weapon weapon, Char attacker, Char defender, int damage , EffectType type){
			return false;
		}
			
		public abstract float proc( Weapon weapon, Char attacker, Char defender, int damage , EffectType type );

		public String name() {
			if (!curse())
				return name( Messages.get(this, "enchant"));
			else
				return name( Messages.get(Item.class, "curse"));
		}
		public String nameProperties() {
			return name(Messages.get(Item.class, "properties"));
		}

		public float accuracyAdapt(Weapon weapon, Char attacker ,Char target ,float ACC){
			return 1;
		}

		public static Class<?>[] getAllenchantments(){
			return enchants;
		}

		public static Class<?>[] getAllcurses(){
			return curses;
		}

		public String name( String weaponName ) {
			return Messages.get(this, "name", weaponName);
		}


		public String desc(Weapon weapon) {
			return Messages.get(this, "desc");
		}

		public void onMissed(Weapon weapon, Char attacker, Char defender) {
			return;
		}

		public boolean curse = false;
		public boolean curse() {
			return curse;
		}

		public boolean curseToEnchantment() {
			if(curse){
				curse = false;
				return true;
			}
			else return false;
		}

		private static final String CURSE	= "curse";

		@Override
		public void storeInBundle( Bundle bundle ) {
			bundle.put(CURSE, curse);
		}

		@Override
		public void restoreFromBundle( Bundle bundle ) {
			curse = bundle.getBoolean(CURSE);
		}
		
		public abstract ItemSprite.Glowing glowing();
		
		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ((Class<Enchantment>)enchants[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}

		@SuppressWarnings("unchecked")
		public static Enchantment randomCurse(){
			try {
				return ((Class<Enchantment>)Random.oneOf(curses)).newInstance();
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}
		
	}
}
