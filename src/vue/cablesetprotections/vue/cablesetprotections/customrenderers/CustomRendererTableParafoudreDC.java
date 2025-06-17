package vue.cablesetprotections.vue.cablesetprotections.customrenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Rendu personnalisé pour les cellules de la table de protection parafoudre en courant continu (DC).
 * 
 * <p>Cette classe détermine la couleur de fond des cellules en fonction de plusieurs critères techniques relatifs 
 * aux protections parafoudre. La couleur de la cellule varie entre vert et rouge selon la validation des critères 
 * de sécurité, et le rendu est appliqué à différentes lignes de la table en fonction des valeurs des colonnes.</p>
 */
public class CustomRendererTableParafoudreDC extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut pour {@code CustomRendererTableParafoudreDC}.
     * 
     * <p>Ce constructeur utilise le constructeur par défaut de {@code DefaultTableCellRenderer}, 
     * sans paramètres supplémentaires.</p>
     */
    public CustomRendererTableParafoudreDC() {
        super();
    }
	
    /**
     * Méthode qui définit le rendu personnalisé de la cellule en fonction des critères de protection parafoudre.
     * 
     * Selon la ligne et les valeurs des cellules de la table, la couleur de fond de la cellule est déterminée :
     * <ul>
     *   <li>Ligne 4 : Vert si {@code Ucpv >= Uocmax}, rouge sinon.</li>
     *   <li>Ligne 5 : Vert si {@code Up < val08Uw}, rouge sinon.</li>
     *   <li>Ligne 6 : Vert si {@code In >= 5.0}, rouge sinon.</li>
     *   <li>Ligne 7 : Vert si {@code Iscpv >= Iscmax}, rouge sinon.</li>
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
            if (row == 4 && table.getValueAt(0, column) != null && table.getValueAt(4, column) != null) {
                double Uocmax = Double.parseDouble(table.getValueAt(0, column).toString());
                double Ucpv = Double.parseDouble(table.getValueAt(4, column).toString());
                cell.setBackground(Ucpv >= Uocmax ? Color.GREEN : Color.RED);
            } else if (row == 5 && table.getValueAt(1, column) != null && table.getValueAt(5, column) != null) {
                double val08Uw = Double.parseDouble(table.getValueAt(1, column).toString());
                double Up = Double.parseDouble(table.getValueAt(5, column).toString());
                cell.setBackground(Up < val08Uw ? Color.GREEN : Color.RED);
            } else if (row == 6 && table.getValueAt(6, column) != null) {
                double In = Double.parseDouble(table.getValueAt(6, column).toString());
                cell.setBackground(In >= 5.0 ? Color.GREEN : Color.RED);
            } else if (row == 7 && table.getValueAt(2, column) != null && table.getValueAt(7, column) != null) {
                double Iscmax = Double.parseDouble(table.getValueAt(2, column).toString());
                double Iscpv = Double.parseDouble(table.getValueAt(7, column).toString());
                cell.setBackground(Iscpv >= Iscmax ? Color.GREEN : Color.RED);
            }
        } catch (Exception e) {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}