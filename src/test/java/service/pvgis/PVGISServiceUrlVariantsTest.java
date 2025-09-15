package service.pvgis;

import modele.pvgis.PVGridAndTrackerRequest;
import modele.pvgis.PVOffGridRequest;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Couvre diverses variantes d'URL pour augmenter la couverture de PVGISService.
 */
class PVGISServiceUrlVariantsTest {

    static class CapturingClient implements PVGISClient {
        final AtomicReference<String> lastUrl = new AtomicReference<>();
        private final String body;
        CapturingClient(String body){ this.body = body; }
        @Override public String get(String url) throws IOException, InterruptedException { lastUrl.set(url); return body; }
    }

    private String minimalResponse(){
        // JSON minimal pour parser: monthly production vide
        return "{\"outputs\":{\"monthly\":[],\"totals\":{}}}";
    }

    private PVGridAndTrackerRequest baseReq(){
        return new PVGridAndTrackerRequest(
                "45","6","1","14","PVGIS-SARAH","crystSi","roof",
                true,"30","0",
                false,false,false,false,"",
                false,false,"",
                false,"","","","",
                false,"","json",false,
                false
        );
    }

    @Test
    void twoAxisAndGlobalAndAngles() throws Exception {
        CapturingClient client = new CapturingClient(minimalResponse());
        PVGISService service = new PVGISService(client);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "45","6","2","15","PVGIS-SARAH","crystSi","free",
                false,"25","180",
                false,false,true,false,"15", // inclinedAxis= true with angle
                true,false,"30",              // verticalAxis = true
                true,"1200","5000","3","20", // twoAxis + finance params
                true,"userH","json",false,
                true // includeGlobal
        );
        PVGISResult r = service.fetchPVcalc(req);
        String url = client.lastUrl.get();
        assertNotNull(r);
        assertTrue(url.contains("twoaxis=1"));
        assertTrue(url.contains("inclined_axis=1"));
        assertTrue(url.contains("vertical_axis=1"));
        assertTrue(url.contains("inclinedaxisangle=15"));
        assertTrue(url.contains("verticalaxisangle=30"));
        assertTrue(url.contains("pvprice=1200"));
        assertTrue(url.contains("systemcost=5000"));
        assertTrue(url.contains("interest=3"));
        assertTrue(url.contains("lifetime=20"));
        assertTrue(url.contains("global=1"));
        assertTrue(url.contains("userhorizon=userH"));
    }

    @Test
    void offGridHorizonAndAngleAspect() throws Exception {
        CapturingClient client = new CapturingClient(minimalResponse());
        PVGISService service = new PVGISService(client);
        PVOffGridRequest req = new PVOffGridRequest(
                "44","5","3","9000","50","3000","25","180","PVGIS-SARAH",
                true,"userH","","json",false
        );
        PVGISResult r = service.fetch(req);
        String url = client.lastUrl.get();
        assertNotNull(r);
        assertTrue(url.contains("batterysize=9000"));
        assertTrue(url.contains("cutoff=50"));
        assertTrue(url.contains("consumptionday=3000"));
        assertTrue(url.contains("angle=25"));
        assertTrue(url.contains("aspect=180"));
        assertTrue(url.contains("raddatabase=PVGIS-SARAH"));
        assertTrue(url.contains("usehorizon=1"));
        assertTrue(url.contains("userhorizon=userH"));
    }

    @Test
    void minimalGridOptionalOmitted() throws Exception {
        CapturingClient client = new CapturingClient(minimalResponse());
        PVGISService service = new PVGISService(client);
        PVGridAndTrackerRequest req = baseReq();
        service.fetchPVcalc(req);
        String url = client.lastUrl.get();
        assertFalse(url.contains("pvprice="));
        assertFalse(url.contains("inclinedaxisangle="));
        assertFalse(url.contains("verticalaxisangle="));
        assertFalse(url.contains("global=1"));
    }
}
