package me.duquee.beproud.blocks.flag;

import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.util.HorizontalFacingShape;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class StandardFlagBlock extends MultiPartFlagBlock {

    public static final HorizontalFacingShape TOP = new HorizontalFacingShape(0, 0, 15, 16, 12, 16);
    public static final HorizontalFacingShape BOTTOM = new HorizontalFacingShape(0, 4, 15, 16, 16, 16);

    public static final IntProperty PART = IntProperty.of("part", 0, 3);

    protected StandardFlagBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (isTop(state) ? TOP : BOTTOM).getFacing(state.get(FACING));
    }

    private boolean isTop(BlockState state) {
        return state.get(PART) < 2;
    }

    @Override
    public IntProperty getPartProperty() {
        return PART;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public float getPoleOffset() {
        return -.34375F;
    }

    @Override
    public FlagBlock getBlank() {
        return BPBlocks.BLANK_FLAG.STANDARD;
    }
}
