package me.duquee.beproud.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class HorizontalFacingShape {

    public final VoxelShape NORTH, SOUTH, EAST, WEST;

    public HorizontalFacingShape(double x1, double y1, double z1, double x2, double y2, double z2) {
        NORTH = Block.createCuboidShape(x1, y1, z1, x2, y2, z2);
        SOUTH = Block.createCuboidShape(x1, y1, 16 - z2, x2, y2, 16 - z1);
        EAST = Block.createCuboidShape(16 - z2, y1, x1, 16 - z1, y2, x2);
        WEST = Block.createCuboidShape(z1, y1, x1, z2, y2, x2);
    }

    public VoxelShape getFacing(Direction direction) {
        return switch (direction) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> null;
        };
    }

}
