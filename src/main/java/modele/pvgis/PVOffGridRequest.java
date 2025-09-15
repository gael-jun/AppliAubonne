package modele.pvgis;

import java.util.Objects;

/**
 * Requête Off-Grid (endpoint SHScalc) vers l'API PVGIS.
 * Champs conservés sous forme de String car envoyés tels quels dans l'URL.
 * Immuable et thread-safe.
 */
public final class PVOffGridRequest {
    /** Latitude décimale (ex: "46.2"). */
    public final String lat;
    /** Longitude décimale (ex: "6.1"). */
    public final String lon;
    /** Puissance crête installée kWc. */
    public final String peakPower;
    /** Capacité batterie (Wh). */
    public final String batterySize;
    /** Tension/cutoff (%) ou paramètre de coupure selon API. */
    public final String cutoff;
    /** Consommation moyenne journalière (Wh/j). */
    public final String consumptionDay;
    /** Inclinaison (°) facultative. */
    public final String angle;
    /** Azimut (°) facultatif (0 = sud). */
    public final String aspect;
    /** Base de données d'irradiation sélectionnée. */
    public final String radDatabase;
    /** Utilisation de l'horizon (1/0). */
    public final boolean useHorizon;
    /** Horizon utilisateur format API si fourni. */
    public final String userHorizon;
    /** Profil de consommation horaire CSV inline éventuel. */
    public final String hourConsumption;
    /** Format de sortie (json). */
    public final String outputFormat;
    /** Indique appel navigateur (pour API). */
    public final boolean browser;

    public PVOffGridRequest(String lat, String lon, String peakPower, String batterySize, String cutoff,
                        String consumptionDay, String angle, String aspect, String radDatabase,
                        boolean useHorizon, String userHorizon, String hourConsumption,
                        String outputFormat, boolean browser) {
        this.lat = lat; this.lon = lon; this.peakPower = peakPower; this.batterySize = batterySize; this.cutoff = cutoff;
        this.consumptionDay = consumptionDay; this.angle = angle; this.aspect = aspect; this.radDatabase = radDatabase;
        this.useHorizon = useHorizon; this.userHorizon = userHorizon; this.hourConsumption = hourConsumption;
        this.outputFormat = outputFormat; this.browser = browser;
    }

    @Override public String toString() { return "PVGISRequest{" + lat+","+lon+"}"; }
    @Override public int hashCode() { return Objects.hash(lat, lon, peakPower, batterySize, cutoff, consumptionDay, angle, aspect, radDatabase, useHorizon, userHorizon, hourConsumption, outputFormat, browser); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PVOffGridRequest other)) return false;
        return browser == other.browser && useHorizon == other.useHorizon &&
                Objects.equals(lat, other.lat) && Objects.equals(lon, other.lon) &&
                Objects.equals(peakPower, other.peakPower) && Objects.equals(batterySize, other.batterySize) &&
                Objects.equals(cutoff, other.cutoff) && Objects.equals(consumptionDay, other.consumptionDay) &&
                Objects.equals(angle, other.angle) && Objects.equals(aspect, other.aspect) &&
                Objects.equals(radDatabase, other.radDatabase) && Objects.equals(userHorizon, other.userHorizon) &&
                Objects.equals(hourConsumption, other.hourConsumption) && Objects.equals(outputFormat, other.outputFormat);
    }
}
