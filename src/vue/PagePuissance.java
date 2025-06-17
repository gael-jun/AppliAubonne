package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Représente la page de gestion de la puissance dans l'application.
 * Cette classe contient des tableaux pour afficher les données de puissance, les coefficients de température,
 * et les valeurs maximales de tension en l'absence de coefficients.
 */
public class PagePuissance {

    private JPanel pagePuissance;
    private static JTable tablePuissance1;
    private static JTable tablePuissance2;
    private static JTable tablePuissance3;

    /**
     * Constructeur de la classe PagePuissance.
     * Initialise les tableaux pour afficher les données de puissance et crée l'interface utilisateur associée.
     */
    public PagePuissance() {
        pagePuissance = new JPanel(new BorderLayout());

        String[] colonnes1 = {"", "T STC", "T min", "T max"};
        Object[][] donnees1 = new Object[6][4];
        donnees1[0][0] = "T (°C)";
        donnees1[1][0] = "Pmax (Wc)";
        donnees1[2][0] = "Uoc (V)";
        donnees1[3][0] = "Umpp (V)";
        donnees1[4][0] = "Isc (A)";
        donnees1[5][0] = "Impp (A)";

        tablePuissance1 = new JTable(new DefaultTableModel(donnees1, colonnes1) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || (row > 0 && column > 1));
            }
        });

        tablePuissance1.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tablePuissance1.getTableHeader().setReorderingAllowed(false);
        tablePuissance1.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablePuissance1.getColumnModel().getColumn(0).setMinWidth(300);
        tablePuissance1.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPane1 = new JScrollPane(tablePuissance1);
        scrollPane1.getViewport().setBackground(Color.WHITE);

        String[] colonnes2 = {"", "Coefficient de température (CT)"};
        Object[][] donnees2 = new Object[3][2];
        donnees2[0][0] = "CT PMPP (%/K)";
        donnees2[1][0] = "CT Uoc(%/K)";
        donnees2[2][0] = "CT Isc(%/K)";

        tablePuissance2 = new JTable(new DefaultTableModel(donnees2, colonnes2) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        });

        tablePuissance2.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tablePuissance2.getTableHeader().setReorderingAllowed(false);
        tablePuissance2.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablePuissance2.getColumnModel().getColumn(0).setMinWidth(300);
        tablePuissance2.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPane2 = new JScrollPane(tablePuissance2);
        scrollPane2.getViewport().setBackground(Color.WHITE);

        String[] colonnes3 = {"Uocmax sans connaitre les coefficients"};
        Object[][] donnees3 = new Object[1][1];

        tablePuissance3 = new JTable(new DefaultTableModel(donnees3, colonnes3) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tablePuissance3.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tablePuissance3.getTableHeader().setReorderingAllowed(false);
        tablePuissance3.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablePuissance3.getColumnModel().getColumn(0).setMinWidth(300);
        tablePuissance3.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPane3 = new JScrollPane(tablePuissance3);
        scrollPane3.getViewport().setBackground(Color.WHITE);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(scrollPane2);
        rightPanel.add(scrollPane3);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane1, rightPanel);
        splitPane.setResizeWeight(0.5);

        pagePuissance.add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Retourne la table 1.
     *
     * @return {@code JTable} représentant la table 1.
     */
    public static JTable getTablePuissance1() {
        return tablePuissance1;
    }

    /**
     * Retourne la table 2.
     *
     * @return {@code JTable} représentant la table 2.
     */
    public static JTable getTablePuissance2() {
        return tablePuissance2;
    }

    /**
     * Retourne la table 3.
     *
     * @return {@code JTable} représentant la table 3.
     */
    public static JTable getTablePuissance3() {
        return tablePuissance3;
    }
    
    /**
     * Obtient le panneau principal de la page de puissance.
     * 
     * @return Le panneau principal de la page de puissance.
     */
    public JPanel getPage() {
        return this.pagePuissance;
    }
}
