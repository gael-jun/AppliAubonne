package modele.pvgis;

import java.util.List;

/**
 * Résultat consolidé PVGIS (Off-Grid ou Grid/Tracker) après parsing JSON.
 * Contient séries mensuelles, histogramme et éventuellement l'irradiation mensuelle (kWh/mois).
 */
public final class PVGISResult {
    public final List<MonthlyResult> monthly;
    public final List<HistogramBucket> histogram;
    /** Irradiation sur le plan incliné (kWh/mois) si demandée (&global=1), sinon liste vide. */
    public final List<Double> irradiationKWhPerMonth;

    public PVGISResult(List<MonthlyResult> monthly, List<HistogramBucket> histogram) {
        this(monthly, histogram, List.of());
    }

    public PVGISResult(List<MonthlyResult> monthly, List<HistogramBucket> histogram, List<Double> irradiationKWhPerMonth) {
        this.monthly = List.copyOf(monthly);
        this.histogram = List.copyOf(histogram);
        this.irradiationKWhPerMonth = List.copyOf(irradiationKWhPerMonth);
    }
}
