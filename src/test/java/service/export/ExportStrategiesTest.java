package service.export;

import modele.finance.FinancialParams;
import modele.finance.FinancialResult;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/** Tests d'intégration légers pour CsvExportStrategy et PdfExportStrategy. */
public class ExportStrategiesTest {

    private final List<File> toDelete = new ArrayList<>();

    private PVGISResult buildPVGIS() {
        List<MonthlyResult> months = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            // E_d (Wh/j), E_m (unused here), E_lost_d, f_f, f_e
            months.add(new MonthlyResult(m, 4000, 0.0, 5.0, 2.0));
        }
        return new PVGISResult(months, List.of());
    }

    private FinancialResult buildFinance() {
        // Minimal coherent financial result (année 0 + 3 ans)
        List<String> annees = Arrays.asList("2025","2026","2027","2028");
        List<Double> cfCum = Arrays.asList(-900.0, -700.0, -400.0, 100.0);
        List<String> anneesRD = Arrays.asList("2026","2027","2028");
        List<Double> recettes = Arrays.asList(200.0, 300.0, 600.0);
        List<Double> depenses = Arrays.asList(50.0, 50.0, 50.0);
        List<Double> van = Arrays.asList(190.0, 260.0, 500.0);
        double vanTotale = 500.0 - 1000.0; // reproduction logique approximative
        return new FinancialResult(annees, cfCum, anneesRD, recettes, depenses, van, vanTotale);
    }

    private ExportContext buildContext(boolean withFinance, String title) {
        Map<String,String> params = new LinkedHashMap<>();
        params.put("Latitude","46.5");
        params.put("Longitude","6.3");
        params.put("Pertes","14%");
        return new ExportContext(params, buildPVGIS(), withFinance ? buildFinance() : null, Collections.emptyList(), title);
    }

    private File tempFile(String suffix) throws IOException {
        File f = File.createTempFile("export_test_", suffix);
        toDelete.add(f);
        return f;
    }

    @AfterEach
    void cleanup() {
        for (File f : toDelete) { // best effort
            try { Files.deleteIfExists(f.toPath()); } catch (Exception ignore) {}
        }
        toDelete.clear();
    }

    @Test
    void testCsvExport_withFinance() throws Exception {
        ExportContext ctx = buildContext(true, "Estimation PVGIS Grid");
        File out = tempFile(".csv");
        new CsvExportStrategy().exportTo(out, ctx);
        assertTrue(out.length() > 0, "CSV should not be empty");
        String content = Files.readString(out.toPath(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Mois;Production (Wh)"));
        assertTrue(content.contains("# Donnees financieres"));
        // 12 lignes de mois -> compter occurrences de ';Production (Wh)'
        long monthLines = content.lines().filter(l -> l.startsWith("Jan;") || l.startsWith("Fév;")).count();
        assertTrue(monthLines >= 1); // Au moins Jan présent
        assertTrue(content.contains("VAN totale"));
    }

    @Test
    void testCsvExport_withoutFinance() throws Exception {
        ExportContext ctx = buildContext(false, "Estimation PVGIS Grid");
        File out = tempFile(".csv");
        new CsvExportStrategy().exportTo(out, ctx);
        String content = Files.readString(out.toPath(), StandardCharsets.UTF_8);
        assertTrue(content.contains("# Donnees financieres : aucune donnee financiere"));
    }

    @Test
    void testPdfExport_basic() throws Exception {
        ExportContext ctx = buildContext(true, "Estimation PVGIS Grid");
        File out = tempFile(".pdf");
        new PdfExportStrategy().exportTo(out, ctx);
        assertTrue(out.length() > 500, "PDF should have some content (>=500 bytes) but was " + out.length());
    }

    @Test
    void testPdfExport_emptyData() throws Exception {
        // Contexte sans monthly data -> PDF minimal avec titre
        ExportContext ctx = new ExportContext(Collections.emptyMap(), new PVGISResult(List.of(), List.of()), null, Collections.emptyList(), "Doc Vide");
        File out = tempFile(".pdf");
        new PdfExportStrategy().exportTo(out, ctx);
        assertTrue(out.length() > 100, "Even empty export should produce a small PDF");
    }
}
