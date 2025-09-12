package modele.pvgis;

import java.util.Objects;

/**
 * Requête Grid/Tracker (endpoint PVcalc) vers l'API PVGIS.
 * Contient l'ensemble des drapeaux de configuration d'orientation et tracking.
 */
public final class PVGridAndTrackerRequest {
    public final String lat;           // latitude décimale
    public final String lon;           // longitude décimale
    public final String peakPower;     // kWc
    public final String loss;          // % pertes système
    public final String radDatabase;   // base irradiation
    public final String pvTechChoice;  // techno PV
    public final String mountingPlace; // roof / free
    public final boolean fixed;
    public final String angle;         // inclinaison (°)
    public final String aspect;        // azimut (°)
    public final boolean optimalInclination;
    public final boolean optimalAngles;
    public final boolean inclinedAxis;
    public final boolean inclinedOptimum;
    public final String inclinedAxisAngle;
    public final boolean verticalAxis;
    public final boolean verticalOptimum;
    public final String verticalAxisAngle;
    public final boolean twoAxis;
    public final String pvPrice;       // prix module €/kWp (option)
    public final String systemCost;    // coût système € (option)
    public final String interest;      // taux intérêt (%) option
    public final String lifetime;      // durée vie (ans) option
    public final boolean useHorizon;
    public final String userHorizon;   // horizon custom
    public final String outputFormat;  // json
    public final boolean browser;
    public final boolean includeGlobal; // demande irradiation plane (&global=1)

    public PVGridAndTrackerRequest(String lat, String lon, String peakPower, String loss, String radDatabase,
                         String pvTechChoice, String mountingPlace, boolean fixed, String angle, String aspect,
                         boolean optimalInclination, boolean optimalAngles, boolean inclinedAxis, boolean inclinedOptimum,
                         String inclinedAxisAngle, boolean verticalAxis, boolean verticalOptimum, String verticalAxisAngle,
                         boolean twoAxis, String pvPrice, String systemCost, String interest, String lifetime,
                         boolean useHorizon, String userHorizon, String outputFormat, boolean browser, boolean includeGlobal) {
        this.lat = lat; this.lon = lon; this.peakPower = peakPower; this.loss = loss; this.radDatabase = radDatabase;
        this.pvTechChoice = pvTechChoice; this.mountingPlace = mountingPlace; this.fixed = fixed; this.angle = angle; this.aspect = aspect;
        this.optimalInclination = optimalInclination; this.optimalAngles = optimalAngles; this.inclinedAxis = inclinedAxis; this.inclinedOptimum = inclinedOptimum;
        this.inclinedAxisAngle = inclinedAxisAngle; this.verticalAxis = verticalAxis; this.verticalOptimum = verticalOptimum; this.verticalAxisAngle = verticalAxisAngle;
        this.twoAxis = twoAxis; this.pvPrice = pvPrice; this.systemCost = systemCost; this.interest = interest; this.lifetime = lifetime;
        this.useHorizon = useHorizon; this.userHorizon = userHorizon; this.outputFormat = outputFormat; this.browser = browser; this.includeGlobal = includeGlobal;
    }

    @Override public String toString() { return "PVcalcRequest{" + lat + "," + lon + "}"; }
    @Override public int hashCode() { return Objects.hash(lat, lon, peakPower, loss, radDatabase, pvTechChoice, mountingPlace, fixed,
            angle, aspect, optimalInclination, optimalAngles, inclinedAxis, inclinedOptimum, inclinedAxisAngle,
            verticalAxis, verticalOptimum, verticalAxisAngle, twoAxis, pvPrice, systemCost, interest, lifetime,
            useHorizon, userHorizon, outputFormat, browser, includeGlobal); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PVGridAndTrackerRequest other)) return false;
        return fixed == other.fixed && optimalInclination == other.optimalInclination && optimalAngles == other.optimalAngles &&
                inclinedAxis == other.inclinedAxis && inclinedOptimum == other.inclinedOptimum && verticalAxis == other.verticalAxis &&
                verticalOptimum == other.verticalOptimum && twoAxis == other.twoAxis && useHorizon == other.useHorizon &&
                browser == other.browser && includeGlobal == other.includeGlobal &&
                Objects.equals(lat, other.lat) && Objects.equals(lon, other.lon) &&
                Objects.equals(peakPower, other.peakPower) && Objects.equals(loss, other.loss) &&
                Objects.equals(radDatabase, other.radDatabase) && Objects.equals(pvTechChoice, other.pvTechChoice) &&
                Objects.equals(mountingPlace, other.mountingPlace) && Objects.equals(angle, other.angle) &&
                Objects.equals(aspect, other.aspect) && Objects.equals(inclinedAxisAngle, other.inclinedAxisAngle) &&
                Objects.equals(verticalAxisAngle, other.verticalAxisAngle) && Objects.equals(pvPrice, other.pvPrice) &&
                Objects.equals(systemCost, other.systemCost) && Objects.equals(interest, other.interest) &&
                Objects.equals(lifetime, other.lifetime) && Objects.equals(userHorizon, other.userHorizon) &&
                Objects.equals(outputFormat, other.outputFormat);
    }
}
