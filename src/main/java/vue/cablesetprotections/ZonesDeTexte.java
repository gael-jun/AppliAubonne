package vue.cablesetprotections;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import modele.cablesetprotections.BDD;
import modele.cablesetprotections.CalculCablesEtProtections;
import modele.cablesetprotections.Fusible;
import vue.PageCablesProtections;

/**
 * La classe {@code ZonesDeTexte} gère les zones de texte utilisées dans l'interface graphique
 * pour les calculs et les paramètres des câbles et protections. Elle fournit des méthodes pour
 * définir les valeurs de ces zones de texte.
 */
public class ZonesDeTexte {

    // Variables pour les composants de l'interface graphique
    private static GridBagConstraints gbc;
    private static String symboleVide = "-1.0";
    private static JComboBox<String> choixCalibreComboBox;
    private static JTextField choixCalibre;
    private static JComboBox<String> choixSectionCablesDeChaineComboBox;
    private static JTextField choixSectionCablesDeChaine;
    private static JComboBox<String> choixSectionCablePrincipalComboBox;
    private static JTextField choixSectionCablePrincipal;
    private static JTextField Irm;
    private static JTextField rappelUocTmin;
    private static JTextField rappelIscSTC;
    private static JTextField rappelImppSTC;
    private static JTextField rappelUmppSTC;
    private static JTextField protectionParafoudreObligatoireDC;
    private static JTextField Uw;
    private static JTextField NkDC;
    private static JTextField NgDC;
    private static JTextField constantePourLcrit;
    private static JTextField Lcrit;
    private static JTextField Ltotale;
    private static JTextField secondParafoudreDC;
    private static JTextField NkAC;
    private static JTextField NgAC;
    private static JTextField protectionParafoudreObligatoireAC;
    private static JTextField lettreSelectionCablesChaine;
    private static JTextField K1CablesChaine;
    private static JTextField K2CablesChaine;
    private static JTextField K3CablesChaine;
    private static JTextField KCablesChaine;
    private static JTextField lettreSelectionCablePrincipal;
    private static JTextField K1CablePrincipal;
    private static JTextField K2CablePrincipal;
    private static JTextField K3CablePrincipal;
    private static JTextField KCablePrincipal;
    private static JTextField chuteTensionAC;

    /**
     * Constructeur par défaut de la classe {@code ZonesDeTexte}.
     * <p>
     * Ce constructeur crée une nouvelle instance de {@code ZonesDeTexte}.
     * Il n'effectue aucune opération particulière lors de l'initialisation.
     * </p>
     */
    public ZonesDeTexte() {
        // Le constructeur par défaut n'a pas besoin d'effectuer des opérations spécifiques
    }
    
    /**
     * Fais pointer la variable {@code gbc} vers le {@code gbc} de {@link PageCablesProtections}.
     */
    public static void setGBC() {
    	gbc = PageCablesProtections.getGBC();
    }
    
