package modele.cablesetprotections;

/**
 * La classe {@code InterrupteurSectionneurDC} représente un interrupteur-sectionneur pour les systèmes en courant continu (DC).
 * Elle hérite de la classe {@code Element} et contient des informations spécifiques concernant les caractéristiques électriques
 * de l'interrupteur-sectionneur.
 * <p>
 * Un interrupteur-sectionneur DC est utilisé pour isoler des parties d'un circuit pour maintenance ou sécurité. 
 * Cette classe permet de spécifier et de récupérer les valeurs de tension nominale ({@code Ue}) et de courant nominal ({@code In}).
 * </p>
 */
public class InterrupteurSectionneurDC extends Element {

    private double Ue; // Tension nominale en volts
    private double In; // Courant nominal en ampères

    /**
     * Initialise une nouvelle instance de {@code InterrupteurSectionneurDC} avec une référence.
     * <p>
     * Ce constructeur permet de créer un interrupteur-sectionneur avec seulement une référence, sans spécifier les caractéristiques électriques.
     * </p>
     * @param reference La référence unique de l'interrupteur-sectionneur.
     */
    public InterrupteurSectionneurDC(String reference) {
        super(reference);
    }

    /**
     * Initialise une nouvelle instance de {@code InterrupteurSectionneurDC} avec une référence, une tension nominale et un courant nominal.
     * <p>
     * Ce constructeur permet de créer un interrupteur-sectionneur avec toutes ses caractéristiques électriques spécifiées.
     * </p>
     * @param reference La référence unique de l'interrupteur-sectionneur.
     * @param Ue La tension nominale de l'interrupteur-sectionneur en volts.
     * @param In Le courant nominal de l'interrupteur-sectionneur en ampères.
     */
    public InterrupteurSectionneurDC(
            String reference,
            double Ue,
            double In
    ) {
        super(reference);
        this.Ue = Ue;
        this.In = In;
    }

    /**
     * Définit la tension nominale de l'interrupteur-sectionneur.
     * 
     * @param Ue La nouvelle tension nominale en volts.
     */
    public void setUe(double Ue) {
        this.Ue = Ue;
    }

    /**
     * Définit le courant nominal de l'interrupteur-sectionneur.
     * 
     * @param In Le nouveau courant nominal en ampères.
     */
    public void setIn(double In) {
        this.In = In;
    }

    /**
     * Obtient la tension nominale de l'interrupteur-sectionneur.
     * 
     * @return La tension nominale en volts.
     */
    public double getUe() {
        return this.Ue;
    }

    /**
     * Obtient le courant nominal de l'interrupteur-sectionneur.
     * 
     * @return Le courant nominal en ampères.
     */
    public double getIn() {
        return this.In;
    }
}
