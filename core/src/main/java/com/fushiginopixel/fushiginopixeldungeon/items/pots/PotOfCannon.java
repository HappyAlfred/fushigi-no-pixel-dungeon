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
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Blacksmith;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.CannonBall;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class PotOfCannon extends InventoryPot {

	{
		initials = 9;

		bones = true;
		collisionProperties = Ballistica.STOP_TARGET;
	}

	@Override
	public boolean clickAble(Hero curuser, Ballistica shot, int cell) {
		if(items.isEmpty()){
			return super.clickAble(curuser, shot, cell);
		}
		return true;
	}

	@Override
	public boolean click(final Hero curuser, Ballistica shot , final int cell) {
		if(!super.click(curuser, shot, cell)) {
			return false;
		}
		final Item ammo = items.get(items.size() - 1);
		Sample.INSTANCE.play( Assets.SND_BLAST );
		CellEmitter.center( curuser.pos ).burst( SmokeParticle.FACTORY, 5 );
		((MissileSprite) curuser.sprite.parent.recycle(MissileSprite.class)).
				reset(curuser.sprite,
						cell,
						ammo,
						curuser,
						new Callback() {
							@Override
							public void call() {
								if(ammo instanceof CannonBall){
									CannonBall cannonBall = ((CannonBall)ammo.detach(curuser.belongings.backpack));
									cannonBall.enemyThrow = curuser;
									cannonBall.lightThrow(cell,0);
								}else if(ammo instanceof Bombs){
									((Bombs)ammo.detach(curuser.belongings.backpack)).lightThrow(cell);
								}else {
									ammo.detach(curuser.belongings.backpack).throwToCell(cell);
								}

								curUser.spendAndNext( TIME_TO_ZAP );
							}
						});
		knownByUse();

		return true;
	}

	@Override
	public int price() {
		return super.price();
	}

	@Override
	public void onItemSelected(Item item) {

	}
}
