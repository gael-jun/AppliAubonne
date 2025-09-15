package controleur;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import main.Main;
import modele.Calcul;
import modele.cablesetprotections.CalculCablesEtProtections;
import vue.PageSurface;
import vue.Exportation;
import vue.PageOnduleur;

/**
 * La classe {@code Controleur} gère les événements déclenchés par les boutons
 * de l'interface utilisateur.
 */
public class Controleur implements ActionListener {

	private static boolean anciennePageCablesEtProtections = false;
	
    /**
     * Constructeur par défaut de la classe {@code Controleur}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code Controleur}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public Controleur() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
	
    /**
     * Met à jour l'état de la variable `anciennePageCablesEtProtections`.
     * 
     * @param b la nouvelle valeur de l'état de `anciennePageCablesEtProtections`
     */
    public static void setAnciennePageCablesEtProtections(boolean b) {
    	anciennePageCablesEtProtections = b;
    }
    
    /**
     * Réagit aux actions des boutons et déclenche les actions appropriées.
     *
     * @param e l'évènement d'action déclenché
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object concerne = e.getSource();
        if (concerne instanceof JButton) {
            JButton button = (JButton) concerne;
            switch (button.getText()) {
                case "Surface":
                    Main.fenetrePrincipale.showSurface();
                    break;
                case "Puissance":
                    Main.fenetrePrincipale.showPuissance();
                    break;
                case "Onduleur":
                    Main.fenetrePrincipale.showOnduleur();
                    break;
                case "Câbles_Protections":
                	if (besoinDeRechargerPageCablesProtections()) {
                    	Main.fenetrePrincipale.reloadPageCablesProtections();
                	}
                	if (PageOnduleur.isPageCablesEtProtectionsOuvrable()) {
                        Main.fenetrePrincipale.showCablesProtections();
                	}
                	else {
                	    JOptionPane.showMessageDialog(null, "Compléter la partie Onduleur avant d'accéder à cette partie", "Attention", JOptionPane.WARNING_MESSAGE);
                	}
                    break;
                case "Calculer":
                    handleCalculerAction();
                    break;
                case "Exporter":
                	Exportation.exporter();
                	break;
                case "Ajouter Pente":
                    PageSurface.ajouterPente();
                    break;
                case "Retirer Pente":
                    PageSurface.retirerPente();
                    break;
                case "Ajouter Onduleur":
                    PageOnduleur.ajouterOnduleur();
                    break;
                case "Retirer Onduleur":
                    PageOnduleur.retirerOnduleur();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     * Vérifie si la page "Câbles_Protections" doit être rechargée.
     * 
     * @return {@code true} si la page doit être rechargée, {@code false} sinon
     */
    public static boolean besoinDeRechargerPageCablesProtections() {
    	boolean ouiOuNon = false;
    	if (PageOnduleur.isPageCablesEtProtectionsOuvrable() == true && anciennePageCablesEtProtections == false) {
    		ouiOuNon = true;
    	}
    	anciennePageCablesEtProtections = PageOnduleur.isPageCablesEtProtectionsOuvrable();
    	return ouiOuNon;
    }

    /**
     * Gère l'action du bouton "Calculer" en fonction de la page courante.
     */
    private void handleCalculerAction() {
        String carteCourante = Main.fenetrePrincipale.getCarteCourante();
        if (carteCourante.equals("Surface")) {
            stopEditingOnAllTables(Main.fenetrePrincipale.getPageSurface().getPage());
            Calcul.toutCalculerSurface();
        } else if (carteCourante.equals("Puissance")) {
            stopEditingOnAllTables(Main.fenetrePrincipale.getPagePuissance().getPage());
            Calcul.toutCalculerPuissance();
        } else if (carteCourante.equals("Onduleur")) {
            stopEditingOnAllTables(Main.fenetrePrincipale.getPageOnduleur().getPage());
            Calcul.toutCalculerOnduleurs();
        } else if (carteCourante.equals("Câbles_Protections")) {
            stopEditingOnAllTables(Main.fenetrePrincipale.getPageCablesProtections().getPage());
            CalculCablesEtProtections.toutMettreAJour();
        }
    }

    /**
     * Arrête l'édition sur tous les composants {@code JTable} présents 
     * dans un conteneur donné.
     *
     * @param container le conteneur dont les tables doivent arrêter l'édition
     */
    private static void stopEditingOnAllTables(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTable) {
                JTable table = (JTable) comp;
                if (table.isEditing()) {
                    table.getCellEditor().stopCellEditing();
                }
            } else if (comp instanceof Container) {
                stopEditingOnAllTables((Container) comp);
            }
        }
    }

}
