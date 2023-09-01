package me.duquee.beproud.blocks;

import me.duquee.beproud.blocks.pole.PoleBlockEntity;
import me.duquee.beproud.blocks.pole.PoleRenderer;
import me.duquee.beproud.blocks.printer.PrinterBlockEntity;
import me.duquee.beproud.registry.Register;
import net.minecraft.block.entity.BlockEntityType;

public class BPBlockEntities {

    public static final BlockEntityType<PrinterBlockEntity> PRINTER = Register
            .blockEntity("printer", PrinterBlockEntity::new)
            .blocks(BPBlocks.PRINTER)
            .register()
            .getType();

    public static final BlockEntityType<PoleBlockEntity> POLE = Register
            .blockEntity("pole", PoleBlockEntity::new)
            .blocks(BPBlocks.POLE)
            .renderer(() -> PoleRenderer::new)
            .register()
            .getType();

    public static void register() {}

}
