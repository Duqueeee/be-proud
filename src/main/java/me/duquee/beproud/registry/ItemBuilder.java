package me.duquee.beproud.registry;

import me.duquee.beproud.BeProud;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ItemBuilder<T extends Item, P extends ItemBuilder<T, P>> {

    private T item;

    private final String name;
    private final FabricItemSettings settings = new FabricItemSettings();

    private final Function<FabricItemSettings, T> factory;

    protected ItemBuilder(String name, Function<FabricItemSettings, T> supplier) {
        this.name = name;
        this.factory = supplier;
    }

    public P settings(Consumer<FabricItemSettings> settings) {
        settings.accept(this.settings);
        return (P) this;
    }

    public P register() {

        item = factory.apply(settings);
        Registry.register(Registries.ITEM, BeProud.asIdentifier(name), item);

        if (Register.currentGroup != null)
            ItemGroupEvents.modifyEntriesEvent(Register.currentGroup).register(content -> content.add(item));

        return (P) this;
    }

    public T getItem() {
        return item;
    }

}
