package com.waisi;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.GuiGraphics;
import java.util.function.Consumer;
import com.waisi.HudRenderer;

public class WaisiConfigScreen extends Screen {
    private final Screen parent;
    private boolean isDraggingConfig = false;

    public WaisiConfigScreen(Screen parent) {
        super(Component.literal("WAISI Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.clearWidgets(); // Clear existing widgets before re-initializing
        WaisiConfig config = WaisiConfig.getInstance();

        int y = 40;
        int buttonWidth = 150;
        int padding = 4;
        int centerX = this.width / 2;
        int leftCol = centerX - buttonWidth - padding;
        int rightCol = centerX + padding;

        // Row 1: Enabled | Reset
        this.addRenderableWidget(
                Button.builder(Component.literal("HUD: " + (config.enabled ? "ON" : "OFF")), (button) -> {
                    config.enabled = !config.enabled;
                    button.setMessage(Component.literal("HUD: " + (config.enabled ? "ON" : "OFF")));
                }).bounds(leftCol, y, buttonWidth, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Reset to Default"), (button) -> {
            config.xPercent = 0.5f;
            config.yPercent = 0.9f;
            config.scale = 1.0f;
            config.backgroundAlpha = 144;
            config.cornerRadius = 0;
            config.backgroundColor = "#000000";
            config.borderColor = "#404040";
            config.textColor = "#FFFFFF";
            this.rebuildWidgets();
        }).bounds(rightCol, y, buttonWidth, 20).build());
        y += 24;

        // Row 2: Show Mod Name | Show Icon
        this.addRenderableWidget(
                Button.builder(Component.literal("Mod Name: " + (config.showModName ? "ON" : "OFF")), (button) -> {
                    config.showModName = !config.showModName;
                    button.setMessage(Component.literal("Mod Name: " + (config.showModName ? "ON" : "OFF")));
                }).bounds(leftCol, y, buttonWidth, 20).build());

        this.addRenderableWidget(
                Button.builder(Component.literal("Show Icon: " + (config.showItemIcon ? "ON" : "OFF")), (button) -> {
                    config.showItemIcon = !config.showItemIcon;
                    button.setMessage(Component.literal("Show Icon: " + (config.showItemIcon ? "ON" : "OFF")));
                }).bounds(rightCol, y, buttonWidth, 20).build());
        y += 24;

        // Row 3: Colors (Hex Inputs)
        EditBox bgBox = new EditBox(this.font, leftCol, y, buttonWidth, 20, Component.literal("BG Color"));
        bgBox.setMaxLength(7);
        bgBox.setValue(config.backgroundColor);
        bgBox.setResponder(val -> config.backgroundColor = val);
        this.addRenderableWidget(bgBox);

        EditBox borderBox = new EditBox(this.font, rightCol, y, buttonWidth, 20, Component.literal("Border Color"));
        borderBox.setMaxLength(7);
        borderBox.setValue(config.borderColor);
        borderBox.setResponder(val -> config.borderColor = val);
        this.addRenderableWidget(borderBox);
        y += 24;

        // Row 4: Scale | Alpha
        this.addRenderableWidget(new ConfigSlider(leftCol, y, buttonWidth, 20, "Scale", config.scale, 0.5f, 2.0f,
                val -> config.scale = val.floatValue()));
        this.addRenderableWidget(new ConfigSlider(rightCol, y, buttonWidth, 20, "Alpha",
                config.backgroundAlpha / 255.0f, 0.0f, 1.0f, val -> config.backgroundAlpha = (int) (val * 255)));
        y += 24;

        // Row 5: Corner Radius
        this.addRenderableWidget(new ConfigSlider(leftCol, y, buttonWidth * 2 + padding * 2, 20, "Radius",
                config.cornerRadius / 10.0f, 0.0f, 1.0f, val -> config.cornerRadius = (int) (val * 10)));
        y += 30;

        // Done Button
        this.addRenderableWidget(Button.builder(Component.literal("Done"), (button) -> {
            this.minecraft.setScreen(this.parent);
        }).bounds((this.width - 200) / 2, this.height - 30, 200, 20).build());

        // Drag Listener (Adding last so it's checked last? or first?
        // Screen.mouseClicked iterates children. It returns true if any child consumes.
        // We want buttons to consume first.
        // If we add DragWidget last, it will be later in the list?
        // Actually, Screen loops 0..n or n..0? usually 0..n (front to back? or back to
        // front?)
        // In many GUIs, last drawn is top. Events usually go top (last) to bottom
        // (first).
        // Let's assume standard behavior: Add last = Topmost = First to check.
        // Wait, if it's first to check, it will consume clicks over buttons?
        // No, `render()` order is usually add order.
        // `mouseClicked` usually iterates in reverse? (Top widget first).
        // So if I add DragWidget last, it catches events first.
        // BUT DragWidget returns true.
        // So I should add it FIRST (index 0) so it's at the "Back", and checked LAST?
        // No, if `mouseClicked` checks last-added first (topmost),
        // and DragWidget covers screen, it will block buttons if added last.
        // SO I SHOULD ADD IT FIRST.
        // But `addRenderableWidget` appends.
        // I can use `addWidget(0, ...)`? No.
        // Actually, let's just make DragWidget check if it overlaps a button? No.

        // Better strategy: DragWidget is always there but only consumes if nothing else
        // did?
        // Screen.mouseClicked:
        // for (child : children) if (child.mouseClicked(...)) return true;

        // It iterates. The order depends on the list.
        // If I can't control order easily, I'll just rely on the fact that `DragWidget`
        // covers the screen.
        // If it's checked first, it blocks.
        // Let's assume I need to put it at the start of the list.
        // Or...
        // I'll add logic to DragWidget: `if (super.mouseClicked(...))`? No
        // `AbstractButton` doesn't know about siblings.

        // Alternative: Just override `mouseClicked` in Screen again?
        // But I had signature issues.
        // What if I fix signature?
        // The error was `required: MouseButtonEvent`.
        // This implies `mouseClicked` is indeed not `d,d,i`.
        // But `AbstractButton` (which DragWidget extends) DOES have
        // `mouseClicked(d,d,i)`.
        // So `AbstractButton` works. `Screen` works.
        // Why did override fail? Because `Screen` probably has the method `final` or
        // something? No.

        // Let's try adding it normally. If it blocks buttons, I'll know.
        // I will add it *before* others by inserting code earlier?
        // I'll add it at the end for now and see. If it blocks, I fix.
        this.addRenderableWidget(new DragWidget(this.width, this.height));
    }

    // Helper method to re-initialize widgets
    private void refreshWidgets() {
        this.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, "WAISI Settings - Drag HUD to Move", this.width / 2, 15, 0xFFFFFF);

        // Render Preview
        HudRenderer.render(guiGraphics, this.minecraft, true);
    }

