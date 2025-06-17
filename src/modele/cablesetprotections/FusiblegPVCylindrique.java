package modele.cablesetprotections;

/**
 * La classe {@code FusiblegPVCylindrique} représente un fusible de type cylindrique pour les applications en courant alternatif (AC),
 * dérivée de la classe {@code Fusible}.
 * <p>
 * Ce type de fusible est conçu avec une forme cylindrique, ce qui est couramment utilisé dans diverses applications électriques pour une protection fiable.
 * </p>
 */
public class FusiblegPVCylindrique extends Fusible {

    /**
     * Initialise une nouvelle instance de {@code FusiblegPVCylindrique} avec une référence et un calibre spécifié.
     *
     * @param reference La référence unique du fusible.
     * @param In Le calibre du fusible.
     * @throws NullPointerException si {@code reference} est {@code null}.
     * @throws IllegalArgumentException si {@code In} est inférieur ou égal à zéro.
     */
    public FusiblegPVCylindrique(String reference, double In) {
        super(reference, In);
    }
}
