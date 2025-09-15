package service.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputValidatorTest {

    @Test void latitudeValid() { assertDoesNotThrow(() -> InputValidator.validateLatitude("45.3")); }
    @Test void latitudeInvalidHigh() { var ex = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLatitude("123")); assertTrue(ex.getMessage().contains("Latitude")); }
    @Test void longitudeInvalid() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLongitude("190")); }
    @Test void peakPowerInvalidNegative() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validatePeakPower("0")); }
    @Test void lossPercentValidEdge() { assertDoesNotThrow(() -> InputValidator.validateLossPercent("100")); }
    @Test void lossPercentInvalid() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLossPercent("101")); }
    @Test void angleBlankIgnored() { assertDoesNotThrow(() -> InputValidator.validateAngle("")); }
    @Test void angleInvalid() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validateAngle("95")); }
    @Test void aspectValid() { assertDoesNotThrow(() -> InputValidator.validateAspect("180")); }
    @Test void aspectInvalid() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validateAspect("400")); }
    @Test void injectionRatioValid() { assertDoesNotThrow(() -> InputValidator.validateInjectionRatio("0.7")); }
    @Test void injectionRatioInvalid() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validateInjectionRatio("1.5")); }
    @Test void projectDurationValid() { assertDoesNotThrow(() -> InputValidator.validateProjectDuration("25")); }
    @Test void projectDurationInvalid() { assertThrows(IllegalArgumentException.class, () -> InputValidator.validateProjectDuration("0")); }
    @Test void parseInvalidNumberMessage() {
        var ex = assertThrows(IllegalArgumentException.class, () -> InputValidator.validateLossPercent("abc"));
        assertTrue(ex.getMessage().contains("abc"));
    }
}
