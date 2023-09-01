package me.duquee.beproud.paintings;

import me.duquee.beproud.BeProud;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BPPaintings {

    public static final PaintingVariant QUEER_LANDING = registerPainting("queer_landing", new PaintingVariant(32, 16));

    private static PaintingVariant registerPainting(String name, PaintingVariant variant) {
        return Registry.register(Registries.PAINTING_VARIANT, BeProud.asIdentifier(name), variant);
    }

    public static void register() {}

}
