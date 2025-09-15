package vue.cablesetprotections;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import modele.cablesetprotections.BDD;
import modele.cablesetprotections.CalculCablesEtProtections;
import vue.Styles;

/**
 * La classe {@code Tables} est responsable de la création et de la gestion de toutes les tables
 * utilisées dans l'application pour l'affichage des données liées aux câbles et protections.
 * Elle gère également la mise à jour des tables en fonction du nombre d'onduleurs et des entrées 
 * spécifiées dans les calculs de câbles et protections.
 * 
 * Cette classe fournit des méthodes pour initialiser les tables, les mettre à jour, et définir des éditeurs 
 * de cellules personnalisés.
 */
public class Tables {
	private static int nbOnduleurs;
	private static int nbColonnesPourTablesAOnduleurs;
	private static String[] nomsOnduleurs;

	private static int nbEntrees;
	private static int nbColonnesPourTablesAEntrees;
	private static String[] nomsEntrees;
	
	private static JTable tableChoixDeFusible;
	private static JTable tableDesDifferentsCables;
	private static JTable tableL;
	private static JTable tableParafoudreDC;
	private static JTable tableInterrupteursSectionneurs;
	private static JTable tableDisjoncteursACAssociesAuxOnduleurs;
	private static JTable tableDisjoncteurACGeneral;
	private static JTable tableParafoudreAC;
	private static JTable tableDisjoncteurAssocieAuParafoudreAC;
	private static JTable tableNcNpIz;
	private static JTable tableChoixCablesDeChaine;
	private static JTable tableChuteDeTension1;
	private static JTable tableChuteDeTension2;
	private static JTable tableCablePrincipal1;
	private static JTable tableCablePrincipal2;
	private static JTable tableChuteDeTensionMaximumARespecter;
	private static JTable tableChuteDeTensionTotaleDCMaximumARespecter;
	private static JTable tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff;
	private static JTable tableChuteDeTensionEntreDisjoncteurDiffEtAGCP;
	
    /**
     * Constructeur par défaut de la classe {@code Tables}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code Tables}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public Tables() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
	
    /**
     * Retourne la table pour le choix des fusibles.
     * 
     * @return La table {@code JTable} pour le choix des fusibles.
     */
    public static JTable getTableChoixDeFusible() {
        return tableChoixDeFusible;
    }
    
    /**
     * Retourne la table pour les différents câbles.
     * 
     * @return La table {@code JTable} pour les différents câbles.
     */
    public static JTable getTableDesDifferentsCables() {
        return tableDesDifferentsCables;
    }
    
    /**
     * Retourne la table L.
     * 
     * @return La table {@code JTable} L.
     */
    public static JTable getTableL() {
        return tableL;
    }
    
    /**
     * Retourne la table pour les parafoudres DC.
     * 
     * @return La table {@code JTable} pour les parafoudres DC.
     */
    public static JTable getTableParafoudreDC() {
        return tableParafoudreDC;
    }
    
    /**
     * Retourne la table pour les interrupteurs-sectionneurs.
     * 
     * @return La table {@code JTable} pour les interrupteurs-sectionneurs.
     */
    public static JTable getTableInterrupteursSectionneurs() {
        return tableInterrupteursSectionneurs;
    }
    
    /**
     * Retourne la table pour les disjoncteurs AC associés aux onduleurs.
     * 
     * @return La table {@code JTable} pour les disjoncteurs AC associés aux onduleurs.
     */
    public static JTable getTableDisjoncteursACAssociesAuxOnduleurs() {
        return tableDisjoncteursACAssociesAuxOnduleurs;
    }
    
    /**
     * Retourne la table pour le disjoncteur AC général.
     * 
     * @return La table {@code JTable} pour le disjoncteur AC général.
     */
    public static JTable getTableDisjoncteurACGeneral() {
        return tableDisjoncteurACGeneral;
    }
    
    /**
     * Retourne la table pour les parafoudres AC.
     * 
     * @return La table {@code JTable} pour les parafoudres AC.
     */
    public static JTable getTableParafoudreAC() {
        return tableParafoudreAC;
    }
    
