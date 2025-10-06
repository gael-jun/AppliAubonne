package service.export;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import modele.pvgis.MonthlyResult;

public final class CsvExportStrategy implements ExportStrategy {
    @Override
    public void exportTo(File file, ExportContext context) throws IOException {
        String[] moisFrancais = {"Jan", "Fev", "Mars", "Avril", "Mai", "Juin", "Juil", "Aout", "Sep", "Oct", "Nov", "Dec"};
        List<Double> jours = Arrays.asList(31., 28., 31., 30., 31., 30., 31., 31., 30., 31., 30., 31.);
        try (PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8)) {
            writer.println("Mois;Production (Wh);Energie perdue (Wh);% jours batt. pleine;% jours batt. vide");
            for (MonthlyResult m : context.pvgisResult.monthly) {
                int idx = m.month - 1;
                String nomMois = (idx >= 0 && idx < 12) ? moisFrancais[idx] : ("Mois " + m.month);
                double prod = m.E_d * jours.get(idx);
                double lost = m.E_lost_d * jours.get(idx);
                writer.printf(Locale.US, "%s;%.0f;%.0f;%.1f;%.1f%n", nomMois, prod, lost, m.f_f, m.f_e);
            }
            writer.println();
            if (context.financialResult != null && !context.financialResult.annees.isEmpty()) {
                writer.println("# Donnees financieres utilisees pour les graphes");
                writer.println("Annee;Cash-flow cumule (euro);Recettes (euro);Depenses (euro);VAN actualise (euro)");
                int rows = Math.min(context.financialResult.annees.size(), context.financialResult.cashFlowCumule.size());
                for (int i = 0; i < rows; i++) {
                    String an = context.financialResult.annees.get(i);
                    double cf = context.financialResult.cashFlowCumule.get(i);
                    String rec = (i < context.financialResult.recettes.size()) ? String.format(Locale.US, "%.2f", context.financialResult.recettes.get(i)) : "";
                    String dep = (i < context.financialResult.depenses.size()) ? String.format(Locale.US, "%.2f", context.financialResult.depenses.get(i)) : "";
                    String vanStr = (i < context.financialResult.van.size()) ? String.format(Locale.US, "%.2f", context.financialResult.van.get(i)) : "";
                    writer.printf(Locale.US, "%s;%.2f;%s;%s;%s%n", an, cf, rec, dep, vanStr);
                }
                writer.println();
                writer.printf(Locale.US, "VAN totale (hors investissement);%.2f%n", context.financialResult.vanTotale);
            } else {
                writer.println("# Donnees financieres : aucune donnee financiere disponible. Utilisez le formulaire 'Donnees financieres' pour tracer et sauvegarder.");
            }
        }
    }
}
