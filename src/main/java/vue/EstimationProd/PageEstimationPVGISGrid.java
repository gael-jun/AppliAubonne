package vue.EstimationProd;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.*;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;

import controleur.EstimationProd.GridController;
import service.export.CsvExportStrategy;
import service.export.ExportContext;
import service.export.PdfExportStrategy;
import modele.finance.FinancialParams;
import modele.finance.FinancialResult;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGridAndTrackerRequest;
import modele.pvgis.PVGISResult;
import service.finance.FinancialCalculator;
import service.pvgis.PVGISService;
import vue.ui.ButtonStyleUtil;
import vue.ui.GraphsPanel;
import vue.ui.TitleBanner;
import vue.ui.ToolbarPanel;
import vue.ui.UIConstants;
import vue.util.ChartsFactory;
import vue.util.ExportWorkerFactory;

/**
 * Page d'estimation PVGIS pour un système raccordé réseau.
 * Version refactorisée (package EstimationProd) lisible et alignée sur OffGrid/Tracker.
 */
public class PageEstimationPVGISGrid extends JPanel {
    private final GridController controller;

    // Paramètres d'entrée
    private JTextField latField;
    private JTextField lonField;
    private JTextField userHorizonField;
    private JTextField peakPowerField;
    private JTextField lossField;
    private JTextField angleField;
    private JTextField aspectField;
    private JTextField inclinedAxisAngleField;
    private JTextField verticalAxisAngleField;
    private JTextField pvPriceField;
    private JTextField systemCostField;
    private JTextField interestField;
    private JTextField lifetimeField;
    private JTextField outputFormatField;
    private JComboBox<String> radDatabaseCombo;
    private JComboBox<String> pvTechChoiceCombo;
    private JComboBox<String> mountingPlaceCombo;
    private JCheckBox useHorizonCheck;
    private JCheckBox fixedCheck;
    private JCheckBox optimalInclinationCheck;
    private JCheckBox optimalAnglesCheck;
    private JCheckBox inclinedAxisCheck;
    private JCheckBox inclinedOptimumCheck;
    private JCheckBox verticalAxisCheck;
    private JCheckBox verticalOptimumCheck;
    private JCheckBox twoAxisCheck;
    private JCheckBox browserCheck;

    private GraphsPanel graphsPanel;
    private final JLabel statusLabel;
    private ToolbarPanel toolbar;
    private PVGISResult lastResult = null;

    // Cache financier pour export
    private String lastInvestissement = "0";
    private String lastSubvention = "0";
    private String lastPrixVente = "0";
    private String lastTauxInjection = "0";
    private String lastCoutAnnuel = "0";
    private String lastDuree = "0";
    private String lastTauxActualisation = "0";
    private String lastAnneeDepart = "0";
    private final List<String> financialAnnees = new ArrayList<>();
    private final List<Double> financialCashFlowCumule = new ArrayList<>();
    private final List<String> financialAnneesRD = new ArrayList<>();
    private final List<Double> financialRecettes = new ArrayList<>();
    private final List<Double> financialDepenses = new ArrayList<>();
    private final List<Double> financialVAN = new ArrayList<>();
    private double financialVANTotal = 0.0;
    private final List<java.awt.image.BufferedImage> graphesFinanciersImages = new ArrayList<>();

    public PageEstimationPVGISGrid() { this(new PVGISService()); }
    public PageEstimationPVGISGrid(PVGISService pvgisService) {
        this.controller = new GridController(pvgisService);
        setLayout(new BorderLayout());
        add(new TitleBanner("ESTIMATION DE LA PRODUCTION"), BorderLayout.NORTH);
        JPanel inputPanel = buildInputPanel();
        ToolbarPanel buttonPanel = buildToolbar();
        JPanel leftPanel = buildLeftPanel(inputPanel, buttonPanel);
        add(buildSplit(leftPanel), BorderLayout.CENTER);
        this.statusLabel = buildStatusBar();
    }

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

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6");
        lossField = new JTextField("14");
        radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"});
        pvTechChoiceCombo = new JComboBox<>(new String[]{"crystSi", "CIS", "CdTe", "amorphous"});
        mountingPlaceCombo = new JComboBox<>(new String[]{"free", "building"});
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

        int row = 0;
        resizeField(latField, 50);
        resizeField(lonField, 50);

