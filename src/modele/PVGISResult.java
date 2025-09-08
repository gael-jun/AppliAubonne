package modele;

import java.util.List;

public final class PVGISResult {
    public final List<MonthlyResult> monthly;
    public final List<HistogramBucket> histogram;

    public PVGISResult(List<MonthlyResult> monthly, List<HistogramBucket> histogram) {
        this.monthly = List.copyOf(monthly);
        this.histogram = List.copyOf(histogram);
    }
}
