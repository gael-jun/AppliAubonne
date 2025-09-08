package modele;

import java.util.Objects;

public final class PVGISRequest {
    public final String lat;
    public final String lon;
    public final String peakPower;
    public final String batterySize;
    public final String cutoff;
    public final String consumptionDay;
    public final String angle;
    public final String aspect;
    public final String radDatabase;
    public final boolean useHorizon;
    public final String userHorizon;
    public final String hourConsumption;
    public final String outputFormat;
    public final boolean browser;

    public PVGISRequest(String lat, String lon, String peakPower, String batterySize, String cutoff,
                        String consumptionDay, String angle, String aspect, String radDatabase,
                        boolean useHorizon, String userHorizon, String hourConsumption,
                        String outputFormat, boolean browser) {
        this.lat = lat;
        this.lon = lon;
        this.peakPower = peakPower;
        this.batterySize = batterySize;
        this.cutoff = cutoff;
        this.consumptionDay = consumptionDay;
        this.angle = angle;
        this.aspect = aspect;
        this.radDatabase = radDatabase;
        this.useHorizon = useHorizon;
        this.userHorizon = userHorizon;
        this.hourConsumption = hourConsumption;
        this.outputFormat = outputFormat;
        this.browser = browser;
    }

    @Override public String toString() { return "PVGISRequest{" + lat+","+lon+"}"; }
    @Override public int hashCode() { return Objects.hash(lat, lon, peakPower, batterySize, cutoff, consumptionDay, angle, aspect, radDatabase, useHorizon, userHorizon, hourConsumption, outputFormat, browser); }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PVGISRequest other)) return false;
        return browser == other.browser && useHorizon == other.useHorizon &&
                Objects.equals(lat, other.lat) && Objects.equals(lon, other.lon) &&
                Objects.equals(peakPower, other.peakPower) && Objects.equals(batterySize, other.batterySize) &&
                Objects.equals(cutoff, other.cutoff) && Objects.equals(consumptionDay, other.consumptionDay) &&
                Objects.equals(angle, other.angle) && Objects.equals(aspect, other.aspect) &&
                Objects.equals(radDatabase, other.radDatabase) && Objects.equals(userHorizon, other.userHorizon) &&
                Objects.equals(hourConsumption, other.hourConsumption) && Objects.equals(outputFormat, other.outputFormat);
    }
}
