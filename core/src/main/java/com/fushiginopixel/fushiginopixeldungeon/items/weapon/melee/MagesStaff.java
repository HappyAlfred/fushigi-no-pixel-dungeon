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
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroClass;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroSubClass;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ElmoParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRecharging;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfCorrosion;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfCorruption;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfDisintegration;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfRegrowth;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndItem;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagesStaff extends MeleeWeapon {

	public Wand wand;

	public static final String AC_IMBUE = "IMBUE";
	public static final String AC_ZAP	= "ZAP";

	private static final float STAFF_SCALE_FACTOR = 0.75f;

	{
		image = ItemSpriteSheet.MAGES_STAFF;

		tier = 1;

		defaultAction = AC_ZAP;
		usesTargeting = true;

		unique = true;
		bones = false;
		classlimit = HeroClass.MAGE;
		LIMIT = 3;
	}

	public MagesStaff() {
		wand = null;
	}

	@Override
	public int max(int lvl) {
		return  9 +    //8 base damage, down from 10
				lvl*(UPGRADE_ATTACK -1);   //scaling unaffected
	}

	public MagesStaff(Wand wand){
		this();
		wand.identify();
		wand.cursed = false;
		this.wand = wand;
		wand.maxCharges = Wand.MAXCHARGE;//Math.min(wand.maxCharges + 1, 10);
		wand.curCharges = 10;//wand.maxCharges;
		name = Messages.get(wand, "staff_name");
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add(AC_IMBUE);
		if (wand!= null && wand.curCharges > 0) {
			actions.add( AC_ZAP );
		}
		return actions;
	}

	@Override
	public void activate( Char ch ) {
		if(wand != null) wand.charge( ch, STAFF_SCALE_FACTOR );
	}

	@Override
	public void execute(Char hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_IMBUE)) {

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.WAND, Messages.get(this, "prompt"));

		} else if (action.equals(AC_ZAP)){

			if (wand == null) {
				GameScene.show(new WndItem(null, this, true));
				return;
			}

			wand.execute(hero, AC_ZAP);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage, EffectType type) {
		if (wand != null && attacker instanceof Hero && ((Hero)attacker).subClass == HeroSubClass.BATTLEMAGE) {
			if (wand.curCharges < 10) wand.partialCharge += 0.25f;
			ScrollOfRecharging.charge(attacker);
			wand.onHit(this, attacker, defender, damage, type);
		}
		return super.proc(attacker, defender, damage, type);
	}

	@Override
	public int reachFactor(Char owner) {
		int reach = super.reachFactor(owner);
		if (owner instanceof Hero
				&& wand instanceof WandOfDisintegration
				&& ((Hero)owner).subClass == HeroSubClass.BATTLEMAGE){
			reach++;
		}
		return reach;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)) {
			if (container.owner != null && wand != null) {
				wand.charge(container.owner, STAFF_SCALE_FACTOR);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach(Bag container ) {
		if (wand != null) wand.stopCharging();
	}

	public Item imbueWand(Wand wand, Char owner){

		wand.cursed = false;

        int charges = 0;
		if(this.wand != null){
            charges = this.wand.curCharges;
        }

		this.wand = null;

		//syncs the level of the two items.
		int targetLevel = Math.max(this.level(), wand.level());

		//if the staff's level is being overridden by the wand, preserve 1 upgrade
		if (wand.level() >= this.level() && this.level() > 0) targetLevel++;

		int staffLevelDiff = targetLevel - this.level();
		if (staffLevelDiff > 0)
			this.upgrade(staffLevelDiff);
		else if (staffLevelDiff < 0)
			this.degrade(Math.abs(staffLevelDiff));

		int wandLevelDiff = targetLevel - wand.level();
		if (wandLevelDiff > 0)
			wand.upgrade(wandLevelDiff);
		else if (wandLevelDiff < 0)
			wand.degrade(Math.abs(wandLevelDiff));

		this.wand = wand;
		wand.maxCharges = Wand.MAXCHARGE;//Math.min(wand.maxCharges + 1, 10);
		//wand.curCharges = wand.maxCharges;
		//wand.curCharges = Math.min(Math.max(charges,wand.curCharges + 1) , wand.maxCharges);
		wand.curCharges = Math.min(charges + wand.curCharges , wand.maxCharges);
		wand.identify();
		if (owner != null) wand.charge(owner);

		name = Messages.get(wand, "staff_name");

		//This is necessary to reset any particles.
		//FIXME this is gross, should implement a better way to fully reset quickslot visuals
		int slot = Dungeon.quickslot.getSlot(this);
		if (slot != -1){
			String action = Dungeon.quickslot.getAction(slot);
			Dungeon.quickslot.clearSlot(slot);
			updateQuickslot();
			Dungeon.quickslot.setSlot( slot, this ,action);
			updateQuickslot();
		}
		
		Badges.validateItemLevelAquired(this);

		return this;
	}
	
	public void gainCharge( float amt ){
		if (wand != null){
			wand.gainCharge(amt);
		}
	}

	public Class<?extends Wand> wandClass(){
		return wand != null ? wand.getClass() : null;
	}

	@Override
	public Item upgrade(boolean enchant) {
		super.upgrade( enchant );

		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.upgrade();
			//gives the wand one additional charge
			//wand.maxCharges = Math.min(wand.maxCharges + 1, 10);
			wand.curCharges = Math.min(wand.curCharges, wand.maxCharges);
			updateQuickslot();
		}

		return this;
	}

	@Override
	public Item degrade() {
		super.degrade();

		if (wand != null) {
			int curCharges = wand.curCharges;
			wand.degrade();
			//gives the wand one additional charge
			//wand.maxCharges = Math.min(wand.maxCharges + 1, Wand.MAXCHARGE);
			wand.curCharges = curCharges-1;
			updateQuickslot();
		}

		return this;
	}

	@Override
	public boolean isDegradeable(){
		return level() > 0;
	}

	@Override
	public Item fusion(Item item){
		if(item instanceof MagesStaff) {
			wand.curCharges = Math.min(wand.curCharges + ((MagesStaff) item).wand.curCharges, wand.maxCharges);
		}
		return super.fusion(item);
	}

	@Override
	public String status() {
		if (wand == null) return super.status();
		else return wand.status();
	}

	@Override
	public String info() {
		String info = super.info();

		if (wand == null){
			info += "\n\n" + Messages.get(this, "no_wand");
		} else {
			info += "\n\n" + Messages.get(this, "has_wand", Messages.get(wand, "name")) + " " + wand.statsDesc();
		}

		return info;
	}

	@Override
	public Emitter emitter() {
		if (wand == null) return null;
		Emitter emitter = new Emitter();
		emitter.pos(12.5f, 3);
		emitter.fillTarget = false;
		emitter.pour(StaffParticleFactory, 0.1f);
		return emitter;
	}

	private static final String WAND = "wand";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(WAND, wand);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		wand = (Wand) bundle.get(WAND);
		if (wand != null) {
			wand.maxCharges = Math.min(wand.maxCharges + 1, Wand.MAXCHARGE);
			name = Messages.get(wand, "staff_name");
		}
	}

	@Override
	public int price() {
		return 0;
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( final Item item ) {
			if (item != null) {

				if (!item.isIdentified()) {
					GLog.w(Messages.get(MagesStaff.class, "id_first"));
					return;
				} else if (item.cursed){
					GLog.w(Messages.get(MagesStaff.class, "cursed"));
					return;
				}

				if (wand == null){
					applyWand((Wand)item);
				} else {
					final int newLevel =
							item.level() >= level() ?
									level() > 0 ?
										item.level() + 1
										: item.level()
									: level();
					GameScene.show(
							new WndOptions("",
									Messages.get(MagesStaff.class, "warning", newLevel),
									Messages.get(MagesStaff.class, "yes"),
									Messages.get(MagesStaff.class, "no")) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										applyWand((Wand)item);
									}
								}
							}
					);
				}
			}
		}

		private void applyWand(Wand wand){
			Sample.INSTANCE.play(Assets.SND_BURNING);
			curUser.sprite.emitter().burst( ElmoParticle.FACTORY, 12 );
			evoke(curUser);

			Dungeon.quickslot.clearItem(wand);

			wand.detach(curUser.belongings.backpack);

			GLog.p( Messages.get(MagesStaff.class, "imbue", wand.name()));
			imbueWand( wand, curUser );

			updateQuickslot();
		}
	};

	private final Emitter.Factory StaffParticleFactory = new Emitter.Factory() {
		@Override
		//reimplementing this is needed as instance creation of new staff particles must be within this class.
		public void emit( Emitter emitter, int index, float x, float y ) {
			StaffParticle c = (StaffParticle)emitter.getFirstAvailable(StaffParticle.class);
			if (c == null) {
				c = new StaffParticle();
				emitter.add(c);
			}
			c.reset(x, y);
		}

		@Override
		//some particles need light mode, others don't
		public boolean lightMode() {
			return !((wand instanceof WandOfDisintegration)
					|| (wand instanceof WandOfCorruption)
					|| (wand instanceof WandOfCorrosion)
					|| (wand instanceof WandOfRegrowth));
		}
	};

	//determines particle effects to use based on wand the staff owns.
	public class StaffParticle extends PixelParticle{

		private float minSize;
		private float maxSize;
		public float sizeJitter = 0;

		public StaffParticle(){
			super();
		}

		public void reset( float x, float y ) {
			revive();

			speed.set(0);

			this.x = x;
			this.y = y;

			if (wand != null)
				wand.staffFx( this );

		}

		public void setSize( float minSize, float maxSize ){
			this.minSize = minSize;
			this.maxSize = maxSize;
		}

		public void setLifespan( float life ){
			lifespan = left = life;
		}

		public void shuffleXY(float amt){
			x += Random.Float(-amt, amt);
			y += Random.Float(-amt, amt);
		}

		public void radiateXY(float amt){
			float hypot = (float)Math.hypot(speed.x, speed.y);
			this.x += speed.x/hypot*amt;
			this.y += speed.y/hypot*amt;
		}

		@Override
		public void update() {
			super.update();
			size(minSize + (left / lifespan)*(maxSize-minSize) + Random.Float(sizeJitter));
		}
	}
}
