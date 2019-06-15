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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.HeroSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.PathFinder;

public class ScrollOfTeleportation extends Scroll {

	{
		initials = 9;
	}

	@Override
	public void doRead() {

		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		teleportHero( curUser );
		knownByUse();

		readAnimation();
	}
	
	@Override
	public void empoweredRead() {
		
		if (Dungeon.bossLevel()){
			GLog.w( Messages.get(this, "no_tele") );
			return;
		}
		
		GameScene.selectCell(new CellSelector.Listener() {
			@Override
			public void onSelect(Integer target) {
				if (target != null) {
					//time isn't spent
					((HeroSprite)curUser.sprite).read();
					teleportToLocation(curUser, target);
				}
			}
			
			@Override
			public String prompt() {
				return Messages.get(ScrollOfTeleportation.class, "prompt");
			}
		});
	}
	
	public static void teleportToLocation(Char hero, int pos){
		PathFinder.buildDistanceMap(pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
		if (PathFinder.distance[hero.pos] == Integer.MAX_VALUE
				|| (!Dungeon.level.passable[pos] && !Dungeon.level.avoid[pos])
				|| Actor.findChar(pos) != null){
			GLog.w( Messages.get(ScrollOfTeleportation.class, "cant_reach") );
			return;
		}
		
		appear( hero, pos );
		Dungeon.level.press( pos, hero );
		Dungeon.observe();
		GameScene.updateFog();
		
		GLog.i( Messages.get(ScrollOfTeleportation.class, "tele") );
	}
	
	public static void teleportHero(Char  hero ) {

		int count = 10;
		int pos;
		do {
			pos = Dungeon.level.randomRespawnCell();
			if (count-- <= 0) {
				break;
			}
		} while (pos == -1);
		
		if (pos == -1 || Dungeon.bossLevel()) {
			
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			
		} else {

			appear( hero, pos );
			Dungeon.level.press( pos, hero );
			Dungeon.observe();
			GameScene.updateFog();
			
			GLog.i( Messages.get(ScrollOfTeleportation.class, "tele") );
			
		}
	}

	public static void appear( Char ch, int pos ) {

		ch.sprite.interruptMotion();

		ch.move( pos );
		ch.sprite.place( pos );

		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}

		ch.sprite.emitter().start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
