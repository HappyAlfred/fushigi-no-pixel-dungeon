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

package com.fushiginopixel.fushiginopixeldungeon.ui;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.QuickSlot;
import com.fushiginopixel.fushiginopixeldungeon.SPDSettings;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.windows.IconTitle;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndMessage;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class QuickSlotButton extends Button implements WndBag.Listener {
	
	private static QuickSlotButton[] instance = new QuickSlotButton[4];
	private int slotNum;

	private ItemSlot slot;
	
	private static Image crossB;
	private static Image crossM;
	
	private static boolean targeting = false;
	public static Char lastTarget = null;
	
	public QuickSlotButton( int slotNum ) {
		super();
		this.slotNum = slotNum;
		item( select( slotNum ) );
		
		instance[slotNum] = this;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		reset();
	}

	public static void reset() {
		instance = new QuickSlotButton[4];

		lastTarget = null;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		slot = new ItemSlot() {
			@Override
			protected void onClick() {
				if(selectAction(slotNum) == null) {
					Dungeon.quickslot.clearSlot(slotNum);
					refresh();
					return;
				}
				if (Item.targetAction(selectAction(slotNum)) && targeting){
					int cell = autoAim(lastTarget, select(slotNum));

					if (cell != -1){
						GameScene.handleCell(cell);
					} else {
						//couldn't auto-aim, just target the position and hope for the best.
						GameScene.handleCell( lastTarget.pos );
					}
				} else {
					Item item = select(slotNum);
					if (Item.targetAction(selectAction(slotNum)))//item.usesTargeting)
						useTargeting();
					item.execute( Dungeon.hero , selectAction(slotNum));
				}
			}
			@Override
			protected boolean onLongClick() {
				GameScene.show(
						new WndOptions( "",
								"",
								Messages.get(QuickSlotButton.class, "add_slot"),
								Messages.get(QuickSlotButton.class, "remove_slot"),
								Messages.get(QuickSlotButton.class, "check_slot") ) {
							@Override
							protected void onSelect( int index ) {
								if (index == 0) {
									QuickSlotButton.this.onLongClick();
								}else if (index == 1) {
									Dungeon.quickslot.clearSlot(slotNum);
									refresh();
								}else if (index == 2) {
									if(selectAction(slotNum) == null) {
										Dungeon.quickslot.clearSlot(slotNum);
										refresh();
										return;
									}
									Item item = Dungeon.quickslot.getItem(slotNum);
									GameScene.show( new WndMessage( item.name()
											+ "\n"
											+ Messages.get(QuickSlotButton.class, "this_action")
											+ Messages.get(item, "ac_" + Dungeon.quickslot.getAction(slotNum)) ) );
								}
							}
						}
				);
				return true;
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}
		};
		slot.showParams( true, false, true );
		add( slot );
		
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add( crossB );
		
		crossM = new Image();
		crossM.copy( crossB );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.fill( this );
		
		crossB.x = x + (width - crossB.width) / 2;
		crossB.y = y + (height - crossB.height) / 2;
		PixelScene.align(crossB);
	}
	
	@Override
	protected void onClick() {
		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, Messages.get(this, "select_item") );
	}
	
	@Override
	protected boolean onLongClick() {

		GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, Messages.get(this, "select_item") );
		return true;
	}

	private static Item select(int slotNum){
		return Dungeon.quickslot.getItem( slotNum );
	}

	private static String selectAction(int slotNum){
		return Dungeon.quickslot.getAction( slotNum );
	}

	@Override
	public void onSelect( final Item item ) {
		if (item != null && item.actions(Dungeon.hero) != null && !(item.actions(Dungeon.hero).isEmpty())) {

			GameScene.show( new WndQuickSlotButton( item ) );
			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
		enableSlot();
	}
	
	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}
	
	private void enableSlot() {
		slot.enable(Dungeon.quickslot.isNonePlaceholder( slotNum ));
	}
	
	private void useTargeting() {

		if (lastTarget != null &&
				Actor.chars().contains( lastTarget ) &&
				lastTarget.isAlive() &&
				Dungeon.level.heroFOV[lastTarget.pos]) {

			targeting = true;
			CharSprite sprite = lastTarget.sprite;
			
			sprite.parent.addToFront( crossM );
			crossM.point(sprite.center(crossM));

			crossB.point(slot.icon.center(crossB));
			crossB.visible = true;

		} else {

			lastTarget = null;
			targeting = false;

		}

	}

	public static int autoAim(Char target){
		//will use generic projectile logic if no item is specified
		return autoAim(target, new Item());
	}

	//FIXME: this is currently very expensive, should either optimize ballistica or this, or both
	public static int autoAim(Char target, Item item){

		//first try to directly target
		if (item.throwPos(Dungeon.hero, target.pos) == target.pos) {
			return target.pos;
		}

		//Otherwise pick nearby tiles to try and 'angle' the shot, auto-aim basically.
		PathFinder.buildDistanceMap( target.pos, BArray.not( new boolean[Dungeon.level.length()], null ), 2 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE
					&& item.throwPos(Dungeon.hero, i) == target.pos)
				return i;
		}

		//couldn't find a cell, give up.
		return -1;
	}
	
	public static void refresh() {
		for (int i = 0; i < instance.length; i++) {
			if (instance[i] != null) {
				instance[i].item(select(i));
			}
		}
	}
	
	public static void target( Char target ) {
		if (target != Dungeon.hero) {
			lastTarget = target;
			
			TargetHealthIndicator.instance.target( target );
		}
	}
	
	public static void cancel() {
		if (targeting) {
			crossB.visible = false;
			crossM.remove();
			targeting = false;
		}
	}

	public class WndQuickSlotButton extends Window {

		private static final float BUTTON_HEIGHT	= 16;

		private static final float GAP	= 2;

		private static final int WIDTH_MIN = 120;
		private static final int WIDTH_MAX = 220;

		public WndQuickSlotButton( final Item item) {

			super();

			int width = WIDTH_MIN;

			RenderedTextMultiline info = PixelScene.renderMultiline(Messages.get(QuickSlotButton.class , "select_action"), 6);
			info.maxWidth(width);

			//info box can go out of the screen on landscape, so widen it
			while (SPDSettings.landscape()
					&& info.height() > 100
					&& width < WIDTH_MAX) {
				width += 20;
				info.maxWidth(width);
			}

			IconTitle titlebar = new IconTitle(item);
			titlebar.setRect(0, 0, width, 0);
			add(titlebar);

			if (item.levelKnown && (item.cursed || item.level() < 0)) {
				titlebar.color(ItemSlot.DEGRADED);
			} else if (item.levelKnown && item.level() > 0) {
				titlebar.color(ItemSlot.UPGRADED);
			}

			info.setPos(titlebar.left(), titlebar.bottom() + GAP);
			add(info);

			float y = info.top() + info.height() + GAP;
			float x = 0;

			ArrayList<RedButton> line = new ArrayList<>();
			for (final String action:item.actions( Dungeon.hero )) {

				RedButton btn = new RedButton( Messages.get(item, "ac_" + action), 8 ) {
					@Override
					protected void onClick() {
						hide();
						Dungeon.quickslot.setSlot( slotNum , item , action);
						refresh();
					};
				};
				btn.setSize( btn.reqWidth(), BUTTON_HEIGHT );
				if (x + btn.width() > width || line.size() == 3) {
					layoutButtons(line, width - x, y);
					x = 0;
					y += BUTTON_HEIGHT + 1;
					line = new ArrayList<>();
				}
				x++;
				add( btn );
				line.add( btn );

				if (action.equals(item.defaultAction)) {
					btn.textColor( 0xFFFF44 );
				}

				x += btn.width();
			}
			layoutButtons(line, width - x, y);

			resize( width, (int)(y + (x > 0 ? BUTTON_HEIGHT : 0)) );
		}

		private void layoutButtons(ArrayList<RedButton> line, float extraWidth, float y) {
			if (line == null || line.size() == 0 || extraWidth == 0) return;
			if (line.size() == 1) {
				line.get(0).setSize(line.get(0).width() + extraWidth, BUTTON_HEIGHT);
				line.get(0).setPos(0, y);
				return;
			}
			ArrayList<RedButton> lineByWidths = new ArrayList<>(line);
			Collections.sort(lineByWidths, widthComparator);
			RedButton smallest, middle, largest;
			smallest = lineByWidths.get(0);
			middle = lineByWidths.get(1);
			largest = null;
			if (lineByWidths.size() == 3) {
				largest = lineByWidths.get(2);
			}

			float btnDiff = middle.width() - smallest.width();
			smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth), BUTTON_HEIGHT);
			extraWidth -= btnDiff;
			if (extraWidth > 0) {
				if (largest == null) {
					smallest.setSize(smallest.width() + extraWidth / 2, BUTTON_HEIGHT);
					middle.setSize(middle.width() + extraWidth / 2, BUTTON_HEIGHT);
				} else {
					btnDiff = largest.width() - smallest.width();
					smallest.setSize(smallest.width() + Math.min(btnDiff, extraWidth / 2), BUTTON_HEIGHT);
					middle.setSize(middle.width() + Math.min(btnDiff, extraWidth / 2), BUTTON_HEIGHT);
					extraWidth -= btnDiff * 2;
					if (extraWidth > 0) {
						smallest.setSize(smallest.width() + extraWidth / 3, BUTTON_HEIGHT);
						middle.setSize(middle.width() + extraWidth / 3, BUTTON_HEIGHT);
						largest.setSize(largest.width() + extraWidth / 3, BUTTON_HEIGHT);
					}
				}
			}

			float x = 0;
			for (RedButton btn : line) {
				btn.setPos(x, y);
				x += btn.width() + 1;
			}
		}

		private Comparator<RedButton> widthComparator = new Comparator<RedButton>() {
			@Override
			public int compare(RedButton lhs, RedButton rhs) {
				if (lhs.width() < rhs.width()) {
					return -1;
				} else if (lhs.width() == rhs.width()) {
					return 0;
				} else {
					return 1;
				}
			}
		};
	}
}
