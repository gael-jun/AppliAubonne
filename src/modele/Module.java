package modele;

/**
 * Représente un module photovoltaïque avec ses caractéristiques physiques et électriques.
 * <p>
 * Un module peut être initialisé avec ses dimensions et sa puissance nominale, ou avec 
 * des paramètres de performance sous des conditions standards. Les méthodes permettent de
 * définir et d'obtenir diverses propriétés liées au module, telles que ses coefficients de
 * température et ses valeurs maximales de puissance.
 * </p>
 */
public class Module {

    private String nom;
    private double longueur;
    private double largeur;
    private double pnom;
    private double TTSTC;
    private double CTPMPP;
    private double CTUoc;
    private double CTIsc;
    private double PmaxTSTC;
    private double UocTSTC;
    private double UmppTSTC;
    private double IscTSTC;
    private double ImppTSTC;

    /**
     * Crée un module avec les dimensions et la puissance nominale spécifiées.
     *
     * @param nom le nom du module
     * @param longueur la longueur du module en mètres
     * @param largeur la largeur du module en mètres
     * @param pnom la puissance nominale du module en watts
     */
    public Module(String nom, double longueur, double largeur, double pnom) {
        this.nom = nom;
        this.longueur = longueur;
        this.largeur = largeur;
        this.pnom = pnom;
        this.TTSTC = 0;
        this.CTPMPP = 0;
        this.CTUoc = 0;
        this.CTIsc = 0;
        this.PmaxTSTC = 0;
        this.UocTSTC = 0;
        this.UmppTSTC = 0;
        this.IscTSTC = 0;
        this.ImppTSTC = 0;
    }

    /**
     * Crée un module avec les paramètres de performance sous conditions standard.
     *
     * @param TTSTC le coefficient de température de la puissance en %/°C
     * @param CTPMPP le coefficient de température pour la puissance au point de puissance maximale
     * @param CTUoc le coefficient de température pour la tension à circuit ouvert
     * @param CTIsc le coefficient de température pour le courant de court-circuit
     * @param PmaxTSTC la puissance maximale à température standard (STC) en watts
     * @param UocTSTC la tension à circuit ouvert à température STC en volts
     * @param UmppTSTC la tension au point de puissance maximale à température STC en volts
     * @param IscTSTC le courant de court-circuit à température STC en ampères
     * @param ImppTSTC le courant au point de puissance maximale à température STC en ampères
     */
    public Module(
            double TTSTC,
            double CTPMPP,
            double CTUoc,
            double CTIsc,
            double PmaxTSTC,
            double UocTSTC,
            double UmppTSTC,
            double IscTSTC,
            double ImppTSTC
    ) {
        this.nom = "NONE";
        this.longueur = 0;
        this.largeur = 0;
        this.pnom = 0;
        this.TTSTC = TTSTC;
        this.CTPMPP = CTPMPP;
        this.CTUoc = CTUoc;
        this.CTIsc = CTIsc;
        this.PmaxTSTC = PmaxTSTC;
        this.UocTSTC = UocTSTC;
        this.UmppTSTC = UmppTSTC;
        this.IscTSTC = IscTSTC;
        this.ImppTSTC = ImppTSTC;
    }

    /**
     * Définit le coefficient de température de la puissance.
     *
     * @param valeur le coefficient de température de la puissance en %/°C
     */
    public void setTTSTC(double valeur) {
        this.TTSTC = valeur;
    }

    /**
     * Définit le coefficient de température pour la puissance au point de puissance maximale.
     *
     * @param valeur le coefficient de température pour la puissance au point de puissance maximale
     */
    public void setCTPMPP(double valeur) {
        this.CTPMPP = valeur;
    }

    /**
     * Définit le coefficient de température pour la tension à circuit ouvert.
     *
     * @param valeur le coefficient de température pour la tension à circuit ouvert
     */
    public void setCTUoc(double valeur) {
        this.CTUoc = valeur;
    }

