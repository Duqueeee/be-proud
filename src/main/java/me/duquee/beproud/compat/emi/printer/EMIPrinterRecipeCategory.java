package me.duquee.beproud.compat.emi.printer;

import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import me.duquee.beproud.BeProud;
import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.blocks.printer.PrinterScreen;
import me.duquee.beproud.recipe.PrinterRecipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

public class EMIPrinterRecipeCategory extends EmiRecipeCategory {

    public static final EmiStack WORKSTATION = EmiStack.of(BPBlocks.PRINTER);
    public static final Identifier ID = BeProud.asIdentifier("printer");

    public static final EMIPrinterRecipeCategory INSTANCE = new EMIPrinterRecipeCategory();

    public EMIPrinterRecipeCategory() {
        super(ID, WORKSTATION, new EmiTexture(PrinterScreen.TEXTURE, 0, 0, 16, 16));
    }

    public static void register(EmiRegistry registry) {

        registry.addCategory(EMIPrinterRecipeCategory.INSTANCE);
        registry.addWorkstation(EMIPrinterRecipeCategory.INSTANCE, EMIPrinterRecipeCategory.WORKSTATION);

        RecipeManager manager = registry.getRecipeManager();
        for (PrinterRecipe recipe : manager.listAllOfType(PrinterRecipe.Type.INSTANCE)) {
            registry.addRecipe(new EMIPrinterRecipe(recipe));
        }

    }

}
