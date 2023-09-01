package me.duquee.beproud.util;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public interface BEE<T extends BlockEntity> {

    Class<T> getBlockEntityClass();

    default <B> B withBlockEntity(World world, BlockPos pos, Function<T, B> execute) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null || blockEntity.getClass() != getBlockEntityClass()) return null;
        return execute.apply((T) blockEntity);
    }

    default void withBlockEntity(World world, BlockPos pos, Consumer<T> execute) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity == null || blockEntity.getClass() != getBlockEntityClass()) return;
        execute.accept((T) blockEntity);
    }

}
