package me.duquee.beproud.compat.rei.printer;

import com.google.common.collect.Lists;
import me.duquee.beproud.BeProud;
import me.duquee.beproud.blocks.BPBlocks;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.text.Text;

import java.util.List;

public class REIPrinterRecipeCategory implements DisplayCategory<REIPrinterDisplay> {

    public static final CategoryIdentifier<REIPrinterDisplay> PRINTER_DISPLAY = CategoryIdentifier.of(BeProud.MOD_ID, "printer");

    @Override
    public List<Widget> setupDisplay(REIPrinterDisplay display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 41, bounds.getCenterY() - 22);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 27, startPoint.y + 4)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 61, startPoint.y + 5)));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 61, startPoint.y + 5))
                .entries(display.getOutputEntries().get(0))
                .disableBackground()
                .markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 5))
                .entries(display.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createDrawableWidget(
                new REIDyesDrawableConsumer(
                        new Point(startPoint.x - 12, startPoint.y + 27),
                        display.getRecipe())));
        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public CategoryIdentifier<REIPrinterDisplay> getCategoryIdentifier() {
        return PRINTER_DISPLAY;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("rei.category.beproud.printer");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(BPBlocks.PRINTER);
    }
}
