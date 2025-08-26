package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Page d'estimation PVGIS pour systèmes photovoltaïques hors réseau (off-grid).
 * Fournit un formulaire ergonomique, l'appel à l'API PVGIS et l'affichage graphique des résultats.
 */
public class PageEstimationPVGISOffGrid extends JPanel {
    // Champ de saisie pour la latitude du site.
    private JTextField latField;
    // Champ de saisie pour la longitude du site.
    private final JTextField lonField;
    // Champ de saisie pour l'horizon utilisateur (optionnel).
    private JTextField userHorizonField;
    // Champ de saisie pour la puissance crête du système PV (W).
    private final JTextField peakPowerField;
    // Champ de saisie pour l'inclinaison du module PV (degrés).
    private final JTextField angleField;
    // Champ de saisie pour l'azimut du module PV (degrés).
    private final JTextField aspectField;
    // Champ de saisie pour la capacité batterie (Wh).
    private final JTextField batterySizeField;
    // Champ de saisie pour la limite de décharge batterie (%).
    private final JTextField cutoffField;
    // Champ de saisie pour la consommation journalière (Wh).
    private final JTextField consumptionDayField;
    // Champ de saisie pour le profil horaire de consommation (optionnel).
    private final JTextField hourConsumptionField;
    // Champ de saisie pour le format de sortie (json, csv, ...).
    private final JTextField outputFormatField;
    // Liste déroulante pour la base de données de radiation.
    private final JComboBox<String> radDatabaseCombo;
    // Case à cocher pour inclure l'horizon naturel.
    private final JCheckBox useHorizonCheck;
    // Case à cocher pour le mode browser (affichage interactif).
    private final JCheckBox browserCheck;
    // Panel d'affichage des graphes.
    private final JPanel graphPanel;
    // Label de statut pour l'utilisateur (attente, succès, erreur).
    private final JLabel statusLabel;
    // Contient la dernière réponse JSON reçue de l'API PVGIS.
    private String lastJson = null;
    // Liste pour stocker les images des graphes financiers (utilisées pour l'export PDF).
    private java.util.List<java.awt.image.BufferedImage> graphesFinanciersImages = new java.util.ArrayList<>();

    // Derniers paramètres financiers saisis (pour pouvoir re-tracer automatiquement avant export)
    private String lastInvestissement = "0";
    private String lastSubvention = "0";
    private String lastPrixVente = "0";
    private String lastTauxInjection = "0";
    private String lastCoutAnnuel = "0";
    private String lastDuree = "0";
    private String lastTauxActualisation = "0";
    private String lastAnneeDepart = "0";

