package service.pvgis;

import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Tests parser pour la forme PVcalc (outputs.monthly objet avec séries). */
class PVGISParserPVCalcTest {

    @Test
    void pvcalc_fixed_Em_converted_and_irradiation_aligned() {
        String json = "{\"outputs\":{\"monthly\":{\"fixed\":[{" +
                "\"month\":1,\"E_m\":31.0,\"H(i)_m\":50.0},{" +
                "\"month\":4,\"E_m\":30.0}]}}}"; // Janvier(31) Avril(30) -> 1000 Wh/j chacune
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.monthly.size());
    assertEquals(1000.0, r.monthly.get(0).E_d, 0.0001);
    assertEquals(1000.0, r.monthly.get(1).E_d, 0.0001);
    // Le parser ne garde l'irradiation que si la taille correspond exactement au nombre de mois
    // Ici il n'y a qu'une valeur sur deux -> liste vide attendue
    assertTrue(r.irradiationKWhPerMonth.isEmpty());
    }

    @Test
    void pvcalc_two_axis_priority() {
        String json = "{\"outputs\":{\"monthly\":{\"two_axis\":[{\"month\":1,\"E_d\":2.0}]}}}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1, r.monthly.size());
    assertEquals(2000.0, r.monthly.get(0).E_d, 0.0001); // 2 kWh/j -> 2000 Wh/j
    }

    @Test
    void pvcalc_vertical_axis_if_no_two_axis() {
        String json = "{\"outputs\":{\"monthly\":{\"vertical_axis\":[{\"month\":2,\"E_d\":1.5}]}}}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1, r.monthly.size());
    assertEquals(1500.0, r.monthly.get(0).E_d, 0.0001);
    }

    @Test
    void pvcalc_fallback_Ed_key() {
        String json = "{\"outputs\":{\"monthly\":{\"fixed\":[{\"month\":3,\"Ed\":1.1}]}}}";
        PVGISResult r = PVGISParser.parse(json);
    assertEquals(1100.0, r.monthly.get(0).E_d, 0.0001);
    }

    @Test
    void pvcalc_irradiation_mismatch_ignored() {
        String json = "{\"outputs\":{\"monthly\":{\"fixed\":[{\"month\":1,\"E_m\":31.0,\"H(i)_m\":10.0},{\"month\":2,\"E_m\":28.0,\"H(i)_m\":11.0},{\"month\":3,\"E_m\":30.0}]}}}"; // irradiation size != monthly size -> ignorée
        PVGISResult r = PVGISParser.parse(json);
        assertTrue(r.irradiationKWhPerMonth.isEmpty());
    }

    @Test
    void pvcalc_histogram_and_H_m() {
        String json = "{\"outputs\":{\"monthly\":{\"fixed\":[{\"month\":1,\"E_m\":31.0,\"H_m\":12.0}]},\"histogram\":[{\"CS_min\":0,\"CS_max\":5},{\"CS_min\":5,\"CS_max\":10}]}}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.histogram.size());
        assertEquals(12.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void invalid_json_returns_empty() {
        PVGISResult r = PVGISParser.parse("{\"outputs\":{\"monthly\":{\"fixed\":["); // tronqué
        assertTrue(r.monthly.isEmpty());
        assertTrue(r.histogram.isEmpty());
    }
}
