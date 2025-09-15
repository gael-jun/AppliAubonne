package service.export;

import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Smoke test direct des stratégies d'export pour améliorer la couverture JaCoCo
 * (certaines classes apparaissaient à 0%).
 */
class ExportStrategiesCoverageTest {

    private ExportContext context(boolean withData) {
        Map<String,String> params = new LinkedHashMap<>();
        params.put("Latitude","46.1");
        params.put("Longitude","6.2");
        List<MonthlyResult> months = withData ? List.of(new MonthlyResult(1, 5000,0,0,0)) : List.of();
        PVGISResult r = new PVGISResult(months, List.of());
        return new ExportContext(params, r, null, Collections.emptyList(), "Doc Test");
    }

    @Test
    void csvAndPdfMinimal() throws Exception {
        ExportContext ctxFull = context(true);
        ExportContext ctxEmpty = context(false);
        File csv = File.createTempFile("cov_csv_",".csv");
        File pdf = File.createTempFile("cov_pdf_",".pdf");
        try {
            new CsvExportStrategy().exportTo(csv, ctxFull);
            assertTrue(csv.length() > 10, "CSV vide inattendu");
            // Export PDF sur data vide pour suivre un autre chemin
            new PdfExportStrategy().exportTo(pdf, ctxEmpty);
            assertTrue(pdf.length() > 50, "PDF trop petit");
        } finally {
            Files.deleteIfExists(csv.toPath());
            Files.deleteIfExists(pdf.toPath());
        }
    }
}
