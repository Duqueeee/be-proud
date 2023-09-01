package me.duquee.beproud.blocks.flag;

import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.util.HorizontalFacingShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SmallFlagBlock extends FlagBlock {

    private static final HorizontalFacingShape SHAPE = new HorizontalFacingShape(0, 2, 15, 16, 14, 16);

    protected SmallFlagBlock(Settings settings) {
        super(settings);
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public float getPoleOffset() {
        return -.46875F;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.getFacing(state.get(FACING));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {

        Direction direction = ctx.getSide();
        if (direction.getAxis().isVertical()) return null;

        Direction opposite = direction.getOpposite();
        BlockPos support = ctx.getBlockPos().offset(opposite);
        World world = ctx.getWorld();

        return isSupportValid(world, support, direction) ? this.getDefaultState().with(FACING, direction) : null;

    }

    @Override
    public FlagBlock getBlank() {
        return BPBlocks.BLANK_FLAG.SMALL;
    }

}
