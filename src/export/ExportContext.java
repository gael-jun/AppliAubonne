package export;

import modele.FinancialResult;
import modele.PVGISResult;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Contexte d'export: paramètres d'entrée (pour PDF), résultats PV, résultats financiers (optionnel),
 * et éventuellement images de graphiques déjà générées (optionnel).
 */
public final class ExportContext {
    public final Map<String, String> inputParams; // labels -> values
    public final PVGISResult pvgisResult;
    public final FinancialResult financialResult; // peut être null
    public final List<BufferedImage> financialCharts; // peut être null/vide

    public ExportContext(Map<String, String> inputParams,
                         PVGISResult pvgisResult,
                         FinancialResult financialResult,
                         List<BufferedImage> financialCharts) {
        this.inputParams = inputParams;
        this.pvgisResult = pvgisResult;
        this.financialResult = financialResult;
        this.financialCharts = financialCharts;
    }
}
