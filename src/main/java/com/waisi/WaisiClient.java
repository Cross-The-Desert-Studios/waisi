package com.waisi;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.DeltaTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaisiClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("waisi");

	@Override
	public void onInitializeClient() {
		LOGGER.info("WAISI: Client Initialized!");
		HudRenderCallback.EVENT.register(this::onHudRender);

		// Register Command to Open Settings (/waisi)
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("waisi")
					.executes(context -> {
						Minecraft.getInstance().setScreen(new WaisiConfigScreen(null));
						return 1;
					}));
		});
	}

	private void onHudRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		// Delegate rendering to shared helper
		HudRenderer.render(guiGraphics, Minecraft.getInstance(), false);

		if (!hasLoggedRender) {
			LOGGER.info("WAISI: Rendered Custom HUD!");
			hasLoggedRender = true;
		}
	}

	private boolean hasLoggedRender = false;
}