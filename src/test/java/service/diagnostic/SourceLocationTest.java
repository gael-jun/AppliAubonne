package service.diagnostic;

import org.junit.jupiter.api.Test;
import service.finance.FinancialCalculator;
import service.validation.InputValidator;
import service.export.CsvExportStrategy;
import service.pvgis.PVGISService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie que les classes clés sont chargées depuis target/classes (et donc instrumentables).
 */
class SourceLocationTest {

    @Test
    void classesLoadFromTargetClasses() {
        assertTrue(codeSourcePath(FinancialCalculator.class).contains("target/classes"), "FinancialCalculator non chargé depuis target/classes");
        assertTrue(codeSourcePath(InputValidator.class).contains("target/classes"), "InputValidator non chargé depuis target/classes");
        assertTrue(codeSourcePath(CsvExportStrategy.class).contains("target/classes"), "CsvExportStrategy non chargé depuis target/classes");
        assertTrue(codeSourcePath(PVGISService.class).contains("target/classes"), "PVGISService non chargé depuis target/classes");
    }

    private String codeSourcePath(Class<?> c) {
        var src = c.getProtectionDomain().getCodeSource();
        return src == null ? "<null>" : String.valueOf(src.getLocation());
    }
}
