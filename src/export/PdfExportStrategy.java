package export;

import modele.HistogramBucket;
import modele.MonthlyResult;
import export.pdf.PdfDocumentBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PdfExportStrategy implements ExportStrategy {
    @Override
    public void exportTo(File file, ExportContext context) throws IOException {
        String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
        List<Double> jours = Arrays.asList(31., 28., 31., 30., 31., 30., 31., 31., 30., 31., 30., 31.);

        List<String> mois = new ArrayList<>();
        List<Double> prod = new ArrayList<>();
        for (MonthlyResult m : context.pvgisResult.monthly) {
            int idx = m.month - 1;
            String nomMois = (idx >= 0 && idx < 12) ? moisFrancais[idx] : ("Mois " + m.month);
            mois.add(nomMois);
            prod.add(m.E_d * jours.get(idx));
        }
        List<Double> lost = new ArrayList<>();
        for (MonthlyResult m : context.pvgisResult.monthly) {
            lost.add(m.E_lost_d * jours.get(m.month - 1));
        }
        List<String> csLabels = new ArrayList<>();
        List<Double> fcs = new ArrayList<>();
        for (HistogramBucket h : context.pvgisResult.histogram) {
            csLabels.add(h.CS_min + "-" + h.CS_max);
            fcs.add(h.f_CS);
        }
        List<Double> ff = new ArrayList<>();
        List<Double> fe = new ArrayList<>();
        for (MonthlyResult m : context.pvgisResult.monthly) {
            ff.add(m.f_f);
            fe.add(m.f_e);
        }

        org.knowm.xchart.CategoryChart chart1 = util.ChartFactory.createProductionChart(mois, prod);
        org.knowm.xchart.CategoryChart chart2 = util.ChartFactory.createLostEnergyChart(mois, lost);
        org.knowm.xchart.CategoryChart chart3 = util.ChartFactory.createHistogramChart(csLabels, fcs);
        org.knowm.xchart.CategoryChart chart4 = util.ChartFactory.createBatteryStatusChart(mois, ff, fe);

        try (PdfDocumentBuilder pdf = new PdfDocumentBuilder()) {
            pdf.addTitle("Estimation PVGIS Off-Grid");
            if (context.inputParams != null && !context.inputParams.isEmpty()) {
                List<String[]> lines = new ArrayList<>();
                context.inputParams.forEach((k, v) -> lines.add(new String[]{k, v}));
                pdf.addKeyValueLines(lines);
            }

            // Tableau mensuel
            String[] headers = {"Mois", "Prod (Wh)", "Energie perdue (Wh)", "% jours batt. pleine", "% jours batt. vide"};
            List<List<String>> rows = new ArrayList<>();
            for (int i = 0; i < mois.size(); i++) {
                rows.add(Arrays.asList(
                        mois.get(i),
                        String.format("%.0f", prod.get(i)),
                        String.format("%.0f", lost.get(i)),
                        String.format("%.1f", ff.get(i)),
                        String.format("%.1f", fe.get(i))
                ));
            }
            pdf.addTable(headers, rows);

            // Graphiques PV
            pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart1), 500, 250);
            pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart2), 500, 250);
            pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart3), 500, 250);
            pdf.addImage(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart4), 500, 250);

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
