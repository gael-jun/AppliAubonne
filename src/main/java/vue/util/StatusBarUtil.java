package vue.util;

import javax.swing.*;
import java.awt.*;
import vue.ui.UIConstants;

/** Status bar helper (ex-ToolbarUtil) for consistent status label styling. */
public final class StatusBarUtil {
    private StatusBarUtil() {}

    public static void setStatusWaiting(JLabel statusLabel) {
        if (statusLabel == null) return;
        statusLabel.setText("En attente de la réponse de l'API...");
        statusLabel.setBackground(UIConstants.STATUS_GREY);
        statusLabel.setForeground(Color.BLACK);
    }

    public static void setStatusExporting(JLabel statusLabel, String label) {
        if (statusLabel == null) return;
        statusLabel.setText(label);
        statusLabel.setBackground(UIConstants.STATUS_GREY);
        statusLabel.setForeground(Color.BLACK);
    }

    public static void setStatusSuccess(JLabel statusLabel, String label) {
        if (statusLabel == null) return;
        statusLabel.setText(label);
        statusLabel.setBackground(UIConstants.SUCCESS_GREEN);
        statusLabel.setForeground(Color.WHITE);
    }

    public static void setStatusError(JLabel statusLabel, String label) {
        if (statusLabel == null) return;
        statusLabel.setText(label);
        statusLabel.setBackground(UIConstants.ERROR_RED);
        statusLabel.setForeground(Color.WHITE);
    }
}
