package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import modele.cablesetprotections.CalculCablesEtProtections;
import vue.cablesetprotections.FenetreSurgissanteAide;
import vue.cablesetprotections.Tables;
import vue.cablesetprotections.ZonesDeTexte;

/**
 * Représente la page pour la gestion des câbles et protections dans l'application.
 * Elle contient différents panneaux et éléments d'interface utilisateur pour la configuration des câbles et protections.
 */
public class PageCablesProtections extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int tailleBoutons = 40;
    private static GridBagConstraints gbc;
    private static ImageIcon imageIconDispositifsProtection;
    private static ImageIcon imageIconConditionsInstallationParafoudreDC;
    private static ImageIcon imageIconNiveauxKerauniques;
    private static ImageIcon imageIconDefLEtSchemaLtotale;
    private static ImageIcon imageIconConditionsParafoudreDC;
    private static ImageIcon imageIconCaracteristiquesProtectionContreLesSurintensites;
    private static ImageIcon imageIconDisjoncteurAC;
    private static ImageIcon imageIconChoixParafoudreAC;
    private static ImageIcon imageIconDisjoncteurParafoudreAC;
    private static ImageIcon imageIconCourantsAdmissiblesCablesDeChainesPV;
    private static ImageIcon imageIconDeterminationSectionCablesSchneider;
    private static ImageIcon imageIconTrouverLettreDeSelection;
    private static ImageIcon imageIconTrouverK1;
    private static ImageIcon imageIconTrouverK2;
    private static ImageIcon imageIconFacteursCorrectionTAmbiante;
    private static ImageIcon imageIconChoixCablePrincipal;
    private static ImageIcon imageIconChuteDeTension;

    /**
     * Retourne les contraintes de la grille utilisées pour la mise en page.
     *
     * @return Les contraintes de la grille.
     */
    public static GridBagConstraints getGBC() {
        return gbc;
    }
    
    /**
     * Initialise les images utilisées dans l'application.
     */
    private void initialiserImages() {
        imageIconDispositifsProtection = new ImageIcon(FenetrePrincipale.class.getResource("/dispositifs_protection.png"));
        imageIconConditionsInstallationParafoudreDC = new ImageIcon(FenetrePrincipale.class.getResource("/conditions_installation_parafoudreDC.png"));
        imageIconNiveauxKerauniques = new ImageIcon(FenetrePrincipale.class.getResource("/niveaux_kerauniques.png"));
        imageIconDefLEtSchemaLtotale = new ImageIcon(FenetrePrincipale.class.getResource("/def_L_et_schema_Ltotale.png"));
        imageIconConditionsParafoudreDC = new ImageIcon(FenetrePrincipale.class.getResource("/conditions_parafoudreDC.png"));
        imageIconCaracteristiquesProtectionContreLesSurintensites = new ImageIcon(FenetrePrincipale.class.getResource("/caracteristiques_protection_contre_les_surintensites.png"));
        imageIconDisjoncteurAC = new ImageIcon(FenetrePrincipale.class.getResource("/disjoncteursAC.png"));
        imageIconChoixParafoudreAC = new ImageIcon(FenetrePrincipale.class.getResource("/choix_parafoudreAC.png"));
        imageIconDisjoncteurParafoudreAC = new ImageIcon(FenetrePrincipale.class.getResource("/disjoncteur_parafoudreAC.png"));
        imageIconCourantsAdmissiblesCablesDeChainesPV = new ImageIcon(FenetrePrincipale.class.getResource("/courants_admissibles_cables_de_chaines_PV.png"));
        imageIconDeterminationSectionCablesSchneider = new ImageIcon(FenetrePrincipale.class.getResource("/determination_section_cables_SCHNEIDER.png"));
        imageIconTrouverLettreDeSelection = new ImageIcon(FenetrePrincipale.class.getResource("/trouverLettreDeSelection.png"));
        imageIconTrouverK1 = new ImageIcon(FenetrePrincipale.class.getResource("/trouverK1.png"));
        imageIconTrouverK2 = new ImageIcon(FenetrePrincipale.class.getResource("/trouverK2.png"));
        imageIconFacteursCorrectionTAmbiante = new ImageIcon(FenetrePrincipale.class.getResource("/facteurs_correction_T_ambiantes.png"));
        imageIconChoixCablePrincipal = new ImageIcon(FenetrePrincipale.class.getResource("/choix_cable_principal.png"));
        imageIconChuteDeTension = new ImageIcon(FenetrePrincipale.class.getResource("/chute_de_tension.png"));
    }
    
    /**
     * Insère une table dans un panneau avec un layout BorderLayout.
     *
     * @param table La table à insérer.
     * @return Le panneau contenant la table.
     */
    private JPanel insererTableDansPanel(JTable table) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.weighty = 1;
        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(getBackground());

        int rowCount = table.getRowCount();
        int rowHeight = table.getRowHeight();
        int tableHeaderHeight = table.getTableHeader().getPreferredSize().height;
        int totalHeight = (rowCount * rowHeight) + tableHeaderHeight;

        table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, totalHeight));

        panelTable.add(table.getTableHeader(), BorderLayout.NORTH);
        panelTable.add(table, BorderLayout.CENTER);
        
        return panelTable;
    }
    
    /**
     * Crée un panneau avec un titre et un bouton d'aide.
     *
     * @param titre Le titre à afficher.
     * @param imageIcon L'icône à utiliser pour l'aide.
     * @return Le panneau créé.
     */
    public JPanel createPanelTitreAvecBoutonAide(String titre, ImageIcon imageIcon) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel panelTitreAvecBouton = new JPanel(new BorderLayout());
        panelTitreAvecBouton.setBackground(getBackground());

        JLabel titreLabel = new JLabel(titre);
        titreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titreLabel.setFont(new Font(titreLabel.getFont().getName(), Font.PLAIN, titreLabel.getFont().getSize()));
        titreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        panelTitreAvecBouton.add(titreLabel, BorderLayout.WEST);

        JButton bouton = new JButton("?");
        bouton.setPreferredSize(new Dimension(tailleBoutons, tailleBoutons));
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FenetreSurgissanteAide.showAide(titre, imageIcon);
            }
        });
        panelTitreAvecBouton.add(bouton, BorderLayout.EAST);
        return panelTitreAvecBouton;
    }
    
    /**
     * Crée un label avec un gros titre.
     *
     * @param titre Le texte du titre.
     * @return Le label créé.
     */
    public JLabel createGrosTitreLabel(String titre) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel titreLabel = new JLabel(titre);
        titreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        Font font = titreLabel.getFont();
        titreLabel.setFont(new Font(font.getName(), Font.BOLD, font.getSize() + 2));
        return titreLabel;
    }
    
    /**
     * Crée un panneau contenant un texte.
     *
     * @param texte Le texte à afficher.
     * @return Le panneau créé.
     */
    public JPanel createTextePanel(String texte) {
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.WEST;
        JPanel panelTexteEtZoneDeTexte = new JPanel(new BorderLayout());
        panelTexteEtZoneDeTexte.setBackground(getBackground());

        JLabel texteLabel = new JLabel(texte);
        texteLabel.setFont(new Font(texteLabel.getFont().getName(), Font.PLAIN, texteLabel.getFont().getSize()));
        panelTexteEtZoneDeTexte.add(texteLabel, BorderLayout.WEST);
        
        return panelTexteEtZoneDeTexte;
    }
    
    /**
     * Constructeur de la page pour la gestion des câbles et protections.
     */
    public PageCablesProtections() {
    	
    	// Initialisation
    	initialiserImages();
    	CalculCablesEtProtections.MAJOnduleurs(PageOnduleur.getTablesBilan(), PageOnduleur.getEtudesConcerneesParChaqueTableDuBilan());
    	Tables.initialiserTables();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Création du panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(getBackground());
        panelPrincipal.setLayout(new GridBagLayout());
        
        // Création du gbc
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        ZonesDeTexte.setGBC();
        
        // DISPOSITIFS DE PROTECTION ÉLECTRIQUE
        panelPrincipal.add(createGrosTitreLabel("DISPOSITIFS DE PROTECTION ÉLECTRIQUE"), gbc);

        // Choix des dispositifs de protection partie DC
        panelPrincipal.add(createGrosTitreLabel("1. Choix des dispositifs de protection partie DC"), gbc);
       
        // Choix de fusible : Protection des modules PV contre les surintensité
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Choix de fusible : Protection des modules PV contre les surintensité", imageIconDispositifsProtection), gbc);
      
        // Irm
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteIrm("Irm (A) : ", 5), gbc);

        // Rappel UocTmin
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteRappelUocTmin("Rappel de Uoc Tmin (V) : ", 5), gbc);

        // Rappel IscSTC
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteRappelIscSTC("Rappel de Isc STC (A) : ", 5), gbc);

        // Rappel UmppSTC
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteRappelUmppSTC("Rappel de Umpp STC (V) : ", 5), gbc);
        
        // Rappel ImppSTC
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteRappelImppSTC("Rappel de Impp STC (A) : ", 5), gbc);
    
        // Table choix de fusible
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChoixDeFusible()), gbc);
        
        // Fusible choisi
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteChoixCalibre("Choix final du calibre du fusible (A) : ", 3), gbc);
        
        // Choix des parafoudres DC
        panelPrincipal.add(createTextePanel("Choix des parafoudres DC"), gbc);

        // Rappels sur L et Ltotale et la constante pour Lcrit
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Aide pour trouver Nk --> ", imageIconNiveauxKerauniques), gbc);

        // Nk
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteNkDC("Nk : ", 5), gbc);
        
        // Ng
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteNgDC("Ng : ", 5), gbc);
        
        // Rappels sur L et Ltotale et la constante pour Lcrit
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Aide pour trouver la constante pour Lcrit --> ", imageIconConditionsInstallationParafoudreDC), gbc);

        // Constante pour Lcrit
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteConstantePourLcrit("Constante pour Lcrit : ", 5), gbc);
        
        // Lcrit
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteLcrit("Lcrit (m) : ", 5), gbc);
        
        // Rappels sur L et Ltotale
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconDefLEtSchemaLtotale), gbc);
        
        // Table des différents câbles
        panelPrincipal.add(insererTableDansPanel(Tables.getTableDesDifferentsCables()), gbc);
        
        // Table L
        panelPrincipal.add(insererTableDansPanel(Tables.getTableL()), gbc);
        
        // Ltotale
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteLtotale("Ltotale (m) : ", 5), gbc);
        
        // Protection parafoudre obligatoire côté DC ?
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteProtectionParafoudreObligatoireDC("Protection parafoudre obligatoire côté DC ? ", 3), gbc);
        
        // Choix et mise en oeuvre des parafoudres côté DC
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Choix et mise en oeuvre des parafoudres côté DC", imageIconConditionsParafoudreDC), gbc);
        
        // Uw
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteUw("Uw (V) : ", 5), gbc);
        
        // Table parafoudre DC
        panelPrincipal.add(insererTableDansPanel(Tables.getTableParafoudreDC()), gbc);

        // Besoin d'un second parafoudre ?
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteSecondParafoudreDC("Besoin d'un second parafoudre ? : ", 3), gbc);

        // Choix de l'interrupteur sectionneur DC
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Choix de l'interrupteur sectionneur DC", imageIconCaracteristiquesProtectionContreLesSurintensites), gbc);
        
        // Table Interrupteurs-sectionneurs
        panelPrincipal.add(insererTableDansPanel(Tables.getTableInterrupteursSectionneurs()), gbc);
        
        // Choix des dispositifs de protection partie AC
        panelPrincipal.add(createGrosTitreLabel("2. Choix des dispositifs de protection partie AC"), gbc);
        
        // Choix des disjoncteurs AC associés à chaque onduleur
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Choix des disjoncteurs AC associés à chaque onduleur", imageIconDisjoncteurAC), gbc);
        
        // Table des disjoncteurs AC associés à chaque onduleur
        panelPrincipal.add(insererTableDansPanel(Tables.getTableDisjoncteursACAssociesAuxOnduleurs()), gbc);
      
        // Choix des disjoncteurs AC Général (AGCP)
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Choix du disjoncteur général (AGCP)", imageIconDisjoncteurAC), gbc);
        
        // Table du disjoncteur général (AGCP)
        panelPrincipal.add(insererTableDansPanel(Tables.getTableDisjoncteurACGeneral()), gbc);
        
        // Choix du parafoudre AC
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Choix du parafoudre AC", imageIconChoixParafoudreAC), gbc);

        // Nk
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteNkAC("Nk : ", 5), gbc);
        
        // Ng
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteNgAC("Ng : ", 5), gbc);
        
        // Protection parafoudre obligatoire ?
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteProtectionParafoudreObligatoireAC("Protection parafoudre obligatoire ? ", 55), gbc);
        
        // Table du parafoudre AC
        panelPrincipal.add(insererTableDansPanel(Tables.getTableParafoudreAC()), gbc);
        
        // Choix du parafoudre AC
        panelPrincipal.add(createPanelTitreAvecBoutonAide("Disjoncteur associé au parafoudre", imageIconDisjoncteurParafoudreAC), gbc);
        
        // Table du disjoncteur AC onduleur du parafoudre AC
        panelPrincipal.add(insererTableDansPanel(Tables.getTableDisjoncteurAssocieAuParafoudreAC()), gbc);
        
        // SECTION DES CABLES ET CHUTE DES TENSIONS
        panelPrincipal.add(createGrosTitreLabel("SECTION DES CABLES ET CHUTE DES TENSIONS"), gbc);
        
        // Choix des câbles de chaîne et vérification de la chute de tension
        panelPrincipal.add(createGrosTitreLabel("1. Choix des câbles de chaîne et vérification de la chute de tension"), gbc);
        
        // Courants admissibles des câbles de chaînes PV
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconCourantsAdmissiblesCablesDeChainesPV), gbc);
        
        // Table NcNpIz
        panelPrincipal.add(insererTableDansPanel(Tables.getTableNcNpIz()), gbc);
        
        // Aide détermination de la section de câbles de SCHNEIDER
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconDeterminationSectionCablesSchneider), gbc);

        // Aide pour trouver la lettre de sélection
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconTrouverLettreDeSelection), gbc);
       
        // Lettre de sélection
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteLettreSelectionCablesChaine("Lettre de sélection : ", 2), gbc);

        // Aide pour trouver K1
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconTrouverK1), gbc);
      
        // K1
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteK1CablesChaine("K1 : ", 4), gbc);

        // Aide pour trouver K2
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconTrouverK2), gbc);
       
        // K2
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteK2CablesChaine("K2 : ", 4), gbc);
        
        // Aide pour trouver K3
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconFacteursCorrectionTAmbiante), gbc);
        
        // K3
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteK3CablesChaine("K3 : ", 4), gbc);
        
        // K
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteKCablesChaine("K = ", 4), gbc);

        // Choix des câbles de chaîne (2.5, 4, 6mm² ?)
        panelPrincipal.add(createTextePanel("Choix des câbles de chaîne (2.5, 4, 6mm² ?) : "), gbc);
        
        // Table choix câbles de chaîne
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChoixCablesDeChaine()), gbc);
        
        // Section pour câbles de chaine choisie
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteChoixSectionCablesDeChaine("Choix final de la section pour les câbles de chaîne : ", 3), gbc);
           
        // Calcul de la chute de tension (chaîne)
        panelPrincipal.add(createTextePanel("Calcul de la chute de tension (chaîne) : "), gbc);
        
        // Table chute de tension 1
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChuteDeTension1()), gbc);
        
        // Table chute de tension 2
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChuteDeTension2()), gbc);
        
        // Choix du câble principal et vérification de la chute de tension
        panelPrincipal.add(createGrosTitreLabel("2. Choix du câble principal et vérification de la chute de tension"), gbc);
        
        // Aide
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconChoixCablePrincipal), gbc);

        // Table choix du câble principal et vérification de la chute de tension 1
        panelPrincipal.add(insererTableDansPanel(Tables.getTableCablePrincipal1()), gbc);
      
        // Aide pour trouver la lettre de sélection
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconTrouverLettreDeSelection), gbc);
       
        // Lettre de sélection
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteLettreSelectionCablePrincipal("Lettre de sélection : ", 2), gbc);
    
        // Aide pour trouver K1
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconTrouverK1), gbc);
         
        // K1
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteK1CablePrincipal("K1 : ", 4), gbc);
     
        // Aide pour trouver K2
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconTrouverK2), gbc);
       
        // K2
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteK2CablePrincipal("K2 : ", 4), gbc);
        
        // Aide pour trouver K3
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconFacteursCorrectionTAmbiante), gbc);
        
        // K3
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteK3CablePrincipal("K3 : ", 4), gbc);
        
        // K
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteKCablePrincipal("K = ", 4), gbc);

        // Choix des câbles de chaîne (2.5, 4, 6mm² ?)
        panelPrincipal.add(createTextePanel("Choix du câble principal (2.5, 4, 6mm² ?) : "), gbc);
        
        // Table choix du câble principal et vérification de la chute de tension 2
        panelPrincipal.add(insererTableDansPanel(Tables.getTableCablePrincipal2()), gbc);

        // Section pour câble principal choisie
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteChoixSectionCablePrincipal("Choix final de la section pour le câble principal : ", 3), gbc);
         
        // Calcul de la chute de tension maximum à respecter
        panelPrincipal.add(createTextePanel("Calcul de la chute de tension maximum à respecter : "), gbc);
        
        // Table calcul de la chute de tension maximum à respecter
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChuteDeTensionMaximumARespecter()), gbc);
        
        // Chute de tension totale DC maximum à respecter
        panelPrincipal.add(createTextePanel("Chute de tension totale DC maximum à respecter : "), gbc);
        panelPrincipal.add(createTextePanel("Règle : 3% maxi selon la norme, mais il est préférable de choisir 1%."), gbc);
        
        // Table chute de tension totale DC maximum à respecter
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChuteDeTensionTotaleDCMaximumARespecter()), gbc);
        
        // Section des câbles et chute de tension côté AC
        panelPrincipal.add(createGrosTitreLabel("3. Section des câbles et chute de tension côté AC"), gbc);
        
        // Aide pour trouver K3
        panelPrincipal.add(createPanelTitreAvecBoutonAide("", imageIconChuteDeTension), gbc);
        
        // Calcul de la chute de tension entre un onduleur et le disjoncteur différentiel en sortie d'onduleur
        panelPrincipal.add(createTextePanel("Calcul de la chute de tension entre un onduleur et le disjoncteur différentiel en sortie d'onduleur"), gbc);
      
        // Table chute de tension entre onduleur et disjoncteur diff
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChuteDeTensionEntreOnduleurEtDisjoncteurDiff()), gbc);
        
        // Calcul de la chute de tension entre le disjoncteur différentiel et l'AGCP
        panelPrincipal.add(createTextePanel("Calcul de la chute de tension maximum à respecter : "), gbc);
        
        // Table chute de tension entre le disjoncteur diff et l'AGCP
        panelPrincipal.add(insererTableDansPanel(Tables.getTableChuteDeTensionEntreDisjoncteurDiffEtAGCP()), gbc);
        
        // Chute de tension totale AC
        panelPrincipal.add(createTextePanel("Chute de tension totale AC : "), gbc);
        panelPrincipal.add(createTextePanel("On retient le cumul le plus important de chute de tension entre onduleur et l’AGCP."), gbc);
        
        // ΔU/U totale (%)
        panelPrincipal.add(ZonesDeTexte.createTexteEtZoneDeTexteChuteTensionAC("ΔU/U totale (%) = ", 55), gbc);
        
        // Ajout final
        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.getViewport().setBackground(getBackground());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setBlockIncrement(100);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Obtient le panneau principal de la page des câbles et protections.
     * 
     * @return Le panneau principal de la page des câbles et protections.
     */
	public JPanel getPage() {
		return this;
	}
	
}
