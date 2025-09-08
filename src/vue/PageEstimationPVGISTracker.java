package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import vue.ui.ButtonStyleUtil;

/**
 * Page d'estimation PVGIS pour systèmes photovoltaïques suiveurs (tracker).
 * Fournit un formulaire ergonomique, l'appel à l'API PVGIS et l'affichage graphique des résultats.
 */
public class PageEstimationPVGISTracker extends JPanel {
    /** Champ de saisie pour la latitude du site. */
    private final JTextField latField;
    /** Champ de saisie pour la longitude du site. */
    private final JTextField lonField;
    /** Champ de saisie pour l'horizon utilisateur (optionnel). */
    private final JTextField userHorizonField;
    /** Champ de saisie pour la puissance crête du système PV (kW). */
    private final JTextField peakPowerField;
    /** Champ de saisie pour les pertes système (%). */
    private final JTextField lossField;
    /** Champ de saisie pour l'inclinaison du module PV (degrés). */
    private final JTextField angleField;
    /** Champ de saisie pour l'azimut du module PV (degrés). */
    private final JTextField aspectField;
    /** Champ de saisie pour l'angle axe incliné (degrés). */
    private final JTextField inclinedAxisAngleField;
    /** Champ de saisie pour l'angle axe vertical (degrés). */
    private final JTextField verticalAxisAngleField;
    /** Champ de saisie pour le prix PV (optionnel). */
    private final JTextField pvPriceField;
    /** Champ de saisie pour le coût système (optionnel). */
    private final JTextField systemCostField;
    /** Champ de saisie pour l'intérêt (optionnel). */
    private final JTextField interestField;
    /** Champ de saisie pour la durée de vie (ans). */
    private final JTextField lifetimeField;
    /** Champ de saisie pour le format de sortie (json, csv, ...). */
    private final JTextField outputFormatField;
    /** Liste déroulante pour la base de données de radiation. */
    private final JComboBox<String> radDatabaseCombo;
    /** Liste déroulante pour la technologie PV. */
    private final JComboBox<String> pvTechChoiceCombo;
    /** Liste déroulante pour le type de montage. */
    private final JComboBox<String> mountingPlaceCombo;
    /** Case à cocher pour inclure l'horizon naturel. */
    private final JCheckBox useHorizonCheck;
    /** Case à cocher pour montage fixe. */
    private final JCheckBox fixedCheck;
    /** Case à cocher pour inclinaison optimale. */
    private final JCheckBox optimalInclinationCheck;
    /** Case à cocher pour angles optimaux. */
    private final JCheckBox optimalAnglesCheck;
    /** Case à cocher pour axe incliné. */
    private final JCheckBox inclinedAxisCheck;
    /** Case à cocher pour inclinaison optimale axe incliné. */
    private final JCheckBox inclinedOptimumCheck;
    /** Case à cocher pour axe vertical. */
    private final JCheckBox verticalAxisCheck;
    /** Case à cocher pour inclinaison optimale axe vertical. */
    private final JCheckBox verticalOptimumCheck;
    /** Case à cocher pour double axe. */
    private final JCheckBox twoAxisCheck;
    /** Case à cocher pour le mode browser (affichage interactif). */
    private final JCheckBox browserCheck;
    /** Panel d'affichage des graphes (unifié). */
    private final vue.ui.GraphsPanel graphsPanel;
    /** Label de statut pour l'utilisateur (attente, succès, erreur). */
    private final JLabel statusLabel;
    /** Boutons actions (désactivés avant estimation) */
    private final JButton graphButton;
    private final JButton exportPdfButton;
    private final JButton financeButton;
    // Export menu
    private javax.swing.JButton exportMenuButton;
    private javax.swing.JMenuItem exportPdfMenuItem;
    private javax.swing.JMenuItem exportCsvMenuItem;
    /** Dernière réponse JSON reçue de l'API PVGIS. */
    private String lastJson = null;

    // Paramètres financiers mémorisés
    private String lastInvestissement = "0";
    private String lastSubvention = "0";
    private String lastPrixVente = "0";
    private String lastTauxInjection = "0";
    private String lastCoutAnnuel = "0";
    private String lastDuree = "0";
    private String lastTauxActualisation = "0";
    private String lastAnneeDepart = "0";
    // Résultats financiers en cache pour l'export
    private final java.util.List<String> financialAnnees = new java.util.ArrayList<>();
    private final java.util.List<Double> financialCashFlowCumule = new java.util.ArrayList<>();
    private final java.util.List<String> financialAnneesRD = new java.util.ArrayList<>();
    private final java.util.List<Double> financialRecettes = new java.util.ArrayList<>();
    private final java.util.List<Double> financialDepenses = new java.util.ArrayList<>();
    private final java.util.List<Double> financialVAN = new java.util.ArrayList<>();
    private double financialVANTotal = 0.0;
    private final java.util.List<java.awt.image.BufferedImage> graphesFinanciersImages = new java.util.ArrayList<>();

