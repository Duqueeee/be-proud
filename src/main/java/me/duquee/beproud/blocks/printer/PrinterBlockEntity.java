package me.duquee.beproud.blocks.printer;

import me.duquee.beproud.blocks.BPBlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class PrinterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    public static final int MAX_DYE = 256;
    protected int[] dyes = new int[3];

    public PrinterBlockEntity(BlockPos pos, BlockState state) {
        super(BPBlockEntities.PRINTER, pos, state);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new PrinterScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(world, pos));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        dyes = nbt.getIntArray("Dyes");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putIntArray("Dyes", dyes);
    }

    public void incrementDye(int index, int amount) {
        if (world != null && world.isClient) return;
        if (amount == 0) return;
        dyes[index] = MathHelper.clamp(dyes[index] + amount, 0, MAX_DYE);
        markDirty();
        updateListeners();
    }

    public void decrementDye(int index, int amount) {
        incrementDye(index, -amount);
    }

    public boolean putDye(ItemStack stack) {
        int index = getIndexFromDye(stack.getItem());
        if (index == -1 || dyes[index] + 64 > MAX_DYE) return false;
        incrementDye(index, 64);
        return true;
    }

    public int getDye(int index) {
        return dyes[index];
    }

    private int getIndexFromDye(Item item) {
        if (!(item instanceof DyeItem dye)) return -1;
        DyeColor color = dye.getColor();
        return switch (color) {
            case CYAN -> 0;
            case MAGENTA -> 1;
            case YELLOW -> 2;
            default -> -1;
        };
    }

    private void updateListeners() {
        if (world == null) return;
        world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeIntArray(dyes);
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

}
