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

package com.fushiginopixel.fushiginopixeldungeon.items.pots;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Splash;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.ItemStatusHandler;
import com.fushiginopixel.fushiginopixeldungeon.journal.Catalog;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.HeroSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndPotTab;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Pot extends Item {
	
	public static final String AC_LOOK	= "LOOK";
	public static final String AC_ADD	= "ADD";
    public static final String AC_ZAP	= "ZAP";

	protected static final float TIME_TO_ADD	= 1f;
    protected static final float TIME_TO_ZAP	= 1f;

	private static final Class<?>[] pots = {
			PotOfIdentify.class,
			PotOfTransmutation.class,
			PotOfRestructure.class,
			PotOfTradeGold.class,
			PotOfFusion.class,
			PotOfFreeze.class,
			PotOfStorge.class,
			PotOfAlchemy.class,
            PotOfDispel.class,
            PotOfCannon.class,
	};

	protected int initials;

	private static final HashMap<String, Integer> shapes = new HashMap<String, Integer>() {
		{
			put("LONG",ItemSpriteSheet.POT_LONG);
			put("WIDE",ItemSpriteSheet.POT_WIDE);
			put("NARROWMOUTH",ItemSpriteSheet.POT_NARROWMOUTH);
			put("GLOBOSE",ItemSpriteSheet.POT_GLOBOSE);
			put("TRIANGULAR",ItemSpriteSheet.POT_TRIANGULAR);
			put("THINNECK",ItemSpriteSheet.POT_THINNECK);
			put("NORMAL",ItemSpriteSheet.POT_NORMAL);
			put("SMALL",ItemSpriteSheet.POT_SMALL);
			put("CRACKED",ItemSpriteSheet.POT_CRACKED);
			put("EAR",ItemSpriteSheet.POT_EAR);
			put("CALABASH",ItemSpriteSheet.POT_CALABASH);
			put("RING",ItemSpriteSheet.POT_RING);
		}
	};

    @Override
    public String status() {
        return items.size() + "/" + size;
    }

	private static ItemStatusHandler<Pot> handler;

	public ArrayList<Item> items = new ArrayList<>();

	private String shape;
	
	public int size;
	
	{
		stackable = false;

		bones = true;
	}

	@SuppressWarnings("unchecked")
	public static void initShapes() {
		handler = new ItemStatusHandler<>( (Class<? extends Pot>[])pots, shapes );
	}

	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		handler.saveSelectively( bundle, items );
	}

	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Pot>[])pots, shapes, bundle );
	}

	public Pot(int size){
		super();
		reset();
		if(size >= 0){
			this.size = size;
		}
	}

	public Pot() {
		this(5);
	}

	@Override
	public void reset(){
		super.reset();
		if (handler != null) {
			image = handler.image(this);
			shape = handler.label(this);
		}
	};
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );

		actions.add(AC_ADD);
		if (items.size() > 0) actions.add(AC_LOOK);
        actions.add(AC_ZAP);
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_LOOK) && items.size() > 0){
			GameScene.show(new WndPotTab(this , null, WndPotTab.Mode.ALL, Messages.get(WndPotTab.class, "pot")));
		}
		else if(action.equals((AC_ADD))){
		    if(!cursed)
			    doAdd();
		    else
		        GLog.n(Messages.get(this,"cursed"));
		}else if (action.equals(AC_ZAP)){
            if(cursed){
                GLog.n(Messages.get(this,"cursed"));
                return;
            }
            GameScene.selectCell( zapper );
        }
	}

	public boolean isFull() {

		return (size <= 0 || items.size() >= size);
	}

	@Override
    public boolean isUnique(){
	    for(Item item : items){
	        if(item.isUnique()){
	            return true;
            }
        }
	    return super.isUnique();
    }

	public boolean isExpandable() {

		return (size < 5);
	}

	public Item expand() {

		if(isExpandable()) {
			this.size++;

		}
		updateQuickslot();

		return this;
	}

	final public Item expand( int n ) {
		for (int i=0; i < n; i++) {
			expand();
		}

		return this;
	}

	@Override
	protected void onThrow( int cell ) {
		if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {

			super.onThrow( cell );

		} else  {

			Dungeon.level.press( cell, null, true );
			shatter( cell );

		}
	}

	public void shatter( int cell ) {
		if (Dungeon.level.heroFOV[cell]) {
			Sample.INSTANCE.play( Assets.SND_SHATTER );
			Splash.at( cell, 0xffd500, 5 );
			if(!items.isEmpty()) {
				for (Item item : items) {
					Dungeon.level.drop(item, cell).sprite.drop();
				}
			}
		}else{
			if(!items.isEmpty()) {
				for (Item item : items) {
					Dungeon.level.drop(item, cell);
				}
			}
		}
	}

	@Override
	public Item fusion(Item item) {
		super.fusion(item);
		if(item instanceof Pot) {
			Pot pot = (Pot) item;
			size = pot.size;
			if (pot.items.size() != 0) {
				for (Item i : pot.items) {
					if (size != 0 && size - items.size() > 0)
						items.add(i);
					else break;
				}
			}
		}
		return this;
	}

	@Override
	public void cast( final Hero user, int dst ) {
		super.cast(user, dst);
	}

	public boolean isEmpty() {

		return items.size() == 0;
	}

	public abstract void doAdd(

    );

	public boolean clickAble( Hero curuser,Ballistica shot ,int cell) {
		return false;
	}

    public boolean click( Hero curuser,Ballistica shot ,int cell) {
    	if(!clickAble(curuser, shot, cell)){
    		return false;
		}
        return true;
    }

	protected void addAnimation() {
		curUser.spend( TIME_TO_ADD );
		curUser.busy();
		((HeroSprite)curUser.sprite).operate( curUser.pos );
	}

	@Override
	public Item random() {
		size = Random.Int(3,6);
		return this;
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
		setKnown();
		return super.identify();
	}

	@Override
	public Item forget() {
		setForgot();
		return super.forget();
	}

	@Override
	public String name() {
		return isKnown() || !guessedName.equals("") ? super.name() : Messages.get(Pot.class, shape);
	}

	@Override
	public String info() {
		return isKnown() ?
				desc() :
				Messages.get(this, "unknown_desc");
	}

	public Integer initials(){
		return isKnown() ? initials : null;
	}

    @Override
    public boolean isIdentified() {
        return isKnown();
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

	public static HashSet<Class<? extends Pot>> getKnown() {
		return handler.known();
	}

	public static HashSet<Class<? extends Pot>> getUnknown() {
		return handler.unknown();
	}

	public static boolean allKnown() {
		return handler.known().size() == pots.length;
	}

	@Override
	public int price() {
		int price = 20;
		if (size > 0)
			price *= 1f + (0.2f * size);
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (price < 1) {
			price = 1;
		}

		for(Item item : items){
			price += item.price();
		}
		return price;
	}

	public static final String ITEMS	= "ITEMS";
	public static final String SIZE	= "SIZE";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(ITEMS, items);
		bundle.put(SIZE, size);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		for (Bundlable item : bundle.getCollection( ITEMS )) {
			if (item != null) items.add((Item)item);
		};

		size = bundle.getInt( SIZE );
	}

    protected int collisionProperties = Ballistica.MAGIC_BOLT;

    protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {
            if (target != null) {
                final Pot curPot;
                if (curItem instanceof Pot) {
                    curPot = (Pot) Pot.curItem;
                } else {
                    return;
                }

                final Ballistica shot = new Ballistica( curUser.pos, target, curPot.collisionProperties);
                int cell = shot.collisionPos;

                curUser.sprite.zap(cell);
                //attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));

                curUser.busy();
                if (!curPot.click(curUser, shot , cell)){
					GLog.i(Messages.get(this,"nothing"));
					curUser.spendAndNext( TIME_TO_ZAP );

				}
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Pot.class, "prompt");
        }
    };
}
