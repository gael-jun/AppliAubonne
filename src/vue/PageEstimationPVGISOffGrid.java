package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Page d'estimation PVGIS pour systèmes photovoltaïques hors réseau (off-grid).
 * Fournit un formulaire ergonomique, l'appel à l'API PVGIS et l'affichage graphique des résultats.
 */
public class PageEstimationPVGISOffGrid extends JPanel {
    /** Champ de saisie pour la latitude du site. */
    private JTextField latField;
    /** Champ de saisie pour la longitude du site. */
    private JTextField lonField;
    /** Champ de saisie pour l'horizon utilisateur (optionnel). */
    private JTextField userHorizonField;
    /** Champ de saisie pour la puissance crête du système PV (W). */
    private JTextField peakPowerField;
    /** Champ de saisie pour l'inclinaison du module PV (degrés). */
    private JTextField angleField;
    /** Champ de saisie pour l'azimut du module PV (degrés). */
    private JTextField aspectField;
    /** Champ de saisie pour la capacité batterie (Wh). */
    private JTextField batterySizeField;
    /** Champ de saisie pour la limite de décharge batterie (%). */
    private JTextField cutoffField;
    /** Champ de saisie pour la consommation journalière (Wh). */
    private JTextField consumptionDayField;
    /** Champ de saisie pour le profil horaire de consommation (optionnel). */
    private JTextField hourConsumptionField;
    /** Champ de saisie pour le format de sortie (json, csv, ...). */
    private JTextField outputFormatField;
    /** Liste déroulante pour la base de données de radiation. */
    private JComboBox<String> radDatabaseCombo;
    /** Case à cocher pour inclure l'horizon naturel. */
    private JCheckBox useHorizonCheck;
    /** Case à cocher pour le mode browser (affichage interactif). */
    private JCheckBox browserCheck;
    /** Panel d'affichage des graphes. */
    private JPanel graphPanel;
    /** Label de statut pour l'utilisateur (attente, succès, erreur). */
    private JLabel statusLabel;
    /** Dernière réponse JSON reçue de l'API PVGIS. */
    private String lastJson = null;

    /**
     * Construit la page d'estimation PVGIS Off-Grid avec formulaire, boutons et zone de graphes.
     */
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
        useHorizonCheck = new JCheckBox();
        useHorizonCheck.setSelected(true);
        userHorizonField = new JTextField("");
        radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"});
        radDatabaseCombo.setSelectedIndex(0);
        angleField = new JTextField("0");
        aspectField = new JTextField("0");
        hourConsumptionField = new JTextField("");
        outputFormatField = new JTextField("json");
        browserCheck = new JCheckBox();
        browserCheck.setSelected(false);

        inputPanel.add(new JLabel("Latitude :*")); inputPanel.add(latField);
        inputPanel.add(new JLabel("Longitude :*")); inputPanel.add(lonField);
        inputPanel.add(new JLabel("Base de données de radiation :")); inputPanel.add(radDatabaseCombo);
        inputPanel.add(new JLabel("Puissance PV crête (W) :*")); inputPanel.add(peakPowerField);
        inputPanel.add(new JLabel("Inclinaison (°) :")); inputPanel.add(angleField);
        inputPanel.add(new JLabel("Azimut (°) :")); inputPanel.add(aspectField);
        inputPanel.add(new JLabel("Capacité batterie (Wh) :*")); inputPanel.add(batterySizeField);
        inputPanel.add(new JLabel("Limite de décharge (%) :*")); inputPanel.add(cutoffField);
        inputPanel.add(new JLabel("Consommation par jour (Wh) :*")); inputPanel.add(consumptionDayField);
        inputPanel.add(new JLabel("Profil horaire de consommation (24 valeurs, séparées par des virgules) :")); inputPanel.add(hourConsumptionField);
        inputPanel.add(new JLabel("Inclure l'horizon :")); inputPanel.add(useHorizonCheck);
        inputPanel.add(new JLabel("Horizon utilisateur (8 valeurs, séparées par des virgules) :")); inputPanel.add(userHorizonField);
        inputPanel.add(new JLabel("Format de sortie :")); inputPanel.add(outputFormatField);
        inputPanel.add(new JLabel("Browser :")); inputPanel.add(browserCheck);

