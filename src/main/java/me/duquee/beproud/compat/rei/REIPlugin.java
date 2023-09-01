package me.duquee.beproud.compat.rei;

import me.duquee.beproud.compat.rei.printer.PrinterDisplay;
import me.duquee.beproud.compat.rei.printer.PrinterRecipeCategory;
import me.duquee.beproud.recipe.PrinterRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;

public class REIPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(PrinterRecipe.class, PrinterRecipe.Type.INSTANCE, PrinterDisplay::new);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PrinterRecipeCategory());
    }

}
