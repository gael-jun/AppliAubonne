package service.export;

import modele.finance.FinancialResult;
import modele.pvgis.PVGISResult;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
 * Contexte d'export: agrège tout ce dont une stratégie d'export a besoin.
 *
 * Contient:
 *  - inputParams: paramètres saisis (clé lisible -> valeur) réinjectés dans le document.
 *  - pvgisResult: résultat de production parsé.
 *  - financialResult: résultat financier (optionnel si calcul non effectué).
 *  - financialCharts: images des graphiques financiers déjà rendus (optionnel) pour éviter de recalculer.
 *  - documentTitle: titre suggéré pour la stratégie (en-tête PDF par ex.).
 */
public final class ExportContext {
    public final Map<String, String> inputParams; // labels -> values
    public final PVGISResult pvgisResult;
    public final FinancialResult financialResult; // peut être null
    public final List<BufferedImage> financialCharts; // peut être null/vide
    /**
     * Titre du document (optionnel). Utilisé par les exports PDF pour l'en-tête.
     * Si null, une valeur par défaut sera utilisée par la stratégie.
     */
    public final String documentTitle;

    public ExportContext(Map<String, String> inputParams,
                         PVGISResult pvgisResult,
                         FinancialResult financialResult,
                         List<BufferedImage> financialCharts) {
        this(inputParams, pvgisResult, financialResult, financialCharts, null);
    }

    public ExportContext(Map<String, String> inputParams,
                         PVGISResult pvgisResult,
                         FinancialResult financialResult,
                         List<BufferedImage> financialCharts,
                         String documentTitle) {
        this.inputParams = inputParams;
        this.pvgisResult = pvgisResult;
        this.financialResult = financialResult;
        this.financialCharts = financialCharts;
        this.documentTitle = documentTitle;
    }
}
