package me.duquee.beproud.compat.emi.printer;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.duquee.beproud.recipe.PrinterRecipe;

public class EMIPrinterRecipe extends BasicEmiRecipe {

    private final int[] dyes;

    public EMIPrinterRecipe(PrinterRecipe recipe) {
        super(EMIPrinterRecipeCategory.INSTANCE, recipe.getId(), 125, 44);
        this.dyes = recipe.getDyes();
        this.inputs.add(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.outputs.add(EmiStack.of(recipe.getOutput()));
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 51, 4);
        widgets.addSlot(inputs.get(0), 28, 4);
        widgets.addSlot(outputs.get(0), 80, 4).recipeContext(this);
        widgets.add(new EMIDyesWidget(dyes, 8, 23));
    }

}
