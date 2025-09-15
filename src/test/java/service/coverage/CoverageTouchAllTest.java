package service.coverage;

import org.junit.jupiter.api.Test;
import service.validation.InputValidator;
import service.finance.FinancialCalculator;
import modele.finance.FinancialParams;
import modele.pvgis.PVGISResult;
import modele.pvgis.MonthlyResult;
import service.export.CsvExportStrategy;
import service.export.ExportContext;
import service.pvgis.PVGISParser;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test sentinelle léger visant à garantir que JaCoCo instrumente bien plusieurs packages.
 */
class CoverageTouchAllTest {

    @Test
    void touchMultipleServices() throws Exception {
        InputValidator.validateLatitude("0");
        InputValidator.validateLongitude("0");
        InputValidator.validatePeakPower("1");

        var months = List.of(new MonthlyResult(1, 1000,0,0,0));
        var pvgis = new PVGISResult(months, List.of());
        var params = new FinancialParams(100,0,0.2,0.5,10,1,0.05,2025);
        var fin = new FinancialCalculator().compute(pvgis, params);
        assertFalse(fin.annees.isEmpty());

        File tmp = File.createTempFile("cov", ".csv");
        tmp.deleteOnExit();
        new CsvExportStrategy().exportTo(tmp, new ExportContext(Map.of(), pvgis, fin, List.of()));
        assertTrue(tmp.length() > 0);

        var parsed = PVGISParser.parse("{\"outputs\":{\"monthly\":[{\"month\":1,\"E_m\":10,\"H(i)_m\":5}]}}");
        assertEquals(1, parsed.monthly.size());
    }
}
