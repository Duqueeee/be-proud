package me.duquee.beproud.items.book;

import joptsimple.internal.Strings;
import me.duquee.beproud.blocks.flag.FlagBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TooltipChapterHandler {

    private static float progress = 0;
    private static ItemStack currentStack;

    public static void addTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip) {

        if (progress > 0 && stack != currentStack) progress = 0;

        if (!(stack.getItem() instanceof BlockItem item && item.getBlock() instanceof FlagBlock flag)) return;
        if (flag.getWrapper().isBlank()) return;

        currentStack = stack;

        if (isHolding() && progress < 1) progress += .02F;
        else if (progress > 0) progress -= .02F;

        if (progress > 0) {
            if (progress >= 1) {
                PrideBookItem.openPrideBook(flag.getWrapper().getChapterPage());
                progress = 0;
            } else {
                tooltip.add(progressBar());
            }
        } else {
            tooltip.add(holdToOpen());
        }

    }

    private static boolean isHolding() {
        int key = KeyBindingHelper.getBoundKeyOf(getHoldKey()).getCode();
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key);
    }

    private static KeyBinding getHoldKey() {
        return MinecraftClient.getInstance().options.forwardKey;
    }

    private static Text holdToOpen() {
        return Text.translatable("beproud.flag.holdToOpen", ((MutableText) getHoldKey().getBoundKeyLocalizedText())
                .formatted(Formatting.GRAY)).formatted(Formatting.DARK_GRAY);
    }

    private static Text progressBar() {

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int length = textRenderer.getWidth(holdToOpen()) / textRenderer.getWidth("|");
        int completed = (int) (progress * length);

        String progressBar = Formatting.GRAY + Strings.repeat('|', completed);
        progressBar += Formatting.DARK_GRAY + Strings.repeat('|', length - completed);
        return Text.literal(progressBar);

    }

}
