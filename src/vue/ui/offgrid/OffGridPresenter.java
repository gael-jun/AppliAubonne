package vue.ui.offgrid;

import modele.PVGISRequest;
import modele.PVGISResult;
import service.PVGISService;

import javax.swing.SwingUtilities;

/**
 * Presenter for Off-Grid PVGIS estimation, decoupling the view from the service.
 */
public final class OffGridPresenter {
    private final OffGridView view;
    private final PVGISService service;

    public OffGridPresenter(OffGridView view, PVGISService service) {
        this.view = view;
        this.service = service;
    }

    public void estimateAsync(PVGISRequest request) {
        SwingUtilities.invokeLater(view::onEstimateStarted);
        new Thread(() -> {
            try {
                PVGISResult res = service.fetch(request);
                SwingUtilities.invokeLater(() -> view.onEstimateSuccess(res));
            } catch (java.io.IOException | InterruptedException ex) {
                SwingUtilities.invokeLater(() -> view.onEstimateError(ex.getMessage(), ex));
                Thread.currentThread().interrupt();
            }
        }, "PVGIS-Estimate-Thread").start();
    }
}
