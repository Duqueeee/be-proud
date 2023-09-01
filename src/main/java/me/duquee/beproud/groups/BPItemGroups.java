package me.duquee.beproud.groups;

import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.registry.Register;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

public class BPItemGroups {

    public static final RegistryKey<ItemGroup> BE_PROUD = Register.group("be_proud")
            .builder(builder -> builder
                    .icon(() -> new ItemStack(BPBlocks.LGBTQIA_FLAG.SMALL.asItem()))
                    .displayName(Text.translatable("itemGroup.be_proud")))
            .register()
            .getKey();

}
