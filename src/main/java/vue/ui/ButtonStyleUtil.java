package vue.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

/**
 * Utilitaire pour appliquer un style cohérent aux boutons d'action.
 */
public final class ButtonStyleUtil {
    private ButtonStyleUtil() {}

    public static void applyActionButtonStyle(AbstractButton btn, Color background, Color foreground, Color bevelShadow, Insets margin) {
        if (btn == null) return;
        btn.setBackground(background);
        btn.setForeground(foreground);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, bevelShadow),
            BorderFactory.createEmptyBorder(margin.top, margin.left, margin.bottom, margin.right)
        ));
        btn.setMargin(margin);
    }
}
