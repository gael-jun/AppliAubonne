package vue.EstimationProd;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
// import javax.swing.SwingUtilities;

import vue.ui.TitleBanner;
import vue.ui.UIConstants;
import controller.OffGridController;
import vue.util.StatusBarUtil;
import vue.util.FinancialCharts;

import modele.pvgis.HistogramBucket;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVOffGridRequest;
import modele.pvgis.PVGISResult;
import service.pvgis.PVGISService;
import vue.ui.ToolbarPanel;
import vue.ui.ButtonStyleUtil;
import javax.swing.JButton;
import service.export.*;
import modele.finance.FinancialResult;
// offgrid MVP presenter/view removed in favor of SwingWorker pattern
import modele.pvgis.PVGISModel;
import vue.ui.GraphsPanel;

/**
 * Page d'estimation PVGIS pour systèmes photovoltaïques hors réseau (off-grid).
 * Fournit un formulaire ergonomique, l'appel à l'API PVGIS et l'affichage graphique des résultats.
 * Appels réseau via SwingWorker (EDT non bloquée) et exports unifiés via ExportWorkerFactory / ExportFacade.
 */
public class PageEstimationPVGISOffGrid extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(PageEstimationPVGISOffGrid.class.getName());
    // Champ de saisie pour la latitude du site.
    private JTextField latField;
    // Champ de saisie pour la longitude du site.
    private JTextField lonField;
    // Champ de saisie pour l'horizon utilisateur (optionnel).
    private JTextField userHorizonField;
    // Champ de saisie pour la puissance crête du système PV (W).
    private JTextField peakPowerField;
    // Champ de saisie pour l'inclinaison du module PV (degrés).
    private JTextField angleField;
    // Champ de saisie pour l'azimut du module PV (degrés).
    private JTextField aspectField;
    // Champ de saisie pour la capacité batterie (Wh).
    private JTextField batterySizeField;
    // Champ de saisie pour la limite de décharge batterie (%).
    private JTextField cutoffField;
    // Champ de saisie pour la consommation journalière (Wh).
    private JTextField consumptionDayField;
    // Champ de saisie pour le profil horaire de consommation (optionnel).
    private JTextField hourConsumptionField;
    // Champ de saisie pour le format de sortie (json, csv, ...).
    private JTextField outputFormatField;
    // Liste déroulante pour la base de données de radiation.
    private JComboBox<String> radDatabaseCombo;
    // Case à cocher pour inclure l'horizon naturel.
    private JCheckBox useHorizonCheck;
    // Case à cocher pour le mode browser (affichage interactif).
    private JCheckBox browserCheck;
        // Graphs are displayed via GraphsPanel (see graphsPanel)
    // Label de statut pour l'utilisateur (attente, succès, erreur).
    private final JLabel statusLabel;
    // Toolbar unifiée
    private ToolbarPanel toolbar;
    // Contient le dernier résultat typé reçu de l'API PVGIS.
    private transient PVGISResult lastResult = null; // now from modele.pvgis
    // Liste pour stocker les images des graphes financiers (utilisées pour l'export PDF).
    private final transient java.util.List<java.awt.image.BufferedImage> graphesFinanciersImages = new java.util.ArrayList<>();

    // Derniers paramètres financiers saisis (pour pouvoir re-tracer automatiquement avant export)
    private String lastInvestissement = "0";
    private String lastSubvention = "0";
    private String lastPrixVente = "0";
    private String lastTauxInjection = "0";
    private String lastCoutAnnuel = "0";
    private String lastDuree = "0";
    private String lastTauxActualisation = "0";
    private String lastAnneeDepart = "0";

    // Résultats financiers calculés (conservés pour l'export PDF)
    private final transient List<String> financialAnnees = new ArrayList<>();
    private final transient List<Double> financialCashFlowCumule = new ArrayList<>();
    private final transient List<String> financialAnneesRD = new ArrayList<>();
    private final transient List<Double> financialRecettes = new ArrayList<>();
    private final transient List<Double> financialDepenses = new ArrayList<>();
    private final transient List<Double> financialVAN = new ArrayList<>();
    private double financialVANTotal = 0.0;
    // Grid controller 
    private final transient OffGridController controller = new OffGridController(new PVGISService());
    // Cache helper for financial charts/images
    private final transient FinancialCharts.Cache finCache = new FinancialCharts.Cache();
    private final transient PVGISModel model = new PVGISModel(); // from modele.pvgis
    private transient GraphsPanel graphsPanel;
    

    /**
     * Constructeur : initialise la page d'estimation avec le formulaire et les zones graphiques.
     */
    public PageEstimationPVGISOffGrid() {
        setLayout(new BorderLayout());
        add(new TitleBanner("ESTIMATION DE LA PRODUCTION"), BorderLayout.NORTH);

    JPanel inputPanel = buildInputPanel();
    ToolbarPanel buttonPanel = buildToolbar();
        JPanel leftPanel = buildLeftPanel(inputPanel, buttonPanel);
        add(buildSplit(leftPanel), BorderLayout.CENTER);
        this.statusLabel = buildStatusBar();
        setupModelListeners();
    }

    private JPanel buildInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));

        int inputWidth = 140;
        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6000");
        batterySizeField = new JTextField("10000");
        cutoffField = new JTextField("20");
        consumptionDayField = new JTextField("2000");
        useHorizonCheck = new JCheckBox(); useHorizonCheck.setSelected(true);
        userHorizonField = new JTextField("");
        radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"}); radDatabaseCombo.setSelectedIndex(0);
        angleField = new JTextField("0");
        aspectField = new JTextField("0");
        hourConsumptionField = new JTextField("");
        outputFormatField = new JTextField("json");
        browserCheck = new JCheckBox(); browserCheck.setSelected(false);

        java.util.List<JTextField> sizeFields = java.util.Arrays.asList(latField, lonField, peakPowerField, batterySizeField, cutoffField, consumptionDayField, userHorizonField, angleField, aspectField, hourConsumptionField, outputFormatField);
        for (JTextField tf : sizeFields) {
            tf.setPreferredSize(new Dimension(inputWidth, 24));
            tf.setMaximumSize(new Dimension(inputWidth, 24));
        }
        radDatabaseCombo.setPreferredSize(new Dimension(inputWidth, 24));
        radDatabaseCombo.setMaximumSize(new Dimension(inputWidth, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(2,2,2,8); gc.anchor = GridBagConstraints.LINE_END;

        // Helper pour ajouter une ligne label + composant
        java.util.function.BiConsumer<String, java.awt.Component> addRow = (label, comp) -> {
            // Colonne label (aligné extrême gauche)
            gc.gridx = 0; gc.weightx = 0; gc.fill = GridBagConstraints.NONE; gc.anchor = GridBagConstraints.LINE_START;
            gc.insets = new Insets(2, 4, 2, 8); // petite marge interne
            JLabel jlbl = new JLabel(label);
            jlbl.setHorizontalAlignment(SwingConstants.LEFT);
            inputPanel.add(jlbl, gc);
            // Colonne champ
            gc.gridx = 1; gc.weightx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.anchor = GridBagConstraints.LINE_START;
            gc.insets = new Insets(2, 0, 2, 4);
            inputPanel.add(comp, gc);
            gc.gridy++;
        };

        addRow.accept("Latitude :*", latField);
        addRow.accept("Longitude :*", lonField);
        addRow.accept("Base de données de radiation :", radDatabaseCombo);
        addRow.accept("Puissance PV crête (W) :*", peakPowerField);
        addRow.accept("Inclinaison (°) :", angleField);
        addRow.accept("Azimut (°) :", aspectField);
        addRow.accept("Capacité batterie (Wh) :*", batterySizeField);
        addRow.accept("Limite de décharge (%) :*", cutoffField);
        addRow.accept("Consommation par jour (Wh) :*", consumptionDayField);
        addRow.accept("<html>Profil horaire (24 valeurs) :</html>", hourConsumptionField);
        addRow.accept("Inclure l'horizon :", useHorizonCheck);
        addRow.accept("<html>Horizon utilisateur (8 valeurs) :</html>", userHorizonField);
        addRow.accept("Format de sortie :", outputFormatField);
        addRow.accept("Browser :", browserCheck);

        // Bouton d'estimation (ligne pleine largeur)
        JButton estimateButton = new JButton("Estimer la production");
        javax.swing.Icon estIcon = javax.swing.UIManager.getIcon("FileView.computerIcon");
        if (estIcon != null) estimateButton.setIcon(estIcon);
        estimateButton.setToolTipText("Lancer l'estimation avec les paramètres saisis (Alt+E)");
        ButtonStyleUtil.applyActionButtonStyle(estimateButton, UIConstants.ACTION_GREEN, java.awt.Color.WHITE, UIConstants.ACTION_GREEN_DARK, UIConstants.PAD_PRIMARY);
        estimateButton.addActionListener(e -> estimerProduction());
        gc.gridx = 0; gc.gridwidth = 2; gc.weightx = 1; gc.fill = GridBagConstraints.NONE; gc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(estimateButton, gc);
        gc.gridwidth = 1;

        return inputPanel;
    }

    /** Construit la barre d'outils unifiée. */
    private ToolbarPanel buildToolbar() {
        toolbar = new ToolbarPanel();
        toolbar.onGraphs(e -> afficherGraphes());
        toolbar.onExportCsv(e -> exporterResultatsEnCSV());
        toolbar.onExportPdf(e -> exporterResultatsEnPDF());
        toolbar.onFinance(e -> ouvrirFormulaireFinancier());
        toolbar.setResultAvailable(false);
        toolbar.showFinanceButton(false);
        return toolbar;
    }

    private JPanel buildLeftPanel(JPanel inputPanel, JPanel buttonPanel) {
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.GREY_BG);
        JLabel headerLabel = new JLabel("Système PV Hors Réseau");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setForeground(java.awt.Color.BLACK);
        headerLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        leftPanel.add(headerPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        return leftPanel;
    }

    /** Crée le split entre le panneau gauche et la zone des graphes. */
    private javax.swing.JSplitPane buildSplit(JPanel leftPanel) {
        graphsPanel = new GraphsPanel();
        JScrollPane graphScrollPane = new JScrollPane(graphsPanel);
        graphScrollPane.setPreferredSize(new Dimension(900, 700));
    javax.swing.JSplitPane split = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphScrollPane);
    // Aligne les réglages avec Grid/Tracker
    split.setResizeWeight(0.32);
    split.setDividerLocation(0.32);
    split.setOneTouchExpandable(true);
        return split;
    }

    /** Construit la barre de statut (messages utilisateur). */
    private JLabel buildStatusBar() {
        JLabel s = new JLabel("En attente d'une estimation.");
        s.setOpaque(true);
        s.setBackground(UIConstants.STATUS_GREY);
        s.setForeground(java.awt.Color.BLACK);
        s.setHorizontalAlignment(SwingConstants.CENTER);
        s.setPreferredSize(new Dimension(400, 30));
    JPanel statusPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        statusPanel.add(s);
        add(statusPanel, BorderLayout.SOUTH);
        return s;
    }

    private void setupModelListeners() {
        model.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "status" -> {
                    modele.pvgis.PVGISModel.Status st = (modele.pvgis.PVGISModel.Status) evt.getNewValue();
                    switch (st) {
                        case LOADING -> { statusLabel.setText("En attente de la réponse de l'API..."); statusLabel.setBackground(UIConstants.STATUS_GREY); statusLabel.setForeground(java.awt.Color.BLACK);}                        
                        case READY -> { statusLabel.setText("Succès : données reçues"); statusLabel.setBackground(UIConstants.SUCCESS_GREEN); statusLabel.setForeground(java.awt.Color.WHITE);}                        
                        case ERROR -> { statusLabel.setText("Erreur lors de la requête"); statusLabel.setBackground(UIConstants.ERROR_RED); statusLabel.setForeground(java.awt.Color.WHITE);}                        
                        default -> {}
                    }
                }
                case "result" -> {
                    PVGISResult r = (PVGISResult) evt.getNewValue();
                    lastResult = r;
                    if (toolbar != null) toolbar.showFinanceButton(r != null);
                }
            }
        });
    }

    // Button styling moved to vue.ui.ButtonStyleUtil

    /**
     * Exporte les résultats mensuels (production, énergie perdue, % jours batterie pleine/vide) dans un fichier CSV.
     */
    private void exporterResultatsEnCSV() {
        // Vérifie la présence d'un résultat typé
        if (lastResult == null) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le CSV");
        if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
        java.io.File chosen = fileChooser.getSelectedFile();
        if (chosen == null) return;
        java.io.File csvFile = chosen.getName().toLowerCase().endsWith(".csv") ? chosen : new java.io.File(chosen.getAbsolutePath() + ".csv");

    StatusBarUtil.setStatusExporting(statusLabel, "Export CSV en cours...");
    toolbar.setActionsEnabled(false);

        java.util.function.Supplier<ExportContext> supplier = () -> buildExportContext();
        javax.swing.SwingWorker<Void, Void> worker = controller.createCsvExportWorker(
                csvFile,
                supplier,
                ex -> {
                    LOGGER.log(Level.SEVERE, "Erreur export CSV", ex);
                    JOptionPane.showMessageDialog(PageEstimationPVGISOffGrid.this, "Erreur lors de l'export CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    StatusBarUtil.setStatusError(statusLabel, "Erreur export CSV");
                    boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
                    toolbar.setResultAvailable(hasData);
                },
                () -> {
                    JOptionPane.showMessageDialog(PageEstimationPVGISOffGrid.this, "CSV exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    StatusBarUtil.setStatusSuccess(statusLabel, "Export CSV terminé");
                    boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
                    toolbar.setResultAvailable(hasData);
                }
        );
        worker.execute();
    }

    /**
     * Effectue l'appel à l'API PVGIS avec les paramètres du formulaire et met à jour le statut.
     */
    private void estimerProduction() {
        // Prépare la requête typée à partir des champs du formulaire
        PVOffGridRequest req = new PVOffGridRequest(
            latField.getText(),
            lonField.getText(),
            peakPowerField.getText(),
            batterySizeField.getText(),
            cutoffField.getText(),
            consumptionDayField.getText(),
            angleField.getText(),
            aspectField.getText(),
            (String) radDatabaseCombo.getSelectedItem(),
            useHorizonCheck.isSelected(),
            userHorizonField.getText(),
            hourConsumptionField.getText(),
            outputFormatField.getText(),
            browserCheck.isSelected()
        );
        // Lancer l'appel via SwingWorker (EDT non bloquée)
    StatusBarUtil.setStatusWaiting(statusLabel);
    if (toolbar != null) toolbar.setActionsEnabled(false);

    javax.swing.SwingWorker<modele.pvgis.PVGISResult, Void> worker = controller.createEstimateWorker(
                req,
                res -> {
                    lastResult = res;
                    StatusBarUtil.setStatusSuccess(statusLabel, "Succès : données reçues");
                    if (toolbar != null) toolbar.showFinanceButton(true);
                    boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
                    if (toolbar != null) toolbar.setResultAvailable(hasData);
                },
                ex -> {
                    StatusBarUtil.setStatusError(statusLabel, "Erreur lors de la requête : " + ex.getMessage());
                    if (toolbar != null) toolbar.setActionsEnabled(false);
                }
        );
        worker.execute();
    }



    /**
     * Exporte les résultats (liste des entrées, tableau structuré, graphes) dans un PDF.
     */
    private void exporterResultatsEnPDF() {
        if (lastResult == null) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le PDF");
        if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
        java.io.File chosen = fileChooser.getSelectedFile();
        if (chosen == null) return;
        java.io.File pdfFile = chosen.getName().toLowerCase().endsWith(".pdf") ? chosen : new java.io.File(chosen.getAbsolutePath() + ".pdf");

    StatusBarUtil.setStatusExporting(statusLabel, "Export PDF en cours...");
    toolbar.setActionsEnabled(false);

        java.util.function.Supplier<ExportContext> supplier = () -> buildExportContext();
        javax.swing.SwingWorker<Void, Void> exportWorker = controller.createPdfExportWorker(
                pdfFile,
                supplier,
                () -> tracerGraphesFinanciers(lastInvestissement, lastSubvention, lastPrixVente, lastTauxInjection, lastCoutAnnuel, lastDuree, lastTauxActualisation, lastAnneeDepart),
                ex -> {
                    LOGGER.log(Level.SEVERE, "Erreur export PDF", ex);
                    JOptionPane.showMessageDialog(PageEstimationPVGISOffGrid.this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    StatusBarUtil.setStatusError(statusLabel, "Erreur export PDF");
                    boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
                    toolbar.setResultAvailable(hasData);
                },
                () -> {
                    JOptionPane.showMessageDialog(PageEstimationPVGISOffGrid.this, "PDF exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    StatusBarUtil.setStatusSuccess(statusLabel, "Export PDF terminé");
                    boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
                    toolbar.setResultAvailable(hasData);
                }
        );
        exportWorker.execute();
    }

    // Construit le contexte d'export à partir de l'état courant de la vue et des résultats
    private ExportContext buildExportContext() {
        Map<String, String> inputs = new LinkedHashMap<>();
        inputs.put("Latitude", latField.getText());
        inputs.put("Longitude", lonField.getText());
        inputs.put("Base de données", String.valueOf(radDatabaseCombo.getSelectedItem()));
        inputs.put("Puissance PV crête (W)", peakPowerField.getText());
        inputs.put("Inclinaison (°)", angleField.getText());
        inputs.put("Azimut (°)", aspectField.getText());
        inputs.put("Capacité batterie (Wh)", batterySizeField.getText());
        inputs.put("Limite de décharge (%)", cutoffField.getText());
        inputs.put("Consommation par jour (Wh)", consumptionDayField.getText());
        inputs.put("Profil horaire", hourConsumptionField.getText());
        inputs.put("Inclure horizon", useHorizonCheck.isSelected() ? "Oui" : "Non");
        inputs.put("Horizon utilisateur", userHorizonField.getText());
        inputs.put("Format de sortie", outputFormatField.getText());
        inputs.put("Browser", browserCheck.isSelected() ? "Oui" : "Non");

    FinancialResult fr = null;
        if (!financialAnnees.isEmpty()) {
            fr = new FinancialResult(
                    financialAnnees,
                    financialCashFlowCumule,
                    financialAnneesRD,
                    financialRecettes,
                    financialDepenses,
                    financialVAN,
                    financialVANTotal
        );
    }
    return new ExportContext(inputs, lastResult, fr, new java.util.ArrayList<>(graphesFinanciersImages), "Estimation PVGIS Off-Grid");
    }

    /** Calcule et trace les graphes financiers et met en cache les résultats pour l'export. */
    private void tracerGraphesFinanciers(String investissement, String subvention, String prixVente, String tauxInjection, String coutAnnuel, String duree, String tauxActualisation, String anneeDepart) {
        // Met à jour les derniers paramètres financiers (persistés pour l'export PDF)
        lastInvestissement = investissement;
        lastSubvention = subvention;
        lastPrixVente = prixVente;
        lastTauxInjection = tauxInjection;
        lastCoutAnnuel = coutAnnuel;
        lastDuree = duree;
        lastTauxActualisation = tauxActualisation;
        lastAnneeDepart = anneeDepart;

        // Construit les DTO de calcul
        double investissementInitial = Double.parseDouble(investissement);
        double subventionVal = Double.parseDouble(subvention);
        double prixVenteVal = Double.parseDouble(prixVente);
        double tauxInjectionVal = Double.parseDouble(tauxInjection);
        double coutAnnuelVal = Double.parseDouble(coutAnnuel);
        int dureeVal = Integer.parseInt(duree);
        double tauxActualisationVal = Double.parseDouble(tauxActualisation) / 100.0;
        int anneeDepartVal = Integer.parseInt(anneeDepart);

    modele.finance.FinancialParams params = new modele.finance.FinancialParams(
                investissementInitial, subventionVal, prixVenteVal,
                tauxInjectionVal, coutAnnuelVal, dureeVal,
                tauxActualisationVal, anneeDepartVal
        );

    // Calcul via le service
    service.finance.FinancialCalculator calc = new service.finance.FinancialCalculator();
    modele.finance.FinancialResult fr = calc.compute(lastResult, params);

    // Rendu via helper et mise à jour du cache
    FinancialCharts.renderIntoPanel(fr, graphsPanel, finCache);

    // Recopier dans les champs existants pour l'export (compatibilité avec ExportContext actuel)
    financialAnnees.clear(); financialAnnees.addAll(finCache.annees);
    financialCashFlowCumule.clear(); financialCashFlowCumule.addAll(finCache.cashFlowCumule);
    financialAnneesRD.clear(); financialAnneesRD.addAll(finCache.anneesRD);
    financialRecettes.clear(); financialRecettes.addAll(finCache.recettes);
    financialDepenses.clear(); financialDepenses.addAll(finCache.depenses);
    financialVAN.clear(); financialVAN.addAll(finCache.van);
    financialVANTotal = finCache.vanTotale;
    graphesFinanciersImages.clear(); graphesFinanciersImages.addAll(finCache.images);
    }

    // updateStatus removed (unused)

    // Formulaire financier (aligné sur Grid/Tracker)
    private void ouvrirFormulaireFinancier() {
        JDialog dialog = new JDialog((Frame) null, "Entrées financières", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JTextField investissementField = new JTextField("600");
        JTextField subventionField = new JTextField("200");
        JTextField prixVenteField = new JTextField("0.18");
        JTextField tauxInjectionField = new JTextField("0.6");
        JTextField coutAnnuelField = new JTextField("40");
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
    javax.swing.JButton tracerButton = new javax.swing.JButton("Tracer les graphes");
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
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 12, 12));
        bottomPanel.add(tracerButton);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // Méthode utilitaire de création de graphiques centralisée dans vue.util.ChartsFactory

    // Méthode pour afficher les graphes à partir du JSON actuel
    private void afficherGraphes() {
        graphsPanel.removeAll(); // nettoie le panneau
        if (lastResult == null || lastResult.monthly == null || lastResult.monthly.isEmpty()) {
            JLabel label = new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production.");
            graphsPanel.add(label);
            graphsPanel.revalidate();
            graphsPanel.repaint();
            return; // rien à afficher
        }
        try {
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            for (MonthlyResult m : lastResult.monthly) {
                int idxMois = m.month - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.month);
                mois.add(nomMois);
                prod.add(m.E_d * jours.get(m.month - 1));
            }
            java.util.List<org.knowm.xchart.CategoryChart> chartsAll = new java.util.ArrayList<>();
            if (!mois.isEmpty() && !prod.isEmpty()) {
                chartsAll.add(vue.util.ChartsFactory.createProductionChart(mois, prod));
            }

            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (MonthlyResult m : lastResult.monthly) {
                lost.add(m.E_lost_d * jours.get(m.month - 1));
            }
            if (!mois.isEmpty() && !lost.isEmpty()) {
                chartsAll.add(vue.util.ChartsFactory.createLostEnergyChart(mois, lost));
            }

            java.util.List<String> csLabels = new java.util.ArrayList<>();
            java.util.List<Double> fcs = new java.util.ArrayList<>();
            for (HistogramBucket h : lastResult.histogram) {
                csLabels.add(h.CS_min + "-" + h.CS_max);
                fcs.add(h.f_CS);
            }
            if (!csLabels.isEmpty() && !fcs.isEmpty()) {
                chartsAll.add(vue.util.ChartsFactory.createHistogramChart(csLabels, fcs));
            }

            java.util.List<Double> ff = new java.util.ArrayList<>();
            java.util.List<Double> fe = new java.util.ArrayList<>();
            for (MonthlyResult m : lastResult.monthly) {
                ff.add(m.f_f);
                fe.add(m.f_e);
            }
            chartsAll.add(vue.util.ChartsFactory.createBatteryStatusChart(mois, ff, fe));

            if (!chartsAll.isEmpty()) {
                graphsPanel.setCharts(chartsAll);
            } else {
                graphsPanel.add(new JLabel("Aucune donnée suffisante pour tracer des graphiques."));
            }
            graphsPanel.revalidate();
            graphsPanel.repaint();
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Erreur lors du tracé des graphes", ex);
            JLabel label = new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage());
            graphsPanel.add(label);
            graphsPanel.revalidate();
            graphsPanel.repaint();
        }
    }
}


