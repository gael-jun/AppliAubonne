package service;

import modele.PVGISRequest;
import modele.PVGISResult;
import service.adapter.PVGISJsonAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PVGISService {
    private final HttpClient http = HttpClient.newHttpClient();
    private final PVGISJsonAdapter adapter = new PVGISJsonAdapter();

    public PVGISResult fetch(PVGISRequest req) throws IOException, InterruptedException {
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/SHScalc?");
        url.append("lat=").append(req.lat)
           .append("&lon=").append(req.lon)
           .append("&peakpower=").append(req.peakPower)
           .append("&batterysize=").append(req.batterySize)
           .append("&cutoff=").append(req.cutoff)
           .append("&consumptionday=").append(req.consumptionDay);
        if (req.angle != null && !req.angle.isEmpty()) url.append("&angle=").append(req.angle);
        if (req.aspect != null && !req.aspect.isEmpty()) url.append("&aspect=").append(req.aspect);
        if (req.radDatabase != null) url.append("&raddatabase=").append(req.radDatabase);
        url.append("&usehorizon=").append(req.useHorizon ? "1" : "0");
        if (req.userHorizon != null && !req.userHorizon.isEmpty()) url.append("&userhorizon=").append(req.userHorizon);
        if (req.hourConsumption != null && !req.hourConsumption.isEmpty()) url.append("&hourconsumption=").append(req.hourConsumption);
        if (req.outputFormat != null && !req.outputFormat.isEmpty()) url.append("&outputformat=").append(req.outputFormat);
        url.append("&browser=").append(req.browser ? "1" : "0");

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url.toString())).GET().build();
        String responseBody = http.send(request, HttpResponse.BodyHandlers.ofString()).body();
        return adapter.fromJson(responseBody);
    }
}
