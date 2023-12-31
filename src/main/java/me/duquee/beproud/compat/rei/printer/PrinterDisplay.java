package me.duquee.beproud.compat.rei.printer;

import me.duquee.beproud.recipe.PrinterRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;
import java.util.Optional;

public class PrinterDisplay extends BasicDisplay {

    private final PrinterRecipe recipe;

    public PrinterDisplay(PrinterRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getIngredients()),
                Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.ofNullable(recipe.getId()));
        this.recipe = recipe;
    }

    @Override
    public CategoryIdentifier<PrinterDisplay> getCategoryIdentifier() {
        return PrinterRecipeCategory.PRINTER_DISPLAY;
    }

    public PrinterRecipe getRecipe() {
        return recipe;
    }

}
