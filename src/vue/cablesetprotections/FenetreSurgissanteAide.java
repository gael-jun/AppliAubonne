package vue.cablesetprotections;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import vue.FenetrePrincipale;

/**
 * Classe pour afficher une fenêtre d'aide avec un titre et une image.
 */
public class FenetreSurgissanteAide {

    /**
     * Constructeur par défaut de la classe {@code FenetreSurgissanteAide}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code FenetreSurgissanteAide}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public FenetreSurgissanteAide() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
	
    /**
     * Affiche une fenêtre d'aide avec un titre et une image.
     * 
     * @param titre Le titre de la fenêtre d'aide.
     * @param imageIcon L'image à afficher dans la fenêtre d'aide.
     */
    public static void showAide(String titre, ImageIcon imageIcon) {
        JPanel panelAide = new JPanel();
        panelAide.setLayout(new BoxLayout(panelAide, BoxLayout.Y_AXIS));
        panelAide.setBackground(Color.WHITE);

        // Création et configuration du titre principal
        JLabel titrePrincipal = new JLabel(titre);
        titrePrincipal.setHorizontalAlignment(SwingConstants.CENTER);
        Font font = titrePrincipal.getFont();
        titrePrincipal.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
        titrePrincipal.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        titrePrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelAide.add(titrePrincipal);

        panelAide.add(Box.createRigidArea(new Dimension(0, 10)));

        // Création du panneau d'image avec défilement
        ImagePanel imagePanel = new ImagePanel(imageIcon);
        imagePanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);

        panelAide.add(scrollPane);

        // Création et affichage de la fenêtre
        JFrame frame = new JFrame("Aide");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        frame.setIconImage(FenetrePrincipale.getLogo().getImage());
        frame.setContentPane(panelAide);
        frame.setVisible(true);
    }
}
