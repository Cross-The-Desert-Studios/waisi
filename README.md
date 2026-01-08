# What Am I Stepping In? (WAISI)

<div align="center">
  <img src="assets/logo/exports/512.png" alt="WAISI Icon" width="256" height="256">
  
  **A lightweight Fabric mod that identifies the block underneath your feet**
  
  ![Mod in Use](assets/screenshots/What%20Am%20I%20Stepping%20In%3F.png)
</div>

## Overview

**What Am I Stepping In?** (WAISI) is a client-side Fabric mod designed to answer a simple question: what block are you currently standing on? Inspired by the classic WAILA (What Am I Looking At) family of mods, WAISI takes a focused approach by displaying information about the block directly beneath your feet rather than what you're looking at.

The mod features a customizable HUD that shows the block name, source mod, and item icon in a clean, unobtrusive display. Whether you're exploring a modpack with hundreds of blocks or just want to quickly identify vanilla terrain, WAISI provides instant feedback without cluttering your screen.

## Features

### Block Identification

WAISI continuously monitors the block beneath your player's feet and displays relevant information in real-time. The HUD shows the **block name** in your configured text color, the **source mod** in a distinct gray color for easy differentiation, and an optional **item icon** for quick visual recognition. The detection system is smart enough to handle edge cases—if you're standing in air, it checks the block at your feet position as a fallback, ensuring consistent behavior whether you're on solid ground or floating.

### Customization System

The mod includes a configuration screen accessible through Mod Menu, offering extensive customization options without overwhelming complexity. Choose from three carefully designed **theme presets**: a classic Dark theme with subtle gray borders, a vibrant Purple theme with gold accents, or a stark High Contrast theme for maximum visibility.

Fine-tune the HUD's appearance with **border thickness control** (0-5 pixels) that snaps to integer values for precise adjustment. The **transparency system** supports full alpha control from 0-255, allowing you to create anything from a solid background to completely invisible backing with floating text. **Scale adjustment** ranges from 0.5x to 2.0x, ensuring the HUD looks perfect on any screen resolution or UI scale setting.

![Configuration Screen](assets/screenshots/Configuring%20the%20Mod.png)

### Positioning and Layout

Rather than forcing you to edit config files or guess at percentage values, WAISI includes an **interactive positioning screen** accessible from the settings menu. Simply click and drag the HUD preview to your desired location—the mod remembers your preference and displays a live preview as you move it. The default position (50% horizontal, 75% vertical) places the HUD just above the hotbar, avoiding overlap with health and armor indicators.

### Compatibility Features

For users running multiple HUD mods like Jade or HWYLA, WAISI offers an optional **"Stepping in:" subtitle** that appears above the block name. This small addition helps distinguish WAISI's information from other on-screen displays, preventing confusion when multiple mods are active. The subtitle can be toggled on or off based on your preference.

### Visual Design

The HUD features **1-pixel rounded corners** for a modern, polished look without excessive visual flair. Borders are rendered **outside the content area** (outset style), preserving the padding around text and icons while maintaining clean alignment. When the mod is disabled, the configuration screen's preview pane displays a clear "MOD DISABLED" message, providing immediate visual feedback about the mod's state.

![Mod Menu](assets/screenshots/Mod%20Menu%20Entry.png)

## Installation

Installing WAISI follows the standard Fabric mod installation process:

