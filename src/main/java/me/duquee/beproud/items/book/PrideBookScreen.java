package me.duquee.beproud.items.book;

import me.duquee.beproud.BeProud;
import me.duquee.beproud.blocks.flag.FlagWrapper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PrideBookScreen extends Screen {

    private static final Identifier TEXTURE = BeProud.asIdentifier("textures/gui/pride_book.png");
    public static final List<PrideBookPage> pages = new ArrayList<>();
    private static int currentPage = 0;

    private int x, y;

    private PageTurnWidget nextPageButton, previousPageButton;

    protected PrideBookScreen() {
        super(Text.translatable("item.beproud.pride_book"));
    }

    protected void init() {
        this.x = (this.width - 256)/2;
        this.y = (this.height - 165)/2;
        addPageButtons();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        this.setFocused(null);
        context.drawTexture(TEXTURE, x, y, 0, 0, 256, 165);

        pages.get(currentPage).drawContents(context, textRenderer, x + 17 ,y + 18);
        if (hasRightPage()) pages.get(currentPage + 1).drawContents(context, textRenderer, x + 136, y + 18);

        super.render(context, mouseX, mouseY, delta);
    }

    private void addPageButtons() {
        this.nextPageButton = addDrawableChild(new PageTurnWidget(x + 210, y + 142, true,
                (button) -> goToNextPage()
        , true));
        this.previousPageButton = addDrawableChild(new PageTurnWidget(x + 21, y + 142, false,
                (button) -> goToPreviousPage()
        , true));
        updatePageButtons();
    }

    private void updatePageButtons() {
        this.nextPageButton.visible = currentPage < pages.size() - 2;
        this.previousPageButton.visible = currentPage > 1;
    }

    private void goToNextPage() {
        if (currentPage < pages.size() - 2) currentPage += 2;
        updatePageButtons();
    }

    private void goToPreviousPage() {
        if (currentPage > 1) currentPage -= 2;
        updatePageButtons();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public static void setPage(int page) {
        currentPage = (page/2)*2;
    }

    private boolean hasRightPage() {
        return pages.size() > currentPage + 1;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!super.keyPressed(keyCode, scanCode, modifiers) && this.client.options.inventoryKey.matchesKey(keyCode, scanCode))
            this.close();
        return true;
    }

    public static int loadChapter(String label) {
        final int index = pages.size();
        pages.addAll(PrideBookPage.loadChapter(label, index%2 == 0));
        return index;
    }

    public static class ResourceReloadListener implements SynchronousResourceReloader, IdentifiableResourceReloadListener {

        public static final ResourceReloadListener INSTANCE = new ResourceReloadListener();
        public static final Identifier ID = BeProud.asIdentifier("chapters");

        @Override
        public void reload(ResourceManager manager) {
            pages.clear();
            FlagWrapper.loadChapters();
        }

        @Override
        public Identifier getFabricId() {
            return ID;
        }
    }

}