        JButton estimateButton = new JButton("Estimer la production");
        estimateButton.addActionListener((ActionEvent e) -> estimerProduction());
        JButton graphButton = new JButton("Voir graphes");
        graphButton.addActionListener(e -> afficherGraphes());
        JButton exportPdfButton = new JButton("Exporter résultats en PDF");
        exportPdfButton.addActionListener(e -> exporterResultatsEnPDF());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(estimateButton);
        buttonPanel.add(graphButton);
        buttonPanel.add(exportPdfButton);

        // Panel vertical pour tout empiler
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
        StringBuilder url = new StringBuilder("https://re.jrc.ec.europa.eu/api/v5_3/SHScalc?");
        url.append("lat=").append(latField.getText());
        url.append("&lon=").append(lonField.getText());
        url.append("&peakpower=").append(peakPowerField.getText());
        url.append("&batterysize=").append(batterySizeField.getText());
        url.append("&cutoff=").append(cutoffField.getText());
        url.append("&consumptionday=").append(consumptionDayField.getText());
        if (!angleField.getText().isEmpty()) url.append("&angle=").append(angleField.getText());
        if (!aspectField.getText().isEmpty()) url.append("&aspect=").append(aspectField.getText());
        if (radDatabaseCombo.getSelectedItem() != null) url.append("&raddatabase=").append(radDatabaseCombo.getSelectedItem());
        url.append("&usehorizon=").append(useHorizonCheck.isSelected() ? "1" : "0");
        if (!userHorizonField.getText().isEmpty()) url.append("&userhorizon=").append(userHorizonField.getText());
        if (!hourConsumptionField.getText().isEmpty()) url.append("&hourconsumption=").append(hourConsumptionField.getText());
        if (!outputFormatField.getText().isEmpty()) url.append("&outputformat=").append(outputFormatField.getText());
        url.append("&browser=").append(browserCheck.isSelected() ? "1" : "0");
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
        // Utilisation de la bibliothèque XChart pour le tracé (si disponible)
        try {
            // --- PARSING JSON ---
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            // Graphique 1 : Production mensuelle
            // Liste des mois en français
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            java.util.List<org.json.JSONObject> monthlyList = outputs.getJSONArray("monthly").toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList();
            for (org.json.JSONObject m : monthlyList) {
                int idxMois = m.getInt("month") - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                mois.add(nomMois);
                prod.add(m.getDouble("E_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart1.addSeries("Production", mois, prod);
            graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart1));
            // Graphique 2 : Énergie perdue mensuelle
            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
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
            for (org.json.JSONObject m : monthlyList) {
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

    /**
     * Exporte les résultats (liste des entrées, tableau structuré, graphes) dans un PDF.
     */
    private void exporterResultatsEnPDF() {
        if (lastJson == null || lastJson.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // 1. Préparer le fichier PDF
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le PDF");
            if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
            java.io.File pdfFile = fileChooser.getSelectedFile();
            if (!pdfFile.getName().toLowerCase().endsWith(".pdf")) {
                pdfFile = new java.io.File(pdfFile.getAbsolutePath() + ".pdf");
            }
            // 2. Extraire les données principales du JSON pour le tableau
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            org.json.JSONArray monthly = outputs.getJSONArray("monthly");
            // 3. Générer les images des graphes (on réutilise le code d'affichage)
            java.util.List<java.awt.image.BufferedImage> chartImages = new java.util.ArrayList<>();
            // Graphique 1 : Production mensuelle
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            java.util.List<org.json.JSONObject> monthlyList = monthly.toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList();
            for (org.json.JSONObject m : monthlyList) {
                int idxMois = m.getInt("month") - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                mois.add(nomMois);
                prod.add(m.getDouble("E_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart1.addSeries("Production", mois, prod);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart1));
            // Graphique 2 : Énergie perdue mensuelle
            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
                lost.add(m.getDouble("E_lost_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart2 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Énergie perdue mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart2.addSeries("Energie perdue", mois, lost);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart2));
            // Graphique 3 : Histogramme des états de charge
            java.util.List<String> csLabels = new java.util.ArrayList<>();
            java.util.List<Double> fcs = new java.util.ArrayList<>();
            for (org.json.JSONObject h : outputs.getJSONArray("histogram").toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList()) {
                csLabels.add(h.getDouble("CS_min") + "-" + h.getDouble("CS_max"));
                fcs.add(h.getDouble("f_CS"));
            }
            org.knowm.xchart.CategoryChart chart3 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Histogramme états de charge").xAxisTitle("% charge").yAxisTitle("% jours").build();
            chart3.addSeries("f_CS", csLabels, fcs);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart3));
            // Graphique 4 : % jours batterie pleine vs vide
            java.util.List<Double> ff = new java.util.ArrayList<>();
            java.util.List<Double> fe = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
                ff.add(m.getDouble("f_f"));
                fe.add(m.getDouble("f_e"));
            }
            org.knowm.xchart.CategoryChart chart4 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("% jours batterie pleine vs vide").xAxisTitle("Mois").yAxisTitle("% jours").build();
            chart4.addSeries("Batterie pleine", mois, ff);
            chart4.addSeries("Batterie vide", mois, fe);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart4));
            // 4. Générer le PDF avec PDFBox
            org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
            doc.addPage(page);
            org.apache.pdfbox.pdmodel.PDPageContentStream content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
            float y = 750;
            // Titre
            content.beginText();
            content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 18);
            content.newLineAtOffset(50, y);
            content.showText("Estimation PVGIS Off-Grid");
            content.endText();
            y -= 30;
            // Liste des entrées du formulaire
            content.beginText();
            content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(50, y);
            content.showText("Paramètres d'entrée :");
            content.endText();
            y -= 18;
            String[][] entrees = {
                {"Latitude", latField.getText()},
                {"Longitude", lonField.getText()},
                {"Base de données", radDatabaseCombo.getSelectedItem().toString()},
                {"Puissance PV crête (W)", peakPowerField.getText()},
                {"Inclinaison (°)", angleField.getText()},
                {"Azimut (°)", aspectField.getText()},
                {"Capacité batterie (Wh)", batterySizeField.getText()},
                {"Limite de décharge (%)", cutoffField.getText()},
                {"Consommation par jour (Wh)", consumptionDayField.getText()},
                {"Profil horaire", hourConsumptionField.getText()},
                {"Inclure horizon", useHorizonCheck.isSelected() ? "Oui" : "Non"},
                {"Horizon utilisateur", userHorizonField.getText()},
                {"Format de sortie", outputFormatField.getText()},
                {"Browser", browserCheck.isSelected() ? "Oui" : "Non"}
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
            // Tableau des résultats mensuels (avec colonnes tracées)
            float tableStartY = y;
            float tableStartX = 50;
            float rowHeight = 18;
            float tableWidth = 480;
            float[] colWidths = {70, 90, 110, 110, 100};
            String[] headers = {"Mois", "Prod (Wh)", "Energie perdue (Wh)", "% jours batt. pleine", "% jours batt. vide"};
            // En-tête en gras
            content.setStrokingColor(Color.BLACK);
            content.setNonStrokingColor(Color.LIGHT_GRAY);
            content.addRect(tableStartX, y - rowHeight, tableWidth, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.BLACK);
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
                for (int j = 0; j < headers.length; j++) {
                    content.setStrokingColor(Color.BLACK);
                    content.addRect(nextX, y - rowHeight, colWidths[j], rowHeight);
                    content.stroke();
                    content.beginText();
                    content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                    content.newLineAtOffset(nextX + 2, y - rowHeight + 4);
                    String val = switch (j) {
                        case 0 -> mois.get(i);
                        case 1 -> String.format("%.0f", prod.get(i));
                        case 2 -> String.format("%.0f", lost.get(i));
                        case 3 -> String.format("%.1f", ff.get(i));
                        case 4 -> String.format("%.1f", fe.get(i));
                        default -> "";
                    };
                    content.showText(val);
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
            // Insertion des graphes sous forme d'images
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
        }
    }
}
