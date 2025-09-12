package modele.finance;

import java.util.List;

/**
 * Résultat financier pré-calculé prêt pour affichage/graphes.
 * Listes immuables afin d'éviter des incohérences UI.
 */
public final class FinancialResult {
    public final List<String> annees;         // taille = duree + 1 (inclut année 0)
    public final List<Double> cashFlowCumule; // cumul €
    public final List<String> anneesRD;       // années 1..duree
    public final List<Double> recettes;       // € / an
    public final List<Double> depenses;       // € / an
    public final List<Double> van;            // VAN annuelle
    public final double vanTotale;            // somme(van) - investissementInitial

    public FinancialResult(List<String> annees, List<Double> cashFlowCumule,
                           List<String> anneesRD, List<Double> recettes,
                           List<Double> depenses, List<Double> van,
                           double vanTotale) {
        this.annees = List.copyOf(annees);
        this.cashFlowCumule = List.copyOf(cashFlowCumule);
        this.anneesRD = List.copyOf(anneesRD);
        this.recettes = List.copyOf(recettes);
        this.depenses = List.copyOf(depenses);
        this.van = List.copyOf(van);
        this.vanTotale = vanTotale;
    }
}
