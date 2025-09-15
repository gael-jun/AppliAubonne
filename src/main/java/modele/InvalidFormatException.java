package modele;

/**
 * Exception levée lorsqu'un format de donnée est invalide.
 * <p>
 * Cette exception est utilisée pour signaler des erreurs liées à des formats incorrects
 * dans les données fournies ou traitées par les classes du package {@code modele}.
 * </p>
 * 
 * @see Exception
 */
public class InvalidFormatException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de l'exception.
     * 
     * @param message Le message décrivant l'erreur. Il peut être utilisé pour fournir plus de détails
     *                sur la nature de l'erreur de format.
     */
    public InvalidFormatException(String message) {
        super(message);
    }
}
