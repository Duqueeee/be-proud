package me.duquee.beproud.registry;

import me.duquee.beproud.BeProud;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class Register {

    protected static RegistryKey<ItemGroup> currentGroup;

    public static void inGroup(RegistryKey<ItemGroup> group) {
        currentGroup = group;
    }

    public static BlockBuilder<Block> block(String name) {
        return block(name, Block::new);
    }

    public static <T extends Block> BlockBuilder<T> block(String name, Function<FabricBlockSettings, T> factory) {
        return new BlockBuilder<>(name, factory);
    }

    public static <P extends ItemBuilder<Item, P>> ItemBuilder<Item, P> item(String name) {
        return item(name, Item::new);
    }

    public static <P extends ItemBuilder<T, P>, T extends Item> ItemBuilder<T, P> item(String name, Function<FabricItemSettings, T> factory) {
        return new ItemBuilder<>(name, factory);
    }

    public static ItemGroupBuilder group(String name) {
        return new ItemGroupBuilder(name);
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String name, FabricBlockEntityTypeBuilder.Factory<T> factory) {
        return new BlockEntityBuilder<>(name, factory);
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandler(ScreenHandlerType.Factory<T> factory) {
        return new ScreenHandlerType<>(factory, null);
    }

    public static <T extends ScreenHandler> ExtendedScreenHandlerType<T> registerExtendedScreenHandler(String name, ExtendedScreenHandlerType.ExtendedFactory<T> factory) {
        ExtendedScreenHandlerType<T> handlerType = new ExtendedScreenHandlerType<>(factory);
        return Registry.register(Registries.SCREEN_HANDLER, BeProud.asIdentifier(name), handlerType);
    }

    public static <M extends ScreenHandler, U extends Screen & ScreenHandlerProvider<M>> void registerScreen(ScreenHandlerType<? extends M> type, HandledScreens.Provider<M, U> provider) {
        HandledScreens.register(type, provider);
    }

    public static SoundEvent sound(String name) {
        Identifier identifier = BeProud.asIdentifier(name);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

}
