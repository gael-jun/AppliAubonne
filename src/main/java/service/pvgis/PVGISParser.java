package service.pvgis;

import modele.pvgis.HistogramBucket;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGISResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse les réponses JSON de l'API PVGIS en objets typés (PVGISResult).
 * Cette implémentation est tolérante: en cas de JSON invalide ou de champs manquants,
 * elle renvoie un résultat vide plutôt que de lever une exception.
 */
public final class PVGISParser {
    private PVGISParser() {}

    /**
     * Convertit une chaîne JSON PVGIS en {@link PVGISResult}.
     * Champs pris en charge:
     *  - outputs.monthly.fixed[]: month, E_m (kWh/mois) ou E_d (kWh/jour), E_lost_d (Wh/jour), f_f, f_e
     *  - outputs.histogram[]: CS_min, CS_max, f_CS (si présent)
     * Valeurs non trouvées → 0. Les unités sont normalisées en Wh/jour pour E_d.
     */
    public static PVGISResult parse(String json) {
        if (json == null || json.isBlank()) {
            return new PVGISResult(List.of(), List.of());
        }
        try {
            org.json.JSONObject root = new org.json.JSONObject(json);
            if (!root.has("outputs")) {
                return new PVGISResult(List.of(), List.of());
            }
            org.json.JSONObject outputs = root.getJSONObject("outputs");

            // --- Monthly ---
            List<MonthlyResult> monthly = new ArrayList<>();
            List<Double> irradiation = new ArrayList<>();
            if (outputs.has("monthly")) {
                Object monthlyNode = outputs.get("monthly");
                if (monthlyNode instanceof org.json.JSONArray monthlyArr) {
                    // Off-Grid shape: outputs.monthly is an array with E_d (Wh/day), E_lost_d, f_f, f_e
                    for (int i = 0; i < monthlyArr.length(); i++) {
                        org.json.JSONObject m = monthlyArr.getJSONObject(i);
                        int month = m.optInt("month", i + 1);
                        double e_d_wh = m.optDouble("E_d", 0.0); // already Wh/day for SHScalc
                        double e_lost_d_wh = m.optDouble("E_lost_d", 0.0);
                        double f_f = m.optDouble("f_f", 0.0);
                        double f_e = m.optDouble("f_e", 0.0);
                        monthly.add(new MonthlyResult(month, e_d_wh, e_lost_d_wh, f_f, f_e));
                    }
                } else if (monthlyNode instanceof org.json.JSONObject monthlyObj) {
                    // PVcalc shape: outputs.monthly.{fixed|optimal|inclined_axis|vertical_axis|two_axis}[]
                    // Selon les options de tracking choisies, la série pertinente peut ne PAS être "fixed".
                    // On choisit la première série existante dans un ordre de priorité du plus spécifique au plus générique.
                    String[] candidateKeys = {"two_axis", "vertical_axis", "inclined_axis", "optimal", "fixed"};
                    org.json.JSONArray chosen = null;
                    for (String k : candidateKeys) {
                        if (monthlyObj.has(k) && monthlyObj.optJSONArray(k) != null) { chosen = monthlyObj.optJSONArray(k); break; }
                    }
                    if (chosen != null) {
                        for (int i = 0; i < chosen.length(); i++) {
                            org.json.JSONObject m = chosen.getJSONObject(i);
                            int month = m.optInt("month", i + 1);
                            int days = switch (month) { case 4,6,9,11 -> 30; case 2 -> 28; default -> 31; };
                            // Normalisation production: E_m (kWh/mois) → Wh/jour ou E_d (kWh/jour) → Wh/jour
                            double e_d_wh;
                            if (m.has("E_m")) {
                                double Em_kwh_per_month = m.optDouble("E_m", 0.0);
                                e_d_wh = (Em_kwh_per_month * 1000.0) / Math.max(1, days);
                            } else if (m.has("E_d")) {
                                double Ed_kwh_per_day = m.optDouble("E_d", 0.0);
                                e_d_wh = Ed_kwh_per_day * 1000.0;
                            } else if (m.has("Ed")) { // fallback défensif
                                double Ed_kwh_per_day = m.optDouble("Ed", 0.0);
                                e_d_wh = Ed_kwh_per_day * 1000.0;
                            } else {
                                e_d_wh = 0.0;
                            }
                            double e_lost_d_wh = m.optDouble("E_lost_d", 0.0);
                            double f_f = m.optDouble("f_f", 0.0);
                            double f_e = m.optDouble("f_e", 0.0);
                            monthly.add(new MonthlyResult(month, e_d_wh, e_lost_d_wh, f_f, f_e));
                            // Irradiation (ex: H(i)_m) kWh/mois
                            if (m.has("H(i)_m")) {
                                irradiation.add(m.optDouble("H(i)_m", 0.0));
                            } else if (m.has("H_m")) { // certains modes trackers utilisent H_m
                                irradiation.add(m.optDouble("H_m", 0.0));
                            }
                        }
                    }
                }
            }

            // --- Histogram ---
            List<HistogramBucket> histogram = new ArrayList<>();
            if (outputs.has("histogram")) {
                org.json.JSONArray histArr = outputs.optJSONArray("histogram");
                if (histArr != null) {
                    for (int i = 0; i < histArr.length(); i++) {
                        org.json.JSONObject h = histArr.getJSONObject(i);
                        double csMin = h.optDouble("CS_min", 0.0);
                        double csMax = h.optDouble("CS_max", 0.0);
                        double fCS = h.optDouble("f_CS", 0.0);
                        histogram.add(new HistogramBucket(csMin, csMax, fCS));
                    }
                }
            }

            // Si irradiation non alignée sur 12 entrées, l'ignorer pour éviter des incohérences
            List<Double> irrOut = irradiation.size() == monthly.size() && !irradiation.isEmpty() ? irradiation : List.of();
            return new PVGISResult(monthly, histogram, irrOut);
        } catch (RuntimeException ex) {
            return new PVGISResult(List.of(), List.of(), List.of());
        }
    }
}
