package service.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Test sentinelle pour forcer l'exécution explicite de chaque méthode InputValidator
 * afin de diagnostiquer la couverture JaCoCo (certaines méthodes n'étaient pas marquées couvertes).
 */
class InputValidatorDirectTest {

    @Test
    void allValidatorMethodsAreInvoked() {
        // Cas valides
        InputValidator.validateLatitude("0");
        InputValidator.validateLongitude("0");
        InputValidator.validatePeakPower("1.5");
        InputValidator.validateLossPercent("10");
        InputValidator.validateAngle("30");
        InputValidator.validateAspect("10");
        InputValidator.validateInjectionRatio("0.5");
        InputValidator.validateProjectDuration("20");

        // Cas invalides contrôlés (assertThrows)
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLatitude("200"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLongitude("181"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validatePeakPower("0"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLossPercent("101"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateAngle("-1"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateAspect("400"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateInjectionRatio("2"));
        assertThrows(IllegalArgumentException.class, () -> InputValidator.validateProjectDuration("0"));
    }
}
