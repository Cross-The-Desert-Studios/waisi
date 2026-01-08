package com.waisi;

public class WaisiConfig {
    private static final WaisiConfig INSTANCE = new WaisiConfig();

    public boolean enabled = true;
    public String currentTheme = "Dark";
    public boolean showModName = true;
    public boolean showItemIcon = true;
    public boolean showSubtitle = false;

    // Customization
    public float scale = 1.0f;
    public float xPercent = 0.5f;
    public float yPercent = 0.75f;
    public int backgroundAlpha = 144; // 0-255
    public int borderThickness = 1; // 0-5

    // Hex Colors (ARGB format expected, but we store as String for easy editing)
    public String backgroundColor = "#000000";
    public String borderColor = "#404040";
    public String textColor = "#FFFFFF";

    // Private constructor
    private WaisiConfig() {
    }

    public static WaisiConfig getInstance() {
        return INSTANCE;
    }
}
