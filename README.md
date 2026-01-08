# What Am I Stepping In? (WAISI)

![Icon](src/main/resources/assets/waisi/icon.png)

**What Am I Stepping In?** is a lightweight, modern HUD mod for Minecraft Fabric that identifies the block directly underneath the player. Never wonder what you're standing on again!

## ✨ Features

### 🖥️ Modern HUD
- **Instant Identification:** Shows the block name and its source mod (e.g., "Minecraft").
- **Item Icons:** Renders the block's item icon for quick visual recognition.
- **Smart Positioning:** Defaults to a non-obtrusive position above the hotbar, avoiding overlap with health/armor bars.
- **Optional Subtitle:** Toggle "Stepping in:" text for clarity with other HUD mods like Jade.

### 🎨 Sodium-Style Customization
Access the sleek, user-friendly config menu via **Mod Menu**:
- **Themes:** Choose from presets like **Dark**, **Purple**, or **High Contrast**.
- **Visuals:** 
  - Adjust **Border Thickness** (0-5px) with integer precision.
  - Fine-tune **Transparency** (Alpha 0-255) - set to 0 for a completely invisible background!
  - **Scale** the HUD (0.5x-2.0x) to fit your screen resolution.
- **Drag & Drop:** Use the intuitive "Adjust Position" screen to click and drag the HUD exactly where you want it.
- **Live Preview:** See your changes in real-time within the settings menu.

### 🛠️ Technical
- **Tooltips:** Hover over any setting for detailed information.
- **Reset:** One-click reset to safe defaults if you need to start fresh.
- **Performance:** Minimal overhead with efficient rendering.
- **Compatibility:** Works seamlessly with Mod Menu and other popular Fabric mods.

## 📦 Installation
1.  Install **Fabric Loader** for Minecraft 1.21.11+.
2.  Install **Fabric API**.
3.  Install **Mod Menu** (recommended for easy configuration).
4.  Drop the `waisi-x.x.x.jar` into your `mods` folder.
5.  Launch Minecraft and configure via Mod Menu → WAISI Settings.

## 🎮 Usage
- The HUD appears automatically when you're standing on a block.
- Press **Escape** → **Mods** → **WAISI** → **Settings** to customize.
- Use **Adjust Position** to drag the HUD to your preferred location.
- Toggle **Show 'Stepping in:'** if you want a subtitle for better compatibility with other HUD mods.

## 🔧 Configuration
All settings are accessible through the in-game GUI:
- **General:** Enable/disable mod, toggle mod name, item icon, and subtitle display.
- **Appearance:** Choose themes, adjust scale, transparency, and border thickness.
- **Layout:** Drag-and-drop positioning with visual preview.

## 🤝 Contributing
Contributions are welcome! Please feel free to submit issues or pull requests.

## 📝 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Credits
- Built with [Fabric](https://fabricmc.net/)
- Inspired by classic "WAILA" (What Am I Looking At) mods
- UI design influenced by [Sodium](https://modrinth.com/mod/sodium)
