package modele.cablesetprotections;

/**
 * La classe {@code DisjoncteurACOnduleur} représente un disjoncteur AC destiné à être utilisé avec des onduleurs.
 * Un disjoncteur AC Onduleur protège les circuits électriques contre les courts-circuits et les surcharges, en particulier
 * dans les systèmes de conversion d'énergie comme les onduleurs.
 */
public class DisjoncteurACOnduleur extends Element {

    /**
     * Courant nominal du disjoncteur en ampères (A).
     */
    private double In; // Calibre

    /**
     * Capacité de coupure du disjoncteur en kiloampères (kA).
     */
    private double Icu; // en kA

    /**
     * Courbe de déclenchement du disjoncteur, représentée par un caractère (par exemple 'B', 'C', 'D').
     */
    private char courbe;

    /**
     * Protection différentielle associée au disjoncteur.
     */
    private ProtectionDifferentielle protectionDifferentielle;

    /**
     * Crée une instance de {@code DisjoncteurACOnduleur} avec les spécifications fournies.
     *
     * @param reference La référence du disjoncteur.
     * @param In Le courant nominal en ampères (A).
     * @param Icu La capacité de coupure en kiloampères (kA).
     * @param courbe La courbe de déclenchement du disjoncteur (par exemple 'B', 'C', 'D').
     */
    public DisjoncteurACOnduleur(String reference, double In, double Icu, char courbe) {
        super(reference);
        this.In = In;
        this.Icu = Icu;
        this.courbe = courbe;
        this.protectionDifferentielle = null;
    }

    /**
     * Retourne la capacité de coupure du disjoncteur.
     *
     * @return La capacité de coupure en kiloampères (kA).
     */
    public double getIcu() {
        return this.Icu;
    }

    /**
     * Retourne le courant nominal du disjoncteur.
     *
     * @return Le courant nominal en ampères (A).
     */
    public double getIn() {
        return this.In;
    }

    /**
     * Retourne la courbe de déclenchement du disjoncteur.
     *
     * @return La courbe de déclenchement sous forme de caractère (par exemple 'B', 'C', 'D').
     */
    public char getCourbe() {
        return this.courbe;
    }

    /**
     * Retourne la protection différentielle associée au disjoncteur.
     *
     * @return La protection différentielle, ou {@code null} si aucune protection différentielle n'est associée.
     */
    public ProtectionDifferentielle getProtectionDifferentielle() {
        return this.protectionDifferentielle;
    }

    /**
     * Définit la protection différentielle associée au disjoncteur.
     *
     * @param protectionDifferentielle La protection différentielle à associer au disjoncteur.
     */
    public void setProtectionDifferentielle(ProtectionDifferentielle protectionDifferentielle) {
        this.protectionDifferentielle = protectionDifferentielle;
    }
}
