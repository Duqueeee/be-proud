package me.duquee.beproud.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class PrinterRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final ItemStack output;
    private final Ingredient ingredient;
    private final int[] dyes;

    public PrinterRecipe(Identifier id, ItemStack output, Ingredient ingredient, int[] dyes) {
        this.id = id;
        this.output = output;
        this.ingredient = ingredient;
        this.dyes = dyes;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return ingredient.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return getOutput();
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.ingredient);
        return defaultedList;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getDye(int index) {
        return dyes[index];
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public int[] getDyes() {
        return dyes;
    }

    public static class Type implements RecipeType<PrinterRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "printer";
    }

    public static class Serializer implements RecipeSerializer<PrinterRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "printer";

        @Override
        public PrinterRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
            Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
            int[] dyes = getDyes(JsonHelper.getObject(json, "dyes"));
            return new PrinterRecipe(id, output, ingredient, dyes);
        }

        private int[] getDyes(JsonObject json) {
            return new int[] {
                    JsonHelper.getInt(json, "magenta"),
                    JsonHelper.getInt(json, "blue"),
                    JsonHelper.getInt(json, "yellow")
            };
        }

        @Override
        public PrinterRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            int[] dyes = buf.readIntArray();
            return new PrinterRecipe(id, output, input, dyes);
        }

        @Override
        public void write(PacketByteBuf buf, PrinterRecipe recipe) {
            recipe.ingredient.write(buf);
            buf.writeItemStack(recipe.output);
            buf.writeIntArray(recipe.dyes);
        }
    }

}
