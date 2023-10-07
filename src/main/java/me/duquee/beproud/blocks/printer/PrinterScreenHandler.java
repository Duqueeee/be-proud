package me.duquee.beproud.blocks.printer;

import me.duquee.beproud.blocks.BPScreenHandlers;
import me.duquee.beproud.recipe.PrinterRecipe;
import me.duquee.beproud.sounds.BPSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PrinterScreenHandler extends ScreenHandler {

    private Runnable listener;

    private final ScreenHandlerContext context;
    private final World world;
    private PrinterBlockEntity printer;

    private final Inventory input = new SimpleInventory(1) {
        @Override
        public void markDirty() {
            super.markDirty();
            onContentChanged(this);
            if (listener != null) listener.run();
        }
    };
    private final CraftingResultInventory output = new CraftingResultInventory();

    private Slot inputSlot, outputSlot;

    private ItemStack inputStack = ItemStack.EMPTY;

    private List<PrinterRecipe> recipes = new ArrayList<>();
    private final Property selected = Property.create();

    private long lastTakeTime;

    public PrinterScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, null, ScreenHandlerContext.EMPTY);
        this.printer = (PrinterBlockEntity) world.getBlockEntity(buf.readBlockPos());
        if (this.printer == null) return;
        this.printer.dyes = buf.readIntArray();
    }

    public PrinterScreenHandler(int syncId, PlayerInventory playerInventory, PrinterBlockEntity printer, final ScreenHandlerContext context) {
        super(BPScreenHandlers.PRINTER, syncId);

        this.context = context;
        this.world = playerInventory.player.getWorld();
        this.printer = printer;

        addSlots();
        addPlayerSlots(playerInventory);

        addProperty(selected);

    }

    private void addSlots() {
        inputSlot = this.addSlot(new Slot(input, 0, 20, 24));
        outputSlot = this.addSlot(new Slot(output, 1, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {

                stack.onCraft(player.getWorld(), player, stack.getCount());

                PrinterRecipe recipe = getSelectedRecipe();
                for (int i = 0; i < 3; i++) {
                    printer.decrementDye(i, recipe.getDye(i));
                }

                ItemStack itemStack = inputSlot.takeStack(1);
                if (!itemStack.isEmpty()) {
                    if (!hasRequiredDyes(recipe)) selected.set(-1);
                    populateResult();
                }

                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (lastTakeTime != l) {
                        world.playSound(null, pos, BPSounds.PRINTING, SoundCategory.BLOCKS, 1.0F, 0.75F);
                        lastTakeTime = l;
                    }
                });

                super.onTakeItem(player, stack);

            }

        });
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }
    }

    public int getDye(int index) {
        return printer.getDye(index);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {

        ItemStack itemStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {

            ItemStack itemStack2 = slot.getStack();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();

            if (slotIndex == 1) {

                item.onCraft(itemStack2, player.getWorld(), player);
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);

            } else if (slotIndex == 0) {
                if (!this.insertItem(itemStack2, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.world.getRecipeManager().getFirstMatch(PrinterRecipe.Type.INSTANCE, new SimpleInventory(itemStack2), this.world).isPresent()) {
                if (!this.insertItem(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 2 && slotIndex < 29) {
                if (!this.insertItem(itemStack2, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex >= 29 && slotIndex < 38 && !this.insertItem(itemStack2, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }

            slot.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }

        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.input.canPlayerUse(player);
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack itemStack = inputSlot.getStack();
        if (!itemStack.isOf(inputStack.getItem())) {
            inputStack = itemStack.copy();
            updateInput(inventory, itemStack);
        }
    }

    private void updateInput(Inventory input, ItemStack stack) {
        recipes.clear();
        selected.set(-1);
        outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.recipes = world.getRecipeManager().getAllMatches(PrinterRecipe.Type.INSTANCE, input, world);
        }
    }

    public boolean hasRequiredDyes(PrinterRecipe recipe) {
        for (int i = 0; i < 3; i++) {
            if (recipe.getDye(i) > getDye(i)) return false;
        }
        return true;
    }

    public boolean hasRequiredDyes(int index) {
        return isInBounds(index) && hasRequiredDyes(getRecipes().get(index));
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (id < 6) {
            boolean leftClick = id < 3;
            if (!leftClick) id -= 3;
            if (isCompatible(id, getCursorStack().getItem()) && getDye(id) + 64 <= PrinterBlockEntity.MAX_DYE) {
                int count = leftClick ? Math.min((PrinterBlockEntity.MAX_DYE - getDye(id)) / 64, getCursorStack().getCount()) : 1;
                printer.incrementDye(id, 64 * count);
                getCursorStack().decrement(count);
            }
        } else {
            id -= 6;
            if (hasRequiredDyes(id)) {
                selected.set(id);
                populateResult();
            }
        }
        return true;
    }

    private boolean isCompatible(int dyeIndex, Item item) {
        if (!(item instanceof DyeItem dye)) return false;
        return switch (dye.getColor()) {
            case CYAN -> dyeIndex == 0;
            case MAGENTA -> dyeIndex == 1;
            case YELLOW -> dyeIndex == 2;
            default -> false;
        };
    }

    private void populateResult() {
        if (!recipes.isEmpty() && hasRequiredDyes(selected.get())) {

            PrinterRecipe recipe = getRecipes().get(selected.get());
            ItemStack itemStack = recipe.craft(input, world.getRegistryManager());

            if (itemStack.isItemEnabled(world.getEnabledFeatures())) {
                output.setLastRecipe(recipe);
                outputSlot.setStackNoCallbacks(itemStack);
            } else {
                outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            }

        } else {
            outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
        }

        sendContentUpdates();
    }

    private boolean isInBounds(int id) {
        return id >= 0 && id < recipes.size();
    }

    public List<PrinterRecipe> getRecipes() {

        List<PrinterRecipe> recipes = new ArrayList<>();
        int lastCraftable = 0;
        for (PrinterRecipe recipe : this.recipes) {
            recipes.add(lastCraftable, recipe);
            if (hasRequiredDyes(recipe)) lastCraftable++;
        }

        return recipes;
    }

    public int getRecipeCount() {
        return recipes.size();
    }

    public int getSelected() {
        return selected.get();
    }

    public PrinterRecipe getSelectedRecipe() {
        return selected.get() != -1 ? recipes.get(selected.get()) : null;
    }

    public PrinterBlockEntity getPrinter() {
        return printer;
    }

    public boolean canCraft() {
        return inputSlot.hasStack() && !recipes.isEmpty();
    }

    public void setListener(Runnable listener) {
        this.listener = listener;
    }
}
