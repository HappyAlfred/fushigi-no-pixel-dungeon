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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

//FOR TESTING!!!
public class TestingShuriken extends MissileWeapon {

	{
		image = ItemSpriteSheet.SHURIKEN;
		LIMIT = 5;
	}

	@Override
	public int min(int lvl) {
		return 4;
	}

	@Override
	public int max(int lvl) {
		return 12;
	}
	
	@Override
	public int price() {
		return 12 * quantity;
	}

	@Override
	public void cast( final Char user, final int dst ) {


		Ballistica ballistica = new Ballistica(user.pos,dst,Ballistica.STOP_TERRAIN);
		final int cell = ballistica.collisionPos;
		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );

		Char enemy = Actor.findChar( cell );
		QuickSlotButton.target(enemy);

		final float delay = castDelay(user, dst);
		final Item throwing = this.detach(user.belongings.backpack);
		for (int c : ballistica.subPath(1, ballistica.dist)) {

			Char ch;
			if ((ch = Actor.findChar( c )) != null && c != cell) {
				final int ch_pos = ch.pos;
				((MissileSprite)user.sprite.parent.recycle( MissileSprite.class )).
						reset( user.sprite, ch.sprite, null, user, new Callback()  {
					@Override
					public void call () {
						((TestingShuriken) throwing).onThrow(ch_pos,true);
					}
				});


			}else if(c == cell) {
				if (enemy != null) {
					((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
							reset(user.sprite, enemy.sprite, this, user, new Callback() {
								@Override
								public void call() {
									((TestingShuriken) throwing).onThrow(cell,false);
									user.spendAndNext(delay);
								}
							});
				} else {
					((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
							reset(user.sprite, cell, this, user, new Callback() {
								@Override
								public void call() {
									throwing.pierceThrow = false;
									((TestingShuriken) throwing).onThrow(cell,false);
									user.spendAndNext(delay);
								}
							});
				}
			}
		}
	}
}
