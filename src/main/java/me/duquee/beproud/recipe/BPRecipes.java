package me.duquee.beproud.recipe;

import me.duquee.beproud.BeProud;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BPRecipes {

    public static void register() {
        Registry.register(Registries.RECIPE_SERIALIZER, BeProud.asIdentifier(PrinterRecipe.Serializer.ID),
                PrinterRecipe.Serializer.INSTANCE);
        Registry.register(Registries.RECIPE_TYPE, BeProud.asIdentifier(PrinterRecipe.Type.ID),
                PrinterRecipe.Type.INSTANCE);
    }

}
