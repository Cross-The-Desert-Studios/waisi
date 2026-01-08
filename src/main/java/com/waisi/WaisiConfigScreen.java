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
    }

    // Helper method to re-initialize widgets
    private void refreshWidgets() {
        this.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, "WAISI Settings - Drag HUD to Move", this.width / 2, 15, 0xFFFFFF);

        // Poll for drag logic (Bypassing event listener signature headaches)
        boolean isMouseDown = net.minecraft.client.Minecraft.getInstance().mouseHandler.isLeftPressed();

        if (isMouseDown && !wasMouseDown) {
            // Just pressed
            boolean hittingWidget = false;
            for (net.minecraft.client.gui.components.events.GuiEventListener child : this.children()) {
                if (child instanceof net.minecraft.client.gui.components.AbstractWidget w && w.visible
                        && w.isMouseOver(mouseX, mouseY)) {
                    hittingWidget = true;
                    break;
                }
            }

            if (!hittingWidget) {
                isDraggingConfig = true;
            }
        } else if (!isMouseDown) {
            isDraggingConfig = false;
        }

        if (isDraggingConfig && isMouseDown) {
            WaisiConfig config = WaisiConfig.getInstance();
            float newX = (float) (mouseX / (double) this.width);
            float newY = (float) (mouseY / (double) this.height);

            // Snap
            if (Math.abs(newX - 0.5f) < 0.05f)
                newX = 0.5f;
            if (Math.abs(newY - 0.5f) < 0.05f)
                newY = 0.5f;

            config.xPercent = newX;
            config.yPercent = newY;
        }

        wasMouseDown = isMouseDown;

        // Render Preview
        HudRenderer.render(guiGraphics, this.minecraft, true);
    }

    // Add field for tracking polling state
    private boolean wasMouseDown = false;

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
