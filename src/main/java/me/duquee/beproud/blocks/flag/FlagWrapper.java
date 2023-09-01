package me.duquee.beproud.blocks.flag;

import me.duquee.beproud.blocks.BPBlocks;
import me.duquee.beproud.items.book.PrideBookScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FlagWrapper {

    public static final List<FlagWrapper> ALL = new ArrayList<>();

    private final String name;
    public final FlagBlock SMALL, STANDARD, LARGE;

    @Environment(EnvType.CLIENT)
    private int chapterPage;

    public FlagWrapper(String name, FlagBlock small, FlagBlock standard, FlagBlock large) {

        this.name = name;

        this.SMALL = small;
        this.STANDARD = standard;
        this.LARGE = large;

        small.setWrapper(this);
        standard.setWrapper(this);
        large.setWrapper(this);

        ALL.add(this);
    }

    public String getName() {
        return name;
    }

    public boolean isOf(ItemStack stack) {
        return stack.isOf(SMALL.asItem()) || stack.isOf(STANDARD.asItem()) || stack.isOf(LARGE.asItem());
    }

    @Environment(EnvType.CLIENT)
    public int getChapterPage() {
        return chapterPage;
    }

    public boolean isBlank() {
        return this == BPBlocks.BLANK_FLAG;
    }

    public FlagBlock[] asArray() {
        return new FlagBlock[] {SMALL, STANDARD, LARGE};
    }

    @Environment(EnvType.CLIENT)
    public static void loadChapters() {
        ALL.forEach(wrapper -> {
            if (!wrapper.isBlank()) wrapper.chapterPage = PrideBookScreen.loadChapter(wrapper.name);
        });
    }

}
