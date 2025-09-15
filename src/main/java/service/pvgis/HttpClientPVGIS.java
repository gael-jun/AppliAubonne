package service.pvgis;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Implémentation HTTP réelle pour l'API PVGIS avec timeouts et quelques réessais simples.
 */
public final class HttpClientPVGIS implements PVGISClient {
    private final HttpClient http;
    private final int maxRetries;
    private final Duration requestTimeout;

    public HttpClientPVGIS() {
        this(HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build(),
            2,
            Duration.ofSeconds(20));
    }

    public HttpClientPVGIS(HttpClient http, int maxRetries, Duration requestTimeout) {
        this.http = http;
        this.maxRetries = Math.max(0, maxRetries);
        this.requestTimeout = requestTimeout;
    }

    @Override
    public String get(String url) throws IOException, InterruptedException {
        IOException lastIo = null;
        InterruptedException lastInt = null;
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(requestTimeout)
                        .GET()
                        .build();
                HttpResponse<String> resp = http.send(request, HttpResponse.BodyHandlers.ofString());
                int code = resp.statusCode();
                if (code >= 200 && code < 300) {
                    return resp.body();
                }
                lastIo = new IOException("HTTP " + code + " for URL: " + url);
            } catch (IOException io) {
                lastIo = io;
            } catch (InterruptedException ie) {
                lastInt = ie;
                throw ie; // propagate interrupt immediately
            }
            // small backoff before retry
            try { Thread.sleep(Math.min(1000L * (attempt + 1), 3000L)); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); throw ie; }
        }
        if (lastIo != null) throw lastIo;
        if (lastInt != null) throw lastInt;
        throw new IOException("Unknown error calling PVGIS");
    }
}
