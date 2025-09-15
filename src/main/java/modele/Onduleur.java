package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code Onduleur} représente un onduleur avec diverses propriétés techniques et configurations.
 * <p>
 * Elle fournit des méthodes pour accéder aux caractéristiques de l'onduleur, telles que la puissance maximale, les entrées, et les courants maximaux.
 * </p>
 */
public class Onduleur {
    
    private String nom;  // Nom de l'onduleur
    private double PDCmax;  // Puissance DC maximale en kW
    private double U_DCmax;  // Tension DC maximale en V
    private String plageUMPP;  // Plage de tension MPP en V
    private double plageUMPPBasse;  // Plage de tension MPP basse en V
    private double plageUMPPHaute;  // Plage de tension MPP haute en V
    private List<Double> I_DCmax;  // Courant DC maximal par entrée en A
    private List<Double> I_DCmaxParString;  // Courant DC maximal par string en A
    private int nbEntreesMPPIndependantes;  // Nombre d'entrées MPP indépendantes
    private List<String> entrees;  // Noms des entrées
    private List<Integer> chainesParEntreeMPP;  // Nombre de chaînes par entrée MPP
    private double PACmax;  // Puissance AC maximale en kW
    private double IACmax;  // Courant AC maximal en A
    private boolean uniqueEntree;  // Indique si l'onduleur a une entrée unique
    
    /**
     * Construit une nouvelle instance de {@code Onduleur} avec les paramètres spécifiés.
     * <p>
     * Ce constructeur est utilisé lorsque l'onduleur a plusieurs entrées et chaînes.
     * </p>
     *
     * @param nom le nom de l'onduleur
     * @param PDCmax la puissance DC maximale en kW
     * @param U_DCmax la tension DC maximale en V
     * @param plageUMPP la plage de tension MPP en V sous la forme "min-max"
     * @param I_DCmax les courants DC maximaux pour chaque entrée sous la forme "entrée1:valeur1/entrée2:valeur2"
     * @param I_DCmaxParString les courants DC maximaux par string sous la forme "valeur1/valeur2"
     * @param nbEntreesMPPIndependantes le nombre d'entrées MPP indépendantes
     * @param chainesParEntreeMPP le nombre de chaînes par entrée MPP sous la forme "entrée1:valeur1/entrée2:valeur2"
     * @param PACmax la puissance AC maximale en kW
     * @param IACmax le courant AC maximal en A
     */
    public Onduleur(
            String nom, 
            double PDCmax, 
            double U_DCmax, 
            String plageUMPP,
            String I_DCmax,
            String I_DCmaxParString,
            int nbEntreesMPPIndependantes,
            String chainesParEntreeMPP,
            double PACmax,
            double IACmax
            ) {
        this.uniqueEntree = false;
        this.nom = nom;
        this.PDCmax = PDCmax;
        this.U_DCmax = U_DCmax;
        this.plageUMPP = plageUMPP;
        this.plageUMPPBasse = Double.parseDouble(plageUMPP.split("-")[0].trim());
        this.plageUMPPHaute = Double.parseDouble(plageUMPP.split("-")[1].trim());
        this.nbEntreesMPPIndependantes = nbEntreesMPPIndependantes;
        this.I_DCmax = new ArrayList<>();
        this.entrees = new ArrayList<>();
        conversionI_DCmax(I_DCmax);
        this.I_DCmaxParString = new ArrayList<>();
        conversionI_DCmaxParString(I_DCmaxParString);
        this.chainesParEntreeMPP = new ArrayList<>();
        conversionChainesParEntreeMPP(chainesParEntreeMPP);
        this.PACmax = PACmax;
        this.IACmax = IACmax;
    }
    
    /**
     * Construit une nouvelle instance de {@code Onduleur} avec les paramètres spécifiés.
     * <p>
     * Ce constructeur est utilisé lorsque l'onduleur a une seule entrée.
     * </p>
     *
     * @param nom le nom de l'onduleur
     * @param PDCmax la puissance DC maximale en kW
     * @param U_DCmax la tension DC maximale en V
     * @param plageUMPP la plage de tension MPP en V sous la forme "min-max"
     * @param I_DCmax le courant DC maximal pour l'entrée unique en A
     * @param I_DCmaxParString le courant DC maximal par string pour l'entrée unique en A
     * @param nbEntreesMPPIndependantes le nombre d'entrées MPP indépendantes
     * @param chainesParEntreeMPP le nombre de chaînes par entrée MPP
     * @param PACmax la puissance AC maximale en kW
     * @param IACmax le courant AC maximal en A
     */
    public Onduleur(
            String nom, 
            double PDCmax, 
            double U_DCmax, 
            String plageUMPP,
            double I_DCmax,
            double I_DCmaxParString,
            int nbEntreesMPPIndependantes,
            int chainesParEntreeMPP,
            double PACmax,
            double IACmax
            ) {
        this.uniqueEntree = true;
        this.nom = nom;
        this.PDCmax = PDCmax;
        this.U_DCmax = U_DCmax;
        this.plageUMPP = plageUMPP;
        this.plageUMPPBasse = Double.parseDouble(plageUMPP.split("-")[0].trim());
        this.plageUMPPHaute = Double.parseDouble(plageUMPP.split("-")[1].trim());
        this.nbEntreesMPPIndependantes = nbEntreesMPPIndependantes;
        this.I_DCmax = new ArrayList<>();
        this.I_DCmax.add(I_DCmax);
        this.entrees = new ArrayList<>();
        this.entrees.add("Entrée unique");
        this.I_DCmaxParString = new ArrayList<>();
        this.I_DCmaxParString.add(I_DCmaxParString);
        this.chainesParEntreeMPP = new ArrayList<>();
        this.chainesParEntreeMPP.add(chainesParEntreeMPP);
        this.PACmax = PACmax;
        this.IACmax = IACmax;
    }
    
