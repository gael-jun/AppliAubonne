package controleur.EstimationProd;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingWorker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import modele.pvgis.HistogramBucket;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGISResult;
import modele.pvgis.PVGridAndTrackerRequest;
import service.pvgis.PVGISService;

class GridControllerTest {

    @Test
        void createEstimateWorkerSuccessCallsCallback() throws Exception {
        PVGISService mockService = Mockito.mock(PVGISService.class);
        PVGISResult fake = new PVGISResult(
                List.of(new MonthlyResult(1, 5000, 0,0,0)),
                List.of(new HistogramBucket(0,1,0.5)),
                List.of()
        );
        when(mockService.fetchPVcalc(any())).thenReturn(fake);

        GridController controller = new GridController(mockService);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "45","6","1","14",null,null,null,
                true,"","",
                false,false,false,false,"",
                false,false,"",
                false,"","","","",
                false,"","json",false,
                false
        );
        AtomicBoolean successCalled = new AtomicBoolean(false);
        AtomicReference<PVGISResult> captured = new AtomicReference<>();
        AtomicBoolean errorCalled = new AtomicBoolean(false);

        SwingWorker<PVGISResult, Void> worker = controller.createEstimateWorker(
                req,
                r -> { successCalled.set(true); captured.set(r); },
                ex -> errorCalled.set(true)
        );
        // Exécution synchrone
                        worker.execute();
                        PVGISResult result = worker.get(); // attend fin calcul
                        // Attente (polling) du callback (jusqu'à 500ms)
                        long deadline = System.currentTimeMillis() + 500;
                                        while (!successCalled.get() && !errorCalled.get() && System.currentTimeMillis() < deadline) {
                                                try {
                                                        Thread.sleep(10);
                                                } catch (InterruptedException ie) {
                                                        Thread.currentThread().interrupt();
                                                        fail("Thread interrompu pendant le polling");
                                                }
                        }
                        assertTrue(successCalled.get(), "Callback succès non appelé (timeout)");
        assertFalse(errorCalled.get(), "Callback erreur ne devrait pas être appelé");
        assertNotNull(result);
        assertEquals(1, result.monthly.size());
        verify(mockService, times(1)).fetchPVcalc(any());
    }

        @Test
        void invalidLatitudeTriggersErrorAndNoServiceCall() throws Exception {
                PVGISService mockService = Mockito.mock(PVGISService.class);
                GridController controller = new GridController(mockService);
                // latitude invalide 200
                PVGridAndTrackerRequest bad = new PVGridAndTrackerRequest(
                                "200","6","1","14",null,null,null,
                                true,"","",
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
                try { w.get(); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); fail("Interrompu pendant get()"); } catch (Exception ignored) { /* ExecutionException attendu */ }
                long deadline = System.currentTimeMillis() + 300;
                while (!success.get() && !error.get() && System.currentTimeMillis() < deadline) {
                        try { Thread.sleep(10); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); fail("Interrompu"); }
                }
                assertFalse(success.get(), "Succès ne doit pas être appelé");
                assertTrue(error.get(), "Erreur doit être appelée");
                // Aucune invocation service car validation échoue avant fetch
                verify(mockService, times(0)).fetchPVcalc(any());
        }
}
