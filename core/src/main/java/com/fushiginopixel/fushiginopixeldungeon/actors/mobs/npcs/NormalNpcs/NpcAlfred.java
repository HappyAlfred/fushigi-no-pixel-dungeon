package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcAlfredSprite;

import java.util.ArrayList;

public class NpcAlfred extends NormalNpc {

    {
        spriteClass = NpcAlfredSprite.class;

        chat = new ArrayList<String>(){
            {
                add(Messages.get(NpcAlfred.class, "chat"));
                add(Messages.get(NpcAlfred.class, "chat1"));
                add(Messages.get(NpcAlfred.class, "chat2"));
                add(Messages.get(NpcAlfred.class, "chat3"));
                add(Messages.get(NpcAlfred.class, "chat4"));
            }
        };
        endChat = new ArrayList<String>() {
            {
                add(Messages.get(NpcAlfred.class, "chat1_0"));
            }
        };
    }
}
