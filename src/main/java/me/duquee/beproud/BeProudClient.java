package me.duquee.beproud;

import me.duquee.beproud.blocks.BPScreenHandlers;
import me.duquee.beproud.blocks.printer.PrinterScreen;
import me.duquee.beproud.events.ClientEvents;
import me.duquee.beproud.items.book.PrideBookScreen;
import me.duquee.beproud.registry.Register;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class BeProudClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        Register.registerScreen(BPScreenHandlers.PRINTER, PrinterScreen::new);
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                PrideBookScreen.ResourceReloadListener.INSTANCE);
        ClientEvents.register();

    }
}
