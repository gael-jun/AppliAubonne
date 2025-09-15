package service.validation;

/** Validation centralisée des entrées numériques PVGIS (grid / tracker / off-grid). */
public final class InputValidator {
    private InputValidator() {}

    public static void validateLatitude(String lat) { double v = parse(lat); if (v < -90 || v > 90) throw new IllegalArgumentException("Latitude hors bornes [-90,90]: " + v); }
    public static void validateLongitude(String lon) { double v = parse(lon); if (v < -180 || v > 180) throw new IllegalArgumentException("Longitude hors bornes [-180,180]: " + v); }
    public static void validatePeakPower(String peak) { double v = parse(peak); if (v <= 0) throw new IllegalArgumentException("Puissance crête doit être > 0: " + v); if (v > 10000) throw new IllegalArgumentException("Puissance crête trop grande: " + v); }
    public static void validateLossPercent(String loss) { double v = parse(loss); if (v < 0 || v > 100) throw new IllegalArgumentException("Pertes (%) hors bornes [0,100]: " + v); }
    public static void validateAngle(String angle) { if (angle == null || angle.isBlank()) return; double v = parse(angle); if (v < 0 || v > 90) throw new IllegalArgumentException("Inclinaison hors bornes [0,90]: " + v); }
    public static void validateAspect(String aspect) { if (aspect == null || aspect.isBlank()) return; double v = parse(aspect); if (v < -180 || v > 360) throw new IllegalArgumentException("Azimut hors bornes [-180,360]: " + v); }
    public static void validateInjectionRatio(String ratio) { if (ratio == null || ratio.isBlank()) return; double v = parse(ratio); if (v < 0 || v > 1) throw new IllegalArgumentException("Taux injection (0-1) invalide: " + v); }
    public static void validateProjectDuration(String years) { if (years == null || years.isBlank()) return; double v = parse(years); if (v <= 0 || v > 60) throw new IllegalArgumentException("Durée projet hors bornes (1-60): " + v); }

    private static double parse(String s) { try { return Double.parseDouble(s.trim()); } catch (Exception e) { throw new IllegalArgumentException("Valeur numérique invalide: '"+s+"'"); } }
}
