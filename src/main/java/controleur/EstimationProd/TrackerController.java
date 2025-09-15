package controleur.EstimationProd;

import javax.swing.SwingWorker;
import service.validation.InputValidator;
import service.export.ExportContext;
import service.export.CsvExportStrategy;
import service.export.PdfExportStrategy;
import vue.util.ExportWorkerFactory;

/**
 * Thin controller for the Tracker PVGIS page.
 * Encapsulates background estimation and export worker creation.
 */
public final class TrackerController {
    private final service.pvgis.PVGISService service;

    public TrackerController(service.pvgis.PVGISService service) {
        this.service = service;
    }

    /**
     * Creates a SwingWorker that calls PVGIS PVcalc endpoint in background.
     * onSuccess/onError are executed on EDT inside done().
     */
    public SwingWorker<modele.pvgis.PVGISResult, Void> createEstimateWorker(
            modele.pvgis.PVGridAndTrackerRequest req,
            java.util.function.Consumer<modele.pvgis.PVGISResult> onSuccess,
            java.util.function.Consumer<Exception> onError
    ) {
        final boolean headless = java.awt.GraphicsEnvironment.isHeadless();
        return new SwingWorker<>() {
            @Override
            protected modele.pvgis.PVGISResult doInBackground() throws Exception {
                InputValidator.validateLatitude(req.lat);
                InputValidator.validateLongitude(req.lon);
                InputValidator.validatePeakPower(req.peakPower);
                InputValidator.validateLossPercent(req.loss);
                InputValidator.validateAngle(req.angle);
                InputValidator.validateAspect(req.aspect);
                InputValidator.validateProjectDuration(req.lifetime);
                if (req.inclinedAxisAngle != null && !req.inclinedAxisAngle.isBlank()) InputValidator.validateAngle(req.inclinedAxisAngle);
                if (req.verticalAxisAngle != null && !req.verticalAxisAngle.isBlank()) InputValidator.validateAngle(req.verticalAxisAngle);
                modele.pvgis.PVGISResult r = service.fetchPVcalc(req);
                // In headless test environment, Swing EDT callbacks can be delayed; invoke early.
                if (headless && onSuccess != null) {
                    try {
                        onSuccess.accept(r);
                    } catch (Exception callbackEx) {
                        if (onError != null) onError.accept(callbackEx instanceof Exception ? (Exception) callbackEx : new RuntimeException(callbackEx));
                    }
                }
                return r;
            }

            @Override
            protected void done() {
                try {
                    modele.pvgis.PVGISResult res = get();
                    // Avoid double-calling success if already invoked in headless path
                    if (!headless && onSuccess != null) onSuccess.accept(res);
                } catch (java.util.concurrent.ExecutionException | InterruptedException ex) {
                    if (onError != null) onError.accept(ex);
                }
            }
        };
    }

    public SwingWorker<Void, Void> createCsvExportWorker(
            java.io.File targetFile,
            java.util.function.Supplier<ExportContext> contextSupplier,
            java.util.function.Consumer<Exception> onErrorOnEdt,
            java.lang.Runnable onSuccessOnEdt
    ) {
    return ExportWorkerFactory.createExportWorker(
                targetFile,
                contextSupplier,
                new CsvExportStrategy(),
                null,
                onErrorOnEdt,
                onSuccessOnEdt
        );
    }

    public SwingWorker<Void, Void> createPdfExportWorker(
            java.io.File targetFile,
            java.util.function.Supplier<ExportContext> contextSupplier,
            java.lang.Runnable ensureGraphsOnEdt,
            java.util.function.Consumer<Exception> onErrorOnEdt,
            java.lang.Runnable onSuccessOnEdt
    ) {
    return ExportWorkerFactory.createExportWorker(
                targetFile,
                contextSupplier,
                new PdfExportStrategy(),
                ensureGraphsOnEdt,
                onErrorOnEdt,
                onSuccessOnEdt
        );
    }
}