    /**
     * Construit la page d'estimation PVGIS Suiveur (Tracker) avec formulaire, boutons et zone de graphes.
     */
    public PageEstimationPVGISTracker() {
    setLayout(new BorderLayout());

    JPanel inputPanel = new JPanel(new GridBagLayout());
    // Ajoute un espacement en haut entre le titre et le début du formulaire
    inputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));

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

        // Ajout des champs sur une seule grille à 4 colonnes (label, champ) x 2
        int row = 0;

        // Ligne 1: Latitude (gauche) + Longitude (droite)
        if (latField instanceof javax.swing.JComponent jcLat) {
            Dimension ps = jcLat.getPreferredSize();
            jcLat.setPreferredSize(new Dimension(50, ps.height));
            jcLat.setMinimumSize(new Dimension(50, ps.height));
            jcLat.setMaximumSize(new Dimension(50, ps.height));
        }
        if (lonField instanceof javax.swing.JComponent jcLon) {
            Dimension ps = jcLon.getPreferredSize();
            jcLon.setPreferredSize(new Dimension(50, ps.height));
            jcLon.setMinimumSize(new Dimension(50, ps.height));
            jcLon.setMaximumSize(new Dimension(50, ps.height));
        }
        row = addRowTwoColumns(inputPanel, row, "Latitude :*", latField, "Longitude :*", lonField);

        // Ligne 2: Fixe (gauche) + Base de données (droite)
        row = addRowTwoColumns(inputPanel, row, "Fixe :", fixedCheck, "<html>Base de données<br>de radiation :</html>", radDatabaseCombo);
        // Ligne 3: Puissance + Technologie
        row = addRowTwoColumns(inputPanel, row, "<html>Puissance<br>PV crête (kW) :*</html>", peakPowerField, "Technologie PV :", pvTechChoiceCombo);
        // Ligne 4: Type de montage + Pertes système
        row = addRowTwoColumns(inputPanel, row, "Type de montage :", mountingPlaceCombo, "Pertes système (%) :*", lossField);
        // Ligne 5: Inclinaison + Azimut
        row = addRowTwoColumns(inputPanel, row, "Inclinaison (°) :", angleField, "Azimut (°) :", aspectField);
        // Ligne 6: Inclinaison optimale + Angles optimaux
        row = addRowTwoColumns(inputPanel, row, "Inclinaison optimale :", optimalInclinationCheck, "Angles optimaux :", optimalAnglesCheck);
        // Ligne 7: Axe incliné + Inclinaison optimale axe incliné
        row = addRowTwoColumns(inputPanel, row, "Axe incliné :", inclinedAxisCheck, "<html>Inclinaison optimale <br/>axe incliné :</html>", inclinedOptimumCheck);
        // Ligne 8: Angle axe incliné + Axe vertical
        row = addRowTwoColumns(inputPanel, row, "Angle axe incliné (°) :", inclinedAxisAngleField, "Axe vertical :", verticalAxisCheck);
        // Ligne 9: Inclinaison optimale axe vertical + Angle axe vertical
        row = addRowTwoColumns(inputPanel, row, "<html>Inclinaison opt.<br> axe vertical :</html>", verticalOptimumCheck, "Angle axe vertical (°) :", verticalAxisAngleField);
        // Ligne 10: Double axe + Prix PV
        row = addRowTwoColumns(inputPanel, row, "Double axe :", twoAxisCheck, "Prix PV :", pvPriceField);
        // Ligne 11: Coût système + Intérêt
        row = addRowTwoColumns(inputPanel, row, "<html>Coût système <br/>(si prix PV) :</html>", systemCostField, "Intérêt (si prix PV) :", interestField);
        // Ligne 12: Durée de vie + Inclure l'horizon
        row = addRowTwoColumns(inputPanel, row, "Durée de vie (ans) :", lifetimeField, "Inclure l'horizon :", useHorizonCheck);
        // Ligne 13: Horizon utilisateur + Format de sortie
        row = addRowTwoColumns(inputPanel, row, "<html>Horizon utilisateur<br/> (8 valeurs,séparées <br/>par des virgules) :</html>", userHorizonField, "Format de sortie :", outputFormatField);
        // Ligne 14: Browser (droite vide)
        row = addRowTwoColumns(inputPanel, row, "Browser :", browserCheck, null, null);

    // Bouton Estimer (vert) placé sous les deux colonnes, centré
    JButton estimateButton = new JButton("Estimer la production");
    javax.swing.Icon estIcon = javax.swing.UIManager.getIcon("FileView.computerIcon");
    if (estIcon != null) estimateButton.setIcon(estIcon);
    estimateButton.setToolTipText("Lancer l'estimation avec les paramètres saisis (Alt+E)");
    ButtonStyleUtil.applyActionButtonStyle(estimateButton, new java.awt.Color(76, 175, 80), java.awt.Color.WHITE, new java.awt.Color(34, 139, 34), new java.awt.Insets(6, 12, 6, 12));
    estimateButton.addActionListener((ActionEvent e) -> estimerProduction());
    JPanel estimatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    estimatePanel.add(estimateButton);
    GridBagConstraints gbcBtn = new GridBagConstraints();
    gbcBtn.gridx = 0; gbcBtn.gridy = row; gbcBtn.gridwidth = 4;
    gbcBtn.insets = new java.awt.Insets(6, 2, 6, 2);
    gbcBtn.anchor = GridBagConstraints.CENTER;
    inputPanel.add(estimatePanel, gbcBtn);

    // Toolbar en bas à gauche: Voir graphes + Export (menu déroulant) + Finances
    graphButton = new JButton("Voir graphes");
    javax.swing.Icon graphIcon = javax.swing.UIManager.getIcon("FileView.directoryIcon");
    if (graphIcon != null) graphButton.setIcon(graphIcon);
    graphButton.setToolTipText("Afficher les graphiques (disponible après estimation)");
    graphButton.setEnabled(false);
    graphButton.addActionListener(e -> afficherGraphes());
    ButtonStyleUtil.applyActionButtonStyle(graphButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));

    // Bouton PDF
    exportPdfButton = new JButton("Exporter résultats en PDF");
    exportPdfButton.setEnabled(false);
    exportPdfButton.addActionListener(e -> new Thread(this::exporterResultatsEnPDF).start());
    javax.swing.Icon exportIcon = javax.swing.UIManager.getIcon("FileView.hardDriveIcon");
    if (exportIcon != null) exportPdfButton.setIcon(exportIcon);
    ButtonStyleUtil.applyActionButtonStyle(exportPdfButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));
    // Bouton Export (menu déroulant)
    exportMenuButton = new javax.swing.JButton("Export");
    exportMenuButton.setToolTipText("Options d'export");
    exportMenuButton.setEnabled(false);
    if (exportIcon != null) exportMenuButton.setIcon(exportIcon);
    ButtonStyleUtil.applyActionButtonStyle(exportMenuButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));
    javax.swing.JPopupMenu exportPopup = new javax.swing.JPopupMenu();
    exportPdfMenuItem = new javax.swing.JMenuItem("Exporter en PDF");
    exportPdfMenuItem.setEnabled(false);
    exportPdfMenuItem.addActionListener(e -> new Thread(this::exporterResultatsEnPDF).start());
    exportCsvMenuItem = new javax.swing.JMenuItem("Exporter en CSV");
    exportCsvMenuItem.setEnabled(false);
    exportCsvMenuItem.addActionListener(e -> exporterResultatsEnCSV());
    exportPopup.add(exportPdfMenuItem);
    exportPopup.add(exportCsvMenuItem);
    exportMenuButton.addActionListener(e -> exportPopup.show(exportMenuButton, 0, exportMenuButton.getHeight()));

    // Bouton Finances
    financeButton = new JButton("Finances");
    financeButton.setVisible(false);
    javax.swing.Icon finIcon = javax.swing.UIManager.getIcon("OptionPane.informationIcon");
    try {
        if (finIcon != null) {
            int targetH = 16; // défaut
            javax.swing.Icon gi = graphButton.getIcon();
            javax.swing.Icon ei = exportMenuButton.getIcon();
            if (gi != null) targetH = Math.max(targetH, gi.getIconHeight());
            if (ei != null) targetH = Math.max(targetH, ei.getIconHeight());
            int iw = finIcon.getIconWidth();
            int ih = finIcon.getIconHeight();
            if (ih > 0 && iw > 0 && ih != targetH) {
                java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(iw, ih, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics2D g2 = bi.createGraphics();
                finIcon.paintIcon(null, g2, 0, 0);
                g2.dispose();
                int targetW = Math.max(1, iw * targetH / ih);
                java.awt.Image scaled = bi.getScaledInstance(targetW, targetH, java.awt.Image.SCALE_SMOOTH);
                financeButton.setIcon(new javax.swing.ImageIcon(scaled));
            } else {
                financeButton.setIcon(finIcon);
            }
        }
    } catch (Throwable ignore) {
        if (finIcon != null) financeButton.setIcon(finIcon);
    }
    ButtonStyleUtil.applyActionButtonStyle(financeButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));
    financeButton.addActionListener(e -> ouvrirFormulaireFinancier());

    javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
    toolBar.setFloatable(false);
    toolBar.add(graphButton);
    toolBar.addSeparator();
    toolBar.add(exportMenuButton);
    toolBar.addSeparator();
    toolBar.add(financeButton);
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.add(toolBar, BorderLayout.CENTER);

    // Colonne gauche: en-tête vert + formulaire + toolbar
    JPanel leftPanel = new JPanel(new BorderLayout());
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new java.awt.Color(210, 235, 210));
    JLabel headerLabel = new JLabel("Formulaire pour PV suiveur (Tracker)");
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    headerLabel.setForeground(new java.awt.Color(0, 100, 0));
    headerLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));
    headerPanel.add(headerLabel, BorderLayout.CENTER);
    leftPanel.add(headerPanel, BorderLayout.NORTH);
    leftPanel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
    leftPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Colonne droite: graphes
    graphsPanel = new vue.ui.GraphsPanel();
    JScrollPane graphScrollPane = new JScrollPane(graphsPanel);
    graphScrollPane.setPreferredSize(new Dimension(900, 700));

    javax.swing.JSplitPane split = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphScrollPane);
    split.setResizeWeight(0.32);
    split.setDividerLocation(0.32);
    split.setOneTouchExpandable(true);
    add(split, BorderLayout.CENTER);

    // Barre de statut
    statusLabel = new JLabel("En attente d'une estimation.");
    statusLabel.setOpaque(true);
    statusLabel.setBackground(new java.awt.Color(220, 220, 220));
    statusLabel.setForeground(java.awt.Color.BLACK);
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusLabel.setPreferredSize(new Dimension(400, 30));
    JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    statusPanel.add(statusLabel);
    add(statusPanel, BorderLayout.SOUTH);
    }

    // (plus d'alignement forcé des labels; on garde l'alignement vertical par défaut centré)

    // Ajoute une ligne avec deux colonnes (label,champ) + (label,champ) sur la même ligne
    private int addRowTwoColumns(JPanel panel, int row, String leftLabel, java.awt.Component leftField, String rightLabel, java.awt.Component rightField) {
        // Colonne gauche
        if (leftLabel != null) {
            JLabel lblL = lab(leftLabel);
            lblL.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints cL = new GridBagConstraints();
            cL.gridx = 0; cL.gridy = row; cL.anchor = GridBagConstraints.BASELINE_LEADING;
            cL.insets = new java.awt.Insets(4, 2, 4, 2);
            panel.add(lblL, cL);
        }
        if (leftField != null) {
            if (leftField instanceof javax.swing.JPanel) {
                // Ne pas contraindre la taille du panneau conteneur; laisser s'étendre
            } else if (leftField instanceof javax.swing.JComboBox) {
                javax.swing.JComponent jc = (javax.swing.JComponent) leftField;
                Dimension ps = jc.getPreferredSize();
                jc.setPreferredSize(new Dimension(90, ps.height));
                jc.setMinimumSize(new Dimension(90, ps.height));
                jc.setMaximumSize(new Dimension(90, ps.height));
            } else if (leftField instanceof javax.swing.JComponent jc) {
                Dimension ps = jc.getPreferredSize();
                jc.setPreferredSize(new Dimension(50, ps.height));
                jc.setMinimumSize(new Dimension(50, ps.height));
                jc.setMaximumSize(new Dimension(50, ps.height));
            }
            GridBagConstraints cLF = new GridBagConstraints();
            cLF.gridx = 1; cLF.gridy = row; cLF.anchor = GridBagConstraints.BASELINE_LEADING;
            cLF.insets = new java.awt.Insets(4, 2, 4, 8);
            cLF.weightx = 0.0; cLF.fill = GridBagConstraints.NONE;
            panel.add(leftField, cLF);
        }
        // Colonne droite
        if (rightLabel != null) {
            JLabel lblR = lab(rightLabel);
            lblR.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints cR = new GridBagConstraints();
            cR.gridx = 2; cR.gridy = row; cR.anchor = GridBagConstraints.BASELINE_LEADING;
            cR.insets = new java.awt.Insets(4, 2, 4, 2);
            panel.add(lblR, cR);
        }
        if (rightField != null) {
            if (rightField instanceof javax.swing.JPanel) {
                // Laisser le conteneur gérer ses éléments; pas de clamp
            } else if (rightField instanceof javax.swing.JComboBox) {
                javax.swing.JComponent jc = (javax.swing.JComponent) rightField;
                Dimension ps = jc.getPreferredSize();
                jc.setPreferredSize(new Dimension(90, ps.height));
                jc.setMinimumSize(new Dimension(90, ps.height));
                jc.setMaximumSize(new Dimension(90, ps.height));
            } else if (rightField instanceof javax.swing.JComponent jc) {
                Dimension ps = jc.getPreferredSize();
                jc.setPreferredSize(new Dimension(50, ps.height));
                jc.setMinimumSize(new Dimension(50, ps.height));
                jc.setMaximumSize(new Dimension(50, ps.height));
            }
            GridBagConstraints cRF = new GridBagConstraints();
            cRF.gridx = 3; cRF.gridy = row; cRF.anchor = GridBagConstraints.BASELINE_LEADING;
            cRF.insets = new java.awt.Insets(4, 2, 4, 2);
            cRF.weightx = 0.0; cRF.fill = GridBagConstraints.NONE;
            panel.add(rightField, cRF);
        }
        return row + 1;
    }

    // Crée un label centré verticalement pour éviter qu'un contenu HTML s'aligne en haut
    private static JLabel lab(String text) {
        JLabel l = new JLabel(text);
        l.setVerticalAlignment(SwingConstants.CENTER);
        return l;
    }

    /**
     * Exporte les résultats (PV mensuels + données financières si disponibles) en CSV via ExportFacade.
     */
    private void exporterResultatsEnCSV() {
        if (lastJson == null || lastJson.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le CSV");
            if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
            java.io.File csvFile = fileChooser.getSelectedFile();
            if (!csvFile.getName().toLowerCase().endsWith(".csv")) {
                csvFile = new java.io.File(csvFile.getAbsolutePath() + ".csv");
            }

            // Construire un PVGISResult minimal à partir du JSON
            modele.PVGISResult pvr = buildPVGISResultFromGridJson();

            // Préparer un FinancialResult si présent
            modele.FinancialResult fr = null;
            if (!financialAnnees.isEmpty()) {
                fr = new modele.FinancialResult(
                        new java.util.ArrayList<>(financialAnnees),
                        new java.util.ArrayList<>(financialCashFlowCumule),
                        new java.util.ArrayList<>(financialAnneesRD),
                        new java.util.ArrayList<>(financialRecettes),
                        new java.util.ArrayList<>(financialDepenses),
                        new java.util.ArrayList<>(financialVAN),
                        financialVANTotal
                );
            }

            // Inputs pour contexte (mêmes champs que PDF)
            java.util.Map<String, String> inputs = new java.util.LinkedHashMap<>();
            inputs.put("Latitude", latField.getText());
            inputs.put("Longitude", lonField.getText());
            inputs.put("Base de données de radiation", String.valueOf(radDatabaseCombo.getSelectedItem()));
            inputs.put("Puissance crête (kW)", peakPowerField.getText());
            inputs.put("Pertes système (%)", lossField.getText());
            inputs.put("Inclinaison (°)", angleField.getText());
            inputs.put("Azimut (°)", aspectField.getText());
            inputs.put("Type de montage", String.valueOf(mountingPlaceCombo.getSelectedItem()));
            inputs.put("Technologie PV", String.valueOf(pvTechChoiceCombo.getSelectedItem()));
            inputs.put("Inclure horizon", useHorizonCheck.isSelected() ? "Oui" : "Non");

            export.ExportContext context = new export.ExportContext(inputs, pvr, fr, new java.util.ArrayList<>(graphesFinanciersImages));
            export.ExportFacade facade = new export.ExportFacade();
            export.ExportStrategy strategy = new export.CsvExportStrategy();
            facade.export(strategy, csvFile, context);

            JOptionPane.showMessageDialog(this, "CSV exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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

            // Assure que les graphes affichés sont à jour puis capture les images depuis GraphsPanel
            try {
                SwingUtilities.invokeAndWait(this::afficherGraphes);
            } catch (InterruptedException | java.lang.reflect.InvocationTargetException ie) {
                // on continue avec un fallback si besoin
            }
            java.util.List<java.awt.image.BufferedImage> chartImages = new java.util.ArrayList<>(graphsPanel.getChartImages());
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
            // Si pour une raison quelconque aucun graphe n'est présent, reconstruire un minimum
            if (chartImages.isEmpty()) {
                org.knowm.xchart.CategoryChart chart1 = util.ChartFactory.createMonthlyProductionKWhChart(mois, prod);
                chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart1));
                org.knowm.xchart.CategoryChart chart2 = util.ChartFactory.createIrradiationChart(mois, irradiation);
                chartImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(chart2));
            }

            org.apache.pdfbox.pdmodel.PDDocument doc = new org.apache.pdfbox.pdmodel.PDDocument();
            org.apache.pdfbox.pdmodel.PDPage page = new org.apache.pdfbox.pdmodel.PDPage();
            doc.addPage(page);
            org.apache.pdfbox.pdmodel.PDPageContentStream content = null;
            try {
                content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                float y = 750;
        // Titre + logo
                String titleText = "Estimation PVGIS Suiveur (Tracker)";
                float titleFontSize = 18f;
                org.apache.pdfbox.pdmodel.font.PDFont titleFont = org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;
                content.setFont(titleFont, titleFontSize);

                float titleX = 50f;
                float spacingAfterTitle = 30f; // minimum
                float lineTopY = y; // référence top-line pour aligner logo et titre
                // Tente de charger le logo depuis le classpath puis depuis le dossier ressources/
                try {
                    java.awt.image.BufferedImage logoImg = null;
                    java.io.InputStream is = PageEstimationPVGISTracker.class.getResourceAsStream("/ressources/logo.png");
                    if (is != null) {
                        logoImg = javax.imageio.ImageIO.read(is);
                        is.close();
                    }
                    if (logoImg == null) {
                        java.io.File f = new java.io.File("ressources/logo.png");
                        if (f.exists()) {
                            logoImg = javax.imageio.ImageIO.read(f);
                        }
                    }
                    if (logoImg != null) {
                        org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdLogo = org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory.createFromImage(doc, logoImg);
                        float desiredH = 72f; // triple taille
                        float desiredW = (float) logoImg.getWidth() * desiredH / (float) logoImg.getHeight();
                        // Dessine le logo à gauche du titre, top-aligné avec lineTopY
                        content.drawImage(pdLogo, 50f, lineTopY - desiredH, desiredW, desiredH);
                        titleX = 50f + desiredW + 10f;
                        spacingAfterTitle = Math.max(spacingAfterTitle, desiredH + 8f); // petit espace sous le logo
                    }
                } catch (java.io.IOException ignore) {
                    // Pas de logo disponible; on continue sans
                }
                // Calcule la ligne de base du titre pour top-aligner le texte avec le haut du logo
                float ascentPt = 0.72f * titleFontSize; // fallback
                try {
                    org.apache.pdfbox.pdmodel.font.PDFontDescriptor fd = titleFont.getFontDescriptor();
                    if (fd != null) {
                        ascentPt = (fd.getAscent() / 1000f) * titleFontSize;
                    }
                } catch (Throwable ignore) {}
                // Descend le titre de quelques points sans bouger le logo
                float titleYOffset = 30f; // ~2.8mm, ajustable au besoin
                float titleBaselineY = lineTopY - ascentPt - titleYOffset;
                content.beginText();
                content.newLineAtOffset(titleX, titleBaselineY);
                content.showText(titleText);
                content.endText();
                y -= spacingAfterTitle;
                // Ajoute un interligne supplémentaire entre le logo/titre et "Paramètres d'entrée :"
                y -= 16f;

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

                // Section financière: (re)trace si nécessaire puis tableau et graphes
                try {
                    SwingUtilities.invokeAndWait(() -> tracerGraphesFinanciers(
                        lastInvestissement, lastSubvention, lastPrixVente, lastTauxInjection,
                        lastCoutAnnuel, lastDuree, lastTauxActualisation, lastAnneeDepart));
                } catch (InterruptedException | java.lang.reflect.InvocationTargetException ignore) { }

                // Titre section
                content.beginText();
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(50, y);
                content.showText("Données financières utilisées pour les graphiques : ");
                content.endText();
                y -= 20;

                // En-têtes (ajout de la colonne Cash-flow cumulé)
                float tableStartX2 = 50;
                float rowHeight2 = 18;
                float[] colWidths2 = {90, 110, 110, 110, 120};
                String[] headers2 = {"Année", "Recettes (€)", "Dépenses (€)", "Flux act. (VAN, €)", "CF cumulé (€)"};
                float tableWidth2 = 0f;
                for (float w : colWidths2) tableWidth2 += w;
                content.setStrokingColor(java.awt.Color.BLACK);
                content.setNonStrokingColor(java.awt.Color.LIGHT_GRAY);
                content.addRect(tableStartX2, y - rowHeight2, tableWidth2, rowHeight2);
                content.fill();
                content.setNonStrokingColor(java.awt.Color.BLACK);
                float nextX2 = tableStartX2;
                for (int i = 0; i < headers2.length; i++) {
                    content.beginText();
                    content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 10);
                    content.newLineAtOffset(nextX2 + 2, y - rowHeight2 + 4);
                    content.showText(headers2[i]);
                    content.endText();
                    nextX2 += colWidths2[i];
                }
                y -= rowHeight2;
                // Ligne Année 0 (CF initial) si disponible
                if (!financialAnnees.isEmpty() && !financialCashFlowCumule.isEmpty()) {
                    float startY = y;
                    String[] vals0 = {
                        financialAnnees.get(0),
                        "0",
                        "0",
                        "0",
                        String.format("%.0f", financialCashFlowCumule.get(0))
                    };
                    float x0 = tableStartX2;
                    for (int j = 0; j < headers2.length; j++) {
                        if (y < 100) {
                            content.close();
                            page = new org.apache.pdfbox.pdmodel.PDPage();
                            doc.addPage(page);
                            content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                            y = 750;
                            startY = y;
                        }
                        content.setStrokingColor(java.awt.Color.BLACK);
                        content.addRect(x0, startY - rowHeight2, colWidths2[j], rowHeight2);
                        content.stroke();
                        content.beginText();
                        content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                        content.newLineAtOffset(x0 + 2, startY - rowHeight2 + 4);
                        content.showText(vals0[j]);
                        content.endText();
                        x0 += colWidths2[j];
                    }
                    y -= rowHeight2;
                }

                int n = Math.min(Math.min(financialAnneesRD.size(), financialRecettes.size()), Math.min(financialDepenses.size(), financialVAN.size()));
                // Cash-flow cumulé contient l'année 0; on aligne sur années 1..n, donc on utilise index i+1
                int cfSize = Math.max(0, financialCashFlowCumule.size() - 1);
                n = Math.min(n, cfSize);
                for (int i = 0; i < n; i++) {
                    nextX2 = tableStartX2;
                    String[] vals = {
                        financialAnneesRD.get(i),
                        String.format("%.0f", financialRecettes.get(i)),
                        String.format("%.0f", financialDepenses.get(i)),
                        String.format("%.0f", financialVAN.get(i)),
                        String.format("%.0f", financialCashFlowCumule.get(i + 1))
                    };
                    for (int j = 0; j < headers2.length; j++) {
                        if (y < 100) {
                            content.close();
                            page = new org.apache.pdfbox.pdmodel.PDPage();
                            doc.addPage(page);
                            content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                            y = 750;
                        }
                        content.setStrokingColor(java.awt.Color.BLACK);
                        content.addRect(nextX2, y - rowHeight2, colWidths2[j], rowHeight2);
                        content.stroke();
                        content.beginText();
                        content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 10);
                        content.newLineAtOffset(nextX2 + 2, y - rowHeight2 + 4);
                        content.showText(vals[j]);
                        content.endText();
                        nextX2 += colWidths2[j];
                    }
                    y -= rowHeight2;
                }
                // Ajoute un petit espacement sous le tableau pour éviter toute superposition
                y -= 15;
                // VAN totale
                if (y < 120) {
                    content.close();
                    page = new org.apache.pdfbox.pdmodel.PDPage();
                    doc.addPage(page);
                    content = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);
                    y = 750;
                }
                content.beginText();
                content.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD, 11);
                content.newLineAtOffset(50, y);
                content.showText(String.format("VAN totale (hors investissement) : %.2f €", financialVANTotal));
                content.endText();
                y -= 24;

                // Graphes financiers
                for (java.awt.image.BufferedImage img : new java.util.ArrayList<>(graphesFinanciersImages)) {
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
                // Numérotation des pages: "Page X / Y" centré en pied de page
                int total = doc.getNumberOfPages();
                float fontSize = 9f;
                for (int i = 0; i < total; i++) {
                    org.apache.pdfbox.pdmodel.PDPage pg = doc.getPage(i);
                    float pageWidth = pg.getMediaBox().getWidth();
                    String txt = "Page " + (i + 1) + " / " + total;
                    float textWidth = org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA.getStringWidth(txt) / 1000f * fontSize;
                    float xFooter = (pageWidth - textWidth) / 2f;
                    float yFooter = 30f;
                    try (org.apache.pdfbox.pdmodel.PDPageContentStream footer = new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, pg, org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode.APPEND, true, true)) {
                        footer.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, fontSize);
                        footer.beginText();
                        footer.newLineAtOffset(xFooter, yFooter);
                        footer.showText(txt);
                        footer.endText();
                    }
                }
                doc.save(pdfFile);
                doc.close();
                JOptionPane.showMessageDialog(this, "PDF exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                try {
                    if (content != null) content.close();
                } catch (java.io.IOException ex2) {
                    // ignore
                }
                try {
                    doc.close();
                } catch (java.io.IOException ex2) {
                    // ignore
                }
            }
    } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
                    statusLabel.setText("Succès : Données de PVGIS reçues");
                    statusLabel.setBackground(new java.awt.Color(0, 180, 0)); // vert
                    statusLabel.setForeground(java.awt.Color.WHITE);
                    graphButton.setEnabled(true);
                    exportPdfButton.setEnabled(true);
                    if (exportMenuButton != null) exportMenuButton.setEnabled(true);
                    if (exportPdfMenuItem != null) exportPdfMenuItem.setEnabled(true);
                    if (exportCsvMenuItem != null) exportCsvMenuItem.setEnabled(true);
                    financeButton.setVisible(true);
                });
            } catch (InterruptedException | java.io.IOException ex) {
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Erreur lors de la requête : " + ex.getMessage());
                    statusLabel.setBackground(new java.awt.Color(200, 0, 0)); // rouge
                    statusLabel.setForeground(java.awt.Color.WHITE);
                });
            }
        }).start();
    }

    // Ouvre le formulaire financier et déclenche le tracé
    private void ouvrirFormulaireFinancier() {
    javax.swing.JDialog dialog = new javax.swing.JDialog((java.awt.Frame) null, "Entrées financières", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField investissementField = new JTextField("600");
        JTextField subventionField = new JTextField("200");
        JTextField prixVenteField = new JTextField("0.18");
        JTextField tauxInjectionField = new JTextField("0.6");
        JTextField coutAnnuelField = new JTextField("60");
        JTextField dureeField = new JTextField("20");
        JTextField tauxActualisationField = new JTextField("0.3");
        JTextField anneeDepartField = new JTextField("2025");
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
        JPanel bottom = new JPanel();
        bottom.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 12, 12));
        bottom.add(tracerButton);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(bottom, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void tracerGraphesFinanciers(String investissement, String subvention, String prixVente, String tauxInjection, String coutAnnuel, String duree, String tauxActualisation, String anneeDepart) {
        graphsPanel.removeAll();
        // Conversion des entrées
        double investissementInitial = parseDoubleSafe(investissement);
        double subventionVal = parseDoubleSafe(subvention);
        double prixVenteVal = parseDoubleSafe(prixVente);
        double tauxInjectionVal = parseDoubleSafe(tauxInjection);
        double coutAnnuelVal = parseDoubleSafe(coutAnnuel);
        int dureeVal = (int) parseDoubleSafe(duree);
        double tauxActualisationVal = parseDoubleSafe(tauxActualisation) / 100.0;
        int anneeDepartVal = (int) parseDoubleSafe(anneeDepart);

        modele.PVGISResult pvr = buildPVGISResultFromGridJson();
        modele.FinancialParams params = new modele.FinancialParams(
                investissementInitial, subventionVal, prixVenteVal,
                tauxInjectionVal, coutAnnuelVal, dureeVal,
                tauxActualisationVal, anneeDepartVal
        );

        service.FinancialCalculator calc = new service.FinancialCalculator();
        modele.FinancialResult fr = calc.compute(pvr, params);

    java.util.List<org.knowm.xchart.CategoryChart> financialCharts = new java.util.ArrayList<>();
    // Conserver les valeurs intactes; le formatage des ordonnées est géré dans ChartFactory (max 2 décimales)
    financialCharts.add(util.ChartFactory.createCashFlowChart(fr.annees, fr.cashFlowCumule));
        financialCharts.add(util.ChartFactory.createRevenueExpenseChart(fr.anneesRD, fr.recettes, fr.depenses));
        financialCharts.add(util.ChartFactory.createVANChart(fr.anneesRD, fr.van));
        graphsPanel.setCharts(financialCharts);

        graphesFinanciersImages.clear();
        for (org.knowm.xchart.CategoryChart ch : financialCharts) {
            try {
                graphesFinanciersImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(ch));
            } catch (RuntimeException ignore) { }
        }

        // Stockage pour export
        financialAnnees.clear();
        financialCashFlowCumule.clear();
        financialAnneesRD.clear();
        financialRecettes.clear();
        financialDepenses.clear();
        financialVAN.clear();
        financialVANTotal = fr.vanTotale;
        financialAnnees.addAll(fr.annees);
        financialCashFlowCumule.addAll(fr.cashFlowCumule);
        financialAnneesRD.addAll(fr.anneesRD);
        financialRecettes.addAll(fr.recettes);
        financialDepenses.addAll(fr.depenses);
        financialVAN.addAll(fr.van);

        JLabel labelVAN = new JLabel(String.format("Valeur Actualisée Nette (VAN) totale : %.2f €", fr.vanTotale));
        graphsPanel.add(labelVAN);
        graphsPanel.revalidate();
        graphsPanel.repaint();
    }

    private modele.PVGISResult buildPVGISResultFromGridJson() {
        try {
            if (lastJson == null || lastJson.isEmpty()) return new modele.PVGISResult(java.util.List.of(), java.util.List.of());
            org.json.JSONObject obj = new org.json.JSONObject(lastJson);
            org.json.JSONObject outputs = obj.getJSONObject("outputs");
            org.json.JSONObject monthly = outputs.getJSONObject("monthly");
            org.json.JSONArray monthlyFixed = monthly.has("fixed") ? monthly.getJSONArray("fixed") : null;
            if (monthlyFixed == null) return new modele.PVGISResult(java.util.List.of(), java.util.List.of());
            java.util.List<modele.MonthlyResult> monthlyList = new java.util.ArrayList<>();
            for (int i = 0; i < monthlyFixed.length(); i++) {
                org.json.JSONObject m = monthlyFixed.getJSONObject(i);
                int month = m.getInt("month");
                double Em = m.optDouble("E_m", 0.0); // kWh/mois
                int days = switch (month) { case 4,6,9,11 -> 30; case 2 -> 28; default -> 31; };
                double Ed_wh_per_day = (Em * 1000.0) / Math.max(1, days); // Wh/jour
                monthlyList.add(new modele.MonthlyResult(month, Ed_wh_per_day, 0.0, 0.0, 0.0));
            }
            return new modele.PVGISResult(monthlyList, java.util.List.of());
        } catch (RuntimeException ex) {
            return new modele.PVGISResult(java.util.List.of(), java.util.List.of());
        }
    }

    private static double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s == null || s.isBlank() ? "0" : s.trim()); } catch (NumberFormatException e) { return 0.0; }
    }

    /**
     * Affiche les graphes de résultats à partir du JSON retourné par l'API PVGIS.
     */
    private void afficherGraphes() {
        graphsPanel.removeAll();
        if (lastJson == null || lastJson.isEmpty()) {
            JLabel label = new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production.");
            graphsPanel.add(label);
            graphsPanel.revalidate();
            graphsPanel.repaint();
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
                graphsPanel.add(label);
                graphsPanel.revalidate();
                graphsPanel.repaint();
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
            java.util.List<org.knowm.xchart.CategoryChart> charts = new java.util.ArrayList<>();
            charts.add(util.ChartFactory.createMonthlyProductionKWhChart(mois, prod));
            // Diagramme 2 : Irradiation mensuelle (kWh/m2/mois)
            if (irradiationOk) {
                charts.add(util.ChartFactory.createIrradiationChart(mois, irradiation));
            } else {
                JLabel label = new JLabel("Champ 'H(i)_m' (irradiation sur plan incliné) absent dans la réponse JSON PVGIS.");
                graphsPanel.add(label);
            }
            // Affiche via GraphsPanel
            graphsPanel.setCharts(charts);
            graphsPanel.revalidate();
            graphsPanel.repaint();
    } catch (RuntimeException ex) {
            JLabel label = new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage());
            graphsPanel.add(label);
            graphsPanel.revalidate();
            graphsPanel.repaint();
        }
    }
}
