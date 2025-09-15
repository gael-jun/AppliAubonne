package service.pvgis;

import modele.pvgis.PVGridAndTrackerRequest;
import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests sur la construction d'URL de {@link PVGISService#fetchPVcalc(PVGridAndTrackerRequest)}.
 * Couvre les combinaisons de drapeaux de tracking et paramètres optionnels.
 */
class PVCalcServiceUrlBuildTest {

    static class StubClient implements PVGISClient {
        String lastUrl;
        String response = "{\"outputs\":{\"monthly\":{\"fixed\":[]}}}";
        @Override public String get(String url) { this.lastUrl = url; return response; }
    }

    private PVGridAndTrackerRequest base(boolean includeGlobal) {
        return new PVGridAndTrackerRequest(
                "46","6","5","14",null,null,null,
                true,null,null,false,false,false,false,null,false,false,null,false,
                null,null,null,null,false,null,null,false, includeGlobal
        );
    }

    @Test
    void baseline_fixed_no_global() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        service.fetchPVcalc(base(false));
        String url = stub.lastUrl;
        assertTrue(url.contains("fixed=1"));
        assertFalse(url.contains("global=1"));
        assertTrue(url.contains("twoaxis=0"));
    }

    @Test
    void include_global_flag() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        service.fetchPVcalc(base(true));
        assertTrue(stub.lastUrl.contains("global=1"));
    }

    @Test
    void two_axis_priority() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "46","6","5","14",null,null,null,
                false,null,null,false,false,false,false,null,false,false,null,true,
                null,null,null,null,false,null,null,false,true
        );
        service.fetchPVcalc(req);
        assertTrue(stub.lastUrl.contains("twoaxis=1"));
    }

    @Test
    void inclined_axis_with_angle() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "46","6","5","14",null,null,null,
                false,null,null,false,false,true,false,"22",false,false,null,false,
                null,null,null,null,false,null,null,false,false
        );
        service.fetchPVcalc(req);
        String url = stub.lastUrl;
        assertTrue(url.contains("inclined_axis=1"));
        assertTrue(url.contains("inclinedaxisangle=22"));
    }

    @Test
    void vertical_axis_optimum() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "46","6","5","14",null,null,null,
                false,null,null,false,false,false,false,null,true,true,"11",false,
                null,null,null,null,false,null,null,false,false
        );
        service.fetchPVcalc(req);
        String url = stub.lastUrl;
        assertTrue(url.contains("vertical_axis=1"));
        assertTrue(url.contains("vertical_optimum=1"));
        assertTrue(url.contains("verticalaxisangle=11"));
    }

    @Test
    void financial_parameters_and_angle_aspect() throws Exception {
        StubClient stub = new StubClient();
        PVGISService service = new PVGISService(stub);
        PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(
                "46","6","5","14","PVGIS-SARAH","crystSi","roof",
                true,"30","180",false,false,false,false,null,false,false,null,false,
                "900","1200","5","25",true,"0,0,0","json",true,true
        );
        service.fetchPVcalc(req);
        String url = stub.lastUrl;
        assertTrue(url.contains("pvprice=900"));
        assertTrue(url.contains("systemcost=1200"));
        assertTrue(url.contains("interest=5"));
        assertTrue(url.contains("lifetime=25"));
        assertTrue(url.contains("angle=30"));
        assertTrue(url.contains("aspect=180"));
        assertTrue(url.contains("raddatabase=PVGIS-SARAH"));
        assertTrue(url.contains("pvtechchoice=crystSi"));
        assertTrue(url.contains("mountingplace=roof"));
        assertTrue(url.contains("usehorizon=1"));
        assertTrue(url.contains("userhorizon=0,0,0"));
        assertTrue(url.contains("outputformat=json"));
    }
}
