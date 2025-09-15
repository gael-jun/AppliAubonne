package modele;

/**
 * Classe représentant l'Espace de Fixation et d'Utilisation (EFU) des modules photovoltaïques.
 * <p>
 * L'EFU contient des informations sur les contraintes d'écart minimales imposées dans
 * les directions de la gouttière et du rampant.
 * </p>
 */
public class EFU {
    private String nom;
    private double ecartMinImposeSensGouttiere;
    private double ecartMinImposeSensRampant;
    
    /**
     * Constructeur de l'EFU.
     * 
     * @param nom Le nom de l'EFU.
     * @param ecartMinImposeSensGouttiere L'écart minimum imposé dans le sens de la gouttière.
     * @param ecartMinImposeSensRampant L'écart minimum imposé dans le sens rampant.
     */
    public EFU(String nom, double ecartMinImposeSensGouttiere, double ecartMinImposeSensRampant) {
        this.nom = nom;
        this.ecartMinImposeSensGouttiere = ecartMinImposeSensGouttiere;
        this.ecartMinImposeSensRampant = ecartMinImposeSensRampant;
    }
    
    /**
     * Obtient le nom de l'EFU.
     * 
     * @return Le nom de l'EFU.
     */
    public String getNom() {
        return this.nom;
    }
    
    /**
     * Obtient l'écart minimum imposé dans le sens de la gouttière.
     * 
     * @return L'écart minimum imposé dans le sens de la gouttière.
     */
    public double getEcartMinImposeSensGouttiere() {
        return this.ecartMinImposeSensGouttiere;
    }
    
    /**
     * Obtient l'écart minimum imposé dans le sens rampant.
     * 
     * @return L'écart minimum imposé dans le sens rampant.
     */
    public double getEcartMinImposeSensRampant() {
        return this.ecartMinImposeSensRampant;
    }
}
