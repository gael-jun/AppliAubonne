package vue;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

/**
 * Page d'estimation PVGIS pour systèmes photovoltaïques couplés au réseau (grid-connected).
 * Fournit un formulaire ergonomique, l'appel à l'API PVGIS et l'affichage graphique des résultats.
 */
public class PageEstimationPVGISGrid extends JPanel {
    /** Champ de saisie pour la latitude du site. */
    private JTextField latField;
    /** Champ de saisie pour la longitude du site. */
    private JTextField lonField;
    /** Champ de saisie pour l'horizon utilisateur (optionnel). */
    private JTextField userHorizonField;
    /** Champ de saisie pour la puissance crête du système PV (kW). */
    private final JTextField peakPowerField;
    /** Champ de saisie pour les pertes système (%). */
    private JTextField lossField;
    /** Champ de saisie pour l'inclinaison du module PV (degrés). */
    private JTextField angleField;
    /** Champ de saisie pour l'azimut du module PV (degrés). */
    private JTextField aspectField;
    /** Champ de saisie pour l'angle axe incliné (degrés). */
    private final JTextField inclinedAxisAngleField;
    /** Champ de saisie pour l'angle axe vertical (degrés). */
    private final JTextField verticalAxisAngleField;
    /** Champ de saisie pour le prix PV (optionnel). */
    private JTextField pvPriceField;
    /** Champ de saisie pour le coût système (optionnel). */
    private final JTextField systemCostField;
    /** Champ de saisie pour l'intérêt (optionnel). */
    private JTextField interestField;
    /** Champ de saisie pour la durée de vie (ans). */
    private JTextField lifetimeField;
    /** Champ de saisie pour le format de sortie (json, csv, ...). */
    private final JTextField outputFormatField;
    /** Liste déroulante pour la base de données de radiation. */
    private final JComboBox<String> radDatabaseCombo;
    /** Liste déroulante pour la technologie PV. */
    private JComboBox<String> pvTechChoiceCombo;
    /** Liste déroulante pour le type de montage. */
    private JComboBox<String> mountingPlaceCombo;
    /** Case à cocher pour inclure l'horizon naturel. */
    private final JCheckBox useHorizonCheck;
    /** Case à cocher pour montage fixe. */
    private final JCheckBox fixedCheck;
    /** Case à cocher pour inclinaison optimale. */
    private JCheckBox optimalInclinationCheck;
    /** Case à cocher pour angles optimaux. */
    private JCheckBox optimalAnglesCheck;
    /** Case à cocher pour axe incliné. */
    private JCheckBox inclinedAxisCheck;
    /** Case à cocher pour inclinaison optimale axe incliné. */
    private final JCheckBox inclinedOptimumCheck;
    /** Case à cocher pour axe vertical. */
    private JCheckBox verticalAxisCheck;
    /** Case à cocher pour inclinaison optimale axe vertical. */
    private final JCheckBox verticalOptimumCheck;
    /** Case à cocher pour double axe. */
    private JCheckBox twoAxisCheck;
    /** Case à cocher pour le mode browser (affichage interactif). */
    private final JCheckBox browserCheck;
    /** Panel d'affichage des graphes. */
    private JPanel graphPanel;
    /** Label de statut pour l'utilisateur (attente, succès, erreur). */
    private final JLabel statusLabel;
    /** Dernière réponse JSON reçue de l'API PVGIS. */
    private String lastJson = null;

    /**
     * Construit la page d'estimation PVGIS Grid-Connected avec formulaire, boutons et zone de graphes.
     */
    public PageEstimationPVGISGrid() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Formulaire pour PV couplé au réseau");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        //Champs obligatoires avec valeurs par défaut

        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6"); // kW
        lossField = new JTextField("14"); // %

