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

package com.fushiginopixel.fushiginopixeldungeon.windows;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.Warehouse;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.GodownKeeper;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.Artifact;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.journal.Catalog;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.ScrollPane;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class WndGuessItem extends Window {

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;

	protected Item guessItem = null;

	public WndGuessItem(final Item item) {
		
		super();
		this.guessItem = item;
		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item));
		titlebar.label(Messages.titleCase(item.name()) + ":" + Messages.get(WndGuessItem.class,"guess_item"));
		titlebar.setRect(0, 0, WIDTH, 0);
		add( titlebar );

		String msg = Messages.get(this, "info");

		RenderedTextMultiline message = PixelScene.renderMultiline( msg, 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );

		RedButton btnGuess = new RedButton( Messages.get(this, "confirm") ) {
			@Override
			protected void onClick() {
				GameScene.show(new WndTextInput(
						Messages.get(WndGuessItem.class,"guess_item"),
						guessItem.name(),
						false,
						Messages.get(WndGuessItem.class,"confirm"),
						Messages.get(WndGuessItem.class,"cancel"),
						guessItem.input

				));
				hide();
			}
		};
		btnGuess.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
		add( btnGuess );

		RedButton btnRemove = new RedButton( Messages.get(this, "remove" ) ) {
			@Override
			protected void onClick() {
				guessItem.guessedName = "";
				hide();
			}
		};
		btnRemove.setRect(0, btnGuess.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add( btnRemove );

		RedButton btnCancel = new RedButton( Messages.get(this, "cancel" ) ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect(0, btnRemove.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add( btnCancel );
		
		resize(WIDTH, (int) btnCancel.bottom());
	}
}
