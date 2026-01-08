package com.waisi;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

public class WaisiConfigScreen extends Screen {
    private final Screen parent;
    private WaisiOptionList list;

    public WaisiConfigScreen(Screen parent) {
        super(Component.literal("WAISI Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Split Layout: List takes up left ~60%
        int listWidth = (int) (this.width * 0.60);

        this.list = new WaisiOptionList(this.minecraft, listWidth, this.height - 64, 32, 25);
        this.list.setX(0); // Ensure it starts at left edge

        WaisiConfig config = WaisiConfig.getInstance();

        // --- General ---
        this.list.addCategoryEntry("General");
        this.list.addButtonEntry("Mod Enabled", (btn) -> config.enabled = !config.enabled,
                () -> config.enabled ? "ON" : "OFF",
                "Toggles the entire HUD on or off.");

        this.list.addButtonEntry("Show Mod Name", (btn) -> config.showModName = !config.showModName,
                () -> config.showModName ? "ON" : "OFF",
                "Displays the mod responsible for the block.");

        this.list.addButtonEntry("Show Item Icon", (btn) -> config.showItemIcon = !config.showItemIcon,
                () -> config.showItemIcon ? "ON" : "OFF",
                "Renders the block's item icon next to the name.");

        this.list.addButtonEntry("Show 'Stepping in:'", (btn) -> config.showSubtitle = !config.showSubtitle,
                () -> config.showSubtitle ? "ON" : "OFF",
                "Adds a subtext header to distinguish from other HUDs.");

        // --- Appearance ---
        this.list.addCategoryEntry("Appearance");

        // Theme Cycler
        this.list.addButtonEntry("Color Theme", (btn) -> cycleTheme(config),
                () -> config.currentTheme,
                "Cycles color themes (Dark, Purple, High Contrast).");

        this.list.addSliderEntry("Scale", config.scale, 0.5f, 2.0f, val -> config.scale = val.floatValue(),
                "Adjusts the overall size of the HUD.");

        this.list.addSliderEntry("Alpha", config.backgroundAlpha, 0.0f, 255.0f,
                val -> config.backgroundAlpha = val.intValue(),
                "Adjusts transparency. If 0, background/border are invisible!");

        this.list.addSliderEntry("Border Thick.", config.borderThickness, 0.0f, 5.0f,
                val -> config.borderThickness = val.intValue(),
                "Adjusts border thickness (0 = No Border).");

        // --- Layout ---
        this.list.addCategoryEntry("Layout");
        this.list.addActionEntry("Adjust Position...",
                (btn) -> this.minecraft.setScreen(new WaisiPositionScreen(this)),
                "Open interactive screen to drag HUD position.");

        this.list.addActionEntry("Reset Defaults", (btn) -> {
            config.currentTheme = "Dark";
            applyTheme(config); // Reset colors to Dark

            config.scale = 1.0f;
            config.backgroundAlpha = 144;
            config.enabled = true;
            config.showModName = true;
            config.showItemIcon = true;
            config.xPercent = 0.5f;
            config.yPercent = 0.75f;
            config.borderThickness = 1;

            // Hard Reload
            this.minecraft.setScreen(new WaisiConfigScreen(this.parent));
        }, "Resets all settings to default values.");

        this.addRenderableWidget(this.list);

        // Footer Done Button
        this.addRenderableWidget(Button.builder(Component.literal("Done"), (button) -> {
            this.minecraft.setScreen(this.parent);
        }).bounds(this.width / 2 - 100, this.height - 27, 200, 20).build());
    }

    // --- Helpers ---

    private void cycleTheme(WaisiConfig config) {
        if (config.currentTheme.equals("Dark"))
            config.currentTheme = "Purple";
        else if (config.currentTheme.equals("Purple"))
            config.currentTheme = "High Contrast";
        else
            config.currentTheme = "Dark";

        applyTheme(config);
    }

    // Only applies colors, leaving Alpha/Thickness/Scale alone
    private void applyTheme(WaisiConfig config) {
        if (config.currentTheme.equals("Dark")) {
            config.backgroundColor = "#000000";
            config.borderColor = "#404040";
            config.textColor = "#FFFFFF";
        } else if (config.currentTheme.equals("Purple")) {
            config.backgroundColor = "#240024";
            config.borderColor = "#FFD700"; // Gold
            config.textColor = "#E0B0FF";
        } else if (config.currentTheme.equals("High Contrast")) {
            config.backgroundColor = "#000000";
            config.borderColor = "#FFFFFF";
            config.textColor = "#FFFFFF";
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Overlay background (Manual fill to avoid blur crash)
        guiGraphics.fill(0, 0, this.width, this.height, 0x90000000);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Title (Centered over Left Pane)
        guiGraphics.drawCenteredString(this.font, "WAISI Settings", this.list.getWidth() / 2, 10, 0xFFFFFFFF);

        // --- PREVIEW PANE (Right Side) ---
        int paneX = this.list.getWidth() + 10;
        int paneY = 40;
        int paneW = this.width - paneX - 10;
        int paneH = 150; // Fixed height box for preview

        // Avoid drawing offscreen if window is narrow
        if (paneW > 20) {
            // Background Box for Preview
            guiGraphics.fill(paneX, paneY, paneX + paneW, paneY + paneH, 0x80000000);
            guiGraphics.renderOutline(paneX, paneY, paneW, paneH, 0xFF888888);

            guiGraphics.drawCenteredString(this.font, "Preview", paneX + paneW / 2, paneY - 12, 0xFFE0E0E0);

            // Render HUD Centered in Preview Pane
            int centerX = paneX + paneW / 2;
            int centerY = paneY + paneH / 2;

            WaisiConfig config = WaisiConfig.getInstance();
            float originalX = config.xPercent;
            float originalY = config.yPercent;

            config.xPercent = (float) centerX / this.width;
            config.yPercent = (float) centerY / this.height;

            HudRenderer.render(guiGraphics, this.minecraft, true);

            config.xPercent = originalX;
            config.yPercent = originalY;
        }
    }
}
