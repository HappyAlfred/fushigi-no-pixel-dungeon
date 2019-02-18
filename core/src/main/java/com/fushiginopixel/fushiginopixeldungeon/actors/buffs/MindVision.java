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

package com.fushiginopixel.fushiginopixeldungeon.actors.buffs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfAlert;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class MindVision extends FlavourBuff {

	public static final float DURATION = 20f;
	
	public int distance = 2;


	public int level = 10;

	{
		type = buffType.POSITIVE;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.MIND_VISION;
	}
	
	@Override
	public void tintIcon(Image icon) {
		greyIcon(icon, 5f, cooldown());
	}
	
	@Override
	public String toString() {
		if(mindvrBonus(target) >= 10){
			return Messages.get(this, "name");
		}
		else{
			return Messages.get(this, "name_1");
		}
	}

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
		GameScene.updateFog();
	}

	@Override
	public String desc() {
		if(mindvrBonus(target) >= 6){
			return Messages.get(this, "desc",dispTurns());
		}
		else if(mindvrBonus(target) >= 1){
			return Messages.get(this, "desc_1",mindvrBonus(target) * 2,dispTurns());
		}
		else return Messages.get(this, "desc_2");
	}

	public static int mindvrBonus(Char target) {
		int alertBonus = Ring.getBonus(target, RingOfAlert.Alert.class);
		int mindvr = target.buff(MindVision.class)!=null ? target.buff(MindVision.class).level : 0;
		return Math.max(alertBonus , mindvr);
	}
}
