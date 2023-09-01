package me.duquee.beproud.registry;

import me.duquee.beproud.BeProud;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.function.Consumer;

public class ItemGroupBuilder {

    private ItemGroup group;

    private final String name;
    private RegistryKey<ItemGroup> key;
    private final ItemGroup.Builder builder = FabricItemGroup.builder();

    protected ItemGroupBuilder(String name) {
        this.name = name;
    }

    public ItemGroupBuilder builder(Consumer<ItemGroup.Builder> builder) {
        builder.accept(this.builder);
        return this;
    }

    public ItemGroupBuilder register() {
        group = builder.build();
        Registry.register(Registries.ITEM_GROUP, BeProud.asIdentifier(name), group);
        key = RegistryKey.of(RegistryKeys.ITEM_GROUP, BeProud.asIdentifier(name));
        return this;
    }

    public ItemGroup getGroup() {
        return group;
    }

    public RegistryKey<ItemGroup> getKey() {
        return key;
    }

}
