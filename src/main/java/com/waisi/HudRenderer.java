package com.waisi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HudRenderer {

    public static void render(GuiGraphics guiGraphics, Minecraft client, boolean isPreview) {
        WaisiConfig config = WaisiConfig.getInstance();
        if (!config.enabled && !isPreview)
            return;

        BlockState targetState = null;

        if (isPreview) {
            // Fake state for preview
            targetState = Blocks.GRASS_BLOCK.defaultBlockState();
        } else {
            if (client.player == null || client.level == null)
                return;
            if (client.getDebugOverlay().showDebugScreen())
                return;

            BlockPos playerPos = client.player.blockPosition();
            BlockState stateAtFeet = client.level.getBlockState(playerPos);

            if (!stateAtFeet.isAir()) {
                targetState = stateAtFeet;
            } else {
                targetState = client.level.getBlockState(playerPos.below());
            }
            if (targetState.isAir())
                return;
        }

        String blockName = targetState.getBlock().getName().getString();

        // Mod Name Logic
        String modName = "";
        if (config.showModName) {
            String keyStr = BuiltInRegistries.BLOCK.getKey(targetState.getBlock()).toString();
            String rawModName = keyStr.split(":")[0];
            modName = rawModName.substring(0, 1).toUpperCase() + rawModName.substring(1);
        }

        // Get the item stack
        ItemStack stack = targetState.getBlock().asItem().getDefaultInstance();

        // Layout constants
        int iconSize = 16;
        int padding = 4;
        int lineSpacing = 2;
        int fontHeight = 9;

        int textWidth = client.font.width(blockName);
        int modNameWidth = config.showModName ? client.font.width(modName) : 0;
        int maxWidth = Math.max(textWidth, modNameWidth);

        int contentWidth = maxWidth;
        if (config.showItemIcon) {
            contentWidth += iconSize + padding;
        }

        int boxWidth = padding + contentWidth + padding;
        int boxHeight = padding + fontHeight + padding;
        if (config.showModName) {
            boxHeight += lineSpacing + fontHeight;
        }

        // Parse Colors
        int backgroundColor = parseColor(config.backgroundColor, config.backgroundAlpha);
        int borderColor = parseColor(config.borderColor, 255);
        int textColor = parseColor(config.textColor, 255);
        // Mod name is usually blueish, let's keep it static or make it configurable
        // later.
        // For now, let's derive it or keep it hardcoded blueish but respect alpha?
        // Actually the user didn't ask to change mod name color, just "border and
        // background".
        int modNameColor = 0xFF5555FF; // Keep default blueish

        // Position & Transform
        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        float centerX = screenWidth * config.xPercent;
        float centerY = screenHeight * config.yPercent;

        var pose = guiGraphics.pose();
        pose.pushMatrix();

        pose.translate(centerX, centerY);
        pose.scale(config.scale, config.scale);
        pose.translate(-boxWidth / 2.0f, -boxHeight / 2.0f);

        // Rendering
        int r = config.cornerRadius;

        if (r > 0) {
            // Draw Rounded Background (Cross shape + corners?)
            // Simple approach: Center rect + top/bottom rects + corner pixels?
            // Or just 3 rects logic:
            // 1. Center vertical rect (full height, width - 2*r)
            // 2. Left and Right vertical rects (height - 2*r, width r) -> Wait, 3
            // horizontal rects is easier

            // Horizontal middle
            guiGraphics.fill(0, r, boxWidth, boxHeight - r, backgroundColor);
            // Top center
            guiGraphics.fill(r, 0, boxWidth - r, r, backgroundColor);
            // Bottom center
            guiGraphics.fill(r, boxHeight - r, boxWidth - r, boxHeight, backgroundColor);

            // Corners? Efficient way is hard with just fill.
            // Let's implement simple "chamfer" or just rects.
            // If R is small (like 0-10), this is fine.

            // Draw simple border frame (square for now, complex to round borders with
            // fills)
            guiGraphics.fill(-1, -1, boxWidth + 1, boxHeight + 1, borderColor);
        } else {
            // Standard Square
            guiGraphics.fill(-1, -1, boxWidth + 1, boxHeight + 1, borderColor); // Border
            guiGraphics.fill(0, 0, boxWidth, boxHeight, backgroundColor); // BG
        }

        // Draw Item Icon
        if (config.showItemIcon) {
            guiGraphics.renderFakeItem(stack, padding, (boxHeight - iconSize) / 2);
        }

        // Draw Block Name
        int textX = padding;
        if (config.showItemIcon) {
            textX += iconSize + padding;
        }

        int textY = padding;
        guiGraphics.drawString(client.font, blockName, textX, textY, textColor, false);

        // Draw Mod Name
        if (config.showModName) {
            int modNameY = textY + fontHeight + lineSpacing;
            guiGraphics.drawString(client.font, modName, textX, modNameY, modNameColor, false);
        }

        pose.popMatrix();
    }

    private static int parseColor(String hex, int alpha) {
        try {
            if (hex.startsWith("#"))
                hex = hex.substring(1);
            int rgb = Integer.parseInt(hex, 16);
            return (alpha << 24) | rgb;
        } catch (NumberFormatException e) {
            return (alpha << 24) | 0x000000; // Default black on error
        }
    }
}
