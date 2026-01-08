package com.waisi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class HudRenderer {

    public static void render(GuiGraphics guiGraphics, Minecraft client, boolean isPreview) {
        WaisiConfig config = WaisiConfig.getInstance();
        if (!config.enabled) {
            if (isPreview) {
                // Render "MOD DISABLED" text for preview
                int screenW = client.getWindow().getGuiScaledWidth();
                int screenH = client.getWindow().getGuiScaledHeight();
                int centerX = (int) (screenW * config.xPercent);
                int centerY = (int) (screenH * config.yPercent);

                guiGraphics.drawCenteredString(client.font, "MOD DISABLED", centerX, centerY - 4, 0xFFFF5555);
            }
            return;
        }

        // Gather Data
        ItemStack itemStack = ItemStack.EMPTY;
        Component text = null;
        Component modName = null;

        if (isPreview) {
            // Mock Data
            text = Component.literal("Grass Block");
            modName = Component.literal("Minecraft");
            itemStack = new ItemStack(net.minecraft.world.level.block.Blocks.GRASS_BLOCK);
        } else {
            Entity camera = client.getCameraEntity();
            if (camera == null)
                return;

            BlockPos pos = camera.blockPosition().below();

            if (client.level.isEmptyBlock(pos)) {
                pos = camera.blockPosition();
            }

            if (client.level.isEmptyBlock(pos))
                return;

            BlockState state = client.level.getBlockState(pos);
            Block block = state.getBlock();
            itemStack = new ItemStack(block);
            text = block.getName();

            String mod = net.fabricmc.loader.api.FabricLoader.getInstance()
                    .getModContainer(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block).getNamespace())
                    .map(c -> c.getMetadata().getName())
                    .orElse("Minecraft");

            modName = Component.literal(mod).withStyle(net.minecraft.ChatFormatting.BLUE,
                    net.minecraft.ChatFormatting.ITALIC);
        }

        // --- Rendering ---
        // Matrix3x2fStack issues? We will manually revert transforms instead of
        // push/pop.
        // guiGraphics.pose().push();

        // Calculate Size
        int padding = 4;
        int iconSize = config.showItemIcon ? 16 : 0;
        int iconGap = (config.showItemIcon && iconSize > 0) ? 4 : 0;

        int textW = client.font.width(text);
        int modW = config.showModName ? client.font.width(modName) : 0;
        int maxTextW = Math.max(textW, modW);

        int contentWidth = iconSize + iconGap + maxTextW;
        int contentHeight = (config.showModName) ? (client.font.lineHeight * 2 + 2) : client.font.lineHeight;

        // Subtitle Logic
        int subtitleH = 0;
        int subtitleW = 0;
        if (config.showSubtitle) {
            String sub = "Stepping in:";
            subtitleW = client.font.width(sub);
            subtitleH = client.font.lineHeight + 2;
            contentWidth = Math.max(contentWidth, subtitleW);
        }

        // Total Box Size
        int boxW = contentWidth + (padding * 2);
        int boxH = contentHeight + subtitleH + (padding * 2);

        // Position
        int screenW = client.getWindow().getGuiScaledWidth();
        int screenH = client.getWindow().getGuiScaledHeight();

        int px = (int) (screenW * config.xPercent);
        int py = (int) (screenH * config.yPercent);

        // Scale
        float scale = config.scale;

        // Apply Transform: Move to Center, Scale, Move Back (to center box on point)
        // Since we are 2D stack:
        guiGraphics.pose().translate(px, py);
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.pose().translate(-boxW / 2f, -boxH / 2f);

        // Colors
        int bgCol = parseColor(config.backgroundColor, config.backgroundAlpha);
        int borderCol = parseColor(config.borderColor, 255);
        boolean showBorder = (config.backgroundAlpha > 0 && config.borderThickness > 0);
        int textCol = parseColor(config.textColor, 255);
        int subCol = 0xFFAAAAAA;

        // Draw Border (OUTSIDE)
        if (showBorder) {
            int t = config.borderThickness;
            int bx1 = -t;
            int by1 = -t;
            int bx2 = boxW + t;
            int by2 = boxH + t;

            guiGraphics.fill(bx1 + 1, by1, bx2 - 1, by2, borderCol);
            guiGraphics.fill(bx1, by1 + 1, bx1 + 1, by2 - 1, borderCol);
            guiGraphics.fill(bx2 - 1, by1 + 1, bx2, by2 - 1, borderCol);
        }

        // Draw Background
        if (config.backgroundAlpha > 0) {
            guiGraphics.fill(1, 0, boxW - 1, boxH, bgCol);
            guiGraphics.fill(0, 1, 1, boxH - 1, bgCol);
            guiGraphics.fill(boxW - 1, 1, boxW, boxH - 1, bgCol);
        }

        // Content
        int cx = padding;
        int cy = padding;

        // Subtitle
        if (config.showSubtitle) {
            guiGraphics.drawString(client.font, "Stepping in:", cx, cy, subCol);
            cy += client.font.lineHeight + 2;
        }

        // Item Icon
        if (config.showItemIcon) {
            guiGraphics.renderItem(itemStack, cx, cy + (contentHeight - 16) / 2);
            cx += 16 + 4;
        }

        // Text
        int textY = cy;
        guiGraphics.drawString(client.font, text, cx, textY, textCol);

        if (config.showModName) {
            // Use alpha from textCol but fixed Gray color for distinction, or just Gray?
            // User requested distinct color. Let's use a standard "info" gray.
            // But we must respect alpha if the user fades the HUD?
            // The textCol has alpha. Let's extract alpha.
            int alpha = (textCol >> 24) & 0xFF;
            int modCol = (alpha << 24) | 0xAAAAAA;

            guiGraphics.drawString(client.font, modName, cx, textY + client.font.lineHeight + 2, modCol);
        }

        // Revert Transforms Manually (Pop simulation)
        guiGraphics.pose().translate(boxW / 2f, boxH / 2f);
        guiGraphics.pose().scale(1 / scale, 1 / scale);
        guiGraphics.pose().translate(-px, -py);

        // guiGraphics.pose().pop();
    }

    private static int parseColor(String hex, int alpha) {
        try {
            hex = hex.replace("#", "");
            int rgb = Integer.parseInt(hex, 16);
            return (alpha << 24) | rgb;
        } catch (Exception e) {
            return (alpha << 24) | 0xFFFFFF;
        }
    }
}