    /**
     * Retourne la table pour le disjoncteur associé au parafoudre AC.
     * 
     * @return La table {@code JTable} pour le disjoncteur associé au parafoudre AC.
     */
    public static JTable getTableDisjoncteurAssocieAuParafoudreAC() {
        return tableDisjoncteurAssocieAuParafoudreAC;
    }
    
    /**
     * Retourne la table pour Nc, Np et Iz.
     * 
     * @return La table {@code JTable} pour Nc, Np et Iz.
     */
    public static JTable getTableNcNpIz() {
        return tableNcNpIz;
    }
    
    /**
     * Retourne la table pour le choix des câbles de chaîne.
     * 
     * @return La table {@code JTable} pour le choix des câbles de chaîne.
     */
    public static JTable getTableChoixCablesDeChaine() {
        return tableChoixCablesDeChaine;
    }
    
    /**
     * Retourne la table pour la chute de tension 1.
     * 
     * @return La table {@code JTable} pour la chute de tension 1.
     */
    public static JTable getTableChuteDeTension1() {
        return tableChuteDeTension1;
    }
    
    /**
     * Retourne la table pour la chute de tension 2.
     * 
     * @return La table {@code JTable} pour la chute de tension 2.
     */
    public static JTable getTableChuteDeTension2() {
        return tableChuteDeTension2;
    }
    
    /**
     * Retourne la table pour le câble principal 1.
     * 
     * @return La table {@code JTable} pour le câble principal 1.
     */
    public static JTable getTableCablePrincipal1() {
        return tableCablePrincipal1;
    }
    
    /**
     * Retourne la table pour le câble principal 2.
     * 
     * @return La table {@code JTable} pour le câble principal 2.
     */
    public static JTable getTableCablePrincipal2() {
        return tableCablePrincipal2;
    }
    
    /**
     * Retourne la table pour la chute de tension maximale à respecter.
     * 
     * @return La table {@code JTable} pour la chute de tension maximale à respecter.
     */
    public static JTable getTableChuteDeTensionMaximumARespecter() {
        return tableChuteDeTensionMaximumARespecter;
    }
    
    /**
     * Retourne la table pour la chute de tension totale DC maximale à respecter.
     * 
     * @return La table {@code JTable} pour la chute de tension totale DC maximale à respecter.
     */
    public static JTable getTableChuteDeTensionTotaleDCMaximumARespecter() {
        return tableChuteDeTensionTotaleDCMaximumARespecter;
    }
    
    /**
     * Retourne la table pour la chute de tension entre l'onduleur et le disjoncteur différentiel.
     * 
     * @return La table {@code JTable} pour la chute de tension entre l'onduleur et le disjoncteur différentiel.
     */
    public static JTable getTableChuteDeTensionEntreOnduleurEtDisjoncteurDiff() {
        return tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff;
    }
    
    /**
     * Retourne la table pour la chute de tension entre le disjoncteur différentiel et l'AGCP.
     * 
     * @return La table {@code JTable} pour la chute de tension entre le disjoncteur différentiel et l'AGCP.
     */
    public static JTable getTableChuteDeTensionEntreDisjoncteurDiffEtAGCP() {
        return tableChuteDeTensionEntreDisjoncteurDiffEtAGCP;
    }
    
    /**
     * Initialise toutes les tables nécessaires à l'application.
     * - Crée les tables et configure les renderers et éditeurs de cellules.
     */
    public static void initialiserTables() {
		nbOnduleurs = CalculCablesEtProtections.getNbOnduleurs();
		nbColonnesPourTablesAOnduleurs = nbOnduleurs + 1;
		nomsOnduleurs = CalculCablesEtProtections.getNomsOnduleurs();

		nbEntrees = CalculCablesEtProtections.getNbEntrees();
		nbColonnesPourTablesAEntrees = nbEntrees + 1;
		nomsEntrees = CalculCablesEtProtections.getNomsEntrees();
		
    	createTableChoixDeFusible();
    	createTableDesDifferentsCables();
    	createTableL();
    	createTableParafoudreDC();
    	createTableInterrupteursSectionneurs();
    	createTableDisjoncteursACAssociesAuxOnduleurs();
    	createTableDisjoncteurACGeneral();
    	createTableParafoudreAC();
    	createTableDisjoncteurAssocieAuParafoudreAC();
    	createTableNcNpIz();
    	createTableChoixCablesDeChaine();
    	createTableChuteDeTension1();
    	createTableChuteDeTension2();
    	createTableCablePrincipal1();
    	createTableCablePrincipal2();
    	createTableChuteDeTensionMaximumARespecter();
    	createTableChuteDeTensionTotaleDCMaximumARespecter();
    	createTableChuteDeTensionEntreOnduleurEtDisjoncteurDiff();
    	createTableChuteDeTensionEntreDisjoncteurDiffEtAGCP();
        configureTableRenderers();
        configureTableHeaders();
    }

