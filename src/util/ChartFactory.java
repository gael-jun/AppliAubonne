package util;

import java.awt.Color;
import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;

/**
 * Centralise la création des graphiques XChart avec une configuration homogène
 * (rotation des labels à 45°, dimensions, styles basiques).
 */
public final class ChartFactory {
    private ChartFactory() {}

    private static CategoryChart baseChart(String title, String xAxis, String yAxis) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(600)
                .height(300)
                .title(title)
                .xAxisTitle(xAxis)
                .yAxisTitle(yAxis)
                .build();
        // Styles communs
        chart.getStyler().setXAxisLabelRotation(45);
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        return chart;
    }

    // --- Graphiques PV ---

    public static CategoryChart createProductionChart(List<String> mois, List<Double> productionWh) {
        CategoryChart chart = baseChart("Production mensuelle", "Mois", "Wh");
        chart.addSeries("Production", mois, productionWh);
        return chart;
    }

    public static CategoryChart createLostEnergyChart(List<String> mois, List<Double> lostWh) {
        CategoryChart chart = baseChart("Énergie perdue mensuelle", "Mois", "Wh");
        chart.addSeries("Énergie perdue", mois, lostWh);
        return chart;
    }

    // Spécifique Grid: Irradiation mensuelle sur plan fixe
    public static CategoryChart createIrradiationChart(List<String> mois, List<Double> irradiation) {
        CategoryChart chart = baseChart("Irradiation mensuelle sur plan fixe (kWh/m²/mois)", "Mois", "kWh/m²");
        chart.addSeries("Irradiation sur plan fixe", mois, irradiation);
        return chart;
    }

    public static CategoryChart createHistogramChart(List<String> csLabels, List<Double> fcs) {
        CategoryChart chart = baseChart("Histogramme états de charge", "% charge", "% jours");
        chart.addSeries("f_CS", csLabels, fcs);
        return chart;
    }

    public static CategoryChart createBatteryStatusChart(List<String> mois, List<Double> fullPct, List<Double> emptyPct) {
        CategoryChart chart = baseChart("% jours batterie pleine vs vide", "Mois", "% jours");
        chart.addSeries("Batterie pleine", mois, fullPct);
        chart.addSeries("Batterie vide", mois, emptyPct);
        return chart;
    }

    // --- Graphiques financiers ---

    public static CategoryChart createCashFlowChart(List<String> annees, List<Double> cashFlowCumule) {
    CategoryChart chart = baseChart("Cash-flow cumulé", "Année", "€");
    // Affiche au plus deux décimales sur l'axe Y (sans imposer des zéros inutiles)
    chart.getStyler().setYAxisDecimalPattern("#,##0.##");
        chart.addSeries("Cash-flow cumulé", annees, cashFlowCumule);
        return chart;
    }

    public static CategoryChart createRevenueExpenseChart(List<String> annees, List<Double> recettes, List<Double> depenses) {
        CategoryChart chart = baseChart("Recettes et Dépenses annuelles", "Année", "€");
        chart.addSeries("Recettes", annees, recettes);
        chart.addSeries("Dépenses", annees, depenses);
        return chart;
    }

    public static CategoryChart createVANChart(List<String> annees, List<Double> van) {
        CategoryChart chart = baseChart("Flux actualisés (VAN)", "Année", "€");
        // Ajoute une série barres + une série courbe pour lisibilité
        chart.addSeries("VAN", annees, van);
        chart.addSeries("Courbe VAN", annees, van)
             .setChartCategorySeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
        return chart;
    }

    // --- Graphiques Grid ---

    public static CategoryChart createMonthlyProductionKWhChart(List<String> mois, List<Double> productionKWh) {
        CategoryChart chart = baseChart("Production PV moyenne mensuelle (kWh/mois)", "Mois", "kWh");
        chart.addSeries("Production mensuelle", mois, productionKWh);
        return chart;
    }
}
