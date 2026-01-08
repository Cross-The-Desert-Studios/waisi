package com.waisi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WaisiOptionList extends ContainerObjectSelectionList<WaisiOptionList.Entry> {

    public WaisiOptionList(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
        this.centerListVertically = false;
    }

    public void addButtonEntry(String label, Consumer<Button> onPress, Supplier<String> statusSupplier,
            String tooltip) {
        this.addEntry(new ButtonEntry(label, onPress, statusSupplier, tooltip));
    }

    public void addActionEntry(String label, Button.OnPress onPress, String tooltip) {
        this.addEntry(new ActionEntry(label, onPress, tooltip));
    }

    public void addSliderEntry(String label, float current, float min, float max, Consumer<Double> onChange,
            String tooltip) {
        this.addEntry(new SliderEntry(label, current, min, max, onChange, tooltip));
    }

    public void addColorEntry(String label, String currentHex, Consumer<String> onChange, String tooltip) {
        this.addEntry(new ColorEntry(label, currentHex, onChange, tooltip));
    }

    public void addCategoryEntry(String label) {
        this.addEntry(new CategoryEntry(label));
    }

    protected int getScrollbarPosition() {
        return this.width - 6;
    }

    @Override
    public int getRowWidth() {
        return this.width - 40; // Provide more padding for a cleaner look
    }

    // --- Entries ---

    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        // Correct signature for 1.21.11?
        // Based on previous success (Step 1763), we use this:
        // abstract void renderContent(GuiGraphics, int, int, boolean, float)
        // mapped to: (guiGraphics, mouseX, mouseY, isHovered, partialTick)

        // However, Step 1832 error explicitly complained about:
        // does not override abstract method
        // renderContent(GuiGraphics,int,int,boolean,float)

        // This confirms the method SHOULD be:
        @Override
        public abstract void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovered,
                float partialTick);
    }

    public class CategoryEntry extends Entry {
        private final String label;

        public CategoryEntry(String label) {
            this.label = label;
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovered,
                float partialTick) {
            int x = this.getX();
            int y = this.getY();
            int rowWidth = WaisiOptionList.this.getRowWidth();

            // Clean, left-aligned header with underline
            guiGraphics.drawString(Minecraft.getInstance().font,
                    Component.literal(this.label).withStyle(net.minecraft.ChatFormatting.BOLD), x, y + 12, 0xFFFFFFFF);
            // Line below
            guiGraphics.fill(x, y + 22, x + rowWidth, y + 23, 0xFFAAAAAA);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.emptyList();
        }
    }

    public abstract class OptionEntry extends Entry {
        protected final String labelText;

        public OptionEntry(String labelText) {
            this.labelText = labelText;
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovered,
                float partialTick) {
            int x = this.getX();
            int y = this.getY();
            int rowWidth = WaisiOptionList.this.getRowWidth();
            int height = 25; // itemHeight form constructor

            // Draw hover background
            if (isHovered) {
                guiGraphics.fill(x - 2, y - 2, x + rowWidth + 2, y + 22, 0x20FFFFFF);
            }
            renderOption(guiGraphics, x, y, rowWidth, height, mouseX, mouseY, partialTick);
        }

        public abstract void renderOption(GuiGraphics guiGraphics, int x, int y, int rowWidth, int height, int mouseX,
                int mouseY, float partialTick);
    }

    public class ButtonEntry extends OptionEntry {
        private final Button button;

        public ButtonEntry(String label, Consumer<Button> onPress, Supplier<String> statusSupplier, String tooltip) {
            super(label);
            this.button = Button.builder(Component.literal(statusSupplier.get()), (b) -> {
                onPress.accept(b);
                b.setMessage(Component.literal(statusSupplier.get()));
            }).bounds(0, 0, 150, 20).build();
            this.button.setTooltip(Tooltip.create(Component.literal(tooltip)));
        }

        @Override
        public void renderOption(GuiGraphics guiGraphics, int x, int y, int rowWidth, int height, int mouseX,
                int mouseY, float partialTick) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.labelText, x, y + 6, 0xFFD0D0D0);

            this.button.setX(x + rowWidth - 150);
            this.button.setY(y);
            this.button.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.button);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.button);
        }
    }

    public class ActionEntry extends OptionEntry {
        private final Button button;

        public ActionEntry(String label, Button.OnPress onPress, String tooltip) {
            super(label);
            this.button = Button.builder(Component.literal(label), onPress)
                    .bounds(0, 0, 310, 20).build();
            this.button.setTooltip(Tooltip.create(Component.literal(tooltip)));
        }

        @Override
        public void renderOption(GuiGraphics guiGraphics, int x, int y, int rowWidth, int height, int mouseX,
                int mouseY, float partialTick) {
            // Center action buttons
            this.button.setWidth(rowWidth);
            this.button.setX(x);
            this.button.setY(y);
            this.button.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.button);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.button);
        }
    }

    public class SliderEntry extends OptionEntry {
        private final AbstractSliderButton slider;

        public SliderEntry(String label, double current, double min, double max, Consumer<Double> onChange,
                String tooltip) {
            super(label);

            double val = current;
            String s = String.format("%.2f", val);
            if (label.equals("Alpha") || label.equals("Radius") || label.equals("Border Thick."))
                s = "" + (int) val;
            Component initialMessage = Component.literal(s);

            this.slider = new AbstractSliderButton(0, 0, 150, 20, initialMessage, (current - min) / (max - min)) {
                @Override
                protected void updateMessage() {
                    double val = min + (this.value * (max - min));
                    String s = String.format("%.2f", val);
                    if (label.equals("Alpha") || label.equals("Radius") || label.equals("Border Thick."))
                        s = "" + (int) Math.round(val);
                    this.setMessage(Component.literal(s));
                }

                @Override
                protected void applyValue() {
                    double val = min + (this.value * (max - min));
                    if (label.equals("Alpha") || label.equals("Radius") || label.equals("Border Thick.")) {
                        val = Math.round(val);
                        // Snap slider position to match the rounded value
                        this.value = (val - min) / (max - min);
                    }
                    onChange.accept(val);
                }
            };
            this.slider.setTooltip(Tooltip.create(Component.literal(tooltip)));
        }

        @Override
        public void renderOption(GuiGraphics guiGraphics, int x, int y, int rowWidth, int height, int mouseX,
                int mouseY, float partialTick) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.labelText, x, y + 6, 0xFFD0D0D0);
            this.slider.setX(x + rowWidth - 150);
            this.slider.setY(y);
            this.slider.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.slider);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.slider);
        }
    }

    public class ColorEntry extends OptionEntry {
        private final EditBox editBox;

        public ColorEntry(String label, String currentHex, Consumer<String> onChange, String tooltip) {
            super(label);
            this.editBox = new EditBox(Minecraft.getInstance().font, 0, 0, 60, 20, Component.literal("Hex"));
            this.editBox.setMaxLength(7);
            this.editBox.setValue(currentHex);
            this.editBox.setResponder(onChange);
            this.editBox.setTooltip(Tooltip.create(Component.literal(tooltip)));
        }

        @Override
        public void renderOption(GuiGraphics guiGraphics, int x, int y, int rowWidth, int height, int mouseX,
                int mouseY, float partialTick) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.labelText, x, y + 6, 0xFFD0D0D0);

            // Preview
            int color = 0xFFFFFFFF;
            try {
                String hex = this.editBox.getValue().replace("#", "");
                if (hex.length() == 6) {
                    color = 0xFF000000 | Integer.parseInt(hex, 16);
                }
            } catch (Exception e) {
            }

            int previewRight = x + rowWidth - 70;
            guiGraphics.fill(previewRight - 22, y + 2, previewRight - 6, y + 18, color);
            guiGraphics.renderOutline(previewRight - 23, y + 1, 18, 19, 0xFF888888);

            this.editBox.setX(x + rowWidth - 70);
            this.editBox.setY(y);
            this.editBox.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.editBox);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return Collections.singletonList(this.editBox);
        }
    }
}
