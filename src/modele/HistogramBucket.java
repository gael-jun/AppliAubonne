package modele;

public final class HistogramBucket {
    public final double CS_min;
    public final double CS_max;
    public final double f_CS;

    public HistogramBucket(double csMin, double csMax, double f_CS) {
        this.CS_min = csMin;
        this.CS_max = csMax;
        this.f_CS = f_CS;
    }
}
