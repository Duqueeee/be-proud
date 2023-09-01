package me.duquee.beproud.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class AxialShape {

    public final VoxelShape X, Y, Z;

    public AxialShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        X = Block.createCuboidShape(y1, x1, z1, y2, x2, z2);
        Y = Block.createCuboidShape(x1, y1, z1, x2, y2, z2);
        Z = Block.createCuboidShape(x1, z1, y1, x2, z2, y2);
    }

    public VoxelShape getFacing(Direction.Axis axis) {
        return switch (axis) {
            case X -> X;
            case Y -> Y;
            case Z -> Z;
        };
    }

}
