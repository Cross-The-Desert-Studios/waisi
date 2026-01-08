package com.waisi;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;

public class WaisiPositionScreen extends Screen {
    private final Screen parent;
    private boolean isDragging = false;
    private Button doneButton;

    public WaisiPositionScreen(Screen parent) {
        super(Component.literal("HUD Position"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Move "Done" to top right corner to avoid obstruction
        this.doneButton = Button.builder(Component.literal("Done"), (button) -> {
            this.onClose();
        }).bounds(this.width - 60, 10, 50, 20).build();
        this.addRenderableWidget(this.doneButton);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean handled) {
        if (handled)
            return true;

        if (event.button() == 0) { // Left Click
            // Check if clicking "Done" button (handled by super)
            if (super.mouseClicked(event, handled)) {
                return true;
            }

            // Check if clicking "Done" button explicitly if super didn't catch it?
            // super.mouseClicked iterates children. If doneButton was clicked, it returns
            // true.

            // Start Dragging
            this.isDragging = true;
            updatePosition(event.x(), event.y());
            return true;
        }
        return super.mouseClicked(event, handled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0) {
            this.isDragging = false;
        }
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (this.isDragging) {
            updatePosition(event.x(), event.y());
            return true;
        }
        return super.mouseDragged(event, dx, dy);
    }

    private void updatePosition(double mouseX, double mouseY) {
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

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Draw grid lines for center
        guiGraphics.fill(this.width / 2, 0, this.width / 2 + 1, this.height, 0x40FFFFFF);
        guiGraphics.fill(0, this.height / 2, this.width, this.height / 2 + 1, 0x40FFFFFF);

        HudRenderer.render(guiGraphics, this.minecraft, true);

        guiGraphics.drawCenteredString(this.font, "Drag HUD to Position", this.width / 2, 20, 0xFFFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
