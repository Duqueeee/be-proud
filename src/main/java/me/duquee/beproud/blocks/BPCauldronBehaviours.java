package me.duquee.beproud.blocks;

import me.duquee.beproud.blocks.flag.FlagBlock;
import me.duquee.beproud.blocks.flag.FlagWrapper;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class BPCauldronBehaviours {

    public static final CauldronBehavior CLEAN_FLAG = (state, world, pos, player, hand, stack) -> {

        if (!(stack.getItem() instanceof BlockItem blockItem)) return ActionResult.PASS;
        if (!(blockItem.getBlock() instanceof FlagBlock flag)) return ActionResult.PASS;
        if (BPBlocks.BLANK_FLAG.isOf(stack)) return ActionResult.PASS;

        if (!world.isClient) {

            ItemStack itemStack = new ItemStack(flag.getBlank().asItem());
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }

            if (stack.isEmpty()) {
                player.setStackInHand(hand, itemStack);
            } else if (player.getInventory().insertStack(itemStack)) {
                player.playerScreenHandler.syncState();
            } else {
                player.dropItem(itemStack, false);
            }

            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }

        return ActionResult.SUCCESS;

    };

    static {
        FlagWrapper.ALL.forEach(wrapper -> {
            for (FlagBlock flag : wrapper.asArray()) {
                CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(flag.asItem(), CLEAN_FLAG);
            }
        });
    }

    public static void register() {}

}
