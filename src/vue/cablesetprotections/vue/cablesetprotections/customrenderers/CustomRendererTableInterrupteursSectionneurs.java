package vue.cablesetprotections.vue.cablesetprotections.customrenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Rendu personnalisé pour les cellules de la table des interrupteurs sectionneurs.
 * 
 * <p>Cette classe modifie la couleur de fond des cellules en fonction des critères techniques spécifiques 
 * aux interrupteurs sectionneurs. La couleur de la cellule est ajustée selon les valeurs des colonnes 
 * et les seuils définis pour les interrupteurs.</p>
 */
public class CustomRendererTableInterrupteursSectionneurs extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    /**
     * Constructeur par défaut pour {@code CustomRendererTableInterrupteursSectionneurs}.
     * 
     * <p>Ce constructeur utilise le constructeur par défaut de {@code DefaultTableCellRenderer}, 
     * sans paramètres supplémentaires.</p>
     */
    public CustomRendererTableInterrupteursSectionneurs() {
        super();
    }
	
    /**
     * Définit le rendu personnalisé de la cellule en fonction des critères des interrupteurs sectionneurs.
     * 
     * La couleur de fond est déterminée selon les conditions suivantes :
     * <ul>
     *   <li>Ligne 3 : Vert si {@code Ue >= Uocmax}, rouge sinon.</li>
     *   <li>Ligne 4 : Vert si {@code In >= Iscmax}, rouge sinon.</li>
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
            if (row == 3 && table.getValueAt(0, column) != null && table.getValueAt(3, column) != null) {
                double Uocmax = Double.parseDouble(table.getValueAt(0, column).toString());
                double Ue = Double.parseDouble(table.getValueAt(3, column).toString());
                cell.setBackground(Ue >= Uocmax ? Color.GREEN : Color.RED);
            } else if (row == 4 && table.getValueAt(1, column) != null && table.getValueAt(4, column) != null) {
                double Iscmax = Double.parseDouble(table.getValueAt(1, column).toString());
                double In = Double.parseDouble(table.getValueAt(4, column).toString());
                cell.setBackground(In >= Iscmax ? Color.GREEN : Color.RED);
            }
        } catch (Exception e) {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}
