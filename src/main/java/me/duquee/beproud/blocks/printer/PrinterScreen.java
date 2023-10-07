package me.duquee.beproud.blocks.printer;

import me.duquee.beproud.BeProud;
import me.duquee.beproud.recipe.PrinterRecipe;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PrinterScreen extends HandledScreen<PrinterScreenHandler> {

    private static final Identifier TEXTURE = BeProud.asIdentifier("textures/gui/printer.png");

    private static final Text[] dyeLabels = {
            Text.translatable("beproud.printer.dyes.cyan").setStyle(Style.EMPTY.withColor(0x29D2FF)),
            Text.translatable("beproud.printer.dyes.magenta").setStyle(Style.EMPTY.withColor(0xFF44FE)),
            Text.translatable("beproud.printer.dyes.yellow").setStyle(Style.EMPTY.withColor(0xFFFF56))
    };

    private boolean canCraft, isDragging;
    private float scroll;
    private int scrollOffset;

    public PrinterScreen(PrinterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setListener(this::onInventoryChange);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderDyes(context, x + 19, y + 48);
        renderTubes(context, x + 18, y + 47);

        renderScroll(context, x + 123, y + 15);

        renderRecipeBackground(context, x + 48, y + 14, mouseX, mouseY, scrollOffset + 12);
        renderRecipeIcons(context, x + 48, y + 14, scrollOffset + 12);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void renderTubes(DrawContext context, int x, int y) {
        for (int i = 0; i < 3; i++) {
            context.drawTexture(TEXTURE, x + 7*i, y, 176, 15, 6, 22);
        }
    }

    private void renderDyes(DrawContext context, int x, int y) {
        for (int i = 0; i < 3; i++) {
            context.drawTexture(TEXTURE, x + 7*i, y, 182, 15, 4, 20 - getScaledDye(i));
        }
    }

    private void renderRecipeIcons(DrawContext context, int x, int y, int scrollOffset) {
        List<PrinterRecipe> list = handler.getRecipes();
        for(int i = this.scrollOffset; i < scrollOffset && i < handler.getRecipeCount(); ++i) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 18 + 1;
            int l = j / 4;
            int m = y + l * 18 + 2;
            context.drawItem(list.get(i).getOutput(client.world.getRegistryManager()), k, m);
        }
    }

    private void renderRecipeBackground(DrawContext context, int x, int y, int mouseX, int mouseY, int scrollOffset) {

        for(int i = this.scrollOffset; i < scrollOffset && i < handler.getRecipeCount(); i++) {
            int j = i - this.scrollOffset;
            int k = x + j % 4 * 18;
            int l = j / 4;
            int m = y + l * 18 + 2;
            int n = backgroundHeight;
            if (i == handler.getSelected()) {
                n += 18;
            } else if (!handler.hasRequiredDyes(i)) {
                n += 54;
            } else if (mouseX >= k && mouseY >= m && mouseX < k + 18 && mouseY < m + 18) {
                n += 36;
            }

            context.drawTexture(TEXTURE, k, m - 1, 0, n, 18, 18);
        }

    }

    private void renderScroll(DrawContext context, int x, int y) {
        int s = (int) (41 * scroll);
        context.drawTexture(TEXTURE, x, y + s, 176 + (shouldScroll() ? 0 : 12), 0, 12, 15);
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        super.drawMouseoverTooltip(context, x, y);

        if (this.canCraft) {
            int i = this.x + 48;
            int j = this.y + 14;
            int k = this.scrollOffset + 12;
            List<PrinterRecipe> list = handler.getRecipes();

            for(int l = this.scrollOffset; l < k && l < handler.getRecipeCount(); l++) {
                int m = l - this.scrollOffset;
                int n = i + m % 4 * 18;
                int o = j + m / 4 * 18 + 2;
                if (x >= n && x < n + 18 && y >= o && y < o + 18) {

                    PrinterRecipe recipe = list.get(l);
                    List<Text> text = Screen.getTooltipFromItem(this.client, recipe.getOutput());
                    List<TooltipComponent> components = text.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Collectors.toList());
                    components.add(new DyeTooltipComponent(recipe.getDyes()));

                    context.drawTooltip(this.textRenderer, components, x, y, HoveredTooltipPositioner.INSTANCE);
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            int j = x - this.x - i*7 - 18;
            int k = y - this.y - 47;
            if (j >= 0 && j <= 5 && k >= 0 && k <= 21) {
                context.drawTooltip(textRenderer,
                        Arrays.asList(dyeLabels[i], Text.literal(handler.getDye(i) + "ml")),
                        x, y);
            }
        }

    }

    private int getScaledDye(int index) {
        return 20 * handler.getDye(index) / PrinterBlockEntity.MAX_DYE;
    }

    private boolean shouldScroll() {
        return canCraft && handler.getRecipeCount() > 12;
    }

    private void onInventoryChange() {
        canCraft = handler.canCraft();
        if (!canCraft) {
            scroll = 0.0F;
            scrollOffset = 0;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        this.isDragging = false;
        if (this.canCraft) {
            int i = x + 50;
            int j = y + 14;
            int k = scrollOffset + 12;

            for (int l = scrollOffset; l < k; l++) {
                int m = l - scrollOffset;
                double d = mouseX - (double) (i + m % 4 * 18);
                double e = mouseY - (double) (j + m / 4 * 18);
                if (d >= 0 && e >= 0 && d < 18 && e < 18 && handler.onButtonClick(client.player, l + 6)) {
                    MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                    client.interactionManager.clickButton(handler.syncId, l + 6);
                    return true;
                }
            }

            i = x + 123;
            j = y + 15;
            if (mouseX >= i && mouseX < i + 12 && mouseY >= j && mouseY < j + 54) {
                this.isDragging = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            double j = mouseX - this.x - i*7 - 18;
            double k = mouseY - this.y - 47;
            if (j >= 0 && j <= 5 && k >= 0 && k <= 21) {
                client.interactionManager.clickButton(handler.syncId, i + button * 3);
                break;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging && this.shouldScroll()) {
            int i = y + 14;
            int j = i + 54;
            scroll = ((float) mouseY - i - 7.5F) / ((float) (j - i) - 15);
            scroll = MathHelper.clamp(scroll, 0, 1);
            scrollOffset = (int) (this.scroll * getMaxScroll() + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int i = getMaxScroll();
            float f = (float)amount / (float) i;
            scroll = MathHelper.clamp(scroll - f, 0.0F, 1.0F);
            scrollOffset = (int) ((double) (scroll * (float) i) + 0.5) * 4;
        }
        return true;
    }

    private int getMaxScroll() {
        return (handler.getRecipeCount() + 3) / 4 - 3;
    }

}
