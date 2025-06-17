import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PVGISHorsReseau {
    public static void main(String[] args) {
        try {
            // Test de l'API PVGIS Off-grid (SHS) avec les paramètres fournis
            String url = "https://re.jrc.ec.europa.eu/api/v5_2/SHScalc?" +
                "lat=48.989" +
                "&lon=2.277" +
                "&peakpower=6" + // Puissance crête installée en kW
                "&batterysize=10" + // Capacité batterie en kWh
                "&cutoff=20" + // Limite de décharge en %
                "&consumptionday=2000" + // Consommation par jour en Wh (clé correcte)
                "&angle=10" + // Inclinaison en degrés
                "&aspect=0" + // Azimut en degrés
                "&outputformat=json";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Résultat JSON de PVGIS Off-grid (SHS):");
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

