package me.duquee.beproud.blocks;

import me.duquee.beproud.blocks.flag.*;
import me.duquee.beproud.blocks.pole.PoleBlock;
import me.duquee.beproud.blocks.printer.PrinterBlock;
import me.duquee.beproud.groups.BPItemGroups;
import me.duquee.beproud.registry.Register;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class BPBlocks {

    static {
        Register.inGroup(BPItemGroups.BE_PROUD);
    }

    public static final Block PRINTER = Register
            .block("printer", PrinterBlock::new)
            .settings(settings -> settings
                    .nonOpaque()
                    .strength(3.5F)
                    .requiresTool())
            .item()
            .register()
            .getBlock();

    public static final Block POLE = Register
            .block("pole", PoleBlock::new)
            .settings(settings -> settings
                    .nonOpaque()
                    .strength(1.5F, 6.0F)
                    .requiresTool())
            .item()
            .register()
            .getBlock();

    public static final FlagWrapper BLANK_FLAG = createFlag("blank");
    public static final FlagWrapper LGBTQIA_FLAG = createFlag("lgbtqia");
    public static final FlagWrapper PROGRESS_FLAG = createFlag("progress");
    public static final FlagWrapper NEW_PROGRESS_FLAG = createFlag("new_progress");
    public static final FlagWrapper LESBIAN_FLAG = createFlag("lesbian");
    public static final FlagWrapper GAY_FLAG = createFlag("gay");
    public static final FlagWrapper BISEXUAL_FLAG = createFlag("bisexual");
    public static final FlagWrapper TRANS_FLAG = createFlag("trans");
    public static final FlagWrapper INTERSEX_FLAG = createFlag("intersex");
    public static final FlagWrapper ASEXUAL_FLAG = createFlag("asexual");
    public static final FlagWrapper NON_BINARY_FLAg = createFlag("non_binary");
    public static final FlagWrapper DEMISEXUAL_FLAG = createFlag("demisexual");
    public static final FlagWrapper PANSEXUAL_FLAG = createFlag("pansexual");
    public static final FlagWrapper ABROSEXUAL_FLAG = createFlag("abrosexual");
    public static final FlagWrapper MAVERIQUE_FLAG = createFlag("maverique");
    public static final FlagWrapper BIGENDER_FLAG = createFlag("bigender");
    public static final FlagWrapper GRAYSEXUAL_FLAG = createFlag("graysexual");
    public static final FlagWrapper GENDERFLUX_FLAG = createFlag("genderflux");
    public static final FlagWrapper GENDERQUEER_FLAG = createFlag("genderqueer");
    public static final FlagWrapper DEMIBOY_FLAG = createFlag("demiboy");
    public static final FlagWrapper DEMIGIRL_FLAG = createFlag("demigirl");
    public static final FlagWrapper GENDERFLUID_FLAG = createFlag("genderfluid");
    public static final FlagWrapper AGENDER_FLAG = createFlag("agender");
    public static final FlagWrapper AROMANTIC_FLAG = createFlag("aromantic");
    public static final FlagWrapper POLYAMORY_FLAG = createFlag("polyamory");
    public static final FlagWrapper POLYSEXUAL_FLAG = createFlag("polysexual");
    public static final FlagWrapper ANDROGYNE_FLAG = createFlag("androgyne");
    public static final FlagWrapper OMNIGENDER_FLAG = createFlag("omnigender");
    public static final FlagWrapper OMNISEXUAL_FLAG = createFlag("omnisexual");
    public static final FlagWrapper TRIGENDER_FLAG = createFlag("trigender");
    public static final FlagWrapper PANGENDER_FLAG = createFlag("pangender");
    public static final FlagWrapper ANDROSEXUAL_FLAG = createFlag("androsexual");
    public static final FlagWrapper GYNESEXUAL_FLAG = createFlag("gynesexual");

    private static FlagWrapper createFlag(String type) {

        FlagBlock small = registerFlag("small_" + type + "_flag", FlagBlock::small);
        FlagBlock standard = registerFlag(type + "_flag", FlagBlock::standard);
        FlagBlock large = registerFlag("large_" + type + "_flag", FlagBlock::large);

        return new FlagWrapper(type, small, standard, large);
    }

    private static <T extends FlagBlock> T registerFlag(String id, Function<FabricBlockSettings,  T> factory) {
        return Register
                .block(id, factory)
                .settings(settings -> settings
                        .nonOpaque()
                        .strength(0.5F)
                        .sounds(BlockSoundGroup.WOOL)
                        .noCollision()
                        .pistonBehavior(PistonBehavior.DESTROY))
                .item()
                .register()
                .getBlock();
    }

    public static void register() {}

}
