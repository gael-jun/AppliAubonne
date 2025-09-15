package service.pvgis;

import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests parser pour la forme Off-Grid (outputs.monthly = array). */
class PVGISParserOffGridTest {

    @Test
    void empty_json_returns_empty_result() {
        PVGISResult r = PVGISParser.parse("");
        assertTrue(r.monthly.isEmpty());
        assertTrue(r.histogram.isEmpty());
    }

    @Test
    void offgrid_monthly_array_and_missing_fields() {
        String json = "{\"outputs\":{\"monthly\":[{" +
                "\"month\":1,\"E_d\":1.2,\"E_lost_d\":0.1,\"f_f\":0.5,\"f_e\":0.6},{" +
                // second item missing E_d -> 0
                "\"month\":2,\"E_lost_d\":0.2}" +
                "]}}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.monthly.size());
    // Off-grid shape fournit déjà E_d en Wh/j selon parser (pas de multiplication) => reste 1.2
    assertEquals(1.2, r.monthly.get(0).E_d, 0.0001);
    assertEquals(0.0, r.monthly.get(1).E_d, 0.0001);
    }

    @Test
    void histogram_present() {
        String json = "{\"outputs\":{\"monthly\":[],\"histogram\":[{" +
                "\"CS_min\":0,\"CS_max\":10,\"f_CS\":0.2},{" +
                "\"CS_min\":10,\"CS_max\":20,\"f_CS\":0.3}]}}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.histogram.size());
    assertEquals(10.0, r.histogram.get(0).CS_max, 0.0001);
    }
}
