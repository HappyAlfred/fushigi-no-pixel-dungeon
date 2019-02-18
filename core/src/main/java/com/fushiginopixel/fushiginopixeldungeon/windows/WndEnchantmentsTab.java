package com.fushiginopixel.fushiginopixeldungeon.windows;

import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.ui.ScrollPane;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndEnchantmentsTab extends WndTabbed {
    public static enum Mode {
        ENCHANTMENT,
        PROPERTY,
        REMOVE
    }
    private static final int WIDTH		= 115;
    private static final int HEIGHT		= 120;
    private WndEnchantmentsTab.EnchantmentsTab enchantments;
    private Listener listener;
    private Mode mode;
    private String title;
    private Item item ;

    public WndEnchantmentsTab(final Item item , Listener listener, Mode mode, String title  ) {

        super();
        this.listener = listener;
        this.mode = mode;
        this.title = title;
        this.item = item;

        resize( WIDTH, HEIGHT );

        ArrayList<Object> enchants = new ArrayList<>();
        if(item instanceof Weapon) {
            if(mode == Mode.PROPERTY){
                for (int i = 0; i < ((Weapon) item).properties.size(); i++) {
                    enchants.add((Object) ((Weapon) item).properties.get(i));
                }

            }
            else {
                for (int i = 0; i < ((Weapon) item).enchantment.size(); i++) {
                    enchants.add((Object) ((Weapon) item).enchantment.get(i));
                }
            }
        }
        else if(item instanceof Armor) {
            if(mode == Mode.PROPERTY){
                for (int i = 0; i < ((Armor) item).properties.size(); i++) {
                    enchants.add((Object) ((Armor) item).properties.get(i));
                }

            }else {
                for (int i = 0; i < ((Armor) item).glyph.size(); i++) {
                    enchants.add((Object) ((Armor) item).glyph.get(i));
                }
            }
        }

        enchantments = new EnchantmentsTab(enchants);
        add( enchantments );
        enchantments.setRect(0, 0, WIDTH, HEIGHT);
        enchantments.setupList();
        add( new WndTabbed.LabeledTab( title ) {
            protected void select( boolean value ) {
                super.select( value );
                enchantments.visible = enchantments.active = selected;
            };
        } );

        layoutTabs();

        select( 0 );


    }

    @Override
    public void onMenuPressed() {
        if (listener == null) {
            hide();
        }
    }

    @Override
    public void onBackPressed() {
        if (listener != null) {
            listener.onSelect( null ,null);
        }
        super.onBackPressed();
    }

    private class EnchantmentsTab extends Component {

        private static final int GAP = 2;

        private float pos;
        private ScrollPane enchantmentsList;
        private ArrayList<EnchantmentsSlot> slots = new ArrayList<>();
        private ArrayList enchants;

        public EnchantmentsTab(ArrayList<Object> it) {
            enchants = it;
            enchantmentsList = new ScrollPane( new Component() ){
                @Override
                public void onClick( float x, float y ) {
                    int size = slots.size();
                    for (int i=0; i < size; i++) {
                        if (slots.get( i ).onClick( x, y )) {
                            break;
                        }
                    }
                }
            };
            add(enchantmentsList);
        }

        @Override
        protected void layout() {
            super.layout();
            enchantmentsList.setRect(0, 0, width, height);
        }

        private void setupList() {
            Component content = enchantmentsList.content();

            if (enchants.get(0) instanceof Weapon.Enchantment){
                for (Weapon.Enchantment en : (ArrayList<Weapon.Enchantment>)enchants) {
                    EnchantmentsSlot slot = new EnchantmentsSlot(en);
                    slot.setRect(0, pos, WIDTH, 16);
                    content.add(slot);
                    slots.add(slot);
                    pos += GAP + slot.height();
                }
            } else if (enchants.get(0) instanceof Armor.Glyph){
                for (Armor.Glyph gl : ((ArrayList<Armor.Glyph>)enchants)) {
                    EnchantmentsSlot slot = new EnchantmentsSlot(gl);
                    slot.setRect(0, pos, WIDTH, 16);
                    content.add(slot);
                    slots.add(slot);
                    pos += GAP + slot.height();
                }
            }
            content.setSize(enchantmentsList.width(), pos);
            enchantmentsList.setSize(enchantmentsList.width(), enchantmentsList.height());
        }

        private class EnchantmentsSlot extends Component {

            private Weapon.Enchantment enchantment;
            private Armor.Glyph glyph;

            RenderedText txt;

            public EnchantmentsSlot( Weapon.Enchantment enchantment ){
                super();
                this.enchantment = enchantment;

                txt = PixelScene.renderText( enchantment.name(), 8 );
                txt.x = GAP;
                txt.y = this.y - (int)(16 + txt.baseLine()) / 2;
                add( txt );

            }
            public EnchantmentsSlot( Armor.Glyph glyph ){
                super();
                this.glyph = glyph;

                txt = PixelScene.renderText( glyph.name(), 8 );
                txt.x = GAP;
                txt.y = this.y - (int)(16 + txt.baseLine()) / 2;
                add( txt );

            }

            @Override
            protected void layout() {
                super.layout();
                txt.x = GAP;
                txt.y = pos;
            }

            protected boolean onClick ( float x, float y ) {
                if (inside( x, y )) {
                    if(mode == Mode.ENCHANTMENT || mode == Mode.PROPERTY) {
                        if (enchantment != null) {
                            GameScene.show(new WndInfoEnchantment((Weapon)item,enchantment));
                        } else if (glyph != null) {
                            GameScene.show(new WndInfoEnchantment((Armor)item,glyph));
                        } else return false;
                        return true;
                    }
                    else/* if(mode == mode.REMOVE)*/{
                        if (listener != null) {

                            hide();
                            if (enchantment != null) {
                                listener.onSelect(item,enchantment);
                            }else if (glyph != null){
                                listener.onSelect(item,glyph);
                            }

                        }
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    public interface Listener {
        void onSelect( Item item,Object enchant );
    }
}
