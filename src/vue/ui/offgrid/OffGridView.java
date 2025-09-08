package vue.ui.offgrid;

import modele.PVGISResult;

/**
 * Minimal view contract for the Off-Grid PVGIS page (MVP increment).
 */
public interface OffGridView {
    void onEstimateStarted();
    void onEstimateSuccess(PVGISResult result);
    void onEstimateError(String message, Throwable error);
}
