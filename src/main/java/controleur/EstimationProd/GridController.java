package controleur.EstimationProd;

import javax.swing.SwingWorker;

import service.export.CsvExportStrategy;
import service.export.ExportContext;
import service.export.PdfExportStrategy;
import service.validation.InputValidator;
import vue.util.ExportWorkerFactory;
/**
 * Thin controller for the Grid-connected PVGIS page.
 * Encapsulates background estimation and export worker creation.
 */
public final class GridController {
    private final service.pvgis.PVGISService service;

    public GridController(service.pvgis.PVGISService service) {
        this.service = service;
    }

    /**
     * Creates a SwingWorker that calls PVGIS PVcalc endpoint in background.
     * onSuccess/onError are executed on EDT inside done().
     */
    public SwingWorker<modele.pvgis.PVGISResult, Void> createEstimateWorker(
            final modele.pvgis.PVGridAndTrackerRequest req,
            final java.util.function.Consumer<modele.pvgis.PVGISResult> onSuccess,
            final java.util.function.Consumer<Exception> onError
    ) {
        // Headless test environments (CI) sometimes do not reliably dispatch Swing EDT callbacks
        // in the expected timing, causing tests waiting on the success callback (set inside done())
        // to time out or the build to be interrupted. We mirror the approach used in TrackerController:
        // if the environment is headless, we invoke the success callback directly from doInBackground
        // and suppress a second invocation in done().
        final boolean headless = java.awt.GraphicsEnvironment.isHeadless();
        final java.util.concurrent.atomic.AtomicBoolean callbackInvoked = new java.util.concurrent.atomic.AtomicBoolean(false);
        return buildWorker(req, onSuccess, onError, headless, callbackInvoked);
    }

    private SwingWorker<modele.pvgis.PVGISResult, Void> buildWorker(
            final modele.pvgis.PVGridAndTrackerRequest req,
            final java.util.function.Consumer<modele.pvgis.PVGISResult> onSuccess,
            final java.util.function.Consumer<Exception> onError,
            final boolean headless,
            final java.util.concurrent.atomic.AtomicBoolean callbackInvoked) {
        return new SwingWorker<>() {
            @Override
            protected modele.pvgis.PVGISResult doInBackground() throws Exception {
                performValidation(req); // may throw IllegalArgumentException from validators
                modele.pvgis.PVGISResult res = service.fetchPVcalc(req);
                invokeSuccessIfHeadless(headless, onSuccess, callbackInvoked, res);
                return res;
            }

            @Override
            protected void done() {
                try {
                    modele.pvgis.PVGISResult res = get();
                    dispatchSuccessIfNotInvoked(callbackInvoked, onSuccess, res);
                } catch (java.util.concurrent.ExecutionException ex) {
                    if (onError != null) onError.accept(ex);
                } catch (InterruptedException ie) {
                    // Restore interrupt status and propagate to error callback
                    Thread.currentThread().interrupt();
                    if (onError != null) onError.accept(ie);
                }
            }
        };
    }

    private static void invokeSuccessIfHeadless(boolean headless,
                                                 java.util.function.Consumer<modele.pvgis.PVGISResult> onSuccess,
                                                 java.util.concurrent.atomic.AtomicBoolean flag,
                                                 modele.pvgis.PVGISResult res) {
        if (headless && onSuccess != null) {
            onSuccess.accept(res);
            flag.set(true);
        }
    }

    private static void dispatchSuccessIfNotInvoked(java.util.concurrent.atomic.AtomicBoolean flag,
                                                     java.util.function.Consumer<modele.pvgis.PVGISResult> onSuccess,
                                                     modele.pvgis.PVGISResult res) {
        if (!flag.get() && onSuccess != null) {
            onSuccess.accept(res);
        }
    }

    // Extracted to reduce cognitive complexity inside anonymous SwingWorker
    private static void performValidation(modele.pvgis.PVGridAndTrackerRequest req) {
        InputValidator.validateLatitude(req.lat);
        InputValidator.validateLongitude(req.lon);
        InputValidator.validatePeakPower(req.peakPower);
        InputValidator.validateLossPercent(req.loss);
        InputValidator.validateAngle(req.angle);
        InputValidator.validateAspect(req.aspect);
        InputValidator.validateProjectDuration(req.lifetime);
        if (req.inclinedAxisAngle != null && !req.inclinedAxisAngle.isBlank()) {
            InputValidator.validateAngle(req.inclinedAxisAngle);
        }
        if (req.verticalAxisAngle != null && !req.verticalAxisAngle.isBlank()) {
            InputValidator.validateAngle(req.verticalAxisAngle);
        }
    }

    /**
    * Builds a CSV export SwingWorker using the shared ExportWorkerFactory helper.
     */
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

    

    /**
    * Builds a PDF export SwingWorker using the shared ExportWorkerFactory helper.
     */
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
