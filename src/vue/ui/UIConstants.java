package vue.ui;

import java.awt.Color;
import java.awt.Insets;

/**
 * Constants for UI styling (colors, paddings) used across pages.
 */
public final class UIConstants {
    private UIConstants() {}

    // Colors
    public static final Color GREY_BG = new Color(235, 235, 235);
    public static final Color STATUS_GREY = new Color(220, 220, 220);
    public static final Color SUCCESS_GREEN = new Color(0, 180, 0);
    public static final Color ERROR_RED = new Color(200, 0, 0);
    public static final Color TITLE_GREEN = new Color(0, 155, 0);
    public static final Color ACTION_YELLOW = new Color(255, 204, 51);
    public static final Color ACTION_YELLOW_DARK = new Color(140, 100, 0);
    public static final Color ACTION_GREEN = new Color(76, 175, 80);
    public static final Color ACTION_GREEN_DARK = new Color(34, 139, 34);

    // Paddings
    public static final Insets PAD_BUTTON = new Insets(4, 8, 4, 8);
    public static final Insets PAD_PRIMARY = new Insets(6, 12, 6, 12);
}
