package me.duquee.beproud.items.book;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrideBookItem extends Item {

    public PrideBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("beproud.pride_book.desc").formatted(Formatting.GRAY));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) openPrideBook();
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Environment(EnvType.CLIENT)
    public static void openPrideBook(int page) {
        PrideBookScreen.setPage(page);
        openPrideBook();
    }

    @Environment(EnvType.CLIENT)
    public static void openPrideBook() {
        MinecraftClient instance = MinecraftClient.getInstance();

        Screen currentScreen = instance.currentScreen;
        if (currentScreen != null) currentScreen.close();

        instance.setScreen(new PrideBookScreen());
        instance.getSoundManager().play(
                PositionedSoundInstance.master(SoundEvents.ITEM_BOOK_PAGE_TURN, 1.0F)
        );

    }

}
