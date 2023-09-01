package me.duquee.beproud.registry;

import me.duquee.beproud.BeProud;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Consumer;
import java.util.function.Function;

public class BlockBuilder<T extends Block> {

    private T block;

    private final String name;
    private final FabricBlockSettings settings = FabricBlockSettings.create();

    private final Function<FabricBlockSettings, T> factory;

    protected BlockBuilder(String name, Function<FabricBlockSettings, T> factory) {
        this.name = name;
        this.factory = factory;
    }

    public BlockBuilder<T> settings(Consumer<FabricBlockSettings> settings) {
        settings.accept(this.settings);
        return this;
    }

    public BlockItemBuilder<T> item() {
        register();
        return new BlockItemBuilder<>(this);
    }

    public BlockBuilder<T> register() {
        block = factory.apply(settings);
        Registry.register(Registries.BLOCK, BeProud.asIdentifier(name), block);
        return this;
    }

    public T getBlock() {
        return block;
    }

    public static class BlockItemBuilder<T extends Block> extends ItemBuilder<BlockItem, BlockItemBuilder<T>> {

        private final BlockBuilder<T> blockBuilder;

        private BlockItemBuilder(BlockBuilder<T> blockBuilder) {
            super(blockBuilder.name, s -> new BlockItem(blockBuilder.block, s));
            this.blockBuilder = blockBuilder;
        }

        public T getBlock() {
            return blockBuilder.block;
        }

    }

}
