package vue;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import main.Main;

/**
 * Page d'accueil modernisée (v1):
 * - Bandeau héro dégradé
 * - Deux cartes CTA stylisées
 * - Actions rapides (pills)
 * - Panneau d'astuces rotatives
 */
public class PageAccueil extends JPanel {

    public PageAccueil() {
    setLayout(new BorderLayout());
    // Bleu très pâle pour le fond de page
    setBackground(new Color(0xEE, 0xF6, 0xFF)); // #EEF6FF

    // Le logo sera affiché dans la bannière (HeroPanel)

        // Héro
        HeroPanel hero = new HeroPanel();
    hero.setPreferredSize(new Dimension(100, 280));
        add(hero, BorderLayout.NORTH);

        // Centre: cartes + quick actions
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 16));
        cardsRow.setOpaque(false);

        CardButton preEtudeCard = new CardButton(
            "Pré-étude photovoltaïque",
            "Dimensionnement, câbles, protections, onduleur",
            javax.swing.UIManager.getIcon("FileView.directoryIcon")
        );
        preEtudeCard.addActionListener(e -> Main.fenetrePrincipale.showSurface());

        CardButton estimationCard = new CardButton(
            "Estimation de production",
            "PV couplé réseau, hors réseau, suiveur (tracker)",
            javax.swing.UIManager.getIcon("FileView.computerIcon")
        );
        estimationCard.addActionListener(e -> Main.fenetrePrincipale.showEstimationHub());

        cardsRow.add(preEtudeCard);
        cardsRow.add(estimationCard);

    JPanel quickActions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
    quickActions.setOpaque(false);
    // Garder uniquement le tutoriel
    quickActions.add(createPillButton("Tutoriel", e -> showTutorielDialog(PageAccueil.this)));

        center.add(Box.createVerticalStrut(8));
        center.add(cardsRow);
        center.add(Box.createVerticalStrut(8));
        center.add(quickActions);
        center.add(Box.createVerticalStrut(8));

        add(center, BorderLayout.CENTER);

        // Bas: astuces
        TipsPanel tips = new TipsPanel(new String[] {
            "Astuce: Utilisez le menu 'Estimer production' en haut à droite pour choisir votre scénario.",
            "Astuce: Dans les pages d'estimation, le menu 'Pré-étude' vous donne un accès rapide aux modules.",
            "Aide: Les exports PDF incluent le logo, l'année 0 et la pagination.",
        });
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        bottom.add(tips, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private static JButton createPillButton(String text, ActionListener onClick) {
        Color base = new Color(0xE7, 0xF1, 0xFB);   // fond bleu pâle
        Color accent = new Color(0x00, 0x5A, 0xAA); // texte/bordure bleu
        ThreeDPillButton b = new ThreeDPillButton(text, base, accent);
        b.addActionListener(onClick);
        return b;
    }

    // Pas d'image en arrière-plan de toute la page

    // ----- Composants personnalisés -----
    private static void showTutorielDialog(java.awt.Component parent) {
        String msg = String.join("\n",
            "Bienvenue dans le tutoriel d'estimation de production :",
            "",
            "1) Accédez à l'estimation",
            "   • Depuis l'accueil: cliquez sur ‘Estimation de production’",
            "   • Depuis d'autres pages: utilisez le bouton ‘Estimer production’ en haut à droite",
            "",
            "2) Choisissez votre scénario",
            "   • PV couplé au réseau",
            "   • PV hors réseau (autonome)",
            "   • PV suiveur (tracker)",
            "",
            "3) Renseignez les paramètres clés",
            "   • Localisation du site, inclinaison/orientation ou suivi pour tracker",
            "   • Puissance crête ou configuration des modules",
            "   • Pertes système et options avancées si nécessaire",
            "   • Conserver le format JSON pour l'api PVGIS",
            "",
            "4) Lancez le calcul et consultez les résultats",
            "   • Graphiques standardisés ",
            "   • Indicateurs annuels et financiers (cash-flow cumulé)",
            "",
            "5) Exports des rapports",
            "   • CSV et PDF avec logo et pagination",
            "",
            "Astuce: dans les pages d'estimation, le menu ‘Pré-étude’ (en haut à droite) donne un accès rapide aux modules (surface, puissance, onduleur, câbles & protections)."
        );
        JTextArea area = new JTextArea(msg);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setOpaque(false);
        JScrollPane sp = new JScrollPane(area);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setPreferredSize(new Dimension(560, 420));
        JOptionPane.showMessageDialog(parent, sp, "Tutoriel – Estimation de production", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class ThreeDPillButton extends JButton {
        private final Color baseColor;
        private final Color accentColor;

        ThreeDPillButton(String text, Color baseColor, Color accentColor) {
            super(text);
            this.baseColor = baseColor;
            this.accentColor = accentColor;
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(accentColor);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            // Angles droits (pas d'arrondi)
            int arc = 0;
            boolean pressed = getModel().isArmed() && getModel().isPressed();
            boolean hover = getModel().isRollover();
            int yOff = pressed ? 1 : 0;

            // Ombre portée (douce) pour l'effet 3D
            Shape shadow = new RoundRectangle2D.Float(2, 2 + 2, w - 4, h - 4, arc, arc);
            g2.setColor(new Color(0, 0, 0, 38));
            g2.fill(shadow);

            // Corps du bouton (dégradé léger)
            Shape body = new RoundRectangle2D.Float(2, 2 + yOff, w - 4, h - 4, arc, arc);
            Color top = lighten(baseColor, hover ? 0.14f : 0.10f);
            Color bottom = darken(baseColor, pressed ? 0.10f : 0.06f);
            GradientPaint gp = new GradientPaint(0, 2 + yOff, top, 0, h, bottom);
            g2.setPaint(gp);
            g2.fill(body);

            // Liseré supérieur de brillance
            g2.setColor(new Color(255, 255, 255, hover ? 130 : 100));
            Shape gloss = new RoundRectangle2D.Float(2, 2 + yOff, w - 4, Math.max(1, (h - 4) / 2f), arc, arc);
            g2.fill(gloss);

            // Bordure bleue
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), pressed ? 220 : (hover ? 200 : 180)));
            g2.draw(body);

            // Dessin du contenu (texte/icône) avec décalage si pressé
            Graphics g3 = g.create();
            g3.translate(0, yOff);
            super.paintComponent(g3);
            g3.dispose();

            g2.dispose();
        }

        private static Color lighten(Color c, float f) {
            f = Math.max(0f, Math.min(1f, f));
            int r = c.getRed() + Math.round((255 - c.getRed()) * f);
            int g = c.getGreen() + Math.round((255 - c.getGreen()) * f);
            int b = c.getBlue() + Math.round((255 - c.getBlue()) * f);
            return new Color(clamp(r), clamp(g), clamp(b), c.getAlpha());
        }

        private static Color darken(Color c, float f) {
            f = Math.max(0f, Math.min(1f, f));
            int r = Math.round(c.getRed() * (1f - f));
            int g = Math.round(c.getGreen() * (1f - f));
            int b = Math.round(c.getBlue() * (1f - f));
            return new Color(clamp(r), clamp(g), clamp(b), c.getAlpha());
        }

        private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }
    }

    private static class HeroPanel extends JPanel {
    private float phase = 0f;
    private final Timer timer;
    private final JLabel titleLabel;
    private final JLabel subtitleLabel;
    private final JLabel logoLabel;

        HeroPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
            setLayout(new GridBagLayout());
            // Prépare le contenu (logo + titres) au-dessus du dégradé
            javax.swing.ImageIcon appLogo = FenetrePrincipale.getLogo();
            javax.swing.Icon logoIcon = null;
            if (appLogo != null && appLogo.getImage() != null) {
                // mise à l'échelle douce à ~120px de haut
                int targetH = 120;
                int targetW = (int) Math.max(1, appLogo.getIconWidth() * (targetH / (double) Math.max(1, appLogo.getIconHeight())));
                java.awt.Image scaled = appLogo.getImage().getScaledInstance(targetW, targetH, java.awt.Image.SCALE_SMOOTH);
                logoIcon = new javax.swing.ImageIcon(scaled);
            }
            logoLabel = new JLabel(logoIcon);
            titleLabel = new JLabel("E-AUBONNE – Pré-études photovoltaïques");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            titleLabel.setForeground(Color.WHITE);
            subtitleLabel = new JLabel("Démarrez une pré-étude ou estimez la production en quelques clics");
            subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            subtitleLabel.setForeground(Color.WHITE);

            JPanel textBox = new JPanel();
            textBox.setOpaque(false);
            textBox.setLayout(new BoxLayout(textBox, BoxLayout.Y_AXIS));
            textBox.add(titleLabel);
            textBox.add(Box.createVerticalStrut(6));
            textBox.add(subtitleLabel);

            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
            row.setOpaque(false);
            row.add(logoLabel);
            row.add(textBox);

            add(row, new GridBagConstraints());
            timer = new Timer(60, e -> {
                phase += 0.003f;
                if (phase > 1f) phase = 0f;
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            int w = getWidth();
            int h = getHeight();
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color c1 = new Color(0x00, 0x78, 0xD7);
            Color c2 = new Color(0x00, 0xB7, 0xC3);
            int x1 = (int) (Math.sin(phase * Math.PI * 2) * 60) + w / 4;
            int x2 = w - x1;
            GradientPaint gp = new GradientPaint(x1, 0, c1, x2, h, c2);
            // Bandeau dégradé semi-transparent pour laisser voir le background
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.88f));
            g2.setPaint(gp);

            Shape rr = new RoundRectangle2D.Float(8, 8, w - 16, h - 16, 24, 24);
            g2.fill(rr);
            // Voile sombre pour accentuer le contraste (opacité réduite)
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.15f));
            g2.setColor(Color.BLACK);
            g2.fill(rr);
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));

            // Titres et logo sont gérés par les composants enfants

            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(new Color(255, 255, 255, 80));
            g2.draw(rr);

            // pas de logo ici: le logo est dessiné en background par PageAccueil
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));

            g2.dispose();
        }
    }

    private static class CardButton extends JButton {
        private boolean hovered = false;

        CardButton(String title, String subtitle, javax.swing.Icon icon) {
            super("<html><div style='text-align:left;'><div style='font-size:16px; font-weight:bold;'>" + title
                + "</div><div style='font-size:12px; opacity:0.9;'>" + subtitle + "</div></div></html>", icon);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(new Color(0x20,0x21,0x24));
            setBackground(Color.WHITE);
            setFocusPainted(false);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
            setPreferredSize(new Dimension(420, 120));
            setHorizontalAlignment(LEFT);
            setIconTextGap(20);
            if (icon != null) {
                // icône un peu plus petite
                setIcon(scaleIcon(icon, 56, 56));
            }
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            });
        }

        private static javax.swing.Icon scaleIcon(javax.swing.Icon icon, int w, int h) {
            int iw = Math.max(1, icon.getIconWidth());
            int ih = Math.max(1, icon.getIconHeight());
            BufferedImage src = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = src.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            icon.paintIcon(null, g2, 0, 0);
            g2.dispose();
            java.awt.Image scaled = src.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH);
            return new javax.swing.ImageIcon(scaled);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            Shape rr = new RoundRectangle2D.Float(0, 0, w-1, h-1, 20, 20);

            // Fond légèrement coloré et translucide pour le cadre
            g2.setColor(new Color(231, 241, 251, 220)); // #E7F1FB avec alpha
            g2.fill(rr);

            // Bordure plus épaisse et bleue
            float sw = hovered ? 4.0f : 3.0f;
            g2.setStroke(new BasicStroke(sw));
            g2.setColor(new Color(0, 90, 170, hovered ? 200 : 160)); // bleu #005AAA avec alpha
            g2.draw(rr);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    private static class TipsPanel extends JPanel {
        private final String[] tips;
        private int index = 0;
        private final JLabel label;
        private final Timer timer;

        TipsPanel(String[] tips) {
            this.tips = tips != null && tips.length > 0 ? tips : new String[] {"Bienvenue !"};
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
            JPanel inner = new JPanel(new GridBagLayout());
            inner.setOpaque(true);
            inner.setBackground(new Color(231, 241, 251));
            inner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0,90,170,60)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            label = new JLabel(this.tips[0]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            inner.add(label, new GridBagConstraints());
            add(inner, BorderLayout.CENTER);

            timer = new Timer(4000, e -> {
                index = (index + 1) % TipsPanel.this.tips.length;
                label.setText(TipsPanel.this.tips[index]);
            });
            timer.start();
        }
    }
}
