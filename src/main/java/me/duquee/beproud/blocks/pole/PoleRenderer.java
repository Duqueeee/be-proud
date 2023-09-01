package me.duquee.beproud.blocks.pole;

import me.duquee.beproud.blocks.flag.FlagBlock;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class PoleRenderer implements BlockEntityRenderer<PoleBlockEntity> {

    private final BlockRenderManager blockRenderer;

    public PoleRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderer = ctx.getRenderManager();
    }

    @Override
    public void render(PoleBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        FlagBlock flag = entity.getFlag();
        if (flag == null) return;

        Direction.Axis axis = entity.getAxis();
        BakedModel flagModel = blockRenderer.getModel(flag.getDefaultState());

        long t = entity.getWorld() == null ? 0 : entity.getWorld().getTime();
        float l = MathHelper.sin(0.062831855F * Math.floorMod(t, 100L)) * 3.1415927F / flag.getHeight();

        matrices.push();
        matrices.multiply(getRotationAxis(axis).rotationDegrees(l),.5F, .5F, .5F);

        matrices.multiply(getRotation(entity.getFacing(), axis), .5F, .5F, .5F);

        if (axis.isVertical()) {
            matrices.translate(-.59375F, 0, -.46875F);
        } else {
            matrices.translate(0, flag.getPoleOffset(), -.46875F);
        }

        blockRenderer.getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout()), null,
                flagModel, 0, 0, 0, light, overlay);
        matrices.pop();

    }

    private RotationAxis getRotationAxis(Direction.Axis axis) {
        return switch (axis) {
            case X -> RotationAxis.POSITIVE_X;
            case Y -> RotationAxis.POSITIVE_Y;
            case Z -> RotationAxis.POSITIVE_Z;
        };
    }

    private Quaternionf getRotation(Direction facing, Direction.Axis axis) {
        float rotation = facing.getAxis() == Direction.Axis.X ? (axis.isVertical() ? 180 : 90) : 0;
        if (axis == Direction.Axis.Z) rotation += 90;
        return RotationAxis.POSITIVE_Y.rotationDegrees(facing.asRotation() + rotation);
    }

}
