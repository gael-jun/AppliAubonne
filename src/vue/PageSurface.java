package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import main.Main;
import modele.Calcul;
import modele.EtudeDUnePente;

/**
 * Cette classe crée et gère les panneaux pour afficher des tableaux relatifs à la surface des pentes
 * et à leurs configurations dans différentes orientations (portrait et paysage).
 */
public class PageSurface {

    private static JPanel testPanel;
    private static JPanel pageSurface;
    private static JPanel ensemble3PanelsQuiSontPortraitPaysageEtFin;
    private static JPanel panelPortrait;
    private static JPanel panelPaysage;
    private static JPanel panelFin;
    private static JTable tableSurface;
    private static JTable tableSurfaceFin;
    private static JTable tablePortrait;
    private static JTable tablePaysage;

    /**
     * Constructeur de la classe PageSurface. Initialise les panneaux et les tableaux.
     */
    public PageSurface() {
        testPanel = new JPanel(new BorderLayout());

        pageSurface = new JPanel(new BorderLayout());
        pageSurface.setBackground(Color.WHITE);

        ensemble3PanelsQuiSontPortraitPaysageEtFin = new JPanel(new GridLayout(3, 1));

        panelPortrait = new JPanel(new GridLayout(1, 1));
        panelPaysage = new JPanel(new GridLayout(1, 1));
        panelFin = new JPanel(new GridLayout(1, 1));

        initialiserTableSurface();
        initialiserTablePortrait();
        initialiserTablePaysage();
        initialiserTableSurfaceFin();

        ensemble3PanelsQuiSontPortraitPaysageEtFin.add(panelPortrait);
        ensemble3PanelsQuiSontPortraitPaysageEtFin.add(panelPaysage);
        ensemble3PanelsQuiSontPortraitPaysageEtFin.add(panelFin);

        testPanel.add(ensemble3PanelsQuiSontPortraitPaysageEtFin, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        JButton boutonAjouterPente = new JButton("Ajouter Pente");
        JButton boutonRetirerPente = new JButton("Retirer Pente");

        boutonAjouterPente.addActionListener(Main.controleur);
        boutonRetirerPente.addActionListener(Main.controleur);

        buttonPanel.add(boutonRetirerPente);
        buttonPanel.add(boutonAjouterPente);
        pageSurface.add(buttonPanel, BorderLayout.NORTH);
        pageSurface.add(testPanel, BorderLayout.CENTER);
    }

    /**
     * Initialise le tableau pour la surface avec les données et les paramètres requis.
     */
    private static void initialiserTableSurface() {
        String[] columnNames = {"", "Pente 1"};
        Object[][] data = new Object[14][2];

        data[0][0] = "Dimension gouttière pente (m)";
        data[1][0] = "Dimension rampant pente (m)";
        data[2][0] = "Module";
        data[3][0] = "Longueur module (m)";
        data[4][0] = "Largeur module (m)";
        data[5][0] = "Pnom module (Wc)";
        data[6][0] = "Élément de fixation";
        data[7][0] = "Écart minimum imposé sens gouttière (m)";
        data[8][0] = "Écart minimum imposé sens rampant (m)";
        data[9][0] = "Débord minimum sens gouttière (m)";
        data[10][0] = "Débord minimum sens rampant (m)";
        data[11][0] = "Surface (m²)";
        data[12][0] = "Longueur module + intermodule (m)";
        data[13][0] = "Largeur module + intermodule (m)";

        tableSurface = new JTable(new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 0 || row > 10);
            }
        });

        tableSurface.setBackground(Color.WHITE);
        tableSurface.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableSurface.getTableHeader().setReorderingAllowed(false);
        tableSurface.getColumnModel().getColumn(0).setPreferredWidth(300);
        tableSurface.getColumnModel().getColumn(0).setMinWidth(300);
        tableSurface.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPane = new JScrollPane(tableSurface);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        testPanel.add(scrollPane, BorderLayout.NORTH);
    }

    /**
     * Initialise le tableau pour l'orientation portrait avec les données et les paramètres requis.
     */
    private static void initialiserTablePortrait() {
        String[] colonnesPortrait = {"PORTRAIT"};
        Object[][] donneesPortrait = new Object[6][1];
        donneesPortrait[0][0] = "Nb modules sens gouttière";
        donneesPortrait[1][0] = "Nb modules sens rampant";
        donneesPortrait[2][0] = "Débord total sens gouttière (m)";
        donneesPortrait[3][0] = "Débord total sens rampant (m)";
        donneesPortrait[4][0] = "Nb total modules / pente";
        donneesPortrait[5][0] = "Nb total modules";

        tablePortrait = new JTable(new DefaultTableModel(donneesPortrait, colonnesPortrait) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        tablePortrait.setBackground(Color.WHITE);
        tablePortrait.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tablePortrait.getTableHeader().setReorderingAllowed(false);
        tablePortrait.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablePortrait.getColumnModel().getColumn(0).setMinWidth(300);
        tablePortrait.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPanelPortrait = new JScrollPane(tablePortrait);
        scrollPanelPortrait.setBackground(Color.WHITE);
        scrollPanelPortrait.getViewport().setBackground(Color.WHITE);

        panelPortrait.add(scrollPanelPortrait, BorderLayout.CENTER);
    }

    /**
     * Initialise le tableau pour l'orientation paysage avec les données et les paramètres requis.
     */
    private static void initialiserTablePaysage() {
        String[] colonnesPaysage = {"PAYSAGE"};
        Object[][] donneesPaysage = new Object[6][1];
        donneesPaysage[0][0] = "Nb modules sens gouttière";
        donneesPaysage[1][0] = "Nb modules sens rampant";
        donneesPaysage[2][0] = "Débord total sens gouttière (m)";
        donneesPaysage[3][0] = "Débord total sens rampant (m)";
        donneesPaysage[4][0] = "Nb total modules / pente";
        donneesPaysage[5][0] = "Nb total modules";

        tablePaysage = new JTable(new DefaultTableModel(donneesPaysage, colonnesPaysage) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tablePaysage.setBackground(Color.WHITE);
        tablePaysage.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tablePaysage.getTableHeader().setReorderingAllowed(false);
        tablePaysage.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablePaysage.getColumnModel().getColumn(0).setMinWidth(300);
        tablePaysage.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPanelPaysage = new JScrollPane(tablePaysage);
        scrollPanelPaysage.setBackground(Color.WHITE);
        scrollPanelPaysage.getViewport().setBackground(Color.WHITE);

        panelPaysage.add(scrollPanelPaysage, BorderLayout.CENTER);
    }

    /**
     * Initialise le tableau pour les données finales de surface avec les données et les paramètres requis.
     */
    private static void initialiserTableSurfaceFin() {
        String[] colonnesFin = {"", ""};
        Object[][] donneesFin = new Object[2][2];

        donneesFin[0][0] = "Configuration optimale";
        donneesFin[1][0] = "Puissance maximum du générateur (kWc)";

        tableSurfaceFin = new JTable(new DefaultTableModel(donneesFin, colonnesFin) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tableSurfaceFin.setBackground(Color.WHITE);
        tableSurfaceFin.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableSurfaceFin.getTableHeader().setReorderingAllowed(false);
        tableSurfaceFin.getColumnModel().getColumn(0).setPreferredWidth(300);
        tableSurfaceFin.getColumnModel().getColumn(0).setMinWidth(300);
        tableSurfaceFin.getColumnModel().getColumn(0).setMaxWidth(300);

        JScrollPane scrollPaneFin = new JScrollPane(tableSurfaceFin);
        scrollPaneFin.setBackground(Color.WHITE);
        scrollPaneFin.getViewport().setBackground(Color.WHITE);

        panelFin.add(scrollPaneFin);
    }

    /**
     * Ajoute une colonne pour une nouvelle pente au tableau de surface.
     */
    public static void ajouterPente() {
        DefaultTableModel modelSurface = (DefaultTableModel) tableSurface.getModel();
        int rowCount = modelSurface.getRowCount();
        int columnCount = modelSurface.getColumnCount();
        Object[][] newData = new Object[rowCount][columnCount + 1];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                newData[i][j] = modelSurface.getValueAt(i, j);
            }
            newData[i][columnCount] = "";
        }

        String[] columnNames = new String[columnCount + 1];
        for (int j = 0; j < columnCount; j++) {
            columnNames[j] = modelSurface.getColumnName(j);
        }
        columnNames[columnCount] = "Pente " + columnCount;

        modelSurface.setDataVector(newData, columnNames);
    }

    /**
     * Retire la dernière colonne pour une pente du tableau de surface.
     */
    public static void retirerPente() {
        DefaultTableModel modelSurface = (DefaultTableModel) tableSurface.getModel();
        int rowCount = modelSurface.getRowCount();
        int columnCount = modelSurface.getColumnCount();

        if (columnCount > 2) {  // Assurez-vous de ne pas retirer toutes les colonnes
            Object[][] newData = new Object[rowCount][columnCount - 1];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount - 1; j++) {
                    newData[i][j] = modelSurface.getValueAt(i, j);
                }
            }
            String[] columnNames = new String[columnCount - 1];
            for (int j = 0; j < columnCount - 1; j++) {
                columnNames[j] = modelSurface.getColumnName(j);
            }

            modelSurface.setDataVector(newData, columnNames);
        }
    }

    /**
     * Ajoute des données supplémentaires aux tableaux portrait et paysage pour une étude de pente donnée.
     * 
     * @param etude L'étude de pente à ajouter aux tableaux.
     */
    public static void ajouterTableSupplementaire(EtudeDUnePente etude) {
        // Pour le tableau portrait
        DefaultTableModel modelPortrait = (DefaultTableModel) tablePortrait.getModel();
        int rowCount = modelPortrait.getRowCount();
        int columnCount = modelPortrait.getColumnCount();
        Object[][] donneesPortrait = new Object[rowCount][columnCount + 1];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                donneesPortrait[i][j] = modelPortrait.getValueAt(i, j);
            }
        }

        donneesPortrait[0][columnCount] = etude.getNbModulesPortraitSensGouttiere();
        donneesPortrait[1][columnCount] = etude.getNbModulesPortraitSensRampant();
        donneesPortrait[2][columnCount] = etude.getDebordTotalSensGouttierePortrait();
        donneesPortrait[3][columnCount] = etude.getDebordTotalSensRampantPortrait();
        donneesPortrait[4][columnCount] = etude.getNbTotalModulesPortrait();
        donneesPortrait[5][columnCount] = Calcul.getNbTotalModulesPentesPortrait();

        String[] columnNames = new String[columnCount + 1];
        for (int j = 0; j < columnCount; j++) {
            columnNames[j] = modelPortrait.getColumnName(j);
        }
        columnNames[columnCount] = "Pente " + columnCount;

        modelPortrait.setDataVector(donneesPortrait, columnNames);

        // Pour le tableau paysage
        DefaultTableModel modelPaysage = (DefaultTableModel) tablePaysage.getModel();
        rowCount = modelPaysage.getRowCount();
        columnCount = modelPaysage.getColumnCount();
        Object[][] donneesPaysage = new Object[rowCount][columnCount + 1];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                donneesPaysage[i][j] = modelPaysage.getValueAt(i, j);
            }
        }

        donneesPaysage[0][columnCount] = etude.getNbModulesPaysageSensGouttiere();
        donneesPaysage[1][columnCount] = etude.getNbModulesPaysageSensRampant();
        donneesPaysage[2][columnCount] = etude.getDebordTotalSensGouttierePaysage();
        donneesPaysage[3][columnCount] = etude.getDebordTotalSensRampantPaysage();
        donneesPaysage[4][columnCount] = etude.getNbTotalModulesPaysage();
        donneesPaysage[5][columnCount] = Calcul.getNbTotalModulesPentesPaysage();

        for (int j = 0; j < columnCount; j++) {
            columnNames[j] = modelPaysage.getColumnName(j);
        }
        columnNames[columnCount] = "Pente " + columnCount;

        modelPaysage.setDataVector(donneesPaysage, columnNames);
    }

    /**
     * Retire la dernière colonne de chaque tableau (portrait et paysage).
     */
    private static void enleverTable() {
        // Pour le tableau portrait
        DefaultTableModel modelPortrait = (DefaultTableModel) tablePortrait.getModel();
        int rowCount = modelPortrait.getRowCount();
        int columnCount = modelPortrait.getColumnCount();

        if (columnCount > 1) {
            Object[][] newData = new Object[rowCount][columnCount - 1];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount - 1; j++) {
                    newData[i][j] = modelPortrait.getValueAt(i, j);
                }
            }
            String[] columnNames = new String[columnCount - 1];
            for (int j = 0; j < columnCount - 1; j++) {
                columnNames[j] = modelPortrait.getColumnName(j);
            }

            modelPortrait.setDataVector(newData, columnNames);
        }

        // Pour le tableau paysage
        DefaultTableModel modelPaysage = (DefaultTableModel) tablePaysage.getModel();
        rowCount = modelPaysage.getRowCount();
        columnCount = modelPaysage.getColumnCount();

        if (columnCount > 1) {
            Object[][] newData = new Object[rowCount][columnCount - 1];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount - 1; j++) {
                    newData[i][j] = modelPaysage.getValueAt(i, j);
                }
            }
            String[] columnNames = new String[columnCount - 1];
            for (int j = 0; j < columnCount - 1; j++) {
                columnNames[j] = modelPaysage.getColumnName(j);
            }

            modelPaysage.setDataVector(newData, columnNames);
        }
    }
    
    /**
     * Met à jour les données des tableaux avec les informations des études de pentes fournies.
     * 
     * @param etudesPentes Liste des études de pentes à utiliser pour mettre à jour les tableaux.
     */
    public static void updateTableData(List<EtudeDUnePente> etudesPentes) {
        int columnCount = tableSurface.getColumnCount();

        for (int j = 0; j < columnCount - 1; j++) {
        	tableSurface.setValueAt(etudesPentes.get(j).getSurface(), 11, j + 1);
        	tableSurface.setValueAt(etudesPentes.get(j).getLi(), 12, j + 1);
        	tableSurface.setValueAt(etudesPentes.get(j).getli(), 13, j + 1);
            ajouterTableSupplementaire(etudesPentes.get(j));
        }

        tableSurfaceFin.setValueAt(Calcul.getConfigurationOptimale(), 0, 1);
        tableSurfaceFin.setValueAt(Calcul.getPuissanceMaxGenerateur(), 1, 1);
    }

    /**
     * Retire toutes les colonnes sauf la première des tableaux portrait et paysage.
     */
    public static void enleverTables() {
        while (tablePaysage.getColumnCount() > 1) {
            enleverTable();
        }
    }
    
    /**
     * Retourne la table pour la surface.
     * 
     * @return {@code JTable} représentant la table pour la surface.
     */
    public static JTable getTableSurface() {
        return tableSurface;
    }

    /**
     * Retourne la table pour la surface finale.
     * 
     * @return {@code JTable} représentant la table pour la surface finale.
     */
    public static JTable getTableSurfaceFin() {
        return tableSurfaceFin;
    }

    /**
     * Retourne la table pour le portrait.
     * 
     * @return {@code JTable} représentant la table pour le format portrait.
     */
    public static JTable getTablePortrait() {
        return tablePortrait;
    }

    /**
     * Retourne la table pour le paysage.
     * 
     * @return {@code JTable} représentant la table pour le format paysage.
     */
    public static JTable getTablePaysage() {
        return tablePaysage;
    }

    /**
     * Renvoie le panneau principal de la page surface.
     * 
     * @return Le panneau principal de la page surface.
     */
    public JPanel getPage() {
        return pageSurface;
    }
}
