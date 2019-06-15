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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SlimyGel;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfBeverage;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.InventoryPot;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BlueGelCubeSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.YellowGelCubeSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class YellowGelCube extends GelCube {
	{
		spriteClass = YellowGelCubeSprite.class;
		
		HP = HT = 190;
		//defenseSkill = 46;
		
		EXP = 24;
	}

	public int zapSkill() {
		return 2;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 48, 82 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 95;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

	@Override
	public boolean shootGel(Hero hero){
		ArrayList<Item> canShoot = new ArrayList<>();
		for(Item itemhad : hero.belongings) {
			if(itemhad instanceof Bag) {
				for (Item item : hero.belongings.backpack.items) {
					if (item instanceof InventoryPot || item instanceof Potion) {
						canShoot.add(item);
					}
				}
			}
		}

		if(!canShoot.isEmpty()) {
			Item item = Random.element(canShoot);
			if(item instanceof InventoryPot) {
				InventoryPot pot = (InventoryPot) Random.element(canShoot);
				Item gel = new SlimyGel();
				if (pot.canInput(gel)) {
					pot.input(new SlimyGel());
					GLog.n(Messages.get(this, "gel_zap", pot.name()));
				} else {
					pot.detach(hero.belongings.backpack);
					pot.shatter(hero.pos);
					GLog.n(Messages.get(this, "gel_zap1", pot.name()));
				}
				return true;
			}else{
				Potion pot = (Potion) Random.element(canShoot);
				pot.detach(hero.belongings.backpack);
				Potion beverage = new PotionOfBeverage();
				if(!beverage.doPickUp(hero)){
					Dungeon.level.drop( beverage, Dungeon.hero.pos ).sprite.drop();
				}
				GLog.n(Messages.get(this, "gel_zap2", pot.name()));
				return true;
			}
		}else return false;
	}
}
