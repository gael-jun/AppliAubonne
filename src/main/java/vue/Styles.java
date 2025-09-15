package vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Cette classe fournit des styles personnalisés pour les cellules de tableau.
 * Elle permet de définir des rendus spécifiques pour les cellules de JTable.
 */
public class Styles {

    /**
     * Constructeur par défaut de la classe {@code Styles}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code Styles}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public Styles() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
	
    /**
     * Crée et retourne un rendu de cellule personnalisé pour les tableaux.
     * Les cellules de la première colonne sont affichées avec une police en gras.
     * Les cellules ayant le focus sont surlignées en jaune.
     * 
     * @return Un DefaultTableCellRenderer avec des styles personnalisés.
     */
    public static DefaultTableCellRenderer tableRenderer() {
        DefaultTableCellRenderer customCellRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            private final Font boldFont = getFont().deriveFont(Font.BOLD);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Change la couleur de fond si la cellule a le focus
                if (hasFocus) {
                    rendererComponent.setBackground(Color.YELLOW);
                } else {
                    rendererComponent.setBackground(Color.WHITE);
                }
                
                // Applique une police en gras pour la première colonne
                if (column == 0) {
                    rendererComponent.setFont(boldFont);
                }

                return rendererComponent;
            }
        };
        return customCellRenderer;
    }
}