    // Checkbox/Slider already consume clicks.
    // We add a listener that catches clicks that missed everything else.
    // However, Screen event handling iterates listeners.
    // If we add this LAST, it runs LAST.
    // If it returns true, we consume.

    private class DragWidget extends net.minecraft.client.gui.components.AbstractWidget {
        public DragWidget(int width, int height) {
            super(0, 0, width, height, Component.empty());
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            // Invisible
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Only handle left click (0)
            if (button == 0) {
                isDraggingConfig = true;
                return true; // Consume!
            }
            return false;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (isDraggingConfig && button == 0) {
                WaisiConfig config = WaisiConfig.getInstance();
                float newX = (float) (mouseX / this.width);
                float newY = (float) (mouseY / this.height);

                // Snap
                if (Math.abs(newX - 0.5f) < 0.05f)
                    newX = 0.5f;
                if (Math.abs(newY - 0.5f) < 0.05f)
                    newY = 0.5f;

                config.xPercent = newX;
                config.yPercent = newY;
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (button == 0) {
                isDraggingConfig = false;
                return true;
            }
            return false;
        }

        @Override
        protected void updateWidgetNarration(
                net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
        }
    }

    private class ConfigSlider extends AbstractSliderButton {
        private final String label;
        private final double min;
        private final double max;
        private final Consumer<Double> onChange;

        public ConfigSlider(int x, int y, int width, int height, String label, double currentValue, double min,
                double max, Consumer<Double> onChange) {
            super(x, y, width, height, Component.empty(), 0); // Init value below
            this.label = label;
            this.min = min;
            this.max = max;
            this.onChange = onChange;

            // Normalize value to 0-1 for the slider logic
            this.value = (currentValue - min) / (max - min);
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            double actualValue = min + (this.value * (max - min));

            String valStr;
            if (this.label.equals("Alpha")) {
                valStr = String.format("%d", (int) (actualValue * 255));
            } else if (this.label.equals("Radius")) {
                valStr = String.format("%d", (int) actualValue);
            } else {
                valStr = String.format("%.2f", actualValue);
            }

            this.setMessage(Component.literal(label + ": " + valStr));
        }

        @Override
        protected void applyValue() {
            double actualValue = min + (this.value * (max - min));
            this.onChange.accept(actualValue);
        }
    }
}