    /**
     * Constructeur : initialise la page d'estimation avec le formulaire et les zones graphiques.
     */
    public PageEstimationPVGISOffGrid() {
        // Définit le layout principal du panneau en BorderLayout
        setLayout(new BorderLayout());
        // Crée un panneau d'entrée avec un GridLayout 2 colonnes
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));

        // --- Champs obligatoires avec valeurs par défaut ---
        latField = new JTextField("48.989"); // valeur par défaut latitude
        lonField = new JTextField("2.277"); // valeur par défaut longitude
        peakPowerField = new JTextField("6000"); // puissance PV par défaut en W
        batterySizeField = new JTextField("10000"); // capacité batterie par défaut en Wh
        cutoffField = new JTextField("20"); // seuil de décharge par défaut en %
        consumptionDayField = new JTextField("2000"); // consommation journalière par défaut en Wh

        // --- Champs facultatifs et options ---
        useHorizonCheck = new JCheckBox(); // inclure l'horizon naturel
        useHorizonCheck.setSelected(true); // activé par défaut
        userHorizonField = new JTextField(""); // horizon utilisateur vide par défaut
        radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"}); // choix raddatabase
        radDatabaseCombo.setSelectedIndex(0);
        angleField = new JTextField("0"); // inclinaison par défaut
        aspectField = new JTextField("0"); // azimut par défaut
        hourConsumptionField = new JTextField(""); // profil horaire vide
        outputFormatField = new JTextField("json"); // format de sortie par défaut
        browserCheck = new JCheckBox(); // option browser
        browserCheck.setSelected(false);

        // Ajout des labels et champs au panneau d'entrée (UI)
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

        // --- Boutons d'action ---
        JButton estimateButton = new JButton("Estimer la production");
        // Lance l'estimation lorsque l'utilisateur clique
        estimateButton.addActionListener((ActionEvent e) -> estimerProduction());
        JButton graphButton = new JButton("Voir graphes");
        // Affiche les graphes en utilisant les données JSON existantes
        graphButton.addActionListener(e -> afficherGraphes());
    JButton exportPdfButton = new JButton("Exporter résultats en PDF");
    // Exporte les résultats dans un fichier PDF (exécuté en arrière-plan pour éviter invokeAndWait sur l'EDT)
    exportPdfButton.addActionListener(e -> new Thread(() -> exporterResultatsEnPDF()).start());
        JButton financeButton = new JButton("Données financières");
        // Ouvre le formulaire financier
        financeButton.addActionListener(e -> ouvrirFormulaireFinancier());
        JButton exportCsvButton = new JButton("Exporter en CSV");
        // Exporte le tableau mensuel au format CSV
        exportCsvButton.addActionListener(e -> exporterResultatsEnCSV());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(estimateButton);
        buttonPanel.add(graphButton);
        buttonPanel.add(exportPdfButton);
        buttonPanel.add(financeButton);
        buttonPanel.add(exportCsvButton);

        // --- Construction du panneau principal vertical ---
        JPanel verticalPanel = new JPanel();
        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));
        // Ajoute le formulaire avec un JScrollPane pour la disponibilité sur petites fenêtres
        verticalPanel.add(new JScrollPane(inputPanel));
        // Ajoute la rangée de boutons
        verticalPanel.add(buttonPanel);

        // --- Zone de statut utilisateur ---
        statusLabel = new JLabel("En attente d'une estimation."); // texte initial
        statusLabel.setOpaque(true); // permet d'afficher un fond coloré
        statusLabel.setBackground(new java.awt.Color(220, 220, 220)); // gris clair
        statusLabel.setForeground(java.awt.Color.BLACK);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setPreferredSize(new Dimension(400, 30));
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        verticalPanel.add(statusPanel);

        // --- Zone d'affichage des graphes (scrollable) ---
        graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        JScrollPane graphScrollPane = new JScrollPane(graphPanel);
        graphScrollPane.setPreferredSize(new Dimension(900, 700));
        verticalPanel.add(graphScrollPane);

        // Ajoute le panneau vertical au centre du panneau principal
        add(verticalPanel, BorderLayout.CENTER);
    }

    /**
     * Exporte les résultats mensuels (production, énergie perdue, % jours batterie pleine/vide) dans un fichier CSV.
     */
    private void exporterResultatsEnCSV() {
        // Vérifie la présence d'un JSON de résultat
        if (lastJson == null || lastJson.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Ouvre un file chooser pour sélectionner la destination CSV
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le CSV");
            if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
            java.io.File csvFile = fileChooser.getSelectedFile();
            if (!csvFile.getName().toLowerCase().endsWith(".csv")) {
                csvFile = new java.io.File(csvFile.getAbsolutePath() + ".csv");
            }
            // Parse le JSON et extrait la section 'monthly'
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            org.json.JSONArray monthly = outputs.getJSONArray("monthly");
            // Noms de mois pour l'export
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            // Estimation du nombre de jours par mois (approx.)
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            // Convertit JSONArray en liste de JSONObject de manière sûre
            java.util.List<org.json.JSONObject> monthlyList = monthly.toList().stream().map(o -> new org.json.JSONObject((java.util.Map)o)).toList();
            // Écriture du CSV en UTF-8 avec PrintWriter (try-with-resources)
            try (java.io.PrintWriter writer = new java.io.PrintWriter(csvFile, java.nio.charset.StandardCharsets.UTF_8)) {
                writer.println("Mois;Production (Wh);Energie perdue (Wh);% jours batt. pleine;% jours batt. vide");
                for (org.json.JSONObject m : monthlyList) {
                    int idxMois = m.getInt("month") - 1;
                    String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                    double prod = m.getDouble("E_d") * jours.get(idxMois);
                    double lost = m.getDouble("E_lost_d") * jours.get(idxMois);
                    double ff = m.getDouble("f_f");
                    double fe = m.getDouble("f_e");
                    writer.printf("%s;%.0f;%.0f;%.1f;%.1f\n", nomMois, prod, lost, ff, fe);
                }
            }
            // Affiche un message de succès
            JOptionPane.showMessageDialog(this, "CSV exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            // En cas d'erreur, log et message d'erreur
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Effectue l'appel à l'API PVGIS avec les paramètres du formulaire et met à jour le statut.
     */
    private void estimerProduction() {
        // Construit l'URL de requête avec les paramètres remplis dans le formulaire
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
        // Met à jour l'interface pour indiquer l'attente
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("En attente de la réponse de l'API...");
            statusLabel.setBackground(new java.awt.Color(220, 220, 220)); // gris clair
            statusLabel.setForeground(java.awt.Color.BLACK);
        });
        // Exécute la requête HTTP dans un thread séparé pour ne pas bloquer l'UI
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url.toString())).GET().build();
                String responseBody = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
                // Stocke la dernière réponse JSON
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
     * Exporte les résultats (liste des entrées, tableau structuré, graphes) dans un PDF.
     */
    private void exporterResultatsEnPDF() {
        if (lastJson == null || lastJson.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Prépare des structures temporaires pour d'éventuels graphiques financiers
            java.util.List<String> annees = new java.util.ArrayList<>();
            java.util.List<Double> cashFlowCumule = new java.util.ArrayList<>();
            java.util.List<String> anneesRD = new java.util.ArrayList<>();
            java.util.List<Double> recettes = new java.util.ArrayList<>();
            java.util.List<Double> depenses = new java.util.ArrayList<>();
            java.util.List<Double> van = new java.util.ArrayList<>();

            // Remplissage d'exemple (si l'utilisateur n'a pas saisi de paramètres financiers)
            for (int i = 0; i < 10; i++) {
                annees.add("202" + i);
                cashFlowCumule.add((double) i * 1000);
                anneesRD.add("202" + i);
                recettes.add((double) i * 500);
                depenses.add((double) i * 300);
                van.add((double) i * 200);
            }

            // 1. Choix du fichier PDF — effectuer le FileChooser sur l'EDT via invokeAndWait
            final java.io.File[] pdfFileHolder = new java.io.File[1];
            try {
                SwingUtilities.invokeAndWait(() -> {
                    javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
                    fileChooser.setDialogTitle("Enregistrer le PDF");
                    if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) {
                        pdfFileHolder[0] = null;
                        return;
                    }
                    java.io.File sel = fileChooser.getSelectedFile();
                    if (!sel.getName().toLowerCase().endsWith(".pdf")) {
                        sel = new java.io.File(sel.getAbsolutePath() + ".pdf");
                    }
                    pdfFileHolder[0] = sel;
                });
            } catch (Exception e) {
                // Échec d'affichage du FileChooser
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur lors de la sélection du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE));
                return;
            }
            if (pdfFileHolder[0] == null) return;
            java.io.File pdfFile = pdfFileHolder[0];
            // 2. Extrait les données mensuelles depuis le JSON
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            org.json.JSONArray monthly = outputs.getJSONArray("monthly");
            // 3. Génération des images des graphiques via XChart
            java.util.List<java.awt.image.BufferedImage> chartImages = new java.util.ArrayList<>();
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            java.util.List<org.json.JSONObject> monthlyList = monthly.toList().stream().map(o -> new org.json.JSONObject((java.util.Map<String, Object>) o)).toList();
            for (org.json.JSONObject m : monthlyList) {
                int idxMois = m.getInt("month") - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                mois.add(nomMois);
                prod.add(m.getDouble("E_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart1 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Production mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart1.addSeries("Production", mois, prod);
            chart1.getStyler().setXAxisLabelRotation(45);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart1));
            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
                lost.add(m.getDouble("E_lost_d") * jours.get(m.getInt("month")-1));
            }
            org.knowm.xchart.CategoryChart chart2 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Énergie perdue mensuelle (Wh/mois)").xAxisTitle("Mois").yAxisTitle("Wh").build();
            chart2.addSeries("Energie perdue", mois, lost);
            chart2.getStyler().setXAxisLabelRotation(45);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart2));
            java.util.List<String> csLabels = new java.util.ArrayList<>();
            java.util.List<Double> fcs = new java.util.ArrayList<>();
            for (org.json.JSONObject h : outputs.getJSONArray("histogram").toList().stream().map(o -> new org.json.JSONObject((java.util.Map<String, Object>) o)).toList()) {
                csLabels.add(h.getDouble("CS_min") + "-" + h.getDouble("CS_max"));
                fcs.add(h.getDouble("f_CS"));
            }
            org.knowm.xchart.CategoryChart chart3 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Histogramme états de charge").xAxisTitle("% charge").yAxisTitle("% jours").build();
            chart3.addSeries("f_CS", csLabels, fcs);
            // Incline les labels de l'axe X pour le histogramme avant de générer l'image pour le PDF
            chart3.getStyler().setXAxisLabelRotation(45);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart3));
            java.util.List<Double> ff = new java.util.ArrayList<>();
            java.util.List<Double> fe = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
                ff.add(m.getDouble("f_f"));
                fe.add(m.getDouble("f_e"));
            }
            org.knowm.xchart.CategoryChart chart4 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("% jours batterie pleine vs vide").xAxisTitle("Mois").yAxisTitle("% jours").build();
            chart4.addSeries("Batterie pleine", mois, ff);
            chart4.addSeries("Batterie vide", mois, fe);
            chart4.getStyler().setXAxisLabelRotation(45);
            chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart4));
            // Ajoute les graphes financiers réels générés lors du tracé
            // S'assure que les graphes financiers sont à jour et ont la bonne inclinaison des labels
            try {
                // Demande au thread EDT de (re)tracer les graphes financiers et attend la fin
                SwingUtilities.invokeAndWait(() -> tracerGraphesFinanciers(lastInvestissement, lastSubvention, lastPrixVente, lastTauxInjection, lastCoutAnnuel, lastDuree, lastTauxActualisation, lastAnneeDepart));
            } catch (Exception ignored) {
                // Si le tracé automatique échoue on continue et on insère ce qui existe
            }
            chartImages.addAll(graphesFinanciersImages);
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
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                content.newLineAtOffset(55f, y);
                content.showText(ligne[0] + " : " + ligne[1]);
                content.endText();
                y -= 13;
            }
            y -= 10;
            // Tableau des résultats mensuels
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

    
    // Formulaire financier
    private void ouvrirFormulaireFinancier() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Entrées financières", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField investissementField = new JTextField();
        JTextField subventionField = new JTextField();
        JTextField prixVenteField = new JTextField();
        JTextField tauxInjectionField = new JTextField();
        JTextField coutAnnuelField = new JTextField();
        JTextField dureeField = new JTextField();
        JTextField tauxActualisationField = new JTextField();
        JTextField anneeDepartField = new JTextField();
        panel.add(new JLabel("Investissement initial (€) :")); panel.add(investissementField);
        panel.add(new JLabel("Subvention (€) :")); panel.add(subventionField);
        panel.add(new JLabel("Prix de vente (€/kWh) :")); panel.add(prixVenteField);
        panel.add(new JLabel("Taux d'injection/autoconsommation (0-1) :")); panel.add(tauxInjectionField);
        panel.add(new JLabel("Coût annuel d'exploitation (€) :")); panel.add(coutAnnuelField);
        panel.add(new JLabel("Durée du projet (années) :")); panel.add(dureeField);
        panel.add(new JLabel("Taux d'actualisation (%) :")); panel.add(tauxActualisationField);
        panel.add(new JLabel("Année de départ :")); panel.add(anneeDepartField);
        JButton tracerButton = new JButton("Tracer les graphes");
        tracerButton.addActionListener(e -> {
            // Sauvegarde les valeurs pour les réutiliser lors d'un export PDF
            lastInvestissement = investissementField.getText();
            lastSubvention = subventionField.getText();
            lastPrixVente = prixVenteField.getText();
            lastTauxInjection = tauxInjectionField.getText();
            lastCoutAnnuel = coutAnnuelField.getText();
            lastDuree = dureeField.getText();
            lastTauxActualisation = tauxActualisationField.getText();
            lastAnneeDepart = anneeDepartField.getText();
            dialog.dispose();
            tracerGraphesFinanciers(lastInvestissement, lastSubvention, lastPrixVente, lastTauxInjection, lastCoutAnnuel, lastDuree, lastTauxActualisation, lastAnneeDepart);
        });
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(tracerButton);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void tracerGraphesFinanciers(String investissement, String subvention, String prixVente, String tauxInjection, String coutAnnuel, String duree, String tauxActualisation, String anneeDepart) {
        // Met à jour les derniers paramètres financiers
        lastInvestissement = investissement;
        lastSubvention = subvention;
        lastPrixVente = prixVente;
        lastTauxInjection = tauxInjection;
        lastCoutAnnuel = coutAnnuel;
        lastDuree = duree;
        lastTauxActualisation = tauxActualisation;
        lastAnneeDepart = anneeDepart;

        graphPanel.removeAll();
        // Extraction de la production annuelle depuis le JSON PVGIS
        double productionAnnuelle = 0;
        try {
            if (lastJson != null && !lastJson.isEmpty()) {
                org.json.JSONObject obj = new org.json.JSONObject(lastJson);
                org.json.JSONObject outputs = obj.getJSONObject("outputs");
                java.util.List<org.json.JSONObject> monthlyList = outputs.getJSONArray("monthly").toList().stream()
                    .map(o -> new org.json.JSONObject((java.util.Map<String, Object>) o)).toList();
                for (org.json.JSONObject m : monthlyList) {
                    productionAnnuelle += m.getDouble("E_d") * (m.getInt("month") == 2 ? 28 : (m.getInt("month") == 4 || m.getInt("month") == 6 || m.getInt("month") == 9 || m.getInt("month") == 11 ? 30 : 31));
                }
                productionAnnuelle /= 1000.0; // conversion Wh -> kWh
            }
        } catch (Exception ex) {
            JLabel label = new JLabel("Erreur extraction production annuelle : " + ex.getMessage());
            graphPanel.add(label);
            graphPanel.revalidate();
            graphPanel.repaint();
            return;
        }
        // Conversion des entrées
        double investissementInitial = Double.parseDouble(investissement);
        double subventionVal = Double.parseDouble(subvention);
        double prixVenteVal = Double.parseDouble(prixVente);
        double tauxInjectionVal = Double.parseDouble(tauxInjection);
        double coutAnnuelVal = Double.parseDouble(coutAnnuel);
        int dureeVal = Integer.parseInt(duree);
        double tauxActualisationVal = Double.parseDouble(tauxActualisation) / 100.0;
        int anneeDepartVal = Integer.parseInt(anneeDepart);
        // Calculs financiers
        java.util.List<Double> cashFlowCumule = new java.util.ArrayList<>();
        java.util.List<Double> recettes = new java.util.ArrayList<>();
        java.util.List<Double> depenses = new java.util.ArrayList<>();
        java.util.List<Double> van = new java.util.ArrayList<>();
        double cashFlow = -investissementInitial + subventionVal;
        cashFlowCumule.add(cashFlow);
        for (int an = 1; an <= dureeVal; an++) {
            double recette = productionAnnuelle * prixVenteVal * tauxInjectionVal;
            double depense = coutAnnuelVal;
            double fluxNet = recette - depense;
            cashFlow += fluxNet;
            cashFlowCumule.add(cashFlow);
            recettes.add(recette);
            depenses.add(depense);
            double fluxActualise = fluxNet / Math.pow(1 + tauxActualisationVal, an);
            van.add(fluxActualise);
        }
        double valeurActualiseeNette = van.stream().mapToDouble(Double::doubleValue).sum() - investissementInitial;
        // Tracé des graphes
        java.util.List<String> annees = new java.util.ArrayList<>();
        for (int i = 0; i <= dureeVal; i++) annees.add("" +(anneeDepartVal + i));

        // Liste pour regrouper les graphiques financiers créés
        java.util.List<org.knowm.xchart.CategoryChart> financialCharts = new java.util.ArrayList<>();

    // Graphique 1 : Cash-flow cumulé
        org.knowm.xchart.CategoryChart chartCF = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Cash-flow cumulé").xAxisTitle("Année").yAxisTitle("€").build();
    // Incline les labels de l'axe X pour améliorer la lisibilité
    chartCF.getStyler().setXAxisLabelRotation(45);
        chartCF.addSeries("Cash-flow cumulé", annees, cashFlowCumule.stream().map(val -> Math.round(val * 100.0) / 100.0).toList());
        financialCharts.add(chartCF);
        graphPanel.add(new org.knowm.xchart.XChartPanel<>(chartCF));

        // Graphique 2 : Recettes et Dépenses annuelles
        java.util.List<String> anneesRD = new java.util.ArrayList<>();
        for (int i = 1; i <= dureeVal; i++) anneesRD.add("" + (anneeDepartVal + i));
        org.knowm.xchart.CategoryChart chartRD = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Recettes et Dépenses annuelles").xAxisTitle("Année").yAxisTitle("€").build();
    // Incline les labels de l'axe X
    chartRD.getStyler().setXAxisLabelRotation(45);
        chartRD.addSeries("Recettes", anneesRD, recettes);
        chartRD.addSeries("Dépenses", anneesRD, depenses);
        financialCharts.add(chartRD);
        graphPanel.add(new org.knowm.xchart.XChartPanel<>(chartRD));

        // Graphique 3 : Flux actualisés (VAN)
        org.knowm.xchart.CategoryChart chartVAN = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("Flux actualisés (VAN)").xAxisTitle("Année").yAxisTitle("€").build();
    // Incline les labels de l'axe X
    chartVAN.getStyler().setXAxisLabelRotation(45);
        chartVAN.addSeries("VAN", anneesRD, van);
        chartVAN.addSeries("Courbe VAN", anneesRD, van).setChartCategorySeriesRenderStyle(org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle.Line);
        financialCharts.add(chartVAN);
        graphPanel.add(new org.knowm.xchart.XChartPanel<>(chartVAN));

        // Met à jour la liste d'images des graphes financiers pour l'export PDF
        graphesFinanciersImages.clear();
                for (org.knowm.xchart.CategoryChart ch : financialCharts) {
            try {
                // S'assure que la rotation est appliquée avant de générer l'image
                ch.getStyler().setXAxisLabelRotation(45);
                graphesFinanciersImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(ch));
            } catch (Exception ex) {
                // Si la génération d'image échoue pour un graphique, on continue avec les autres
                ex.printStackTrace();
            }
        }

        JLabel labelVAN = new JLabel(String.format("Valeur Actualisée Nette (VAN) totale : %.2f €", valeurActualiseeNette));
        graphPanel.add(labelVAN);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    // Méthode utilitaire pour mettre à jour le statut (améliore lisibilité)
    private void updateStatus(String message, Color backgroundColor, Color foregroundColor) {
        statusLabel.setText(message);
        statusLabel.setBackground(backgroundColor);
        statusLabel.setForeground(foregroundColor);
    }

    // Ajoute un graphique XChart au panneau et rafraîchit l'affichage
    private void addGraphToPanel(org.knowm.xchart.CategoryChart chart) {
        graphPanel.add(new org.knowm.xchart.XChartPanel<>(chart));
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    // Crée un graphique catégorie XChart simple avec une série
    private org.knowm.xchart.CategoryChart createChart(String title, String xAxisTitle, String yAxisTitle, java.util.List<String> xData, java.util.List<Double> yData) {
        org.knowm.xchart.CategoryChart chart = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title(title).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle).build();
        // Incline les labels de l'axe X pour améliorer la lisibilité (45 degrés)
        chart.getStyler().setXAxisLabelRotation(45);
        chart.addSeries(title, xData, yData);
        return chart;
    }

    // Méthode pour afficher les graphes à partir du JSON actuel
    private void afficherGraphes() {
        graphPanel.removeAll(); // nettoie le panneau
        if (lastJson == null || lastJson.isEmpty()) {
            JLabel label = new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production.");
            graphPanel.add(label);
            graphPanel.revalidate();
            graphPanel.repaint();
            return; // rien à afficher
        }
        // Tente de parser le JSON et générer les graphiques
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
            java.util.List<org.json.JSONObject> monthlyList = outputs.getJSONArray("monthly").toList().stream()
                .map(o -> new org.json.JSONObject((java.util.Map<String, Object>) o)).toList();
            for (org.json.JSONObject m : monthlyList) {
                int idxMois = m.getInt("month") - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.getInt("month"));
                mois.add(nomMois);
                prod.add(m.getDouble("E_d") * jours.get(m.getInt("month")-1));
            }
            addGraphToPanel(createChart("Production mensuelle", "Mois", "Wh", mois, prod));
            // Graphique 2 : Énergie perdue mensuelle
            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
                lost.add(m.getDouble("E_lost_d") * jours.get(m.getInt("month")-1));
            }
            addGraphToPanel(createChart("Énergie perdue mensuelle", "Mois", "Wh", mois, lost));
            // Graphique 3 : Histogramme des états de charge
            java.util.List<String> csLabels = new java.util.ArrayList<>();
            java.util.List<Double> fcs = new java.util.ArrayList<>();
            for (org.json.JSONObject h : outputs.getJSONArray("histogram").toList().stream().map(o -> new org.json.JSONObject((java.util.Map<String, Object>) o)).toList()) {
                csLabels.add(h.getDouble("CS_min") + "-" + h.getDouble("CS_max"));
                fcs.add(h.getDouble("f_CS"));
            }
            addGraphToPanel(createChart("Histogramme états de charge", "% charge", "% jours", csLabels, fcs));
            // Graphique 4 : % jours batterie pleine vs vide
            java.util.List<Double> ff = new java.util.ArrayList<>();
            java.util.List<Double> fe = new java.util.ArrayList<>();
            for (org.json.JSONObject m : monthlyList) {
                ff.add(m.getDouble("f_f"));
                fe.add(m.getDouble("f_e"));
            }
            org.knowm.xchart.CategoryChart chart4 = new org.knowm.xchart.CategoryChartBuilder().width(600).height(300).title("% jours batterie pleine vs vide").xAxisTitle("Mois").yAxisTitle("% jours").build();
            // Incline les labels de l'axe X pour ce graphique spécifique
            chart4.getStyler().setXAxisLabelRotation(45);
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


