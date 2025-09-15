package vue.EstimationProd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import controleur.EstimationProd.TrackerController;
import service.export.ExportContext;
import modele.finance.FinancialResult;
import modele.pvgis.MonthlyResult;
import modele.pvgis.PVGridAndTrackerRequest;
import modele.pvgis.PVGISModel;
import modele.pvgis.PVGISResult;
import service.pvgis.PVGISService;
import vue.ui.ButtonStyleUtil;
import vue.ui.TitleBanner;
import vue.ui.ToolbarPanel;
import vue.ui.UIConstants;
import vue.ui.GraphsPanel;
import vue.util.StatusBarUtil;
import vue.util.FinancialCharts;

/**
 * Page estimation PVGIS Tracker (restaurée propre) dans package EstimationProd.
 */
public class PageEstimationPVGISTracker extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(PageEstimationPVGISTracker.class.getName());

    private JTextField latField, lonField, peakPowerField, aspectField, angleField,
        lossField, outputFormatField, userHorizonField;
    private JComboBox<String> radDatabaseCombo, pvTechChoiceCombo, mountingPlaceCombo;
    private JCheckBox useHorizonCheck, optimalInclinationCheck, optimalAnglesCheck, browserCheck,
        fixedCheck, inclinedAxisCheck, inclinedOptimumCheck, verticalAxisCheck, verticalOptimumCheck, twoAxisCheck;
    private JTextField inclinedAxisAngleField, verticalAxisAngleField, pvPriceField, systemCostField, interestField, lifetimeField;
    private JLabel statusLabel; private ToolbarPanel toolbar; private PVGISResult lastResult; private GraphsPanel graphsPanel;

    private final TrackerController controller = new TrackerController(new PVGISService());
    private final PVGISModel model = new PVGISModel(); // reserved for future status bindings

    // Finance cache
    private final FinancialCharts.Cache finCache = new FinancialCharts.Cache();
    private final List<String> financialAnnees = new ArrayList<>();
    private final List<Double> financialCashFlowCumule = new ArrayList<>();
    private final List<String> financialAnneesRD = new ArrayList<>();
    private final List<Double> financialRecettes = new ArrayList<>();
    private final List<Double> financialDepenses = new ArrayList<>();
    private final List<Double> financialVAN = new ArrayList<>();
    private double financialVANTotal = 0.0;
    private final List<java.awt.image.BufferedImage> graphesFinanciersImages = new ArrayList<>();
    private String lastInvestissement="0", lastSubvention="0", lastPrixVente="0", lastTauxInjection="0", lastCoutAnnuel="0", lastDuree="0", lastTauxActualisation="0", lastAnneeDepart="0";

    public PageEstimationPVGISTracker(){ setLayout(new BorderLayout()); add(new TitleBanner("ESTIMATION DE LA PRODUCTION"), BorderLayout.NORTH); JPanel inputPanel = buildInputPanel(); toolbar = buildToolbar(); JPanel leftPanel = buildLeftPanel(inputPanel, toolbar); add(buildSplit(leftPanel), BorderLayout.CENTER); statusLabel = buildStatusBar(); }

    private ToolbarPanel buildToolbar(){ ToolbarPanel t = new ToolbarPanel(); t.onGraphs(e->afficherGraphes()); t.onExportCsv(e->exporterResultatsEnCSV()); t.onExportPdf(e->exporterResultatsEnPDF()); t.onFinance(e->ouvrirFormulaireFinancier()); t.setResultAvailable(false); t.showFinanceButton(false); return t; }

    private JPanel buildInputPanel(){
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // Champs (valeurs par défaut alignées sur la version Grid)
        latField = new JTextField("48.989");
        lonField = new JTextField("2.277");
        peakPowerField = new JTextField("6"); // kW comme Grid
        lossField = new JTextField("14");
    radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3","PVGIS-ERA5"});
    pvTechChoiceCombo = new JComboBox<>(new String[]{"crystSi","CIS","CdTe","amorphous"});
    mountingPlaceCombo = new JComboBox<>(new String[]{"free","building"});
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

        // Redimensionnement similaire Grid
        resizeField(latField, 50); resizeField(lonField, 50);

        int row = 0;
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
        ButtonStyleUtil.applyActionButtonStyle(estimateButton, UIConstants.ACTION_GREEN, Color.WHITE, UIConstants.ACTION_GREEN_DARK, UIConstants.PAD_PRIMARY);
        estimateButton.addActionListener(e -> estimerProduction());
        JPanel estimatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        estimatePanel.add(estimateButton);
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 0; gbcBtn.gridy = row; gbcBtn.gridwidth = 4; gbcBtn.insets = new java.awt.Insets(6,2,6,2); gbcBtn.anchor = GridBagConstraints.CENTER;
        panel.add(estimatePanel, gbcBtn);
        return panel;
    }

    // --- Helpers repris de la version Grid ---
    private void resizeField(JComponent comp, int width) {
        Dimension ps = comp.getPreferredSize();
        comp.setPreferredSize(new Dimension(width, ps.height));
        comp.setMinimumSize(new Dimension(width, ps.height));
        comp.setMaximumSize(new Dimension(width, ps.height));
    }

    private int addRowTwoColumns(JPanel panel, int row, String leftLabel, java.awt.Component leftField, String rightLabel, java.awt.Component rightField) {
        if (leftLabel != null) {
            JLabel lblL = lab(leftLabel);
            lblL.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints cL = new GridBagConstraints();
            cL.gridx = 0; cL.gridy = row; cL.anchor = GridBagConstraints.BASELINE_LEADING; cL.insets = new java.awt.Insets(4,2,4,2);
            panel.add(lblL, cL);
        }
        if (leftField != null) {
            sizeComponent(leftField);
            GridBagConstraints cLF = new GridBagConstraints();
            cLF.gridx = 1; cLF.gridy = row; cLF.anchor = GridBagConstraints.BASELINE_LEADING; cLF.insets = new java.awt.Insets(4,2,4,8);
            panel.add(leftField, cLF);
        }
        if (rightLabel != null) {
            JLabel lblR = lab(rightLabel);
            lblR.setHorizontalAlignment(SwingConstants.LEFT);
            GridBagConstraints cR = new GridBagConstraints();
            cR.gridx = 2; cR.gridy = row; cR.anchor = GridBagConstraints.BASELINE_LEADING; cR.insets = new java.awt.Insets(4,2,4,2);
            panel.add(lblR, cR);
        }
        if (rightField != null) {
            sizeComponent(rightField);
            GridBagConstraints cRF = new GridBagConstraints();
            cRF.gridx = 3; cRF.gridy = row; cRF.anchor = GridBagConstraints.BASELINE_LEADING; cRF.insets = new java.awt.Insets(4,2,4,2);
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

    // (pas de shrinkCombo: largeur harmonisée via sizeComponent comme dans la page Grid)

    private static JLabel lab(String text) {
        JLabel l = new JLabel(text);
        l.setVerticalAlignment(SwingConstants.CENTER);
        return l;
    }
    private JPanel buildLeftPanel(JPanel inputPanel, JPanel buttonPanel){ JPanel left=new JPanel(new BorderLayout()); JPanel header=new JPanel(new BorderLayout()); header.setBackground(UIConstants.GREY_BG); JLabel hl=new JLabel("Système PV Tracker"); hl.setHorizontalAlignment(SwingConstants.CENTER); hl.setForeground(Color.BLACK); hl.setBorder(BorderFactory.createEmptyBorder(6,8,6,8)); header.add(hl,BorderLayout.CENTER); left.add(header,BorderLayout.NORTH); left.add(new JScrollPane(inputPanel),BorderLayout.CENTER); left.add(buttonPanel,BorderLayout.SOUTH); return left; }
    private JSplitPane buildSplit(JPanel leftPanel){ graphsPanel=new GraphsPanel(); JScrollPane sc=new JScrollPane(graphsPanel); sc.setPreferredSize(new Dimension(900,700)); JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPanel,sc); split.setResizeWeight(0.32); split.setDividerLocation(0.32); split.setOneTouchExpandable(true); return split; }
    private JLabel buildStatusBar(){ JLabel s=new JLabel("En attente d'une estimation."); s.setOpaque(true); s.setBackground(UIConstants.STATUS_GREY); s.setForeground(Color.BLACK); s.setHorizontalAlignment(SwingConstants.CENTER); s.setPreferredSize(new Dimension(400,30)); JPanel sp=new JPanel(new FlowLayout(FlowLayout.LEFT)); sp.add(s); add(sp,BorderLayout.SOUTH); return s; }

    private void estimerProduction(){ StatusBarUtil.setStatusWaiting(statusLabel); if (toolbar!=null) toolbar.setActionsEnabled(false); PVGridAndTrackerRequest req = new PVGridAndTrackerRequest(latField.getText(), lonField.getText(), peakPowerField.getText(), lossField.getText(), (String)radDatabaseCombo.getSelectedItem(), (String)pvTechChoiceCombo.getSelectedItem(), (String)mountingPlaceCombo.getSelectedItem(), fixedCheck.isSelected(), angleField.getText(), aspectField.getText(), optimalInclinationCheck.isSelected(), optimalAnglesCheck.isSelected(), inclinedAxisCheck.isSelected(), inclinedOptimumCheck.isSelected(), inclinedAxisAngleField.getText(), verticalAxisCheck.isSelected(), verticalOptimumCheck.isSelected(), verticalAxisAngleField.getText(), twoAxisCheck.isSelected(), pvPriceField.getText(), systemCostField.getText(), interestField.getText(), lifetimeField.getText(), useHorizonCheck.isSelected(), userHorizonField.getText(), outputFormatField.getText(), browserCheck.isSelected(), true); SwingWorker<PVGISResult,Void> w = controller.createEstimateWorker(req, res->{ lastResult=res; StatusBarUtil.setStatusSuccess(statusLabel,"Succès : données reçues"); if (toolbar!=null) toolbar.showFinanceButton(true); boolean hasData= lastResult!=null && lastResult.monthly!=null && !lastResult.monthly.isEmpty(); if (toolbar!=null) toolbar.setResultAvailable(hasData); }, ex->{ StatusBarUtil.setStatusError(statusLabel,"Erreur : "+ex.getMessage()); if (toolbar!=null) toolbar.setActionsEnabled(false); }); w.execute(); }

    private void exporterResultatsEnCSV(){ if (lastResult==null){ JOptionPane.showMessageDialog(this,"Aucun résultat à exporter. Veuillez d'abord estimer la production.","Erreur",JOptionPane.ERROR_MESSAGE); return; } JFileChooser fc=new JFileChooser(); fc.setDialogTitle("Enregistrer le CSV"); if (fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) return; java.io.File chosen=fc.getSelectedFile(); if (chosen==null) return; java.io.File csvFile = chosen.getName().toLowerCase().endsWith(".csv")? chosen : new java.io.File(chosen.getAbsolutePath()+".csv"); StatusBarUtil.setStatusExporting(statusLabel,"Export CSV en cours..."); toolbar.setActionsEnabled(false); java.util.function.Supplier<ExportContext> supplier=this::buildExportContext; SwingWorker<Void,Void> worker = controller.createCsvExportWorker(csvFile, supplier, ex->{ JOptionPane.showMessageDialog(PageEstimationPVGISTracker.this,"Erreur export CSV : "+ex.getMessage(),"Erreur",JOptionPane.ERROR_MESSAGE); StatusBarUtil.setStatusError(statusLabel,"Erreur export CSV"); boolean hasData= lastResult!=null && lastResult.monthly!=null && !lastResult.monthly.isEmpty(); toolbar.setResultAvailable(hasData); }, ()->{ JOptionPane.showMessageDialog(PageEstimationPVGISTracker.this,"CSV exporté avec succès !","Succès",JOptionPane.INFORMATION_MESSAGE); StatusBarUtil.setStatusSuccess(statusLabel,"Export CSV terminé"); boolean hasData= lastResult!=null && lastResult.monthly!=null && !lastResult.monthly.isEmpty(); toolbar.setResultAvailable(hasData); }); worker.execute(); }
    private void exporterResultatsEnPDF(){ if (lastResult==null){ JOptionPane.showMessageDialog(this,"Aucun résultat à exporter. Veuillez d'abord estimer la production.","Erreur",JOptionPane.ERROR_MESSAGE); return; } JFileChooser fc=new JFileChooser(); fc.setDialogTitle("Enregistrer le PDF"); if (fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION) return; java.io.File chosen=fc.getSelectedFile(); if (chosen==null) return; java.io.File pdfFile = chosen.getName().toLowerCase().endsWith(".pdf")? chosen : new java.io.File(chosen.getAbsolutePath()+".pdf"); StatusBarUtil.setStatusExporting(statusLabel,"Export PDF en cours..."); toolbar.setActionsEnabled(false); java.util.function.Supplier<ExportContext> supplier=this::buildExportContext; SwingWorker<Void,Void> worker = controller.createPdfExportWorker(pdfFile, supplier, ()-> tracerGraphesFinanciers(lastInvestissement,lastSubvention,lastPrixVente,lastTauxInjection,lastCoutAnnuel,lastDuree,lastTauxActualisation,lastAnneeDepart), ex->{ JOptionPane.showMessageDialog(PageEstimationPVGISTracker.this,"Erreur export PDF : "+ex.getMessage(),"Erreur",JOptionPane.ERROR_MESSAGE); StatusBarUtil.setStatusError(statusLabel,"Erreur export PDF"); boolean hasData= lastResult!=null && lastResult.monthly!=null && !lastResult.monthly.isEmpty(); toolbar.setResultAvailable(hasData); }, ()->{ JOptionPane.showMessageDialog(PageEstimationPVGISTracker.this,"PDF exporté avec succès !","Succès",JOptionPane.INFORMATION_MESSAGE); StatusBarUtil.setStatusSuccess(statusLabel,"Export PDF terminé"); boolean hasData= lastResult!=null && lastResult.monthly!=null && !lastResult.monthly.isEmpty(); toolbar.setResultAvailable(hasData); }); worker.execute(); }

    private ExportContext buildExportContext(){ Map<String,String> inputs=new LinkedHashMap<>(); inputs.put("Latitude",latField.getText()); inputs.put("Longitude",lonField.getText()); inputs.put("Puissance PV crête (W)",peakPowerField.getText()); inputs.put("Inclinaison (°)",angleField.getText()); inputs.put("Azimut (°)",aspectField.getText()); inputs.put("Pertes totales (%)",lossField.getText()); inputs.put("Base de données",String.valueOf(radDatabaseCombo.getSelectedItem())); inputs.put("Technologie PV",String.valueOf(pvTechChoiceCombo.getSelectedItem())); inputs.put("Type de montage",String.valueOf(mountingPlaceCombo.getSelectedItem())); inputs.put("Fixe", fixedCheck.isSelected()?"Oui":"Non"); inputs.put("Inclinaison optimale",optimalInclinationCheck.isSelected()?"Oui":"Non"); inputs.put("Angles optimaux",optimalAnglesCheck.isSelected()?"Oui":"Non"); inputs.put("Axe incliné",inclinedAxisCheck.isSelected()?"Oui":"Non"); inputs.put("Inclinaison opt. axe incliné",inclinedOptimumCheck.isSelected()?"Oui":"Non"); inputs.put("Angle axe incliné",inclinedAxisAngleField.getText()); inputs.put("Axe vertical",verticalAxisCheck.isSelected()?"Oui":"Non"); inputs.put("Inclinaison opt. axe vertical",verticalOptimumCheck.isSelected()?"Oui":"Non"); inputs.put("Angle axe vertical",verticalAxisAngleField.getText()); inputs.put("Double axe",twoAxisCheck.isSelected()?"Oui":"Non"); inputs.put("Prix PV",pvPriceField.getText()); inputs.put("Coût système",systemCostField.getText()); inputs.put("Intérêt",interestField.getText()); inputs.put("Durée vie",lifetimeField.getText()); inputs.put("Inclure horizon",useHorizonCheck.isSelected()?"Oui":"Non"); inputs.put("Horizon utilisateur",userHorizonField.getText()); inputs.put("Format sortie",outputFormatField.getText()); inputs.put("Browser",browserCheck.isSelected()?"Oui":"Non"); FinancialResult fr=null; if(!financialAnnees.isEmpty()){ fr=new FinancialResult(financialAnnees,financialCashFlowCumule,financialAnneesRD,financialRecettes,financialDepenses,financialVAN,financialVANTotal);} return new ExportContext(inputs,lastResult,fr,new ArrayList<>(graphesFinanciersImages),"Estimation PVGIS Tracker"); }

    private void tracerGraphesFinanciers(String investissement,String subvention,String prixVente,String tauxInjection,String coutAnnuel,String duree,String tauxActualisation,String anneeDepart){ lastInvestissement=investissement; lastSubvention=subvention; lastPrixVente=prixVente; lastTauxInjection=tauxInjection; lastCoutAnnuel=coutAnnuel; lastDuree=duree; lastTauxActualisation=tauxActualisation; lastAnneeDepart=anneeDepart; double investissementInitial=Double.parseDouble(investissement); double subventionVal=Double.parseDouble(subvention); double prixVenteVal=Double.parseDouble(prixVente); double tauxInjectionVal=Double.parseDouble(tauxInjection); double coutAnnuelVal=Double.parseDouble(coutAnnuel); int dureeVal=Integer.parseInt(duree); double tauxActVal=Double.parseDouble(tauxActualisation)/100.0; int anneeDepartVal=Integer.parseInt(anneeDepart); modele.finance.FinancialParams params=new modele.finance.FinancialParams(investissementInitial,subventionVal,prixVenteVal,tauxInjectionVal,coutAnnuelVal,dureeVal,tauxActVal,anneeDepartVal); service.finance.FinancialCalculator calc=new service.finance.FinancialCalculator(); modele.finance.FinancialResult fr=calc.compute(lastResult,params); FinancialCharts.renderIntoPanel(fr,graphsPanel,finCache); financialAnnees.clear(); financialAnnees.addAll(finCache.annees); financialCashFlowCumule.clear(); financialCashFlowCumule.addAll(finCache.cashFlowCumule); financialAnneesRD.clear(); financialAnneesRD.addAll(finCache.anneesRD); financialRecettes.clear(); financialRecettes.addAll(finCache.recettes); financialDepenses.clear(); financialDepenses.addAll(finCache.depenses); financialVAN.clear(); financialVAN.addAll(finCache.van); financialVANTotal=finCache.vanTotale; graphesFinanciersImages.clear(); graphesFinanciersImages.addAll(finCache.images); }

    private void ouvrirFormulaireFinancier(){ JDialog dialog=new JDialog((Frame)null,"Entrées financières",true); JPanel panel=new JPanel(new GridLayout(0,2,5,5)); panel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12)); JTextField investissementField=new JTextField("600"); JTextField subventionField=new JTextField("200"); JTextField prixVenteField=new JTextField("0.18"); JTextField tauxInjectionField=new JTextField("0.6"); JTextField coutAnnuelField=new JTextField("40"); JTextField dureeField=new JTextField("20"); JTextField tauxActualisationField=new JTextField("0.3"); JTextField anneeDepartField=new JTextField("2025"); panel.add(new JLabel("Investissement initial (€) :")); panel.add(investissementField); panel.add(new JLabel("Subvention (€) :")); panel.add(subventionField); panel.add(new JLabel("Prix de vente (€/kWh) :")); panel.add(prixVenteField); panel.add(new JLabel("Taux d'injection/autoconsommation (0-1) :")); panel.add(tauxInjectionField); panel.add(new JLabel("Coût annuel d'exploitation (€) :")); panel.add(coutAnnuelField); panel.add(new JLabel("Durée du projet (années) :")); panel.add(dureeField); panel.add(new JLabel("Taux d'actualisation (%) :")); panel.add(tauxActualisationField); panel.add(new JLabel("Année de départ :")); panel.add(anneeDepartField); JButton tracerButton=new JButton("Tracer les graphes"); tracerButton.addActionListener(e->{ lastInvestissement=investissementField.getText(); lastSubvention=subventionField.getText(); lastPrixVente=prixVenteField.getText(); lastTauxInjection=tauxInjectionField.getText(); lastCoutAnnuel=coutAnnuelField.getText(); lastDuree=dureeField.getText(); lastTauxActualisation=tauxActualisationField.getText(); lastAnneeDepart=anneeDepartField.getText(); dialog.dispose(); tracerGraphesFinanciers(lastInvestissement,lastSubvention,lastPrixVente,lastTauxInjection,lastCoutAnnuel,lastDuree,lastTauxActualisation,lastAnneeDepart); }); JPanel bottom=new JPanel(); bottom.setBorder(BorderFactory.createEmptyBorder(6,12,12,12)); bottom.add(tracerButton); dialog.getContentPane().add(panel,BorderLayout.CENTER); dialog.getContentPane().add(bottom,BorderLayout.SOUTH); dialog.pack(); dialog.setLocationRelativeTo(this); dialog.setVisible(true); }

    private void afficherGraphes(){
        graphsPanel.removeAll();
        if (lastResult == null || lastResult.monthly == null || lastResult.monthly.isEmpty()) {
            graphsPanel.add(new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production."));
            graphsPanel.revalidate(); graphsPanel.repaint(); return; }
        try {
            PVGISResult pvr = lastResult;
            if (pvr.monthly.isEmpty()) {
                graphsPanel.add(new JLabel("Aucune donnée mensuelle trouvée dans la réponse JSON."));
                graphsPanel.revalidate(); graphsPanel.repaint(); return; }
            String[] moisFrancais = {"Jan","Fév","Mars","Avril","Mai","Juin","Juil","Août","Sep","Oct","Nov","Déc"};
            List<String> mois = new ArrayList<>();
            List<Double> prodKWh = new ArrayList<>(); // production en kWh/mois comme la page Grid
            List<Double> irradiation = (pvr.irradiationKWhPerMonth != null) ? pvr.irradiationKWhPerMonth : List.of();
            for (MonthlyResult m : pvr.monthly) {
                int idxMois = m.month - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.month);
                mois.add(nomMois);
                // Conversion: E_d (Wh/jour) → kWh/mois (comme Grid): (E_d /1000) * nbJours
                double days = switch (m.month) { case 4,6,9,11 -> 30.0; case 2 -> 28.0; default -> 31.0; };
                prodKWh.add((m.E_d / 1000.0) * days);
            }
            List<org.knowm.xchart.CategoryChart> charts = new ArrayList<>();
            charts.add(vue.util.ChartsFactory.createMonthlyProductionKWhChart(mois, prodKWh));
            if (!irradiation.isEmpty() && irradiation.size() == mois.size()) {
                charts.add(vue.util.ChartsFactory.createIrradiationChart(mois, irradiation));
            } else {
                graphsPanel.add(new JLabel("Champ 'H(i)_m' (irradiation) absent ou incomplet dans la réponse JSON PVGIS."));
            }
            graphsPanel.setCharts(charts);
            graphsPanel.revalidate(); graphsPanel.repaint();
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Erreur lors du tracé des graphes", ex);
            graphsPanel.add(new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage()));
            graphsPanel.revalidate(); graphsPanel.repaint();
        }
    }
}
