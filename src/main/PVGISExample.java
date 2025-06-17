import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PVGISExample {
    public static void main(String[] args) throws Exception {
        String url = "https://re.jrc.ec.europa.eu/api/v5_2/PVcalc?" +
                    "lat=48.989&lon=2.277&peakpower=6&loss=14" +
                    "&angle=20&aspect=0&mountingplace=free" +
                    "&pvtechchoice=crystSi&outputformat=json";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}

