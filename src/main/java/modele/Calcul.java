package modele;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import vue.PageOnduleur;
import vue.PagePuissance;
import vue.PageSurface;

/**
 * Cette classe gère les calculs nécessaires pour la configuration et l'évaluation des panneaux solaires, 
 * y compris les études de pentes, les onduleurs, et les modules solaires.
 */
public class Calcul {

    private static List<EtudeDUnePente> etudesPentes = new ArrayList<>();
    private static List<EtudeDUnOnduleur> etudesOnduleurs = new ArrayList<>();
    private static EtudeDUnModule etudeModule;
    private static double puissanceMaxGenerateur;
    private static int nbTotalModulesPentesPortrait;
    private static int nbTotalModulesPentesPaysage;
    private static boolean verificationCompatibiliteEnPuissance = false;
    private static boolean presenceBilanPossible = false;
    private static List<EtudeDUnOnduleur> etudesOnduleursConcernesBilan = new ArrayList<>();
    private static List<EtudeDUnOnduleur> anciennesEtudesOnduleursBilan = new ArrayList<>();
    private static List<Integer> choixNbOnduleurs = new ArrayList<>();
    private static List<Integer> anciensChoixNbOnduleurs = new ArrayList<>();
    private static int nbTotalModulesChoisi;
    private static double puissanceTotaleChoisie;

    /**
     * Constructeur par défaut de la classe {@code Calcul}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code Calcul}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public Calcul() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }

    /**
     * Ajoute une étude de pente à la liste des études de pentes.
     * 
     * @param etude l'étude de pente à ajouter
     */
    public static void addEtudePente(EtudeDUnePente etude) {
        etudesPentes.add(etude);
    }

    /**
     * Ajoute une étude d'onduleur à la liste des études d'onduleurs.
     * 
     * @param etude l'étude d'onduleur à ajouter
     */
    public static void addEtudeOnduleur(EtudeDUnOnduleur etude) {
        etudesOnduleurs.add(etude);
    }

    /**
     * Supprime toutes les études de pentes de la liste.
     */
    public static void enleverEtudesPentes() {
        etudesPentes.clear();
    }

    /**
     * Supprime toutes les études d'onduleurs de la liste.
     */
    public static void enleverEtudesOnduleurs() {
        etudesOnduleurs.clear();
    }

