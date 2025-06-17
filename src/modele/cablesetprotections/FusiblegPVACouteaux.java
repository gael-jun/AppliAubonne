package modele.cablesetprotections;

/**
 * La classe {@code FusiblegPVACouteaux} représente un fusible de type "PVA" avec des couteaux,
 * dérivée de la classe {@code Fusible}.
 * <p>
 * Ce type de fusible est spécifique pour les applications en courant alternatif (AC) et 
 * peut être utilisé pour des protections spécifiques où le design à couteaux est requis.
 * </p>
 */
public class FusiblegPVACouteaux extends Fusible {

    /**
     * Initialise une nouvelle instance de {@code FusiblegPVACouteaux} avec une référence et un calibre spécifié.
     *
     * @param reference La référence unique du fusible.
     * @param In Le calibre du fusible.
     * @throws NullPointerException si {@code reference} est {@code null}.
     * @throws IllegalArgumentException si {@code In} est inférieur ou égal à zéro.
     */
    public FusiblegPVACouteaux(String reference, double In) {
        super(reference, In);
    }
}
