package vue;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.Icon;

import main.Main;
import vue.ui.ButtonStyleUtil;
import vue.EstimationProd.PageEstimationPVGISGrid;
import vue.EstimationProd.PageEstimationPVGISTracker;
import vue.EstimationProd.PageEstimationPVGISOffGrid;

/**
 * Classe représentant la fenêtre principale de l'application.
 * <p>
 * La fenêtre principale contient un panneau de navigation avec des boutons pour accéder
 * aux différentes pages de l'application, ainsi qu'un panneau central qui utilise
 * un CardLayout pour afficher les différentes pages.
 * </p>
 */
public class FenetrePrincipale {

    private static JFrame fenetre;
    private static JPanel cardPanel;
    private static CardLayout cardSurface;
    private static ImageIcon logo;
    private static String carteCourante;
    private PageSurface pageSurface;
    private PagePuissance pagePuissance;
    private PageOnduleur pageOnduleur;
    private PageCablesProtections pageCablesProtections;
    private PageAccueil pageAccueil;
    private PageEstimationHub pageEstimationHub;
    // Contrôles de la barre supérieure
    private JPanel boutonsMenuPanel;
    private JButton boutonSurface;
    private JButton boutonPuissance;
    private JButton boutonOndulateur;
    private JButton boutonCablesProtections;
    private JButton boutonPreEtudeMenu;
    private JButton boutonEstimerProduction;
    private JButton boutonAccueilTop;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;
    private JPanel bottomPanel;
    
    /**
     * Constructeur de la classe {@code FenetrePrincipale}.
     * <p>
     * Initialise la fenêtre principale, les pages et les boutons de navigation.
     * </p>
     */
    public FenetrePrincipale() {
        
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.background", new Color(0, 120, 215));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.border", new EmptyBorder(5, 15, 5, 15));
        UIManager.put("Button.focus", new Color(0, 120, 215));

