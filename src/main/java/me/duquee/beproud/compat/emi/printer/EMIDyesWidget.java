package me.duquee.beproud.compat.emi.printer;

import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import me.duquee.beproud.blocks.printer.DyeTooltipComponent;
import me.duquee.beproud.recipe.PrinterRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class EMIDyesWidget extends Widget {

    private final int x, y;
    private final int[] dyes;

    public EMIDyesWidget(int[] dyes, int x, int y) {
        this.dyes = dyes;
        this.x = x;
        this.y = y;
    }

    @Override
    public Bounds getBounds() {
        return Bounds.EMPTY;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        DyeTooltipComponent.drawDyeRequisites(
                MinecraftClient.getInstance().textRenderer,
                x, y, context, dyes);
    }

}
