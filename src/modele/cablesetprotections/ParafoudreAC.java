package modele.cablesetprotections;

/**
 * La classe {@code ParafoudreAC} représente un parafoudre pour les systèmes en courant alternatif (AC).
 * <p>
 * Cette classe est une extension de {@code Element} et contient des informations spécifiques au parafoudre telles que
 * la tension de service, le courant total et les tensions de protection pour différents types de protection.
 * </p>
 */
public class ParafoudreAC extends Element {

    private String Ue;
    private double In; // Itotal en kA
    private double UpLPE; // en kV
    private double UpLN; // en kV
    private double UpNPE; // en kV

    /**
     * Construit un nouvel objet {@code ParafoudreAC} avec les valeurs spécifiées.
     *
     * @param reference La référence unique de l'élément.
     * @param Ue La tension de service en volts.
     * @param In Le courant total en kiloamperes (kA).
     * @param UpLPE La tension de protection entre phase et terre en kilo-volts (kV).
     * @param UpLN La tension de protection entre phase et neutre en kilo-volts (kV).
     * @param UpNPE La tension de protection entre neutre et terre en kilo-volts (kV).
     */
    public ParafoudreAC(String reference, String Ue, double In, double UpLPE, double UpLN, double UpNPE) {
        super(reference);
        this.Ue = Ue;
        this.In = In;
        this.UpLPE = UpLPE;
        this.UpLN = UpLN;
        this.UpNPE = UpNPE;
    }

    /**
     * Obtient la tension de service de l'élément.
     *
     * @return La tension de service en volts.
     */
    public String getUe() {
        return this.Ue;
    }

    /**
     * Obtient le courant total du parafoudre.
     *
     * @return Le courant total en kiloamperes (kA).
     */
    public double getIn() {
        return this.In;
    }

    /**
     * Obtient la tension de protection entre phase et terre.
     * <p>
     * Si la valeur est -1, cela indique qu'il n'y a pas de donnée disponible.
     * </p>
     *
     * @return La tension de protection en kilo-volts (kV), ou -1 si aucune donnée n'est disponible.
     */
    public double getUpLPE() {
        return this.UpLPE;
    }

    /**
     * Obtient la tension de protection entre phase et neutre.
     * <p>
     * Si la valeur est -1, cela indique qu'il n'y a pas de donnée disponible.
     * </p>
     *
     * @return La tension de protection en kilo-volts (kV), ou -1 si aucune donnée n'est disponible.
     */
    public double getUpLN() {
        return this.UpLN;
    }

    /**
     * Obtient la tension de protection entre neutre et terre.
     * <p>
     * Si la valeur est -1, cela indique qu'il n'y a pas de donnée disponible.
     * </p>
     *
     * @return La tension de protection en kilo-volts (kV), ou -1 si aucune donnée n'est disponible.
     */
    public double getUpNPE() {
        return this.UpNPE;
    }

}
