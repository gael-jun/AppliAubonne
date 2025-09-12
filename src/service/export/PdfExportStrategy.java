package service.export;

import modele.pvgis.HistogramBucket;
import modele.pvgis.MonthlyResult;
import service.export.pdf.PdfDocumentBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PdfExportStrategy implements ExportStrategy {
    @Override
    public void exportTo(File file, ExportContext context) throws IOException {
        if (context == null || context.pvgisResult == null || context.pvgisResult.monthly == null || context.pvgisResult.monthly.isEmpty()) {
            // Nothing to export
            try (PdfDocumentBuilder pdf = new PdfDocumentBuilder()) {
                pdf.addTitle(context != null && context.documentTitle != null ? context.documentTitle : "Estimation PVGIS");
                pdf.saveTo(file);
            }
            return;
        }
        String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
        List<Double> jours = Arrays.asList(31., 28., 31., 30., 31., 30., 31., 31., 30., 31., 30., 31.);

        List<String> mois = new ArrayList<>();
        List<Double> prodWh = new ArrayList<>();
        List<Double> prodKWh = new ArrayList<>();
        for (MonthlyResult m : context.pvgisResult.monthly) {
            int idx = m.month - 1;
            String nomMois = (idx >= 0 && idx < 12) ? moisFrancais[idx] : ("Mois " + m.month);
            mois.add(nomMois);
            double whMonth = m.E_d * jours.get(idx);
            prodWh.add(whMonth);
            prodKWh.add(whMonth / 1000.0);
        }
        List<Double> lost = new ArrayList<>();
        List<String> csLabels = new ArrayList<>();
        List<Double> fcs = new ArrayList<>();
        List<Double> ff = new ArrayList<>();
        List<Double> fe = new ArrayList<>();
        for (MonthlyResult m : context.pvgisResult.monthly) {
            lost.add(m.E_lost_d * jours.get(m.month - 1));
            ff.add(m.f_f);
            fe.add(m.f_e);
        }
        if (context.pvgisResult.histogram != null) {
            for (HistogramBucket h : context.pvgisResult.histogram) {
                csLabels.add(h.CS_min + "-" + h.CS_max);
                fcs.add(h.f_CS);
            }
        }

    String titleLower = (context.documentTitle == null ? "" : context.documentTitle.toLowerCase());
    // Attention: "Off-Grid" contient la sous-chaîne "grid" -> on exclut explicitement off-grid
    boolean isGridOrTracker = (titleLower.contains("tracker") || (titleLower.contains("grid") && !titleLower.contains("off-grid")));

        // Créer les graphiques uniquement si les données ne sont pas vides pour éviter les erreurs XChart
        org.knowm.xchart.CategoryChart chart1 = null;
        org.knowm.xchart.CategoryChart chart2 = null;
        org.knowm.xchart.CategoryChart chart3 = null;
        org.knowm.xchart.CategoryChart chart4 = null;
        if (isGridOrTracker) {
            if (!mois.isEmpty() && !prodKWh.isEmpty()) {
                 chart1 = vue.util.ChartsFactory.createMonthlyProductionKWhChart(mois, prodKWh);
            }
            List<Double> irr = context.pvgisResult.irradiationKWhPerMonth;
            if (irr != null && !irr.isEmpty() && irr.size() == mois.size()) {
                 chart2 = vue.util.ChartsFactory.createIrradiationChart(mois, irr);
            }
        } else {
            if (!mois.isEmpty() && !prodWh.isEmpty()) {
                 chart1 = vue.util.ChartsFactory.createProductionChart(mois, prodWh);
            }
            if (!mois.isEmpty() && !lost.isEmpty()) {
                 chart2 = vue.util.ChartsFactory.createLostEnergyChart(mois, lost);
            }
            if (!csLabels.isEmpty() && !fcs.isEmpty()) {
                 chart3 = vue.util.ChartsFactory.createHistogramChart(csLabels, fcs);
            }
            if (!mois.isEmpty() && (!ff.isEmpty() || !fe.isEmpty())) {
                 chart4 = vue.util.ChartsFactory.createBatteryStatusChart(mois, ff, fe);
            }
        }

    try (PdfDocumentBuilder pdf = new PdfDocumentBuilder()) {
        String title = (context.documentTitle == null || context.documentTitle.isBlank())
            ? "Estimation PVGIS"
            : context.documentTitle;
        pdf.addTitle(title);
            if (context.inputParams != null && !context.inputParams.isEmpty()) {
                List<String[]> lines = new ArrayList<>();
                context.inputParams.forEach((k, v) -> lines.add(new String[]{k, v}));
                pdf.addKeyValueLines(lines);
            }

            // Tableau mensuel (adapté selon mode)
            if (isGridOrTracker) {
                List<Double> irr = context.pvgisResult.irradiationKWhPerMonth;
                boolean hasIrr = irr != null && irr.size() == mois.size();
                String[] headers = hasIrr
                        ? new String[]{"Mois", "Prod (kWh)", "Irradiation (kWh/m²)"}
                        : new String[]{"Mois", "Prod (kWh)"};
                List<List<String>> rows = new ArrayList<>();
                for (int i = 0; i < mois.size(); i++) {
                    List<String> row = new ArrayList<>();
                    row.add(mois.get(i));
                    row.add(String.format("%.2f", prodKWh.get(i)));
                    if (hasIrr) row.add(String.format("%.2f", irr.get(i)));
                    rows.add(row);
                }
                pdf.addTable(headers, rows);
            } else {
                String[] headers = {"Mois", "Prod (Wh)", "Énergie perdue (Wh)", "% jours batt. pleine", "% jours batt. vide"};
                List<List<String>> rows = new ArrayList<>();
                for (int i = 0; i < mois.size(); i++) {
                    rows.add(Arrays.asList(
                            mois.get(i),
                            String.format("%.0f", prodWh.get(i)),
                            String.format("%.0f", lost.get(i)),
                            String.format("%.1f", ff.get(i)),
                            String.format("%.1f", fe.get(i))
                    ));
                }
                pdf.addTable(headers, rows);
            }

            // Graphiques PV (ajoute uniquement ceux disponibles)
            if (chart1 != null) pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart1), 500, 250);
            if (chart2 != null) pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart2), 500, 250);
            if (chart3 != null) pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart3), 500, 250);
            if (chart4 != null) pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart4), 500, 250);

            // Section financière si disponible
            if (context.financialResult != null && !context.financialResult.annees.isEmpty()) {
                // Titre de la section au-dessus du tableau financier
                pdf.addSectionTitle("Données financières utilisées pour les graphiques : ");
                // Tableau financier
                String[] finHeaders = {"Année", "Cash-flow cumulé (€)", "Recettes (€)", "Dépenses (€)", "VAN actualisé (€)"};
                List<List<String>> finRows = new ArrayList<>();
                int rowsCount = Math.min(context.financialResult.annees.size(), context.financialResult.cashFlowCumule.size());
                for (int i = 0; i < rowsCount; i++) {
                    finRows.add(Arrays.asList(
                            context.financialResult.annees.get(i),
                            String.format("%.2f", context.financialResult.cashFlowCumule.get(i)),
                            i < context.financialResult.recettes.size() ? String.format("%.2f", context.financialResult.recettes.get(i)) : "",
                            i < context.financialResult.depenses.size() ? String.format("%.2f", context.financialResult.depenses.get(i)) : "",
                            i < context.financialResult.van.size() ? String.format("%.2f", context.financialResult.van.get(i)) : ""
                    ));
                }
                pdf.addTable(finHeaders, finRows);

                // Ligne VAN totale (hors investissement) sous le tableau
                pdf.addTextLine(String.format("VAN totale (hors investissement) : %.2f €", context.financialResult.vanTotale));

                // Graphiques financiers si fournis
                if (context.financialCharts != null && !context.financialCharts.isEmpty()) {
                    for (java.awt.image.BufferedImage img : context.financialCharts) {
                        pdf.addImage(img, 500, 250);
                    }
                }
            }

            pdf.saveTo(file);
        }
    }
}
