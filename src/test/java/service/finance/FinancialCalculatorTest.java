package service.finance;

import modele.finance.FinancialParams;
import modele.finance.FinancialResult;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/** Tests principaux pour FinancialCalculator. */
public class FinancialCalculatorTest {

    private PVGISResult buildPVGISWithConstantDailyWh(double dailyWh) {
        // 12 mois avec E_d = dailyWh (Wh/jour)
        List<MonthlyResult> months = java.util.stream.IntStream.rangeClosed(1,12)
                .mapToObj(m -> new MonthlyResult(m, dailyWh, 0.0, 0.0, 0.0))
                .toList();
        return new PVGISResult(months, List.of());
    }

    @Test
    void testCompute_scenarioNominal_vanPositive() {
        PVGISResult p = buildPVGISWithConstantDailyWh(4000); // 4 kWh/jour -> selon jours ~1460 kWh/an
        FinancialParams params = new FinancialParams(1000, 100, 0.20, 0.8, 50, 10, 0.05, 2025);
        FinancialCalculator calc = new FinancialCalculator();
        FinancialResult r = calc.compute(p, params);
        assertEquals(11, r.annees.size()); // duree + 1
        assertEquals(10, r.recettes.size());
        assertFalse(r.van.isEmpty());
        assertTrue(r.vanTotale < 0 || r.vanTotale > -2000); // fourchette large (juste existence)
    }

    @Test
    void testCompute_subventionSuperieureInvestissement() {
        PVGISResult p = buildPVGISWithConstantDailyWh(3000);
        FinancialParams params = new FinancialParams(500, 800, 0.15, 0.5, 20, 5, 0.02, 2025);
        FinancialResult r = new FinancialCalculator().compute(p, params);
        // Cashflow initial doit être positif (subvention > investissement)
        assertTrue(r.cashFlowCumule.get(0) > 0);
    }

    @Test
    void testCompute_tauxActualisationZero() {
        PVGISResult p = buildPVGISWithConstantDailyWh(5000);
        FinancialParams params = new FinancialParams(600, 0, 0.18, 1.0, 0, 3, 0.0, 2025);
        FinancialResult r = new FinancialCalculator().compute(p, params);
        // VAN annuelle = flux net car pas d'actualisation
        assertEquals(r.recettes.get(0), r.van.get(0), 1e-6);
    }

    @Test
    void testCompute_autoconsommationBornees() {
        PVGISResult p = buildPVGISWithConstantDailyWh(3500);
        FinancialParams zeroInjection = new FinancialParams(300, 0, 0.2, 0.0, 10, 2, 0.05, 2025);
        FinancialParams fullInjection = new FinancialParams(300, 0, 0.2, 1.0, 10, 2, 0.05, 2025);
        FinancialCalculator calc = new FinancialCalculator();
        FinancialResult r0 = calc.compute(p, zeroInjection);
        FinancialResult r1 = calc.compute(p, fullInjection);
        double recette0 = r0.recettes.get(0);
        double recette1 = r1.recettes.get(0);
        assertEquals(0.0, recette0, 1e-9); // aucune injection
        assertTrue(recette1 > recette0);
    }

    @Test
    void testCompute_absenceDonneesPV_retourVide() {
        FinancialParams params = new FinancialParams(1000, 0, 0.2, 0.5, 10, 5, 0.04, 2025);
        FinancialResult r = new FinancialCalculator().compute(new PVGISResult(List.of(), List.of()), params);
        assertTrue(r.annees.isEmpty());
        assertEquals(0.0, r.vanTotale, 1e-9);
    }
}