    /**
     * Met à jour le texte de la zone de texte pour le calibre choisi.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setChoixCalibre(String texte) {
        if (texte.equals(symboleVide)) {
            choixCalibre.setText("");
        } else {
            choixCalibre.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la section des câbles de chaîne.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setChoixSectionCablesDeChaine(String texte) {
        if (texte.equals(symboleVide)) {
            choixSectionCablesDeChaine.setText("");
        } else {
            choixSectionCablesDeChaine.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la section du câble principal.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void SetChoixSectionCablePrincipal(String texte) {
        if (texte.equals(symboleVide)) {
            choixSectionCablePrincipal.setText("");
        } else {
            choixSectionCablePrincipal.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le courant de court-circuit (Irm).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setIrm(String texte) {
        if (texte.equals(symboleVide)) {
            Irm.setText("");
        } else {
            Irm.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la tension de circuit ouvert minimum (Uoc Tmin).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setRappelUocTmin(String texte) {
        if (texte.equals(symboleVide)) {
            rappelUocTmin.setText("");
        } else {
            rappelUocTmin.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le courant de court-circuit STC (Isc STC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setRappelIscSTC(String texte) {
        if (texte.equals(symboleVide)) {
            rappelIscSTC.setText("");
        } else {
            rappelIscSTC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le courant de puissance maximale STC (Impp STC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setRappelImppSTC(String texte) {
        if (texte.equals(symboleVide)) {
            rappelImppSTC.setText("");
        } else {
            rappelImppSTC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la tension maximale STC (Umpp STC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setRappelUmppSTC(String texte) {
        if (texte.equals(symboleVide)) {
            rappelUmppSTC.setText("");
        } else {
            rappelUmppSTC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la protection parafoudre obligatoire DC.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setProtectionParafoudreObligatoireDC(String texte) {
        if (texte.equals(symboleVide)) {
            protectionParafoudreObligatoireDC.setText("");
        } else {
            protectionParafoudreObligatoireDC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le second parafoudre DC.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setSecondParafoudreDC(String texte) {
        if (texte.equals(symboleVide)) {
            secondParafoudreDC.setText("");
        } else {
            secondParafoudreDC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la tension de l'installation (Uw).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setUw(String texte) {
        if (texte.equals(symboleVide)) {
            Uw.setText("");
        } else {
            Uw.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le nombre de câbles DC (Nk DC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setNkDC(String texte) {
        if (texte.equals(symboleVide)) {
            NkDC.setText("");
        } else {
            NkDC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le nombre de groupes DC (Ng DC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setNgDC(String texte) {
        if (texte.equals(symboleVide)) {
            NgDC.setText("");
        } else {
            NgDC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la constante pour la longueur critique (Lcrit).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setConstantePourLcrit(String texte) {
        if (texte.equals(symboleVide)) {
            constantePourLcrit.setText("");
        } else {
            constantePourLcrit.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la longueur critique (Lcrit).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setLcrit(String texte) {
        if (texte.equals(symboleVide)) {
            Lcrit.setText("");
        } else {
            Lcrit.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la longueur totale (Ltotale).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setLtotale(String texte) {
        if (texte.equals(symboleVide)) {
            Ltotale.setText("");
        } else {
            Ltotale.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le nombre de câbles AC (Nk AC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setNkAC(String texte) {
        if (texte.equals(symboleVide)) {
            NkAC.setText("");
        } else {
            NkAC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le nombre de groupes AC (Ng AC).
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setNgAC(String texte) {
        if (texte.equals(symboleVide)) {
            NgAC.setText("");
        } else {
            NgAC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la protection parafoudre obligatoire AC.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setProtectionParafoudreObligatoireAC(String texte) {
        if (texte.equals(symboleVide)) {
            protectionParafoudreObligatoireAC.setText("");
        } else {
            protectionParafoudreObligatoireAC.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la lettre de sélection des câbles de chaîne.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setLettreSelectionCablesChaine(String texte) {
        if (texte.equals(symboleVide)) {
            lettreSelectionCablesChaine.setText("");
        } else {
            lettreSelectionCablesChaine.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K1 des câbles de chaîne.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setK1CablesChaine(String texte) {
        if (texte.equals(symboleVide)) {
            K1CablesChaine.setText("");
        } else {
            K1CablesChaine.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K2 des câbles de chaîne.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setK2CablesChaine(String texte) {
        if (texte.equals(symboleVide)) {
            K2CablesChaine.setText("");
        } else {
            K2CablesChaine.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K3 des câbles de chaîne.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setK3CablesChaine(String texte) {
        if (texte.equals(symboleVide)) {
            K3CablesChaine.setText("");
        } else {
            K3CablesChaine.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K des câbles de chaîne.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setKCablesChaine(String texte) {
        if (texte.equals(symboleVide)) {
            KCablesChaine.setText("");
        } else {
            KCablesChaine.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la lettre de sélection du câble principal.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setLettreSelectionCablePrincipal(String texte) {
        if (texte.equals(symboleVide)) {
            lettreSelectionCablePrincipal.setText("");
        } else {
            lettreSelectionCablePrincipal.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K1 du câble principal.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setK1CablePrincipal(String texte) {
        if (texte.equals(symboleVide)) {
            K1CablePrincipal.setText("");
        } else {
            K1CablePrincipal.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K2 du câble principal.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setK2CablePrincipal(String texte) {
        if (texte.equals(symboleVide)) {
            K2CablePrincipal.setText("");
        } else {
            K2CablePrincipal.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K3 du câble principal.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setK3CablePrincipal(String texte) {
        if (texte.equals(symboleVide)) {
            K3CablePrincipal.setText("");
        } else {
            K3CablePrincipal.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour le coefficient K du câble principal.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setKCablePrincipal(String texte) {
        if (texte.equals(symboleVide)) {
            KCablePrincipal.setText("");
        } else {
            KCablePrincipal.setText(texte);
        }
    }

    /**
     * Met à jour le texte de la zone de texte pour la chute de tension AC.
     *
     * @param texte le texte à afficher dans la zone de texte.
     */
    public static void setChuteTensionAC(String texte) {
        if (texte.equals(symboleVide)) {
            chuteTensionAC.setText("");
        } else {
            chuteTensionAC.setText(texte);
        }
    }
	
