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

package com.fushiginopixel.fushiginopixeldungeon.items;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Combo;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroClass;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfAlert;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfKnowledge;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Boomerang;
import com.fushiginopixel.fushiginopixeldungeon.journal.Catalog;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndGuessItem;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndTextInput;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Item implements Bundlable {

	protected static final String TXT_TO_STRING_LVL		= "%s %+d";
	protected static final String TXT_TO_STRING_X		= "%s x%d";
	
	protected static final float TIME_TO_THROW		= 1.0f;
	protected static final float TIME_TO_PICK_UP	= 1.0f;
	protected static final float TIME_TO_DROP		= 0.5f;
	
	public static final String AC_DROP		= "DROP";
	public static final String AC_THROW		= "THROW";
	public static final String AC_GUESS		= "GUESS";
	
	public String defaultAction;
	public boolean usesTargeting;
	
	protected String name = Messages.get(this, "name");
	public int image = 0;
	public String guessedName = "";
	
	public boolean stackable = false;
	protected int quantity = 1;
	
	private int level = 0;

	public boolean levelKnown = false;
	
	public boolean cursed;
	public boolean cursedKnown;
	
	// Unique items persist through revival
	public boolean unique = false;

	// whether an item can be included in heroes remains
	public boolean bones = false;

	public HeroClass classlimit = null;
	
	private static Comparator<Item> itemComparator = new Comparator<Item>() {
		@Override
		public int compare( Item lhs, Item rhs ) {
			return Generator.Category.order( lhs ) - Generator.Category.order( rhs );
		}
	};
	
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = new ArrayList<String>();
		actions.add( AC_DROP );
		actions.add( AC_THROW );
		if(!isIdentified()){
			actions.add( AC_GUESS );
		}
		return actions;
	}
	
	public boolean doPickUp( Char hero ) {
		if (collect( hero.belongings.backpack )) {

			int ringCharges = Ring.getBonus(hero, RingOfKnowledge.Knowledge.class);
			if(ringCharges > 5)
				identify();
			
			GameScene.pickUp( this, hero.pos );
			Sample.INSTANCE.play( Assets.SND_ITEM );
			hero.spendAndNext( TIME_TO_PICK_UP );
			return true;
			
		} else {
			return false;
		}
	}
	
	public void doDrop( Char hero ) {
		hero.spendAndNext( TIME_TO_DROP );
		Dungeon.level.drop( detachAll( hero.belongings.backpack ), hero.pos ).sprite.drop( hero.pos );
	}

	public void doGuess( Char hero ) {
		hero.sprite.operate( hero.pos );
		GameScene.show(new WndGuessItem(this));
	}

	//resets an item's properties, to ensure consistency between runs
	public void reset(){
		//resets the name incase the language has changed.
		name = Messages.get(this, "name");
	}

	public void doThrow( Char hero ) {
		GameScene.selectCell( thrower );
	}
	
	public void execute( Char hero, String action ) {
		
		curUser = hero;
		curItem = this;

		Combo combo = hero.buff(Combo.class);
		if (combo != null) combo.detach();
		
		if (action.equals( AC_DROP )) {
			
			doDrop( hero );
			
		} else if (action.equals( AC_THROW )) {
			
			doThrow( hero );
			
		} else if (action.equals( AC_GUESS ) && !this.isIdentified() && hero instanceof Hero) {

			doGuess( hero );

		}
	}
	
	public void execute( Char hero ) {
		execute( hero, defaultAction );
	}

	public boolean pierceThrow = false;

	protected void onThrow( int cell ,boolean pierce) {
		pierceThrow = pierce;
		onThrow( cell );
	}

	protected void onThrow( int cell ) {
		if(pierceThrow) return;

		Heap heap = Dungeon.level.drop( this, cell );
		if (!heap.isEmpty()) {
			heap.sprite.drop( cell );
		}
	}
    protected void onCatch( Char c ) {}

    public void throwTo(int cell){
        Char c = Actor.findChar(cell);
        if(!pierceThrow && c != null && c.catchItem(this, curUser)){
            onCatch(c);
            return;
        }
	    onThrow(cell);
    }
	
	//takes two items and merges them (if possible)
	public Item merge( Item other ){
		if (isSimilar( other )){
			quantity += other.quantity;
			other.quantity = 0;
		}
		return this;
	}
	
	public boolean collect( Bag container ) {

		if(container.owner instanceof Hero){
			if(classlimit != null) {
				if ((((Hero) container.owner).heroClass) != classlimit) {
					return false;
				}
			}

			if(isIdentified() && Dungeon.hero != null && Dungeon.hero.isAlive()){
				Catalog.setSeen(getClass());
			}
		}
		
		ArrayList<Item> items = container.items;
		
		if (items.contains( this )) {
			return true;
		}
		
		for (Item item:items) {
			if (item instanceof Bag && ((Bag)item).grab( this )) {
				return collect( (Bag)item );
			}
		}
		
		if (stackable) {
			for (Item item:items) {
				if (isSimilar( item )) {
					item.merge( this );
					item.updateQuickslot();
					return true;
				}
			}
		}
		
		if (items.size() < container.realSize()) {
			
			if (container.owner instanceof Hero && Dungeon.hero != null && Dungeon.hero.isAlive()) {
				Badges.validateItemLevelAquired( this );
			}
			
			items.add( this );
			Dungeon.quickslot.replacePlaceholder(this);
			updateQuickslot();
			Collections.sort( items, itemComparator );
			return true;
			
		} else {
			
			GLog.n( Messages.get(Item.class, "pack_full", name()) );
			return false;
			
		}
	}
	
	public boolean collect() {
		return collect( Dungeon.hero.belongings.backpack );
	}
	
	//returns a new item if the split was sucessful and there are now 2 items, otherwise null
	public Item split( int amount ){
		if (amount <= 0 || amount >= quantity()) {
			return null;
		} else {
			try {
				
				//pssh, who needs copy constructors?
				Item split = getClass().newInstance();
				Bundle copy = new Bundle();
				this.storeInBundle(copy);
				split.restoreFromBundle(copy);
				split.quantity(amount);
				quantity -= amount;
				
				return split;
			} catch (Exception e){
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}
	}
	
	public final Item detach( Bag container ) {
		
		if (quantity <= 0) {
			
			return null;
			
		} else
		if (quantity == 1) {

			if (stackable || this instanceof Boomerang){
				Dungeon.quickslot.convertToPlaceholder(this);
			}

			return detachAll( container );
			
		} else {
			
			
			Item detached = split(1);
			updateQuickslot();
			if (detached != null) detached.onDetach(container );
			return detached;
			
		}
	}
	
	public final Item detachAll( Bag container ) {
		
		return detachAll(container , false);
	}

	public final Item detachAll( Bag container ,boolean grab) {
		if(!grab) {
			Dungeon.quickslot.clearItem(this);
			updateQuickslot();
		}

		if(container != null) {
			for (Item item : container.items) {
				if (item == this) {
					container.items.remove(this);
					item.onDetach(container);
					return this;
				} else if (item instanceof Bag) {
					Bag bag = (Bag) item;
					if (bag.contains(this)) {
						return detachAll(bag);
					}
				} else if (item instanceof Pot) {
					for (Item item1 : ((Pot) item).items) {
						if (item1 == this) {
							((Pot) item).items.remove(this);
							item.onDetach(container);
							return this;
						}
					}
				}
			}
		}else if(getPot() != null){
			Pot item = (Pot)getPot();
			for (Item item1 : item.items) {
				if (item1 == this) {
					item.items.remove(this);
					item.onDetach(container);
					return this;
				}
			}
		}

		return this;
	}

	public Item getPot() {
		for(Item item :Dungeon.hero.belongings.backpack){
			if(item instanceof Pot){
				for(Item item1 : ((Pot)item).items){
					if(item1 == this){
						return item;
					}
				}
			}
		}
		return null;
	}
	
	public boolean isSimilar( Item item ) {
		return getClass() == item.getClass();
	}

	protected void onDetach(Bag container){}

	public int level(){
		return level;
	}

	public void level( int value ){
		level = value;

		updateQuickslot();
	}

	public boolean isUnique(){
		return unique;
	}

	public Item fusion(Item item){
		if(isUpgradable()) {
			int level = item.level();
			if (level > 0) {
				upgrade(level);
			} else if (level < 0) {
				degrade(-level);
			}
		}
		return this;
	}
	
	public Item upgrade() {

		if(isUpgradable()) {
			this.level++;

		}
		updateQuickslot();
		
		return this;
	}
	
	final public Item upgrade( int n ) {
		for (int i=0; i < n; i++) {
			upgrade();
		}
		
		return this;
	}
	
	public Item degrade() {

		if(isDegradeable()) {
			this.level--;

		}
		
		return this;
	}
	
	final public Item degrade( int n ) {
		for (int i=0; i < n; i++) {
			degrade();
		}
		
		return this;
	}
	
	public int visiblyUpgraded() {
		return levelKnown ? level : 0;
	}
	
	public boolean visiblyCursed() {
		return cursed && cursedKnown;
	}
	
	public boolean isUpgradable() {
		return level() < 100;
	}

	public boolean isDegradeable() {
		return level() > 0;
	}
	
	public boolean isIdentified() {
		return levelKnown && cursedKnown;
	}

	public boolean isIdentifiedForAutomatic() {
		if(getPot() != null) {
			return true;
		}else{
			return isIdentified();
		}
	}
	
	public boolean isEquipped( Char hero ) {
		return false;
	}
	
	public Item identify() {
		
		levelKnown = true;
		cursedKnown = true;
		
		if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
			Catalog.setSeen(getClass());
		}
		
		return this;
	}

	public Item forget() {

		levelKnown = false;
		cursedKnown = false;
		guessedName = "";

		if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
			Catalog.setNoSeen(getClass());
		}

		return this;
	}
	
	public static void evoke( Char hero ) {
		hero.sprite.emitter().burst( Speck.factory( Speck.EVOKE ), 5 );
	}
	
	@Override
	public String toString() {

		String name = name();

		if (visiblyUpgraded() != 0)
			name = Messages.format( TXT_TO_STRING_LVL, name, visiblyUpgraded()  );

		if (quantity > 1)
			name = Messages.format( TXT_TO_STRING_X, name, quantity );

		return name;

	}
	
	public String name() {
		return isIdentified() || guessedName.equals("")  ? name : guessedName;
	}
	
	public final String trueName() {
		return name;
	}
	
	public int image() {
		return image;
	}
	
	public ItemSprite.Glowing glowing() {
		return null;
	}

	public Emitter emitter() { return null; }
	
	public String info() {
		return desc();
	}
	
	public String desc() {
		return Messages.get(this, "desc");
	}
	
	public int quantity() {
		return quantity;
	}
	
	public Item quantity( int value ) {
		quantity = value;
		return this;
	}
	
	public int price() {
		return 0;
	}
	
	public static Item virtual( Class<? extends Item> cl ) {
		try {
			
			Item item = (Item)cl.newInstance();
			item.quantity = 0;
			return item;
			
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

	public static boolean targetAction(String action){
		return action.equals("THROW") || action.equals("ZAP") || action.equals("LIGHTTHROW" ) || action.equals("CAST");
	}
	
	public Item random() {
		return this;
	}
	
	public String status() {
		return quantity != 1 ? Integer.toString( quantity ) : null;
	}
	
	public void updateQuickslot() {
			QuickSlotButton.refresh();
	}
	
	private static final String QUANTITY		= "quantity";
	private static final String LEVEL			= "level";
	private static final String LEVEL_KNOWN		= "levelKnown";
	private static final String CURSED			= "cursed";
	private static final String CURSED_KNOWN	= "cursedKnown";
	private static final String GUESSEDNAME		= "guessedName";
	private static final String QUICKSLOT		= "quickslotpos";
	private static final String QUICKACTION		= "quickactionpos";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( QUANTITY, quantity );
		bundle.put( LEVEL, level );
		bundle.put( LEVEL_KNOWN, levelKnown );
		bundle.put( CURSED, cursed );
		bundle.put( CURSED_KNOWN, cursedKnown );
		bundle.put( GUESSEDNAME, guessedName );
		/*
		if (Dungeon.quickslot.contains(this)) {
			bundle.put( QUICKSLOT, Dungeon.quickslot.getSlot(this) );
			bundle.put( QUICKACTION, Dungeon.quickslot.getAction(Dungeon.quickslot.getSlot(this) ) );
		}
		*/
		if (Dungeon.quickslot.contains(this)) {
			int size = Dungeon.quickslot.getSlots(this).size();
			Integer[] array = (Dungeon.quickslot.getSlots(this).toArray(new Integer[size]));
			int[] slots = new int[size];
			for(int i = 0;i < size; i++){
				slots[i] = array[i];
			}
			bundle.put( QUICKSLOT, slots );
			bundle.put( QUICKACTION, Dungeon.quickslot.getActions(Dungeon.quickslot.getSlots(this) ).toArray(new String[size]) );
		}
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		quantity	= bundle.getInt( QUANTITY );
		levelKnown	= bundle.getBoolean( LEVEL_KNOWN );
		cursedKnown	= bundle.getBoolean( CURSED_KNOWN );
		
		int level = bundle.getInt( LEVEL );
		if (level > 0) {
			upgrade( level );
		} else if (level < 0) {
			degrade( -level );
		}
		
		cursed	= bundle.getBoolean( CURSED );
		guessedName	= bundle.getString( GUESSEDNAME );

		/*
		//only want to populate slot on first load.
		if (Dungeon.hero == null) {
			if (bundle.contains(QUICKSLOT) && bundle.contains(QUICKACTION)) {
				Dungeon.quickslot.setSlot(bundle.getInt(QUICKSLOT), this, bundle.getString(QUICKACTION));
			}
		}
		*/
		if (Dungeon.hero == null) {
			if (bundle.contains(QUICKSLOT) && bundle.contains(QUICKACTION)) {
				int[] slots = bundle.getIntArray(QUICKSLOT);
				String[] actions = bundle.getStringArray(QUICKACTION);
				if(slots != null && actions != null) {
					for (int i = 0; i < slots.length; i++) {
						Dungeon.quickslot.setSlot(slots[i], this, actions[i]);
					}
				}
			}
		}
	}

	public final int throwPos( Char user, int dst){
		return throwPos(user, user.pos, dst);
	}

	public int throwPos( Char user, int from, int dst){
		return new Ballistica( from, dst, Ballistica.PROJECTILE ).collisionPos;
	}
	
	public void cast( final Char user, final int dst ) {
		
		final int cell = throwPos( user, dst );
		user.sprite.zap( cell );
		if(user instanceof Hero) {
			((Hero)user).busy();
		}

		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );

		Char enemy = Actor.findChar( cell );
		QuickSlotButton.target(enemy);
		
		final float delay = castDelay(user, dst);

		if (enemy != null) {
			((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
					reset(user.sprite,
							enemy.sprite,
							this,
							user,
							new Callback() {
						@Override
						public void call() {
							Item.this.detach(user.belongings.backpack).throwTo(cell);
							user.spendAndNext(delay);
						}
					});
		} else {
			((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
					reset(user.sprite,
							cell,
							this,
							user,
							new Callback() {
						@Override
						public void call() {
							Item.this.detach(user.belongings.backpack).throwTo(cell);
							user.spendAndNext(delay);
						}
					});
		}
	}
	
	public float castDelay( Char user, int dst ){
		return TIME_TO_THROW;
	}
	
	protected static Char curUser = null;
	protected static Item curItem = null;
	protected static CellSelector.Listener thrower = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				curItem.cast( curUser, target );
			}
		}
		@Override
		public String prompt() {
			return Messages.get(Item.class, "prompt");
		}
	};

    public WndTextInput.Listener input = new WndTextInput.Listener() {
        @Override
        public void onSelect( String str ) {
            if (str != null) {
                guessedName = str;
            }
        }
    };
}
