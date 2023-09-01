package me.duquee.beproud.blocks.flag;

import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.util.HorizontalFacingShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class LargeFlagBlock extends MultiPartFlagBlock {

    private static final HorizontalFacingShape SHAPE = new HorizontalFacingShape(0, 0, 15, 16, 16, 16);

    public static final IntProperty PART = IntProperty.of("part", 0, 11);

    protected LargeFlagBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.getFacing(state.get(FACING));
    }

    @Override
    public IntProperty getPartProperty() {
        return PART;
    }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public float getPoleOffset() {
        return -.59375F;
    }

    @Override
    public FlagBlock getBlank() {
        return BPBlocks.BLANK_FLAG.LARGE;
    }

}
