package controller;

import javax.swing.SwingWorker;
import service.export.*;
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
            modele.pvgis.PVGridAndTrackerRequest req,
            java.util.function.Consumer<modele.pvgis.PVGISResult> onSuccess,
            java.util.function.Consumer<Exception> onError
    ) {
        return new SwingWorker<>() {
            @Override
            protected modele.pvgis.PVGISResult doInBackground() throws Exception {
                return service.fetchPVcalc(req);
            }

            @Override
            protected void done() {
                try {
                    modele.pvgis.PVGISResult res = get();
                    if (onSuccess != null) onSuccess.accept(res);
                } catch (java.util.concurrent.ExecutionException | InterruptedException ex) {
                    if (onError != null) onError.accept(ex);
                }
            }
        };
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
