package me.duquee.beproud.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import me.duquee.beproud.compat.emi.printer.EMIPrinterRecipeCategory;

public class EMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        EMIPrinterRecipeCategory.register(registry);
    }

}
