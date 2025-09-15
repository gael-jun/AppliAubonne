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
import modele.pvgis.PVOffGridRequest;
import service.pvgis.PVGISService;

class OffGridControllerTest {

    private PVOffGridRequest req(){
        return new PVOffGridRequest(
                "44","5","3","9000","50","3000","","","PVGIS-SARAH",
                true,"","","json",false
        );
    }

    @Test
    void successCallback() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        PVGISResult fake = new PVGISResult(List.of(), List.of());
        when(mockService.fetch(any(PVOffGridRequest.class))).thenReturn(fake);
        OffGridController controller = new OffGridController(mockService);
        AtomicBoolean success = new AtomicBoolean(false);
        SwingWorker<PVGISResult, Void> w = controller.createEstimateWorker(
                req(), r -> success.set(true), ex -> fail("Erreur inattendue")
        );
        w.execute();
        long deadline = System.currentTimeMillis() + 500;
        while (!success.get() && System.currentTimeMillis() < deadline) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                fail("Thread interrompu pendant polling succès offgrid");
            }
        }
        assertTrue(success.get(), "Callback succès non appelé (offgrid)");
        verify(mockService, times(1)).fetch(any(PVOffGridRequest.class));
    }

    @Test
    void errorCallback() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        when(mockService.fetch(any(PVOffGridRequest.class))).thenThrow(new java.io.IOException("io"));
        OffGridController controller = new OffGridController(mockService);
        AtomicBoolean error = new AtomicBoolean(false);
        SwingWorker<PVGISResult, Void> w = controller.createEstimateWorker(
                req(), r -> fail("Succès inattendu"), ex -> error.set(true)
        );
        w.execute();
        try {
            w.get();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            fail("Thread interrompu pendant w.get() erreur offgrid");
        } catch (Exception ignored) {
            // ExecutionException attendu
        }
        long deadline = System.currentTimeMillis() + 500;
        while (!error.get() && System.currentTimeMillis() < deadline) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                fail("Thread interrompu pendant polling erreur offgrid");
            }
        }
        assertTrue(error.get(), "Callback erreur non appelé (offgrid)");
    }

    @Test
    void invalidLatitudeNoServiceCall() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        OffGridController controller = new OffGridController(mockService);
        PVOffGridRequest bad = new PVOffGridRequest(
                "123","5","3","9000","50","3000","","","PVGIS-SARAH",
                true,"","","json",false
        );
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicBoolean error = new AtomicBoolean(false);
        SwingWorker<PVGISResult, Void> w = controller.createEstimateWorker(bad, r -> success.set(true), ex -> error.set(true));
        w.execute();
        try { w.get(); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); fail("Interrompu"); } catch (Exception ignored) { /* ExecutionException attendu */ }
        long deadline = System.currentTimeMillis() + 300;
        while (!success.get() && !error.get() && System.currentTimeMillis() < deadline) {
            try { Thread.sleep(10); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); fail("Interrompu"); }
        }
        assertTrue(error.get(), "Erreur attendue non propagée");
        assertTrue(!success.get(), "Succès inattendu");
        verify(mockService, times(0)).fetch(any(PVOffGridRequest.class));
    }
}
