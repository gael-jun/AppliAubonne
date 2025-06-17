package vue.cablesetprotections.vue.cablesetprotections.customrenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Rendu personnalisé pour les cellules de la table des disjoncteurs associés au parafoudre AC.
 * 
 * <p>Cette classe ajuste la couleur de fond des cellules en fonction des critères techniques des disjoncteurs 
 * associés au parafoudre AC. La couleur de la cellule est modifiée selon les valeurs des colonnes spécifiques.</p>
 */
public class CustomRendererTableDisjoncteurAssocieAuParafoudreAC extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	
    /**
     * Constructeur par défaut pour {@code CustomRendererTableDisjoncteurAssocieAuParafoudreAC}.
     * 
     * <p>Ce constructeur utilise le constructeur par défaut de {@code DefaultTableCellRenderer}, 
     * sans paramètres supplémentaires.</p>
     */
    public CustomRendererTableDisjoncteurAssocieAuParafoudreAC() {
        super();
    }
	
    /**
     * Définit le rendu personnalisé de la cellule en fonction des critères des disjoncteurs associés au parafoudre AC.
     * 
     * La couleur de fond est déterminée selon les conditions suivantes :
     * <ul>
     *   <li>Ligne 2 : Vert si {@code Icu >= 5.0}, rouge sinon.</li>
     * </ul>
     * Si une exception survient ou si les conditions ne sont pas remplies, la couleur par défaut est le blanc.
     * 
     * @param table la table contenant la cellule.
     * @param value la valeur de la cellule.
     * @param isSelected indique si la cellule est sélectionnée.
     * @param hasFocus indique si la cellule a le focus.
     * @param row l'indice de la ligne de la cellule.
     * @param column l'indice de la colonne de la cellule.
     * @return le composant à afficher comme cellule de la table, avec le rendu défini.
     */
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        cell.setBackground(Color.WHITE);

        try {
            if (row == 2 && table.getValueAt(2, column) != null) {
                double Icu = Double.parseDouble(table.getValueAt(2, column).toString());
                cell.setBackground(Icu >= 5.0 ? Color.GREEN : Color.RED);
            }
        } catch (Exception e) {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}
