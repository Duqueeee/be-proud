package me.duquee.beproud.blocks;

import me.duquee.beproud.blocks.printer.PrinterScreenHandler;
import me.duquee.beproud.registry.Register;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;

public class BPScreenHandlers {

    public static final ExtendedScreenHandlerType<PrinterScreenHandler> PRINTER = Register
            .registerExtendedScreenHandler("printer", PrinterScreenHandler::new);

    public static void register() {}

}