        // Champs facultatifs ergonomiques
        radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"});
        radDatabaseCombo.setSelectedIndex(0);
        pvTechChoiceCombo = new JComboBox<>(new String[]{"crystSi", "CIS", "CdTe", "amorphous"});
        pvTechChoiceCombo.setSelectedIndex(0);
        mountingPlaceCombo = new JComboBox<>(new String[]{"free", "building"});
        mountingPlaceCombo.setSelectedIndex(0);
        fixedCheck = new JCheckBox(); fixedCheck.setSelected(true);
        angleField = new JTextField("0");
        aspectField = new JTextField("0");
        optimalInclinationCheck = new JCheckBox();
        optimalAnglesCheck = new JCheckBox();
        inclinedAxisCheck = new JCheckBox();
        inclinedOptimumCheck = new JCheckBox();
        inclinedAxisAngleField = new JTextField("0");
        verticalAxisCheck = new JCheckBox();
        verticalOptimumCheck = new JCheckBox();
        verticalAxisAngleField = new JTextField("0");
        twoAxisCheck = new JCheckBox();
        pvPriceField = new JTextField("0");
        systemCostField = new JTextField("");
        interestField = new JTextField("");
        lifetimeField = new JTextField("25");
        useHorizonCheck = new JCheckBox(); useHorizonCheck.setSelected(true);
        userHorizonField = new JTextField("");
        outputFormatField = new JTextField("json");
        browserCheck = new JCheckBox();

        // Ajout des champs au formulaire ergonomique
        inputPanel.add(new JLabel("Latitude :*")); inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude :*")); inputPanel.add(lonField);
        inputPanel.add(new JLabel("Base de données de radiation :")); inputPanel.add(radDatabaseCombo);
        inputPanel.add(new JLabel("Puissance PV crête (kW) :*")); inputPanel.add(peakPowerField);
        inputPanel.add(new JLabel("Technologie PV :")); inputPanel.add(pvTechChoiceCombo);
        inputPanel.add(new JLabel("Type de montage :")); inputPanel.add(mountingPlaceCombo);
        inputPanel.add(new JLabel("Pertes système (%) :*")); inputPanel.add(lossField);
        inputPanel.add(new JLabel("Fixe :")); inputPanel.add(fixedCheck);
        inputPanel.add(new JLabel("Inclinaison (°) :")); inputPanel.add(angleField);
        inputPanel.add(new JLabel("Azimut (°) :")); inputPanel.add(aspectField);
        inputPanel.add(new JLabel("Inclinaison optimale :")); inputPanel.add(optimalInclinationCheck);
        inputPanel.add(new JLabel("Angles optimaux :")); inputPanel.add(optimalAnglesCheck);
        inputPanel.add(new JLabel("Axe incliné :")); inputPanel.add(inclinedAxisCheck);
        inputPanel.add(new JLabel("Inclinaison optimale axe incliné :")); inputPanel.add(inclinedOptimumCheck);
        inputPanel.add(new JLabel("Angle axe incliné (°) :")); inputPanel.add(inclinedAxisAngleField);
        inputPanel.add(new JLabel("Axe vertical :")); inputPanel.add(verticalAxisCheck);
        inputPanel.add(new JLabel("Inclinaison optimale axe vertical :")); inputPanel.add(verticalOptimumCheck);
        inputPanel.add(new JLabel("Angle axe vertical (°) :")); inputPanel.add(verticalAxisAngleField);
        inputPanel.add(new JLabel("Double axe :")); inputPanel.add(twoAxisCheck);
        inputPanel.add(new JLabel("Prix PV :")); inputPanel.add(pvPriceField);
        inputPanel.add(new JLabel("Coût système (si prix PV) :")); inputPanel.add(systemCostField);
        inputPanel.add(new JLabel("Intérêt (si prix PV) :")); inputPanel.add(interestField);
        inputPanel.add(new JLabel("Durée de vie (ans) :")); inputPanel.add(lifetimeField);
        inputPanel.add(new JLabel("Inclure l'horizon :")); inputPanel.add(useHorizonCheck);
        inputPanel.add(new JLabel("Horizon utilisateur (8 valeurs, séparées par des virgules) :")); inputPanel.add(userHorizonField);
        inputPanel.add(new JLabel("Format de sortie :")); inputPanel.add(outputFormatField);
        inputPanel.add(new JLabel("Browser :")); inputPanel.add(browserCheck);

