package service.pvgis;

import modele.pvgis.PVGISResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests de base pour PVGISParser: priorité des séries trackers et irradiation. */
public class PVGISParserTest {

    @Test
    void testParseFixedMonthly_ok() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": {\n" +
                "      \"fixed\": [\n" +
                "        {\"month\":1,\"E_m\":50,\"H(i)_m\":100},\n" +
                "        {\"month\":2,\"E_m\":40,\"H(i)_m\":90}\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.monthly.size());
        // E_m(kWh/mois) -> Wh/jour: (50*1000)/31 ≈ 1612.9
        double eDayMonth1 = r.monthly.get(0).E_d;
        assertEquals((50*1000.0)/31.0, eDayMonth1, 0.5); // tolérance
        assertEquals(2, r.irradiationKWhPerMonth.size());
        assertEquals(100.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void testParse_twoAxis_prioritaire() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": {\n" +
                "      \"two_axis\": [ {\"month\":1,\"E_m\":10,\"H(i)_m\":11} ],\n" +
                "      \"fixed\": [ {\"month\":1,\"E_m\":99,\"H(i)_m\":111} ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        PVGISResult r = PVGISParser.parse(json);
        // Doit choisir two_axis (E_m=10) pas fixed (E_m=99)
        assertEquals(1, r.monthly.size());
        double eDayWh = r.monthly.get(0).E_d; // (10*1000)/31 ≈ 322.6
        assertTrue(eDayWh < 400, "Devrait refléter la série two_axis");
        assertEquals(1, r.irradiationKWhPerMonth.size());
        assertEquals(11.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void testParse_verticalAxis_fallback() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": {\n" +
                "      \"vertical_axis\": [ {\"month\":1,\"E_m\":20,\"H(i)_m\":21} ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1, r.monthly.size());
        assertEquals(21.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void testParse_irradiation_H_m_fallback() {
        // Pas de H(i)_m, mais H_m présent
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": {\n" +
                "      \"fixed\": [ {\"month\":1,\"E_m\":30,\"H_m\":55} ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1, r.irradiationKWhPerMonth.size());
        assertEquals(55.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void testParse_invalidJson_retourVide() {
        PVGISResult r = PVGISParser.parse("not a json");
        assertTrue(r.monthly.isEmpty());
        assertTrue(r.irradiationKWhPerMonth.isEmpty());
    }

    @Test
    void testParse_inclinedAxis_fallback_apresVerticalEtTwoAxisAbsents() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": { \n" +
                "       \"inclined_axis\": [ {\"month\":3,\"E_m\":33,\"H(i)_m\":44} ] \n" +
                "    }\n" +
                "  }\n" +
                "}"; // only inclined_axis
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1, r.monthly.size());
        assertEquals(44.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void testParse_optimal_fallback_apresInclinedAbsent() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": { \n" +
                "       \"optimal\": [ {\"month\":4,\"E_m\":24,\"H(i)_m\":30} ] \n" +
                "    }\n" +
                "  }\n" +
                "}"; // only optimal
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1, r.monthly.size());
        assertEquals(30.0, r.irradiationKWhPerMonth.get(0));
    }

    @Test
    void testParse_mismatchIrradiation_tailleDifferentIgnoree() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": { \n" +
                "       \"fixed\": [ {\"month\":1,\"E_m\":10,\"H(i)_m\":1}, {\"month\":2,\"E_m\":12} ] \n" +
                "    }\n" +
                "  }\n" +
                "}"; // 2 months, irradiation list aura 1 élément => doit être vidée
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.monthly.size());
        assertTrue(r.irradiationKWhPerMonth.isEmpty(), "Irradiation non alignée doit être vide");
    }

    @Test
    void testParse_histogram_ok() {
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": { \"fixed\": [ {\"month\":1,\"E_m\":5} ] },\n" +
                "    \"histogram\": [ {\"CS_min\":0,\"CS_max\":50,\"f_CS\":0.2}, {\"CS_min\":50,\"CS_max\":100,\"f_CS\":0.8} ]\n" +
                "  }\n" +
                "}";
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(2, r.histogram.size());
        assertEquals(50.0, r.histogram.get(0).CS_max);
    }

    @Test
    void testParse_Ed_fallback() {
        // Utilise champ 'Ed' pour vérifier fallback (rare)
        String json = "{\n" +
                "  \"outputs\": {\n" +
                "    \"monthly\": { \"fixed\": [ {\"month\":1,\"Ed\":1.5} ] }\n" +
                "  }\n" +
                "}"; // Ed en kWh/jour => Wh/jour = 1500
        PVGISResult r = PVGISParser.parse(json);
        assertEquals(1500.0, r.monthly.get(0).E_d, 0.0001);
    }

    @Test
    void testParse_monthlyAbsent_resultVide() {
        String json = "{ \"outputs\": { } }"; // pas de monthly
        PVGISResult r = PVGISParser.parse(json);
        assertTrue(r.monthly.isEmpty());
        assertTrue(r.histogram.isEmpty());
    }
}
