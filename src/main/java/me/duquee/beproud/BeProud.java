package me.duquee.beproud;

import me.duquee.beproud.blocks.BPBlockEntities;
import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.blocks.BPCauldronBehaviours;
import me.duquee.beproud.blocks.BPScreenHandlers;
import me.duquee.beproud.items.BPItems;
import me.duquee.beproud.paintings.BPPaintings;
import me.duquee.beproud.recipe.BPRecipes;
import me.duquee.beproud.sounds.BPSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeProud implements ModInitializer {

    public static final String MOD_ID = "beproud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        BPItems.register();
        BPBlocks.register();
        BPBlockEntities.register();
        BPScreenHandlers.register();
        BPRecipes.register();
        BPPaintings.register();
        BPCauldronBehaviours.register();
        BPSounds.register();
    }

    public static Identifier asIdentifier(String id) {
        return new Identifier(MOD_ID, id);
    }

}
