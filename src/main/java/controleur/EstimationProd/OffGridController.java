package controleur.EstimationProd;

import javax.swing.SwingWorker;
import service.validation.InputValidator;
import service.export.ExportContext;
import service.export.CsvExportStrategy;
import service.export.PdfExportStrategy;
import vue.util.ExportWorkerFactory;
import java.util.function.*;

/**
 * Thin controller for the Off-Grid PVGIS page.
 * Encapsulates background estimation and export worker creation.
 */
public final class OffGridController {
    private final service.pvgis.PVGISService service;

    public OffGridController(service.pvgis.PVGISService service) {
        this.service = service;
    }

    /**
    * Creates a SwingWorker that calls PVGIS Off-Grid endpoint in background using PVGISRequest.
     * onSuccess/onError are executed on EDT inside done().
     */
    public SwingWorker<modele.pvgis.PVGISResult, Void> createEstimateWorker(
            modele.pvgis.PVOffGridRequest req,
            Consumer<modele.pvgis.PVGISResult> onSuccess,
            Consumer<Exception> onError
    ) {
        return new SwingWorker<>() {
            @Override
            protected modele.pvgis.PVGISResult doInBackground() throws Exception {
                InputValidator.validateLatitude(req.lat);
                InputValidator.validateLongitude(req.lon);
                InputValidator.validatePeakPower(req.peakPower);
                // angle / aspect optionnels
                InputValidator.validateAngle(req.angle);
                InputValidator.validateAspect(req.aspect);
                return service.fetch(req);
            }

            @Override
            protected void done() {
                try {
                    modele.pvgis.PVGISResult res = get();
                    if (onSuccess != null) onSuccess.accept(res);
                } catch (java.util.concurrent.ExecutionException ex) {
                    if (onError != null) onError.accept(ex);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    if (onError != null) onError.accept(ex);
                }
            }
        };
    }

    public SwingWorker<Void, Void> createCsvExportWorker(
            java.io.File targetFile,
            Supplier<ExportContext> contextSupplier,
            Consumer<Exception> onErrorOnEdt,
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
            Supplier<ExportContext> contextSupplier,
            java.lang.Runnable ensureGraphsOnEdt,
            Consumer<Exception> onErrorOnEdt,
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
