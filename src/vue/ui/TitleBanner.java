package vue.ui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Bandeau de titre harmonisé (fond gris, texte vert imposant, contour blanc fin).
 * Utilise une police daffichage si disponible (Algerian/Impact/Arial Black...).
 */
public class TitleBanner extends JPanel {
    private final String text;
    private final Font titleFont;

    public TitleBanner(String text) {
        this.text = text;
        this.titleFont = pickDisplayFont(36);
        setOpaque(true);
        setBackground(new java.awt.Color(235, 235, 235));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 8, 12));
        setPreferredSize(new Dimension(100, 64));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        g2.setFont(titleFont);
        java.awt.FontMetrics fm = g2.getFontMetrics();
        int textW = fm.stringWidth(text);
        java.awt.Insets ins = getInsets();
        int contentW = Math.max(0, w - ins.left - ins.right);
        int contentH = Math.max(0, h - ins.top - ins.bottom);
        int x = ins.left + Math.max(12, (contentW - textW) / 2);
        int y = ins.top + (contentH - fm.getHeight()) / 2 + fm.getAscent();

        java.awt.font.FontRenderContext frc = g2.getFontRenderContext();
        java.awt.font.GlyphVector gv = titleFont.createGlyphVector(frc, text);
        java.awt.Shape shape = gv.getOutline(x, y);

        // Remplissage vert imposant (harmonisé)
        g2.setColor(new java.awt.Color(0, 155, 0));
        g2.fill(shape);

        // Contour de surbrillance (blanc translucide) fin
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new java.awt.Color(255, 255, 255, 220));
        g2.draw(shape);

        // Ligne de séparation subtile en bas
        g2.setColor(new java.awt.Color(0, 0, 0, 30));
        g2.drawLine(0, h - 1, w, h - 1);
        g2.dispose();
    }

    private static Font pickDisplayFont(int size) {
        String[] preferred = new String[] { "Algerian", "Impact", "Arial Black", "Segoe UI Black", "Rockwell Extra Bold", "Eras Bold ITC" };
        try {
            String[] available = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
            java.util.Arrays.sort(available, String.CASE_INSENSITIVE_ORDER);
            for (String fam : preferred) {
                if (java.util.Arrays.binarySearch(available, fam, String.CASE_INSENSITIVE_ORDER) >= 0) {
                    return new Font(fam, Font.BOLD, size);
                }
            }
        } catch (Throwable ignore) {}
        return new Font("SansSerif", Font.BOLD, size + 2);
    }
}
