package com.waisi;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;

public class WaisiColorScreen extends Screen {
    private final Screen parent;
    private EditBox bgBox;
    private EditBox borderBox;
    private EditBox textBox;

    public WaisiColorScreen(Screen parent) {
        super(Component.literal("Advanced Colors"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        WaisiConfig config = WaisiConfig.getInstance();
        int width = 200;
        int height = 20;
        int padding = 10;
        int x = (this.width - width) / 2;
        int y = 40;

        // Background Color
        bgBox = new EditBox(this.font, x, y, width, height, Component.literal("Display Name"));
        bgBox.setMaxLength(7);
        bgBox.setValue(config.backgroundColor);
        bgBox.setResponder(val -> config.backgroundColor = val);
        this.addRenderableWidget(bgBox);
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), b -> {
            config.backgroundColor = "#000000";
            bgBox.setValue("#000000");
        }).bounds(x + width + 5, y, 50, height).build());

        // Add label (visual only, rendered in render)
        // Y offset for next
        y += 40;

        // Border Color
        borderBox = new EditBox(this.font, x, y, width, height, Component.literal("Display Name"));
        borderBox.setMaxLength(7);
        borderBox.setValue(config.borderColor);
        borderBox.setResponder(val -> config.borderColor = val);
        this.addRenderableWidget(borderBox);
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), b -> {
            config.borderColor = "#404040";
            borderBox.setValue("#404040");
        }).bounds(x + width + 5, y, 50, height).build());

        y += 40;

        // Text Color
        textBox = new EditBox(this.font, x, y, width, height, Component.literal("Display Name"));
        textBox.setMaxLength(7);
        textBox.setValue(config.textColor);
        textBox.setResponder(val -> config.textColor = val);
        this.addRenderableWidget(textBox);
        this.addRenderableWidget(Button.builder(Component.literal("Reset"), b -> {
            config.textColor = "#FFFFFF";
            textBox.setValue("#FFFFFF");
        }).bounds(x + width + 5, y, 50, height).build());

        y += 40;

        // Done Button
        this.addRenderableWidget(Button.builder(Component.literal("Done"), (button) -> {
            this.minecraft.setScreen(this.parent);
        }).bounds((this.width - 200) / 2, this.height - 30, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Dark overlay
        guiGraphics.fill(0, 0, this.width, this.height, 0x90000000);

        guiGraphics.drawCenteredString(this.font, "Advanced Colors (Hex)", this.width / 2, 10, 0xFFFFFF);

        // Labels for boxes
        int width = 200;
        int x = (this.width - width) / 2;
        int y = 40;

        guiGraphics.drawString(this.font, "Background Color", x, y - 12, 0xAAAAAA);
        y += 40;
        guiGraphics.drawString(this.font, "Border Color", x, y - 12, 0xAAAAAA);
        y += 40;
        guiGraphics.drawString(this.font, "Text Color", x, y - 12, 0xAAAAAA);

        // Render Preview
        HudRenderer.render(guiGraphics, this.minecraft, true);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
