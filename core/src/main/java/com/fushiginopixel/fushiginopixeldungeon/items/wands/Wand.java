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

package com.fushiginopixel.fushiginopixeldungeon.items.wands;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Recharging;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.SoulMark;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroClass;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroSubClass;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.ItemStatusHandler;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.MagicalHolster;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfEnergy;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.journal.Catalog;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Wand extends Item {

	private static final int USAGES_TO_KNOW    = 10;

	public static final String AC_ZAP	= "ZAP";

	private static final float TIME_TO_ZAP	= 1f;

	protected Integer initials;

	private static final Class<?>[] wands = {
			WandOfBlastWave.class,
			WandOfCorrosion.class,
			WandOfCorruption.class,
			WandOfDisintegration.class,
			WandOfElements.class,
			WandOfFireblast.class,
			//WandOfFrost.class,
			WandOfMagician.class,
			WandOfLightning.class,
			WandOfMagicMissile.class,
			WandOfNightmare.class,
			WandOfPrismaticLight.class,
			//WandOfRegrowth.class,
			WandOfKaleidoscope.class,
			//WandOfTransfusion.class,
			WandOfHoly.class,
	};

	private static final HashMap<String, Integer> spells = new HashMap<String, Integer>() {
		{
			put("magic_missile", ItemSpriteSheet.WAND_MAGIC_MISSILE);
			put("firebolt",ItemSpriteSheet.WAND_FIREBOLT);
			put("frost",ItemSpriteSheet.WAND_FROST);
			put("lightning",ItemSpriteSheet.WAND_LIGHTNING);
			put("disintegration",ItemSpriteSheet.WAND_DISINTEGRATION);
			put("prismatic_light",ItemSpriteSheet.WAND_PRISMATIC_LIGHT);
			put("corrosion",ItemSpriteSheet.WAND_CORROSION);
			put("living_earth",ItemSpriteSheet.WAND_LIVING_EARTH);
			put("blast_wave",ItemSpriteSheet.WAND_BLAST_WAVE);
			put("corruption",ItemSpriteSheet.WAND_CORRUPTION);
			put("warding",ItemSpriteSheet.WAND_WARDING);
			put("regrowth",ItemSpriteSheet.WAND_REGROWTH);
			put("transfusion",ItemSpriteSheet.WAND_TRANSFUSION);
		}
	};

	private static ItemStatusHandler<Wand> handler;

	private String spell;

	public static final int MAXCHARGE = 100;

	public int maxCharges = MAXCHARGE;
	public int curCharges = initialCharges();
	public float partialCharge = 0f;
	
	protected Charger charger;
	
	private boolean curChargeKnown = false;

	protected int usagesToKnow = USAGES_TO_KNOW;

	protected int collisionProperties = Ballistica.MAGIC_BOLT;
	
	{
		defaultAction = AC_ZAP;
		usesTargeting = true;
	}

	@SuppressWarnings("unchecked")
	public static void initSpells() {
		handler = new ItemStatusHandler<>( (Class<? extends Wand>[])wands, spells );
	}

	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		handler.saveSelectively( bundle, items );
	}

	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Wand>[])wands, spells, bundle );
	}

	public Wand() {
		super();
		reset();
	}

	@Override
	public void reset(){
		super.reset();
		if (handler != null) {
			image = handler.image(this);
			spell = handler.label(this);
		}
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (curCharges > 0 || !curChargeKnown) {
			actions.add( AC_ZAP );
		}

		return actions;
	}
	
	@Override
	public void execute( Char hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_ZAP )) {
			
			curUser = hero;
			curItem = this;
			GameScene.selectCell( zapper );
			
		}
	}
	
	protected abstract void onZap( Ballistica attack );

	public abstract void onHit( MagesStaff staff, Char attacker, Char defender, int damage, EffectType type);

	/*
	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null) {
				if (container instanceof MagicalHolster)
					charge( container.owner, ((MagicalHolster) container).HOLSTER_SCALE_FACTOR );
				else
					charge( container.owner );
			}
			return true;
		} else {
			return false;
		}
	}
	*/
	
	public void gainCharge( float amt ){
		partialCharge += amt;
		while (partialCharge >= 1) {
			curCharges = Math.min(maxCharges, curCharges+1);
			partialCharge--;
			updateQuickslot();
		}
	}
	
	public void charge( Char owner ) {
		if (charger == null) charger = new Charger();
		charger.attachTo( owner);
	}

	public void charge( Char owner, float chargeScaleFactor ){
		charge( owner );
		charger.setScaleFactor( chargeScaleFactor );
	}

	protected void processSoulMark(Char target, int chargesUsed){
		if (target != Dungeon.hero &&
				Dungeon.hero.subClass == HeroSubClass.WARLOCK
				//Random.Float() > Math.pow(0.9f, (level()*chargesUsed)+1)
				){
			SoulMark.prolong(target, SoulMark.class, SoulMark.DURATION + level(),new EffectType(EffectType.MAGICAL_BOLT,EffectType.SPIRIT));
		}
	}

	@Override
	public void onDetach(Bag container ) {
		stopCharging();
	}

	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}
	
	public void level( int value) {
		super.level( value );
		updateLevel();
	}

	public boolean isKnown() {
		return handler.isKnown( this );
	}

	public void setKnown() {
		if (!isKnown()) {
			handler.know(this);
			updateQuickslot();
		}

		if (Dungeon.hero.isAlive()) {
			Catalog.setSeen(getClass());
		}
	}

	public void knownByUse(){
		if (!isKnown()) {
			GLog.i(Messages.get(this,"identify",this.name));
		}
		setKnown();
	}

	public void setForgot() {
		if (isKnown()) {
			handler.forget(this);
			updateQuickslot();
		}

		if (Dungeon.hero.isAlive()) {
			Catalog.setNoSeen(getClass());
		}
	}

	@Override
	public Item identify() {

		curChargeKnown = true;
		setKnown();
		
		updateQuickslot();
		
		return super.identify();
	}

	@Override
	public Item forget() {

		curChargeKnown = false;


		updateQuickslot();

		return super.forget();
	}

	@Override
	public String name() {
		return isKnown() || !guessedName.equals("") ? super.name() : Messages.get(Wand.class, spell);
	}

	@Override
	public String info() {
		String desc = "";
		if(isKnown()) {
			//desc += desc();
			desc += "\n\n" + statsDesc();
		}else {
			desc += Messages.get(Wand.class, "unknown_desc");
		}
		if (cursed && cursedKnown)
			desc += "\n\n" + Messages.get(Wand.class, "cursed");

		return desc;
	}

	public String statsDesc(){
		return Messages.get(this, "stats_desc");
	};

	public Integer initials(){
		return isKnown() ? initials : null;
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && curChargeKnown;
	}

	public static HashSet<Class<? extends Wand>> getKnown() {
		return handler.known();
	}

	public static HashSet<Class<? extends Wand>> getUnknown() {
		return handler.unknown();
	}

	public static boolean allKnown() {
		return handler.known().size() == wands.length;
	}
	
	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
		} else {
			return null;
		}
	}
	
	@Override
	public Item upgrade() {

		super.upgrade();

		if (Random.Float() > Math.pow(0.8, level())) {
			cursed = false;
		}

		updateLevel();
		curCharges = Math.min( curCharges + 1, maxCharges );
		updateQuickslot();
		
		return this;
	}

	@Override
	public Item fusion(Item item){
		if(item instanceof Wand) {
			curCharges = Math.min(curCharges + ((Wand) item).curCharges, maxCharges);
		}
		return super.fusion(item);
	}
	
	@Override
	public Item degrade() {
		super.degrade();
		
		updateLevel();
		updateQuickslot();
		
		return this;
	}
	
	public void updateLevel() {
		//maxCharges = Math.min( initialCharges() + level(), 10 );
		curCharges = Math.min( curCharges, maxCharges );
	}
	
	public int initialCharges() {
		return Random.Int(5,9);
	}

	protected int chargesPerCast() {
		return 1;
	}
	
	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	public void staffFx( MagesStaff.StaffParticle particle ){
		particle.color(0xFFFFFF); particle.am = 0.3f;
		particle.setLifespan( 1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f );
		particle.radiateXY(0.5f);
	}

	protected void wandUsed() {
		usagesToKnow -= cursed ? 1 : chargesPerCast();
		curCharges -= cursed ? 1 : chargesPerCast();
		if (!isIdentified() && usagesToKnow <= 0) {
			identify();
			GLog.w( Messages.get(Wand.class, "identify", name()) );
		} else {
			//if (curUser.heroClass == HeroClass.MAGE) levelKnown = true;
			if(((Hero)curUser).heroClass == HeroClass.MAGE && !isIdentified()){
				identify();
			}
			updateQuickslot();
		}

		curUser.spendAndNext( TIME_TO_ZAP );
	}
	
	@Override
	public Item random() {
		//+0: 66.67% (2/3)
		//+1: 26.67% (4/15)
		//+2: 6.67%  (1/15)
		int n = 0;
		if (Random.Int(3) == 0) {
			n++;
			if (Random.Int(5) == 0){
				n++;
			}
		}
		level(n);
		
		//30% chance to be cursed
		//if (Random.Float() < 0.3f) {
		if (Random.Float() < 0.1f) {
			cursed = true;
		}

		return this;
	}
	
	@Override
	public int price() {
		//int price = 75;
		int price = 30;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level() > 0) {
				price *= (level() + 1);
			} else if (level() < 0) {
				price /= (1 - level());
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	private static final String UNFAMILIRIARITY     = "unfamiliarity";
	private static final String CUR_CHARGES			= "curCharges";
	private static final String CUR_CHARGE_KNOWN	= "curChargeKnown";
	private static final String PARTIALCHARGE 		= "partialCharge";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, usagesToKnow );
		bundle.put( CUR_CHARGES, curCharges );
		bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
		bundle.put( PARTIALCHARGE , partialCharge );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		curCharges = bundle.getInt( CUR_CHARGES );
		curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN );
		partialCharge = bundle.getFloat( PARTIALCHARGE );
	}
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {
				
				//FIXME this safety check shouldn't be necessary
				//it would be better to eliminate the curItem static variable.
				final Wand curWand;
				if (curItem instanceof Wand) {
					curWand = (Wand) Wand.curItem;
				} else {
					return;
				}

				final Ballistica shot = new Ballistica( curUser.pos, target, curWand.collisionProperties);
				int cell = shot.collisionPos;
				
				if (target == curUser.pos || cell == curUser.pos) {
					GLog.i( Messages.get(Wand.class, "self_target") );
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));
				
				if (curWand.curCharges >= (curWand.cursed ? 1 : curWand.chargesPerCast())) {
					
					curUser.busy();

					if (curWand.cursed){
						CursedWand.cursedZap(curWand, curUser, new Ballistica( curUser.pos, target, Ballistica.MAGIC_BOLT));
						if (!curWand.cursedKnown){
							curWand.cursedKnown = true;
							GLog.n(Messages.get(Wand.class, "curse_discover", curWand.name()));
						}
					} else {
						curWand.fx(shot, new Callback() {
							public void call() {
								curWand.onZap(shot);
								curWand.wandUsed();
							}
						});
					}
					
					Invisibility.dispel(curUser);
					
				} else {

					GLog.w( Messages.get(Wand.class, "fizzles") );

				}
				
			}
		}
		
		@Override
		public String prompt() {
			return Messages.get(Wand.class, "prompt");
		}
	};
	
	public class Charger extends Buff {
		
		private static final float BASE_CHARGE_DELAY = 10f;
		//private static final float SCALING_CHARGE_ADDITION = 40f;
		private static final float SCALING_CHARGE_ADDITION = 250f;
		private static final float NORMAL_SCALE_FACTOR = 0.875f;

		private static final float CHARGE_BUFF_BONUS = 0.25f;

		float scalingFactor = NORMAL_SCALE_FACTOR;
		
		@Override
		public boolean attachTo( Char target, EffectType type ) {
			super.attachTo( target,type );
			
			return true;
		}
		
		@Override
		public boolean act() {
			if (curCharges < maxCharges)
				recharge();
			
			if (partialCharge >= 1 && curCharges < maxCharges) {
				partialCharge--;
				curCharges++;
				updateQuickslot();
			}
			
			spend( TICK );
			
			return true;
		}

		private void recharge(){
			/*
			int missingCharges = maxCharges - curCharges;
			int ringCharges = Ring.getBonus(target, RingOfEnergy.Energy.class);
			missingCharges += ringCharges;
			missingCharges = Math.max(0, missingCharges);

			float turnsToCharge = (float) (BASE_CHARGE_DELAY
					+ (SCALING_CHARGE_ADDITION * Math.pow(scalingFactor, missingCharges)));
			*/
			float turnsToCharge = (float) (BASE_CHARGE_DELAY
					+ (SCALING_CHARGE_ADDITION * scalingFactor));

			/*
			if(ringCharges > 5){
				turnsToCharge *= 0.5f;
			}

			LockedFloor lock = target.buff(LockedFloor.class);
			if (lock == null || lock.regenOn())
				partialCharge += 1f/turnsToCharge;
			*/
			LockedFloor lock = target.buff(LockedFloor.class);
			if ((lock == null || lock.regenOn()) && target instanceof Hero && ((Hero)target).subClass == HeroSubClass.WARLOCK )
				partialCharge += 1f/turnsToCharge;

			for (Recharging bonus : target.buffs(Recharging.class)){
				if (bonus != null && bonus.remainder() > 0f) {
					partialCharge += CHARGE_BUFF_BONUS * bonus.remainder();
				}
			}
		}

		public void gainCharge(float charge){
			partialCharge += charge;
			while (partialCharge >= 1f){
				curCharges++;
				partialCharge--;
			}
			curCharges = Math.min(curCharges, maxCharges);
			updateQuickslot();
		}

		private void setScaleFactor(float value){
			this.scalingFactor = value;
		}
	}
}