        row = addRowTwoColumns(panel, row, "Latitude :*", latField, "Longitude :*", lonField);
        row = addRowTwoColumns(panel, row, "Fixe :", fixedCheck, "<html>Base de données<br>de radiation :</html>", radDatabaseCombo);
        row = addRowTwoColumns(panel, row, "<html>Puissance<br>PV crête (kW) :*</html>", peakPowerField, "Technologie PV :", pvTechChoiceCombo);
        row = addRowTwoColumns(panel, row, "Type de montage :", mountingPlaceCombo, "Pertes système (%) :*", lossField);
        row = addRowTwoColumns(panel, row, "Inclinaison (°) :", angleField, "Azimut (°) :", aspectField);
        row = addRowTwoColumns(panel, row, "Inclinaison optimale :", optimalInclinationCheck, "Angles optimaux :", optimalAnglesCheck);
        row = addRowTwoColumns(panel, row, "Axe incliné :", inclinedAxisCheck, "<html>Inclinaison optimale <br/>axe incliné :</html>", inclinedOptimumCheck);
        row = addRowTwoColumns(panel, row, "Angle axe incliné (°) :", inclinedAxisAngleField, "Axe vertical :", verticalAxisCheck);
        row = addRowTwoColumns(panel, row, "<html>Inclinaison opt.<br> axe vertical :</html>", verticalOptimumCheck, "Angle axe vertical (°) :", verticalAxisAngleField);
        row = addRowTwoColumns(panel, row, "Double axe :", twoAxisCheck, "Prix PV :", pvPriceField);
        row = addRowTwoColumns(panel, row, "<html>Coût système <br/>(si prix PV) :</html>", systemCostField, "Intérêt (si prix PV) :", interestField);
        row = addRowTwoColumns(panel, row, "Durée de vie (ans) :", lifetimeField, "Inclure l'horizon :", useHorizonCheck);
        row = addRowTwoColumns(panel, row, "<html>Horizon utilisateur<br/> (8 valeurs,séparées <br/>par des virgules) :</html>", userHorizonField, "Format de sortie :", outputFormatField);
        row = addRowTwoColumns(panel, row, "Browser :", browserCheck, null, null);

        JButton estimateButton = new JButton("Estimer la production");
        Icon estIcon = UIManager.getIcon("FileView.computerIcon");
        if (estIcon != null) estimateButton.setIcon(estIcon);
        estimateButton.setToolTipText("Lancer l'estimation avec les paramètres saisis (Alt+E)");
        ButtonStyleUtil.applyActionButtonStyle(estimateButton, UIConstants.ACTION_GREEN, java.awt.Color.WHITE, UIConstants.ACTION_GREEN_DARK, UIConstants.PAD_PRIMARY);
        estimateButton.addActionListener((ActionEvent e) -> estimerProduction());

