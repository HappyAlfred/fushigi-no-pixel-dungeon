package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NormalNpc;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcRenSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.NpcYunMengSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.fushiginopixel.fushiginopixeldungeon.windows.IconTitle;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;

public class YunMeng extends NPC {

    {
        spriteClass = NpcYunMengSprite.class;

        properties.add(Property.IMMOVABLE);
    }
    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    @Override
    public boolean interact() {
        GameScene.show( new WndYunMeng( this ) );
        return false;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 1000;
    }

    @Override
    public int damage( int dmg, Object src ,EffectType type) {
        return 0;
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    public class WndYunMeng extends Window {

        private static final int WIDTH = 120;
        private static final int BTN_HEIGHT = 20;
        private static final float GAP = 2;

        public WndYunMeng(final YunMeng yunmeng) {

            super();

            IconTitle titlebar = new IconTitle();
            titlebar.icon(yunmeng.sprite());
            titlebar.label(Messages.titleCase(yunmeng.name));
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);

            String msg = "";
            msg = Messages.get(yunmeng, "chat",Dungeon.hero.givenName());

            RenderedTextMultiline message = PixelScene.renderMultiline(msg, 6);
            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add(message);

            RedButton btnWand1 = new RedButton(Messages.get(yunmeng, "remove_curse")) {
                @Override
                protected void onClick() {
                    GameScene.selectItem(itemSelector, WndBag.Mode.ALL, Messages.get(ScrollOfRemoveCurse.class,"inv_title"));
                }
            };
            btnWand1.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
            add(btnWand1);

            resize(WIDTH, (int) btnWand1.bottom());
        }
    }

    protected static WndBag.Listener itemSelector = new WndBag.Listener() {
        @Override
        public void onSelect( Item item ) {

            if (item != null) {

                ScrollOfRemoveCurse rc = new ScrollOfRemoveCurse();
                rc.onItemSelected( item );

            }
        }
    };
}
