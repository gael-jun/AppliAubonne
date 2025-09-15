package service.pvgis;

import modele.pvgis.PVGridAndTrackerRequest;
import modele.pvgis.PVOffGridRequest;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/** Tests ciblés sur la construction d'URL (PVcalc & SHScalc) et la robustesse parsing. */
class PVGISServiceTest {

    /** Stub de client HTTP capturant la dernière URL appelée. */
    static class StubClient implements PVGISClient {
        final AtomicReference<String> lastUrl = new AtomicReference<>();
        String jsonResponse = "{\n" +
                "  \"outputs\": { \n" +
                "    \"monthly\": [ { \"month\":1, \"E_d\": 1000.0, \"E_m\": 1000.0, \"H(i)_m\": 50.0, \"SD_m\":0.0, \"SD_H\":0.0, \"E_lost_d\":0.0, \"f_f\":0.0, \"f_e\":0.0 } ]\n" +
                "  }\n" +
                "}";
        @Override public String get(String url) { lastUrl.set(url); return jsonResponse; }
    }

    @Test
    void testPVcalcTrackerUrlBuild() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "45.0","6.0","10","14","PVGIS-SARAH","crystSi","roof",
                false, // fixed
                "35","180", // angle / aspect
                false, // optimalInclination
                true,  // optimalAngles
                false,false,"",      // inclined flags + angle
                false,false,"",      // vertical flags + angle
                true,  // twoAxis
                "","","","",   // pvPrice/systemCost/interest/lifetime
                false,"","json",false, // useHorizon, userHorizon, outputFormat, browser
                true   // includeGlobal
        );
        PVGISResult res = service.fetchPVcalc(req);
        assertNotNull(res);
        String url = stub.lastUrl.get();
        assertNotNull(url);
        assertTrue(url.startsWith("https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?"));
        assertTrue(url.contains("lat=45.0"));
        assertTrue(url.contains("twoaxis=1"));
        assertTrue(url.contains("fixed=0"));
        assertTrue(url.contains("optimalangles=1"));
        assertTrue(url.contains("global=1"));
        assertTrue(url.contains("angle=35"));
        assertTrue(url.contains("aspect=180"));
        assertEquals(1, res.monthly.size());
    assertEquals(1000.0, res.monthly.get(0).E_d, 1e-6);
    // Le stub JSON est de forme SHScalc simplifiée, irradiation non extraite (liste vide)
    assertTrue(res.irradiationKWhPerMonth.isEmpty());
    }

    @Test
    void testOffGridUrlBuild() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVOffGridRequest req = new PVOffGridRequest(
                "44.1","5.2","5","8000","50","3000","","","PVGIS-SARAH",
                true,"","","json",false
        );
        PVGISResult res = service.fetch(req);
        assertNotNull(res);
        String url = stub.lastUrl.get();
        assertTrue(url.startsWith("https://re.jrc.ec.europa.eu/api/v5_3/SHScalc?"));
        assertTrue(url.contains("batterysize=8000"));
        assertTrue(url.contains("consumptionday=3000"));
        assertTrue(url.contains("usehorizon=1"));
        assertTrue(url.contains("raddatabase=PVGIS-SARAH"));
        assertEquals(1, res.monthly.size());
    }

    @Test
    void testMalformedJsonGraceful() throws Exception {
        StubClient stub = new StubClient();
        stub.jsonResponse = "{not json}"; // JSON mal formé volontaire
        PVGISService service = new PVGISService(stub);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "45","6","1","14",null,null,null,
                true,"","",  // fixed, angle, aspect
                false,false,false,false,"", // inclined
                false,false,"", // vertical
                false, // twoAxis
                "","","","", // finance
                false,"","json",false, // horizon/output
                false // includeGlobal
        );
        PVGISResult res = service.fetchPVcalc(req);
        assertNotNull(res);
        assertTrue(res.monthly.isEmpty(), "Monthly list should be empty on malformed JSON");
    }
}
