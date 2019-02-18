package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcLynnSprite;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;

import java.util.ArrayList;

public class NpcLynn extends NormalNpc {

    {
        spriteClass = NpcLynnSprite.class;

        chat = new ArrayList<String>(){
            {
                add(Messages.get(NpcLynn.class, "chat"));
                add(Messages.get(NpcLynn.class, "chat1"));
                add(Messages.get(NpcLynn.class, "chat2"));
                add(Messages.get(NpcLynn.class, "chat3"));
            }
        };

        endChat = new ArrayList<String>() {
            {
                add(Messages.get(NpcLynn.class, "chat1_0"));
            }
        };
    }

    @Override
    public boolean interact() {
        Hero hero = Dungeon.hero;
        if(hero.HP < hero.HT){
            hero.HP = hero.HT;
            hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
        }
        if(Statistics.amuletObtained){
            sprite.turnTo( pos, Dungeon.hero.pos );
            WndQuest.chating(this,endChat);
        }else{
            super.interact();
        }
        return true;
    }
}
