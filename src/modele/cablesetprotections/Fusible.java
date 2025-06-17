package modele.cablesetprotections;

/**
 * La classe abstraite {@code Fusible} représente un fusible dans une collection d'éléments.
 * Elle étend la classe {@code Element} en ajoutant une caractéristique supplémentaire,
 * le calibre du fusible.
 * <p>
 * Les sous-classes de {@code Fusible} peuvent spécifier des types de fusibles plus détaillés
 * tout en utilisant les fonctionnalités de base fournies par {@code Element} et {@code Fusible}.
 * </p>
 */
public abstract class Fusible extends Element {

    private double In; // calibre du fusible
    
    /**
     * Initialise une nouvelle instance de {@code Fusible} avec une référence et un calibre spécifié.
     *
     * @param reference La référence unique du fusible.
     * @param calibre Le calibre du fusible.
     * @throws NullPointerException si {@code reference} est {@code null}.
     * @throws IllegalArgumentException si {@code calibre} est inférieur ou égal à zéro.
     */
    public Fusible(String reference, double calibre) {
        super(reference);
        if (calibre <= 0) {
            throw new IllegalArgumentException("Le calibre doit être supérieur à zéro.");
        }
        this.In = calibre;
    }
    
    /**
     * Obtient le calibre du fusible.
     *
     * @return Le calibre du fusible.
     */
    public double getIn() {
        return this.In;
    }
}