        JButton estimateButton = new JButton("Estimer la production");
        estimateButton.addActionListener((ActionEvent e) -> estimerProduction());
        JButton graphButton = new JButton("Voir graphes");
        graphButton.addActionListener(e -> afficherGraphes());
        JButton exportPdfButton = new JButton("Exporter en PDF");
        exportPdfButton.addActionListener(e -> exporterResultatsEnPDF());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(estimateButton);
        buttonPanel.add(graphButton);
        buttonPanel.add(exportPdfButton);
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

    /**
     * Effectue l'appel à l'API PVGIS avec les paramètres du formulaire et met à jour le statut.
     */
    private void estimerProduction() {
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?");
        url.append("lat=").append(latField.getText());
        url.append("&lon=").append(lonField.getText());
        url.append("&peakpower=").append(peakPowerField.getText());
        url.append("&loss=").append(lossField.getText());
        if (radDatabaseCombo.getSelectedItem() != null) url.append("&raddatabase=").append(radDatabaseCombo.getSelectedItem());
        if (pvTechChoiceCombo.getSelectedItem() != null) url.append("&pvtechchoice=").append(pvTechChoiceCombo.getSelectedItem());
        if (mountingPlaceCombo.getSelectedItem() != null) url.append("&mountingplace=").append(mountingPlaceCombo.getSelectedItem());
        url.append("&fixed=").append(fixedCheck.isSelected() ? "1" : "0");
        if (!angleField.getText().isEmpty()) url.append("&angle=").append(angleField.getText());
        if (!aspectField.getText().isEmpty()) url.append("&aspect=").append(aspectField.getText());
        url.append("&optimalinclination=").append(optimalInclinationCheck.isSelected() ? "1" : "0");
        url.append("&optimalangles=").append(optimalAnglesCheck.isSelected() ? "1" : "0");
        url.append("&inclined_axis=").append(inclinedAxisCheck.isSelected() ? "1" : "0");
        url.append("&inclined_optimum=").append(inclinedOptimumCheck.isSelected() ? "1" : "0");
        if (!inclinedAxisAngleField.getText().isEmpty()) url.append("&inclinedaxisangle=").append(inclinedAxisAngleField.getText());
        url.append("&vertical_axis=").append(verticalAxisCheck.isSelected() ? "1" : "0");
        url.append("&vertical_optimum=").append(verticalOptimumCheck.isSelected() ? "1" : "0");
        if (!verticalAxisAngleField.getText().isEmpty()) url.append("&verticalaxisangle=").append(verticalAxisAngleField.getText());
        url.append("&twoaxis=").append(twoAxisCheck.isSelected() ? "1" : "0");
        if (!pvPriceField.getText().isEmpty()) url.append("&pvprice=").append(pvPriceField.getText());
        if (!systemCostField.getText().isEmpty()) url.append("&systemcost=").append(systemCostField.getText());
        if (!interestField.getText().isEmpty()) url.append("&interest=").append(interestField.getText());
        if (!lifetimeField.getText().isEmpty()) url.append("&lifetime=").append(lifetimeField.getText());
        url.append("&usehorizon=").append(useHorizonCheck.isSelected() ? "1" : "0");
        if (!userHorizonField.getText().isEmpty()) url.append("&userhorizon=").append(userHorizonField.getText());
        if (!outputFormatField.getText().isEmpty()) url.append("&outputformat=").append(outputFormatField.getText());
        url.append("&browser=").append(browserCheck.isSelected() ? "1" : "0");
        url.append("&global=1"); // Ajout pour irradiation sur plan incliné
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

    /**
     * Affiche les graphes de résultats à partir du JSON retourné par l'API PVGIS.
     */
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
            // Liste des mois en français
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> irradiation = new java.util.ArrayList<>();
            boolean irradiationOk = true;
            for (int i = 0; i < monthlyFixed.length(); i++) {
                org.json.JSONObject m = monthlyFixed.getJSONObject(i);
                int idxMois = m.getInt("month") - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                mois.add(nomMois);
                double val = m.getDouble("E_m");
                prod.add(val);
                if (m.has("H(i)_m")) {
                    irradiation.add(m.getDouble("H(i)_m"));
                } else {
                    irradiationOk = false;
                    irradiation.add(0.0);
                }
            }
            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production PV moyenne mensuelle (kWh/mois)").xAxisTitle("Mois").yAxisTitle("kWh").build();
            chart1.addSeries("Production mensuelle", mois, prod);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart1));
            // Diagramme 2 : Irradiation mensuelle sur plan fixe (kWh/m2/mois)
            if (irradiationOk) {
                org.knowm.xchart.CategoryChart chartIrr = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Irradiation mensuelle sur plan fixe (kWh/m²/mois)").xAxisTitle("Mois").yAxisTitle("kWh/m²").build();
                chartIrr.addSeries("Irradiation sur plan fixe", mois, irradiation);
                graphPanel.add(new org.knowm.xchart.XChartPanel<>(chartIrr));
            } else {
                JLabel label = new JLabel("Champ 'H(i)_m' (irradiation sur plan incliné) absent dans la réponse JSON PVGIS.");
                graphPanel.add(label);
            }
            // Suppression du diagramme de variabilité interannuelle (écart-type)
            graphPanel.revalidate();
            graphPanel.repaint();
        } catch (Exception ex) {
            JLabel label = new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage());
            graphPanel.add(label);
            graphPanel.revalidate();
            graphPanel.repaint();
        }
    }

    /**
     * Exporte les résultats en tant que tableau et graphes dans un fichier PDF.
     */
    private void exporterResultatsEnPDF() {
        if (lastJson == null || lastJson.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le PDF");
            if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
            java.io.File pdfFile = fileChooser.getSelectedFile();
            if (!pdfFile.getName().toLowerCase().endsWith(".pdf")) {
                pdfFile = new java.io.File(pdfFile.getAbsolutePath() + ".pdf");
            }

            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            org.json.JSONObject monthly = outputs.getJSONObject("monthly");
            org.json.JSONArray monthlyFixed = monthly.has("fixed") ? monthly.getJSONArray("fixed") : null;
            if (monthlyFixed == null) {
                JOptionPane.showMessageDialog(this, "Aucune donnée mensuelle 'fixed' trouvée dans la réponse JSON.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.List<java.awt.image.BufferedImage> chartImages = new java.util.ArrayList<>();
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> irradiation = new java.util.ArrayList<>();
            java.util.List<Double> deviation = new java.util.ArrayList<>();
            for (int i = 0; i < monthlyFixed.length(); i++) {
                org.json.JSONObject m = monthlyFixed.getJSONObject(i);
                int idxMois = m.getInt("month") - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                mois.add(nomMois);
                prod.add(m.getDouble("E_m"));
                irradiation.add(m.getDouble("H(i)_m"));
                deviation.add(m.getDouble("E_m") - m.getDouble("H(i)_m"));
            }

            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production PV moyenne mensuelle (kWh/mois)").xAxisTitle("Mois").yAxisTitle("kWh").build();
            chart1.addSeries("Production mensuelle", mois, prod);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart1));

            org.knowm.xchart.CategoryChart chart2 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Irradiation mensuelle (kWh/m²/mois)").xAxisTitle("Mois").yAxisTitle("kWh/m²").build();
            chart2.addSeries("Irradiation", mois, irradiation);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart2));



            org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
            doc.addPage(page);
            org.apache.pdfbox.pdmodel.PDPageContentStream content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
            float y = 750;
            try {
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 18);
                content.beginText();
                content.newLineAtOffset(50, y);
                content.showText("Estimation PVGIS Grid-Connected");
                content.endText();
                y -= 30;

                content.beginText();
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(50, y);
                content.showText("Paramètres d'entrée :");
                content.endText();
                y -= 18;
                String[][] entrees = {
                    {"Latitude", latField.getText()},
                    {"Longitude", lonField.getText()},
                    {"Base de données de radiation", radDatabaseCombo.getSelectedItem().toString()},
                    {"Inclure horizon", useHorizonCheck.isSelected() ? "Oui" : "Non"},
                    {"Inclinaison (°)", angleField.getText()},
                    {"Azimut (°)", aspectField.getText()},
                    {"Type de montage", mountingPlaceCombo.getSelectedItem().toString()},
                    {"Technologie PV", pvTechChoiceCombo.getSelectedItem().toString()},
                    {"Puissance crête (kW)", peakPowerField.getText()},
                    {"Pertes système (%)", lossField.getText()}
                };
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                for (String[] ligne : entrees) {
                    content.beginText();
                    content.newLineAtOffset(55, y);
                    content.showText(ligne[0] + " : " + ligne[1]);
                    content.endText();
                    y -= 13;
                }
                y -= 10;

                // Ajout du tableau des valeurs mensuelles
                float tableStartY = y;
                float tableStartX = 50;
                float rowHeight = 18;
                float tableWidth = 400;
                float[] colWidths = {70, 110, 110, 110};
                String[] headers = {"Mois", "Prod (kWh)", "Irradiation (kWh/m²)", "Déviation (kWh)"};
                // En-tête en gras
                content.setStrokingColor(java.awt.Color.BLACK);
                content.setNonStrokingColor(java.awt.Color.LIGHT_GRAY);
                content.addRect(tableStartX, y - rowHeight, tableWidth, rowHeight);
                content.fill();
                content.setNonStrokingColor(java.awt.Color.BLACK);
                float nextX = tableStartX;
                for (int i = 0; i < headers.length; i++) {
                    content.beginText();
                    content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 10);
                    content.newLineAtOffset(nextX + 2, y - rowHeight + 4);
                    content.showText(headers[i]);
                    content.endText();
                    nextX += colWidths[i];
                }
                // Lignes du tableau
                y -= rowHeight;
                for (int i = 0; i < mois.size(); i++) {
                    nextX = tableStartX;
                    String[] vals = {
                        mois.get(i),
                        String.format("%.0f", prod.get(i)),
                        String.format("%.0f", irradiation.get(i)),
                        String.format("%.1f", deviation.get(i))
                    };
                    for (int j = 0; j < headers.length; j++) {
                        content.setStrokingColor(java.awt.Color.BLACK);
                        content.addRect(nextX, y - rowHeight, colWidths[j], rowHeight);
                        content.stroke();
                        content.beginText();
                        content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                        content.newLineAtOffset(nextX + 2, y - rowHeight + 4);
                        content.showText(vals[j]);
                        content.endText();
                        nextX += colWidths[j];
                    }
                    y -= rowHeight;
                }
                // Saut de page si besoin
                if (y < 200) {
                    content.close();
                    page = new org.apache.pdfbox.pdmodel.PDPage();
                    doc.addPage(page);
                    content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                    y = 750;
                }

                for (java.awt.image.BufferedImage img : chartImages) {
                    org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImage = org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory.createFromImage(doc, img);
                    if (y < 350) {
                        content.close();
                        page = new org.apache.pdfbox.pdmodel.PDPage();
                        doc.addPage(page);
                        content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                        y = 750;
                    }
                    content.drawImage(pdImage, 50, y - 250, 500, 250);
                    y -= 270;
                }
                content.close();
                doc.save(pdfFile);
                doc.close();
                JOptionPane.showMessageDialog(this, "PDF exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                try { content.close(); } catch (Exception ignore) {}
                try { doc.close(); } catch (Exception ignore) {}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
