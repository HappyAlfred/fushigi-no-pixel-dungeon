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
import com.fushiginopixel.fushiginopixeldungeon.SPDSettings;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.journal.Catalog;
import com.fushiginopixel.fushiginopixeldungeon.journal.Document;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.ui.Icons;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.ScrollPane;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

//FIXME a lot of cleanup and improvements to do here
public class WndJournal extends WndTabbed {
	
	private static final int WIDTH_P    = 120;
	private static final int HEIGHT_P   = 160;
	
	private static final int WIDTH_L    = 160;
	private static final int HEIGHT_L   = 128;
	
	private static final int ITEM_HEIGHT	= 18;
	
	private GuideTab guideTab;
	private AlchemyGuideTab alchemisticTab;
	private NotesTab notesTab;
	private CatalogTab catalogTab;
	
	public static int last_index = 0;
	
	public WndJournal(){
		
		int width = SPDSettings.landscape() ? WIDTH_L : WIDTH_P;
		int height = SPDSettings.landscape() ? HEIGHT_L : HEIGHT_P;
		
		resize(width, height);
		
		guideTab = new GuideTab();
		add(guideTab);
		guideTab.setRect(0, 0, width, height);
		guideTab.updateList();

		alchemisticTab = new AlchemyGuideTab();
		add(alchemisticTab);
		alchemisticTab.setRect(0, 0, width, height);
		alchemisticTab.updateList();

		notesTab = new NotesTab();
		add(notesTab);
		notesTab.setRect(0, 0, width, height);
		notesTab.updateList();
		
		catalogTab = new CatalogTab();
		add(catalogTab);
		catalogTab.setRect(0, 0, width, height);
		catalogTab.updateList();
		
		Tab[] tabs = {
				new LabeledTab( Messages.get(this, "guide") ) {
					protected void select( boolean value ) {
						super.select( value );
						guideTab.active = guideTab.visible = value;
						if (value) last_index = 0;
					}
				},
				new LabeledTab( Messages.get(this, "alchemy") ) {
					protected void select( boolean value ) {
						super.select( value );
						alchemisticTab.active = alchemisticTab.visible = value;
						if (value) last_index = 1;
					}
				},
				new LabeledTab( Messages.get(this, "notes") ) {
					protected void select( boolean value ) {
						super.select( value );
						notesTab.active = notesTab.visible = value;
						if (value) last_index = 2;
					}
				},
				new LabeledTab( Messages.get(this, "items") ) {
					protected void select( boolean value ) {
						super.select( value );
						catalogTab.active = catalogTab.visible = value;
						if (value) last_index = 3;
					}
				}
		};
		
		for (Tab tab : tabs) {
			add( tab );
		}
		
		layoutTabs();
		
		select(last_index);
	}
	
	private static class ListItem extends Component {
		
		protected RenderedTextMultiline label;
		protected BitmapText depth;
		protected ColorBlock line;
		protected Image icon;
		
		public ListItem( Image icon, String text ) {
			this(icon, text, -1);
		}
		
		public ListItem( Image icon, String text, int d ) {
			super();
			
			this.icon.copy(icon);
			
			label.text( text );
			
			if (d >= 0) {
				depth.text(Integer.toString(d));
				depth.measure();
				
				if (d == Dungeon.depth) {
					label.hardlight(TITLE_COLOR);
					depth.hardlight(TITLE_COLOR);
				}
			}
		}
		
		@Override
		protected void createChildren() {
			label = PixelScene.renderMultiline( 8 );
			add( label );
			
			icon = new Image();
			add( icon );
			
			depth = new BitmapText( PixelScene.pixelFont);
			add( depth );
			
			line = new ColorBlock( 1, 1, 0xFF222222);
			add(line);
			
		}
		
