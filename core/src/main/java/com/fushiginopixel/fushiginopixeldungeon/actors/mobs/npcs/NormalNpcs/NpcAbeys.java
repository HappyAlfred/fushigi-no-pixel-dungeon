package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcAbbeySprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcAbeysSprite;

import java.util.ArrayList;

public class NpcAbeys extends NormalNpc {

    {
        spriteClass = NpcAbeysSprite.class;

        chat = new ArrayList<String>(){
            {
                add(Messages.get(NpcAbeys.class, "chat"));
                add(Messages.get(NpcAbeys.class, "chat1"));
                add(Messages.get(NpcAbeys.class, "chat2"));
                add(Messages.get(NpcAbeys.class, "chat3"));
            }
        };
        endChat = new ArrayList<String>() {
            {
                add(Messages.get(NpcAbeys.class, "chat1_0"));
                add(Messages.get(NpcAbeys.class, "chat1_1"));
                add(Messages.get(NpcAbeys.class, "chat1_2"));
            }
        };
    }
}
