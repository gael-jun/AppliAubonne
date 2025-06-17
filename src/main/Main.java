package main;

import javax.swing.UnsupportedLookAndFeelException;

import controleur.*;
import vue.*;

/**
 * La classe {@code Main} est le point d'entrée de l'application.
 * <p>
 * Elle initialise les composants principaux de l'application : 
 * la fenêtre principale et le contrôleur, puis affiche 
 * l'interface utilisateur.
 * </p>
 */
public class Main {

    /** La fenêtre principale de l'application. */
    public static FenetrePrincipale fenetrePrincipale;
    
    /** Le contrôleur pour gérer les événements de l'application. */
    public static Controleur controleur;
    
    /**
     * Constructeur par défaut de la classe {@code Main}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code Main}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public Main() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
    
    /**
     * Méthode principale qui lance l'application.
     *
     * @param args les arguments de la ligne de commande
     * @throws ClassNotFoundException si la classe LookAndFeel n'est pas trouvée
     * @throws InstantiationException si l'instanciation de la classe LookAndFeel échoue
     * @throws IllegalAccessException si la classe LookAndFeel ou son constructeur est inaccessible
     * @throws UnsupportedLookAndFeelException si LookAndFeel est non supporté
     */
    public static void main(String[] args) 
            throws ClassNotFoundException, InstantiationException, 
                   IllegalAccessException, UnsupportedLookAndFeelException {
        controleur = new Controleur();
        fenetrePrincipale = new FenetrePrincipale();
        fenetrePrincipale.show();
    }
}
