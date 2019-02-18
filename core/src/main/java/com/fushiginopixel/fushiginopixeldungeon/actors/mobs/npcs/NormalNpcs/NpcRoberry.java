package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcRoberrySprite;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;

import java.util.ArrayList;

public class NpcRoberry extends NormalNpc {

    {
        spriteClass = NpcRoberrySprite.class;

        chat = new ArrayList<String>(){
            {
                add(Messages.get(NpcRoberry.class, "chat"));
            }
        };
    }
}
