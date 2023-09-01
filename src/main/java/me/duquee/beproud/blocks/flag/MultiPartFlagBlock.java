package me.duquee.beproud.blocks.flag;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class MultiPartFlagBlock extends FlagBlock {

    protected MultiPartFlagBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(getPartProperty(), 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(getPartProperty()));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        if (state.get(getPartProperty()) != 0) {
            return BlockRenderType.INVISIBLE;
        } else {
            return BlockRenderType.MODEL;
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (!world.isClient && player.isCreative()) {
            int part = state.get(getPartProperty());
            if (part != 0) {
                BlockPos topLeft = getTopLeft(part, state.get(FACING), pos);
                BlockState topLeftState = world.getBlockState(topLeft);
                if (topLeftState.isOf(this) && topLeftState.get(getPartProperty()) == 0) {
                    world.setBlockState(topLeft, Blocks.AIR.getDefaultState(), 35);
                    world.syncWorldEvent(player, 2001, topLeft, Block.getRawIdFromState(topLeftState));
                }
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        Direction direction = ctx.getSide();
        if (direction.getAxis().isVertical()) return null;

        Direction opposite = direction.getOpposite();
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        boolean allowed = false;
        int placePart = 0;
        for (int p = 0; p < getWidth() * getHeight(); p++) {

            allowed = true;
            BlockPos[] parts = getPartsPositions(direction, getTopLeft(p, direction, pos));
            for (int i = 0; i < parts.length; i++) {

                BlockPos part = parts[i];
                BlockPos support = part.offset(opposite);

                if (!isSupportValid(world, support, direction)) {
                    allowed = false;
                    break;
                }
                if (i == p) continue;

                if (world.getBlockState(part).canReplace(ctx) && world.getWorldBorder().contains(part)) continue;
                allowed = false;
                break;
            }

            if (allowed) {
                placePart = p;
                break;
            }

        }

        return allowed ? this.getDefaultState().with(FACING, direction).with(getPartProperty(), placePart) : null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (isPartOfFlag(state, pos, neighborPos)) {
            return neighborState.isOf(this) ? state : Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    private boolean isPartOfFlag(BlockState state, BlockPos pos, BlockPos neighborPos) {

        int part = state.get(getPartProperty());
        Direction facing = state.get(FACING);

        BlockPos v = getTopLeft(part, facing, pos).subtract(neighborPos);
        if (v.getY() < 0 || v.getY() >= getHeight()) return false;

        switch (facing) {
            case NORTH -> {
                return v.getZ() == 0 && v.getX() >= 0 && v.getX() < getWidth();
            }
            case SOUTH -> {
                return v.getZ() == 0 && v.getX() <= 0 && v.getX() > -getWidth();
            }
            case EAST -> {
                return v.getX() == 0 && v.getZ() >= 0 && v.getZ() < getWidth();
            }
            case WEST -> {
                return v.getX() == 0 && v.getZ() <= 0 && v.getZ() > -getWidth();
            }
        }

        return false;

    }

    private BlockPos getTopLeft(int part, Direction facing, BlockPos pos) {

        int y = part/getWidth();
        int x = 0, z = 0;
        switch (facing) {
            case NORTH -> x = part % getWidth();
            case SOUTH -> x = -part % getWidth();
            case EAST -> z = part % getWidth();
            case WEST -> z = -part % getWidth();
        }

        return pos.add(x, y, z);

    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

        if (!world.isClient) {

            int part = state.get(getPartProperty());
            BlockPos topLeft = getTopLeft(part, state.get(FACING), pos);

            BlockPos[] parts = getPartsPositions(state.get(FACING), topLeft);
            for (int i = 0; i < parts.length; i++) {
                if (i == part) continue;
                world.setBlockState(parts[i], state.with(getPartProperty(), i), 3);
            }

            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, 3);
        }
    }

    protected BlockPos[] getPartsPositions(Direction facing, BlockPos topLeft) {
        if (facing.getAxis().isVertical()) return new BlockPos[0];
        BlockPos[] positions = new BlockPos[getWidth() * getHeight()];
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                positions[x + getWidth() * y] =  switch (facing) {
                    case NORTH -> topLeft.add(-x, -y, 0);
                    case SOUTH -> topLeft.add(x, -y, 0);
                    case EAST -> topLeft.add(0, -y, -x);
                    case WEST -> topLeft.add(0, -y, x);
                    default -> null;
                };
            }
        }
        return positions;
    }

    public abstract IntProperty getPartProperty();

}