    /**
     * Arrondit une valeur à un nombre spécifié de chiffres après la virgule.
     * 
     * @param valeur la valeur à arrondir
     * @param nbChiffresApresLaVirgule le nombre de chiffres après la virgule
     * @return la valeur arrondie
     */
    public static double arrondirDouble(double valeur, int nbChiffresApresLaVirgule) {
        BigDecimal bd = new BigDecimal(valeur).setScale(nbChiffresApresLaVirgule, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Calcule la puissance maximale du générateur en fonction des études de pentes.
     */
    public static void calculerPentes() {
        for (EtudeDUnePente etude : etudesPentes) {
            etude.calculer();
            puissanceMaxGenerateur += Math.max(etude.getNbTotalModulesPortrait(), etude.getNbTotalModulesPaysage()) * etude.getModule().getPnom();
            nbTotalModulesPentesPortrait += etude.getNbTotalModulesPortrait();
            nbTotalModulesPentesPaysage += etude.getNbTotalModulesPaysage();
        }

        puissanceMaxGenerateur /= 1000; // On la met en kWc
        puissanceMaxGenerateur = arrondirDouble(puissanceMaxGenerateur, 4);
    }

    /**
     * Retourne la puissance maximale du générateur calculée.
     * 
     * @return la puissance maximale du générateur
     */
    public static double getPuissanceMaxGenerateur() {
        return puissanceMaxGenerateur;
    }

    /**
     * Retourne le nombre total de modules en orientation portrait.
     * 
     * @return le nombre total de modules en orientation portrait
     */
    public static int getNbTotalModulesPentesPortrait() {
        return nbTotalModulesPentesPortrait;
    }

    /**
     * Retourne le nombre total de modules en orientation paysage.
     * 
     * @return le nombre total de modules en orientation paysage
     */
    public static int getNbTotalModulesPentesPaysage() {
        return nbTotalModulesPentesPaysage;
    }

    /**
     * Détermine la configuration optimale (portrait ou paysage) en fonction du nombre total de modules.
     * 
     * @return "Paysage" si la configuration paysage est optimale, "Portrait" sinon
     */
    public static String getConfigurationOptimale() {
        return nbTotalModulesPentesPortrait < nbTotalModulesPentesPaysage ? "Paysage" : "Portrait";
    }

    /**
     * Retourne le nombre optimal de modules en fonction de la configuration.
     * 
     * @return le nombre optimal de modules
     */
    public static int getNbModulesOptimal() {
        return nbTotalModulesPentesPortrait < nbTotalModulesPentesPaysage ? nbTotalModulesPentesPaysage : nbTotalModulesPentesPortrait;
    }

    /**
     * Crée des études de pentes à partir des données fournies dans un tableau.
     * 
     * @param table La {@code JTable} contenant les données des pentes.
     */
    public static void creerEtudesPentes(JTable table) {
        int columnCount = table.getColumnCount();
        
        for (int j = 1; j < columnCount; j++) {
            try {
                Module module = new Module(
                    validateAndConvertToString(table.getValueAt(2, j), "Module", j),
                    validateAndConvertToDouble(table.getValueAt(3, j), "Longueur module (m)", j),
                    validateAndConvertToDouble(table.getValueAt(4, j), "Largeur module (m)", j),
                    validateAndConvertToDouble(table.getValueAt(5, j), "Pnom module (Wc)", j)
                );

                EFU efu = new EFU(
                    validateAndConvertToString(table.getValueAt(6, j), "Élément de fixation", j),
                    validateAndConvertToDouble(table.getValueAt(7, j), "Écart minimum imposé sens gouttière (m)", j),
                    validateAndConvertToDouble(table.getValueAt(8, j), "Écart minimum imposé sens rampant (m)", j)
                );

                Pente pente = new Pente(
                    validateAndConvertToDouble(table.getValueAt(0, j), "Dimension gouttière pente (m)", j),
                    validateAndConvertToDouble(table.getValueAt(1, j), "Dimension rampant pente (m)", j)
                );

                EtudeDUnePente etude = new EtudeDUnePente(
                    module, pente, efu, 
                    validateAndConvertToDouble(table.getValueAt(9, j), "Débord minimum sens gouttière", j),
                    validateAndConvertToDouble(table.getValueAt(10, j), "Débord minimum sens Rampant", j)
                );

                addEtudePente(etude);
            } catch (NullDataException | InvalidFormatException e) {
                showErrorDialog(e.getMessage());
            }
        }
    }

    /**
     * Valide et convertit une donnée en chaîne de caractères.
     * 
     * @param data la donnée à convertir
     * @param fieldName le nom du champ pour l'affichage d'erreur
     * @param index l'index de la donnée pour l'affichage d'erreur
     * @return la donnée convertie en chaîne de caractères
     * @throws NullDataException si la donnée est nulle
     */
    private static String validateAndConvertToString(Object data, String fieldName, int index) throws NullDataException {
        if (data == null) {
            throw new NullDataException("Null data at field: " + fieldName + ", index: " + index);
        }
        return data.toString();
    }

    /**
     * Valide et convertit une donnée en double.
     * 
     * @param data la donnée à convertir
     * @param fieldName le nom du champ pour l'affichage d'erreur
     * @param index l'index de la donnée pour l'affichage d'erreur
     * @return la donnée convertie en double
     * @throws NullDataException si la donnée est nulle
     * @throws InvalidFormatException si la donnée n'est pas dans un format valide
     */
    private static double validateAndConvertToDouble(Object data, String fieldName, int index) throws NullDataException, InvalidFormatException {
        if (data == null) {
            throw new NullDataException("Null data at field: " + fieldName + ", index: " + index);
        }
        try {
            return Double.parseDouble(data.toString());
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid format for field: " + fieldName + ", index: " + index);
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur avec le message spécifié.
     * 
     * @param message le message d'erreur à afficher
     */
    private static void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Actualise les listes des études d'onduleurs concernés et des choix de nombres d'onduleurs à partir de la table d'onduleurs.
     */
    public static void actualiserLes2ListesEtudesEtChoix() {
        etudesOnduleursConcernesBilan.clear();
        choixNbOnduleurs.clear();
        JTable tableADecortiquer = PageOnduleur.getTableOnduleur();
        for (int j = 1; j < tableADecortiquer.getColumnCount(); j++) {
            Onduleur onduleur;
            if (tableADecortiquer.getValueAt(4, j).toString().contains("/")) {
                onduleur = new Onduleur(
                    tableADecortiquer.getValueAt(0, j).toString(),
                    Double.parseDouble(tableADecortiquer.getValueAt(1, j).toString()),
                    Double.parseDouble(tableADecortiquer.getValueAt(2, j).toString()),
                    tableADecortiquer.getValueAt(3, j).toString(),
                    tableADecortiquer.getValueAt(4, j).toString(),
                    tableADecortiquer.getValueAt(5, j).toString(),
                    Integer.parseInt(tableADecortiquer.getValueAt(6, j).toString()),
                    tableADecortiquer.getValueAt(7, j).toString(),
                    Double.parseDouble(tableADecortiquer.getValueAt(8, j).toString()),
                    Double.parseDouble(tableADecortiquer.getValueAt(9, j).toString())
                );
            } else {
                onduleur = new Onduleur(
                    tableADecortiquer.getValueAt(0, j).toString(),
                    Double.parseDouble(tableADecortiquer.getValueAt(1, j).toString()),
                    Double.parseDouble(tableADecortiquer.getValueAt(2, j).toString()),
                    tableADecortiquer.getValueAt(3, j).toString(),
                    Double.parseDouble(tableADecortiquer.getValueAt(4, j).toString()),
                    Double.parseDouble(tableADecortiquer.getValueAt(5, j).toString()),
                    Integer.parseInt(tableADecortiquer.getValueAt(6, j).toString()),
                    Integer.parseInt(tableADecortiquer.getValueAt(7, j).toString()),
                    Double.parseDouble(tableADecortiquer.getValueAt(8, j).toString()),
                    Double.parseDouble(tableADecortiquer.getValueAt(9, j).toString())
                );
            }
            EtudeDUnOnduleur etude = new EtudeDUnOnduleur(onduleur, etudeModule.getModule());
            etudesOnduleursConcernesBilan.add(etude);
            if (tableADecortiquer.getValueAt(14, j) == null || tableADecortiquer.getValueAt(14, j).toString().equals("")) {
                choixNbOnduleurs.add(-1);
            } else {
                choixNbOnduleurs.add(Integer.parseInt(tableADecortiquer.getValueAt(14, j).toString()));
            }
        }
    }

    /**
     * Conserve les anciennes listes d'études d'onduleurs et de choix de nombres d'onduleurs pour comparaison future.
     */
    public static void conserverLes2ListesAnciennesEtudesEtChoix() {
        anciennesEtudesOnduleursBilan.clear();
        anciensChoixNbOnduleurs.clear();
        for (int i = 0; i < etudesOnduleursConcernesBilan.size(); i++) {
            anciennesEtudesOnduleursBilan.add(etudesOnduleursConcernesBilan.get(i));
            anciensChoixNbOnduleurs.add(choixNbOnduleurs.get(i));
        }
    }

    /**
     * Crée les études d'onduleurs à partir des données d'une table.
     *
     * @param table la table contenant les données des onduleurs
     */
    public static void creerEtudesOnduleurs(JTable table) {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        Object[][] donnees = new Object[rowCount][columnCount];
        
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount - 1; j++) {
                donnees[i][j] = table.getValueAt(i, j + 1);
            }
        }
        
        for (int j = 0; j < donnees[0].length - 1; j++) {
            Onduleur onduleur;
            if (donnees[4][j].toString().contains("/")) {
                onduleur = new Onduleur(
                    donnees[0][j].toString(),
                    Double.parseDouble(donnees[1][j].toString()),
                    Double.parseDouble(donnees[2][j].toString()),
                    donnees[3][j].toString(),
                    donnees[4][j].toString(),
                    donnees[5][j].toString(),
                    Integer.parseInt(donnees[6][j].toString()),
                    donnees[7][j].toString(),
                    Double.parseDouble(donnees[8][j].toString()),
                    Double.parseDouble(donnees[9][j].toString())
                );
            } else {
                onduleur = new Onduleur(
                    donnees[0][j].toString(),
                    Double.parseDouble(donnees[1][j].toString()),
                    Double.parseDouble(donnees[2][j].toString()),
                    donnees[3][j].toString(),
                    Double.parseDouble(donnees[4][j].toString()),
                    Double.parseDouble(donnees[5][j].toString()),
                    Integer.parseInt(donnees[6][j].toString()),
                    Integer.parseInt(donnees[7][j].toString()),
                    Double.parseDouble(donnees[8][j].toString()),
                    Double.parseDouble(donnees[9][j].toString())
                );
            }
            EtudeDUnOnduleur etude = new EtudeDUnOnduleur(onduleur, etudeModule.getModule());
            if (donnees[12][j] == null || donnees[12][j].toString().equals("")) {
                table.setValueAt("", 13, j + 1);
                verificationCompatibiliteEnPuissance = false;
            } else {
                etude.setPuissance(donnees[12][j].toString());
                verificationCompatibiliteEnPuissance = true;
            }
            addEtudeOnduleur(etude);
        }
    }

    /**
     * Calcule les bilans pour les onduleurs concernés et met à jour les tables de bilan.
     *
     * @param onduleursConcernes la liste des études d'onduleurs concernées
     */
    public static void calculerTablesBilan(List<EtudeDUnOnduleur> onduleursConcernes) {
        List<JTable> tablesBilan = PageOnduleur.getTablesBilan();
        if (tablesBilan.isEmpty()) {
            return;
        }
        for (int k = 0; k < tablesBilan.size(); k++) {
            JTable table = tablesBilan.get(k);
            for (int i = 1; i < table.getColumnCount(); i++) {
                EtudeDUnOnduleur etude = onduleursConcernes.get(i - 1);
                if (table.getValueAt(0, i) == null || table.getValueAt(0, i).toString().equals("") || table.getValueAt(1, i) == null || table.getValueAt(1, i).toString().equals("")) {
                    return;
                }
                int nbModules = Integer.parseInt(table.getValueAt(0, i).toString()) * Integer.parseInt(table.getValueAt(1, i).toString());
                table.setValueAt(nbModules, 2, i);
                nbTotalModulesChoisi += nbModules;
                double puissanceTotale = Double.parseDouble(table.getValueAt(2, i).toString()) * etude.getModuleAssocie().getPmaxTSTC() / 1000;
                table.setValueAt(puissanceTotale, 3, i);
                puissanceTotaleChoisie += puissanceTotale;
            }
        }
        PageOnduleur.initialiserTableComparatifAvecSurface();
    }

    /**
     * Vérifie si une présence de bilan est possible.
     *
     * @return true si la présence de bilan est possible, sinon false
     */
    public static boolean isPresenceBilanPossible() {
        return presenceBilanPossible;
    }

    /**
     * Obtient le nombre total de modules choisis.
     *
     * @return le nombre total de modules choisis
     */
    public static int getNbTotalModulesChoisi() {
        return nbTotalModulesChoisi;
    }

    /**
     * Obtient la puissance totale choisie.
     *
     * @return la puissance totale choisie
     */
    public static double getPuissanceTotaleChoisie() {
        return puissanceTotaleChoisie;
    }

    /**
     * Obtient les études de pentes.
     *
     * @return la liste des études de pentes
     */
    public static List<EtudeDUnePente> getEtudesPentes() {
        return etudesPentes;
    }

    /**
     * Obtient les études d'onduleurs.
     *
     * @return la liste des études d'onduleurs
     */
    public static List<EtudeDUnOnduleur> getEtudesOnduleurs() {
        return etudesOnduleurs;
    }

    /**
     * Vérifie la compatibilité en puissance.
     *
     * @return true si la compatibilité en puissance est vérifiée, sinon false
     */
    public static boolean getVerificationCompatibiliteEnPuissance() {
        return verificationCompatibiliteEnPuissance;
    }

    /**
     * Obtient les études d'onduleurs concernés par le bilan.
     *
     * @return la liste des études d'onduleurs concernés par le bilan
     */
    public static List<EtudeDUnOnduleur> getEtudesOnduleursConcernesBilan() {
        return etudesOnduleursConcernesBilan;
    }

    /**
     * Vérifie si les onduleurs du bilan ont changé par rapport aux anciennes études.
     *
     * @return true si les onduleurs du bilan ont changé, sinon false
     */
    public static boolean lesOnduleursDuBilanOntChange() {
        if (etudesOnduleursConcernesBilan.size() != anciennesEtudesOnduleursBilan.size()) {
            return true;
        } else {
            if (!etudesOnduleursConcernesBilan.isEmpty()) {
                for (int i = 0; i < etudesOnduleursConcernesBilan.size(); i++) {
                    if (!etudesOnduleursConcernesBilan.get(i).getOnduleur().getNom().equals(anciennesEtudesOnduleursBilan.get(i).getOnduleur().getNom())) {
                        return true;
                    }
                }
            }
            if (!choixNbOnduleurs.isEmpty()) {
                for (int i = 0; i < choixNbOnduleurs.size(); i++) {
                    if (!choixNbOnduleurs.get(i).equals(anciensChoixNbOnduleurs.get(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Effectue tous les calculs pour la surface et met à jour la table des données.
     */
    public static void toutCalculerSurface() {
        PageSurface.enleverTables();
        nbTotalModulesChoisi = 0;
        puissanceTotaleChoisie = 0;
        puissanceMaxGenerateur = 0;
        nbTotalModulesPentesPortrait = 0;
        nbTotalModulesPentesPaysage = 0;
        creerEtudesPentes(PageSurface.getTableSurface());
        calculerPentes();
        PageSurface.updateTableData(Calcul.getEtudesPentes());
        enleverEtudesPentes();
    }

    /**
     * Effectue tous les calculs pour la puissance et met à jour les tables de puissance.
     */
    public static void toutCalculerPuissance() {
        try {
            Module module = new Module(
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(0, 1).toString(), "T (°C)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance2().getValueAt(0, 1).toString(), "CT PMPP (%/K)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance2().getValueAt(1, 1).toString(), "CT Uoc (%/K)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance2().getValueAt(2, 1).toString(), "CT Isc (%/K)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(1, 1).toString(), "Pmax (Wc)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(2, 1).toString(), "Uoc (V)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(3, 1).toString(), "Umpp (V)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(4, 1).toString(), "Isc (A)", 0),
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(5, 1).toString(), "Impp (A)", 0)
            );
            
            EtudeDUnModule etude = new EtudeDUnModule(
                module,
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(0, 2).toString(), "T (°C)", 1),
                validateAndConvertToDouble(PagePuissance.getTablePuissance1().getValueAt(0, 3).toString(), "T (°C)", 2)
            );
            
            etudeModule = etude;
            etudeModule.calculer();
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(10), 2), 1, 2);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(16), 2), 1, 3);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(11), 2), 2, 2);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(17), 2), 2, 3);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(12), 2), 3, 2);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(18), 2), 3, 3);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(13), 2), 4, 2);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(19), 2), 4, 3);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(14), 2), 5, 2);
            PagePuissance.getTablePuissance1().setValueAt(arrondirDouble(etudeModule.getAttributs().get(20), 2), 5, 3);
            PagePuissance.getTablePuissance3().setValueAt(arrondirDouble(etudeModule.getAttributs().get(21), 4), 0, 0);
        } catch (NullDataException | InvalidFormatException e) {
            showErrorDialog(e.getMessage());
        }
    }

    /**
     * Effectue tous les calculs pour les onduleurs et met à jour les tables correspondantes.
     */
    public static void toutCalculerOnduleurs() {
        PageOnduleur.detruireTableComparatif();
        PageOnduleur.enleverTablesAdditionnelles();
        actualiserLes2ListesEtudesEtChoix();
        JTable tableOnduleur = PageOnduleur.getTableOnduleur();
        creerEtudesOnduleurs(tableOnduleur);
        int j = 1;
        for (EtudeDUnOnduleur etude : etudesOnduleurs) {
            tableOnduleur.setValueAt(etude.getNbModulesMax(), 10, j);
            tableOnduleur.setValueAt(etude.getPlageDePuissanceAdmissibleAuxEntrees(), 11, j);
            if (verificationCompatibiliteEnPuissance) {
                tableOnduleur.setValueAt(etude.conclusionPuissanceDemandee(), 13, j);
            } else {
                tableOnduleur.setValueAt("", 13, j);
            }
            j++;
        }
        
        for (int i = 0; i < etudesOnduleurs.size(); i++) {
            PageOnduleur.ajouterTableSupplementaire(etudesOnduleurs.get(i));
        }
        
        if (lesOnduleursDuBilanOntChange()) {
            PageOnduleur.detruireTableComparatif();
            PageOnduleur.enleverTablesBilan();
            boolean peutMettreLesTablesBilan = true;
            for (int i = 1; i < tableOnduleur.getColumnCount(); i++) {
                if (tableOnduleur.getValueAt(14, i) == null || tableOnduleur.getValueAt(14, i).toString().equals("")) {
                    peutMettreLesTablesBilan = false;
                }
            }
            if (peutMettreLesTablesBilan) {
                PageOnduleur.ajouterTablesBilan();
            }
        }
        calculerTablesBilan(PageOnduleur.getEtudesConcerneesParChaqueTableDuBilan());
        conserverLes2ListesAnciennesEtudesEtChoix();
        enleverEtudesOnduleurs();
    }
}
