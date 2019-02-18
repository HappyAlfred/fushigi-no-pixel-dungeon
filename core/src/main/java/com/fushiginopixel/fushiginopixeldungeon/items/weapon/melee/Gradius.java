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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Beam;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.FirstStrike;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.CellSelector;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.ui.QuickSlotButton;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Gradius extends MeleeWeapon {

	public static final String AC_ZAP	= "ZAP";

	private static final float TIME_TO_ZAP	= 1f;
	
	{
		image = ItemSpriteSheet.GRADIUS;

		tier = 5;
		LIMIT = 0;
		ACC = 1.5f;

		defaultAction = AC_ZAP;
		usesTargeting = true;
	}

	@Override
	public int min(int lvl) {
		return  19 +
				lvl;
	}

	@Override
	public int max(int lvl) {
		return  85 +
				lvl*UPGRADE_ATTACK/2;
	}

	public int damageRoll(int lvl) {
		return  Random.Int(5 + lvl/4,10 + lvl);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_ZAP );
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_ZAP)) {

			if (!isEquipped(hero)) {
				GLog.i(Messages.get(Weapon.class, "need_to_equip"));
				return;
			}else{
				curUser = hero;
				curItem = this;
				GameScene.selectCell( zapper );
			}
		}
	}


	protected void onZap( Ballistica beam ) {

		boolean terrainAffected = false;

		int maxDistance = Math.min(20, beam.dist);

		ArrayList<Char> chars = new ArrayList<>();
		for (int c : beam.subPath(1, maxDistance)) {

			Char ch;
			if ((ch = Actor.findChar( c )) != null) {
				chars.add( ch );
			}

			if (Dungeon.level.flamable[c]) {

				Dungeon.level.destroy( c );
				GameScene.updateMap( c );
				terrainAffected = true;

			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}

		for (Char ch : chars) {
			ch.damage( damageRoll(level()), this ,new EffectType(EffectType.BEAM,EffectType.LIGHT));
			ch.sprite.flash();
		}
	}

	protected void fx( Ballistica beam, Callback callback ) {

		int cell = beam.path.get(Math.min(beam.dist, 20));
		curUser.sprite.parent.add(new Beam.DeathRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
		callback.call();
	}

	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {

			if (target != null && curItem instanceof Gradius) {

				final Ballistica shot = new Ballistica( curUser.pos, target, Ballistica.STOP_TERRAIN);
				int cell = shot.collisionPos;

				if (target == curUser.pos || cell == curUser.pos) {
					GLog.i( Messages.get(Wand.class, "self_target") );
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));

				curUser.busy();

				((Gradius)curItem).fx(shot, new Callback() {
					public void call() {
						((Gradius)curItem).onZap(shot);

						curUser.spendAndNext( TIME_TO_ZAP );
					}
				});

				Invisibility.dispel();

			}
		}

		@Override
		public String prompt() {
			return Messages.get(Gradius.class, "prompt");
		}
	};

}