1. Download and install **[Fabric Loader](https://fabricmc.net/)** for Minecraft 1.21.11 or later
2. Download **[Fabric API](https://modrinth.com/mod/fabric-api)** and place it in your mods folder
3. Download **[Mod Menu](https://modrinth.com/mod/modmenu)** (recommended for easy configuration access)
4. Place the `waisi-1.0.0.jar` file in your `.minecraft/mods` folder
5. Launch Minecraft and access settings via **Mods** → **WAISI** → **Settings**

The mod is entirely client-side and does not require installation on servers. It works seamlessly in both single-player and multiplayer environments.

## Usage

Once installed, WAISI works automatically—no initial configuration required. The HUD appears whenever you're standing on a block, displaying information based on your current settings. To customize the mod's appearance and behavior:

1. Press **Escape** to open the game menu
2. Click **Mods** to open Mod Menu
3. Find **WAISI (What Am I Stepping In?)** in the mod list
4. Click the **Settings** button to open the configuration screen

From the settings screen, you can adjust all visual options, change themes, and access the positioning tool. The **live preview pane** on the right side of the screen updates in real-time as you make changes, allowing you to see exactly how your adjustments will look before closing the menu. If you ever want to start fresh, the **Reset Defaults** button restores all settings to their original values.

## Configuration Reference

### General Options

The General section controls core functionality. **Mod Enabled** toggles the entire HUD on or off—useful if you want to temporarily disable the mod without removing the file. **Show Mod Name** determines whether the source mod appears below the block name. **Show Item Icon** controls the 16x16 block icon that appears to the left of the text. **Show 'Stepping in:'** adds the optional subtitle for compatibility with other HUD mods.

### Appearance Options

**Color Theme** cycles through the three preset themes, each with carefully chosen color combinations. **Scale** adjusts the overall size of the HUD using a smooth slider. **Alpha** controls background transparency—set to 0 for invisible backgrounds, or 255 for fully opaque. **Border Thickness** uses integer-only values from 0 (no border) to 5 pixels for maximum visibility.

### Layout Options

**Adjust Position** opens the interactive positioning screen where you can drag the HUD to any location on your screen. The position is saved immediately and persists across game sessions.

## Default Settings

WAISI ships with sensible defaults designed to work well in most scenarios:

- **Position**: Centered horizontally (50%), positioned at 75% vertical (just above the hotbar)
- **Theme**: Dark (black background, gray border, white text)
- **Scale**: 1.0x (native size)
- **Background Alpha**: 144 (semi-transparent, approximately 56% opacity)
- **Border Thickness**: 1 pixel
- **Subtitle**: Disabled (can be enabled for multi-HUD setups)
- **Mod Name**: Enabled
- **Item Icon**: Enabled

These defaults provide a clean, unobtrusive display that integrates naturally with Minecraft's vanilla UI while remaining clearly visible during gameplay.

## Technical Details

WAISI is built using **Fabric Loader** and requires **Fabric API** as a dependency. The mod is compatible with Minecraft **1.21.11 and later** versions on the 1.21.x branch. It requires **Java 21** or newer to run.

The mod uses efficient rendering techniques to minimize performance impact. Block detection occurs only when necessary, and the HUD rendering pipeline is optimized for 2D display without unnecessary matrix transformations. Memory usage is minimal, with all configuration stored in a lightweight singleton pattern.

## Contributing

Contributions to WAISI are welcome! If you encounter bugs, have feature suggestions, or want to contribute code:

- **Issues**: Report bugs or request features on the [GitHub Issues](https://github.com/Cross-The-Desert-Studios/waisi/issues) page
- **Pull Requests**: Submit code improvements or new features via pull requests
- **Discussions**: Share ideas and feedback in the GitHub Discussions section

Please ensure any code contributions follow the existing code style and include appropriate comments for maintainability.

## License

This project is licensed under the **MIT License**, allowing free use, modification, and distribution. See the [LICENSE](LICENSE) file for complete details.

## Credits and Acknowledgments

WAISI builds upon the legacy of block identification mods in the Minecraft community:

- **[WAILA](https://www.curseforge.com/minecraft/mc-mods/waila)** (What Am I Looking At) - The original mod that pioneered in-game block identification
- **[HWYLA](https://www.curseforge.com/minecraft/mc-mods/hwyla)** (Here's What You're Looking At) - A spiritual successor that continued WAILA's mission
- **[Jade](https://modrinth.com/mod/jade)** - The modern evolution of block identification mods

Built with **[Fabric](https://fabricmc.net/)**, the lightweight modding toolchain that makes modern Minecraft modding accessible and performant.

---

**Cross The Desert Interactive** | [Website](https://crossthedesert.dev/) | [GitHub](https://github.com/Cross-The-Desert-Studios/waisi)
