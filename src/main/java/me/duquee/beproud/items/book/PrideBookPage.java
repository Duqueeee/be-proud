package me.duquee.beproud.items.book;

import me.duquee.beproud.BeProud;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PrideBookPage {

    private final List<Content> content = new ArrayList<>();

    public void drawContents(DrawContext context, TextRenderer textRenderer, int x, int y) {
        for (Content content : content) {
            y = content.draw(context, textRenderer, x, y);
        }
    }

    public static List<PrideBookPage> loadChapter(String label, boolean startsAtLeft) {

        List<OrderedText> titleLines = MinecraftClient.getInstance().textRenderer
                .wrapLines(Text.translatable("beproud.pride_book." + label + ".title").styled(s -> s.withBold(true)), 95);

        List<OrderedText> lines = MinecraftClient.getInstance().textRenderer
                .wrapLines(Text.translatable("beproud.pride_book." + label + ".content"), 105);

        List<PrideBookPage> pages = new ArrayList<>();

        PrideBookPage page = new PrideBookPage();
        page.content.add(Content.bookmark(BeProud.asIdentifier("textures/bookmark/" + label + ".png"), startsAtLeft));
        page.content.add(Content.title(titleLines));

        int k = 11 - titleLines.size();
        page.content.add(Content.lines(lines.subList(0, Math.min(k, lines.size()))));
        pages.add(page);

        for (int i = k; i < lines.size(); i += 14) {
            PrideBookPage p = new PrideBookPage();
            p.content.add(Content.lines(lines.subList(i, Math.min(i + 14, lines.size()))));
            pages.add(p);
        }

        return pages;

    }

    private interface Content {
        int draw(DrawContext context, TextRenderer textRenderer, int x, int y);

        private static Content bookmark(Identifier texture, boolean left) {
            return (context, textRenderer, x, y) -> {
                context.drawTexture(texture, x + (left ? 0 : 83), y - 12, 0, 0, 18, 27, 18, 27);
                return y;
            };
        }

        private static Content title(List<OrderedText> titleLines) {
            return (context, textRenderer, x, y) -> {
                for (OrderedText titleLine : titleLines) {
                    context.drawText(textRenderer, titleLine, x + 53 - textRenderer.getWidth(titleLine)/2, y + 18, 0, false);
                    y += 9;
                }
                return y + 27;
            };
        }

        private static Content lines(List<OrderedText> lines) {
            return (context, textRenderer, x, y) -> {
                for (OrderedText line : lines) {
                    context.drawText(textRenderer, line, x, y, 0, false);
                    y += 9;
                }
                return y;
            };
        }

    }

}
