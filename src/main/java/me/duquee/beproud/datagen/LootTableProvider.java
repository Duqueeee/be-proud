package me.duquee.beproud.datagen;

import me.duquee.beproud.blocks.flag.FlagWrapper;
import me.duquee.beproud.blocks.flag.MultiPartFlagBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.StatePredicate;

public class LootTableProvider extends FabricBlockLootTableProvider {

    protected LootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        FlagWrapper.ALL.forEach(wrapper -> {
            for (Block flag : wrapper.asArray()) {
                if (flag instanceof MultiPartFlagBlock multiFlag)
                    addDrop(flag, dropsFirstPart(multiFlag));
                else
                    addDrop(flag);
            }
        });
    }

    public LootTable.Builder dropsFirstPart(MultiPartFlagBlock flag) {
        return LootTable.builder().pool(
                LootPool.builder()
                .conditionally(withFirstPart(flag))
                        .with(ItemEntry.builder(flag)));
    }

    public LootCondition.Builder withFirstPart(MultiPartFlagBlock flag) {
        return BlockStatePropertyLootCondition.builder(flag)
                .properties(StatePredicate.Builder.create()
                        .exactMatch(flag.getPartProperty(), 0));
    }

}
