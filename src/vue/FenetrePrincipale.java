package vue;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import main.Main;

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

        // Tentative de chargement du logo depuis /img/logo.png puis /logo.png
        java.net.URL logoUrl = FenetrePrincipale.class.getResource("/img/logo.png");
        if (logoUrl == null) {
            logoUrl = FenetrePrincipale.class.getResource("/logo.png");
        }
        if (logoUrl != null) {
            logo = new ImageIcon(logoUrl);
        } else {
            logo = new ImageIcon(); // Logo vide si non trouvé
            System.err.println("Logo non trouvé dans le classpath (ni /img/logo.png ni /logo.png)");
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

        JPanel boutonsMenuPanel = new JPanel();
        boutonsMenuPanel.setLayout(new FlowLayout());
        boutonsMenuPanel.setBackground(Color.WHITE);
        
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBackground(Color.WHITE);
        pageSurface = new PageSurface();
        pagePuissance = new PagePuissance();
        pageOnduleur = new PageOnduleur();
        pageCablesProtections = new PageCablesProtections();

        cardPanel.add(pageSurface.getPage(), "Surface");
        cardPanel.add(pagePuissance.getPage(), "Puissance");
        cardPanel.add(pageOnduleur.getPage(), "Onduleur");
        cardPanel.add(pageCablesProtections.getPage(), "Câbles_Protections");

        JButton boutonSurface = new JButton("Surface");
        JButton boutonPuissance = new JButton("Puissance");
        JButton boutonOndulateur = new JButton("Onduleur");
        JButton boutonCablesProtections = new JButton("Câbles_Protections");
        JButton boutonExporter = new JButton("Exporter");
        JButton boutonCalculer = new JButton("Calculer");
        
        JPanel calculPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        calculPanel.add(boutonExporter);
        calculPanel.add(boutonCalculer);
        calculPanel.setBackground(Color.WHITE);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(logoPanel, BorderLayout.CENTER);
        bottomPanel.add(calculPanel, BorderLayout.EAST);
        bottomPanel.setBackground(Color.WHITE);

        boutonsMenuPanel.add(boutonSurface);
        boutonsMenuPanel.add(boutonPuissance);
        boutonsMenuPanel.add(boutonOndulateur);
        boutonsMenuPanel.add(boutonCablesProtections);

        mainPanel.add(boutonsMenuPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        fenetre.add(mainPanel);
        
        cardSurface = (CardLayout) cardPanel.getLayout();
        cardSurface.show(cardPanel, "Surface");
        carteCourante = "Surface";
        
        boutonSurface.addActionListener(Main.controleur);
        boutonPuissance.addActionListener(Main.controleur);
        boutonOndulateur.addActionListener(Main.controleur);
        boutonCablesProtections.addActionListener(Main.controleur);
        boutonCalculer.addActionListener(Main.controleur);
        boutonExporter.addActionListener(Main.controleur);
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
    }
    
    /**
     * Affiche la page "Puissance".
     */
    public void showPuissance() {
        cardSurface.show(cardPanel, "Puissance");
        carteCourante = "Puissance";
    }
    
    /**
     * Affiche la page "Onduleur".
     */
    public void showOnduleur() {
        cardSurface.show(cardPanel, "Onduleur");
        carteCourante = "Onduleur";
    }
    
    /**
     * Affiche la page "Câbles_Protections".
     */
    public void showCablesProtections() {
        cardSurface.show(cardPanel, "Câbles_Protections");
        carteCourante = "Câbles_Protections";
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
}
