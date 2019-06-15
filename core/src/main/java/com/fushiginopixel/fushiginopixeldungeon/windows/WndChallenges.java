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

import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.SPDSettings;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.SpecialMode;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.CheckBox;
import com.fushiginopixel.fushiginopixeldungeon.ui.IconButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.Icons;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.watabou.noosa.RenderedText;

import java.util.ArrayList;

public class WndChallenges extends Window {

	private static final int WIDTH		= 120;
	private static final int TTL_HEIGHT    = 12;
	private static final int BTN_HEIGHT    = 18;
	private static final int GAP        = 1;

	private boolean editable;
	private ArrayList<CheckBox> challengeBoxes;
	private ArrayList<CheckBox> modeBoxes;

	public WndChallenges( int checked, int modeChecked, boolean editable ) {

		super();

		this.editable = editable;

		RenderedText title = PixelScene.renderText( Messages.get(this, "title"), 9 );
		title.hardlight( TITLE_COLOR );
		title.x = (WIDTH - title.width()) / 2;
		title.y = (TTL_HEIGHT - title.height()) / 2;
		PixelScene.align(title);
		add( title );

		challengeBoxes = new ArrayList<>();

		float pos = TTL_HEIGHT;
		for (int i=0; i < Challenges.NAME_IDS.length; i++) {

			final String challenge = Challenges.NAME_IDS[i];
			
			CheckBox cb = new CheckBox( Messages.get(Challenges.class, challenge) );
			cb.checked( (checked & Challenges.MASKS[i]) != 0 );
			cb.active = editable;

			if (i > 0) {
				pos += GAP;
			}
			cb.setRect( 0, pos, WIDTH-16, BTN_HEIGHT );

			add( cb );
			challengeBoxes.add( cb );
			
			IconButton info = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					Fushiginopixeldungeon.scene().add(
							new WndMessage(Messages.get(Challenges.class, challenge+"_desc"))
					);
				}
			};
			info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
			add(info);
			
			pos = cb.bottom();
		}

		//specialmode
		pos += GAP;
		RenderedText title1 = PixelScene.renderText( Messages.get(this, "title1"), 9 );
		title1.hardlight( TITLE_COLOR );
		title1.x = (WIDTH - title1.width()) / 2;
		title1.y = pos;
		PixelScene.align(title1);
		add( title1 );
		pos += title1.height() + GAP;

		modeBoxes = new ArrayList<>();

		for (int i = 0; i < SpecialMode.MASKS.length; i++) {

			final String mode = Messages.get(SpecialMode.MODES.get(i + 1), "name");

			CheckBox cb = new CheckBox( mode ){
				@Override
				protected void onClick() {
					for (int j = 0; j < SpecialMode.MASKS.length; j++) {
					    if(modeBoxes.get(j) != this)
						modeBoxes.get(j).checked(false);
					}
					super.onClick();
				}
			};
			cb.checked( (modeChecked == SpecialMode.MASKS[i]));
			cb.active = editable;

			if (i > 0) {
				pos += GAP;
			}
			cb.setRect( 0, pos, WIDTH-16, BTN_HEIGHT );

			add( cb );
			modeBoxes.add( cb );

			final int j = i;
			IconButton info = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					Fushiginopixeldungeon.scene().add(
							new WndMessage(Messages.get(SpecialMode.MODES.get(j + 1), "desc"))
					);
				}
			};
			info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
			add(info);

			pos = cb.bottom();
		}

		resize( WIDTH, (int)pos );
	}

	@Override
	public void onBackPressed() {

		if (editable) {
			int value = 0;
			for (int i=0; i < challengeBoxes.size(); i++) {
				if (challengeBoxes.get( i ).checked()) {
					value |= Challenges.MASKS[i];
				}
			}
			SPDSettings.challenges( value );

			value = 0;
			for (int i=0; i < modeBoxes.size(); i++) {
				if (modeBoxes.get( i ).checked()) {
					value = SpecialMode.MASKS[i];
				}
			}
			SPDSettings.specialMode( value );
		}

		super.onBackPressed();
	}
}