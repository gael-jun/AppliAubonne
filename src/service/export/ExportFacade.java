package service.export;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class ExportFacade {
    public void export(ExportStrategy strategy, File file, ExportContext context) throws IOException {
        Objects.requireNonNull(strategy, "strategy");
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(context, "context");
        if (context.pvgisResult == null) {
            throw new IOException("Aucun résultat PVGIS à exporter");
        }
        strategy.exportTo(file, context);
    }
}
