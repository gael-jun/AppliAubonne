package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import modele.HistogramBucket;
import modele.MonthlyResult;
import modele.PVGISRequest;
import modele.PVGISResult;
import service.PVGISService;
import vue.ui.ButtonStyleUtil;
import export.*;
import modele.FinancialResult;
import vue.ui.offgrid.OffGridPresenter;
import vue.ui.offgrid.OffGridView;
import modele.PVGISModel;
import vue.ui.GraphsPanel;

/**
 * Page d'estimation PVGIS pour systèmes photovoltaïques hors réseau (off-grid).
 * Fournit un formulaire ergonomique, l'appel à l'API PVGIS et l'affichage graphique des résultats.
 */
public class PageEstimationPVGISOffGrid extends JPanel implements OffGridView {
    private static final Logger LOGGER = Logger.getLogger(PageEstimationPVGISOffGrid.class.getName());
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
        // Graphs are displayed via GraphsPanel (see graphsPanel)
    // Label de statut pour l'utilisateur (attente, succès, erreur).
    private final JLabel statusLabel;
    // Bouton pour ouvrir le formulaire financier (visible seulement après réception des données PVGIS)
    private javax.swing.JButton financeButton;
    // Boutons d'action accessibles depuis la toolbar
    private javax.swing.JButton graphButton;
    private javax.swing.JButton exportPdfButton;
    private javax.swing.JButton exportCsvButton;
    // Export menu (regroupe les options d'export CSV/PDF)
    private javax.swing.JButton exportMenuButton;
    private javax.swing.JMenuItem exportPdfMenuItem;
    private javax.swing.JMenuItem exportCsvMenuItem;
    // Contient le dernier résultat typé reçu de l'API PVGIS.
    private PVGISResult lastResult = null;
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

    // Résultats financiers calculés (conservés pour l'export PDF)
    private java.util.List<String> financialAnnees = new java.util.ArrayList<>();
    private java.util.List<Double> financialCashFlowCumule = new java.util.ArrayList<>();
    private java.util.List<String> financialAnneesRD = new java.util.ArrayList<>();
    private java.util.List<Double> financialRecettes = new java.util.ArrayList<>();
    private java.util.List<Double> financialDepenses = new java.util.ArrayList<>();
    private java.util.List<Double> financialVAN = new java.util.ArrayList<>();
    private double financialVANTotal = 0.0;
    // MVP presenter (kept for backward-compat during refactor) and new controller/model
    private final OffGridPresenter presenter = new OffGridPresenter(this, new PVGISService());
    private final PVGISModel model = new PVGISModel();
    private final GraphsPanel graphsPanel = new GraphsPanel();
    // ToolbarPanel was removed to keep original UI appearance