    /**
     * Configure les renderers pour les tables.
     */
    private static void configureTableRenderers() {
        tableChoixDeFusible.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableDesDifferentsCables.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableL.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableParafoudreDC.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableInterrupteursSectionneurs.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableDisjoncteursACAssociesAuxOnduleurs.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableDisjoncteurACGeneral.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableParafoudreAC.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableDisjoncteurAssocieAuParafoudreAC.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableNcNpIz.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChoixCablesDeChaine.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChuteDeTension1.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChuteDeTension2.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableCablePrincipal1.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableCablePrincipal2.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChuteDeTensionMaximumARespecter.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChuteDeTensionTotaleDCMaximumARespecter.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setDefaultRenderer(Object.class, Styles.tableRenderer());
        tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setDefaultRenderer(Object.class, Styles.tableRenderer());
    }

    /**
     * Configure les en-têtes des tables pour empêcher leur réordonnancement.
     */
    private static void configureTableHeaders() {
        tableChoixDeFusible.getTableHeader().setReorderingAllowed(false);
        tableDesDifferentsCables.getTableHeader().setReorderingAllowed(false);
        tableL.getTableHeader().setReorderingAllowed(false);
        tableParafoudreDC.getTableHeader().setReorderingAllowed(false);
        tableInterrupteursSectionneurs.getTableHeader().setReorderingAllowed(false);
        tableDisjoncteursACAssociesAuxOnduleurs.getTableHeader().setReorderingAllowed(false);
        tableDisjoncteurACGeneral.getTableHeader().setReorderingAllowed(false);
        tableParafoudreAC.getTableHeader().setReorderingAllowed(false);
        tableDisjoncteurAssocieAuParafoudreAC.getTableHeader().setReorderingAllowed(false);
        tableNcNpIz.getTableHeader().setReorderingAllowed(false);
        tableChoixCablesDeChaine.getTableHeader().setReorderingAllowed(false);
        tableChuteDeTension1.getTableHeader().setReorderingAllowed(false);
        tableChuteDeTension2.getTableHeader().setReorderingAllowed(false);
        tableCablePrincipal1.getTableHeader().setReorderingAllowed(false);
        tableCablePrincipal2.getTableHeader().setReorderingAllowed(false);
        tableChuteDeTensionMaximumARespecter.getTableHeader().setReorderingAllowed(false);
        tableChuteDeTensionTotaleDCMaximumARespecter.getTableHeader().setReorderingAllowed(false);
        tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getTableHeader().setReorderingAllowed(false);
        tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getTableHeader().setReorderingAllowed(false);
    }
    
