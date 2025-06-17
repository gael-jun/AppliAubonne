package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PageEstimationPVGIS extends JPanel {
    private JTextField latField, lonField, peakPowerField, batterySizeField, cutoffField, consumptionDayField, angleField, aspectField;
    private JTextField elevationField, radiationDbField, meteoDbField, yearMinField, yearMaxField, useHorizonField, horizonDbField;
    private JTextArea resultArea;

    public PageEstimationPVGIS() {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6");
        batterySizeField = new JTextField("10");
        cutoffField = new JTextField("20");
        consumptionDayField = new JTextField("2000");
        angleField = new JTextField("10");
        aspectField = new JTextField("0");
        radiationDbField = new JTextField("PVGIS-SARAH3");

        inputPanel.add(new JLabel("Latitude:"));
        inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude:"));
        inputPanel.add(lonField);
        inputPanel.add(new JLabel("Puissance crête installée (kW):"));
        inputPanel.add(peakPowerField);
        inputPanel.add(new JLabel("Capacité batterie (kWh):"));
        inputPanel.add(batterySizeField);
        inputPanel.add(new JLabel("Limite de décharge (%):"));
        inputPanel.add(cutoffField);
        inputPanel.add(new JLabel("Consommation par jour (Wh):"));
        inputPanel.add(consumptionDayField);
        inputPanel.add(new JLabel("Inclinaison (°):"));
        inputPanel.add(angleField);
        inputPanel.add(new JLabel("Azimut (°):"));
        inputPanel.add(aspectField);

        inputPanel.add(new JLabel("Base de données de radiation:"));
        inputPanel.add(radiationDbField);

        JButton estimateButton = new JButton("Estimer la production");
        estimateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                estimerProduction();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(estimateButton, BorderLayout.SOUTH);

        resultArea = new JTextArea(15, 50);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void estimerProduction() {
        String lat = latField.getText();
        String lon = lonField.getText();
        String peakPower = peakPowerField.getText();
        String batterySize = batterySizeField.getText();
        String cutoff = cutoffField.getText();
        String consumptionDay = consumptionDayField.getText();
        String angle = angleField.getText();
        String aspect = aspectField.getText();
        String radiationDb = radiationDbField.getText();


        String url = "https://re.jrc.ec.europa.eu/api/v5_3/SHScalc?" +
                "lat=" + lat +
                "&lon=" + lon +
                "&raddatabase=" + radiationDb +
                "&peakpower=" + peakPower +
                "&batterysize=" + batterySize +
                "&cutoff=" + cutoff +
                "&consumptionday=" + consumptionDay +
                "&angle=" + angle +
                "&aspect=" + aspect +
                "&outputformat=json";

        resultArea.setText("Requête envoyée à :\n" + url + "\n\nEn attente de la réponse...");

        // Appel API dans un thread séparé pour ne pas bloquer l'UI
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                SwingUtilities.invokeLater(() -> resultArea.setText(response.body()));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> resultArea.setText("Erreur lors de la requête :\n" + ex.getMessage()));
            }
        }).start();
    }
}
