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
import javax.swing.Timer;
import javax.swing.UIManager;

import main.Main;

/**
 * Hub d'estimation de production stylisé: bannière + 3 cartes cliquables (Grid / Off-Grid / Tracker).
 */
public class PageEstimationHub extends JPanel {
    public PageEstimationHub() {
        setLayout(new BorderLayout());
        // fond bleu très pâle pour cohérence visuelle avec l'accueil
        setBackground(new Color(0xEE, 0xF6, 0xFF)); // #EEF6FF

        // Bannière supérieure
        HeroPanel hero = new HeroPanel();
        hero.setPreferredSize(new Dimension(100, 200));
        add(hero, BorderLayout.NORTH);

        // Centre: cartes
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JPanel cardsRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 18));
        cardsRow.setOpaque(false);

        CardButton gridCard = new CardButton(
            "PV couplé au réseau",
            "Production injectée avec raccordement au réseau public",
            UIManager.getIcon("FileView.computerIcon")
        );
        gridCard.addActionListener(e -> Main.fenetrePrincipale.showEstimationGrid());

        CardButton offGridCard = new CardButton(
            "PV hors réseau",
            "Autonome avec batteries, sites isolés",
            UIManager.getIcon("FileView.hardDriveIcon")
        );
        offGridCard.addActionListener(e -> Main.fenetrePrincipale.showEstimationOffGrid());

        CardButton trackerCard = new CardButton(
            "PV suiveur (tracker)",
            "Suivi solaire pour maximiser la production",
            UIManager.getIcon("FileView.directoryIcon")
        );
        trackerCard.addActionListener(e -> Main.fenetrePrincipale.showEstimationTracker());

        cardsRow.add(gridCard);
        cardsRow.add(offGridCard);
        cardsRow.add(trackerCard);

        center.add(Box.createVerticalStrut(10));
        center.add(cardsRow);
        center.add(Box.createVerticalStrut(10));

        add(center, BorderLayout.CENTER);
    }

    // ----- Composants personnalisés -----

    private static class HeroPanel extends JPanel {
        private float phase = 0f;
        private final Timer timer;
        private final JLabel title;
        private final JLabel subtitle;

        HeroPanel() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
            setLayout(new GridBagLayout());
            title = new JLabel("Estimation de production");
            title.setFont(new Font("Segoe UI", Font.BOLD, 28));
            title.setForeground(Color.WHITE);

            subtitle = new JLabel("Choisissez un type de scénario");
            subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            subtitle.setForeground(new Color(255,255,255,230));

            JPanel vbox = new JPanel();
            vbox.setOpaque(false);
            vbox.setLayout(new BoxLayout(vbox, BoxLayout.Y_AXIS));
            vbox.add(title);
            vbox.add(Box.createVerticalStrut(6));
            vbox.add(subtitle);

            // Ajout du logo à côté du titre
            javax.swing.ImageIcon appLogo = FenetrePrincipale.getLogo();
            javax.swing.Icon logoIcon = null;
            if (appLogo != null && appLogo.getImage() != null) {
                int targetH = 80; // taille raisonnable pour cette bannière
                int targetW = (int) Math.max(1, appLogo.getIconWidth() * (targetH / (double) Math.max(1, appLogo.getIconHeight())));
                java.awt.Image scaled = appLogo.getImage().getScaledInstance(targetW, targetH, java.awt.Image.SCALE_SMOOTH);
                logoIcon = new javax.swing.ImageIcon(scaled);
            }
            JLabel logoLabel = new JLabel(logoIcon);

            JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            row.setOpaque(false);
            row.add(logoLabel);
            row.add(vbox);

            add(row, new GridBagConstraints());

            timer = new Timer(60, e -> { phase += 0.003f; if (phase > 1f) phase = 0f; repaint(); });
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
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.88f));
            g2.setPaint(gp);

            Shape rr = new RoundRectangle2D.Float(8, 8, w - 16, h - 16, 24, 24);
            g2.fill(rr);

            // Voile léger pour lisibilité
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.25f));
            g2.setColor(Color.BLACK);
            g2.fill(rr);
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1f));

            g2.setStroke(new BasicStroke(1.2f));
            g2.setColor(new Color(255, 255, 255, 80));
            g2.draw(rr);
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
            setPreferredSize(new Dimension(360, 120));
            setHorizontalAlignment(LEFT);
            setIconTextGap(16);
            if (icon != null) {
                setIcon(scaleIcon(icon, 52, 52));
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

            // Fond translucide bleu pâle
            g2.setColor(new Color(231, 241, 251, 220));
            g2.fill(rr);

            // Bordure bleue épaisse
            float sw = hovered ? 4.0f : 3.0f;
            g2.setStroke(new BasicStroke(sw));
            g2.setColor(new Color(0, 90, 170, hovered ? 200 : 160));
            g2.draw(rr);

            super.paintComponent(g);
            g2.dispose();
        }
    }
}
