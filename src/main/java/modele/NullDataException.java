package modele;

/**
 * Exception lancée lorsqu'une donnée requise est nulle ou manquante.
 * <p>
 * Cette exception est utilisée pour signaler des erreurs liées à des données
 * qui devraient être présentes mais qui sont nulles ou absentes dans les 
 * contextes de traitement de données.
 * </p>
 */
public class NullDataException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de {@code NullDataException} avec le message spécifié.
     *
     * @param message le message d'erreur décrivant la cause de l'exception
     */
    public NullDataException(String message) {
        super(message);
    }
}
