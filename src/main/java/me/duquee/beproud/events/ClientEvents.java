package me.duquee.beproud.events;

import me.duquee.beproud.items.book.TooltipChapterHandler;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class ClientEvents {

    public static void register() {
        ItemTooltipCallback.EVENT.register(TooltipChapterHandler::addTooltip);
    }

}
