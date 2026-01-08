# What Am I Stepping In? (WAISI)

<div align="center">
  <img src="src/main/resources/assets/waisi/icon.png" alt="WAISI Icon" width="128" height="128">
  
  **A lightweight Fabric mod that identifies the block underneath your feet**
  
  ![Mod in Use](screenshots/What%20Am%20I%20Stepping%20In%3F.png)
</div>

## Features

**Block Identification**
- Displays the name of the block you're standing on
- Shows which mod provides the block
- Renders the block's item icon for quick recognition
- Smart detection with air fallback

**Customization**
- Theme presets: Dark, Purple, High Contrast
- Adjustable border thickness (0-5px)
- Transparency control (0-255 alpha)
- HUD scaling (0.5x-2.0x)
- Drag-and-drop positioning
- Optional "Stepping in:" subtitle

**User Interface**
- Sodium-style configuration screen via Mod Menu
- Live preview of changes
- Tooltips on every setting
- One-click reset to defaults

![Configuration Screen](screenshots/Configuring%20the%20Mod.png)

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) for Minecraft 1.21.11+
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Install [Mod Menu](https://modrinth.com/mod/modmenu) (recommended)
4. Drop `waisi-1.0.0.jar` into your `mods` folder
5. Configure via Mod Menu → WAISI Settings

![Mod Menu](screenshots/Mod%20Menu%20Entry.png)

## Usage

The HUD appears automatically when standing on a block. Access settings through:
- **Escape** → **Mods** → **WAISI** → **Settings**

Use **Adjust Position** to drag the HUD to your preferred location.

## Configuration

**General**
- Enable/disable mod
- Toggle mod name display
- Toggle item icon
- Toggle subtitle

**Appearance**
- Theme selection
- Scale adjustment
- Transparency control
- Border thickness

**Layout**
- Interactive positioning screen
- Live preview

## Default Settings

- Position: 50% horizontal, 75% vertical (above hotbar)
- Theme: Dark
- Scale: 1.0x
- Background Alpha: 144 (semi-transparent)
- Border Thickness: 1px
- Subtitle: Disabled

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits

- Built with [Fabric](https://fabricmc.net/)
- Inspired by [WAILA](https://www.curseforge.com/minecraft/mc-mods/waila) (What Am I Looking At), [HWYLA](https://www.curseforge.com/minecraft/mc-mods/hwyla) (Here's What You're Looking At), and [Jade](https://modrinth.com/mod/jade)
- UI design influenced by [Sodium](https://modrinth.com/mod/sodium)
