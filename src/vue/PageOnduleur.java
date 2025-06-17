package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controleur.Controleur;
import main.Main;
import modele.Calcul;
import modele.EtudeDUnOnduleur;

/**
 * Représente la page pour la gestion des onduleurs dans l'application.
 * Cette classe gère l'affichage des tables liées aux onduleurs, ainsi que l'ajout et la suppression d'onglets.
 */
public class PageOnduleur {

	private static boolean pageCablesEtProtectionsOuvrable;
    private JPanel pageOnduleur;
    private static JTable tableOnduleur;
    private static JTable tableOnduleurBilan;
    private static JTable tableComparatif;
    private static List<JTable> tablesAdditionnelles;
    private static List<JTable> tablesBilan;
    private static List<EtudeDUnOnduleur> etudesConcerneesParChaqueTableDuBilan;
    private static JPanel tablesContainer;
    private static JPanel tablesBilanContainer;
    private static JPanel trioPanels;
    private static JPanel testPanel;
    
    /**
     * Constructeur de la classe PageOnduleur.
     * Initialise les panneaux et les tables pour l'affichage des informations des onduleurs.
     */
    public PageOnduleur() {
    	pageCablesEtProtectionsOuvrable = false;
        testPanel = new JPanel(new BorderLayout());
        pageOnduleur = new JPanel(new BorderLayout());
        pageOnduleur.setBackground(Color.WHITE);
        tablesAdditionnelles = new ArrayList<>();
        tablesBilan = new ArrayList<>();
        etudesConcerneesParChaqueTableDuBilan = new ArrayList<>();

        String[] columnNames = {"", "Onduleur 1"};
        Object[][] data = new Object[15][2];
        
        data[0][0] = "Nom";
        data[1][0] = "PDC max (W)";
        data[2][0] = "UDC max (V)";
        data[3][0] = "Plage UMPP (V)";
        data[4][0] = "IDC max (A)";
        data[5][0] = "IDC max par string (A)";
        data[6][0] = "Nb d'entrées MPP indépendantes";
        data[7][0] = "Chaines par entrée MPP";
        data[8][0] = "PAC max (VA)";
        data[9][0] = "IAC max (A)";
        data[10][0] = "Nb modules max";
        data[11][0] = "Plage de puissance admissible aux entrées";
        data[12][0] = "Choix nb chaînes par entrée";
        data[13][0] = "Compatibilité en puissance";
        data[14][0] = "Choix nb onduleurs";

        tableOnduleur = new JTable(new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || row == 10 || row == 11 || row == 13);
            }
        });

        tableOnduleur.setBackground(Color.WHITE);
        tableOnduleur.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableOnduleur.getTableHeader().setReorderingAllowed(false);
        tableOnduleur.getColumnModel().getColumn(0).setPreferredWidth(300);
        tableOnduleur.getColumnModel().getColumn(0).setMinWidth(300);
        tableOnduleur.getColumnModel().getColumn(0).setMaxWidth(300);
        
        JScrollPane scrollPane = new JScrollPane(tableOnduleur);
        scrollPane.getViewport().setBackground(Color.WHITE);
        testPanel.add(scrollPane, BorderLayout.NORTH);

        trioPanels = new JPanel();
        trioPanels.setLayout(new GridLayout(3, 1));
        trioPanels.setBackground(Color.WHITE);
        
        tablesContainer = new JPanel();
        tablesContainer.setLayout(new GridLayout(1, 1));
        tablesContainer.setBackground(Color.WHITE);

        tablesBilanContainer = new JPanel();
        tablesBilanContainer.setLayout(new GridLayout(1, 1));
        tablesBilanContainer.setBackground(Color.WHITE);
        
        trioPanels.add(tablesContainer);
        trioPanels.add(tablesBilanContainer);
        
        testPanel.add(trioPanels, BorderLayout.CENTER);
        testPanel.setBackground(Color.WHITE);
        
        pageOnduleur.add(testPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        JButton boutonAjouterOnduleur = new JButton("Ajouter Onduleur");
        JButton boutonRetirerOnduleur = new JButton("Retirer Onduleur");

        boutonAjouterOnduleur.addActionListener(Main.controleur);
        boutonRetirerOnduleur.addActionListener(Main.controleur);

        buttonPanel.add(boutonRetirerOnduleur);
        buttonPanel.add(boutonAjouterOnduleur);
        pageOnduleur.add(buttonPanel, BorderLayout.NORTH);
    }
    
    /**
     * Initialise la table de comparatif avec les données de surface.
     * Met à jour la table comparatif avec les données issues du calcul de l'application.
     */
    public static void initialiserTableComparatifAvecSurface() {
        String[] col = {"", "Estimation calepinage physique", "Après dimensionnement \n chaînes - onduleurs", "OK ou NON ?"};
        Object[][] donnees = new Object[2][4];
        donnees[0][0] = "Nb modules";
        donnees[1][0] = "Puissance totale (STC)";
        donnees[0][1] = Calcul.getNbModulesOptimal();
        donnees[1][1] = Calcul.getPuissanceMaxGenerateur();
        donnees[0][2] = Calcul.getNbTotalModulesChoisi();
        donnees[1][2] = Calcul.getPuissanceTotaleChoisie();
        donnees[0][3] = Calcul.getNbTotalModulesChoisi() > Calcul.getNbModulesOptimal() ? "NON" : "OK";
        donnees[1][3] = Calcul.getPuissanceTotaleChoisie() > Calcul.getPuissanceMaxGenerateur() ? "NON" : "OK";

        tableComparatif = new JTable(new DefaultTableModel(donnees, col) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        tableComparatif.setBackground(Color.WHITE);
        tableComparatif.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableComparatif.getTableHeader().setReorderingAllowed(false);
        tableComparatif.getColumnModel().getColumn(0).setPreferredWidth(300);
        tableComparatif.getColumnModel().getColumn(0).setMinWidth(300);
        tableComparatif.getColumnModel().getColumn(0).setMaxWidth(300);
        
        JScrollPane scrollPanel = new JScrollPane(tableComparatif);
        scrollPanel.setBackground(Color.WHITE);
        scrollPanel.getViewport().setBackground(Color.WHITE);
        
        trioPanels.add(scrollPanel, BorderLayout.CENTER);
        
        pageCablesEtProtectionsOuvrable = true;
    }
    
    /**
     * Détruit la table comparatif en supprimant le panneau contenant cette table.
     */
    public static void detruireTableComparatif() {
        if (tableComparatif != null) {
            Container parent = tableComparatif.getParent();
            if (parent != null && parent.getParent() instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) parent.getParent();
                Container grandParent = scrollPane.getParent();
                if (grandParent != null) {
                    grandParent.remove(scrollPane);
                    grandParent.revalidate();
                    grandParent.repaint();
                }
            }
            tableComparatif = null;
        }
        pageCablesEtProtectionsOuvrable = false;
        Controleur.setAnciennePageCablesEtProtections(false);
    }
    
    /**
     * Ajoute une colonne pour un nouvel onduleur dans la table des onduleurs.
     */
    public static void ajouterOnduleur() {
        int rowCount = tableOnduleur.getRowCount();
        int columnCount = tableOnduleur.getColumnCount();
        Object[][] newData = new Object[rowCount][columnCount + 1];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                newData[i][j] = tableOnduleur.getValueAt(i, j);
            }
            newData[i][columnCount] = "";
        }

        String[] columnNames = new String[columnCount + 1];
        for (int j = 0; j < columnCount; j++) {
            columnNames[j] = tableOnduleur.getColumnName(j);
        }
        columnNames[columnCount] = "Onduleur " + columnCount;

        ((DefaultTableModel) tableOnduleur.getModel()).setDataVector(newData, columnNames);
    }
    
    /**
     * Retire la dernière colonne de la table des onduleurs.
     */
    public static void retirerOnduleur() {
        int rowCount = tableOnduleur.getRowCount();
        int columnCount = tableOnduleur.getColumnCount();

        if (columnCount > 2) {
            Object[][] newData = new Object[rowCount][columnCount - 1];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount - 1; j++) {
                    newData[i][j] = tableOnduleur.getValueAt(i, j);
                }
            }
            String[] columnNames = new String[columnCount - 1];
            for (int j = 0; j < columnCount - 1; j++) {
                columnNames[j] = tableOnduleur.getColumnName(j);
            }

            ((DefaultTableModel) tableOnduleur.getModel()).setDataVector(newData, columnNames);
        }
    }
    
    /**
     * Ajoute les tables de bilan en fonction des études des onduleurs.
     */
    public static void ajouterTablesBilan() {
        List<EtudeDUnOnduleur> etudes = Calcul.getEtudesOnduleurs();
        int k = 1;
        for (EtudeDUnOnduleur etude : etudes) {
            for (int i = 0; i < Integer.parseInt(tableOnduleur.getValueAt(14, k).toString()); i++) {
                etudesConcerneesParChaqueTableDuBilan.add(etude);
                ajouterTableBilan(etude);
            }
            k++;
        }
    }
    
    /**
     * Ajoute une table de bilan pour une étude d'onduleur donnée.
     * 
     * @param etude L'étude d'onduleur à ajouter.
     */
    public static void ajouterTableBilan(EtudeDUnOnduleur etude) {
        int nbEntrees = etude.getOnduleur().getEntreesNoms().size();
        String[] col = new String[nbEntrees + 1];
        col[0] = etude.getOnduleur().getNom();
        Object[][] donnees = new Object[4][1];
        donnees[0][0] = "Nb modules par chaîne";
        donnees[1][0] = "Nb chaînes";
        donnees[2][0] = "Nb modules total";
        donnees[3][0] = "Puissance (STC kW)";
        
        for (int i = 1; i <= nbEntrees; i++) {
            col[i] = "Entrée " + etude.getOnduleur().getEntreesNoms().get(i - 1);
        }
        
        tableOnduleurBilan = new JTable(new DefaultTableModel(donnees, col) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && (row == 0 || row == 1);
            }
        });
        
        tableOnduleurBilan.getTableHeader().setReorderingAllowed(false);
        tablesBilan.add(tableOnduleurBilan);
        JScrollPane scrollPane = new JScrollPane(tableOnduleurBilan);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablesBilanContainer.add(scrollPane);
        tablesBilanContainer.revalidate();
        tablesBilanContainer.repaint();
    }
    
    /**
     * Ajoute une table supplémentaire pour une étude d'onduleur donnée.
     * 
     * @param etude L'étude d'onduleur pour laquelle ajouter une table supplémentaire.
     */
    public static void ajouterTableSupplementaire(EtudeDUnOnduleur etude) {
        int nbEntrees = etude.getOnduleur().getEntreesNoms().size();
        String[] columnNames = new String[nbEntrees + 1];
        
        columnNames[0] = etude.getOnduleur().getNom();
        for (int i = 1; i <= nbEntrees; i++) {
            columnNames[i] = "Entrée " + etude.getOnduleur().getEntreesNoms().get(i - 1);
        }
        
        int nbChainesMax = Collections.max(etude.getOnduleur().getChainesParEntreeMPP());
        
        Object[][] data = new Object[nbChainesMax + 1][nbEntrees + 1];

        data[0][0] = "Nb chaînes";
        
        for (int i = 1; i <= nbEntrees; i++) {
            data[0][i] = "IDC (< " + etude.getOnduleur().getI_DCmax().get(i - 1) + " A)";
        }

        for (int i = 1; i <= nbChainesMax; i++) {
            data[i][0] = "Chaîne " + i;
        }

        for (int i = 1; i <= nbEntrees; i++) {
            int k = 1;
            for (int j = 1; j <= nbChainesMax; j++) {
                double IDCCourant = Calcul.arrondirDouble(etude.getEtudeModule().getImppTmax() * k, 2);
                data[j][i] = IDCCourant < etude.getOnduleur().getI_DCmax().get(i - 1) ? IDCCourant : "-";
                k++;
            }
        }
        
        JTable tableAdditionnelle = new JTable(new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        tableAdditionnelle.getTableHeader().setReorderingAllowed(false);
        tablesAdditionnelles.add(tableAdditionnelle);
        JScrollPane scrollPane = new JScrollPane(tableAdditionnelle);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablesContainer.add(scrollPane);
        tablesContainer.revalidate();
        tablesContainer.repaint();
    }
    
    /**
     * Enlève toutes les tables additionnelles de l'affichage.
     */
    public static void enleverTablesAdditionnelles() {
        while (!tablesAdditionnelles.isEmpty()) {
            JTable table = tablesAdditionnelles.remove(tablesAdditionnelles.size() - 1);
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            tablesContainer.remove(scrollPane);
        }
        tablesContainer.revalidate();
        tablesContainer.repaint();
    }
    
    /**
     * Obtient la liste des études concernées par chaque table du bilan.
     * 
     * @return La liste des études concernées.
     */
    public static List<EtudeDUnOnduleur> getEtudesConcerneesParChaqueTableDuBilan() {
        return etudesConcerneesParChaqueTableDuBilan;
    }
    
    /**
     * Enlève toutes les tables de bilan de l'affichage.
     */
    public static void enleverTablesBilan() {
        etudesConcerneesParChaqueTableDuBilan.clear();
        while (!tablesBilan.isEmpty()) {
            JTable table = tablesBilan.remove(tablesBilan.size() - 1);
            JScrollPane scrollPane = (JScrollPane) table.getParent().getParent();
            tablesBilanContainer.remove(scrollPane);
        }
        tablesBilanContainer.revalidate();
        tablesBilanContainer.repaint();
    }
    
    /**
     * Indique si la page "Câbles et Protections" est ouvrable.
     * 
     * @return {@code true} si la page "Câbles et Protections" est ouvrable, sinon {@code false}.
     */
    public static boolean isPageCablesEtProtectionsOuvrable() {
    	return pageCablesEtProtectionsOuvrable;
    }
    
    /**
     * Retourne la table représentant les onduleurs.
     * 
     * @return {@code JTable} représentant la table des onduleurs.
     */
    public static JTable getTableOnduleur() {
        return tableOnduleur;
    }

    /**
     * Retourne la table pour le comparatif.
     * 
     * @return {@code JTable} représentant la table du comparatif.
     */
    public static JTable getTableComparatif() {
        return tableComparatif;
    }

    /**
     * Retourne la liste des tables additionnelles.
     * 
     * @return {@code List<JTable>} représentant les tables additionnelles.
     */
    public static List<JTable> getTablesAdditionnelles() {
        return tablesAdditionnelles;
    }

    /**
     * Retourne la liste des tables bilan.
     * 
     * @return {@code List<JTable>} représentant les tables bilan.
     */
    public static List<JTable> getTablesBilan() {
        return tablesBilan;
    }
    
    /**
     * Obtient le panneau principal de la page des onduleurs.
     * 
     * @return Le panneau principal de la page des onduleurs.
     */
    public JPanel getPage() {
        return this.pageOnduleur;
    }
}
