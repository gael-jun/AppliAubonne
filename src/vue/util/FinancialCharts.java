package vue.util;

import modele.finance.FinancialResult;
import vue.ui.GraphsPanel;
import java.util.ArrayList;
import java.util.List;

/** Render financial charts into a GraphsPanel and update export caches. */
public final class FinancialCharts {
    private FinancialCharts() {}

    public static class Cache {
        public final List<String> annees = new ArrayList<>();
        public final List<Double> cashFlowCumule = new ArrayList<>();
        public final List<String> anneesRD = new ArrayList<>();
        public final List<Double> recettes = new ArrayList<>();
        public final List<Double> depenses = new ArrayList<>();
        public final List<Double> van = new ArrayList<>();
        public double vanTotale = 0.0;
        public final List<java.awt.image.BufferedImage> images = new ArrayList<>();
    }

    /**
     * Renders charts from FinancialResult, updates panel and fills the cache for export.
     */
    public static void renderIntoPanel(FinancialResult fr, GraphsPanel graphsPanel, Cache cache) {
        graphsPanel.removeAll();
        List<org.knowm.xchart.CategoryChart> charts = new ArrayList<>();
    charts.add(ChartsFactory.createCashFlowChart(fr.annees, fr.cashFlowCumule));
    charts.add(ChartsFactory.createRevenueExpenseChart(fr.anneesRD, fr.recettes, fr.depenses));
    charts.add(ChartsFactory.createVANChart(fr.anneesRD, fr.van));
        graphsPanel.setCharts(charts);

        cache.images.clear();
        for (org.knowm.xchart.CategoryChart ch : charts) {
            try {
                cache.images.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(ch));
            } catch (RuntimeException ignore) { }
        }
        cache.annees.clear(); cache.annees.addAll(fr.annees);
        cache.cashFlowCumule.clear(); cache.cashFlowCumule.addAll(fr.cashFlowCumule);
        cache.anneesRD.clear(); cache.anneesRD.addAll(fr.anneesRD);
        cache.recettes.clear(); cache.recettes.addAll(fr.recettes);
        cache.depenses.clear(); cache.depenses.addAll(fr.depenses);
        cache.van.clear(); cache.van.addAll(fr.van);
        cache.vanTotale = fr.vanTotale;

        javax.swing.JLabel labelVAN = new javax.swing.JLabel(String.format("Valeur Actualisée Nette (VAN) totale : %.2f €", fr.vanTotale));
        graphsPanel.add(labelVAN);
        graphsPanel.revalidate();
        graphsPanel.repaint();
    }
}
