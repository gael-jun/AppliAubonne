package service.export;

import java.io.File;
import java.io.IOException;

public interface ExportStrategy {
    void exportTo(File file, ExportContext context) throws IOException;
}
