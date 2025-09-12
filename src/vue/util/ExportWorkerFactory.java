package vue.util;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.function.Supplier;
import java.util.function.Consumer;
import service.export.ExportStrategy;
import service.export.ExportContext;
import service.export.ExportFacade;

/**
 * Small helper to build background SwingWorkers for CSV/PDF exports.
 * Callers provide the ExportContext supplier, the ExportStrategy, and UI callbacks.
 */
public final class ExportWorkerFactory {
    private ExportWorkerFactory() {}

    /**
     * Creates a SwingWorker that builds an ExportContext via the given supplier and exports
     * with the provided strategy to the target file. Graph rendering can be ensured on EDT
     * via ensureGraphsOnEdt (optional). Success/error callbacks execute on EDT inside done().
     */
    public static SwingWorker<Void, Void> createExportWorker(
            java.io.File targetFile,
            Supplier<ExportContext> contextSupplier,
            ExportStrategy strategy,
            Runnable ensureGraphsOnEdt,
            Consumer<Exception> onErrorOnEdt,
            Runnable onSuccessOnEdt
    ) {
        return new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (ensureGraphsOnEdt != null) {
                    try {
                        SwingUtilities.invokeAndWait(ensureGraphsOnEdt);
                    } catch (InterruptedException | java.lang.reflect.InvocationTargetException ignored) {
                        // Ignore; charts will be added if possible by the caller logic
                    }
                }
                ExportContext context = contextSupplier.get();
                new ExportFacade().export(strategy, targetFile, context);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    if (onSuccessOnEdt != null) onSuccessOnEdt.run();
                } catch (java.util.concurrent.ExecutionException | InterruptedException ex) {
                    if (onErrorOnEdt != null) onErrorOnEdt.accept(ex);
                }
            }
        };
    }
}
