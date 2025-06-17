package modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une étude d'une pente pour l'installation de modules photovoltaïques.
 * <p>
 * Cette classe calcule et fournit des informations sur la disposition des modules photovoltaïques
 * en fonction de la pente, des dimensions et des contraintes d'installation.
 * </p>
 */
public class EtudeDUnePente {
    private Module module;
    private Pente pente;
    private EFU efu;
    private double debordMinDansSensGouttiere;
    private double debordMinDansSensRampant;
    private double surface;
    private double Li; // longueur module + intermodule
    private double li; // largeur module + intermodule
    private int nbModulesPortraitSensGouttiere;
    private int nbModulesPortraitSensRampant;
    private int nbModulesPaysageSensGouttiere;
    private int nbModulesPaysageSensRampant;
    private int nbTotalModulesPortait;
    private int nbTotalModulesPaysage;
    private double debordTotalSensGouttierePortrait;
    private double debordTotalSensRampantPortrait;
    private double debordTotalSensGouttierePaysage;
    private double debordTotalSensRampantPaysage;

    /**
     * Constructeur de l'étude d'une pente.
     * 
     * @param module Le module photovoltaïque à étudier.
     * @param pente La pente sur laquelle les modules seront installés.
     * @param efu L'EFU (Espace de Fixation et d'Utilisation) avec les contraintes d'écart.
     * @param debordGoutt Le débord minimal dans le sens de la gouttière.
     * @param debordRamp Le débord minimal dans le sens rampant.
     */
    public EtudeDUnePente(Module module, Pente pente, EFU efu, double debordGoutt, double debordRamp) {
        this.module = module;
        this.pente = pente;
        this.efu = efu;
        this.debordMinDansSensGouttiere = debordGoutt;
        this.debordMinDansSensRampant = debordRamp;
        this.surface = 0;
        this.Li = 0;
        this.li = 0;
        this.nbModulesPortraitSensGouttiere = 0;
        this.nbModulesPortraitSensRampant = 0;
        this.nbModulesPaysageSensGouttiere = 0;
        this.nbModulesPaysageSensRampant = 0;
        this.nbTotalModulesPortait = 0;
        this.nbTotalModulesPaysage = 0;
        this.debordTotalSensGouttierePortrait = 0;
        this.debordTotalSensRampantPortrait = 0;
        this.debordTotalSensGouttierePaysage = 0;
        this.debordTotalSensRampantPaysage = 0;
    }

    /**
     * Réinitialise les attributs de l'étude à zéro.
     */
    public void remiseAZero() {
        this.surface = 0;
        this.Li = 0;
        this.li = 0;
        this.nbModulesPortraitSensGouttiere = 0;
        this.nbModulesPortraitSensRampant = 0;
        this.nbModulesPaysageSensGouttiere = 0;
        this.nbModulesPaysageSensRampant = 0;
        this.nbTotalModulesPortait = 0;
        this.nbTotalModulesPaysage = 0;
        this.debordTotalSensGouttierePortrait = 0;
        this.debordTotalSensRampantPortrait = 0;
        this.debordTotalSensGouttierePaysage = 0;
        this.debordTotalSensRampantPaysage = 0;
    }

    /**
     * Effectue les calculs nécessaires pour l'étude en fonction des dimensions et des contraintes.
     */
    public void calculer() {
        this.remiseAZero();
        
        this.calculerSurface();
        this.calculerLi();
        this.calculerli();
        this.calculerNbModulesPortraitSensGouttiere();
        this.calculerNbModulesPortraitSensRampant();
        this.nbTotalModulesPortait = this.nbModulesPortraitSensGouttiere * this.nbModulesPortraitSensRampant;
        this.debordTotalSensGouttierePortrait = this.pente.getDimGouttiere() - ((this.nbModulesPortraitSensGouttiere * this.li) - this.efu.getEcartMinImposeSensGouttiere());
        this.debordTotalSensRampantPortrait = this.pente.getDimRampant() - ((this.nbModulesPortraitSensRampant * this.Li) - this.efu.getEcartMinImposeSensRampant());
        this.calculerNbModulesPaysageSensGouttiere();
        this.calculerNbModulesPaysageSensRampant();
        this.nbTotalModulesPaysage = this.nbModulesPaysageSensGouttiere * this.nbModulesPaysageSensRampant;
        this.debordTotalSensGouttierePaysage = this.pente.getDimGouttiere() - ((this.nbModulesPaysageSensGouttiere * this.Li) - this.efu.getEcartMinImposeSensGouttiere());
        this.debordTotalSensRampantPaysage = this.pente.getDimRampant() - ((this.nbModulesPaysageSensRampant * this.li) - this.efu.getEcartMinImposeSensRampant());
        
        this.arrondir();
    }

