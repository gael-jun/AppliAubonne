package vue.cablesetprotections.vue.cablesetprotections.customrenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Rendu personnalisé pour les cellules de la table de protection parafoudre en courant alternatif (AC).
 * 
 * <p>Cette classe modifie la couleur de fond des cellules en fonction des critères techniques spécifiques 
 * aux protections parafoudre en AC. La couleur de la cellule est ajustée selon les valeurs des colonnes et 
 * les seuils définis pour les protections.</p>
 */
public class CustomRendererTableParafoudreAC extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    /**
     * La valeur de la tension de protection (Uw).
     * <p>Utilisée pour comparer avec les valeurs des cellules afin de déterminer la couleur de fond.</p>
     */
	private double Uw;
	
    /**
     * Constructeur pour {@code CustomRendererTableParafoudreAC}.
     * 
     * @param Uw la valeur de la tension de protection utilisée pour les comparaisons.
     */
	public CustomRendererTableParafoudreAC(double Uw) {
		this.Uw = Uw;
	}

    /**
     * Définit le rendu personnalisé de la cellule en fonction des critères de protection parafoudre en AC.
     * 
     * La couleur de fond est déterminée selon les conditions suivantes :
     * <ul>
     *   <li>Ligne 2 : Vert si {@code Up < 0.8 * Uw}, rouge sinon.</li>
     *   <li>Ligne 3 : Vert si {@code In >= 5}, rouge sinon.</li>
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
            if (row == 2 && table.getValueAt(2, column) != null && Uw != -1.0) {
                double Up = Double.parseDouble(table.getValueAt(2, column).toString());
                cell.setBackground(Up < 0.8 * Uw ? Color.GREEN : Color.RED);
            } else if (row == 3 && table.getValueAt(3, column) != null) {
                double In = Double.parseDouble(table.getValueAt(3, column).toString());
                cell.setBackground(In >= 5 ? Color.GREEN : Color.RED);
            }
        } catch (Exception e) {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}
