package vue.cablesetprotections.vue.cablesetprotections.customrenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Rendu personnalisé pour les cellules de la table des disjoncteurs AC associés aux onduleurs.
 * 
 * <p>Cette classe ajuste la couleur de fond des cellules en fonction des critères techniques des disjoncteurs 
 * associés aux onduleurs. La couleur de la cellule est modifiée selon les valeurs des colonnes spécifiques.</p>
 */
public class CustomRendererTableDisjoncteursACAssociesAuxOnduleurs extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut pour {@code CustomRendererTableDisjoncteursACAssociesAuxOnduleurs}.
     * 
     * <p>Ce constructeur utilise le constructeur par défaut de {@code DefaultTableCellRenderer}, 
     * sans paramètres supplémentaires.</p>
     */
    public CustomRendererTableDisjoncteursACAssociesAuxOnduleurs() {
        super();
    }
	
    /**
     * Définit le rendu personnalisé de la cellule en fonction des critères des disjoncteurs AC associés aux onduleurs.
     * 
     * La couleur de fond est déterminée selon les conditions suivantes :
     * <ul>
     *   <li>Ligne 4 : Vert si {@code Icu >= 3}, rouge sinon.</li>
     *   <li>Ligne 5 : Vert si {@code In >= Ie}, rouge sinon.</li>
     *   <li>Ligne 7 : Vert si la courbe est "C", rouge sinon.</li>
     *   <li>Ligne 8 : Vert si le type est "AC" ou "A", rouge sinon.</li>
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
            if (row == 4 && table.getValueAt(4, column) != null) {
                double Icu = Double.parseDouble(table.getValueAt(4, column).toString());
                cell.setBackground(Icu >= 3 ? Color.GREEN : Color.RED);
            } else if (row == 5 && table.getValueAt(1, column) != null && table.getValueAt(5, column) != null) {
                double In = Double.parseDouble(table.getValueAt(5, column).toString());
                double Ie = Double.parseDouble(table.getValueAt(1, column).toString());
                cell.setBackground(In >= Ie ? Color.GREEN : Color.RED);
            } else if (row == 7 && table.getValueAt(7, column) != null) {
                String courbe = table.getValueAt(7, column).toString();
                cell.setBackground(courbe.equals("C") ? Color.GREEN : Color.RED);
            } else if (row == 8 && table.getValueAt(8, column) != null) {
                String type = table.getValueAt(8, column).toString();
                cell.setBackground(type.equals("AC") || type.equals("A") ? Color.GREEN : Color.RED);
            }
        } catch (Exception e) {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}
