package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PageEstimationPVGISOffGrid extends JPanel {
    private JTextField latField, lonField, useHorizonField, userHorizonField, radDatabaseField, peakPowerField, angleField, aspectField, batterySizeField, cutoffField, consumptionDayField, hourConsumptionField, outputFormatField, browserField;
    private JTextArea resultArea;
    private JPanel graphPanel;
    private String lastJson = null;

    public PageEstimationPVGISOffGrid() {
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Champs obligatoires avec valeurs par défaut
        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6000"); // W
        batterySizeField = new JTextField("10000"); // Wh
        cutoffField = new JTextField("20");
        consumptionDayField = new JTextField("2000");

        // Champs facultatifs avec valeurs par défaut PVGIS
        useHorizonField = new JTextField("1");
        userHorizonField = new JTextField("");
        radDatabaseField = new JTextField("PVGIS-SARAH3");
        angleField = new JTextField("0");
        aspectField = new JTextField("0");
        hourConsumptionField = new JTextField("");
        outputFormatField = new JTextField("json");
        browserField = new JTextField("0");

        inputPanel.add(new JLabel("Latitude :*")); inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude :*")); inputPanel.add(lonField);
        inputPanel.add(new JLabel("Base de données de radiation :")); inputPanel.add(radDatabaseField);
        inputPanel.add(new JLabel("Puissance PV crête (W) :*")); inputPanel.add(peakPowerField);
        inputPanel.add(new JLabel("Inclinaison (°) :")); inputPanel.add(angleField);
        inputPanel.add(new JLabel("Azimut (°) :")); inputPanel.add(aspectField);
        inputPanel.add(new JLabel("Capacité batterie (Wh) :*")); inputPanel.add(batterySizeField);
        inputPanel.add(new JLabel("Limite de décharge (%) :*")); inputPanel.add(cutoffField);
        inputPanel.add(new JLabel("Consommation par jour (Wh) :*")); inputPanel.add(consumptionDayField);
        inputPanel.add(new JLabel("Profil horaire de consommation (24 valeurs, séparées par des virgules) :")); inputPanel.add(hourConsumptionField);
        inputPanel.add(new JLabel("Inclure l'horizon (1=oui, 0=non) :")); inputPanel.add(useHorizonField);
        inputPanel.add(new JLabel("Horizon utilisateur (8 valeurs, séparées par des virgules) :")); inputPanel.add(userHorizonField);
        inputPanel.add(new JLabel("Format de sortie :")); inputPanel.add(outputFormatField);
        inputPanel.add(new JLabel("Browser (1=oui, 0=non) :")); inputPanel.add(browserField);

        JButton estimateButton = new JButton("Estimer la production");
        estimateButton.addActionListener((ActionEvent e) -> estimerProduction());
        JButton graphButton = new JButton("Voir graphes");
        graphButton.addActionListener(e -> afficherGraphes());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(estimateButton);
        buttonPanel.add(graphButton);

        // Panel vertical pour tout empiler
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.add(new JScrollPane(inputPanel));
        verticalPanel.add(buttonPanel);

        resultArea = new JTextArea(18, 60);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        verticalPanel.add(scrollPane);

        graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        JScrollPane graphScrollPane = new JScrollPane(graphPanel);
        graphScrollPane.setPreferredSize(new Dimension(900, 700));
        verticalPanel.add(graphScrollPane);

        add(verticalPanel, BorderLayout.CENTER);
    }

    private void estimerProduction() {
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/SHScalc?");
        url.append("lat=").append(latField.getText());
        url.append("&lon=").append(lonField.getText());
        url.append("&peakpower=").append(peakPowerField.getText());
        url.append("&batterysize=").append(batterySizeField.getText());
        url.append("&cutoff=").append(cutoffField.getText());
        url.append("&consumptionday=").append(consumptionDayField.getText());
        if (!angleField.getText().isEmpty()) url.append("&angle=").append(angleField.getText());
        if (!aspectField.getText().isEmpty()) url.append("&aspect=").append(aspectField.getText());
        if (!radDatabaseField.getText().isEmpty()) url.append("&raddatabase=").append(radDatabaseField.getText());
        if (!useHorizonField.getText().isEmpty()) url.append("&usehorizon=").append(useHorizonField.getText());
        if (!userHorizonField.getText().isEmpty()) url.append("&userhorizon=").append(userHorizonField.getText());
        if (!hourConsumptionField.getText().isEmpty()) url.append("&hourconsumption=").append(hourConsumptionField.getText());
        if (!outputFormatField.getText().isEmpty()) url.append("&outputformat=").append(outputFormatField.getText());
        if (!browserField.getText().isEmpty()) url.append("&browser=").append(browserField.getText());

        resultArea.setText("Requête envoyée à :\n" + url + "\n\nEn attente de la réponse...");

        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url.toString())).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                lastJson = response.body();
                SwingUtilities.invokeLater(() -> resultArea.setText(lastJson));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> resultArea.setText("Erreur lors de la requête :\n" + ex.getMessage()));
            }
        }).start();
    }

    private void afficherGraphes() {
        graphPanel.removeAll();
        if (lastJson == null || lastJson.isEmpty()) {
            JLabel label = new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production.");
            graphPanel.add(label);
            graphPanel.revalidate();
            graphPanel.repaint();
            return;
        }
        // Utilisation de la bibliothèque XChart pour le tracé (si disponible)
        try {
            // --- PARSING JSON ---
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            // Graphique 1 : Production mensuelle
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            for (org.json.JSONObject m : outputs.getJSONArray("monthly").toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList()) {
                mois.add("Mois " + m.getInt("month"));
                prod.add(m.getDouble("E_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart1.addSeries("Production", mois, prod);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart1));
            // Graphique 2 : Énergie perdue mensuelle
            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (org.json.JSONObject m : outputs.getJSONArray("monthly").toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList()) {
                lost.add(m.getDouble("E_lost_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart2 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Énergie perdue mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart2.addSeries("Energie perdue", mois, lost);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart2));
            // Graphique 3 : Histogramme des états de charge
            java.util.List<String> csLabels = new java.util.ArrayList<>();
            java.util.List<Double> fcs = new java.util.ArrayList<>();
            for (org.json.JSONObject h : outputs.getJSONArray("histogram").toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList()) {
                csLabels.add(h.getDouble("CS_min") + "-" + h.getDouble("CS_max"));
                fcs.add(h.getDouble("f_CS"));
            }
            org.knowm.xchart.CategoryChart chart3 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Histogramme états de charge").xAxisTitle("% charge").yAxisTitle("% jours").build();
            chart3.addSeries("f_CS", csLabels, fcs);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart3));
            // Graphique 4 : % jours batterie pleine vs vide
            java.util.List<Double> ff = new java.util.ArrayList<>();
            java.util.List<Double> fe = new java.util.ArrayList<>();
            for (org.json.JSONObject m : outputs.getJSONArray("monthly").toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList()) {
                ff.add(m.getDouble("f_f"));
                fe.add(m.getDouble("f_e"));
            }
            org.knowm.xchart.CategoryChart chart4 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("% jours batterie pleine vs vide").xAxisTitle("Mois").yAxisTitle("% jours").build();
            chart4.addSeries("Batterie pleine", mois, ff);
            chart4.addSeries("Batterie vide", mois, fe);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart4));
            graphPanel.revalidate();
            graphPanel.repaint();
        } catch (Exception ex) {
            JLabel label = new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage());
            graphPanel.add(label);
            graphPanel.revalidate();
            graphPanel.repaint();
        }
    }
}
