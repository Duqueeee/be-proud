package me.duquee.beproud.blocks.pole;

import me.duquee.beproud.blocks.flag.FlagBlock;
import me.duquee.beproud.util.AxialShape;
import me.duquee.beproud.util.BEE;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class PoleBlock extends BlockWithEntity implements BEE<PoleBlockEntity>, Waterloggable {

    public static final EnumProperty<Direction.Axis> AXIS = PillarBlock.AXIS;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final AxialShape SHAPE = new AxialShape(6.5, 0, 6.5, 9.5, 16, 9.5);

    public PoleBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(AXIS, Direction.Axis.Y).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS).add(WATERLOGGED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.getFacing(state.get(AXIS));
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis()).with(WATERLOGGED, bl);
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return state;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PoleBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (player.isSneaking() && hand == Hand.MAIN_HAND) {
            if (!world.isClient) {
                FlagBlock flag = withBlockEntity(world, pos, PoleBlockEntity::removeFlag);
                if (flag != null) {
                    ItemStack flagStack = new ItemStack(flag.asItem());
                    if (!player.getInventory().insertStack(flagStack))
                        player.dropItem(flagStack, false);
                }
            }
            return ActionResult.SUCCESS;
        }

        ItemStack stack = player.getStackInHand(hand);
        if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof FlagBlock flag) {

            boolean hasFlag = withBlockEntity(world, pos, PoleBlockEntity::hasFlag);
            if (hasFlag) return ActionResult.PASS;

            if (!world.isClient) {
                if (!player.getAbilities().creativeMode) stack.decrement(1);
                withBlockEntity(world, pos, pole -> {
                    pole.placeFlag(flag, player.getHorizontalFacing());
                });
            }
            return ActionResult.SUCCESS;

        } else {
            return ActionResult.PASS;
        }

    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (world.isClient) return;
        FlagBlock flag = withBlockEntity(world, pos, PoleBlockEntity::removeFlag);
        if (flag != null) {
            ItemStack flagStack = new ItemStack(flag.asItem());
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), flagStack);
        }
    }

    @Override
    public Class<PoleBlockEntity> getBlockEntityClass() {
        return PoleBlockEntity.class;
    }

}
