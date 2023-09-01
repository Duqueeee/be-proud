package me.duquee.beproud.blocks.pole;

import me.duquee.beproud.blocks.BPBlockEntities;
import me.duquee.beproud.blocks.flag.FlagBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PoleBlockEntity extends BlockEntity {

    private FlagBlock flag;
    private Direction facing;
    private BlockPos holder;

    public PoleBlockEntity(BlockPos pos, BlockState state) {
        super(BPBlockEntities.POLE, pos, state);
    }

    private void setFlag(@NotNull FlagBlock flag) {

        if (world == null) return;
        if (this.flag == flag) return;
        if (this.flag != null && this.flag.getClass() == flag.getClass()) {
            this.flag = flag;
            return;
        }

        propagateHolder(null);
        this.flag = flag;
        propagateHolder(pos);

    }

    private void propagateHolder(BlockPos holder) {
        if (world == null) return;
        if (flag == null) return;
        Direction.Axis axis = getAxis();
        int limit = axis.isVertical() ? flag.getHeight() : flag.getWidth();
        this.holder = holder;
        for (int i = 1; i < limit; i++) {
            PoleBlockEntity connected = getConnectedPole(axis, i * getDirection(facing));
            if (connected == null) continue;
            connected.holder = holder;
            connected.markDirty();
        }
    }

    public FlagBlock getFlag() {
        return flag;
    }

    public boolean isAvailable() {
        return holder == null;
    }

    public Direction getFacing() {
        return facing;
    }

    public void placeFlag(FlagBlock flag, Direction facing) {
        PoleBlockEntity holder = getAvailableHolder(flag, facing);
        if (holder == null) return;
        holder.facing = facing;
        holder.setFlag(flag);
        holder.markDirty();
        holder.updateListeners();
    }

    public FlagBlock removeFlag() {
        if (world == null || holder == null) return null;
        if (!(world.getBlockEntity(holder) instanceof PoleBlockEntity pole)) return null;
        pole.propagateHolder(null);
        FlagBlock flag = pole.flag;
        pole.flag = null;
        pole.facing = null;
        pole.markDirty();
        pole.updateListeners();
        return flag;
    }

    private void updateListeners() {
        if (world == null) return;
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }

    @Nullable
    public PoleBlockEntity getAvailableHolder(FlagBlock flag, Direction facing) {

        if (world == null) return null;
        if (!this.isAvailable()) return null;

        Direction.Axis axis = getAxis();
        int limit = axis.isVertical() ? flag.getHeight() : flag.getWidth();
        PoleBlockEntity connected = null;
        int direction = getDirection(facing);

        int i = 1;
        for (; i < limit; i++) {
            connected = getConnectedPole(axis, i * direction);
            if (connected == null || !connected.isAvailable()) break;
        }

        if (i == limit) return this;

        for (int j = 1; j <= limit - i; j++) {
            connected = getConnectedPole(axis, -j * direction);
            if (connected == null || !connected.isAvailable()) return null;
        }

        return connected;
    }

    private PoleBlockEntity getConnectedPole(Direction.Axis axis, int distance) {
        if (world == null) return null;
        BlockEntity neighbor = world.getBlockEntity(pos.offset(axis, distance));
        if (!(neighbor instanceof PoleBlockEntity neighborPole)) return null;
        if (neighborPole.getAxis() != getAxis()) return null;
        return neighborPole;
    }

    public Direction.Axis getAxis() {
        if (world == null) return null;
        return getCachedState().get(PoleBlock.AXIS);
    }

    private int getDirection(Direction facing) {
        Direction.Axis axis = getAxis();
        return axis.isVertical() ? -1 : facing.getDirection().offset() * (axis == Direction.Axis.X ? -1 : 1);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {

        if (holder != null) {
            nbt.put("Holder", NbtHelper.fromBlockPos(holder));
        }

        if (flag != null) {
            Identifier flagId = Registries.BLOCK.getId(flag);
            nbt.putString("Flag", flagId.getNamespace() + ":" + flagId.getPath());
        }

        if (facing != null) {
            nbt.putInt("Facing", facing.getId());
        }

        nbt.putInt("Helper", 0); // placeholder to force the server to send the update packet to the client

    }

    @Override
    public void readNbt(NbtCompound nbt) {

        if (nbt.contains("Holder")) {
            holder = NbtHelper.toBlockPos(nbt.getCompound("Holder"));
        } else holder = null;

        if (nbt.contains("Flag")) {
            Identifier flagId = new Identifier(nbt.getString("Flag"));
            flag = (FlagBlock) Registries.BLOCK.get(flagId);
        } else flag = null;

        if (nbt.contains("Facing")) {
            facing = Direction.byId(nbt.getInt("Facing"));
        } else facing = null;

    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public boolean hasFlag() {
        return flag != null || holder != null;
    }
}
