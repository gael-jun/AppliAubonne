package service.pvgis;

import modele.pvgis.PVOffGridRequest;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests ciblés sur la construction d'URL de {@link PVGISService#fetch(PVOffGridRequest)}.
 * Ces tests n'analysent pas le parsing JSON (retourne une réponse minimale). Ils visent
 * à couvrir les branches d'ajout conditionnel de paramètres optionnels.
 */
class PVOffGridServiceUrlBuildTest {

    static class StubClient implements PVGISClient {
        String lastUrl;
        String response = "{\"outputs\":{\"monthly\":[]}}"; // JSON minimal valide
        @Override public String get(String url) { this.lastUrl = url; return response; }
    }

    @Test
    void all_optional_parameters_present() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVOffGridRequest req = new PVOffGridRequest(
                "46.1","6.2","5","10000","50","3000",
                "35","180","PVGIS-SARAH", true, "0,0,0", "10,20,30", "json", true
        );
        PVGISResult r = service.fetch(req);
        assertNotNull(r);
        assertNotNull(stub.lastUrl);
        String url = stub.lastUrl;
        assertTrue(url.contains("lat=46.1"));
        assertTrue(url.contains("&lon=6.2"));
        assertTrue(url.contains("&angle=35"));
        assertTrue(url.contains("&aspect=180"));
        assertTrue(url.contains("&raddatabase=PVGIS-SARAH"));
        assertTrue(url.contains("&usehorizon=1"));
        assertTrue(url.contains("&userhorizon=0,0,0"));
        assertTrue(url.contains("&hourconsumption=10,20,30"));
        assertTrue(url.contains("&outputformat=json"));
        assertTrue(url.contains("&browser=1"));
    }

    @Test
    void no_optional_parameters_added() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVOffGridRequest req = new PVOffGridRequest(
                "46","6","4","8000","40","2500",
                null,null,null,false,null,null,null,false
        );
        service.fetch(req);
        String url = stub.lastUrl;
        assertFalse(url.contains("angle="));
        assertFalse(url.contains("aspect="));
        assertFalse(url.contains("raddatabase="));
        assertTrue(url.contains("usehorizon=0"));
        assertFalse(url.contains("userhorizon="));
        assertFalse(url.contains("hourconsumption="));
        assertFalse(url.contains("outputformat="));
        assertTrue(url.endsWith("browser=0"));
    }

    @Test
    void single_optional_parameter_hourConsumption() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVOffGridRequest req = new PVOffGridRequest(
                "45","5","3","5000","30","2000",
                null,null,null,false,null,"5,5,5",null,false
        );
        service.fetch(req);
        String url = stub.lastUrl;
        assertTrue(url.contains("hourconsumption=5,5,5"));
        assertFalse(url.contains("angle="));
        assertFalse(url.contains("aspect="));
    }
}
