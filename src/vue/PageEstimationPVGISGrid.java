package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class PageEstimationPVGISGrid extends JPanel {
    // Champs du formulaire Grid-Connected
    private JTextField latField, lonField, useHorizonField, userHorizonField, radDatabaseField, peakPowerField, pvTechChoiceField, mountingPlaceField, lossField, fixedField, angleField, aspectField, optimalInclinationField, optimalAnglesField, inclinedAxisField, inclinedOptimumField, inclinedAxisAngleField, verticalAxisField, verticalOptimumField, verticalAxisAngleField, twoAxisField, pvPriceField, systemCostField, interestField, lifetimeField, outputFormatField, browserField;
    private JPanel graphPanel;
    private JLabel statusLabel; // Nouvelle box de statut
    private String lastJson = null;

    public PageEstimationPVGISGrid() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Formulaire pour PV couplé au réseau");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 2));

        // Champs obligatoires avec valeurs par défaut
        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6"); // kW
        lossField = new JTextField("14"); // %

        // Champs facultatifs avec valeurs par défaut PVGIS
        useHorizonField = new JTextField("1");
        userHorizonField = new JTextField("");
        radDatabaseField = new JTextField("PVGIS-SARAH3");
        pvTechChoiceField = new JTextField("crystSi");
        mountingPlaceField = new JTextField("free");
        fixedField = new JTextField("1");
        angleField = new JTextField("0");
        aspectField = new JTextField("0");
        optimalInclinationField = new JTextField("0");
        optimalAnglesField = new JTextField("0");
        inclinedAxisField = new JTextField("0");
        inclinedOptimumField = new JTextField("0");
        inclinedAxisAngleField = new JTextField("0");
        verticalAxisField = new JTextField("0");
        verticalOptimumField = new JTextField("0");
        verticalAxisAngleField = new JTextField("0");
        twoAxisField = new JTextField("0");
        pvPriceField = new JTextField("0");
        systemCostField = new JTextField("");
        interestField = new JTextField("");
        lifetimeField = new JTextField("25");
        outputFormatField = new JTextField("json");
        browserField = new JTextField("0");

        // Ajout des champs au formulaire
        inputPanel.add(new JLabel("Latitude :*")); inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude :*")); inputPanel.add(lonField);
        inputPanel.add(new JLabel("Base de données de radiation :")); inputPanel.add(radDatabaseField);
        inputPanel.add(new JLabel("Puissance PV crête (kW) :*")); inputPanel.add(peakPowerField);
        inputPanel.add(new JLabel("Technologie PV :")); inputPanel.add(pvTechChoiceField);
        inputPanel.add(new JLabel("Type de montage :")); inputPanel.add(mountingPlaceField);
        inputPanel.add(new JLabel("Pertes système (%) :*")); inputPanel.add(lossField);
        inputPanel.add(new JLabel("Fixe (1=oui, 0=non) :")); inputPanel.add(fixedField);
        inputPanel.add(new JLabel("Inclinaison (°) :")); inputPanel.add(angleField);
        inputPanel.add(new JLabel("Azimut (°) :")); inputPanel.add(aspectField);
        inputPanel.add(new JLabel("Inclinaison optimale (1=oui, 0=non) :")); inputPanel.add(optimalInclinationField);
        inputPanel.add(new JLabel("Angles optimaux (1=oui, 0=non) :")); inputPanel.add(optimalAnglesField);
        inputPanel.add(new JLabel("Axe incliné (1=oui, 0=non) :")); inputPanel.add(inclinedAxisField);
        inputPanel.add(new JLabel("Inclinaison optimale axe incliné (1=oui, 0=non) :")); inputPanel.add(inclinedOptimumField);
        inputPanel.add(new JLabel("Angle axe incliné (°) :")); inputPanel.add(inclinedAxisAngleField);
        inputPanel.add(new JLabel("Axe vertical (1=oui, 0=non) :")); inputPanel.add(verticalAxisField);
        inputPanel.add(new JLabel("Inclinaison optimale axe vertical (1=oui, 0=non) :")); inputPanel.add(verticalOptimumField);
        inputPanel.add(new JLabel("Angle axe vertical (°) :")); inputPanel.add(verticalAxisAngleField);
        inputPanel.add(new JLabel("Double axe (1=oui, 0=non) :")); inputPanel.add(twoAxisField);
        inputPanel.add(new JLabel("Prix PV (1=oui, 0=non) :")); inputPanel.add(pvPriceField);
        inputPanel.add(new JLabel("Coût système (si prix PV) :")); inputPanel.add(systemCostField);
        inputPanel.add(new JLabel("Intérêt (si prix PV) :")); inputPanel.add(interestField);
        inputPanel.add(new JLabel("Durée de vie (ans) :")); inputPanel.add(lifetimeField);
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
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        verticalPanel.add(new JScrollPane(inputPanel));
        verticalPanel.add(buttonPanel);
        // Ajout de la box de statut
        statusLabel = new JLabel("En attente d'une estimation.");
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new java.awt.Color(220, 220, 220)); // gris clair
        statusLabel.setForeground(java.awt.Color.BLACK);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(400, 30));
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        verticalPanel.add(statusPanel);
        // Suppression de la zone JSON
        // resultArea = new JTextArea(18, 60);
        // resultArea.setLineWrap(true);
        // resultArea.setWrapStyleWord(true);
        // JScrollPane scrollPane = new JScrollPane(resultArea);
        // verticalPanel.add(scrollPane);
        graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        JScrollPane graphScrollPane = new JScrollPane(graphPanel);
        graphScrollPane.setPreferredSize(new Dimension(900, 700));
        verticalPanel.add(graphScrollPane);
        add(verticalPanel, BorderLayout.CENTER);
    }

    private void estimerProduction() {
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?");
        url.append("lat=").append(latField.getText());
        url.append("&lon=").append(lonField.getText());
        url.append("&peakpower=").append(peakPowerField.getText());
        url.append("&loss=").append(lossField.getText());
        if (!radDatabaseField.getText().isEmpty()) url.append("&raddatabase=").append(radDatabaseField.getText());
        if (!pvTechChoiceField.getText().isEmpty()) url.append("&pvtechccadrehoice=").append(pvTechChoiceField.getText());
        if (!mountingPlaceField.getText().isEmpty()) url.append("&mountingplace=").append(mountingPlaceField.getText());
        if (!fixedField.getText().isEmpty()) url.append("&fixed=").append(fixedField.getText());
        if (!angleField.getText().isEmpty()) url.append("&angle=").append(angleField.getText());
        if (!aspectField.getText().isEmpty()) url.append("&aspect=").append(aspectField.getText());
        if (!optimalInclinationField.getText().isEmpty()) url.append("&optimalinclination=").append(optimalInclinationField.getText());
        if (!optimalAnglesField.getText().isEmpty()) url.append("&optimalangles=").append(optimalAnglesField.getText());
        if (!inclinedAxisField.getText().isEmpty()) url.append("&inclined_axis=").append(inclinedAxisField.getText());
        if (!inclinedOptimumField.getText().isEmpty()) url.append("&inclined_optimum=").append(inclinedOptimumField.getText());
        if (!inclinedAxisAngleField.getText().isEmpty()) url.append("&inclinedaxisangle=").append(inclinedAxisAngleField.getText());
        if (!verticalAxisField.getText().isEmpty()) url.append("&vertical_axis=").append(verticalAxisField.getText());
        if (!verticalOptimumField.getText().isEmpty()) url.append("&vertical_optimum=").append(verticalOptimumField.getText());
        if (!verticalAxisAngleField.getText().isEmpty()) url.append("&verticalaxisangle=").append(verticalAxisAngleField.getText());
        if (!twoAxisField.getText().isEmpty()) url.append("&twoaxis=").append(twoAxisField.getText());
        if (!pvPriceField.getText().isEmpty()) url.append("&pvprice=").append(pvPriceField.getText());
        if (!systemCostField.getText().isEmpty()) url.append("&systemcost=").append(systemCostField.getText());
        if (!interestField.getText().isEmpty()) url.append("&interest=").append(interestField.getText());
        if (!lifetimeField.getText().isEmpty()) url.append("&lifetime=").append(lifetimeField.getText());
        if (!useHorizonField.getText().isEmpty()) url.append("&usehorizon=").append(useHorizonField.getText());
        if (!userHorizonField.getText().isEmpty()) url.append("&userhorizon=").append(userHorizonField.getText());
        if (!outputFormatField.getText().isEmpty()) url.append("&outputformat=").append(outputFormatField.getText());
        if (!browserField.getText().isEmpty()) url.append("&browser=").append(browserField.getText());
        // Affichage attente
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("En attente de la réponse de l'API...");
            statusLabel.setBackground(new java.awt.Color(220, 220, 220)); // gris clair
            statusLabel.setForeground(java.awt.Color.BLACK);
        });
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url.toString())).GET().build();
                String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
                lastJson = responseBody;
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Succès : données reçues");
                    statusLabel.setBackground(new java.awt.Color(0, 180, 0)); // vert
                    statusLabel.setForeground(java.awt.Color.WHITE);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Erreur lors de la requête : " + ex.getMessage());
                    statusLabel.setBackground(new java.awt.Color(200, 0, 0)); // rouge
                    statusLabel.setForeground(java.awt.Color.WHITE);
                });
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
        try {
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            // --- Correction parsing PVGIS ---
            org.json.JSONObject monthly = outputs.getJSONObject("monthly");
            org.json.JSONArray monthlyFixed = monthly.has("fixed") ? monthly.getJSONArray("fixed") : null;
            if (monthlyFixed == null) {
                JLabel label = new JLabel("Aucune donnée mensuelle 'fixed' trouvée dans la réponse JSON.");
                graphPanel.add(label);
                graphPanel.revalidate();
                graphPanel.repaint();
                return;
            }
            // Diagramme 1 : Production PV moyenne (mensuelle et annuelle)
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            double totalProd = 0;
            for (int i = 0; i < monthlyFixed.length(); i++) {
                org.json.JSONObject m = monthlyFixed.getJSONObject(i);
                mois.add("Mois " + m.getInt("month"));
                double val = m.getDouble("E_m");
                prod.add(val);
                totalProd += val;
            }
            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production PV moyenne mensuelle (kWh/mois)").xAxisTitle("Mois").yAxisTitle("kWh").build();
            chart1.addSeries("Production mensuelle", mois, prod);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart1));
            // Production annuelle
            org.knowm.xchart.PieChart chartAnn = new org.knowm.xchart.PieChartBuilder().width(400).height(300).title("Production PV annuelle totale").build();
            chartAnn.addSeries("Année", totalProd);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chartAnn));
            // Diagramme 2 : Variabilité interannuelle (écart-type)
            org.json.JSONObject totals = outputs.getJSONObject("totals");
            org.json.JSONObject totalsFixed = totals.has("fixed") ? totals.getJSONObject("fixed") : null;
            if (totalsFixed != null && totalsFixed.has("SD_y")) {
                double std = totalsFixed.getDouble("SD_y");
                org.knowm.xchart.PieChart chartStd = new org.knowm.xchart.PieChartBuilder().width(400).height(300).title("Variabilité interannuelle (écart-type)").build();
                chartStd.addSeries("Ecart-type annuel", std);
                graphPanel.add(new org.knowm.xchart.XChartPanel<>(chartStd));
            } else {
                JLabel label = new JLabel("Variabilité interannuelle non disponible dans la réponse JSON.");
                graphPanel.add(label);
            }
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