		@Override
		protected void layout() {
			
			icon.y = y + 1 + (height() - 1 - icon.height()) / 2f;
			PixelScene.align(icon);
			
			depth.x = icon.x + (icon.width - depth.width()) / 2f;
			depth.y = icon.y + (icon.height - depth.height()) / 2f + 1;
			PixelScene.align(depth);
			
			line.size(width, 1);
			line.x = 0;
			line.y = y;
			
			label.maxWidth((int)(width - icon.width() - 8 - 1));
			label.setPos(icon.x + icon.width() + 1, y + 1 + (height() - label.height()) / 2f);
			PixelScene.align(label);
		}
	}
	
	protected static class GuideTab extends Component {
		
		private ScrollPane list;
		private ArrayList<GuideItem> pages = new ArrayList<>();
		protected static Document document = Document.ADVENTURERS_GUIDE;
		
		@Override
		protected void createChildren() {
			list = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = pages.size();
					for (int i=0; i < size; i++) {
						if (pages.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add( list );
		}
		
		@Override
		protected void layout() {
			super.layout();
			list.setRect( 0, 0, width, height);
		}

		protected void updateList(){
			Component content = list.content();
			
			float pos = 0;
			
			ColorBlock line = new ColorBlock( width(), 1, 0xFF222222);
			line.y = pos;
			content.add(line);
			
			RenderedTextMultiline title = PixelScene.renderMultiline(document.title(), 9);
			title.hardlight(TITLE_COLOR);
			title.maxWidth( (int)width() - 2 );
			title.setPos( (width() - title.width())/2f, pos + 1 + ((ITEM_HEIGHT) - title.height())/2f);
			PixelScene.align(title);
			content.add(title);
			
			pos += Math.max(ITEM_HEIGHT, title.height());
			
			for (String page : document.pages()){
				GuideItem item = new GuideItem( page );
				
				item.setRect( 0, pos, width(), ITEM_HEIGHT );
				content.add( item );
				
				pos += item.height();
				pages.add(item);
			}
			
			content.setSize( width(), pos );
			list.setSize( list.width(), list.height() );
		}
		
		private static class GuideItem extends ListItem {
			
			private boolean found = false;
			private String page;
			
			public GuideItem( String page ){
				super( new ItemSprite( ItemSpriteSheet.GUIDE_PAGE, null),
						Messages.titleCase(document.pageTitle(page)), -1);
				
				this.page = page;
				found = document.hasPage(page);
				
				if (!found) {
					icon.hardlight( 0.5f, 0.5f, 0.5f);
					label.text( Messages.titleCase(Messages.get( this, "missing" )));
					label.hardlight( 0x999999 );
				}
				
			}
			
			public boolean onClick( float x, float y ) {
				if (inside( x, y ) && found) {
					GameScene.show( new WndStory( document.pageBody(page) ));
					return true;
				} else {
					return false;
				}
			}
			
		}
		
	}

	protected static class AlchemyGuideTab extends GuideTab{
		{
			document = Document.ALCHEMISTIC_GUIDE;
		}
	}
	
	private static class NotesTab extends Component {
		
		private ScrollPane list;
		
		@Override
		protected void createChildren() {
			list = new ScrollPane( new Component() );
			add( list );
		}
		
		@Override
		protected void layout() {
			super.layout();
			list.setRect( 0, 0, width, height);
		}
		
		private void updateList(){
			Component content = list.content();
			
			float pos = 0;
			
			//Keys
			ArrayList<Notes.KeyRecord> keys = Notes.getRecords(Notes.KeyRecord.class);
			if (!keys.isEmpty()){
				ColorBlock line = new ColorBlock( width(), 1, 0xFF222222);
				line.y = pos;
				content.add(line);
				
				RenderedTextMultiline title = PixelScene.renderMultiline(Messages.get(this, "keys"), 9);
				title.hardlight(TITLE_COLOR);
				title.maxWidth( (int)width() - 2 );
				title.setPos( (width() - title.width())/2f, pos + 1 + ((ITEM_HEIGHT) - title.height())/2f);
				PixelScene.align(title);
				content.add(title);
				
				pos += Math.max(ITEM_HEIGHT, title.height());
			}
			for(Notes.Record rec : keys){
				ListItem item = new ListItem( Icons.get(Icons.DEPTH),
						Messages.titleCase(rec.desc()), rec.depth() );
				item.setRect( 0, pos, width(), ITEM_HEIGHT );
				content.add( item );
				
				pos += item.height();
			}
			
			//Landmarks
			ArrayList<Notes.LandmarkRecord> landmarks = Notes.getRecords(Notes.LandmarkRecord.class);
			if (!landmarks.isEmpty()){
				ColorBlock line = new ColorBlock( width(), 1, 0xFF222222);
				line.y = pos;
				content.add(line);
				
				RenderedTextMultiline title = PixelScene.renderMultiline(Messages.get(this, "landmarks"), 9);
				title.hardlight(TITLE_COLOR);
				title.maxWidth( (int)width() - 2 );
				title.setPos( (width() - title.width())/2f, pos + 1 + ((ITEM_HEIGHT) - title.height())/2f);
				PixelScene.align(title);
				content.add(title);
				
				pos += Math.max(ITEM_HEIGHT, title.height());
			}
			for (Notes.Record rec : landmarks) {
				ListItem item = new ListItem( Icons.get(Icons.DEPTH),
						Messages.titleCase(rec.desc()), rec.depth() );
				item.setRect( 0, pos, width(), ITEM_HEIGHT );
				content.add( item );
				
				pos += item.height();
			}
			
			content.setSize( width(), pos );
			list.setSize( list.width(), list.height() );
		}
		
	}
	
	private static class CatalogTab extends Component{
		
		private RedButton[] itemButtons;
		private static final int NUM_BUTTONS = 8;
		
		private static int currentItemIdx   = 0;
		
		private static final int WEAPON_IDX = 0;
		private static final int ARMOR_IDX  = 1;
		private static final int WAND_IDX   = 2;
		private static final int RING_IDX   = 3;
		private static final int ARTIF_IDX  = 4;
		private static final int POTION_IDX = 5;
		private static final int SCROLL_IDX = 6;
		private static final int POT_IDX 	 = 7;
		
		private ScrollPane list;
		
		private ArrayList<CatalogItem> items = new ArrayList<>();
		
		@Override
		protected void createChildren() {
			itemButtons = new RedButton[NUM_BUTTONS];
			for (int i = 0; i < NUM_BUTTONS; i++){
				final int idx = i;
				itemButtons[i] = new RedButton( "" ){
					@Override
					protected void onClick() {
						currentItemIdx = idx;
						updateList();
					}
				};
				itemButtons[i].icon(new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER + i, null));
				add( itemButtons[i] );
			}
			
			list = new ScrollPane( new Component() ) {
				@Override
				public void onClick( float x, float y ) {
					int size = items.size();
					for (int i=0; i < size; i++) {
						if (items.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add( list );
		}
		
		private static final int BUTTON_HEIGHT = 17;
		
		@Override
		protected void layout() {
			super.layout();
			
			int perRow = NUM_BUTTONS;
			float buttonWidth = width()/perRow;
			
			for (int i = 0; i < NUM_BUTTONS; i++) {
				itemButtons[i].setRect((i%perRow) * (buttonWidth), (i/perRow) * (BUTTON_HEIGHT + 1),
						buttonWidth, BUTTON_HEIGHT);
				PixelScene.align(itemButtons[i]);
			}
			
			list.setRect(0, itemButtons[NUM_BUTTONS-1].bottom() + 1, width,
					height - itemButtons[NUM_BUTTONS-1].bottom() - 1);
		}
		
		private void updateList() {
			
			items.clear();
			
			for (int i = 0; i < NUM_BUTTONS; i++){
				if (i == currentItemIdx){
					itemButtons[i].icon().color(TITLE_COLOR);
				} else {
					itemButtons[i].icon().resetColor();
				}
			}
			
			Component content = list.content();
			content.clear();
			list.scrollTo( 0, 0 );
			
			ArrayList<Class<? extends Item>> itemClasses;
			final HashMap<Class<?  extends Item>, Boolean> known = new HashMap<>();
			if (currentItemIdx == WEAPON_IDX) {
				itemClasses = new ArrayList<>(Catalog.WEAPONS.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == ARMOR_IDX){
				itemClasses = new ArrayList<>(Catalog.ARMOR.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == WAND_IDX){
				itemClasses = new ArrayList<>(Catalog.WANDS.items());
				//for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Wand.getKnown().contains(cls));
			} else if (currentItemIdx == RING_IDX){
				itemClasses = new ArrayList<>(Catalog.RINGS.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Ring.getKnown().contains(cls));
			} else if (currentItemIdx == ARTIF_IDX){
				itemClasses = new ArrayList<>(Catalog.ARTIFACTS.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, true);
			} else if (currentItemIdx == POTION_IDX){
				itemClasses = new ArrayList<>(Catalog.POTIONS.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Potion.getKnown().contains(cls));
			} else if (currentItemIdx == SCROLL_IDX) {
				itemClasses = new ArrayList<>(Catalog.SCROLLS.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Scroll.getKnown().contains(cls));
			} else if (currentItemIdx == POT_IDX) {
				itemClasses = new ArrayList<>(Catalog.POTS.items());
				for (Class<? extends Item> cls : itemClasses) known.put(cls, Pot.getKnown().contains(cls));
			} else {
				itemClasses = new ArrayList<>();
			}
			
			Collections.sort(itemClasses, new Comparator<Class<? extends Item>>() {
				@Override
				public int compare(Class<? extends Item> a, Class<? extends Item> b) {
					int result = 0;
					
					//specifically known items appear first, then seen items, then unknown items.
					if (known.get(a) && Catalog.isSeen(a)) result -= 2;
					if (known.get(b) && Catalog.isSeen(b)) result += 2;
					if (Catalog.isSeen(a))                 result --;
					if (Catalog.isSeen(b))                 result ++;
					
					return result;
				}
			});
			
			float pos = 0;
			for (Class<? extends Item> itemClass : itemClasses) {
				try{
					CatalogItem item = new CatalogItem(itemClass.newInstance(), known.get(itemClass), Catalog.isSeen(itemClass));
					item.setRect( 0, pos, width, ITEM_HEIGHT );
					content.add( item );
					items.add( item );
					
					pos += item.height();
				} catch (Exception e) {
					Fushiginopixeldungeon.reportException(e);
				}
			}
			
			content.setSize( width, pos );
			list.setSize( list.width(), list.height() );
		}
		
		private static class CatalogItem extends ListItem {
			
			private Item item;
			private boolean seen;
			
			public CatalogItem(Item item, boolean IDed, boolean seen ) {
				super( new ItemSprite(item), Messages.titleCase(item.trueName()));
				
				this.item = item;
				this.seen = seen;
				
				if (!seen) {
					icon.copy( new ItemSprite( ItemSpriteSheet.WEAPON_HOLDER + currentItemIdx, null) );
					label.text("???");
					label.hardlight( 0x999999 );
				} else if (!IDed) {
					icon.copy( new ItemSprite( ItemSpriteSheet.WEAPON_HOLDER + currentItemIdx, null) );
					label.hardlight( 0xCCCCCC );
				}
				
			}
			
			public boolean onClick( float x, float y ) {
				if (inside( x, y ) && seen) {
					GameScene.show(new WndTitledMessage( new Image(icon),
								Messages.titleCase(item.trueName()), item.desc() ));
					return true;
				} else {
					return false;
				}
			}
		}
		
	}
	
}
