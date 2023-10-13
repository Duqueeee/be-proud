package me.duquee.beproud.compat.rei.printer;

import me.duquee.beproud.blocks.printer.DyeTooltipComponent;
import me.duquee.beproud.recipe.PrinterRecipe;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.DrawableConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class REIDyesDrawableConsumer implements DrawableConsumer {

    private final Point point;
    private final PrinterRecipe recipe;

    public REIDyesDrawableConsumer(Point point, PrinterRecipe recipe) {
        this.recipe = recipe;
        this.point = point;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        DyeTooltipComponent.drawDyeRequisites(
                MinecraftClient.getInstance().textRenderer,
                this.point.x, this.point.y, context, recipe.getDyes());
    }

}
