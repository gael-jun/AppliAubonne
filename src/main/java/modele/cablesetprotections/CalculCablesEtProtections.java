package modele.cablesetprotections;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;

import modele.EtudeDUnOnduleur;
import vue.PagePuissance;
import vue.cablesetprotections.Tables;
import vue.cablesetprotections.ZonesDeTexte;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableChuteDeTensionTotaleDCMaximumARespecter;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableDisjoncteurACGeneral;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableDisjoncteurAssocieAuParafoudreAC;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableDisjoncteursACAssociesAuxOnduleurs;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableInterrupteursSectionneurs;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableParafoudreAC;
import vue.cablesetprotections.vue.cablesetprotections.customrenderers.CustomRendererTableParafoudreDC;

/**
 * La classe {@code CalculCablesEtProtections} effectue les calculs nécessaires pour déterminer
 * les caractéristiques des câbles et des protections dans un système photovoltaïque.
 * <p>
 * Elle gère les paramètres des onduleurs, des câbles, des protections et des calculs associés. 
 * Les variables statiques stockent les données par défaut et les résultats intermédiaires des calculs.
 * </p>
 */
public class CalculCablesEtProtections {

	private static List<String> nomsOnduleurs;
	private static List<String> nomsEntreesOnduleurs;
	private static List<Integer> nbModulesParChaineParEntree;
	private static List<Integer> nbChainesChoisi;
	private static List<Double> IACmaxOnduleurs;
	private static List<String> IscmaxOuI2PourChaqueOnduleur = new ArrayList<>();
	private static BDD bdd = new BDD();
	private static int nbOnduleurs = 0;
	private static int nbEntrees = 0;
	private static double vide = -1.0;
	private static double Ki = 1.25;
	private static Fusible choixFusible;
	private static double choixSectionCablesDeChaine;
	private static double choixSectionCablePrincipal;
	private static double Irm;
	private static double rappelUocTmin;
	private static double rappelIscSTC;
	private static double rappelUmppSTC;
	private static double rappelImppSTC;
	private static String protectionParafoudreObligatoireDC;
	private static double NkDC;
	private static double NgDC;
	private static double constantePourLcrit;
	private static double Lcrit;
	private static double Ltotale;
	private static double Uw;
	private static String secondParafoudreDC;
	private static double NkAC;
	private static double NgAC;
	private static String protectionParafoudreObligatoireAC;
	private static String lettreSelectionCablesChaine;
	private static double K1CablesChaine;
	private static double K2CablesChaine;
	private static double K3CablesChaine;
	private static double KCablesChaine;
	private static String lettreSelectionCablePrincipal;
	private static double K1CablePrincipal;
	private static double K2CablePrincipal;
	private static double K3CablePrincipal;
	private static double KCablePrincipal;
	private static String chuteTensionAC;
	private static double doubleChuteTensionAC1;
	private static double doubleChuteTensionAC2;
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
     * Constructeur par défaut de la classe {@code CalculCablesEtProtections}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code CalculCablesEtProtections}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public CalculCablesEtProtections() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
    
    /**
     * Initialise les listes statiques qui stockent les informations sur les onduleurs
     * et les paramètres associés.

     */
    private static void init() {
    	nomsOnduleurs = new ArrayList<>();
    	nomsEntreesOnduleurs = new ArrayList<>();
    	nbModulesParChaineParEntree = new ArrayList<>();
    	nbChainesChoisi = new ArrayList<>();
    	IACmaxOnduleurs = new ArrayList<>();
    }
    
    /**
     * Met à jour les informations sur les onduleurs en fonction des données fournies.
     * <p>
     * Cette méthode parcourt une liste de tableaux de bilan et met à jour les listes statiques
     * avec les informations correspondantes, telles que les noms des onduleurs, les courants maximaux,
     * le nombre de modules par chaîne, et le nombre de chaînes choisies.
     * </p>
     *
     * @param tablesBilan la liste des tableaux du bilan de la partie Onduleur
     * @param etudesConcernees la liste des études liées aux onduleurs pour lesquelles les calculs sont effectués
     */
    public static void MAJOnduleurs(List<JTable> tablesBilan, List<EtudeDUnOnduleur> etudesConcernees) {
    	init();
    	for (int i = 0; i < tablesBilan.size(); i++) {
    		JTable table = tablesBilan.get(i);
    		nomsOnduleurs.add(table.getColumnName(0));
    		IACmaxOnduleurs.add(etudesConcernees.get(i).getOnduleur().getIACmax());
    		for (int col = 1; col < table.getColumnCount(); col ++) {
    			nomsEntreesOnduleurs.add(table.getColumnName(col) + " (" + nomsOnduleurs.get(i) + ")");
    			nbModulesParChaineParEntree.add(Integer.parseInt(table.getValueAt(0, col).toString()));
        		nbChainesChoisi.add(Integer.parseInt(table.getValueAt(1, col).toString()));
    		}
    	}
		nbOnduleurs = nomsOnduleurs.size();
		nbEntrees = nomsEntreesOnduleurs.size();
    }
	
	/**
	 * Arrondit une valeur en double à un nombre spécifié de chiffres après la virgule.
	 * <p>
	 * Cette méthode utilise la classe {@code BigDecimal} pour effectuer un arrondi avec une précision
	 * spécifiée. Le mode d'arrondi utilisé est {@code RoundingMode.HALF_UP}, ce qui signifie que les
	 * valeurs sont arrondies au chiffre le plus proche, avec un arrondi au chiffre supérieur en cas
	 * d'égalité.
	 * </p>
	 *
	 * @param valeur La valeur en double à arrondir.
	 * @param nbChiffresApresLaVirgule Le nombre de chiffres à conserver après la virgule.
	 * @return La valeur arrondie à {@code nbChiffresApresLaVirgule} chiffres après la virgule.
	 * @throws IllegalArgumentException Si {@code nbChiffresApresLaVirgule} est négatif.
	 */
	public static double arrondirDouble(double valeur, int nbChiffresApresLaVirgule) {
	    if (nbChiffresApresLaVirgule < 0) {
	        throw new IllegalArgumentException("Le nombre de chiffres après la virgule ne peut pas être négatif.");
	    }
	    BigDecimal bd = new BigDecimal(valeur).setScale(nbChiffresApresLaVirgule, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * Met à jour toutes les informations liées aux onduleurs, câbles, et protections.
	 * Cette méthode effectue les opérations suivantes :
	 * <ul>
	 *   <li>Réinitialise la liste des valeurs de courant de court-circuit.</li>
	 *   <li>Récupère les valeurs des champs de texte et les données des tables.</li>
	 *   <li>Effectue les calculs nécessaires.</li>
	 *   <li>Met à jour les champs de texte avec les résultats calculés.</li>
	 * </ul>
	 */
	public static void toutMettreAJour() {
		IscmaxOuI2PourChaqueOnduleur.clear();
		recupererLesValeursDeTousLesTextField();
		recupererToutesLesTables();
		toutCalculer();
		remettreLesValeursDeTousLesTexteField();
	}
	
	/**
	 * Met à jour toutes les informations liées aux onduleurs, câbles, et protections.
	 * Cette méthode effectue les opérations suivantes :
	 * <ul>
	 *   <li>Réinitialise la liste des valeurs de courant de court-circuit.</li>
	 *   <li>Récupère les valeurs des champs de texte et les données des tables.</li>
	 *   <li>Effectue les calculs nécessaires.</li>
	 *   <li>Met à jour les champs de texte avec les résultats calculés.</li>
	 * </ul>
	 */
	private static void toutCalculer() {
		calculerRappelUocTmin();
		calculerRappelIscSTC();
		calculerRappelUmppSTC();
		calculerRappelImppSTC();
		MAJtableChoixDeFusibles();
		calculerNkDC();
		calculerNgDC();
		calculerLcrit();
		calculerLtotale();
		MAJtableParafoudreDC();
		calculerProtectionParafoudreObligatoireDC();
		MAJtableInterrupteursSectionneurs();
		MAJtableDisjoncteursACAssociesAuxOnduleurs();
		MAJtableDisjoncteurACGeneral();
		MAJtableParafoudreAC();
		MAJtableDisjoncteurAssocieAuParafoudreAC();
		calculerNkAC();
		calculerNgAC();
		calculerProtectionParafoudreObligatoireAC();
		MAJtableNcNpIz();
		calculerKCablesChaine();
		MAJtableChoixCablesDeChaine();
		MAJtableChuteDeTension1();
		MAJtableChuteDeTension2();
		MAJtableCablePrincipal1();
		calculerKCablePrincipal();
		MAJtableCablePrincipal2();
		MAJtableChuteDeTensionMaximumARespecter();
		MAJtableChuteDeTensionTotaleDCMaximumARespecter();
		MAJtableChuteDeTensionEntreOnduleurEtDisjoncteurDiff();
		MAJtableChuteDeTensionEntreDisjoncteurDiffEtAGCP();
		calculerChuteTensionAC();
	}
	
	/**
	 * Définit le fusible sélectionné pour le calcul.
	 *
	 * @param fusible Le fusible à utiliser dans les calculs et évaluations.
	 */
	public static void setFusible(Fusible fusible) {
	    choixFusible = fusible;
	}

	/**
	 * Définit la section des câbles de chaîne.
	 *
	 * @param section La section du câble de chaîne en millimètres carrés (mm²).
	 */
	public static void setSectionCablesDeChaine(double section) {
	    choixSectionCablesDeChaine = section;
	}

	/**
	 * Définit la section du câble principal.
	 *
	 * @param section La section du câble principal en millimètres carrés (mm²).
	 */
	public static void setSectionCablePrincipal(double section) {
	    choixSectionCablePrincipal = section;
	}
	
	/**
	 * Calcule la valeur de rappel de la tension de circuit ouvert minimale (Uoc Tmin) à partir des données du module.
	 * La valeur est lue depuis la table de données et affectée à la variable {@code rappelUocTmin} si elle n'est pas déjà définie.
	 * La valeur par défaut est {@code -1.0}.
	 */
	public static void calculerRappelUocTmin() {
	    if (!(PagePuissance.getTablePuissance1().getValueAt(2, 2) == null || PagePuissance.getTablePuissance1().getValueAt(2, 2).toString().equals(""))) {
	        if (rappelUocTmin == vide) {
	            rappelUocTmin = Double.parseDouble(PagePuissance.getTablePuissance1().getValueAt(2, 2).toString());
	        }
	    }
	}

	/**
	 * Calcule la valeur de rappel de la courant de court-circuit à température de fonctionnement standard (Isc STC) à partir des données du module.
	 * La valeur est lue depuis la table de données et affectée à la variable {@code rappelIscSTC} si elle n'est pas déjà définie.
	 * La valeur par défaut est {@code -1.0}.
	 */
	public static void calculerRappelIscSTC() {
	    if (!(PagePuissance.getTablePuissance1().getValueAt(4, 1) == null || PagePuissance.getTablePuissance1().getValueAt(4, 1).toString().equals(""))) {
	        if (rappelIscSTC == vide) {
	            rappelIscSTC = Double.parseDouble(PagePuissance.getTablePuissance1().getValueAt(4, 1).toString());
	        }
	    }
	}

	/**
	 * Calcule la valeur de rappel de la tension à puissance maximale (Umpp STC) à partir des données du module.
	 * La valeur est lue depuis la table de données et affectée à la variable {@code rappelUmppSTC} si elle n'est pas déjà définie.
	 * La valeur par défaut est {@code -1.0}.
	 */
	public static void calculerRappelUmppSTC() {
	    if (!(PagePuissance.getTablePuissance1().getValueAt(3, 1) == null || PagePuissance.getTablePuissance1().getValueAt(3, 1).toString().equals(""))) {
	        if (rappelUmppSTC == vide) {
	            rappelUmppSTC = Double.parseDouble(PagePuissance.getTablePuissance1().getValueAt(3, 1).toString());
	        }
	    }
	}
	
	/**
	 * Calcule la valeur de rappel du courant à puissance maximale (Impp STC) à partir des données du module.
	 * La valeur est lue depuis la table de données et affectée à la variable {@code rappelImppSTC} si elle n'est pas déjà définie.
	 * La valeur par défaut est {@code -1.0}.
	 */
	public static void calculerRappelImppSTC() {
		if (!(PagePuissance.getTablePuissance1().getValueAt(5, 1) == null || PagePuissance.getTablePuissance1().getValueAt(5, 1).toString().equals(""))) {
			if (rappelImppSTC == vide) {
				rappelImppSTC = Double.parseDouble(PagePuissance.getTablePuissance1().getValueAt(5, 1).toString());
			}
		}
	}
	
	/**
	 * Met à jour les données dans la table de choix de fusibles basée sur les informations disponibles.
	 * Cette méthode remplit la table avec des valeurs calculées pour chaque entrée :
	 * <ul>
	 *   <li>Nombre de chaînes utilisées par MPPT</li>
	 *   <li>Necessité de fusible</li>
	 *   <li>Courant de court-circuit maximal</li>
	 *   <li>Nombre maximum de chaînes par dispositif de protection</li>
	 *   <li>Plage de courant admissible pour les fusibles</li>
	 *   <li>Liste des calibres de fusibles adaptés</li>
	 * </ul>
	 * Les valeurs sont calculées en fonction des paramètres tels que {@code Irm}, {@code rappelIscSTC}, et d'autres variables.
	 */
	public static void MAJtableChoixDeFusibles() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			if (!nomsEntreesOnduleurs.isEmpty()) {
				tableChoixDeFusible.setValueAt(nbChainesChoisi.get(col - 1), 0, col);
			}
			if (Irm != vide && rappelIscSTC != vide) {
				double NcmaxBorneSup = arrondirDouble(1 + Irm / rappelIscSTC, 2);
				int nbChainesMaxEnParalleleSansProtection = (int) Math.floor(NcmaxBorneSup);
				tableChoixDeFusible.setValueAt("Ncmax \u2264 " + NcmaxBorneSup + " --> " + 
				nbChainesMaxEnParalleleSansProtection + " chaîne(s) max en // sans protection", 1, col);
				
				if (tableChoixDeFusible.getValueAt(0, col) != null && !tableChoixDeFusible.getValueAt(0, col).toString().equals("")) {
					String necessiteDeFusible = "NON";
					if (Integer.parseInt(tableChoixDeFusible.getValueAt(0, col).toString()) > 2) {
						necessiteDeFusible = "OUI";
						tableChoixDeFusible.setValueAt(necessiteDeFusible, 2, col);
					}
					else {
						tableChoixDeFusible.setValueAt(necessiteDeFusible, 2, col);
					}
					
					if (necessiteDeFusible.equals("OUI")) {
						double Iscmax = arrondirDouble(Ki * rappelIscSTC, 2);
						double NpmaxBorneSup = arrondirDouble(0.5 * (1 + Irm / Iscmax), 2);
						Fusibles fusibles = BDD.getFusibles();
						int nbChainesParDispositifDeProtection = (int) Math.floor(NpmaxBorneSup);
						tableChoixDeFusible.setValueAt(Iscmax, 3, col);
						tableChoixDeFusible.setValueAt("Npmax \u2264 " + NpmaxBorneSup + " --> " + 
						nbChainesParDispositifDeProtection + " chaîne(s) max par dispositif de protection", 4, col);
						tableChoixDeFusible.setValueAt(arrondirDouble(1.1 * Iscmax, 2) + " \u2264 In \u2264 " + Irm, 5, col);
						
						List<Double> calibresAdaptes = new ArrayList<>();
					   
						for (Element e : fusibles.getElements()) {
							Fusible fusible = (Fusible) e;
					        double calibre = fusible.getIn();
					        if (calibre >= 1.1 * Iscmax && calibre <= Irm) {
					            calibresAdaptes.add(calibre);
					        }
					    }
						
						tableChoixDeFusible.setValueAt("Les calibres " + calibresAdaptes.toString() + " A sont adaptés", 6, col);
					}
					else {
						tableChoixDeFusible.setValueAt("sans objet", 3, col);
						tableChoixDeFusible.setValueAt("sans objet", 4, col);
						tableChoixDeFusible.setValueAt("sans objet", 5, col);
						tableChoixDeFusible.setValueAt("sans objet", 6, col);
					}
				}
				else {
					tableChoixDeFusible.setValueAt("", 0, col);
				}
			}
			else {
				tableChoixDeFusible.setValueAt("", 1, col);
				tableChoixDeFusible.setValueAt("", 2, col);
				tableChoixDeFusible.setValueAt("", 3, col);
				tableChoixDeFusible.setValueAt("", 4, col);
				tableChoixDeFusible.setValueAt("", 5, col);
				tableChoixDeFusible.setValueAt("", 6, col);
			}
		}
	}
	
	/**
	 * Calcule la somme totale des longueurs à partir des valeurs présentes dans la table des longueurs {@code tableL}.
	 * Cette méthode itère sur les colonnes de la table pour obtenir les valeurs de longueur, les convertit en double, 
	 * et les additionne pour obtenir la longueur totale. 
	 * Si une valeur est invalide ou manquante, le calcul s'arrête immédiatement.
	 * La longueur totale calculée est stockée dans la variable {@code Ltotale}.
	 */
	public static void calculerLtotale() {
	    double somme = 0;
	    for (int col = 1; col < tableL.getColumnCount(); col++) {
	        Object value = tableL.getValueAt(0, col);
	        try {
	            if (value == null) {
	                return;
	            }
	            double doubleValue = calculerLCellule(value.toString());
	            somme += doubleValue;
	        } catch (NumberFormatException e) {
	            return;
	        }
	    }
	    Ltotale = somme;
	}
	
	/**
	 * Calcule la longueur critique (Lcrit) en fonction des variables {@code NgDC}, {@code NkDC}, et {@code constantePourLcrit}.
	 * La méthode ajuste le calcul en fonction des valeurs disponibles :
	 * <ul>
	 *   <li>Si {@code NgDC} et {@code constantePourLcrit} sont définis, Lcrit est calculé comme {@code constantePourLcrit / NgDC}.</li>
	 *   <li>Si {@code NgDC} n'est pas défini mais {@code NkDC} et {@code constantePourLcrit} le sont, Lcrit est calculé comme {@code constantePourLcrit / (NkDC / 10)}.</li>
	 * </ul>
	 * La longueur critique calculée est stockée dans la variable {@code Lcrit}.
	 */
	public static void calculerLcrit() {
		if (NgDC != vide && constantePourLcrit != vide) {
			Lcrit = constantePourLcrit / NgDC;
		}
		else if (NkDC != vide && constantePourLcrit != vide) {
			Lcrit = constantePourLcrit / (NkDC / 10);
		}
	}
	
	/**
	 * Calcule la somme des valeurs associées à des numéros de câble extraits d'une chaîne de caractères.
	 * 
	 * <p>Cette méthode utilise une expression régulière pour extraire tous les numéros de câbles de la chaîne
	 * fournie et utilise ces numéros pour extraire les valeurs correspondantes de {@code tableDesDifferentsCables}.
	 * Les valeurs sont ensuite additionnées pour obtenir une somme totale. En cas d'erreur, la méthode retourne {@code null} 
	 * ou {@code vide} selon le type d'exception rencontrée.</p>
	 * 
	 * @param ligne La chaîne de caractères contenant les numéros de câbles à extraire.
	 * @return La somme des valeurs extraites des câbles, ou {@code null} si une erreur se produit.
	 */
	private static Double calculerLCellule(String ligne) {
	    List<Integer> numeros = new ArrayList<>();
	    
	    Pattern pattern = Pattern.compile("L(\\d+)");
	    Matcher matcher = pattern.matcher(ligne);

	    while (matcher.find()) {
	        String numero = matcher.group(1);
	        numeros.add(Integer.parseInt(numero));
	    }
	    
	    double somme = 0;
	    
	    try {
	        for (int i = 0; i < numeros.size(); i++) {
	            Object value = tableDesDifferentsCables.getValueAt(0, numeros.get(i));
	            
	            if (value == null || value.toString().isEmpty()) {
	                return null;
	            }
	            
	            somme += Double.parseDouble(value.toString());
	        }
	    } catch (IndexOutOfBoundsException | NumberFormatException e) {
	        return vide;
	    }
	    
	    return somme;
	}
	
	/**
	 * Met à jour les valeurs dans la table des parafoudres DC {@code tableParafoudreDC}.
	 * 
	 * <p>La méthode met à jour les cellules de la table en fonction des valeurs disponibles dans les variables globales
	 * et des données spécifiques de chaque entrée. Les valeurs mises à jour incluent :</p>
	 * <ul>
	 *   <li>Nom de l'entrée</li>
	 *   <li>Tension maximale en circuit ouvert (Uocmax)</li>
	 *   <li>Valeur ajustée de Uw (val08Uw)</li>
	 *   <li>Courant de court-circuit maximal (Iscmax)</li>
	 * </ul>
	 * 
	 * @see #arrondirDouble(double, int)
	 */
	public static void MAJtableParafoudreDC() {
	    for (int col = 1; col < nbEntrees + 1; col++) {
	        // Calcul et mise à jour des valeurs dans les cellules
	        if (rappelUocTmin != vide) {
	            double Uocmax = arrondirDouble(nbModulesParChaineParEntree.get(col - 1) * rappelUocTmin, 2);
	            tableParafoudreDC.setValueAt(Uocmax, 0, col);
	        } else {
	            tableParafoudreDC.setValueAt("", 0, col);
	        }
	        if (Uw != vide) {
	            double val08Uw = arrondirDouble(0.8 * Uw, 2);
	            tableParafoudreDC.setValueAt(val08Uw, 1, col);
	        } else {
	            tableParafoudreDC.setValueAt("", 1, col);
	        }
	        if (rappelIscSTC != vide) {
	            double Iscmax = arrondirDouble(1.25 * rappelIscSTC * nbChainesChoisi.get(col - 1), 2);
	            tableParafoudreDC.setValueAt(Iscmax, 2, col);
	        } else {
	            tableParafoudreDC.setValueAt("", 2, col);
	        }
	        tableParafoudreDC.getColumnModel().getColumn(col).setCellRenderer(new CustomRendererTableParafoudreDC());
	    }

	    tableParafoudreDC.repaint();
	}
	
	/**
	 * Met à jour les valeurs dans la table des interrupteurs et sectionneurs {@code tableInterrupteursSectionneurs}.
	 * 
	 * <p>La méthode met à jour les cellules de la table en fonction des valeurs disponibles dans les variables globales
	 * pour chaque entrée. Les valeurs mises à jour incluent :</p>
	 * <ul>
	 *   <li>Nom de l'entrée</li>
	 *   <li>Tension maximale en circuit ouvert (Uocmax)</li>
	 *   <li>Courant de court-circuit maximal (Iscmax)</li>
	 * </ul>
	 * 
	 * @see #arrondirDouble(double, int)
	 */
	public static void MAJtableInterrupteursSectionneurs() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			if (rappelUocTmin != vide) {
				double Uocmax = arrondirDouble(nbModulesParChaineParEntree.get(col - 1) * rappelUocTmin, 2);
				tableInterrupteursSectionneurs.setValueAt(Uocmax, 0, col);
			}
			else {
				tableInterrupteursSectionneurs.setValueAt("", 0, col);
			}
			if (rappelIscSTC != vide) {
				double Iscmax = arrondirDouble(1.25 * rappelIscSTC * nbChainesChoisi.get(col - 1), 2);
				tableInterrupteursSectionneurs.setValueAt(Iscmax, 1, col);
			}
			else {
				tableInterrupteursSectionneurs.setValueAt("", 1, col);
			}
			tableInterrupteursSectionneurs.getColumnModel().getColumn(col).setCellRenderer(new CustomRendererTableInterrupteursSectionneurs());
	    }

		tableInterrupteursSectionneurs.repaint();
	}
	
	/**
	 * Calcule si une protection parafoudre est obligatoire pour le courant continu (DC).
	 * 
	 * <p>La méthode compare la somme totale des longueurs de câbles ({@code Ltotale}) avec la longueur critique ({@code Lcrit}).
	 * Si {@code Ltotale} est supérieur ou égal à {@code Lcrit}, la protection est jugée obligatoire ("OUI"), sinon non
	 * obligatoire ("NON").</p>
	 * 
	 * @see #calculerLtotale()
	 * @see #calculerLcrit()
	 */
	public static void calculerProtectionParafoudreObligatoireDC() {
		if (Ltotale != vide && Lcrit != vide) {
			protectionParafoudreObligatoireDC = Ltotale >= Lcrit ? "OUI" : "NON";
		}
		else {
			protectionParafoudreObligatoireDC = "";
		}
	}
	
	/**
	 * Met à jour les valeurs dans la table des disjoncteurs AC associés aux onduleurs {@code tableDisjoncteursACAssociesAuxOnduleurs}.
	 * 
	 * <p>La méthode met à jour la table en fonction des valeurs disponibles dans la liste {@code IACmaxOnduleurs}. Les valeurs
	 * mises à jour correspondent au courant maximal en AC pour chaque onduleur.</p>
	 */
	public static void MAJtableDisjoncteursACAssociesAuxOnduleurs() {
		for (int col = 1; col < nbOnduleurs + 1; col++) {
			if (!IACmaxOnduleurs.isEmpty()) {
				tableDisjoncteursACAssociesAuxOnduleurs.setValueAt(IACmaxOnduleurs.get(col - 1), 1, col);
			}
			else {
				tableDisjoncteursACAssociesAuxOnduleurs.setValueAt("", 1, col);
			}
			tableDisjoncteursACAssociesAuxOnduleurs.getColumnModel().getColumn(col).setCellRenderer(new CustomRendererTableDisjoncteursACAssociesAuxOnduleurs());
	    }

		tableDisjoncteursACAssociesAuxOnduleurs.repaint();
	}
	
	/**
	 * Met à jour les rendus visuels de la table des parafoudres AC {@code tableParafoudreAC}.
	 */
	public static void MAJtableParafoudreAC() {
		tableParafoudreAC.getColumnModel().getColumn(1).setCellRenderer(new CustomRendererTableParafoudreAC(Uw));
		tableParafoudreAC.repaint();
	}
	
	/**
	 * Met à jour les rendus visuels de la table associée au disjoncteur du parafoudre AC {@code tableDisjoncteurAssocieAuParafoudreAC}.
	 */
	public static void MAJtableDisjoncteurAssocieAuParafoudreAC() {
		tableDisjoncteurAssocieAuParafoudreAC.getColumnModel().getColumn(1).setCellRenderer(new CustomRendererTableDisjoncteurAssocieAuParafoudreAC());
		tableDisjoncteurAssocieAuParafoudreAC.repaint();
	}
	
	/**
	 * Calcule la valeur de {@code NkAC} en fonction de {@code NgAC}.
	 * 
	 * <p>La méthode calcule {@code NkAC} comme étant 10 fois {@code NgAC}. Si {@code NgAC} est égal à {@code vide}, 
	 * la valeur de {@code NkAC} reste inchangée.</p>
	 * 
	 * @see #NgAC
	 * @see #NkAC
	 */
	public static void calculerNkAC() {
		if (NgAC != vide) {
			NkAC = 10 * NgAC;
		}
	}
	

	/**
	 * Calcule la valeur de {@code NgAC} en fonction de {@code NkAC}.
	 * 
	 * <p>La méthode calcule {@code NgAC} comme étant {@code NkAC} divisé par 10. Si {@code NkAC} est égal à {@code vide},
	 * la valeur de {@code NgAC} reste inchangée.</p>
	 * 
	 * @see #NkAC
	 * @see #NgAC
	 */
	public static void calculerNgAC() {
		if (NkAC != vide) {
			NgAC = NkAC / 10;
		}
	}
	
	/**
	 * Met à jour la valeur dans la table du disjoncteur AC général {@code tableDisjoncteurACGeneral}.
	 * 
	 * <p>La méthode calcule la somme des courants maximaux en AC ({@code IACmaxOnduleurs}) pour tous les onduleurs et 
	 * met à jour la cellule correspondante dans la table. Si la liste {@code IACmaxOnduleurs} est vide, la cellule est 
	 * laissée vide.</p>
	 */
    public static void MAJtableDisjoncteurACGeneral() {
    	if (!IACmaxOnduleurs.isEmpty()) {
        	Double Ie = 0.0;
        	for (Double d : IACmaxOnduleurs) {
        		Ie += d;
        	}
        	tableDisjoncteurACGeneral.setValueAt(Ie, 0, 1);
    	}
    	else {
        	tableDisjoncteurACGeneral.setValueAt("", 0, 1);
	    }

    	tableDisjoncteurACGeneral.getColumnModel().getColumn(1).setCellRenderer(new CustomRendererTableDisjoncteurACGeneral());
    	tableDisjoncteurACGeneral.repaint();
    }
    
    /**
     * Met à jour la table {@code tableNcNpIz} en fonction des valeurs de {@code Nc} et {@code Np}.
     * 
     * <p>Pour chaque colonne, la méthode vérifie les valeurs de {@code Nc} (nombre de chaînes) et {@code Np} 
     * (nombre de dispositifs de protection) dans la table {@code tableNcNpIz}. Selon les conditions spécifiques, 
     * elle calcule une valeur de {@code Iz} (courant de protection) et la met à jour dans la table.</p>
     * 
     * <p>Les conditions sont les suivantes :</p>
     * <ul>
     *     <li>Si {@code Nc} est égal à 1 et {@code Np} est égal à 0, {@code Iz} est calculé comme 1.25 fois {@code rappelIscSTC}.</li>
     *     <li>Si {@code Nc} est inférieur à 20 et {@code Np} est égal à 1, {@code Iz} est calculé comme 1.45 fois la valeur de {@code choixFusible}.</li>
     * </ul>
     * 
     * @see #rappelIscSTC
     * @see #choixFusible
     * @see #IscmaxOuI2PourChaqueOnduleur
     * @see #tableNcNpIz
     */
	public static void MAJtableNcNpIz() {
		for (int col = 1; col < nbOnduleurs + 1; col++) {
			if (tableNcNpIz.getValueAt(0, col) != null && tableNcNpIz.getValueAt(1, col) != null) {
				int Nc = Integer.parseInt(tableNcNpIz.getValueAt(0, col).toString());
				int Np = Integer.parseInt(tableNcNpIz.getValueAt(1, col).toString());
				boolean compIscmax = Nc == 1 && Np == 0;
				boolean compI2 = Nc < 20 && Np == 1;
				double IzBorneInf = 0;
				if (compIscmax && rappelIscSTC != vide) {
					IzBorneInf = arrondirDouble(1.25 * rappelIscSTC, 2);
					tableNcNpIz.setValueAt(IzBorneInf + " A", 2, col);
					IscmaxOuI2PourChaqueOnduleur.add("Iscmax");
				}
				else if (compI2 && !(choixFusible == null)) {
					IzBorneInf = arrondirDouble(1.45 * choixFusible.getIn(), 2);
					tableNcNpIz.setValueAt(IzBorneInf + " A", 2, col);
					IscmaxOuI2PourChaqueOnduleur.add("I2");
				}
			}
			else {
				tableNcNpIz.setValueAt("", 1, col);
				tableNcNpIz.setValueAt("", 2, col);
			}
		}
	}
	
	/**
	 * Calcule la valeur de {@code NkDC} en fonction de {@code NgDC} ou {@code Lcrit}.
	 * 
	 * <p>La méthode calcule {@code NkDC} de la manière suivante :</p>
	 * <ul>
	 *     <li>Si {@code NgDC} est défini, {@code NkDC} est calculé comme 10 fois {@code NgDC}.</li>
	 *     <li>Sinon, si {@code Lcrit} et {@code constantePourLcrit} sont définis, {@code NkDC} est calculé comme 10 fois {@code constantePourLcrit} divisé par {@code Lcrit}.</li>
	 * </ul>
	 * 
	 * @see #NgDC
	 * @see #Lcrit
	 * @see #constantePourLcrit
	 * @see #NkDC
	 */
	public static void calculerNkDC() {
		if (NgDC != vide) {
			NkDC = 10 * NgDC;
		}
		else if (Lcrit != vide && constantePourLcrit != vide) {
			NkDC = 10 * constantePourLcrit / Lcrit;
		}
	}
	
	/**
	 * Calcule la valeur de {@code KCablesChaine} en fonction des valeurs de {@code K1CablesChaine}, {@code K2CablesChaine}, et {@code K3CablesChaine}.
	 * 
	 * <p>La méthode calcule {@code KCablesChaine} comme le produit de {@code K1CablesChaine}, {@code K2CablesChaine}, et {@code K3CablesChaine}. Si l'une des valeurs est {@code vide},
	 * {@code KCablesChaine} est également défini comme {@code vide}.</p>
	 * 
	 * @see #K1CablesChaine
	 * @see #K2CablesChaine
	 * @see #K3CablesChaine
	 * @see #KCablesChaine
	 */
	public static void calculerKCablesChaine() {
		if (K1CablesChaine != vide && K2CablesChaine != vide && K3CablesChaine != vide) {
			KCablesChaine = arrondirDouble(K1CablesChaine * K2CablesChaine * K3CablesChaine, 3);
		}
		else {
			KCablesChaine = vide;
		}
	}
	
	/**
	 * Met à jour la table {@code tableChoixCablesDeChaine} en fonction des valeurs de {@code IscmaxOuI2PourChaqueOnduleur},
	 * {@code KCablesChaine}, et {@code choixFusible}.
	 * 
	 * <p>Pour chaque colonne, la méthode vérifie si la liste {@code IscmaxOuI2PourChaqueOnduleur} n'est pas vide. 
	 * Selon que le choix est "Iscmax" ou "I2", elle calcule une valeur de courant de protection minimale {@code IzPrime}
	 * et la compare avec les valeurs des cellules de la table.</p>
	 * 
	 * <p>Les conditions de calcul et mise à jour des cellules sont les suivantes :</p>
	 * <ul>
	 *     <li>Si {@code IscmaxOuI2} est égal à "Iscmax" et {@code KCablesChaine} est défini, {@code IzPrime} est calculé comme 
	 *     1.25 fois {@code rappelIscSTC} divisé par {@code KCablesChaine}. Les résultats sont mis à jour dans les cellules correspondantes.</li>
	 *     <li>Si {@code IscmaxOuI2} est égal à "I2", {@code KCablesChaine} est défini, et {@code choixFusible} n'est pas null, 
	 *     {@code IzPrime} est calculé comme 1.45 fois le courant nominal du fusible divisé par {@code KCablesChaine}. Les résultats 
	 *     sont mis à jour dans les cellules correspondantes.</li>
	 * </ul>
	 * 
	 * <p>Les valeurs mises à jour sont :</p>
	 * <ul>
	 *     <li>La première ligne indique le critère de protection minimum {@code Iz}.</li>
	 *     <li>La deuxième ligne affiche la valeur de {@code IzPrime} calculée.</li>
	 *     <li>Les lignes suivantes affichent les résultats de la comparaison entre {@code IzPrime} et les valeurs des cellules 
	 *     correspondantes. La valeur est "OK" si le critère est satisfait, sinon "NON".</li>
	 * </ul>
	 * 
	 * @see #IscmaxOuI2PourChaqueOnduleur
	 * @see #KCablesChaine
	 * @see #choixFusible
	 * @see #rappelIscSTC
	 * @see #tableChoixCablesDeChaine
	 */
	public static void MAJtableChoixCablesDeChaine() {
		for (int col = 1; col < nbOnduleurs + 1; col++) {
			if (!IscmaxOuI2PourChaqueOnduleur.isEmpty()) {
				String IscmaxOuI2 = IscmaxOuI2PourChaqueOnduleur.get(col - 1);
				tableChoixCablesDeChaine.setValueAt("Iz \u2265 " + IscmaxOuI2, 0, col);
				if (IscmaxOuI2.equals("Iscmax") && KCablesChaine != vide) {
					double IzPrime = arrondirDouble(1.25 * rappelIscSTC / KCablesChaine, 2);
					tableChoixCablesDeChaine.setValueAt(IzPrime + " A", 1, col);
					if (tableChoixCablesDeChaine.getValueAt(2, col) != null && !tableChoixCablesDeChaine.getValueAt(2, col).toString().equals("")) {
						String resultat = Double.parseDouble(tableChoixCablesDeChaine.getValueAt(2, col).toString()) >= IzPrime ? "OK" : "NON";
						tableChoixCablesDeChaine.setValueAt(resultat, 5, col);
					}
					else {
						tableChoixCablesDeChaine.setValueAt("", 5, col);
					}
					if (tableChoixCablesDeChaine.getValueAt(3, col) != null && !tableChoixCablesDeChaine.getValueAt(3, col).toString().equals("")) {
						String resultat = Double.parseDouble(tableChoixCablesDeChaine.getValueAt(3, col).toString()) >= IzPrime ? "OK" : "NON";
						tableChoixCablesDeChaine.setValueAt(resultat, 6, col);
					}
					else {
						tableChoixCablesDeChaine.setValueAt("", 6, col);
					}
					if (tableChoixCablesDeChaine.getValueAt(4, col) != null && !tableChoixCablesDeChaine.getValueAt(4, col).toString().equals("")) {
						String resultat = Double.parseDouble(tableChoixCablesDeChaine.getValueAt(4, col).toString()) >= IzPrime ? "OK" : "NON";
						tableChoixCablesDeChaine.setValueAt(resultat, 7, col);
					}
					else {
						tableChoixCablesDeChaine.setValueAt("", 7, col);
					}
				}
				else if (IscmaxOuI2.equals("I2") && KCablesChaine != vide && choixFusible != null) {
					double IzPrime = arrondirDouble(1.45 * choixFusible.getIn() / KCablesChaine, 2);
					tableChoixCablesDeChaine.setValueAt(IzPrime + " A", 1, col);
					if (tableChoixCablesDeChaine.getValueAt(2, col) != null && !tableChoixCablesDeChaine.getValueAt(2, col).toString().equals("")) {
						String resultat = Double.parseDouble(tableChoixCablesDeChaine.getValueAt(2, col).toString()) >= IzPrime ? "OK" : "NON";
						tableChoixCablesDeChaine.setValueAt(resultat, 5, col);
					}
					else {
						tableChoixCablesDeChaine.setValueAt("", 5, col);
					}
					if (tableChoixCablesDeChaine.getValueAt(3, col) != null && !tableChoixCablesDeChaine.getValueAt(3, col).toString().equals("")) {
						String resultat = Double.parseDouble(tableChoixCablesDeChaine.getValueAt(3, col).toString()) >= IzPrime ? "OK" : "NON";
						tableChoixCablesDeChaine.setValueAt(resultat, 6, col);
					}
					else {
						tableChoixCablesDeChaine.setValueAt("", 6, col);
					}
					if (tableChoixCablesDeChaine.getValueAt(4, col) != null && !tableChoixCablesDeChaine.getValueAt(4, col).toString().equals("")) {
						String resultat = Double.parseDouble(tableChoixCablesDeChaine.getValueAt(4, col).toString()) >= IzPrime ? "OK" : "NON";
						tableChoixCablesDeChaine.setValueAt(resultat, 7, col);
					}
					else {
						tableChoixCablesDeChaine.setValueAt("", 7, col);
					}
				}
			}
			else {
				tableChoixCablesDeChaine.setValueAt("", 0, col);
				tableChoixCablesDeChaine.setValueAt("", 1, col);
			}
		}
	}
	
	/**
	 * Récupère la référence du fusible courant sélectionné.
	 * 
	 * @return une chaîne de caractères représentant la référence du fusible courant.
	 */
	public static String getFusibleCourant() {
		if (choixFusible == null) {
			return "";
		}
		return choixFusible.getReference();
	}
	
	/**
	 * Récupère la section des câbles de chaîne choisie.
	 * 
	 * @return la section des câbles de chaîne sous forme de double.
	 */
	public static double getChoixSectionCablesDeChaine() {
		return choixSectionCablesDeChaine;
	} 
	
	/**
	 * Récupère la section du câble principal choisie.
	 * 
	 * @return la section du câble principal sous forme de double.
	 */
	public static double getChoixSectionCablePrincipal() {
		return choixSectionCablePrincipal;
	}
	
	/**
	 * Met à jour la table {@code tableChuteDeTension1} avec les valeurs de tension des chaînes.
	 * 
	 * <p>Pour chaque colonne, les valeurs de tension sont mises à jour comme suit :</p>
	 * <ul>
	 *     <li>La première ligne affiche {@code rappelImppSTC} (tension en mode point de puissance maximum, STC) arrondie à 2 décimales.</li>
	 *     <li>La deuxième ligne affiche le produit de {@code rappelUmppSTC} (tension en mode point de puissance maximum, STC) et le nombre de modules par chaîne pour l'entrée, arrondi à 2 décimales.</li>
	 * </ul>
	 * 
	 * <p>Si les valeurs de {@code rappelImppSTC} ou {@code rappelUmppSTC} sont nulles, les cellules correspondantes seront vides.</p>
	 * 
	 * @see #rappelImppSTC
	 * @see #rappelUmppSTC
	 * @see #nbModulesParChaineParEntree
	 * @see #tableChuteDeTension1
	 */
	public static void MAJtableChuteDeTension1() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			if (rappelImppSTC != vide) {
				tableChuteDeTension1.setValueAt(arrondirDouble(rappelImppSTC, 2), 0, col);
			}
			else {
				tableChuteDeTension1.setValueAt("", 0, col);
			}
			if (rappelUmppSTC != vide) {
				tableChuteDeTension1.setValueAt(arrondirDouble(rappelUmppSTC * nbModulesParChaineParEntree.get(col - 1), 2), 1, col);
			}
			else {
				tableChuteDeTension1.setValueAt("", 1, col);
			}
		}
	}
	
	/**
	 * Met à jour la table {@code tableChuteDeTension2} avec les calculs de chute de tension pour chaque entrée.
	 * 
	 * <p>Pour chaque colonne, la méthode calcule et met à jour les valeurs suivantes :</p>
	 * <ul>
	 *     <li>La résistance totale des câbles ({@code Rc}) est calculée à partir de la longueur totale de la chaîne et de la section des câbles. Cette valeur est affichée dans la troisième ligne.</li>
	 *     <li>La chute de tension totale ({@code deltaU}) est calculée comme le produit de {@code Rc} et {@code rappelImppSTC}, et est affichée dans la quatrième ligne.</li>
	 *     <li>Le pourcentage de chute de tension par rapport à la tension en mode point de puissance maximum associée ({@code valUmppAssocie}) est calculé et affiché dans la cinquième ligne.</li>
	 * </ul>
	 * 
	 * <p>Si les valeurs nécessaires ne sont pas disponibles, les cellules correspondantes seront laissées vides.</p>
	 * 
	 * @see #tableChuteDeTension2
	 * @see #choixSectionCablesDeChaine
	 * @see #rappelImppSTC
	 * @see #calculerLCellule(String)
	 * @see #arrondirDouble(double, int)
	 */
	public static void MAJtableChuteDeTension2() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			double valChainePlusLongue = vide;
			if (tableChuteDeTension2.getValueAt(0, col) != null && !tableChuteDeTension2.getValueAt(0, col).toString().equals("") && calculerLCellule(tableChuteDeTension2.getValueAt(0, col).toString()) != null) {
				valChainePlusLongue = calculerLCellule(tableChuteDeTension2.getValueAt(0, col).toString());
			}
			double valUmppAssocie = vide;
			if (tableChuteDeTension2.getValueAt(1, col) != null && !tableChuteDeTension2.getValueAt(1, col).toString().equals("")) {
				valUmppAssocie = Double.parseDouble(tableChuteDeTension2.getValueAt(1, col).toString());
			}
			double Rc = vide;
			if (valChainePlusLongue != vide && choixSectionCablesDeChaine != 0) {
				Rc = arrondirDouble(0.023 * 2 * valChainePlusLongue / choixSectionCablesDeChaine, 3);
				tableChuteDeTension2.setValueAt(Rc + " \u2126", 2, col);
			}
			else {
				tableChuteDeTension2.setValueAt("", 2, col);
			}
			double deltaU = vide;
			if (Rc != vide && rappelImppSTC != vide) {
				deltaU = arrondirDouble(Rc * rappelImppSTC, 2);
				tableChuteDeTension2.setValueAt(deltaU + " V", 3, col);
			}
			else {
				tableChuteDeTension2.setValueAt("", 3, col);
			}
			if (deltaU != vide && valUmppAssocie != vide) {
				double chuteDeTension = arrondirDouble(100 * deltaU / valUmppAssocie, 3);
				tableChuteDeTension2.setValueAt(chuteDeTension, 4, col);
			}
			else {
				tableChuteDeTension2.setValueAt("", 4, col);
			}
		}
	}
	
	/**
	 * Extrait un nombre à partir d'une chaîne de caractères contenant une valeur numérique suivie d'une unité.
	 * 
	 * <p>La méthode recherche la première occurrence d'un espace dans la chaîne, considère que la partie avant cet espace 
	 * est la partie numérique, et tente de la convertir en un nombre décimal.</p>
	 * 
	 * <p>Si la chaîne est nulle ou vide, ou si la partie numérique ne peut pas être convertie en un nombre décimal, la méthode 
	 * retourne {@code null}.</p>
	 * 
	 * @param valeurAvecUnite La chaîne contenant la valeur numérique suivie d'une unité (e.g., "25 A").
	 * @return La valeur numérique extraite et convertie en {@code Double}, ou {@code null} si la chaîne est invalide ou la conversion échoue.
	 */
	public static Double extraireDouble(String valeurAvecUnite) {
	    if (valeurAvecUnite != null && !valeurAvecUnite.isEmpty()) {
	        try {
	            int espaceIndex = valeurAvecUnite.indexOf(' ');
	            if (espaceIndex > 0) {
	                String partieNumerique = valeurAvecUnite.substring(0, espaceIndex);
	                return Double.parseDouble(partieNumerique);
	            }
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    }
	    return null;
	}
		
	/**
	 * Met à jour la table {@code tableCablePrincipal1} avec les valeurs de courant de court-circuit maximal pour chaque entrée.
	 * 
	 * <p>Pour chaque colonne, la méthode calcule le courant maximal de court-circuit ({@code IscmaxGen}) comme étant 1.25 fois 
	 * {@code rappelIscSTC} multiplié par le nombre de chaînes choisies pour l'entrée correspondante. Les résultats sont affichés 
	 * dans la première ligne de la table.</p>
	 * 
	 * <p>Les valeurs calculées sont arrondies à deux décimales et ajoutées à la liste {@code Iscmax_gen}. Si les conditions ne sont pas 
	 * remplies (par exemple, si {@code nbChainesChoisi} ou {@code rappelIscSTC} sont invalides), les cellules sont laissées vides.</p>
	 * 
	 * @see #nbChainesChoisi
	 * @see #rappelIscSTC
	 * @see #tableCablePrincipal1
	 * @see #arrondirDouble(double, int)
	 */
	public static void MAJtableCablePrincipal1() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			if (!nbChainesChoisi.isEmpty() && rappelIscSTC != vide) {
				double IscmaxGen = arrondirDouble(nbChainesChoisi.get(col - 1) * 1.25 * rappelIscSTC, 2);
				tableCablePrincipal1.setValueAt(IscmaxGen, 0, col);
			}
			else {
				tableCablePrincipal1.setValueAt("", 0, col);
			}
		}
	}
	
	/**
	 * Calcule la valeur de {@code NgDC} à partir des valeurs de {@code NkDC} et, le cas échéant, des valeurs de {@code Lcrit} 
	 * et {@code constantePourLcrit}.
	 * 
	 * <p>Si {@code NkDC} est défini, {@code NgDC} est calculé comme {@code NkDC} divisé par 10. Si {@code NkDC} n'est pas défini 
	 * mais que {@code Lcrit} et {@code constantePourLcrit} sont disponibles, {@code NgDC} est calculé comme le produit de 
	 * {@code constantePourLcrit} divisé par {@code Lcrit}.</p>
	 * 
	 * @see #NkDC
	 * @see #Lcrit
	 * @see #constantePourLcrit
	 * @see #NgDC
	 */
	public static void calculerNgDC() {
		if (NkDC != vide) {
			NgDC = NkDC / 10;
		}
		else if (Lcrit != vide && constantePourLcrit != vide){
			NgDC = constantePourLcrit / Lcrit;
		}
	}
	
	/**
	 * Calcule le coefficient global des câbles principaux ({@code KCablePrincipal}).
	 * 
	 * <p>Le coefficient est calculé comme le produit de trois coefficients ({@code K1CablePrincipal}, {@code K2CablePrincipal}, 
	 * et {@code K3CablePrincipal}). Si l'un de ces coefficients est non défini ou invalide, le coefficient global est défini 
	 * comme {@code vide}.</p>
	 * 
	 * @see #K1CablePrincipal
	 * @see #K2CablePrincipal
	 * @see #K3CablePrincipal
	 * @see #KCablePrincipal
	 * @see #arrondirDouble(double, int)
	 */
	public static void calculerKCablePrincipal() {
		if (K1CablePrincipal != vide && K2CablePrincipal != vide && K3CablePrincipal != vide) {
			KCablePrincipal = arrondirDouble(K1CablePrincipal * K2CablePrincipal * K3CablePrincipal, 3);
		}
		else {
			KCablePrincipal = vide;
		}
	}
	
	/**
	 * Met à jour la table {@code tableCablePrincipal2} avec les valeurs de courant minimums nécessaires pour chaque entrée,
	 * en fonction des valeurs de la table {@code tableCablePrincipal1} et du coefficient global des câbles principaux.
	 * 
	 * <p>Pour chaque colonne, la méthode calcule le courant minimum ({@code IzPrime}) comme étant le courant maximal de court-circuit 
	 * divisé par {@code KCablePrincipal}. Cette valeur est ensuite comparée avec les valeurs présentes dans les lignes suivantes 
	 * de la table {@code tableCablePrincipal2}. Les résultats sont affichés dans les lignes correspondantes en utilisant 
	 * "OK" ou "NON" en fonction des comparaisons.</p>
	 * 
	 * <p>Si les conditions nécessaires (valeurs non nulles et non vides dans {@code tableCablePrincipal1} et {@code KCablePrincipal} 
	 * définie) ne sont pas remplies, les cellules de la table {@code tableCablePrincipal2} sont laissées vides.</p>
	 * 
	 * @see #tableCablePrincipal1
	 * @see #tableCablePrincipal2
	 * @see #KCablePrincipal
	 * @see #arrondirDouble(double, int)
	 */
	public static void MAJtableCablePrincipal2() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			if (tableCablePrincipal1.getValueAt(0, col) != null && !tableCablePrincipal1.getValueAt(0, col).toString().equals("") && KCablePrincipal != vide) {
				double IzPrime = arrondirDouble(Double.parseDouble(tableCablePrincipal1.getValueAt(0, col).toString()) / KCablePrincipal, 2);
				tableCablePrincipal2.setValueAt(IzPrime + " A", 0, col);
				if (tableCablePrincipal2.getValueAt(1, col) != null && !tableCablePrincipal2.getValueAt(1, col).toString().equals("")) {
					String resultat = Double.parseDouble(tableCablePrincipal2.getValueAt(1, col).toString()) >= IzPrime ? "OK" : "NON";
					tableCablePrincipal2.setValueAt(resultat, 4, col);
				}
				else {
					tableCablePrincipal2.setValueAt("", 4, col);
				}
				if (tableCablePrincipal2.getValueAt(2, col) != null && !tableCablePrincipal2.getValueAt(2, col).toString().equals("")) {
					String resultat = Double.parseDouble(tableCablePrincipal2.getValueAt(2, col).toString()) >= IzPrime ? "OK" : "NON";
					tableCablePrincipal2.setValueAt(resultat, 5, col);
				}
				else {
					tableCablePrincipal2.setValueAt("", 5, col);
				}
				if (tableCablePrincipal2.getValueAt(3, col) != null && !tableCablePrincipal2.getValueAt(3, col).toString().equals("")) {
					String resultat = Double.parseDouble(tableCablePrincipal2.getValueAt(3, col).toString()) >= IzPrime ? "OK" : "NON";
					tableCablePrincipal2.setValueAt(resultat, 6, col);
				}
				else {
					tableCablePrincipal2.setValueAt("", 6, col);
				}
			}
			else {
				tableCablePrincipal2.setValueAt("", 0, col);
			}
		}
	}
	
	/**
	 * Met à jour la table {@code tableChuteDeTensionMaximumARespecter} avec les valeurs de chute de tension à respecter pour chaque entrée.
	 * 
	 * <p>Pour chaque colonne, la méthode calcule les valeurs suivantes :</p>
	 * <ul>
	 *     <li><b>Longueur de chaîne du bon ordre du onduleur :</b> Calculée en fonction de la cellule de la table {@code tableChuteDeTensionMaximumARespecter} si elle n'est pas vide et si la méthode {@code calculerLCellule} renvoie une valeur non nulle.</li>
	 *     <li><b>Nombre de chaînes choisi :</b> Récupéré de {@code nbChainesChoisi} et affiché dans la table.</li>
	 *     <li><b>Résistance du câble :</b> Calculée si la longueur de la chaîne et la section du câble principal sont définies. Calculée comme {@code Rc = 0.023 * 2 * longueurChaineBJOnduleur / choixSectionCablePrincipal} et arrondie à trois décimales.</li>
	 *     <li><b>Variation de tension :</b> Calculée comme {@code deltaU = Rc * rappelImppSTC * nbChainesChoisi} et arrondie à deux décimales.</li>
	 *     <li><b>Chute de tension :</b> Calculée comme {@code chuteDeTension = 100 * deltaU / valUmppAssocie} et arrondie à trois décimales.</li>
	 * </ul>
	 * 
	 * <p>Les valeurs calculées sont ensuite insérées dans les colonnes appropriées de {@code tableChuteDeTensionMaximumARespecter}. Si les données nécessaires pour les calculs sont manquantes ou invalides, les cellules correspondantes sont laissées vides.</p>
	 * 
	 * @see #tableChuteDeTensionMaximumARespecter
	 * @see #tableChuteDeTension2
	 * @see #calculerLCellule(String)
	 * @see #arrondirDouble(double, int)
	 * @see #choixSectionCablePrincipal
	 * @see #nbChainesChoisi
	 * @see #rappelImppSTC
	 */
	public static void MAJtableChuteDeTensionMaximumARespecter() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			double longueurChaineBJOnduleur = vide;
			if (tableChuteDeTensionMaximumARespecter.getValueAt(0, col) != null && !tableChuteDeTensionMaximumARespecter.getValueAt(0, col).toString().equals("") && calculerLCellule(tableChuteDeTensionMaximumARespecter.getValueAt(0, col).toString()) != null) {
				longueurChaineBJOnduleur = calculerLCellule(tableChuteDeTensionMaximumARespecter.getValueAt(0, col).toString());
			}
			if (!nbChainesChoisi.isEmpty() && rappelImppSTC != vide) {
				tableChuteDeTensionMaximumARespecter.setValueAt(nbChainesChoisi.get(col - 1), 1, col);
			}
			double valUmppAssocie = vide;
			if (tableChuteDeTension2.getValueAt(1, col) != null && !tableChuteDeTension2.getValueAt(1, col).toString().equals("")) {
				valUmppAssocie = Double.parseDouble(tableChuteDeTension2.getValueAt(1, col).toString());
			}
			double Rc = vide;
			if (longueurChaineBJOnduleur != vide && choixSectionCablePrincipal != 0) {
				Rc = arrondirDouble(0.023 * 2 * longueurChaineBJOnduleur / choixSectionCablePrincipal, 3);
				tableChuteDeTensionMaximumARespecter.setValueAt(Rc + " \u2126", 2, col);
			}
			else {
				tableChuteDeTensionMaximumARespecter.setValueAt("", 2, col);
			}
			double deltaU = vide;
			if (Rc != vide && rappelImppSTC != vide && !nbChainesChoisi.isEmpty()) {
				deltaU = arrondirDouble(Rc * rappelImppSTC * Double.parseDouble(tableChuteDeTensionMaximumARespecter.getValueAt(1, col).toString()), 2);
				tableChuteDeTensionMaximumARespecter.setValueAt(deltaU + " V", 3, col);
			}
			else {
				tableChuteDeTensionMaximumARespecter.setValueAt("", 3, col);
			}
			if (deltaU != vide && valUmppAssocie != vide) {
				double chuteDeTension = arrondirDouble(100 * deltaU / valUmppAssocie, 3);
				tableChuteDeTensionMaximumARespecter.setValueAt(chuteDeTension, 4, col);
			}
			else {
				tableChuteDeTensionMaximumARespecter.setValueAt("", 4, col);
			}
		}
	}
	
	/**
	 * Met à jour la table {@code tableChuteDeTensionTotaleDCMaximumARespecter} avec les valeurs de chute de tension totale à respecter pour chaque entrée.
	 * 
	 * <p>Pour chaque colonne, la méthode calcule et met à jour les valeurs suivantes :</p>
	 * <ul>
	 *     <li><b>Chute de tension de la chaîne :</b> Récupérée depuis {@code tableChuteDeTension2} et affichée si elle est valide.</li>
	 *     <li><b>Chute de tension du câble principal :</b> Récupérée depuis {@code tableChuteDeTensionMaximumARespecter} et affichée si elle est valide.</li>
	 *     <li><b>Chute de tension totale :</b> Calculée comme la somme de la chute de tension de la chaîne et du câble principal. Comparée à 1% pour déterminer si elle est acceptable. La valeur est affichée avec un message indiquant si elle est inférieure à 1% (OK) ou supérieure à 1% (NON).</li>
	 * </ul>
	 * 
	 * @see #tableChuteDeTensionTotaleDCMaximumARespecter
	 * @see #tableChuteDeTension2
	 * @see #tableChuteDeTensionMaximumARespecter
	 * @see #arrondirDouble(double, int)
	 */
	public static void MAJtableChuteDeTensionTotaleDCMaximumARespecter() {
		for (int col = 1; col < nbEntrees + 1; col++) {
			double chuteDeTensionChaine = vide;
			if (tableChuteDeTension2.getValueAt(4, col) != null && !tableChuteDeTension2.getValueAt(4, col).toString().equals("")) {
				chuteDeTensionChaine = Double.parseDouble(tableChuteDeTension2.getValueAt(4, col).toString());
				tableChuteDeTensionTotaleDCMaximumARespecter.setValueAt(chuteDeTensionChaine, 0, col);
			}
			else {
				tableChuteDeTensionTotaleDCMaximumARespecter.setValueAt("", 0, col);
			}
			double chuteDeTensionCablePrincipal = vide;
			if (tableChuteDeTensionMaximumARespecter.getValueAt(4, col) != null && !tableChuteDeTensionMaximumARespecter.getValueAt(4, col).toString().equals("")) {
				chuteDeTensionCablePrincipal = Double.parseDouble(tableChuteDeTensionMaximumARespecter.getValueAt(4, col).toString());
				tableChuteDeTensionTotaleDCMaximumARespecter.setValueAt(tableChuteDeTensionMaximumARespecter.getValueAt(4, col).toString(), 1, col);
			}
			else {
				tableChuteDeTensionTotaleDCMaximumARespecter.setValueAt("", 1, col);
			}
			double resultat = vide;
			if (chuteDeTensionChaine != vide && chuteDeTensionCablePrincipal != vide) {
				resultat = arrondirDouble(chuteDeTensionChaine + chuteDeTensionCablePrincipal, 3);
				tableChuteDeTensionTotaleDCMaximumARespecter.setValueAt(resultat, 2, col);
			}
			else {
				tableChuteDeTensionTotaleDCMaximumARespecter.setValueAt("", 2, col);
			}
			tableChuteDeTensionTotaleDCMaximumARespecter.getColumnModel().getColumn(col).setCellRenderer(new CustomRendererTableChuteDeTensionTotaleDCMaximumARespecter(resultat));
		}
	    tableChuteDeTensionTotaleDCMaximumARespecter.repaint();
	}
	
	/**
	 * Met à jour la table {@code tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff} avec les valeurs de chute de tension entre l'onduleur et le disjoncteur pour chaque entrée.
	 * 
	 * <p>Pour chaque colonne, la méthode calcule et met à jour les valeurs suivantes :</p>
	 * <ul>
	 *     <li><b>Résistance du câble :</b> Déterminée en fonction du type de câble (Cuivre ou Aluminium) récupéré depuis la table. La valeur de résistance est utilisée pour les calculs ultérieurs.</li>
	 *     <li><b>Chute de tension :</b> Calculée à partir des valeurs de résistance, longueur du câble, courant et section du câble selon la formule : {@code chuteDeTension = b * (rho * L / S * 1) * Ib}. La valeur est arrondie à trois décimales et affichée dans la table.</li>
	 *     <li><b>Pourcentage de chute de tension :</b> Calculé comme le pourcentage de la chute de tension par rapport à la tension totale disponible, arrondi à trois décimales. La plus grande valeur est conservée dans {@code doubleChuteTensionAC1}.</li>
	 * </ul>
	 * 
	 * @see #tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff
	 * @see #arrondirDouble(double, int)
	 * @see #IACmaxOnduleurs
	 * @see #doubleChuteTensionAC1
	 */
	public static void MAJtableChuteDeTensionEntreOnduleurEtDisjoncteurDiff() {
		chuteTensionAC = "";
		doubleChuteTensionAC1 = vide;
		for (int col = 1; col < nbOnduleurs + 1; col++) {
			double rho = vide;
			if (tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(2, col) != null  && !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(2, col).toString().equals("")) {
				if (tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(2, col).toString().equals("Cuivre")) {
					rho = 0.023;
				}
				else if (tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(2, col).toString().equals("Aluminium")) {
					rho = 0.037;
				}
			}
			if (!IACmaxOnduleurs.isEmpty()) {
				tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setValueAt(IACmaxOnduleurs.get(col - 1), 3, col);
				if (
						rho != vide 
						&& tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(0, col) != null 
						&& !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(0, col).toString().equals("")
						&& tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(1, col) != null 
						&& !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(1, col).toString().equals("")
						&& tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(3, col) != null 
						&& !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(3, col).toString().equals("")
						&& tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(4, col) != null 
						&& !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(4, col).toString().equals("")
					)
				{
					double b = Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(0, col).toString());
					double L = Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(1, col).toString());
					double Ib = Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(3, col).toString());
					double S = Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(4, col).toString());
					tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setValueAt(arrondirDouble(b * (rho * L / S * 1) * Ib, 3), 6, col);
				}
				else {
					tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setValueAt("", 6, col);
				}
			}
			else {
				tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setValueAt("", 6, col);
			}
			if (
					tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(5, col) != null 
					&& !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(5, col).toString().equals("")
					&& tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(6, col) != null 
					&& !tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(6, col).toString().equals("")
				)
			{
				tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setValueAt(arrondirDouble(100 * Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(6, col).toString()) / 
						Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(5, col).toString()), 3), 7, col);
				if (Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(7, col).toString()) > doubleChuteTensionAC1) {
					doubleChuteTensionAC1 = Double.parseDouble(tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.getValueAt(7, col).toString());
				}
			}
			else {
				tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff.setValueAt("", 7, col);
			}
		}
		
	}
	
	/**
	 * Met à jour la table {@code tableChuteDeTensionEntreDisjoncteurDiffEtAGCP} avec les valeurs de chute de tension entre le disjoncteur différentiel et l'AGCP pour chaque entrée.
	 * 
	 * <p>La méthode effectue les opérations suivantes :</p>
	 * <ul>
	 *     <li><b>Calcul de la résistance du câble :</b> Déterminée en fonction du type de câble (Cuivre ou Aluminium) récupéré depuis la table. La valeur de résistance est utilisée pour les calculs ultérieurs.</li>
	 *     <li><b>Calcul de la chute de tension :</b> Calculée à partir des valeurs de résistance, longueur du câble, courant et section du câble selon la formule : {@code chuteDeTension = b * (rho * L / S * 1) * Ib}. La valeur est arrondie à trois décimales et affichée dans la table.</li>
	 *     <li><b>Pourcentage de chute de tension :</b> Calculé comme le pourcentage de la chute de tension par rapport à la tension totale disponible, arrondi à trois décimales. La valeur est conservée dans {@code doubleChuteTensionAC2}.</li>
	 * </ul>
	 * 
	 * @see #tableChuteDeTensionEntreDisjoncteurDiffEtAGCP
	 * @see #arrondirDouble(double, int)
	 * @see #IACmaxOnduleurs
	 * @see #doubleChuteTensionAC2
	 */
	public static void MAJtableChuteDeTensionEntreDisjoncteurDiffEtAGCP() {
		doubleChuteTensionAC2 = vide;
		double rho = vide;
		if (tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(2, 1) != null  && !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(2, 1).toString().equals("")) {
			if (tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(2, 1).toString().equals("Cuivre")) {
				rho = 0.023;
			}
			else if (tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(2, 1).toString().equals("Aluminium")) {
				rho = 0.037;
			}
		}
		if (!IACmaxOnduleurs.isEmpty()) {
			int sommeIACmaxOnduleurs = 0;
			for (int i = 0; i < IACmaxOnduleurs.size(); i++) {
				sommeIACmaxOnduleurs += IACmaxOnduleurs.get(i);
			}
			tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setValueAt(sommeIACmaxOnduleurs, 3, 1);
			if (
					rho != vide 
					&& tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(0, 1) != null 
					&& !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(0, 1).toString().equals("")
					&& tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(1, 1) != null 
					&& !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(1, 1).toString().equals("")
					&& tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(3, 1) != null 
					&& !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(3, 1).toString().equals("")
					&& tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(4, 1) != null 
					&& !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(4, 1).toString().equals("")
				)
			{
				double b = Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(0, 1).toString());
				double L = Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(1, 1).toString());
				double Ib = Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(3, 1).toString());
				double S = Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(4, 1).toString());
				tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setValueAt(arrondirDouble(b * (rho * L / S * 1) * Ib, 3), 6, 1);
			}
			else {
				tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setValueAt("", 6, 1);
			}
		}
		else {
			tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setValueAt("", 6, 1);
		}
		if (
				tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(5, 1) != null 
				&& !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(5, 1).toString().equals("")
				&& tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(6, 1) != null 
				&& !tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(6, 1).toString().equals("")
			)
		{
			tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setValueAt(arrondirDouble(100 * Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(6, 1).toString()) / 
					Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(5, 1).toString()), 3), 7, 1);
			doubleChuteTensionAC2 = Double.parseDouble(tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.getValueAt(7, 1).toString());
		}
		else {
			tableChuteDeTensionEntreDisjoncteurDiffEtAGCP.setValueAt("", 7, 1);
		}
	}
	
	/**
	 * Calcule si la protection contre la foudre est obligatoire pour le courant alternatif (AC) en fonction de la densité locale de foudroiement.
	 * 
	 * <p>La méthode utilise la valeur de {@code NgAC} pour déterminer si la protection est obligatoire :</p>
	 * <ul>
	 *     <li>Si {@code NgAC} est inférieur à 2,5, la protection n'est pas obligatoire.</li>
	 *     <li>Si {@code NgAC} est supérieur ou égal à 2,5, la protection est obligatoire.</li>
	 * </ul>
	 * 
	 * @see #NgAC
	 * @see #protectionParafoudreObligatoireAC
	 */
	public static void calculerProtectionParafoudreObligatoireAC() {
		if (NgAC != vide) {
			protectionParafoudreObligatoireAC = NgAC < 2.5 ? "Densité locale de foudroiement < 2,5 donc non-obligatoire" : "Densité locale de foudroiement > 2,5 donc obligatoire";
		}
		else {
			protectionParafoudreObligatoireAC = "";
		}
	}
	
	/**
	 * Calcule la chute de tension totale en courant alternatif (AC) et génère un message indiquant si elle est acceptable ou non.
	 * 
	 * <p>La méthode calcule la somme des chutes de tension {@code doubleChuteTensionAC1} et {@code doubleChuteTensionAC2}. Ensuite, elle vérifie si la chute de tension totale est inférieure à 3% :</p>
	 * <ul>
	 *     <li>Si la chute de tension est inférieure à 1,5%, le message indique que la chute est acceptable et proche de 1%.</li>
	 *     <li>Si la chute de tension est entre 1,5% et 3%, le message indique que la chute est acceptable mais pas proche de 1%.</li>
	 *     <li>Si la chute de tension est supérieure à 3%, le message indique que la chute est inacceptable.</li>
	 * </ul>
	 * 
	 * @see #doubleChuteTensionAC1
	 * @see #doubleChuteTensionAC2
	 * @see #chuteTensionAC
	 */
	public static void calculerChuteTensionAC() {
		if (doubleChuteTensionAC1 != vide && doubleChuteTensionAC2 != vide) {
			double resultat = doubleChuteTensionAC1 + doubleChuteTensionAC2;
			if (resultat < 3.0) {
				if (resultat < 1.5) {
					chuteTensionAC = doubleChuteTensionAC1 + " + " + doubleChuteTensionAC2 + " = " + resultat + " < 3% et proche de 1%, donc OK";
				}
				else {
					chuteTensionAC = doubleChuteTensionAC1 + " + " + doubleChuteTensionAC2 + " = " + resultat + " < 3% mais pas proche de 1%";
				}
			}
			else {
				chuteTensionAC = doubleChuteTensionAC1 + " + " + doubleChuteTensionAC2 + " = " + resultat + " > 3%, donc NON";
			}
		}
		else {
			chuteTensionAC = "";
		}
	}
	
	/**
	 * Met à jour tous les champs de texte de l'interface utilisateur avec les valeurs des variables correspondantes.
	 */ 
	private static void remettreLesValeursDeTousLesTexteField() {
	    ZonesDeTexte.setRappelUocTmin(String.valueOf(rappelUocTmin));
	    ZonesDeTexte.setRappelIscSTC(String.valueOf(rappelIscSTC));
	    ZonesDeTexte.setRappelUmppSTC(String.valueOf(rappelUmppSTC));
	    ZonesDeTexte.setRappelImppSTC(String.valueOf(rappelImppSTC));
	    ZonesDeTexte.setProtectionParafoudreObligatoireDC(protectionParafoudreObligatoireDC);
	    ZonesDeTexte.setNkDC(String.valueOf(NkDC));
	    ZonesDeTexte.setNgDC(String.valueOf(NgDC));
	    ZonesDeTexte.setConstantePourLcrit(String.valueOf(constantePourLcrit));
	    ZonesDeTexte.setLcrit(String.valueOf(Lcrit));
	    ZonesDeTexte.setLtotale(String.valueOf(Ltotale));
	    ZonesDeTexte.setSecondParafoudreDC(secondParafoudreDC);
	    ZonesDeTexte.setNkAC(String.valueOf(NkAC));
	    ZonesDeTexte.setNgAC(String.valueOf(NgAC));
	    ZonesDeTexte.setProtectionParafoudreObligatoireAC(protectionParafoudreObligatoireAC);
	    ZonesDeTexte.setLettreSelectionCablesChaine(lettreSelectionCablesChaine);
	    ZonesDeTexte.setK1CablesChaine(String.valueOf(K1CablesChaine));
	    ZonesDeTexte.setK2CablesChaine(String.valueOf(K2CablesChaine));
	    ZonesDeTexte.setK3CablesChaine(String.valueOf(K3CablesChaine));
	    ZonesDeTexte.setKCablesChaine(String.valueOf(KCablesChaine));
	    ZonesDeTexte.setLettreSelectionCablePrincipal(lettreSelectionCablePrincipal);
	    ZonesDeTexte.setK1CablePrincipal(String.valueOf(K1CablePrincipal));
	    ZonesDeTexte.setK2CablePrincipal(String.valueOf(K2CablePrincipal));
	    ZonesDeTexte.setK3CablePrincipal(String.valueOf(K3CablePrincipal));
	    ZonesDeTexte.setKCablePrincipal(String.valueOf(KCablePrincipal));
	    ZonesDeTexte.setChuteTensionAC(chuteTensionAC);
	}
	
	/**
	 * Récupère toutes les valeurs des champs de texte de l'interface utilisateur et les stocke dans les variables correspondantes.
	 */
	private static void recupererLesValeursDeTousLesTextField() {
		Irm = Double.parseDouble(ZonesDeTexte.getIrm());
		rappelUocTmin = Double.parseDouble(ZonesDeTexte.getRappelUocTmin());
		rappelIscSTC = Double.parseDouble(ZonesDeTexte.getRappelIscSTC());
		rappelUmppSTC = Double.parseDouble(ZonesDeTexte.getRappelUmppSTC());
		rappelImppSTC = Double.parseDouble(ZonesDeTexte.getRappelImppSTC());
		protectionParafoudreObligatoireDC = ZonesDeTexte.getProtectionParafoudreObligatoireDC();
		NkDC = Double.parseDouble(ZonesDeTexte.getNkDC());
		NgDC = Double.parseDouble(ZonesDeTexte.getNgDC());
		constantePourLcrit = Double.parseDouble(ZonesDeTexte.getConstantePourLcrit());
		Lcrit = Double.parseDouble(ZonesDeTexte.getLcrit());
		Ltotale = Double.parseDouble(ZonesDeTexte.getLtotale());
		Uw = Double.parseDouble(ZonesDeTexte.getUw());
		secondParafoudreDC = ZonesDeTexte.getSecondParafoudreDC();
		NkAC = Double.parseDouble(ZonesDeTexte.getNkAC());
		NgAC = Double.parseDouble(ZonesDeTexte.getNgAC());
		protectionParafoudreObligatoireAC = ZonesDeTexte.getProtectionParafoudreObligatoireAC();
		lettreSelectionCablesChaine = ZonesDeTexte.getLettreSelectionCablesChaine();
		K1CablesChaine = Double.parseDouble(ZonesDeTexte.getK1CablesChaine());
		K2CablesChaine = Double.parseDouble(ZonesDeTexte.getK2CablesChaine());
		K3CablesChaine = Double.parseDouble(ZonesDeTexte.getK3CablesChaine());
		KCablesChaine = Double.parseDouble(ZonesDeTexte.getKCablesChaine());
		lettreSelectionCablePrincipal = ZonesDeTexte.getLettreSelectionCablePrincipal();
		K1CablePrincipal = Double.parseDouble(ZonesDeTexte.getK1CablePrincipal());
		K2CablePrincipal = Double.parseDouble(ZonesDeTexte.getK2CablePrincipal());
		K3CablePrincipal = Double.parseDouble(ZonesDeTexte.getK3CablePrincipal());
		KCablePrincipal = Double.parseDouble(ZonesDeTexte.getKCablePrincipal());
	}
	
	/**
	 * Récupère toutes les tables nécessaires à partir de la classe {@code Tables} et les stocke dans les variables correspondantes.
	 */
	private static void recupererToutesLesTables() {
	    tableChoixDeFusible = Tables.getTableChoixDeFusible();
	    tableDesDifferentsCables = Tables.getTableDesDifferentsCables();
	    tableL = Tables.getTableL();
	    tableParafoudreDC = Tables.getTableParafoudreDC();
	    tableInterrupteursSectionneurs = Tables.getTableInterrupteursSectionneurs();
	    tableDisjoncteursACAssociesAuxOnduleurs = Tables.getTableDisjoncteursACAssociesAuxOnduleurs();
	    tableDisjoncteurACGeneral = Tables.getTableDisjoncteurACGeneral();
	    tableParafoudreAC = Tables.getTableParafoudreAC();
	    tableDisjoncteurAssocieAuParafoudreAC = Tables.getTableDisjoncteurAssocieAuParafoudreAC();
	    tableNcNpIz = Tables.getTableNcNpIz();
	    tableChoixCablesDeChaine = Tables.getTableChoixCablesDeChaine();
	    tableChuteDeTension1 = Tables.getTableChuteDeTension1();
	    tableChuteDeTension2 = Tables.getTableChuteDeTension2();
	    tableCablePrincipal1 = Tables.getTableCablePrincipal1();
	    tableCablePrincipal2 = Tables.getTableCablePrincipal2();
	    tableChuteDeTensionMaximumARespecter = Tables.getTableChuteDeTensionMaximumARespecter();
	    tableChuteDeTensionTotaleDCMaximumARespecter = Tables.getTableChuteDeTensionTotaleDCMaximumARespecter();
	    tableChuteDeTensionEntreOnduleurEtDisjoncteurDiff = Tables.getTableChuteDeTensionEntreOnduleurEtDisjoncteurDiff();
	    tableChuteDeTensionEntreDisjoncteurDiffEtAGCP = Tables.getTableChuteDeTensionEntreDisjoncteurDiffEtAGCP();
	}

	/**
	 * Retourne les noms des onduleurs disponibles sous forme de tableau de chaînes de caractères.
	 * 
	 * <p>Si la liste des noms d'onduleurs est vide, retourne un tableau contenant un nom d'onduleur de test.</p>
	 * 
	 * @return Un tableau de chaînes de caractères contenant les noms des onduleurs.
	 */
	public static String[] getNomsOnduleurs() {
		String[] noms;
		if (nomsOnduleurs.isEmpty()) {
			noms = new String[2];
			noms[0] = "";
			noms[1] = "Nom d'onduleur de test 1";
		}
		else {
			noms = new String[1 + nomsOnduleurs.size()];
			noms[0] = "";
			for (int i = 0; i < nomsOnduleurs.size(); i++) {
				noms[i + 1] = nomsOnduleurs.get(i);
			}
		}
		return noms;
	}
	
	/**
	 * Retourne les noms des entrées des onduleurs disponibles sous forme de tableau de chaînes de caractères.
	 * 
	 * <p>Si la liste des noms des entrées est vide, retourne un tableau contenant un nom de chaîne d'onduleur de test.</p>
	 * 
	 * @return Un tableau de chaînes de caractères contenant les noms des entrées des onduleurs.
	 */
	public static String[] getNomsEntrees() {
		String[] nomsEntrees;
		if (nomsEntreesOnduleurs.isEmpty()) {
			nomsEntrees = new String[2];
			nomsEntrees[0] = "";
			nomsEntrees[1] = "Nom de chaîne d'onduleur de test 1";
		}
		else {
			nomsEntrees = new String[1 + nomsEntreesOnduleurs.size()];
			nomsEntrees[0] = "";
			for (int i = 0; i < nomsEntreesOnduleurs.size(); i++) {
				nomsEntrees[i + 1] = nomsEntreesOnduleurs.get(i);
			}
		}
		return nomsEntrees;
	}
	
	/**
	 * Crée une nouvelle instance de la base de données {@code BDD}.
	 * 
	 * <p>Cette méthode initialise la variable {@code bdd} en créant un nouvel objet {@code BDD}.</p>
	 */
	public static void creerBDD() {
		bdd = new BDD();
	}
	
	/**
	 * Retourne l'instance actuelle de la base de données {@code BDD}.
	 * 
	 * @return L'objet {@code BDD} actuellement utilisé.
	 */
	public static BDD getBDD() {
		return bdd;
	}
	
	/**
	 * Retourne le nombre d'onduleurs.
	 * 
	 * @return Le nombre d'onduleurs.
	 */
	public static int getNbOnduleurs() {
		return nbOnduleurs;
	}
	
	/**
	 * Retourne le nombre d'entrées d'onduleurs.
	 * 
	 * @return Le nombre d'entrées.
	 */
	public static int getNbEntrees() {
		return nbEntrees;
	}
	
	/**
	 * Calcule la somme des éléments d'une liste de nombres réels.
	 * 
	 * <p>Cette méthode additionne tous les éléments non nuls de la liste {@code list} et retourne la somme.</p>
	 * 
	 * @param list La liste des nombres réels dont il faut calculer la somme.
	 * @return La somme des éléments non nuls de la liste.
	 */
	public static double sommeList(List<Double> list) {
        double sum = 0.0;
        for (Double number : list) {
            if (number != null) {
                sum += number;
            }
        }
        return sum;
    }
	
}