    /**
     * Crée la table pour le choix de fusibles.
     */
    private static void createTableChoixDeFusible() {
        Object[][] donnees = new Object[7][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Nombre de chaines utilisées par MPPT";
        donnees[1][0] = "Calcul de Nc max";
        donnees[2][0] = "Nécessité de fusible ? (OUI / NON)";
        donnees[3][0] = "Calcul de Iscmax (chaîne)";
        donnees[4][0] = "Calcul de Np max";
        donnees[5][0] = "Détermination In";
        donnees[6][0] = "Calibres adaptés";
        
        tableChoixDeFusible = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
    
    /**
     * Crée la table pour les différents câbles.
     */
    private static void createTableDesDifferentsCables() {
    	String[] nomsColonnes = new String[nbColonnesPourTablesAEntrees + 1];
    	nomsColonnes[0] = "Câble";
    	for (int i = 1; i < nbColonnesPourTablesAEntrees + 1; i++) {
    		nomsColonnes[i] = "L" + i;
    	}
        Object[][] donnees = new Object[1][nbColonnesPourTablesAEntrees + 1];
        donnees[0][0] = "Longueur simple (m)";
        
        tableDesDifferentsCables = new JTable(new DefaultTableModel(donnees, nomsColonnes) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
    }
    
    /**
     * Crée la table pour les longueurs.
     */
    private static void createTableL() {
        Object[][] donnees = new Object[1][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "L";
        
        tableL = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
    }
    
    /**
     * Crée la table pour les parafoudres DC.
     */
    private static void createTableParafoudreDC() {
        Object[][] donnees = new Object[8][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Uoc max (V)";
        donnees[1][0] = "0,8 x Uw (V)";
        donnees[2][0] = "Isc max (A)";
        donnees[3][0] = "Modèle parafoudre DC choisi";
        donnees[4][0] = "Ucpv \u2265 Uocmax ?";
        donnees[5][0] = "Up < 0,8 x Uw";
        donnees[6][0] = "In > 5 kA";
        donnees[7][0] = "Iscpv \u2265 Iscmax ?";
        
        tableParafoudreDC = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row > 2;
            }
        });
    }
	
    /**
     * Prototype pour les presets (ne fonctionne pas tout à fait).
     * 
     * @param table la table concernée
     */
    public static void setCustomCellEditor(JTable table) {
        CalculCablesEtProtections.getBDD();
		List<String> parafoudresDC = BDD.getParafoudresDC().getReferences();
        parafoudresDC.add(0, "");
        JComboBox<String> comboBox = new JComboBox<>(parafoudresDC.toArray(new String[0]));

        for (int col = 1; col < table.getColumnCount(); col++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(col);
            tableColumn.setCellEditor(new DefaultCellEditor(new JTextField()) {
                private static final long serialVersionUID = 1L;

                @Override
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    if (row == 4) {
                        comboBox.setSelectedItem(value);
                        return comboBox;
                    } else {
                        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
                    }
                }
            });
        }
    }
    
    /**
     * Crée la table pour les interrupteurs sectionneurs.
     */
    private static void createTableInterrupteursSectionneurs() {
        Object[][] donnees = new Object[5][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Uocmax (V)";
        donnees[1][0] = "Iscmax (A)";
        donnees[2][0] = "Modèle choisi";
        donnees[3][0] = "Ue \u2265 Uocmax ?";
        donnees[4][0] = "In \u2265 Iscmax ?";
        
        tableInterrupteursSectionneurs = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row > 1;
            }
        });
    }
    
    /**
     * Crée la table pour les disjoncteurs AC associés aux onduleurs.
     */
    private static void createTableDisjoncteursACAssociesAuxOnduleurs() {
        Object[][] donnees = new Object[9][nbColonnesPourTablesAOnduleurs];
        donnees[0][0] = "Ue (V)";
        donnees[1][0] = "Ie (A)";
        donnees[2][0] = "Modèle disjoncteur choisi";
        donnees[3][0] = "Modèle protection différentielle choisie";
        donnees[4][0] = "Icu \u2265 3 kA ?";
        donnees[5][0] = "In \u2265 Ie ?";
        donnees[6][0] = "Sensibilité DDR";
        donnees[7][0] = "Courbe (A, B, C, D...)";
        donnees[8][0] = "Type (A, AC...)";
        
        tableDisjoncteursACAssociesAuxOnduleurs = new JTable(new DefaultTableModel(donnees, nomsOnduleurs) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row != 1;
            }
        });
    }
    
    /**
     * Crée la table pour le disjoncteur AC général.
     */
    private static void createTableDisjoncteurACGeneral() {
    	String[] nomsColonnes = {"Ue (V)", "400"};
        Object[][] donnees = new Object[6][2];
        donnees[0][0] = "Ie (A)";
        donnees[1][0] = "Modèle disjoncteur choisi";
        donnees[2][0] = "Icu \u2265 3 kA ?";
        donnees[3][0] = "In \u2265 Ie ?";
        donnees[4][0] = "Courbe (A, B, C, D...)";
        donnees[5][0] = "Type (A, AC...)";
        
        tableDisjoncteurACGeneral = new JTable(new DefaultTableModel(donnees, nomsColonnes) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row != 0;
            }
        });
    }
    
    /**
     * Crée la table pour les parafoudres AC.
     */
    private static void createTableParafoudreAC() {
    	String[] nomsColonnes = {"", ""};
        Object[][] donnees = new Object[4][2];
        donnees[0][0] = "Modèle de parafoudre choisi";
        donnees[1][0] = "Ue (V)";
        donnees[2][0] = "Up (V)";
        donnees[3][0] = "In \u2265 5 kA ?";
        
        tableParafoudreAC = new JTable(new DefaultTableModel(donnees, nomsColonnes) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
    }
    
    /**
     * Crée la table pour le disjoncteur associé au parafoudre AC.
     */
    private static void createTableDisjoncteurAssocieAuParafoudreAC() {
    	String[] nomsColonnes = {"", ""};
        Object[][] donnees = new Object[3][2];
        donnees[0][0] = "Modèle de disjoncteur choisi";
        donnees[1][0] = "Ue (V)";
        donnees[2][0] = "Icu \u2265 5 kA ?";
        
        tableDisjoncteurAssocieAuParafoudreAC = new JTable(new DefaultTableModel(donnees, nomsColonnes) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
    }
    
    /**
     * Crée la table pour les valeurs Nc, Np et Iz.
     */
    private static void createTableNcNpIz() {
        Object[][] donnees = new Object[3][nbColonnesPourTablesAOnduleurs];
        donnees[0][0] = "Nb de chaînes (par MPPT) Nc";
        donnees[1][0] = "Nb de chaînes par dispositif de protection : Np";
        donnees[2][0] = "Iz \u2265 ?";
        
        tableNcNpIz = new JTable(new DefaultTableModel(donnees, nomsOnduleurs) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row != 2;
            }
        });
    }
    
    /**
     * Crée la table pour le choix des câbles de chaîne.
     */
	private static void createTableChoixCablesDeChaine() {
        Object[][] donnees = new Object[8][nbColonnesPourTablesAOnduleurs];
        donnees[0][0] = "Iz \u2265 ?";
        donnees[1][0] = "Iz' = I / K";
        donnees[2][0] = "Iz pour 2,5 mm²";
        donnees[3][0] = "Iz pour 4 mm²";
        donnees[4][0] = "Iz pour 6 mm²";
        donnees[5][0] = "Iz \u2265 Iz' pour 2,5 mm² ?";
        donnees[6][0] = "Iz \u2265 Iz' pour 4 mm² ?";
        donnees[7][0] = "Iz \u2265 Iz' pour 6 mm² ?";
        
        tableChoixCablesDeChaine = new JTable(new DefaultTableModel(donnees, nomsOnduleurs) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && (row == 2 || row == 3 || row == 4);
            }
        });
    }
	
    /**
     * Crée la table 1 pour la chute de tension.
     */
	private static void createTableChuteDeTension1() {
        Object[][] donnees = new Object[2][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Impp (A)";
        donnees[1][0] = "Umpp (V)";
        
        tableChuteDeTension1 = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
	
    /**
     * Crée la table 2 pour la chute de tension.
     */
	private static void createTableChuteDeTension2() {
        Object[][] donnees = new Object[5][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Chaîne la plus longue ?";
        donnees[1][0] = "Umpp associé (cf. tableau ci-dessus)";
        donnees[2][0] = "Rc = 0,023 x 2 x L / S";
        donnees[3][0] = "ΔU = Rc x Impp";
        donnees[4][0] = "Chute de tension (%) = 100 x ΔU / Umpp";
        
        tableChuteDeTension2 = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row < 2;
            }
        });
    }
	
    /**
     * Crée la table 1 pour le câble principal.
     */
	private static void createTableCablePrincipal1() {
        Object[][] donnees = new Object[1][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Iscmax_gen (A)";
        
        tableCablePrincipal1 = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
	
    /**
     * Crée la table 2 pour le câble principal.
     */
	private static void createTableCablePrincipal2() {
        Object[][] donnees = new Object[7][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Iz' = Iscmax_gen / K";
        donnees[1][0] = "Iz pour 2,5 mm²";
        donnees[2][0] = "Iz pour 4 mm²";
        donnees[3][0] = "Iz pour 6 mm²";
        donnees[4][0] = "Iz \u2265 Iz' pour 2,5 mm² ?";
        donnees[5][0] = "Iz \u2265 Iz' pour 4 mm² ?";
        donnees[6][0] = "Iz \u2265 Iz' pour 6 mm² ?";
        
        tableCablePrincipal2 = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && (row == 1 || row == 2 || row == 3);
            }
        });
    }
	
    /**
     * Crée la table pour la chute de tension maximale à respecter.
     */
	private static void createTableChuteDeTensionMaximumARespecter() {
        Object[][] donnees = new Object[5][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Câble entre boîte de jonction et onduleur";
        donnees[1][0] = "Impp_principal";
        donnees[2][0] = "Rc = 0,023 x 2 x L / S";
        donnees[3][0] = "ΔU = Rc x Impp_principal";
        donnees[4][0] = "Chute de tension (%) = 100 x ΔU / Umpp";
        
        tableChuteDeTensionMaximumARespecter = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && row == 0;
            }
        });
    }
	
    /**
     * Crée la table pour la chute de tension totale DC maximale à respecter.
     */
	private static void createTableChuteDeTensionTotaleDCMaximumARespecter() {
        Object[][] donnees = new Object[3][nbColonnesPourTablesAEntrees];
        donnees[0][0] = "Chute de tension chaîne (%)";
        donnees[1][0] = "Chute de tension câble principal (%)";
        donnees[2][0] = "Chute de tension totale (%)";
        
        tableChuteDeTensionTotaleDCMaximumARespecter = new JTable(new DefaultTableModel(donnees, nomsEntrees) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }
	
    /**
     * Crée la table pour la chute de tension entre l'onduleur et le disjoncteur différentiel.
     */
	private static void createTableChuteDeTensionEntreOnduleurEtDisjoncteurDiff() {
        Object[][] donnees = new Object[8][nbColonnesPourTablesAOnduleurs];
        donnees[0][0] = "Coefficient b";
        donnees[1][0] = "Longueur de câble L (m)";
        donnees[2][0] = "Nature des conducteurs";
        donnees[3][0] = "Courant d’emploi Ib (A)";
        donnees[4][0] = "Section câble S (mm²)";
        donnees[5][0] = "U0 (V)";
        donnees[6][0] = "𝛥u (V)";
        donnees[7][0] = "Chute de tension relative (%)";
        tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff = new JTable(new DefaultTableModel(donnees, nomsOnduleurs) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && (row == 0 || row == 1 || row == 2|| row == 4 || row == 5);
            }
        });
    }
	
    /**
     * Crée la table pour la chute de tension entre le disjoncteur différentiel et l'AGCP.
     */
	private static void createTableChuteDeTensionEntreDisjoncteurDiffEtAGCP() {
    	String[] nomsColonnes = {"", ""};
        Object[][] donnees = new Object[8][2];
        donnees[0][0] = "Coefficient b";
        donnees[1][0] = "Longueur de câble L (m)";
        donnees[2][0] = "Nature des conducteurs";
        donnees[3][0] = "Courant d’emploi Ib (A)";
        donnees[4][0] = "Section câble S (mm²)";
        donnees[5][0] = "Tension ph/N U0 (V)";
        donnees[6][0] = "Chute de tension 𝛥u (V)";
        donnees[7][0] = "Chute de tension relative (%)";
        
        tableChuteDeTensionEntreDisjoncteurDiffEtAGCP = new JTable(new DefaultTableModel(donnees, nomsColonnes) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0 && (row == 0 || row == 1 || row == 2|| row == 4 || row == 5);
            }
        });
    }
}