    /**
     * Constructeur : initialise la page d'estimation avec le formulaire et les zones graphiques.
     */
    public PageEstimationPVGISOffGrid() {
        // Définit le layout principal du panneau en BorderLayout
        setLayout(new BorderLayout());
    // Crée un panneau d'entrée avec un GridLayout 2 colonnes (espacements réduits)
    // Réduit l'espace horizontal entre les deux colonnes à 0 et vertical à 0 pour coller les champs.
    JPanel inputPanel = new JPanel(new GridLayout(0, 2, 0, 0));
    // Ajoute une marge en haut et à gauche pour éviter que le formulaire soit collé au bord
    inputPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 0, 0));

        // --- Champs obligatoires avec valeurs par défaut ---
    // Taille cible des champs de saisie (limite la largeur pour éviter qu'ils s'étendent)
    int inputWidth = 120;
    latField = new JTextField("48.989"); // valeur par défaut latitude
    latField.setPreferredSize(new Dimension(inputWidth, 18));
    latField.setMaximumSize(new Dimension(inputWidth, 18));
    latField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    lonField = new JTextField("2.277"); // valeur par défaut longitude
    lonField.setPreferredSize(new Dimension(inputWidth, 24));
    lonField.setMaximumSize(new Dimension(inputWidth, 24));
    lonField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    peakPowerField = new JTextField("6000"); // puissance PV par défaut en W
    peakPowerField.setPreferredSize(new Dimension(inputWidth, 24));
    peakPowerField.setMaximumSize(new Dimension(inputWidth, 24));
    peakPowerField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    batterySizeField = new JTextField("10000"); // capacité batterie par défaut en Wh
    batterySizeField.setPreferredSize(new Dimension(inputWidth, 24));
    batterySizeField.setMaximumSize(new Dimension(inputWidth, 24));
    batterySizeField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    cutoffField = new JTextField("20"); // seuil de décharge par défaut en %
    cutoffField.setPreferredSize(new Dimension(inputWidth, 24));
    cutoffField.setMaximumSize(new Dimension(inputWidth, 24));
    cutoffField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    consumptionDayField = new JTextField("2000"); // consommation journalière par défaut en Wh
    consumptionDayField.setPreferredSize(new Dimension(inputWidth, 24));
    consumptionDayField.setMaximumSize(new Dimension(inputWidth, 24));
    consumptionDayField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        // --- Champs facultatifs et options ---
        useHorizonCheck = new JCheckBox(); // inclure l'horizon naturel
        useHorizonCheck.setSelected(true); // activé par défaut
        userHorizonField = new JTextField(""); // horizon utilisateur vide par défaut
    userHorizonField.setPreferredSize(new Dimension(inputWidth, 24));
    userHorizonField.setMaximumSize(new Dimension(inputWidth, 24));
    userHorizonField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        radDatabaseCombo = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"}); // choix raddatabase
        radDatabaseCombo.setSelectedIndex(0);
        radDatabaseCombo.setPreferredSize(new Dimension(inputWidth, 24));
        radDatabaseCombo.setMaximumSize(new Dimension(inputWidth, 24));
        radDatabaseCombo.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        angleField = new JTextField("0"); // inclinaison par défaut
    angleField.setPreferredSize(new Dimension(inputWidth, 24));
    angleField.setMaximumSize(new Dimension(inputWidth, 24));
    angleField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        aspectField = new JTextField("0"); // azimut par défaut
    aspectField.setPreferredSize(new Dimension(inputWidth, 24));
    aspectField.setMaximumSize(new Dimension(inputWidth, 24));
    aspectField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        hourConsumptionField = new JTextField(""); // profil horaire vide
    hourConsumptionField.setPreferredSize(new Dimension(inputWidth, 24));
    hourConsumptionField.setMaximumSize(new Dimension(inputWidth, 24));
    hourConsumptionField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        outputFormatField = new JTextField("json"); // format de sortie par défaut
    outputFormatField.setPreferredSize(new Dimension(inputWidth, 24));
    outputFormatField.setMaximumSize(new Dimension(inputWidth, 24));
    outputFormatField.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        browserCheck = new JCheckBox(); // option browser
        browserCheck.setSelected(false);

    // Ajout des labels et champs au panneau d'entrée (UI) — labels au-dessus des champs
    JPanel row;
    JLabel lbl;

    lbl = new JLabel("Latitude :*");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(latField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Longitude :*");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(lonField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Base de données de radiation :");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(radDatabaseCombo); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Puissance PV crête (W) :*");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(peakPowerField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Inclinaison (°) :");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(angleField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Azimut (°) :");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(aspectField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Capacité batterie (Wh) :*");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(batterySizeField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Limite de décharge (%) :*");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(cutoffField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Consommation par jour (Wh) :*");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(consumptionDayField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("<html>Profil horaire de consommation<br/>(24 valeurs, séparées par des virgules) :</html>");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(hourConsumptionField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Inclure l'horizon :");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(useHorizonCheck); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("<html>Horizon utilisateur<br/>(8 valeurs, séparées par des virgules) :</html>");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(userHorizonField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Format de sortie :");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(outputFormatField); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);

    lbl = new JLabel("Browser :");
    row = new JPanel(); row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS)); row.add(lbl); row.add(browserCheck); row.setBorder(javax.swing.BorderFactory.createEmptyBorder(1,0,1,0)); row.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT); inputPanel.add(row);
    // --- Boutons d'action ---
    // Crée d'abord le bouton Estimer (déclaré avant d'être ajouté au formulaire)
    JButton estimateButton = new JButton("Estimer la production");
    // Icônes locales depuis UIManager (fallback si null)
    javax.swing.Icon estIcon = javax.swing.UIManager.getIcon("FileView.computerIcon");
    if (estIcon != null) estimateButton.setIcon(estIcon);
    estimateButton.setToolTipText("Lancer l'estimation avec les paramètres saisis (Alt+E)");
    // Applique un style réutilisable au bouton Estimer
    ButtonStyleUtil.applyActionButtonStyle(estimateButton, new java.awt.Color(76, 175, 80), java.awt.Color.WHITE, new java.awt.Color(34, 139, 34), new java.awt.Insets(6, 12, 6, 12));
    // Lance l'estimation lorsque l'utilisateur clique
    estimateButton.addActionListener((ActionEvent e) -> estimerProduction());

    // Place le bouton Estimer à l'intérieur du formulaire pour qu'il défile avec les champs
    JPanel estimatePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    estimatePanel.add(estimateButton);
    // Pour respecter le GridLayout 2-colonnes, on ajoute un placeholder puis le panneau centré
    inputPanel.add(new JLabel(""));
    inputPanel.add(estimatePanel);

    // graph/export buttons are instance fields so we can enable/disable them from other methods
    graphButton = new JButton("Voir graphes");
    javax.swing.Icon graphIcon = javax.swing.UIManager.getIcon("FileView.directoryIcon");
    if (graphIcon != null) graphButton.setIcon(graphIcon);
    graphButton.setToolTipText("Afficher les graphiques (disponible après estimation)");
    graphButton.setEnabled(false);
    graphButton.addActionListener(e -> afficherGraphes());
    // Style réutilisable pour le bouton Voir graphes
    ButtonStyleUtil.applyActionButtonStyle(graphButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));
    // Nous remplaçons les boutons d'export par un menu "Export" pour regrouper les options
    exportPdfButton = new JButton("Exporter résultats en PDF");
    exportPdfButton.setEnabled(false);
    exportPdfButton.addActionListener(e -> new Thread(() -> exporterResultatsEnPDF()).start());

    exportCsvButton = new JButton("Exporter en CSV");
    javax.swing.Icon csvIcon = javax.swing.UIManager.getIcon("FileView.fileIcon");
    if (csvIcon != null) exportCsvButton.setIcon(csvIcon);
    exportCsvButton.setToolTipText("Exporter les résultats au format CSV (disponible après estimation)");
    exportCsvButton.setEnabled(false);
    exportCsvButton.addActionListener(e -> exporterResultatsEnCSV());

    // Création du bouton Export qui affichera un JPopupMenu
    exportMenuButton = new javax.swing.JButton("Export");
    exportMenuButton.setToolTipText("Options d'export");
    // Désactivé par défaut jusqu'à réception des données PVGIS
    exportMenuButton.setEnabled(false);
    javax.swing.Icon exportIcon = javax.swing.UIManager.getIcon("FileView.hardDriveIcon");
    if (exportIcon != null) exportMenuButton.setIcon(exportIcon);
    // Style réutilisable pour le bouton Export
    ButtonStyleUtil.applyActionButtonStyle(exportMenuButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));
    // menu déroulant
    javax.swing.JPopupMenu exportPopup = new javax.swing.JPopupMenu();
    exportPdfMenuItem = new javax.swing.JMenuItem("Exporter en PDF");
    exportPdfMenuItem.addActionListener(e -> new Thread(() -> exporterResultatsEnPDF()).start());
    exportCsvMenuItem = new javax.swing.JMenuItem("Exporter en CSV");
    exportCsvMenuItem.setEnabled(false);
    exportCsvMenuItem.addActionListener(e -> exporterResultatsEnCSV());
    exportPopup.add(exportPdfMenuItem);
    exportPopup.add(exportCsvMenuItem);
    exportMenuButton.addActionListener(e -> exportPopup.show(exportMenuButton, 0, exportMenuButton.getHeight()));
    // Initialize Finances button before using its icon
    financeButton = new JButton("Finances");
    javax.swing.Icon finIcon = javax.swing.UIManager.getIcon("OptionPane.informationIcon");
    if (finIcon != null) {
        try {
            int targetHeight = 16; // sensible default
            if (graphButton != null && graphButton.getIcon() != null && graphButton.getIcon().getIconHeight() > 0) {
                targetHeight = graphButton.getIcon().getIconHeight();
            }
            if (finIcon instanceof javax.swing.ImageIcon) {
                java.awt.Image img = ((javax.swing.ImageIcon) finIcon).getImage();
                java.awt.Image scaled = img.getScaledInstance(-1, targetHeight, java.awt.Image.SCALE_SMOOTH);
                financeButton.setIcon(new javax.swing.ImageIcon(scaled));
            } else {
                java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(finIcon.getIconWidth(), finIcon.getIconHeight(), java.awt.image.BufferedImage.TYPE_INT_ARGB);
                java.awt.Graphics g2 = bi.getGraphics();
                finIcon.paintIcon(null, g2, 0, 0);
                g2.dispose();
                java.awt.Image scaled = bi.getScaledInstance(-1, targetHeight, java.awt.Image.SCALE_SMOOTH);
                financeButton.setIcon(new javax.swing.ImageIcon(scaled));
            }
        } catch (RuntimeException ex) {
            // fallback to original icon if scaling fails
            financeButton.setIcon(finIcon);
        }
    }
    // Graph rendering now handled by GraphsPanel#setCharts
    financeButton.addActionListener(e -> ouvrirFormulaireFinancier());
    // Masque le bouton tant que nous n'avons pas encore reçu de données PVGIS
    financeButton.setVisible(false);
    // Style réutilisable pour le bouton Finances
    ButtonStyleUtil.applyActionButtonStyle(financeButton, new java.awt.Color(255, 204, 51), java.awt.Color.BLACK, new java.awt.Color(140, 100, 0), new java.awt.Insets(4, 8, 4, 8));

    // Les actions d'export sont maintenant disponibles via le menu "Export" (exportPdfMenuItem / exportCsvMenuItem)

    // Remplace la rangée de boutons par une toolbar pour meilleure hiérarchie visuelle
    javax.swing.JToolBar toolBar = new javax.swing.JToolBar();
    toolBar.setFloatable(false);
    // estimateButton est déplacé hors de la toolbar et placé à la fin du formulaire
    toolBar.add(graphButton);
    toolBar.addSeparator();
    // ajoute le bouton Export (menu) au lieu des boutons individuels
    toolBar.add(exportMenuButton);
    toolBar.addSeparator();
    toolBar.add(financeButton);
    JPanel buttonPanel = new JPanel(new BorderLayout());
    buttonPanel.add(toolBar, BorderLayout.CENTER);

    // --- Composition UI conservant l'apparence d'origine (formulaire + barre de boutons) ---
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BorderLayout());
    // En-tête local du formulaire (vert foncé sur fond vert clair)
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(new java.awt.Color(210, 235, 210)); // vert clair
    JLabel headerLabel = new JLabel("Formulaire pour PV hors réseau");
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
    headerLabel.setForeground(new java.awt.Color(0, 100, 0)); // vert foncé
    headerLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));
    headerPanel.add(headerLabel, BorderLayout.CENTER);
    leftPanel.add(headerPanel, BorderLayout.NORTH);
    leftPanel.add(new JScrollPane(inputPanel), BorderLayout.CENTER);
    leftPanel.add(buttonPanel, BorderLayout.SOUTH);
    JScrollPane graphScrollPane = new JScrollPane(graphsPanel);
    graphScrollPane.setPreferredSize(new Dimension(900, 700));
    javax.swing.JSplitPane split = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphScrollPane);
    split.setResizeWeight(0.35);
    split.setOneTouchExpandable(true);
    add(split, BorderLayout.CENTER);

    // --- Zone de statut utilisateur (barre fixe en bas) ---
    statusLabel = new JLabel("En attente d'une estimation."); // texte initial
    statusLabel.setOpaque(true); // permet d'afficher un fond coloré
    statusLabel.setBackground(new java.awt.Color(220, 220, 220)); // gris clair
    statusLabel.setForeground(java.awt.Color.BLACK);
    statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    statusLabel.setPreferredSize(new Dimension(400, 30));
    JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    statusPanel.add(statusLabel);
    add(statusPanel, BorderLayout.SOUTH);
    // Observe model changes to update UI state
        model.addPropertyChangeListener(evt -> {
            switch (evt.getPropertyName()) {
                case "status" -> {
                    modele.PVGISModel.Status st = (modele.PVGISModel.Status) evt.getNewValue();
                    switch (st) {
                        case LOADING -> { statusLabel.setText("En attente de la réponse de l'API..."); statusLabel.setBackground(new java.awt.Color(220,220,220)); statusLabel.setForeground(java.awt.Color.BLACK);}                        
                        case READY -> { statusLabel.setText("Succès : données reçues"); statusLabel.setBackground(new java.awt.Color(0,180,0)); statusLabel.setForeground(java.awt.Color.WHITE);}                        
                        case ERROR -> { statusLabel.setText("Erreur lors de la requête"); statusLabel.setBackground(new java.awt.Color(200,0,0)); statusLabel.setForeground(java.awt.Color.WHITE);}                        
                        default -> {}
                    }
                }
                case "result" -> {
                    PVGISResult r = (PVGISResult) evt.getNewValue();
                    lastResult = r;
                    if (financeButton != null) financeButton.setVisible(r != null);
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
        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setDialogTitle("Enregistrer le CSV");
            if (fileChooser.showSaveDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
            java.io.File csvFile = fileChooser.getSelectedFile();
            if (!csvFile.getName().toLowerCase().endsWith(".csv")) {
                csvFile = new java.io.File(csvFile.getAbsolutePath() + ".csv");
            }

            ExportFacade facade = new ExportFacade();
            ExportStrategy strategy = new CsvExportStrategy();
            facade.export(strategy, csvFile, buildExportContext());

            JOptionPane.showMessageDialog(this, "CSV exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (java.io.IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur export CSV", ex);
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export CSV : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Effectue l'appel à l'API PVGIS avec les paramètres du formulaire et met à jour le statut.
     */
    private void estimerProduction() {
        // Prépare la requête typée à partir des champs du formulaire
        PVGISRequest req = new PVGISRequest(
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
        // Route via le presenter (MVP)
        presenter.estimateAsync(req);
    }

    // OffGridView implementation
    @Override
    public void onEstimateStarted() {
        statusLabel.setText("En attente de la réponse de l'API...");
        statusLabel.setBackground(new java.awt.Color(220, 220, 220));
        statusLabel.setForeground(java.awt.Color.BLACK);
    }

    @Override
    public void onEstimateSuccess(PVGISResult result) {
        lastResult = result;
        statusLabel.setText("Succès : données reçues");
        statusLabel.setBackground(new java.awt.Color(0, 180, 0));
        statusLabel.setForeground(java.awt.Color.WHITE);
        if (financeButton != null) financeButton.setVisible(true);
        if (graphButton != null) graphButton.setEnabled(true);
        if (exportPdfButton != null) exportPdfButton.setEnabled(true);
        if (exportCsvButton != null) exportCsvButton.setEnabled(true);
        if (exportMenuButton != null) exportMenuButton.setEnabled(true);
        if (exportPdfMenuItem != null) exportPdfMenuItem.setEnabled(true);
        if (exportCsvMenuItem != null) exportCsvMenuItem.setEnabled(true);
    }

    @Override
    public void onEstimateError(String message, Throwable error) {
        LOGGER.log(Level.SEVERE, "Erreur lors de la requête PVGIS", error);
        statusLabel.setText("Erreur lors de la requête : " + message);
        statusLabel.setBackground(new java.awt.Color(200, 0, 0));
        statusLabel.setForeground(java.awt.Color.WHITE);
    }


    /**
     * Exporte les résultats (liste des entrées, tableau structuré, graphes) dans un PDF.
     */
    private void exporterResultatsEnPDF() {
        if (lastResult == null) {
            JOptionPane.showMessageDialog(this, "Aucun résultat à exporter. Veuillez d'abord estimer la production.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
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
            } catch (InterruptedException | java.lang.reflect.InvocationTargetException e) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Erreur lors de la sélection du fichier : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE));
                return;
            }
            if (pdfFileHolder[0] == null) return;

            ExportFacade facade = new ExportFacade();
            ExportStrategy strategy = new PdfExportStrategy();

            // Assure-toi que les graphiques financiers sont à jour avant l'export
            try {
                SwingUtilities.invokeAndWait(() -> tracerGraphesFinanciers(lastInvestissement, lastSubvention, lastPrixVente, lastTauxInjection, lastCoutAnnuel, lastDuree, lastTauxActualisation, lastAnneeDepart));
            } catch (InterruptedException | java.lang.reflect.InvocationTargetException ite) {
                LOGGER.log(Level.WARNING, "Impossible de (re)tracer automatiquement les graphes financiers", ite);
            }

            facade.export(strategy, pdfFileHolder[0], buildExportContext());

            JOptionPane.showMessageDialog(this, "PDF exporté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (java.io.IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur export PDF", ex);
            JOptionPane.showMessageDialog(this, "Erreur lors de l'export PDF : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
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
        return new ExportContext(inputs, lastResult, fr, new java.util.ArrayList<>(graphesFinanciersImages));
    }

    
    // Formulaire financier
    private void ouvrirFormulaireFinancier() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Entrées financières", true);
    JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
    // Ajoute des marges autour du formulaire financier (haut, gauche, bas, droite)
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
    // Petite marge autour du panneau bas pour espacer le bouton
    bottomPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 12, 12));
    bottomPanel.add(tracerButton);
    dialog.getContentPane().add(panel, BorderLayout.CENTER);
    dialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

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

    graphsPanel.removeAll();

        // Construit les DTO de calcul
        double investissementInitial = Double.parseDouble(investissement);
        double subventionVal = Double.parseDouble(subvention);
        double prixVenteVal = Double.parseDouble(prixVente);
        double tauxInjectionVal = Double.parseDouble(tauxInjection);
        double coutAnnuelVal = Double.parseDouble(coutAnnuel);
        int dureeVal = Integer.parseInt(duree);
        double tauxActualisationVal = Double.parseDouble(tauxActualisation) / 100.0;
        int anneeDepartVal = Integer.parseInt(anneeDepart);

        modele.FinancialParams params = new modele.FinancialParams(
                investissementInitial, subventionVal, prixVenteVal,
                tauxInjectionVal, coutAnnuelVal, dureeVal,
                tauxActualisationVal, anneeDepartVal
        );

        // Calcul via le service
        service.FinancialCalculator calc = new service.FinancialCalculator();
        modele.FinancialResult fr = calc.compute(lastResult, params);

        // Graphiques via ChartFactory
        java.util.List<org.knowm.xchart.CategoryChart> financialCharts = new java.util.ArrayList<>();
        org.knowm.xchart.CategoryChart chartCF = util.ChartFactory.createCashFlowChart(fr.annees, fr.cashFlowCumule);
    financialCharts.add(chartCF);

        org.knowm.xchart.CategoryChart chartRD = util.ChartFactory.createRevenueExpenseChart(fr.anneesRD, fr.recettes, fr.depenses);
    financialCharts.add(chartRD);

        org.knowm.xchart.CategoryChart chartVAN = util.ChartFactory.createVANChart(fr.anneesRD, fr.van);
    financialCharts.add(chartVAN);
    graphsPanel.setCharts(financialCharts);

        // Images pour PDF
        graphesFinanciersImages.clear();
        for (org.knowm.xchart.CategoryChart ch : financialCharts) {
            try {
                graphesFinanciersImages.add(org.knowm.xchart.BitmapEncoder.getBufferedImage(ch));
            } catch (RuntimeException ex) {
                LOGGER.log(Level.WARNING, "Impossible de générer l'image d'un graphe financier", ex);
            }
        }

        // Sauvegarde des tableaux de résultats financiers pour l'export PDF
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

    // updateStatus removed (unused)

    // Ajoute un graphique XChart au panneau et rafraîchit l'affichage
    private void addGraphToPanel(org.knowm.xchart.CategoryChart chart) {
        // deprecated - kept to avoid breaking references; prefer graphsPanel.setCharts
        java.util.List<org.knowm.xchart.CategoryChart> current = new java.util.ArrayList<>();
        current.add(chart);
        graphsPanel.setCharts(current);
    }

    // Méthode utilitaire de création de graphiques supprimée au profit de util.ChartFactory

    // Méthode pour afficher les graphes à partir du JSON actuel
    private void afficherGraphes() {
    graphsPanel.removeAll(); // nettoie le panneau
        if (lastResult == null) {
            JLabel label = new JLabel("Aucun résultat JSON à afficher. Veuillez d'abord estimer la production.");
            graphsPanel.add(label);
            graphsPanel.revalidate();
            graphsPanel.repaint();
            return; // rien à afficher
        }
        // Tente de parser le JSON et générer les graphiques
        try {
            // Liste des mois en français
            String[] moisFrancais = {"Jan", "Fév", "Mars", "Avril", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc"};
            java.util.List<String> mois = new java.util.ArrayList<>();
            java.util.List<Double> prod = new java.util.ArrayList<>();
            java.util.List<Double> jours = java.util.Arrays.asList(31.,28.,31.,30.,31.,30.,31.,31.,30.,31.,30.,31.);
            for (MonthlyResult m : lastResult.monthly) {
                int idxMois = m.month - 1;
                String nomMois = (idxMois >= 0 && idxMois < 12) ? moisFrancais[idxMois] : ("Mois " + m.month);
                mois.add(nomMois);
                prod.add(m.E_d * jours.get(m.month-1));
            }
            addGraphToPanel(util.ChartFactory.createProductionChart(mois, prod));

            // Graphique 2 : Énergie perdue mensuelle
            java.util.List<Double> lost = new java.util.ArrayList<>();
            for (MonthlyResult m : lastResult.monthly) {
                lost.add(m.E_lost_d * jours.get(m.month-1));
            }
            addGraphToPanel(util.ChartFactory.createLostEnergyChart(mois, lost));

            // Graphique 3 : Histogramme des états de charge
            java.util.List<String> csLabels = new java.util.ArrayList<>();
            java.util.List<Double> fcs = new java.util.ArrayList<>();
            for (HistogramBucket h : lastResult.histogram) {
                csLabels.add(h.CS_min + "-" + h.CS_max);
                fcs.add(h.f_CS);
            }
            addGraphToPanel(util.ChartFactory.createHistogramChart(csLabels, fcs));

            // Graphique 4 : % jours batterie pleine vs vide
            java.util.List<Double> ff = new java.util.ArrayList<>();
            java.util.List<Double> fe = new java.util.ArrayList<>();
            for (MonthlyResult m : lastResult.monthly) {
                ff.add(m.f_f);
                fe.add(m.f_e);
            }
            org.knowm.xchart.CategoryChart chart4b = util.ChartFactory.createBatteryStatusChart(mois, ff, fe);
            java.util.List<org.knowm.xchart.CategoryChart> chartsAll = new java.util.ArrayList<>();
            chartsAll.add(util.ChartFactory.createProductionChart(mois, prod));
            chartsAll.add(util.ChartFactory.createLostEnergyChart(mois, lost));
            chartsAll.add(util.ChartFactory.createHistogramChart(csLabels, fcs));
            chartsAll.add(chart4b);
            graphsPanel.setCharts(chartsAll);
    } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Erreur lors du tracé des graphes", ex);
            JLabel label = new JLabel("Erreur lors du tracé des graphes : " + ex.getMessage());
            graphsPanel.add(label);
            graphsPanel.revalidate();
            graphsPanel.repaint();
        }
    }
}


