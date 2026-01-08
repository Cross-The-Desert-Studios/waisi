package com.waisi;

public class WaisiConfig {
    private static final WaisiConfig INSTANCE = new WaisiConfig();

    public boolean enabled = true;
    public boolean showModName = true;
    public boolean showItemIcon = true;

    // Customization
    public float scale = 1.0f;
    public float xPercent = 0.5f;
    public float yPercent = 0.9f;
    public int backgroundAlpha = 144; // 0-255
    public int cornerRadius = 0; // 0-10

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
