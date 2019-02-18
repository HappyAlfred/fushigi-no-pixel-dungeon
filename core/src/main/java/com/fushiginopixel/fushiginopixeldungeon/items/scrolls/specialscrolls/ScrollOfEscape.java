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

package com.fushiginopixel.fushiginopixeldungeon.items.scrolls.specialscrolls;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Drowsy;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.TimekeepersHourglass;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.InterlevelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;

public class ScrollOfEscape extends Scroll {

	{
		initials = 31;
		image = ItemSpriteSheet.SCROLL_OF_ESCAPE;
	}

	@Override
	public void doRead() {

		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();

		readAnimation();

		if(!Statistics.overed && !Statistics.thief && !Dungeon.bossLevel()) {
			Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
			if (buff != null) buff.detach();

			Dungeon.escape(getClass());
			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnDepth = 0;
			InterlevelScene.returnPos = -1;
			Game.switchScene(InterlevelScene.class);
		}else{

			GLog.w( Messages.get(this, "no_tele") );
		}
	}
	
	@Override
	public void empoweredRead() {
	}


	public boolean isKnown() {
		return true;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return isKnown() ? 10 * quantity : super.price();
	}
}
