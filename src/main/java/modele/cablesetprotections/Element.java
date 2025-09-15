package modele.cablesetprotections;

/**
 * La classe abstraite {@code Element} représente un élément de base dans le système de gestion des câbles et protections.
 * Chaque {@code Element} possède une référence unique qui permet de l'identifier.
 * <p>
 * Les sous-classes de {@code Element} étendront cette classe pour ajouter des fonctionnalités spécifiques aux différents types d'éléments.
 * </p>
 */
public abstract class Element {

    private String reference;

    /**
     * Crée une instance de {@code Element} avec une référence spécifiée.
     *
     * @param reference La référence de l'élément, utilisée pour l'identifier de manière unique.
     */
    public Element(String reference) {
        this.reference = reference;
    }

    /**
     * Obtient la référence de cet élément.
     *
     * @return La référence de l'élément.
     */
    public String getReference() {
        return this.reference;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de cet élément.
     * <p>
     * La représentation est simplement la référence de l'élément.
     * </p>
     *
     * @return La chaîne de caractères représentant l'élément.
     */
    @Override
    public String toString() {
        return this.reference;
    }
}
