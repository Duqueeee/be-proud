package me.duquee.beproud.blocks.flag;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public abstract class FlagBlock extends HorizontalFacingBlock {

    private FlagWrapper wrapper;

    protected FlagBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING).getOpposite()) {
            return isSupportValid(world, neighborPos, direction) ? state : Blocks.AIR.getDefaultState();
        } else {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    protected boolean isSupportValid(WorldAccess world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos).isSideSolidFullSquare(world, pos, direction);
    }

    protected void setWrapper(FlagWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public FlagWrapper getWrapper() {
        return wrapper;
    }

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract float getPoleOffset();

    public abstract FlagBlock getBlank();

    public static SmallFlagBlock small(Settings settings) {
        return new SmallFlagBlock(settings);
    }

    public static StandardFlagBlock standard(Settings settings) {
        return new StandardFlagBlock(settings);
    }

    public static LargeFlagBlock large(Settings settings) {
        return new LargeFlagBlock(settings);
    }

}
