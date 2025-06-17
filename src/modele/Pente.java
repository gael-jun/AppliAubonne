package modele;

/**
 * La classe {@code Pente} représente une pente avec une gouttière et un rampant.
 * <p>
 * Elle fournit des méthodes pour obtenir les dimensions de la gouttière et du rampant.
 * </p>
 */
public class Pente {
    
    private double dimGouttiere;  // Dimension de la gouttière
    private double dimRampant;    // Dimension du rampant
    
    /**
     * Construit une nouvelle instance de {@code Pente} avec les dimensions spécifiées pour la gouttière et le rampant.
     * <p>
     * Ce constructeur ne calcule pas l'inclinaison, elle doit être calculée séparément.
     * </p>
     *
     * @param dimG la dimension de la gouttière
     * @param dimR la dimension du rampant
     */
    public Pente(double dimG, double dimR) {
        this.dimGouttiere = dimG;
        this.dimRampant = dimR;
    }
    
    /**
     * Obtient la dimension de la gouttière.
     *
     * @return la dimension de la gouttière
     */
    public double getDimGouttiere() {
        return this.dimGouttiere;
    }
    
    /**
     * Obtient la dimension du rampant.
     *
     * @return la dimension du rampant
     */
    public double getDimRampant() {
        return this.dimRampant;
    }
}
