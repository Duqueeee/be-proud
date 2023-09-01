package me.duquee.beproud.registry;

import me.duquee.beproud.BeProud;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockEntityBuilder<T extends BlockEntity> {

    BlockEntityType<T> blockEntityType;

    private final String name;
    private final FabricBlockEntityTypeBuilder.Factory<T> factory;

    private Block[] blocks;
    private Function<BlockEntityRendererFactory.Context, BlockEntityRenderer<T>> renderer;

    protected BlockEntityBuilder(String name, FabricBlockEntityTypeBuilder.Factory<T> factory) {
        this.name = name;
        this.factory = factory;
    }

    public BlockEntityBuilder<T> blocks(Block... blocks) {
        this.blocks = blocks;
        return this;
    }

    public BlockEntityBuilder<T> renderer(Supplier<Function<BlockEntityRendererFactory.Context, BlockEntityRenderer<T>>> factory) {
        if (isClient()) this.renderer = factory.get();
        return this;
    }

    public BlockEntityBuilder<T> register() {
        if (blocks == null) {
            BeProud.LOGGER.error("Tried to register a BlockEntity without associated blocks");
            return this;
        }

        blockEntityType = Registry.register(Registries.BLOCK_ENTITY_TYPE, BeProud.asIdentifier(name),
                FabricBlockEntityTypeBuilder.create(factory, blocks).build());

        if (renderer != null && isClient()) {
            BlockEntityRendererFactories.register(blockEntityType, renderer::apply);
        }

        return this;
    }

    public BlockEntityType<T> getType() {
        return blockEntityType;
    }

    private boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

}
