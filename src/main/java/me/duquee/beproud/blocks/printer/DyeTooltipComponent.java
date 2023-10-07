package me.duquee.beproud.blocks.printer;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DyeTooltipComponent implements TooltipComponent {

    private static final ItemStack[] stacks = new ItemStack[]{
            new ItemStack(Items.CYAN_DYE),
            new ItemStack(Items.MAGENTA_DYE),
            new ItemStack(Items.YELLOW_DYE)
    };

    public final int[] dyes;

    public DyeTooltipComponent(int[] dyes) {
        this.dyes = dyes;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        drawDyeRequisites(textRenderer, x, y, context, dyes);
    }

    @Override
    public int getHeight() {
        return 20;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 90;
    }

    public static void drawDyeRequisites(TextRenderer textRenderer, int x, int y, DrawContext context, int[] dyes) {
        for (int i = 0; i < 3; i++) {
            context.drawItem(stacks[i], x + 30*i, y);
        }

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 200);
        for (int i = 0; i < 3; i++) {
            context.drawText(textRenderer, String.valueOf(dyes[i]), x + 30*i, y + 9, 16777215, true);
        }

        context.getMatrices().scale(.8F, .8F, .8F);
        for (int i = 0; i < 3; i++) {
            context.drawText(textRenderer, "/64", (int) ((x + 30*i + textRenderer.getWidth(String.valueOf(dyes[i])))/.8F), (int) ((y + 11)/.8F), 10263959, true);
        }

        context.getMatrices().pop();
    }

}
