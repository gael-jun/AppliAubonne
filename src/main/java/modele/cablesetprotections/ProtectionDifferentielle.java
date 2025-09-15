package modele.cablesetprotections;

/**
 * La classe {@code ProtectionDifferentielle} représente un dispositif de protection différentielle.
 * <p>
 * Elle hérite de {@code Element} et contient des informations spécifiques sur la sensibilité
 * du dispositif et son type.
 * </p>
 */
public class ProtectionDifferentielle extends Element {

    /**
     * La sensibilité du dispositif de protection différentielle en milliampères (mA).
     */
    private double sensibiliteDDR;

    /**
     * Le type de protection différentielle.
     */
    private String type;

    /**
     * Construit une nouvelle instance de {@code ProtectionDifferentielle}.
     *
     * @param reference La référence unique de l'élément.
     * @param sensibiliteDDR La sensibilité du dispositif en milliampères (mA).
     * @param type Le type de protection différentielle.
     */
    public ProtectionDifferentielle(String reference, double sensibiliteDDR, String type) {
        super(reference);
        this.sensibiliteDDR = sensibiliteDDR;
        this.type = type;
    }

    /**
     * Obtient la sensibilité du dispositif de protection différentielle.
     *
     * @return La sensibilité en milliampères (mA).
     */
    public double getSensibiliteDDR() {
        return this.sensibiliteDDR;
    }

    /**
     * Obtient le type de protection différentielle.
     *
     * @return Le type de protection.
     */
    public String getType() {
        return this.type;
    }
}