    /**
     * Arrondit les valeurs des attributs à 4 décimales.
     */
    public void arrondir() {
        this.surface = Calcul.arrondirDouble(this.surface, 4);
        this.Li = Calcul.arrondirDouble(this.Li, 4);
        this.li = Calcul.arrondirDouble(this.li, 4);
        this.debordTotalSensGouttierePortrait = Calcul.arrondirDouble(this.debordTotalSensGouttierePortrait, 4);
        this.debordTotalSensRampantPortrait = Calcul.arrondirDouble(this.debordTotalSensRampantPortrait, 4);
        this.debordTotalSensGouttierePaysage = Calcul.arrondirDouble(this.debordTotalSensGouttierePaysage, 4);
        this.debordTotalSensRampantPaysage = Calcul.arrondirDouble(this.debordTotalSensRampantPaysage, 4);
    }

    /**
     * Calcule la surface totale de la pente.
     */
    private void calculerSurface() {
        this.surface = this.pente.getDimGouttiere() * this.pente.getDimRampant();
    }

    /**
     * Calcule la longueur totale d'un module plus l'écart intermodule dans le sens de la gouttière.
     */
    private void calculerLi() {
        this.Li = this.module.getLongueur() + this.efu.getEcartMinImposeSensGouttiere();
    }

    /**
     * Calcule la largeur totale d'un module plus l'écart intermodule dans le sens rampant.
     */
    private void calculerli() {
        this.li = this.module.getLargeur() + this.efu.getEcartMinImposeSensRampant();
    }

    /**
     * Calcule le nombre de modules en orientation portrait dans le sens de la gouttière.
     */
    private void calculerNbModulesPortraitSensGouttiere() {
        this.nbModulesPortraitSensGouttiere = (int) Math.floor((this.pente.getDimGouttiere() - this.debordMinDansSensGouttiere + this.efu.getEcartMinImposeSensGouttiere()) / this.getli());
    }

    /**
     * Calcule le nombre de modules en orientation portrait dans le sens rampant.
     */
    private void calculerNbModulesPortraitSensRampant() {
        this.nbModulesPortraitSensRampant = (int) Math.floor((this.pente.getDimRampant() - this.debordMinDansSensRampant + this.efu.getEcartMinImposeSensRampant()) / this.getLi());
    }

    /**
     * Calcule le nombre de modules en orientation paysage dans le sens de la gouttière.
     */
    private void calculerNbModulesPaysageSensGouttiere() {
        this.nbModulesPaysageSensGouttiere = (int) Math.floor((this.pente.getDimGouttiere() - this.debordMinDansSensGouttiere + this.efu.getEcartMinImposeSensGouttiere()) / this.getLi());
    }

    /**
     * Calcule le nombre de modules en orientation paysage dans le sens rampant.
     */
    private void calculerNbModulesPaysageSensRampant() {
        this.nbModulesPaysageSensRampant = (int) Math.floor((this.pente.getDimRampant() - this.debordMinDansSensRampant + this.efu.getEcartMinImposeSensRampant()) / this.getli());
    }

    /**
     * Obtient le module photovoltaïque associé à l'étude.
     * 
     * @return Le module photovoltaïque.
     */
    public Module getModule() {
        return this.module;
    }

    /**
     * Obtient la pente sur laquelle les modules sont installés.
     * 
     * @return La pente.
     */
    public Pente getPente() {
        return this.pente;
    }

    /**
     * Obtient l'EFU (Espace de Fixation et d'Utilisation) associé à l'étude.
     * 
     * @return L'EFU.
     */
    public EFU getEFU() {
        return this.efu;
    }

    /**
     * Obtient le débord minimal dans le sens de la gouttière.
     * 
     * @return Le débord minimal dans le sens de la gouttière.
     */
    public double getDebordMinDansSensGouttiere() {
        return this.debordMinDansSensGouttiere;
    }

    /**
     * Obtient le débord minimal dans le sens rampant.
     * 
     * @return Le débord minimal dans le sens rampant.
     */
    public double getDebordMinDansSensRampant() {
        return this.debordMinDansSensRampant;
    }

    /**
     * Obtient la surface totale de la pente.
     * 
     * @return La surface totale de la pente.
     */
    public double getSurface() {
        return this.surface;
    }

    /**
     * Obtient la longueur totale d'un module plus l'écart intermodule dans le sens de la gouttière.
     * 
     * @return La longueur totale d'un module plus l'écart intermodule.
     */
    public double getLi() {
        return this.Li;
    }

    /**
     * Obtient la largeur totale d'un module plus l'écart intermodule dans le sens rampant.
     * 
     * @return La largeur totale d'un module plus l'écart intermodule.
     */
    public double getli() {
        return this.li;
    }