        JPanel estimatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        estimatePanel.add(estimateButton);
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 0; gbcBtn.gridy = row; gbcBtn.gridwidth = 4;
        gbcBtn.insets = new java.awt.Insets(6, 2, 6, 2);
        gbcBtn.anchor = GridBagConstraints.CENTER;
        panel.add(estimatePanel, gbcBtn);
        return panel;
    }

    private void resizeField(JComponent comp, int width) {
        Dimension ps = comp.getPreferredSize();
        comp.setPreferredSize(new Dimension(width, ps.height));
        comp.setMinimumSize(new Dimension(width, ps.height));
        comp.setMaximumSize(new Dimension(width, ps.height));
    }

    private JPanel buildLeftPanel(JPanel inputPanel, JPanel buttonPanel) {
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.GREY_BG);
        JLabel headerLabel = new JLabel("Système PV Couplé Au Réseau");
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setForeground(java.awt.Color.BLACK);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        leftPanel.add(headerPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        return leftPanel;
    }

    private JSplitPane buildSplit(JPanel leftPanel) {
        graphsPanel = new GraphsPanel();
        JScrollPane graphScrollPane = new JScrollPane(graphsPanel);
        graphScrollPane.setPreferredSize(new Dimension(900, 700));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphScrollPane);
        split.setResizeWeight(0.32);
        split.setDividerLocation(0.32);
        split.setOneTouchExpandable(true);
        return split;
    }

    private JLabel buildStatusBar() {
        JLabel s = new JLabel("En attente d'une estimation.");
        s.setOpaque(true);
        s.setBackground(UIConstants.STATUS_GREY);
        s.setForeground(java.awt.Color.BLACK);
        s.setHorizontalAlignment(SwingConstants.CENTER);
        s.setPreferredSize(new Dimension(400, 30));
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(s);
        add(statusPanel, BorderLayout.SOUTH);
        return s;
    }

    private int addRowTwoColumns(JPanel panel, int row, String leftLabel, java.awt.Component leftField, String rightLabel, java.awt.Component rightField) {
        if (leftLabel != null) {
            JLabel lblL = lab(leftLabel);
            lblL.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints cL = new GridBagConstraints();
            cL.gridx = 0; cL.gridy = row; cL.anchor = GridBagConstraints.BASELINE_LEADING;
            cL.insets = new java.awt.Insets(4, 2, 4, 2);
            panel.add(lblL, cL);
        }
        if (leftField != null) {
            sizeComponent(leftField);
            GridBagConstraints cLF = new GridBagConstraints();
            cLF.gridx = 1; cLF.gridy = row; cLF.anchor = GridBagConstraints.BASELINE_LEADING;
            cLF.insets = new java.awt.Insets(4, 2, 4, 8);
            panel.add(leftField, cLF);
        }
        if (rightLabel != null) {
            JLabel lblR = lab(rightLabel);
            lblR.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints cR = new GridBagConstraints();
            cR.gridx = 2; cR.gridy = row; cR.anchor = GridBagConstraints.BASELINE_LEADING;
            cR.insets = new java.awt.Insets(4, 2, 4, 2);
            panel.add(lblR, cR);
        }
        if (rightField != null) {
            sizeComponent(rightField);
            GridBagConstraints cRF = new GridBagConstraints();
            cRF.gridx = 3; cRF.gridy = row; cRF.anchor = GridBagConstraints.BASELINE_LEADING;
            cRF.insets = new java.awt.Insets(4, 2, 4, 2);
            panel.add(rightField, cRF);
        }
        return row + 1;
    }

    private void sizeComponent(java.awt.Component comp) {
        if (comp instanceof JComboBox<?> jc) {
            Dimension ps = jc.getPreferredSize();
            jc.setPreferredSize(new Dimension(90, ps.height));
            jc.setMinimumSize(new Dimension(90, ps.height));
            jc.setMaximumSize(new Dimension(90, ps.height));
        } else if (comp instanceof JComponent jc) {
            Dimension ps = jc.getPreferredSize();
            jc.setPreferredSize(new Dimension(50, ps.height));
            jc.setMinimumSize(new Dimension(50, ps.height));
            jc.setMaximumSize(new Dimension(50, ps.height));
        }
    }

    private static JLabel lab(String text) {
        JLabel l = new JLabel(text);
        l.setVerticalAlignment(SwingConstants.CENTER);
        return l;
    }

    private void estimerProduction() {
        statusLabel.setText("En attente de la réponse de l'API...");
        statusLabel.setBackground(UIConstants.STATUS_GREY);
        statusLabel.setForeground(java.awt.Color.BLACK);
        if (toolbar != null) toolbar.setActionsEnabled(false);

        PVGridAndTrackerRequest reqObj = new PVGridAndTrackerRequest(
                latField.getText(), lonField.getText(), peakPowerField.getText(), lossField.getText(),
                (String) radDatabaseCombo.getSelectedItem(), (String) pvTechChoiceCombo.getSelectedItem(),
                (String) mountingPlaceCombo.getSelectedItem(), fixedCheck.isSelected(), angleField.getText(), aspectField.getText(),
                optimalInclinationCheck.isSelected(), optimalAnglesCheck.isSelected(), inclinedAxisCheck.isSelected(), inclinedOptimumCheck.isSelected(),
                inclinedAxisAngleField.getText(), verticalAxisCheck.isSelected(), verticalOptimumCheck.isSelected(), verticalAxisAngleField.getText(),
                twoAxisCheck.isSelected(), pvPriceField.getText(), systemCostField.getText(), interestField.getText(), lifetimeField.getText(),
                useHorizonCheck.isSelected(), userHorizonField.getText(), outputFormatField.getText(), browserCheck.isSelected(), true
        );

        SwingWorker<PVGISResult, Void> worker = controller.createEstimateWorker(
                reqObj,
                res -> {
                    lastResult = res;
                    statusLabel.setText("Succès : Données de PVGIS reçues");
                    statusLabel.setBackground(UIConstants.SUCCESS_GREEN);
                    statusLabel.setForeground(java.awt.Color.WHITE);
                    boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
                    if (toolbar != null) {
                        toolbar.setResultAvailable(hasData);
                        toolbar.showFinanceButton(true);
                    }
                },
                ex -> {
                    statusLabel.setText("Erreur lors de la requête : " + ex.getMessage());
                    statusLabel.setBackground(UIConstants.ERROR_RED);
                    statusLabel.setForeground(java.awt.Color.WHITE);
                    if (toolbar != null) toolbar.setActionsEnabled(false);
                }
        );
        worker.execute();
    }

    private void exporterResultatsEnCSV() {
        if (lastResult == null || lastResult.monthly == null || lastResult.monthly.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le CSV");
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        java.io.File chosen = fileChooser.getSelectedFile();
        if (chosen == null) return;
        java.io.File csvFile = chosen.getName().toLowerCase().endsWith(".csv") ? chosen : new java.io.File(chosen.getAbsolutePath() + ".csv");

        statusLabel.setText("Export CSV en cours...");
        statusLabel.setBackground(UIConstants.STATUS_GREY);
        statusLabel.setForeground(java.awt.Color.BLACK);
        if (toolbar != null) toolbar.setActionsEnabled(false);

        Supplier<ExportContext> supplier = () -> {
            PVGISResult pvr = buildPVGISResultFromGridJson();
            FinancialResult fr = buildFinancialResultIfAny();
            Map<String, String> inputs = collectCommonInputs();
            return new ExportContext(inputs, pvr, fr, new ArrayList<>(graphesFinanciersImages));
        };
        SwingWorker<Void, Void> worker = ExportWorkerFactory.createExportWorker(
                csvFile,
                supplier,
                new CsvExportStrategy(),
                null,
                ex -> onExportError("CSV", ex),
                () -> onExportSuccess("CSV")
        );
        worker.execute();
    }

    private void exporterResultatsEnPDF() {
        if (lastResult == null || lastResult.monthly == null || lastResult.monthly.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le PDF");
            if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            java.io.File chosen = fileChooser.getSelectedFile();
            if (chosen == null) return;
            java.io.File pdfFile = chosen.getName().toLowerCase().endsWith(".pdf") ? chosen : new java.io.File(chosen.getAbsolutePath() + ".pdf");

            statusLabel.setText("Export PDF en cours...");
            statusLabel.setBackground(UIConstants.STATUS_GREY);
            statusLabel.setForeground(java.awt.Color.BLACK);
            if (toolbar != null) toolbar.setActionsEnabled(false);

            Supplier<ExportContext> supplier = () -> {
                PVGISResult pvr = buildPVGISResultFromGridJson();
                FinancialResult fr = buildFinancialResultIfAny();
                Map<String, String> inputs = collectCommonInputs();
                return new ExportContext(inputs, pvr, fr, new ArrayList<>(graphesFinanciersImages), "Estimation PVGIS Grid-Connected");
            };
            SwingWorker<Void, Void> exportWorker = ExportWorkerFactory.createExportWorker(
                    pdfFile,
                    supplier,
                    new PdfExportStrategy(),
                    this::afficherGraphes,
                    ex -> onExportError("PDF", ex),
                    () -> onExportSuccess("PDF")
            );
            exportWorker.execute();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onExportError(String type, Throwable ex) {
        JOptionPane.showMessageDialog(this, "Erreur lors de l'export " + type + " : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Erreur export " + type);
        statusLabel.setBackground(UIConstants.ERROR_RED);
        statusLabel.setForeground(java.awt.Color.WHITE);
        boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
        if (toolbar != null) toolbar.setResultAvailable(hasData);
    }

    private void onExportSuccess(String type) {
        JOptionPane.showMessageDialog(this, type + " exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        statusLabel.setText("Export " + type + " terminé");
        statusLabel.setBackground(UIConstants.SUCCESS_GREEN);
        statusLabel.setForeground(java.awt.Color.WHITE);
        boolean hasData = lastResult != null && lastResult.monthly != null && !lastResult.monthly.isEmpty();
        if (toolbar != null) toolbar.setResultAvailable(hasData);
    }

    private Map<String, String> collectCommonInputs() {
        Map<String, String> inputs = new LinkedHashMap<>();
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
        return inputs;
    }

    private FinancialResult buildFinancialResultIfAny() {
        if (financialAnnees.isEmpty()) return null;
        return new FinancialResult(
                new ArrayList<>(financialAnnees),
                new ArrayList<>(financialCashFlowCumule),
                new ArrayList<>(financialAnneesRD),
                new ArrayList<>(financialRecettes),
                new ArrayList<>(financialDepenses),
                new ArrayList<>(financialVAN),
                financialVANTotal
        );
    }

    private void ouvrirFormulaireFinancier() {
        JDialog dialog = new JDialog((java.awt.Frame) null, "Entrées financières", true);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
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
            tracerGraphesFinanciers(lastInvestissement, lastSubvention, lastPrixVente, lastTauxInjection,
                    lastCoutAnnuel, lastDuree, lastTauxActualisation, lastAnneeDepart);
        });
        JPanel bottom = new JPanel();
        bottom.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        bottom.add(tracerButton);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(bottom, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void tracerGraphesFinanciers(String investissement, String subvention, String prixVente, String tauxInjection,
                                         String coutAnnuel, String duree, String tauxActualisation, String anneeDepart) {
        graphsPanel.removeAll();
        double investissementInitial = parseDoubleSafe(investissement);
        double subventionVal = parseDoubleSafe(subvention);
        double prixVenteVal = parseDoubleSafe(prixVente);
        double tauxInjectionVal = parseDoubleSafe(tauxInjection);
        double coutAnnuelVal = parseDoubleSafe(coutAnnuel);
        int dureeVal = (int) parseDoubleSafe(duree);
        double tauxActualisationVal = parseDoubleSafe(tauxActualisation) / 100.0;
        int anneeDepartVal = (int) parseDoubleSafe(anneeDepart);

        PVGISResult pvr = buildPVGISResultFromGridJson();
        FinancialParams params = new FinancialParams(
                investissementInitial, subventionVal, prixVenteVal,
                tauxInjectionVal, coutAnnuelVal, dureeVal,
                tauxActualisationVal, anneeDepartVal
        );
        FinancialCalculator calc = new FinancialCalculator();
        FinancialResult fr = calc.compute(pvr, params);

        List<CategoryChart> financialCharts = new ArrayList<>();
        financialCharts.add(ChartsFactory.createCashFlowChart(fr.annees, fr.cashFlowCumule));
        financialCharts.add(ChartsFactory.createRevenueExpenseChart(fr.anneesRD, fr.recettes, fr.depenses));
        financialCharts.add(ChartsFactory.createVANChart(fr.anneesRD, fr.van));
        graphsPanel.setCharts(financialCharts);

        graphesFinanciersImages.clear();
        for (CategoryChart ch : financialCharts) {
            try { graphesFinanciersImages.add(BitmapEncoder.getBufferedImage(ch)); } catch (RuntimeException ignore) { }
        }

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

    private PVGISResult buildPVGISResultFromGridJson() { return lastResult; }
    private static double parseDoubleSafe(String s) { try { return Double.parseDouble(s == null || s.isBlank() ? "0" : s.trim()); } catch (NumberFormatException e) { return 0.0; } }

    private void afficherGraphes() {
        graphsPanel.removeAll();
        if (lastResult == null || lastResult.monthly == null || lastResult.monthly.isEmpty()) {
            graphsPanel.add(new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production."));
            graphsPanel.revalidate();
            graphsPanel.repaint();
            return;
        }
        try {
            PVGISResult pvr = lastResult;
            if (pvr.monthly.isEmpty()) {
                graphsPanel.add(new JLabel("Aucune donnée mensuelle trouvée dans la réponse JSON."));
                graphsPanel.revalidate();
                graphsPanel.repaint();
                return;
            }
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            List<String> mois = new ArrayList<>();
            List<Double> prod = new ArrayList<>();
            List<Double> irradiation = (pvr.irradiationKWhPerMonth != null) ? pvr.irradiationKWhPerMonth : List.of();
            for (MonthlyResult m : pvr.monthly) {
                int idxMois = m.month - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.month);
                mois.add(nomMois);
                prod.add(m.E_d / 1000.0 * switch (m.month) { case 4,6,9,11 -> 30.0; case 2 -> 28.0; default -> 31.0; });
            }
            List<CategoryChart> charts = new ArrayList<>();
            charts.add(ChartsFactory.createMonthlyProductionKWhChart(mois, prod));
            if (!irradiation.isEmpty() && irradiation.size() == mois.size()) {
                charts.add(ChartsFactory.createIrradiationChart(mois, irradiation));
            } else {
                graphsPanel.add(new JLabel("Champ 'H(i)_m' (irradiation) absent ou incomplet dans la réponse JSON PVGIS."));
            }
            graphsPanel.setCharts(charts);
            graphsPanel.revalidate();
            graphsPanel.repaint();
        } catch (RuntimeException ex) {
            graphsPanel.add(new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage()));
            graphsPanel.revalidate();
            graphsPanel.repaint();
        }
    }
}
