package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcAlfredSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcRenSprite;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;

import java.util.ArrayList;

public class NpcRen extends NormalNpc {

    {
        spriteClass = NpcRenSprite.class;

        chat = new ArrayList<String>(){
            {
                add(Messages.get(NpcRen.class, "chat"));
                add(Messages.get(NpcRen.class, "chat1"));
                add(Messages.get(NpcRen.class, "chat2"));
                add(Messages.get(NpcRen.class, "chat3"));
            }
        };

        endChat = new ArrayList<String>() {
            {
                add(Messages.get(NpcRen.class, "chat1_0"));
            }
        };
    }
}
