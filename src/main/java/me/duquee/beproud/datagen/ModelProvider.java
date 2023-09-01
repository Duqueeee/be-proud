package me.duquee.beproud.datagen;

import com.google.gson.JsonElement;
import me.duquee.beproud.BeProud;
import me.duquee.beproud.blocks.flag.FlagWrapper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class ModelProvider extends FabricModelProvider {

    public static final TextureKey FLAG_TEXTURE = TextureKey.of("flag");

    public static final BiFunction<String, String, TexturedModel> SMALL_FLAG_FACTORY = makeFactory(ModelProvider::flag, flagModel("small"));
    public static final BiFunction<String, String, TexturedModel> STANDARD_FLAG_FACTORY = makeFactory(ModelProvider::flag, flagModel("standard"));
    public static final BiFunction<String, String, TexturedModel> LARGE_FLAG_FACTORY = makeFactory(ModelProvider::flag, flagModel("large"));

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        FlagWrapper.ALL.forEach(wrapper -> {
            registerSmallFlag(blockStateModelGenerator, wrapper.getName(), wrapper.SMALL);
            registerStandardFlag(blockStateModelGenerator, wrapper.getName(), wrapper.STANDARD);
            registerLargeFlag(blockStateModelGenerator, wrapper.getName(), wrapper.LARGE);
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        FlagWrapper.ALL.forEach(wrapper -> {
            for (Block flag : wrapper.asArray()) {
                registerFlagItem(itemModelGenerator, flag.asItem(), wrapper.getName());
            }
        });
    }

    public void registerFlagItem(ItemModelGenerator itemModelGenerator, Item item, String type) {
        Models.GENERATED.upload(ModelIds.getItemModelId(item), layer0(type), itemModelGenerator.writer);
    }

    public static TextureMap layer0(String type) {
        return (new TextureMap()).put(TextureKey.LAYER0, BeProud.asIdentifier("item/flag/" + type));
    }

    public void registerSmallFlag(BlockStateModelGenerator blockStateModelGenerator, String name, Block block) {
        registerFlag(blockStateModelGenerator, "small", name, block, SMALL_FLAG_FACTORY);
    }

    public void registerStandardFlag(BlockStateModelGenerator blockStateModelGenerator, String name, Block block) {
        registerFlag(blockStateModelGenerator, "standard", name, block, STANDARD_FLAG_FACTORY);
    }

    public void registerLargeFlag(BlockStateModelGenerator blockStateModelGenerator, String name, Block block) {
        registerFlag(blockStateModelGenerator, "large", name, block, LARGE_FLAG_FACTORY);
    }

    public void registerFlag(BlockStateModelGenerator blockStateModelGenerator, String size, String name, Block block, BiFunction<String, String, TexturedModel> factory) {
        blockStateModelGenerator.blockStateCollector
                .accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, upload(factory, size, name, blockStateModelGenerator.modelCollector)))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
    }

    public static TextureMap flag(String size, String name) {
        Identifier id = BeProud.asIdentifier("block/flag/" + name + "/" + size);
        return (new TextureMap()).put(FLAG_TEXTURE, id).put(TextureKey.PARTICLE, id);
    }

    public static Identifier upload(BiFunction<String, String, TexturedModel> factory, String size, String name, BiConsumer<Identifier, Supplier<JsonElement>> writer) {
        TexturedModel texturedModel = factory.apply(size, name);
        return texturedModel.getModel().upload(
                BeProud.asIdentifier("block/flag/" + name + "/" + size),
                texturedModel.getTextures(), writer);
    }

    public static BiFunction<String, String, TexturedModel> makeFactory(BiFunction<String, String, TextureMap> texturesGetter, Model model) {
        return (size, name) -> new TexturedModel(texturesGetter.apply(size, name), model);
    }

    private static Model block(String parent, TextureKey... requiredTextureKeys) {
        return new Model(Optional.of(BeProud.asIdentifier("block/" + parent)), Optional.empty(), requiredTextureKeys);
    }

    private static Model flagModel(String size) {
        return block("flag/base/" + size, FLAG_TEXTURE, TextureKey.PARTICLE);
    }

}
