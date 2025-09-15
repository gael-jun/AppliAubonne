package vue.cablesetprotections.vue.cablesetprotections.customrenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Rendu personnalisé pour une cellule de table qui représente la chute de tension totale en courant continu (DC).
 * 
 * <p>Cette classe détermine la couleur de fond de la cellule en fonction du résultat calculé. Si la valeur est inférieure à 
 * 1.0, la cellule est colorée en vert. Si elle est comprise entre 1.0 et 3.0, elle est colorée en orange, et au-delà 
 * de 3.0, elle est colorée en rouge. Ce rendu est appliqué uniquement à la ligne 2 de la table.</p>
 */
public class CustomRendererTableChuteDeTensionTotaleDCMaximumARespecter extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

    /**
     * La valeur du résultat.
     * <p>Utilisée pour comparer avec les valeurs des cellules afin de déterminer la couleur de fond.</p>
     */
	private double resultat;
	
    /**
     * Constructeur qui initialise le rendu avec le résultat calculé.
     * 
     * @param resultat le résultat de la chute de tension à utiliser pour déterminer la couleur de la cellule.
     */
	public CustomRendererTableChuteDeTensionTotaleDCMaximumARespecter(double resultat) {
		this.resultat = resultat;
	}
	
    /**
     * Méthode qui définit le rendu personnalisé de la cellule en fonction du résultat.
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
            if (row == 2 && resultat != -1) {
                cell.setBackground(this.resultat < 1.0 ? Color.GREEN : (this.resultat < 3.0 ? Color.ORANGE : Color.RED));
            }
        } catch (Exception e) {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }
}
