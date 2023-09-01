package me.duquee.beproud.blocks.printer;

import me.duquee.beproud.util.BEE;
import me.duquee.beproud.util.HorizontalFacingShape;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class PrinterBlock extends BlockWithEntity implements BEE<PrinterBlockEntity> {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private static final HorizontalFacingShape SHAPE = new HorizontalFacingShape(1, 0, 5, 15, 5, 14);

    public PrinterBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.getFacing(state.get(FACING));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return isSupportValid(world, pos.down());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction != Direction.DOWN) return state;
        return isSupportValid(world, pos.down()) ? state : Blocks.AIR.getDefaultState();
    }

    private boolean isSupportValid(WorldView world, BlockPos pos) {
        return world.getBlockState(pos).isSideSolidFullSquare(world, pos, Direction.UP);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PrinterBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {

            ItemStack stack = player.getStackInHand(hand);
            boolean success = withBlockEntity(world, pos, printer -> {
                return printer.putDye(stack);
            });

            if (success) {
                if (!player.getAbilities().creativeMode) stack.decrement(1);
                return ActionResult.SUCCESS;
            }

            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if (factory != null) {
                player.openHandledScreen(factory);
            }

        }
        return ActionResult.SUCCESS;
    }

    @Override
    public Class<PrinterBlockEntity> getBlockEntityClass() {
        return PrinterBlockEntity.class;
    }

}