    /**
     * Récupère le texte de la zone de texte pour le calibre choisi.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getChoixCalibre() {
        return choixCalibre.getText().isEmpty() ? symboleVide : choixCalibre.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la section des câbles de chaîne.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getChoixSectionCablesDeChaine() {
        return choixSectionCablesDeChaine.getText().isEmpty() ? symboleVide : choixSectionCablesDeChaine.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la section du câble principal.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getChoixSectionCablePrincipal() {
        return choixSectionCablePrincipal.getText().isEmpty() ? symboleVide : choixSectionCablePrincipal.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le courant de court-circuit (Irm).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getIrm() {
        return Irm.getText().isEmpty() ? symboleVide : Irm.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la tension de circuit ouvert minimum (Uoc Tmin).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getRappelUocTmin() {
        return rappelUocTmin.getText().isEmpty() ? symboleVide : rappelUocTmin.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le courant de court-circuit STC (Isc STC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getRappelIscSTC() {
        return rappelIscSTC.getText().isEmpty() ? symboleVide : rappelIscSTC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le courant de puissance maximale STC (Impp STC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getRappelImppSTC() {
        return rappelImppSTC.getText().isEmpty() ? symboleVide : rappelImppSTC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la tension maximale STC (Umpp STC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getRappelUmppSTC() {
        return rappelUmppSTC.getText().isEmpty() ? symboleVide : rappelUmppSTC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la protection parafoudre obligatoire DC.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getProtectionParafoudreObligatoireDC() {
        return protectionParafoudreObligatoireDC.getText().isEmpty() ? symboleVide : protectionParafoudreObligatoireDC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le second parafoudre DC.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getSecondParafoudreDC() {
        return secondParafoudreDC.getText().isEmpty() ? symboleVide : secondParafoudreDC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la tension de l'installation (Uw).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getUw() {
        return Uw.getText().isEmpty() ? symboleVide : Uw.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le nombre de câbles DC (Nk DC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getNkDC() {
        return NkDC.getText().isEmpty() ? symboleVide : NkDC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le nombre de groupes DC (Ng DC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getNgDC() {
        return NgDC.getText().isEmpty() ? symboleVide : NgDC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la constante pour la longueur critique (Lcrit).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getConstantePourLcrit() {
        return constantePourLcrit.getText().isEmpty() ? symboleVide : constantePourLcrit.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la longueur critique (Lcrit).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getLcrit() {
        return Lcrit.getText().isEmpty() ? symboleVide : Lcrit.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la longueur totale (Ltotale).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getLtotale() {
        return Ltotale.getText().isEmpty() ? symboleVide : Ltotale.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le nombre de câbles AC (Nk AC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getNkAC() {
        return NkAC.getText().isEmpty() ? symboleVide : NkAC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le nombre de groupes AC (Ng AC).
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getNgAC() {
        return NgAC.getText().isEmpty() ? symboleVide : NgAC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la protection parafoudre obligatoire AC.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getProtectionParafoudreObligatoireAC() {
        return protectionParafoudreObligatoireAC.getText().isEmpty() ? symboleVide : protectionParafoudreObligatoireAC.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la lettre de sélection des câbles de chaîne.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getLettreSelectionCablesChaine() {
        return lettreSelectionCablesChaine.getText().isEmpty() ? symboleVide : lettreSelectionCablesChaine.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K1 des câbles de chaîne.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getK1CablesChaine() {
        return K1CablesChaine.getText().isEmpty() ? symboleVide : K1CablesChaine.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K2 des câbles de chaîne.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getK2CablesChaine() {
        return K2CablesChaine.getText().isEmpty() ? symboleVide : K2CablesChaine.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K3 des câbles de chaîne.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getK3CablesChaine() {
        return K3CablesChaine.getText().isEmpty() ? symboleVide : K3CablesChaine.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K des câbles de chaîne.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getKCablesChaine() {
        return KCablesChaine.getText().isEmpty() ? symboleVide : KCablesChaine.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la lettre de sélection du câble principal.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getLettreSelectionCablePrincipal() {
        return lettreSelectionCablePrincipal.getText().isEmpty() ? symboleVide : lettreSelectionCablePrincipal.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K1 du câble principal.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getK1CablePrincipal() {
        return K1CablePrincipal.getText().isEmpty() ? symboleVide : K1CablePrincipal.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K2 du câble principal.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getK2CablePrincipal() {
        return K2CablePrincipal.getText().isEmpty() ? symboleVide : K2CablePrincipal.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K3 du câble principal.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getK3CablePrincipal() {
        return K3CablePrincipal.getText().isEmpty() ? symboleVide : K3CablePrincipal.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour le coefficient K du câble principal.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getKCablePrincipal() {
        return KCablePrincipal.getText().isEmpty() ? symboleVide : KCablePrincipal.getText();
    }

    /**
     * Récupère le texte de la zone de texte pour la chute de tension AC.
     *
     * @return le texte affiché dans la zone de texte. Si la zone est vide, retourne une valeur par défaut définie par <code>symboleVide</code>.
     */
    public static String getChuteTensionAC() {
        return chuteTensionAC.getText().isEmpty() ? symboleVide : chuteTensionAC.getText();
    }

