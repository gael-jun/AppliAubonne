package service;

import modele.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de calcul financier (VAN, cash-flows, recettes/dépenses) à partir des
 * résultats PVGIS et des paramètres financiers.
 */
public final class FinancialCalculator {

    /**
     * Calcule les séries financières et la VAN totale.
     * Hypothèse: la production annuelle est la somme des productions mensuelles (Wh/j * jours) / 1000 (kWh).
     */
    public FinancialResult compute(PVGISResult pvgis, FinancialParams params) {
        if (pvgis == null || pvgis.monthly == null || pvgis.monthly.isEmpty()) {
            // Retourne des listes vides en cas d'absence de données PV
            return new FinancialResult(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), 0.0);
        }

        // Production annuelle (kWh)
        double productionAnnuelleKWh = pvgis.monthly.stream()
                .mapToDouble(m -> m.E_d * daysInMonth(m.month))
                .sum() / 1000.0; // Wh -> kWh

        int duree = Math.max(0, params.duree);

        List<Double> cashFlowCumule = new ArrayList<>(duree + 1);
        List<Double> recettes = new ArrayList<>(duree);
        List<Double> depenses = new ArrayList<>(duree);
        List<Double> van = new ArrayList<>(duree);
        List<String> annees = new ArrayList<>(duree + 1);
        List<String> anneesRD = new ArrayList<>(duree);

        // Année 0: investissement initial - investissement + subvention
        double cashFlow = -params.investissementInitial + params.subvention;
        cashFlowCumule.add(cashFlow);
        annees.add(Integer.toString(params.anneeDepart));

        for (int an = 1; an <= duree; an++) {
            double recette = productionAnnuelleKWh * params.prixVente * params.tauxInjection;
            double depense = params.coutAnnuel;
            double fluxNet = recette - depense;

            cashFlow += fluxNet;
            cashFlowCumule.add(cashFlow);
            recettes.add(recette);
            depenses.add(depense);

            double fluxActualise = fluxNet / Math.pow(1 + params.tauxActualisation, an);
            van.add(fluxActualise);

            annees.add(Integer.toString(params.anneeDepart + an));
            anneesRD.add(Integer.toString(params.anneeDepart + an));
        }

        double vanTotale = van.stream().mapToDouble(Double::doubleValue).sum() - params.investissementInitial;
        return new FinancialResult(annees, cashFlowCumule, anneesRD, recettes, depenses, van, vanTotale);
    }

    private static int daysInMonth(int month) {
        return switch (month) {
            case 4, 6, 9, 11 -> 30;
            case 2 -> 28; // approximation
            default -> 31;
        };
    }
}
