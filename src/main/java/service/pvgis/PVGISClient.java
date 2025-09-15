package service.pvgis;

import java.io.IOException;

/**
 * Abstraction HTTP pour appeler l'API PVGIS.
 * Permet d'injecter une implémentation (HTTP réel, mock pour tests) dans les services.
 */
public interface PVGISClient {
    /**
     * Exécute une requête GET et retourne le corps de réponse en texte.
     * @throws IOException si la requête échoue (statut non 2xx ou I/O)
     * @throws InterruptedException si le thread est interrompu
     */
    String get(String url) throws IOException, InterruptedException;
}
