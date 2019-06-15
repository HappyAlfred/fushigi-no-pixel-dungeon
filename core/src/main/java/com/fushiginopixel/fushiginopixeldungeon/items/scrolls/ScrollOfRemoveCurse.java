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

package com.fushiginopixel.fushiginopixeldungeon.items.scrolls;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Flare;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndEnchantmentsTab;
import com.watabou.noosa.audio.Sample;

import java.util.Iterator;

public class ScrollOfRemoveCurse extends InventoryScroll {

	{
		initials = 8;
		mode = WndBag.Mode.UNIDED_OR_CURSED;
	}
	public WndEnchantmentsTab.Mode mode1 = WndEnchantmentsTab.Mode.REMOVE;
	
	@Override
	public void empoweredRead() {
		for (Item item : curUser.belongings){
			if (item.cursed){
				item.cursedKnown = true;
			}
		}
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		doRead();
	}
	
	@Override
	public void onItemSelected(Item item) {
		curUser = Dungeon.hero;
		new Flare( 6, 32 ).show( curUser.sprite, 2f ) ;

		boolean procced = uncurse( curUser, item );

		if(item instanceof Weapon && ((Weapon) item).enchantmentCount()> 0)
		GameScene.show(new WndEnchantmentsTab( item,enchantSelector, mode1, Messages.get(WndEnchantmentsTab.class, "enchantmentremove") ));
		else if(item instanceof Armor && ((Armor) item).glyphCount()> 0)
		GameScene.show(new WndEnchantmentsTab( item,enchantSelector, mode1, Messages.get(WndEnchantmentsTab.class, "glyphremove") ));

		Weakness.detach( curUser, Weakness.class );

		if (procced) {
			GLog.p( Messages.get(this, "cleansed") );
		} else {
			GLog.i( Messages.get(this, "not_cleansed") );
		}
	}

	public static boolean uncurse(Char hero, Item... items ) {
		
		boolean procced = false;
		for (Item item : items) {
			if (item != null && item.cursed) {
				item.cursed = false;
				procced = true;
			}
			if (item instanceof Weapon){
				Weapon w = (Weapon) item;
				if (w.hasCurseEnchant()){
					Iterator<Weapon.Enchantment> iterator = ((Weapon)item).enchantment.iterator();
					while (iterator.hasNext()){
						Weapon.Enchantment next = iterator.next();
						if(next.curse()){
							iterator.remove();
						}
					}
					w.cursed = false;
					procced = true;
				}
				item = (Item)w;
			}
			if (item instanceof Armor){
				Armor a = (Armor) item;
				if (a.hasCurseGlyph()){
					Iterator<Armor.Glyph> iterator = ((Armor)item).glyph.iterator();
					while (iterator.hasNext()){
						Armor.Glyph next = iterator.next();
						if(next.curse()){
							iterator.remove();
						}
					}
					a.cursed = false;
					procced = true;
				}
				item = (Item)a;
			}
			if (item instanceof Bag){
				for (Item bagItem : ((Bag)item).items){
					if (bagItem != null && bagItem.cursed) {
						bagItem.cursed = false;
						procced = true;
					}
				}
			}
		}
		
		if (procced) {
			hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
			hero.updateHT( false ); //for ring of might
		}
		
		return procced;
	}

	protected static void onEnchantSelected(Item item,Object enchant) {
		if(enchant != null) {
		    boolean remove = false;
			if (enchant instanceof Weapon.Enchantment) {
				((Weapon)item).enchantment.remove((Weapon.Enchantment)enchant);
                remove = true;
			}else if (enchant instanceof Armor.Glyph){
				((Armor)item).glyph.remove((Armor.Glyph)enchant);
                remove = true;
			}
			if(remove){
                GLog.n( Messages.get(ScrollOfRemoveCurse.class, "removed") );
            }
		}
	}


	protected static WndEnchantmentsTab.Listener enchantSelector = new WndEnchantmentsTab.Listener() {
		@Override
		public void onSelect( Item item , Object enchant ) {

			if (item != null && enchant != null) {

				ScrollOfRemoveCurse.onEnchantSelected(item, enchant );

			}
			else GLog.n( Messages.get(ScrollOfRemoveCurse.class, "not_removed") );
		}
	};
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
