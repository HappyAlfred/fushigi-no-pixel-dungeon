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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.RatSceptre;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.StoneOfEnchantment;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RatKingSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.IconTitle;

public class RatKing extends NPC {

	{
		spriteClass = RatKingSprite.class;
		
		state = SLEEPING;
	}

	public static Item reward = new ScrollOfMagicalInfusion();

	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public float speed() {
		return 2f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage( int dmg, Object src,EffectType type ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact() {
		sprite.turnTo( pos, Dungeon.hero.pos );
		RatSceptre tokens = Dungeon.hero.belongings.getItem( RatSceptre.class );
		if (state == SLEEPING) {
			notice();
			yell( Messages.get(this, "not_sleeping") );
			state = WANDERING;
		} else {
			if (tokens != null){
				GameScene.show( new WndRatKing( this, tokens ) );
			}
			else
				yell( Messages.get(this, "what_is_it") );
		}
		return true;
	}
	
	@Override
	public String description() {
		return ((RatKingSprite)sprite).festive ?
				Messages.get(this, "desc_festive")
				: super.description();
	}

	public class WndRatKing extends Window {

		private static final int WIDTH      = 120;
		private static final int BTN_HEIGHT = 20;
		private static final int GAP        = 2;

		public WndRatKing( final RatKing ratKing, final Item tokens ) {

			super();

			IconTitle titlebar = new IconTitle();
			titlebar.icon( new ItemSprite( tokens.image(), null ) );
			titlebar.label( Messages.titleCase( tokens.name() ) );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );

			RenderedTextMultiline message = PixelScene.renderMultiline( Messages.get(RatKing.class, "message"), 6 );
			message.maxWidth(WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			RedButton btnReward = new RedButton( Messages.get(RatKing.class, "reward") ) {
				@Override
				protected void onClick() {

					if (tokens.isEquipped( Dungeon.hero )) {
						if(!((RatSceptre)tokens).doUnequip( Dungeon.hero, false ))
							return;
					}
					tokens.detach( Dungeon.hero.belongings.backpack );

					takeReward( ratKing, RatKing.reward );
				}
			};
			btnReward.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
			add( btnReward );

			resize( WIDTH, (int)btnReward.bottom() );
		}

		private void takeReward(RatKing ratKing, Item reward ) {

			hide();
			if (reward == null) return;

			reward.identify();
			if (reward.doPickUp( Dungeon.hero )) {
				GLog.i( Messages.get(Dungeon.hero, "you_now_have", reward.name()) );
			} else {
				Dungeon.level.drop( reward, ratKing.pos ).sprite.drop();
			}
		}
	}
}