    /**
     * Obtient le nombre de modules en orientation portrait dans le sens de la gouttière.
     * 
     * @return Le nombre de modules en orientation portrait dans le sens de la gouttière.
     */
    public int getNbModulesPortraitSensGouttiere() {
        return this.nbModulesPortraitSensGouttiere;
    }

    /**
     * Obtient le nombre de modules en orientation portrait dans le sens rampant.
     * 
     * @return Le nombre de modules en orientation portrait dans le sens rampant.
     */
    public int getNbModulesPortraitSensRampant() {
        return this.nbModulesPortraitSensRampant;
    }

    /**
     * Obtient le nombre de modules en orientation paysage dans le sens de la gouttière.
     * 
     * @return Le nombre de modules en orientation paysage dans le sens de la gouttière.
     */
    public int getNbModulesPaysageSensGouttiere() {
        return this.nbModulesPaysageSensGouttiere;
    }

    /**
     * Obtient le nombre de modules en orientation paysage dans le sens rampant.
     * 
     * @return Le nombre de modules en orientation paysage dans le sens rampant.
     */
    public int getNbModulesPaysageSensRampant() {
        return this.nbModulesPaysageSensRampant;
    }

    /**
     * Obtient le nombre total de modules en orientation portrait.
     * 
     * @return Le nombre total de modules en orientation portrait.
     */
    public int getNbTotalModulesPortrait() {
        return this.nbTotalModulesPortait;
    }

    /**
     * Obtient le nombre total de modules en orientation paysage.
     * 
     * @return Le nombre total de modules en orientation paysage.
     */
    public int getNbTotalModulesPaysage() {
        return this.nbTotalModulesPaysage;
    }

    /**
     * Obtient le débord total dans le sens de la gouttière pour l'orientation portrait.
     * 
     * @return Le débord total dans le sens de la gouttière pour l'orientation portrait.
     */
    public double getDebordTotalSensGouttierePortrait() {
        return this.debordTotalSensGouttierePortrait;
    }

    /**
     * Obtient le débord total dans le sens rampant pour l'orientation portrait.
     * 
     * @return Le débord total dans le sens rampant pour l'orientation portrait.
     */
    public double getDebordTotalSensRampantPortrait() {
        return this.debordTotalSensRampantPortrait;
    }

    /**
     * Obtient le débord total dans le sens de la gouttière pour l'orientation paysage.
     * 
     * @return Le débord total dans le sens de la gouttière pour l'orientation paysage.
     */
    public double getDebordTotalSensGouttierePaysage() {
        return this.debordTotalSensGouttierePaysage;
    }

    /**
     * Obtient le débord total dans le sens rampant pour l'orientation paysage.
     * 
     * @return Le débord total dans le sens rampant pour l'orientation paysage.
     */
    public double getDebordTotalSensRampantPaysage() {
        return this.debordTotalSensRampantPaysage;
    }

    /**
     * Obtient une liste des attributs de l'étude.
     * 
     * @return Une liste d'objets représentant les attributs de l'étude.
     */
    public List<Object> getAttributs() {
        List<Object> attributs = new ArrayList<>();
        attributs.add(this.pente.getDimGouttiere());
        attributs.add(this.pente.getDimRampant());
        attributs.add(this.module.getNom());
        attributs.add(this.module.getLongueur());
        attributs.add(this.module.getLargeur());
        attributs.add(this.module.getPnom());
        attributs.add(this.efu.getNom());
        attributs.add(this.efu.getEcartMinImposeSensGouttiere());
        attributs.add(this.efu.getEcartMinImposeSensRampant());
        attributs.add(this.debordMinDansSensGouttiere);
        attributs.add(this.debordMinDansSensRampant);
        attributs.add(this.surface);
        attributs.add(this.Li);
        attributs.add(this.li);
        attributs.add(this.nbModulesPortraitSensGouttiere);
        attributs.add(this.nbModulesPortraitSensRampant);
        attributs.add(this.debordTotalSensGouttierePortrait);
        attributs.add(this.debordTotalSensRampantPortrait);
        attributs.add(this.nbModulesPaysageSensGouttiere);
        attributs.add(this.nbModulesPaysageSensRampant);
        attributs.add(this.debordTotalSensGouttierePaysage);
        attributs.add(this.debordTotalSensRampantPaysage);
        attributs.add(this.nbTotalModulesPortait);
        attributs.add(this.nbTotalModulesPaysage);
        return attributs;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères des attributs de l'étude.
     * 
     * @return Une chaîne de caractères représentant les attributs de l'étude.
     */
    @Override
    public String toString() {
        return this.getAttributs().toString();
    }
}
