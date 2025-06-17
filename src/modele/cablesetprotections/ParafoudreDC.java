package modele.cablesetprotections;

/**
 * La classe {@code ParafoudreDC} représente un parafoudre pour les systèmes en courant continu (DC).
 * <p>
 * Cette classe est une extension de {@code Element} et contient des informations spécifiques au parafoudre telles que
 * la tension de circuit photovoltaïque, le courant de court-circuit photovoltaïque, le courant total et les tensions de
 * protection pour différents types de protection.
 * </p>
 */
public class ParafoudreDC extends Element {

    private double Ucpv; // Tension du circuit photovoltaïque en volts
    private double Iscpv; // Courant de court-circuit photovoltaïque en ampères
    private double In; // Courant nominal en kiloamperes (kA)
    private String Up; // Tension de protection en kilo-watts (kW), format "UpMC/UpMO"
    private double UpMC; // Tension de protection entre le + et le - en kilo-watts (kW)
    private double UpMO; // Tension de protection entre le + et la masse en kilo-watts (kW)

    /**
     * Construit un nouvel objet {@code ParafoudreDC} avec la référence spécifiée.
     *
     * @param reference La référence unique de l'élément.
     */
    public ParafoudreDC(String reference) {
        super(reference);
    }

    /**
     * Construit un nouvel objet {@code ParafoudreDC} avec les valeurs spécifiées.
     *
     * @param reference La référence unique de l'élément.
     * @param Ucpv La tension du circuit photovoltaïque en volts.
     * @param Iscpv Le courant de court-circuit photovoltaïque en ampères.
     * @param In Le courant nominal en kiloamperes (kA).
     * @param Up La tension de protection en kilo-watts (kW), format "UpMC/UpMO".
     */
    public ParafoudreDC(
            String reference,
            double Ucpv,
            double Iscpv,
            double In,
            String Up
    ) {
        super(reference);
        this.Ucpv = Ucpv;
        this.Iscpv = Iscpv;
        this.In = In;
        this.Up = Up;
        // Parse Up pour obtenir UpMC et UpMO
        String[] upValues = Up.split("/");
        this.UpMC = Double.parseDouble(upValues[0]);
        this.UpMO = Double.parseDouble(upValues[1]);
    }

    /**
     * Définit la tension du circuit photovoltaïque.
     *
     * @param Ucpv La tension du circuit photovoltaïque en volts.
     */
    public void setUcpv(double Ucpv) {
        this.Ucpv = Ucpv;
    }

    /**
     * Définit le courant de court-circuit photovoltaïque.
     *
     * @param Iscpv Le courant de court-circuit photovoltaïque en ampères.
     */
    public void setIscpv(double Iscpv) {
        this.Iscpv = Iscpv;
    }

    /**
     * Définit le courant nominal.
     *
     * @param In Le courant nominal en kiloamperes (kA).
     */
    public void setIn(double In) {
        this.In = In;
    }

    /**
     * Définit la tension de protection.
     * <p>
     * La tension est fournie sous la forme "UpMC/UpMO" où UpMC est la tension entre le + et le - et UpMO est la tension entre
     * le + et la masse.
     * </p>
     *
     * @param Up La tension de protection en kilo-watts (kW), format "UpMC/UpMO".
     */
    public void setUp(String Up) {
        this.Up = Up;
        String[] upValues = Up.split("/");
        this.UpMC = Double.parseDouble(upValues[0]);
        this.UpMO = Double.parseDouble(upValues[1]);
    }

    /**
     * Obtient la tension du circuit photovoltaïque.
     *
     * @return La tension du circuit photovoltaïque en volts.
     */
    public double getUcpv() {
        return this.Ucpv;
    }

    /**
     * Obtient le courant de court-circuit photovoltaïque.
     *
     * @return Le courant de court-circuit photovoltaïque en ampères.
     */
    public double getIscpv() {
        return this.Iscpv;
    }

    /**
     * Obtient le courant nominal.
     *
     * @return Le courant nominal en kiloamperes (kA).
     */
    public double getIn() {
        return this.In;
    }

    /**
     * Obtient la tension de protection.
     *
     * @return La tension de protection en kilo-watts (kW), format "UpMC/UpMO".
     */
    public String getUp() {
        return this.Up;
    }

    /**
     * Obtient la tension de protection entre le + et le -.
     *
     * @return La tension de protection en kilo-watts (kW).
     */
    public double getUpMC() {
        return this.UpMC;
    }

    /**
     * Obtient la tension de protection entre le + et la masse.
     *
     * @return La tension de protection en kilo-watts (kW).
     */
    public double getUpMO() {
        return this.UpMO;
    }

}