    /**
     * Avance le composant dans le layout GridBagConstraints.
     * Met à jour la contrainte de remplissage et l'ancrage dans le layout.
     */
    private static void avancer() {
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
    }

    /**
     * Crée un nouveau panneau avec un layout BorderLayout et un fond blanc.
     *
     * @return le panneau nouvellement créé.
     */
    private static JPanel createPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        return panel;
    }
	
    /**
     * Crée un composant JLabel avec le texte spécifié.
     * Le JLabel est configuré avec une police de taille normale et sans attributs supplémentaires.
     *
     * @param texte le texte à afficher dans le JLabel.
     * @return un JLabel contenant le texte spécifié, avec une police normale.
     */
    private static JLabel createTexte(String texte) {
        JLabel texteLabel = new JLabel(texte);
        texteLabel.setFont(new Font(texteLabel.getFont().getName(), Font.PLAIN, texteLabel.getFont().getSize()));
        return texteLabel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de sélection pour les calibres.
     * Le texte est affiché à gauche et la zone de sélection (JComboBox) est ajoutée à droite.
     * La JComboBox est remplie avec les références de fusibles obtenues à partir de la base de données.
     * La JComboBox est configurée pour ne pas être modifiable directement et ajuste sa taille en fonction du paramètre `longueurZoneDeTexte`.
     * Lorsque l'utilisateur sélectionne un élément dans la JComboBox, l'action associée met à jour l'objet `CalculCablesEtProtections` en fonction de la sélection.
     *
     * @param texte le texte à afficher à gauche de la JComboBox.
     * @param longueurZoneDeTexte la longueur souhaitée pour la JComboBox, exprimée en nombre de caractères (ajustée à 100 pixels par caractère).
     * @return un JPanel contenant le texte à gauche et la JComboBox à droite.
     */
    public static JPanel createTexteEtZoneDeTexteChoixCalibre(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);

        CalculCablesEtProtections.getBDD(); // Obtient la base de données des fusibles.
        List<String> menuItems = BDD.getFusibles().getReferences(); // Récupère les références des fusibles à partir de la base de données.
        menuItems.add(0, ""); // Ajoute un élément vide au début de la liste pour permettre une option de sélection vide.
        choixCalibreComboBox = new JComboBox<>(menuItems.toArray(new String[0])); // Crée une JComboBox avec les références des fusibles.
        choixCalibreComboBox.setEditable(false); // Configure la JComboBox pour qu'elle ne soit pas modifiable directement.
        choixCalibreComboBox.setPreferredSize(new Dimension(longueurZoneDeTexte * 100, choixCalibreComboBox.getPreferredSize().height)); // Ajuste la taille préférée de la JComboBox.
        choixCalibreComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedReference = (String) choixCalibreComboBox.getSelectedItem(); // Obtient l'élément sélectionné.
                if (!selectedReference.isEmpty()) {
                    CalculCablesEtProtections.getBDD(); // Obtient la base de données des fusibles.
                    CalculCablesEtProtections.setFusible((Fusible) BDD.getFusibles().obtenirElement(selectedReference)); // Met à jour le fusible sélectionné.
                } else {
                    CalculCablesEtProtections.setFusible(null); // Réinitialise le fusible si aucun élément n'est sélectionné.
                }
            }
        });
        panel.add(choixCalibreComboBox, BorderLayout.CENTER); // Ajoute la JComboBox au centre du panneau.
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de sélection pour la section des câbles de chaîne.
     * Le texte est affiché à gauche et la zone de sélection (JComboBox) est ajoutée à droite.
     * La JComboBox est remplie avec les valeurs de section des câbles disponibles (2.5 mm², 4 mm², 6 mm²).
     * La JComboBox est configurée pour ne pas être modifiable directement et ajuste sa taille en fonction du paramètre `longueurZoneDeTexte`.
     * Lorsque l'utilisateur sélectionne un élément dans la JComboBox, l'action associée met à jour la section des câbles de chaîne dans l'objet `CalculCablesEtProtections`.
     *
     * @param texte le texte à afficher à gauche de la JComboBox.
     * @param longueurZoneDeTexte la longueur souhaitée pour la JComboBox, exprimée en nombre de caractères (ajustée à 100 pixels par caractère).
     * @return un JPanel contenant le texte à gauche et la JComboBox à droite.
     */
    public static JPanel createTexteEtZoneDeTexteChoixSectionCablesDeChaine(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);

        List<String> menuItems = new ArrayList<>();
        menuItems.add(""); // Ajoute un élément vide pour permettre une option de sélection vide.
        menuItems.add("2.5 mm²"); // Ajoute les options de section des câbles.
        menuItems.add("4 mm²");
        menuItems.add("6 mm²");
        choixSectionCablesDeChaineComboBox = new JComboBox<>(menuItems.toArray(new String[0])); // Crée une JComboBox avec les options de section.
        choixSectionCablesDeChaineComboBox.setEditable(false); // Configure la JComboBox pour qu'elle ne soit pas modifiable directement.
        choixSectionCablesDeChaineComboBox.setPreferredSize(new Dimension(longueurZoneDeTexte * 100, choixSectionCablesDeChaineComboBox.getPreferredSize().height)); // Ajuste la taille préférée de la JComboBox.
        choixSectionCablesDeChaineComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedReference = (String) choixSectionCablesDeChaineComboBox.getSelectedItem(); // Obtient l'élément sélectionné.
                if (!selectedReference.isEmpty()) {
                    CalculCablesEtProtections.setSectionCablesDeChaine(Double.parseDouble(selectedReference.replace(" mm²", ""))); // Met à jour la section des câbles de chaîne.
                } else {
                    CalculCablesEtProtections.setSectionCablesDeChaine(-1.0); // Réinitialise la section des câbles de chaîne si aucun élément n'est sélectionné.
                }
            }
        });
        panel.add(choixSectionCablesDeChaineComboBox, BorderLayout.CENTER); // Ajoute la JComboBox au centre du panneau.
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de sélection pour la section du câble principal.
     * Le texte est affiché à gauche et la zone de sélection (JComboBox) est ajoutée à droite.
     * La JComboBox est remplie avec les valeurs de section des câbles disponibles (2.5 mm², 4 mm², 6 mm²).
     * La JComboBox est configurée pour ne pas être modifiable directement et ajuste sa taille en fonction du paramètre `longueurZoneDeTexte`.
     * Lorsque l'utilisateur sélectionne un élément dans la JComboBox, l'action associée met à jour la section du câble principal dans l'objet `CalculCablesEtProtections`.
     *
     * @param texte le texte à afficher à gauche de la JComboBox.
     * @param longueurZoneDeTexte la longueur souhaitée pour la JComboBox, exprimée en nombre de caractères (ajustée à 100 pixels par caractère).
     * @return un JPanel contenant le texte à gauche et la JComboBox à droite.
     */
    public static JPanel createTexteEtZoneDeTexteChoixSectionCablePrincipal(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);

        List<String> menuItems = new ArrayList<>();
        menuItems.add(""); // Ajoute un élément vide pour permettre une option de sélection vide.
        menuItems.add("2.5 mm²"); // Ajoute les options de section des câbles.
        menuItems.add("4 mm²");
        menuItems.add("6 mm²");
        choixSectionCablePrincipalComboBox = new JComboBox<>(menuItems.toArray(new String[0])); // Crée une JComboBox avec les options de section.
        choixSectionCablePrincipalComboBox.setEditable(false); // Configure la JComboBox pour qu'elle ne soit pas modifiable directement.
        choixSectionCablePrincipalComboBox.setPreferredSize(new Dimension(longueurZoneDeTexte * 100, choixSectionCablePrincipalComboBox.getPreferredSize().height)); // Ajuste la taille préférée de la JComboBox.
        choixSectionCablePrincipalComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedReference = (String) choixSectionCablePrincipalComboBox.getSelectedItem(); // Obtient l'élément sélectionné.
                if (!selectedReference.isEmpty()) {
                    CalculCablesEtProtections.setSectionCablePrincipal(Double.parseDouble(selectedReference.replace(" mm²", ""))); // Met à jour la section du câble principal.
                } else {
                    CalculCablesEtProtections.setSectionCablePrincipal(-1.0); // Réinitialise la section du câble principal si aucun élément n'est sélectionné.
                }
            }
        });
        panel.add(choixSectionCablePrincipalComboBox, BorderLayout.CENTER); // Ajoute la JComboBox au centre du panneau.
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Irm".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteIrm(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        Irm = new JTextField(longueurZoneDeTexte);
        panel.add(Irm, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Rappel Uoc Tmin".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteRappelUocTmin(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        rappelUocTmin = new JTextField(longueurZoneDeTexte);
        panel.add(rappelUocTmin, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Rappel Isc STC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteRappelIscSTC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        rappelIscSTC = new JTextField(longueurZoneDeTexte);
        panel.add(rappelIscSTC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Rappel Impp STC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteRappelImppSTC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        rappelImppSTC = new JTextField(longueurZoneDeTexte);
        panel.add(rappelImppSTC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Rappel Umpp STC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteRappelUmppSTC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        rappelUmppSTC = new JTextField(longueurZoneDeTexte);
        panel.add(rappelUmppSTC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Nk DC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteNkDC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        NkDC = new JTextField(longueurZoneDeTexte);
        panel.add(NkDC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Ng DC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteNgDC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        NgDC = new JTextField(longueurZoneDeTexte);
        panel.add(NgDC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Constante Pour Lcrit".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteConstantePourLcrit(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        constantePourLcrit = new JTextField(longueurZoneDeTexte);
        panel.add(constantePourLcrit, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Lcrit".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteLcrit(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        Lcrit = new JTextField(longueurZoneDeTexte);
        panel.add(Lcrit, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Ltotale".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteLtotale(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        Ltotale = new JTextField(longueurZoneDeTexte);
        panel.add(Ltotale, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Protection Parafoudre Obligatoire DC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteProtectionParafoudreObligatoireDC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        protectionParafoudreObligatoireDC = new JTextField(longueurZoneDeTexte);
        panel.add(protectionParafoudreObligatoireDC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Second Parafoudre DC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteSecondParafoudreDC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        secondParafoudreDC = new JTextField(longueurZoneDeTexte);
        panel.add(secondParafoudreDC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Uw".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteUw(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        Uw = new JTextField(longueurZoneDeTexte);
        panel.add(Uw, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Nk AC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteNkAC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        NkAC = new JTextField(longueurZoneDeTexte);
        panel.add(NkAC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Ng AC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteNgAC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        NgAC = new JTextField(longueurZoneDeTexte);
        panel.add(NgAC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Protection Parafoudre Obligatoire AC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteProtectionParafoudreObligatoireAC(String texte, int longueurZoneDeTexte) {
        avancer();
        JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        protectionParafoudreObligatoireAC = new JTextField(longueurZoneDeTexte);
        panel.add(protectionParafoudreObligatoireAC, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Lettre Sélection Câbles Chaîne".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteLettreSelectionCablesChaine(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        lettreSelectionCablesChaine = new JTextField(longueurZoneDeTexte);
        panel.add(lettreSelectionCablesChaine, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K1 Câbles Chaîne".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteK1CablesChaine(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        K1CablesChaine = new JTextField(longueurZoneDeTexte);
        panel.add(K1CablesChaine, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K2 Câbles Chaîne".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteK2CablesChaine(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        K2CablesChaine = new JTextField(longueurZoneDeTexte);
        panel.add(K2CablesChaine, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K3 Câbles Chaîne".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteK3CablesChaine(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        K3CablesChaine = new JTextField(longueurZoneDeTexte);
        panel.add(K3CablesChaine, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K Câbles Chaîne".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteKCablesChaine(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        KCablesChaine = new JTextField(longueurZoneDeTexte);
        KCablesChaine.setEditable(false);
        panel.add(KCablesChaine, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Lettre Sélection Câble Principal".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteLettreSelectionCablePrincipal(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        lettreSelectionCablePrincipal = new JTextField(longueurZoneDeTexte);
        panel.add(lettreSelectionCablePrincipal, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K1 Câble Principal".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteK1CablePrincipal(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        K1CablePrincipal = new JTextField(longueurZoneDeTexte);
        panel.add(K1CablePrincipal, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K2 Câble Principal".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteK2CablePrincipal(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        K2CablePrincipal = new JTextField(longueurZoneDeTexte);
        panel.add(K2CablePrincipal, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K3 Câble Principal".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteK3CablePrincipal(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        K3CablePrincipal = new JTextField(longueurZoneDeTexte);
        panel.add(K3CablePrincipal, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "K Câble Principal".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteKCablePrincipal(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        KCablePrincipal = new JTextField(longueurZoneDeTexte);
        KCablePrincipal.setEditable(false);
        panel.add(KCablePrincipal, BorderLayout.CENTER);
        return panel;
    }
	
    /**
     * Crée un panneau (JPanel) contenant un texte et une zone de texte pour "Chute Tension AC".
     * Le texte est affiché à gauche et la zone de texte (JTextField) est ajoutée à droite.
     * La zone de texte est initialisée avec une longueur spécifiée.
     *
     * @param texte le texte à afficher à gauche de la zone de texte.
     * @param longueurZoneDeTexte la longueur souhaitée pour la zone de texte, exprimée en nombre de colonnes.
     * @return un JPanel contenant le texte à gauche et la zone de texte à droite.
     */
    public static JPanel createTexteEtZoneDeTexteChuteTensionAC(String texte, int longueurZoneDeTexte) {
        avancer();
    	JPanel panel = createPanel();
        panel.add(createTexte(texte), BorderLayout.WEST);
        chuteTensionAC = new JTextField(longueurZoneDeTexte);
        panel.add(chuteTensionAC, BorderLayout.CENTER);
        return panel;
    }
    
}
