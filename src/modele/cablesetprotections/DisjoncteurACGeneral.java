package modele.cablesetprotections;

/**
 * La classe {@code DisjoncteurACGeneral} représente un disjoncteur général AC.
 * Un disjoncteur est un dispositif de protection qui protège les circuits électriques contre les courts-circuits
 * et les surcharges. Cette classe étend la classe {@code Element} et ajoute des informations spécifiques
 * au disjoncteur AC général, telles que sa capacité de coupure, le courant nominal, la courbe de déclenchement et le type.
 */
public class DisjoncteurACGeneral extends Element {

    /**
     * Capacité de coupure en kiloampères (kA) du disjoncteur.
     */
    private String Icu; // en kA

    /**
     * Courant nominal du disjoncteur en ampères (A).
     */
    private double In;

    /**
     * Courbe de déclenchement du disjoncteur, représentée par un caractère (par exemple 'B', 'C', 'D').
     */
    private char courbe;

    /**
     * Type de disjoncteur, par exemple "AC", "A", etc.
     */
    private String type;
    
    /**
     * Crée une instance de {@code DisjoncteurACGeneral} avec les spécifications fournies.
     *
     * @param reference La référence du disjoncteur.
     * @param Icu La capacité de coupure en kiloampères (kA).
     * @param In Le courant nominal en ampères (A).
     * @param courbe La courbe de déclenchement du disjoncteur (par exemple 'B', 'C', 'D').
     * @param type Le type de disjoncteur (par exemple "AC", "A").
     */
    public DisjoncteurACGeneral(String reference, String Icu, double In, char courbe, String type) {
        super(reference);
        this.Icu = Icu;
        this.In = In;
        this.courbe = courbe;
        this.type = type;
    }

    /**
     * Retourne la capacité de coupure du disjoncteur.
     *
     * @return La capacité de coupure en kiloampères (kA).
     */
    public String getIcu() {
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
     * Retourne le type du disjoncteur.
     *
     * @return Le type du disjoncteur (par exemple "AC", "A").
     */
    public String getType() {
        return this.type;
    }
}
