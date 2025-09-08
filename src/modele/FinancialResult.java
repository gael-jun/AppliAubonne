package modele;

import java.util.List;

/**
 * Résultat financier immuable: séries prêtes pour l'affichage et VAN totale.
 */
public final class FinancialResult {
    public final List<String> annees;           // taille = duree + 1 (inclut année 0)
    public final List<Double> cashFlowCumule;   // taille = duree + 1
    public final List<String> anneesRD;         // taille = duree (1..duree)
    public final List<Double> recettes;         // taille = duree
    public final List<Double> depenses;         // taille = duree
    public final List<Double> van;              // taille = duree
    public final double vanTotale;              // somme(van) - investissementInitial

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
