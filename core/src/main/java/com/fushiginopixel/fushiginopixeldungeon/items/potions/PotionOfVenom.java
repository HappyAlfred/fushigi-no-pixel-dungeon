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

package com.fushiginopixel.fushiginopixeldungeon.items.potions;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ConfusionGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Levitation;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Slow;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.WeakeningTrap;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class PotionOfVenom extends Potion {

	{
		initials = 14;

		bones = true;
	}

	@Override
	public void shatter( int cell ) {

		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.get(cell).burst(ShadowParticle.UP, 5);
			knownByUse();

			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}

		Char ch = Actor.findChar( cell );
		if (ch == Dungeon.hero){
			if(((Hero)ch).STR >= 1){
				ch.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(WeakeningTrap.class, "msg_1", 1));
				((Hero)ch).STR -= 1;
			}
			venom(ch);
			Sample.INSTANCE.play( Assets.SND_CURSED );
		}else if(ch != null){
			venom(ch);
		}
	}

	private void venom(Char ch){
		Buff.affect( ch, Poison.class,new EffectType(0,EffectType.POISON)).set(3 + Dungeon.depth / 3);
		Buff.prolong( ch, Slow.class, Slow.DURATION * 2,new EffectType(0,EffectType.POISON));
		Buff.prolong( ch, Weakness.class, 20f,new EffectType(0,EffectType.POISON));
	}
	
	@Override
	public void apply( Hero hero ) {
		knownByUse();
		CellEmitter.get(hero.pos).burst(ShadowParticle.UP, 5);

		if(hero.STR > 1){
			hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(WeakeningTrap.class, "msg_1", 1));
			hero.STR -= 1;
		}
		venom(hero);
		Sample.INSTANCE.play( Assets.SND_CURSED );
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
