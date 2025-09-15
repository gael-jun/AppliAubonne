package controleur.EstimationProd;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingWorker;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import modele.pvgis.PVGISResult;
import modele.pvgis.PVGridAndTrackerRequest;
import service.pvgis.PVGISService;

class TrackerControllerTest {

    private PVGridAndTrackerRequest minimalReq(boolean twoAxis){
        return new PVGridAndTrackerRequest(
                "45","6","2","12",null,null,null,
                false,"","",
                false,false,false,false,"",
                false,false,"",
                twoAxis,"","","","",
                false,"","json",false,
                false
        );
    }

    @Test
    void successTwoAxis() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        PVGISResult fake = new PVGISResult(List.of(), List.of());
        when(mockService.fetchPVcalc(any())).thenReturn(fake);
        TrackerController controller = new TrackerController(mockService);
        AtomicBoolean called = new AtomicBoolean(false);
        SwingWorker<PVGISResult, Void> w = controller.createEstimateWorker(
                minimalReq(true), r -> called.set(true), ex -> fail("Erreur inattendue")
        );
    w.execute();
    long deadline = System.currentTimeMillis() + 500; // 500 ms timeout
    while (!called.get() && System.currentTimeMillis() < deadline) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            fail("Thread interrompu pendant polling succès tracker");
        }
    }
    assertTrue(called.get(), "Callback succès non appelé (tracker deux axes)");
        verify(mockService, times(1)).fetchPVcalc(any());
    }

    @Test
    void errorPropagated() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        when(mockService.fetchPVcalc(any())).thenThrow(new java.io.IOException("network"));
        TrackerController controller = new TrackerController(mockService);
        AtomicBoolean error = new AtomicBoolean(false);
        SwingWorker<PVGISResult, Void> w = controller.createEstimateWorker(
                minimalReq(false), r -> fail("Succès inattendu"), ex -> error.set(true)
        );
    w.execute();
    // Attendre que le worker termine (get peut lancer ExecutionException)
    try { w.get(); } catch (Exception ignored) { }
    long deadline = System.currentTimeMillis() + 500;
    while (!error.get() && System.currentTimeMillis() < deadline) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            fail("Thread interrompu pendant polling erreur tracker");
        }
    }
    assertTrue(error.get(), "Callback erreur non appelé (tracker)");
    }

    @Test
    void invalidLatitudeDoesNotCallService() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        TrackerController controller = new TrackerController(mockService);
        PVGridAndTrackerRequest bad = new PVGridAndTrackerRequest(
                "-200","6","2","12",null,null,null,
                false,"","",
                false,false,false,false,"",
                false,false,"",
                false,"","","","",
                false,"","json",false,
                false
        );
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicBoolean error = new AtomicBoolean(false);
        SwingWorker<PVGISResult, Void> w = controller.createEstimateWorker(bad, r -> success.set(true), ex -> error.set(true));
        w.execute();
    try { w.get(); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); fail("Interrompu"); } catch (Exception ignored) { /* ExecutionException attendu enveloppant IllegalArgumentException validation */ }
        long deadline = System.currentTimeMillis() + 300;
        while (!success.get() && !error.get() && System.currentTimeMillis() < deadline) {
            try { Thread.sleep(10); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); fail("Interrompu"); }
        }
        assertTrue(error.get(), "Erreur attendue non levée/propagée");
        assertTrue(!success.get(), "Succès inattendu");
        verify(mockService, times(0)).fetchPVcalc(any());
    }
}
