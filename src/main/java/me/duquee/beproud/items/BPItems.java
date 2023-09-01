package me.duquee.beproud.items;

import me.duquee.beproud.groups.BPItemGroups;
import me.duquee.beproud.items.book.PrideBookItem;
import me.duquee.beproud.registry.Register;
import net.minecraft.item.Item;

public class BPItems {

    static {
        Register.inGroup(BPItemGroups.BE_PROUD);
    }

    public static final Item PRIDE_BOOK = Register
            .item("pride_book", PrideBookItem::new)
            .register()
            .getItem();

    public static void register() {}

}
