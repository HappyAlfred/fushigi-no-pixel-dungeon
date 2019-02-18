package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcAbbeySprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcAlfredSprite;

import java.util.ArrayList;

public class NpcAbbey extends NormalNpc {

    {
        spriteClass = NpcAbbeySprite.class;

        chat = new ArrayList<String>(){
            {
                add(Messages.get(NpcAbbey.class, "chat"));
                add(Messages.get(NpcAbbey.class, "chat1"));
                add(Messages.get(NpcAbbey.class, "chat2"));
                add(Messages.get(NpcAbbey.class, "chat3"));
            }
        };
        endChat = new ArrayList<String>() {
            {
                add(Messages.get(NpcAbbey.class, "chat1_0"));
            }
        };
    }
}
