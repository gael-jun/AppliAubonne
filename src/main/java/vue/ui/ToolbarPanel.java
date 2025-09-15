package vue.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Barre d'outils unifiée pour les pages PVGIS (Grid / Off-Grid / Tracker).
 * Fournit : Estimer, Voir graphes, Export (popup CSV/PDF) et bouton Finances optionnel.
 * Centralise: style (via {@link ButtonStyleUtil}), activation/désactivation groupée et callbacks.
 */
public final class ToolbarPanel extends JPanel {
    private final JButton graphsBtn = new JButton("Graphes");
    private final JButton exportBtn = new JButton("Export");
    private final JButton financeBtn = new JButton("Finances");
    private final JMenuItem exportCsv = new JMenuItem("Exporter en CSV");
    private final JMenuItem exportPdf = new JMenuItem("Exporter en PDF");
    private final JPopupMenu exportMenu = new JPopupMenu();

    private boolean resultAvailable = false;

    public ToolbarPanel() {
        setLayout(new BorderLayout());
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

    // Icônes petites (utilise icônes LAF standards comme fallback)
        Icon graphIcon = UIManager.getIcon("FileView.directoryIcon");
        if (graphIcon != null) graphsBtn.setIcon(graphIcon);
        Icon exportIcon = UIManager.getIcon("FileView.hardDriveIcon");
        if (exportIcon != null) exportBtn.setIcon(exportIcon);
        Icon financeIcon = UIManager.getIcon("OptionPane.informationIcon");
        // Déterminer une hauteur cible (prend celle d'un des premiers icônes sinon 16)
        int targetH = 16;
        if (graphIcon != null) targetH = graphIcon.getIconHeight();
        else if (exportIcon != null) targetH = exportIcon.getIconHeight();
        if (financeIcon != null) {
            if (financeIcon.getIconHeight() != targetH) {
                financeBtn.setIcon(scaleIcon(financeIcon, targetH));
            } else {
                financeBtn.setIcon(financeIcon);
            }
        }

        styleSecondary(graphsBtn);
        styleSecondary(exportBtn);
        styleSecondary(financeBtn);
        financeBtn.setVisible(false); // Affiché seulement après données + page concernée

        tb.add(graphsBtn);
        tb.addSeparator();
        tb.add(exportBtn);
        tb.addSeparator();
        tb.add(financeBtn);

        exportMenu.add(exportCsv);
        exportMenu.add(exportPdf);
        exportBtn.addActionListener(e -> exportMenu.show(exportBtn, 0, exportBtn.getHeight()));

        add(tb, BorderLayout.CENTER);
        applyState();
    }

    // --- Public API (callbacks) ------------------------------------------------
    public void onGraphs(ActionListener al) { graphsBtn.addActionListener(al); }
    public void onExportCsv(ActionListener al) { exportCsv.addActionListener(al); }
    public void onExportPdf(ActionListener al) { exportPdf.addActionListener(al); }
    public void onFinance(ActionListener al) { financeBtn.addActionListener(al); }

    // --- Visibility / State ----------------------------------------------------
    public void showFinanceButton(boolean show) {
        financeBtn.setVisible(show);
        // Re-applique l'état pour (ré)activer le bouton si un résultat est déjà disponible
        applyState();
    }

    /** Indique qu'un résultat PV est disponible (active graphes / export / finances visibles). */
    public void setResultAvailable(boolean hasResult) {
        this.resultAvailable = hasResult;
        applyState();
    }

    /** Désactive temporairement les actions (ex: pendant export). */
    public void setActionsEnabled(boolean enabled) {
        if (!enabled) {
            graphsBtn.setEnabled(false);
            exportBtn.setEnabled(false);
            exportCsv.setEnabled(false);
            exportPdf.setEnabled(false);
            financeBtn.setEnabled(false);
        } else {
            applyState();
        }
    }

    // --- Internal styling & state ----------------------------------------------
    private void applyState() {
        graphsBtn.setEnabled(resultAvailable);
        exportBtn.setEnabled(resultAvailable);
        exportCsv.setEnabled(resultAvailable);
        exportPdf.setEnabled(resultAvailable);
        if (financeBtn.isVisible()) financeBtn.setEnabled(resultAvailable);
    }

    private static void styleSecondary(JButton b) {
        ButtonStyleUtil.applyActionButtonStyle(b, UIConstants.ACTION_YELLOW, Color.BLACK, UIConstants.ACTION_YELLOW_DARK, UIConstants.PAD_BUTTON);
    }

    /** Mise à l'échelle douce d'un icône pour uniformiser la hauteur des boutons. */
    private static Icon scaleIcon(Icon src, int targetH) {
        if (src == null) {
            return new ImageIcon(new java.awt.image.BufferedImage(1,1, java.awt.image.BufferedImage.TYPE_INT_ARGB));
        }
        try {
            int w = src.getIconWidth();
            int h = src.getIconHeight();
            if (h <= 0 || w <= 0) return src;
            int targetW = (int) Math.round(((double) w / (double) h) * targetH);
            if (src instanceof ImageIcon ii) {
                Image scaled = ii.getImage().getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
            java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            src.paintIcon(null, g2, 0, 0);
            g2.dispose();
            Image scaled = bi.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (RuntimeException ex) {
            return src;
        }
    }

    // Getters si besoin d'une personnalisation extérieure
    public JButton getGraphsButton() { return graphsBtn; }
    public JButton getExportButton() { return exportBtn; }
    public JButton getFinanceButton() { return financeBtn; }
}
