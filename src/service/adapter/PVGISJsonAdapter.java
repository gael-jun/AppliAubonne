package service.adapter;

import modele.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public final class PVGISJsonAdapter {
    public PVGISResult fromJson(String json) {
        JSONObject obj = new JSONObject(json);
        JSONObject outputs = obj.getJSONObject("outputs");
        JSONArray monthly = outputs.getJSONArray("monthly");
        JSONArray histogram = outputs.getJSONArray("histogram");

        List<MonthlyResult> monthlyList = new ArrayList<>();
        for (int i = 0; i < monthly.length(); i++) {
            JSONObject m = monthly.getJSONObject(i);
            monthlyList.add(new MonthlyResult(
                m.getInt("month"),
                m.getDouble("E_d"),
                m.getDouble("E_lost_d"),
                m.getDouble("f_f"),
                m.getDouble("f_e")
            ));
        }
        List<HistogramBucket> histList = new ArrayList<>();
        for (int i = 0; i < histogram.length(); i++) {
            JSONObject h = histogram.getJSONObject(i);
            histList.add(new HistogramBucket(
                h.getDouble("CS_min"),
                h.getDouble("CS_max"),
                h.getDouble("f_CS")
            ));
        }
        return new PVGISResult(monthlyList, histList);
    }
}
