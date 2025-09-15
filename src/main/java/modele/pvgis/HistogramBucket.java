package modele.pvgis;

/**
 * Intervalle d'histogramme de l'état de charge (SOC) ou variable similaire.
 * CS_min / CS_max: bornes de classe.
 * f_CS: fréquence relative (somme des f_CS ≈ 1).
 */
public final class HistogramBucket {
    public final double CS_min;
    public final double CS_max;
    public final double f_CS;
    public HistogramBucket(double csMin, double csMax, double f_CS) { this.CS_min = csMin; this.CS_max = csMax; this.f_CS = f_CS; }
}