        // Robust logo loading: try several classpath locations + filesystem fallback
        java.net.URL logoUrl = FenetrePrincipale.class.getResource("/logo.png");
        if (logoUrl == null) {
            logoUrl = FenetrePrincipale.class.getResource("/ressources/logo.png");
        }
        if (logoUrl == null) {
            logoUrl = FenetrePrincipale.class.getResource("logo.png");
        }
        if (logoUrl == null) {
            java.io.File f = new java.io.File("ressources/logo.png");
            if (f.exists()) {
                logo = new ImageIcon(f.getAbsolutePath());
            }
        }
        if (logo == null) {
            if (logoUrl != null) {
                logo = new ImageIcon(logoUrl);
            } else {
                // Fallback: create a placeholder icon
                java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(64, 64, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D g2 = img.createGraphics();
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(0,0,64,64);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString("LOGO", 10, 32);
                g2.dispose();
                logo = new ImageIcon(img);
            }
        } else if (logo == null && logoUrl != null) {
            logo = new ImageIcon(logoUrl);
        }
        
        fenetre = new JFrame("E-AUBONNE pré-études");
        fenetre.setIconImage(logo.getImage());
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setSize(1024, 780);
        fenetre.setMinimumSize(new Dimension(1024, 780));
        fenetre.setResizable(true);
        fenetre.setLocationRelativeTo(null);
        
        JLabel logoLabel = new JLabel(logo);
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.add(logoLabel);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

    boutonsMenuPanel = new JPanel(new BorderLayout());
        boutonsMenuPanel.setBackground(Color.WHITE);
        topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topLeftPanel.setBackground(Color.WHITE);
        topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.setBackground(Color.WHITE);
        
    cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(Color.WHITE);
    // Pages Pré-étude
    pageAccueil = new PageAccueil();
    pageSurface = new PageSurface();
        pagePuissance = new PagePuissance();
        pageOnduleur = new PageOnduleur();
        pageCablesProtections = new PageCablesProtections();
    // Hub Estimation
    pageEstimationHub = new PageEstimationHub();
        
    // Cartes
    cardPanel.add(pageAccueil, "Accueil");
    cardPanel.add(pageSurface.getPage(), "Surface");
        cardPanel.add(pagePuissance.getPage(), "Puissance");
        cardPanel.add(pageOnduleur.getPage(), "Onduleur");
        cardPanel.add(pageCablesProtections.getPage(), "Câbles_Protections");
    cardPanel.add(pageEstimationHub, "Estimation_Hub");
        
        cardPanel.add(new PageEstimationPVGISGrid(), "Estimation_PVGIS_Grid");
        cardPanel.add(new PageEstimationPVGISTracker(), "Estimation_PVGIS_Tracker");
        cardPanel.add(new PageEstimationPVGISOffGrid(), "Estimation_PVGIS_OffGrid");

    // Bouton Accueil tout à gauche sur la même ligne que les autres boutons
    boutonAccueilTop = new ThreeDNavButton(
        "Accueil",
        new Color(0xE7, 0xF1, 0xFB), // base pâle
        new Color(0, 90, 170),       // accent bleu
        new HomeIcon(16, 16, new Color(0, 90, 170))
    );
    topLeftPanel.add(boutonAccueilTop);
        
    boutonSurface = new JButton("Surface");
        boutonPuissance = new JButton("Puissance");
        boutonOndulateur = new JButton("Onduleur");
        boutonCablesProtections = new JButton("Câbles_Protections");
    // Boutons "Exporter" et "Calculer" supprimés; fonctionnalités disponibles dans les pages
        
        // Remplace la liste déroulante par un bouton menu "Estimer production" (bleu)
    boutonEstimerProduction = new JButton("Estimer production");
        ButtonStyleUtil.applyActionButtonStyle(
            boutonEstimerProduction,
            new Color(0, 120, 215),
            Color.WHITE,
            new Color(0, 90, 170),
            new java.awt.Insets(4, 12, 4, 12)
        );
        JPopupMenu menuEstimation = new JPopupMenu();
        JMenuItem miGrid = new JMenuItem("PV couplé au réseau");
        miGrid.addActionListener(e -> showEstimationGrid());
        JMenuItem miTracker = new JMenuItem("PV suiveur (tracker)");
        miTracker.addActionListener(e -> showEstimationTracker());
        JMenuItem miOffGrid = new JMenuItem("PV hors réseau");
        miOffGrid.addActionListener(e -> showEstimationOffGrid());
        menuEstimation.add(miGrid);
        menuEstimation.add(miOffGrid);
        menuEstimation.add(miTracker);
        boutonEstimerProduction.addActionListener(e -> menuEstimation.show(boutonEstimerProduction, 0, boutonEstimerProduction.getHeight()));
    topRightPanel.add(boutonEstimerProduction);
        // Bouton menu Pré-étude (remplace les 4 boutons sur les pages d'estimation)
        boutonPreEtudeMenu = new JButton("Pré-étude");
        ButtonStyleUtil.applyActionButtonStyle(
            boutonPreEtudeMenu,
            new Color(0, 120, 215),
            Color.WHITE,
            new Color(0, 90, 170),
            new java.awt.Insets(4, 12, 4, 12)
        );
        JPopupMenu menuPreEtude = new JPopupMenu();
        JMenuItem peSurface = new JMenuItem("Surface"); peSurface.addActionListener(e -> showSurface());
        JMenuItem pePuissance = new JMenuItem("Puissance"); pePuissance.addActionListener(e -> showPuissance());
        JMenuItem peOnduleur = new JMenuItem("Onduleur"); peOnduleur.addActionListener(e -> showOnduleur());
        JMenuItem peCables = new JMenuItem("Câbles & Protections"); peCables.addActionListener(e -> showCablesProtections());
        menuPreEtude.add(peSurface);
        menuPreEtude.add(pePuissance);
        menuPreEtude.add(peOnduleur);
        menuPreEtude.add(peCables);
        boutonPreEtudeMenu.addActionListener(e -> menuPreEtude.show(boutonPreEtudeMenu, 0, boutonPreEtudeMenu.getHeight()));
    topRightPanel.add(boutonPreEtudeMenu);
    topLeftPanel.add(boutonSurface);
    topLeftPanel.add(boutonPuissance);
    topLeftPanel.add(boutonOndulateur);
    topLeftPanel.add(boutonCablesProtections);
    boutonsMenuPanel.add(topLeftPanel, BorderLayout.WEST);
    boutonsMenuPanel.add(topRightPanel, BorderLayout.EAST);
        

    bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(logoPanel, BorderLayout.CENTER);
    bottomPanel.setBackground(Color.WHITE);

        mainPanel.add(boutonsMenuPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        fenetre.add(mainPanel);
        
    cardSurface = (CardLayout) cardPanel.getLayout();
    // Afficher l'accueil par défaut
    cardSurface.show(cardPanel, "Accueil");
    carteCourante = "Accueil";
        
        boutonSurface.addActionListener(Main.controleur);
        boutonPuissance.addActionListener(Main.controleur);
        boutonOndulateur.addActionListener(Main.controleur);
        boutonCablesProtections.addActionListener(Main.controleur);
    // Listeners supprimés pour les anciens boutons de bas de page
    boutonAccueilTop.addActionListener(e -> showAccueil());
        // Applique la visibilité initiale (selon carteCourante)
        updateTopBarForCurrentCard();
    }

    private void updateTopBarForCurrentCard() {
        boolean isAccueil = "Accueil".equals(carteCourante);
        boolean isEstimation = "Estimation_PVGIS_Grid".equals(carteCourante)
                || "Estimation_PVGIS_OffGrid".equals(carteCourante)
                || "Estimation_PVGIS_Tracker".equals(carteCourante)
                || "Estimation_Hub".equals(carteCourante);

    // Accueil invisible sur la page Accueil
    boutonAccueilTop.setVisible(!isAccueil);

        if (isAccueil) {
            // Sur l'accueil: ne garder QUE le bouton Accueil
            boutonSurface.setVisible(false);
            boutonPuissance.setVisible(false);
            boutonOndulateur.setVisible(false);
            boutonCablesProtections.setVisible(false);
            boutonPreEtudeMenu.setVisible(false);
            boutonEstimerProduction.setVisible(false);
            topRightPanel.setVisible(false);
            topLeftPanel.setVisible(true);
            if (bottomPanel != null) bottomPanel.setVisible(false);
        } else if (isEstimation) {
            // En estimation: masquer 4 boutons, afficher les menus
            boutonSurface.setVisible(false);
            boutonPuissance.setVisible(false);
            boutonOndulateur.setVisible(false);
            boutonCablesProtections.setVisible(false);
            boutonPreEtudeMenu.setVisible(true);
            boutonEstimerProduction.setVisible(true);
            topRightPanel.setVisible(true);
            topLeftPanel.setVisible(true);
            if (bottomPanel != null) bottomPanel.setVisible(true);
        } else {
            // En pré-étude: afficher 4 boutons, masquer le menu Pré-étude
            boutonSurface.setVisible(true);
            boutonPuissance.setVisible(true);
            boutonOndulateur.setVisible(true);
            boutonCablesProtections.setVisible(true);
            boutonPreEtudeMenu.setVisible(false);
            boutonEstimerProduction.setVisible(true);
            topRightPanel.setVisible(true);
            topLeftPanel.setVisible(true);
            if (bottomPanel != null) bottomPanel.setVisible(true);
        }

        boutonsMenuPanel.revalidate();
        boutonsMenuPanel.repaint();
    }
    
    /**
     * Recharge la page "Câbles_Protections".
     * <p>
     * Cette méthode supprime la page "Câbles_Protections" actuelle du panneau de cartes,
     * la réinitialise en créant une nouvelle instance, puis l'ajoute de nouveau au panneau de cartes.
     * </p>
     */
    public void reloadPageCablesProtections() {
        cardPanel.remove(pageCablesProtections.getPage());
        pageCablesProtections = new PageCablesProtections();
        cardPanel.add(pageCablesProtections.getPage(), "Câbles_Protections");
    }
    
    /**
     * Rend la fenêtre visible.
     */
    public void show() {
        fenetre.setVisible(true);
    }
    
    /**
     * Affiche la page "Surface".
     */
    public void showSurface() {
        cardSurface.show(cardPanel, "Surface");
        carteCourante = "Surface";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Affiche la page "Puissance".
     */
    public void showPuissance() {
        cardSurface.show(cardPanel, "Puissance");
        carteCourante = "Puissance";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Affiche la page "Onduleur".
     */
    public void showOnduleur() {
        cardSurface.show(cardPanel, "Onduleur");
        carteCourante = "Onduleur";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Affiche la page "Câbles_Protections".
     */
    public void showCablesProtections() {
        cardSurface.show(cardPanel, "Câbles_Protections");
        carteCourante = "Câbles_Protections";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Affiche la page d'accueil.
     */
    public void showAccueil() {
        cardSurface.show(cardPanel, "Accueil");
        carteCourante = "Accueil";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Affiche le hub d'estimation.
     */
    public void showEstimationHub() {
        cardSurface.show(cardPanel, "Estimation_Hub");
        carteCourante = "Estimation_Hub";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Accès rapide: pages d'estimation PVGIS.
     */
    public void showEstimationGrid() {
        cardSurface.show(cardPanel, "Estimation_PVGIS_Grid");
        carteCourante = "Estimation_PVGIS_Grid";
    updateTopBarForCurrentCard();
    }
    public void showEstimationOffGrid() {
        cardSurface.show(cardPanel, "Estimation_PVGIS_OffGrid");
        carteCourante = "Estimation_PVGIS_OffGrid";
    updateTopBarForCurrentCard();
    }
    public void showEstimationTracker() {
        cardSurface.show(cardPanel, "Estimation_PVGIS_Tracker");
        carteCourante = "Estimation_PVGIS_Tracker";
    updateTopBarForCurrentCard();
    }
    
    /**
     * Obtient la carte courante affichée.
     * 
     * @return Le nom de la carte courante.
     */
    public String getCarteCourante() {
        return carteCourante;
    }
    
    /**
     * Obtient la page "Surface".
     * 
     * @return La page "Surface".
     */
    public PageSurface getPageSurface() {
        return this.pageSurface;
    }
    
    /**
     * Obtient la page "Puissance".
     * 
     * @return La page "Puissance".
     */
    public PagePuissance getPagePuissance() {
        return this.pagePuissance;
    }
    
    /**
     * Obtient la page "Onduleur".
     * 
     * @return La page "Onduleur".
     */
    public PageOnduleur getPageOnduleur() {
        return this.pageOnduleur;
    }
    
    /**
     * Obtient la page "Câbles_Protections".
     * 
     * @return La page "Câbles_Protections".
     */
    public PageCablesProtections getPageCablesProtections() {
        return this.pageCablesProtections;
    }
    
    /**
     * Obtient le logo de l'application.
     * 
     * @return Le logo de l'application.
     */
    public static ImageIcon getLogo() {
        return logo;
    }

    // ----- Icône et style 3D pour le bouton Accueil -----

    private static class HomeIcon implements Icon {
        private final int w, h;
        private final Color color;
        HomeIcon(int w, int h, Color color) { this.w = w; this.h = h; this.color = color; }
        @Override public int getIconWidth() { return w; }
        @Override public int getIconHeight() { return h; }
        @Override public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(color);
            int roofH = (int) (h * 0.45);
            int bodyH = h - roofH + 1;
            int bodyY = roofH - 1;
            int bodyW = (int) (w * 0.78);
            int bodyX = (w - bodyW) / 2;
            // toit
            int[] rx = { w/2, w-1, 1 };
            int[] ry = { 0, roofH, roofH };
            g2.fillPolygon(rx, ry, 3);
            // corps
            g2.fillRoundRect(bodyX, bodyY, bodyW, bodyH, 3, 3);
            // porte
            int doorW = (int) (bodyW * 0.22);
            int doorH = (int) (bodyH * 0.50);
            int doorX = bodyX + (bodyW - doorW) / 2;
            int doorY = bodyY + bodyH - doorH;
            g2.setColor(new Color(0,0,0,60));
            g2.fillRoundRect(doorX, doorY, doorW, doorH, 2, 2);
            g2.dispose();
        }
    }

    private static class ThreeDNavButton extends JButton {
        private final Color baseColor;
        private final Color accentColor;
        ThreeDNavButton(String text, Color baseColor, Color accentColor, Icon icon) {
            super(text, icon);
            this.baseColor = baseColor;
            this.accentColor = accentColor;
            setForeground(accentColor);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setMargin(new Insets(6, 12, 6, 12));
            setIconTextGap(6);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            boolean pressed = getModel().isArmed() && getModel().isPressed();
            boolean hover = getModel().isRollover();
            int yOff = pressed ? 1 : 0;

            // ombre
            Shape shadow = new RoundRectangle2D.Float(1, 1 + 2, w - 2, h - 4, 8, 8);
            g2.setColor(new Color(0, 0, 0, 36));
            g2.fill(shadow);

            // corps dégradé
            Shape body = new RoundRectangle2D.Float(1, 1 + yOff, w - 2, h - 4, 8, 8);
            Color top = lighten(baseColor, hover ? 0.14f : 0.10f);
            Color bottom = darken(baseColor, pressed ? 0.10f : 0.06f);
            GradientPaint gp = new GradientPaint(0, 1 + yOff, top, 0, h, bottom);
            g2.setPaint(gp);
            g2.fill(body);

            // liseré
            g2.setColor(new Color(255, 255, 255, hover ? 140 : 110));
            g2.setStroke(new BasicStroke(1f));
            g2.draw(body);

            // bordure accent
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), pressed ? 220 : (hover ? 200 : 180)));
            g2.draw(body);

            // contenu
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
}