    /**
     * Convertit la chaîne de caractères représentant les courants DC maximaux pour chaque entrée en une liste de doubles.
     *
     * @param IDC la chaîne de caractères représentant les courants DC maximaux
     */
    private void conversionI_DCmax(String IDC) {
        String[] parties = IDC.split("/");
        for (String partie : parties) {
            String[] sousPartie = partie.split(":");
            this.entrees.add(sousPartie[0].trim());
            this.I_DCmax.add(Double.parseDouble(sousPartie[1].trim()));
        }
    }
    
    /**
     * Convertit la chaîne de caractères représentant les courants DC maximaux par string en une liste de doubles.
     *
     * @param IDC la chaîne de caractères représentant les courants DC maximaux par string
     */
    private void conversionI_DCmaxParString(String IDC) {
        String[] parties = IDC.split("/");
        for (String partie : parties) {
            String[] sousPartie = partie.split(":");
            this.I_DCmaxParString.add(Double.parseDouble(sousPartie[1].trim()));
        }
    }
    
    /**
     * Convertit la chaîne de caractères représentant le nombre de chaînes par entrée MPP en une liste d'entiers.
     *
     * @param chainesParEntreeMPP la chaîne de caractères représentant le nombre de chaînes par entrée MPP
     */
    private void conversionChainesParEntreeMPP(String chainesParEntreeMPP) {
        String[] parties = chainesParEntreeMPP.split("/");
        for (String partie : parties) {
            String[] sousPartie = partie.split(":");
            this.chainesParEntreeMPP.add(Integer.parseInt(sousPartie[1].trim()));
        }
    }
    
    /**
     * Obtient le nom de l'onduleur.
     *
     * @return le nom de l'onduleur
     */
    public String getNom() {
        return this.nom;
    }
    
    /**
     * Obtient la puissance DC maximale de l'onduleur.
     *
     * @return la puissance DC maximale en kW
     */
    public double getPDCmax() {
        return this.PDCmax;
    }
    
    /**
     * Obtient la tension DC maximale de l'onduleur.
     *
     * @return la tension DC maximale en V
     */
    public double getU_DCmax() {
        return this.U_DCmax;
    }
    
    /**
     * Obtient la valeur basse de la plage de tension MPP.
     *
     * @return la valeur basse de la plage de tension MPP en V
     */
    public double getPlageUMPPBasse() {
        return this.plageUMPPBasse;
    }
    
    /**
     * Obtient la valeur haute de la plage de tension MPP.
     *
     * @return la valeur haute de la plage de tension MPP en V
     */
    public double getPlageUMPPHaute() {
        return this.plageUMPPHaute;
    }
    
    /**
     * Obtient la plage de tension MPP sous forme de chaîne de caractères.
     *
     * @return la plage de tension MPP en V sous forme de chaîne
     */
    public String getPlageUMPP() {
        return this.plageUMPP;
    }
    
    /**
     * Obtient la liste des noms des entrées de l'onduleur.
     *
     * @return la liste des noms des entrées
     */
    public List<String> getEntreesNoms() {
        return this.entrees;
    }
    
    /**
     * Obtient la liste des courants DC maximaux pour chaque entrée.
     *
     * @return la liste des courants DC maximaux en A
     */
    public List<Double> getI_DCmax() {
        return this.I_DCmax;
    }
    
    /**
     * Obtient la liste des courants DC maximaux par string.
     *
     * @return la liste des courants DC maximaux par string en A
     */
    public List<Double> getI_DCmaxParString() {
        return this.I_DCmaxParString;
    }
    
    /**
     * Obtient le nombre d'entrées MPP indépendantes de l'onduleur.
     *
     * @return le nombre d'entrées MPP indépendantes
     */
    public int getNbEntreesMPPIndependantes() {
        return this.nbEntreesMPPIndependantes;
    }
    
    /**
     * Obtient la liste des nombres de chaînes par entrée MPP.
     *
     * @return la liste des nombres de chaînes par entrée MPP
     */
    public List<Integer> getChainesParEntreeMPP() {
        return this.chainesParEntreeMPP;
    }
    
    /**
     * Obtient la puissance AC maximale de l'onduleur.
     *
     * @return la puissance AC maximale en kW
     */
    public double getPACmax() {
        return this.PACmax;
    }
    
    /**
     * Obtient le courant AC maximal de l'onduleur.
     *
     * @return le courant AC maximal en A
     */
    public double getIACmax() {
        return this.IACmax;
    }
    
    /**
     * Vérifie si l'onduleur a une entrée unique.
     *
     * @return {@code true} si l'onduleur a une entrée unique, sinon {@code false}
     */
    public boolean getUniqueEntree() {
        return this.uniqueEntree;
    }
}