    /**
     * Définit le coefficient de température pour le courant de court-circuit.
     *
     * @param valeur le coefficient de température pour le courant de court-circuit
     */
    public void setCTIsc(double valeur) {
        this.CTIsc = valeur;
    }

    /**
     * Définit la puissance maximale à température standard (STC).
     *
     * @param valeur la puissance maximale à température STC en watts
     */
    public void setPmaxTSTC(double valeur) {
        this.PmaxTSTC = valeur;
    }

    /**
     * Définit la tension à circuit ouvert à température STC.
     *
     * @param valeur la tension à circuit ouvert à température STC en volts
     */
    public void setUocTSTC(double valeur) {
        this.UocTSTC = valeur;
    }

    /**
     * Définit la tension au point de puissance maximale à température STC.
     *
     * @param valeur la tension au point de puissance maximale à température STC en volts
     */
    public void setUmppTSTC(double valeur) {
        this.UmppTSTC = valeur;
    }

    /**
     * Définit le courant de court-circuit à température STC.
     *
     * @param valeur le courant de court-circuit à température STC en ampères
     */
    public void setIscTSTC(double valeur) {
        this.IscTSTC = valeur;
    }

    /**
     * Définit le courant au point de puissance maximale à température STC.
     *
     * @param valeur le courant au point de puissance maximale à température STC en ampères
     */
    public void setImppTSTC(double valeur) {
        this.ImppTSTC = valeur;
    }

    /**
     * Retourne le nom du module.
     *
     * @return le nom du module
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne la longueur du module.
     *
     * @return la longueur du module en mètres
     */
    public double getLongueur() {
        return this.longueur;
    }

    /**
     * Retourne la largeur du module.
     *
     * @return la largeur du module en mètres
     */
    public double getLargeur() {
        return this.largeur;
    }

    /**
     * Retourne la puissance nominale du module.
     *
     * @return la puissance nominale du module en watts
     */
    public double getPnom() {
        return this.pnom;
    }

    /**
     * Retourne le coefficient de température de la puissance.
     *
     * @return le coefficient de température de la puissance en %/°C
     */
    public double getTTSTC() {
        return this.TTSTC;
    }

    /**
     * Retourne le coefficient de température pour la puissance au point de puissance maximale.
     *
     * @return le coefficient de température pour la puissance au point de puissance maximale
     */
    public double getCTPMPP() {
        return this.CTPMPP;
    }

    /**
     * Retourne le coefficient de température pour la tension à circuit ouvert.
     *
     * @return le coefficient de température pour la tension à circuit ouvert
     */
    public double getCTUoc() {
        return this.CTUoc;
    }

    /**
     * Retourne le coefficient de température pour le courant de court-circuit.
     *
     * @return le coefficient de température pour le courant de court-circuit
     */
    public double getCTIsc() {
        return this.CTIsc;
    }

    /**
     * Retourne la puissance maximale à température standard (STC).
     *
     * @return la puissance maximale à température STC en watts
     */
    public double getPmaxTSTC() {
        return this.PmaxTSTC;
    }

    /**
     * Retourne la tension à circuit ouvert à température STC.
     *
     * @return la tension à circuit ouvert à température STC en volts
     */
    public double getUocTSTC() {
        return this.UocTSTC;
    }

    /**
     * Retourne la tension au point de puissance maximale à température STC.
     *
     * @return la tension au point de puissance maximale à température STC en volts
     */
    public double getUmppTSTC() {
        return this.UmppTSTC;
    }

    /**
     * Retourne le courant de court-circuit à température STC.
     *
     * @return le courant de court-circuit à température STC en ampères
     */
    public double getIscTSTC() {
        return this.IscTSTC;
    }

    /**
     * Retourne le courant au point de puissance maximale à température STC.
     *
     * @return le courant au point de puissance maximale à température STC en ampères
     */
    public double getImppTSTC() {
        return this.ImppTSTC;
    }
}
