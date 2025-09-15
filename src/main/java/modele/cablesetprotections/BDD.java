package modele.cablesetprotections;

/**
 * La classe BDD représente une base de données simulée
 */
public class BDD {

    // Les différentes familles d'éléments
    private static Fusibles fusibles;
    private static ParafoudresDC parafoudresDC;
    private static InterrupteursSectionneursDC interrupteursSectionneursDC;
    private static DisjoncteursACOnduleur disjoncteursACOnduleur;
    private static ProtectionsDifferentielles protectionsDifferentielles;
    private static DisjoncteursACGeneraux disjoncteursACGeneraux;
    private static ParafoudresAC parafoudresAC;

    /**
     * Constructeur de la classe BDD.
     */
    public BDD() {
		
		fusibles = new Fusibles();
		parafoudresDC = new ParafoudresDC();
		interrupteursSectionneursDC = new InterrupteursSectionneursDC();
		disjoncteursACOnduleur = new DisjoncteursACOnduleur();
		protectionsDifferentielles = new ProtectionsDifferentielles();
		disjoncteursACGeneraux = new DisjoncteursACGeneraux();
		parafoudresAC = new ParafoudresAC();
		
		// Fusibles gPV cylindriques
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0001", 1));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0002", 2));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0003", 3));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0004", 4));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0006", 6));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0008", 8));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0010", 10));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0012", 12));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0015", 15));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0016", 16));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0020", 20));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0C25", 25));
		fusibles.ajouterElement(new FusiblegPVCylindrique("DF Electric 60PV 0C32", 32));
		
		// Fusibles gPV à couteaux
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0032", 32));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0040", 40));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0050", 50));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0063", 63));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0080", 80));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0100", 100));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0125", 125));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0160", 160));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 1200", 200));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 1250", 250));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 1315", 315));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 1400", 400));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0500", 500));
		fusibles.ajouterElement(new FusiblegPVACouteaux("DF Electric 60PV 0600", 600));
	
		// Parafoudres DC
		parafoudresDC.ajouterElement(new ParafoudreDC("Citel DS50VGPV-600G/51", 720, 1000, 15, "2.2/3.4"));
		parafoudresDC.ajouterElement(new ParafoudreDC("Citel DS50VGPV-1000G/51", 1200, 1000, 15, "2.8/5.1"));
		parafoudresDC.ajouterElement(new ParafoudreDC("Citel DS50VGPV-1500G/51", 1500, 1000, 15, "3.4/6.8"));
		
		// Interrupteurs-sectionneurs DC
		interrupteursSectionneursDC.ajouterElement(new InterrupteurSectionneurDC("Schneider 28907", 600, 10));
		interrupteursSectionneursDC.ajouterElement(new InterrupteurSectionneurDC("Schneider A9N61690", 1000, 20));
		interrupteursSectionneursDC.ajouterElement(new InterrupteurSectionneurDC("Schneider MGN61699", 1000, 50));
	
		// Protections différentielles
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21450", 30, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21451", 300, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21452", 30, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21453", 300, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21454", 30, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21455", 300, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21456", 30, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21457", 300, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21470", 30, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21471", 300, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21472", 30, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21473", 300, "AC"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21474", 30, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21475", 300, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21476", 30, "A"));
		protectionsDifferentielles.ajouterElement(new ProtectionDifferentielle("Schneider A9N21477", 300, "A"));
		
		// Disjoncteurs différentiels AC - sortie onduleur
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21101", 2, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21102", 6, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21103", 10, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21104", 18, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21105", 20, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21106", 25, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21107", 32, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21108", 40, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21113", 10, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21114", 16, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21115", 20, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21116", 25, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21117", 32, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21118", 40, 4.5, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21019", 1, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21020", 2, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21021", 3, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21022", 4, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21023", 6, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21024", 10, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21025", 16, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21026", 20, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21027", 25, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21028", 32, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21029", 40, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21063", 6, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21064", 10, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21065", 16, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21066", 20, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21067", 25, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21068", 32, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21069", 40, 6, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21009", 6, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21010", 10, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21011", 16, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21012", 20, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21013", 25, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21014", 32, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21015", 40, 6, 'B'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21073", 6, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21074", 10, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21075", 16, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21076", 20, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21077", 25, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21078", 32, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21079", 40, 6, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21360", 1, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21361", 2, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21362", 3, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21363", 4, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21364", 6, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21365", 10, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21366", 16, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21367", 20, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21368", 25, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21369", 32, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21370", 40, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21404", 6, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21405", 10, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21406", 16, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21407", 20, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21408", 25, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21409", 32, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21410", 40, 10, 'C'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21371", 1, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21372", 2, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21373", 4, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21374", 6, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21375", 10, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21376", 16, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21377", 20, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21378", 25, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21379", 32, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21380", 40, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21414", 6, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21415", 10, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21416", 16, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21417", 20, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21418", 25, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21419", 32, 10, 'D'));
		disjoncteursACOnduleur.ajouterElement(new DisjoncteurACOnduleur("Schneider A9N21420", 40, 10, 'D'));
		
		// Disjoncteurs généraux
		disjoncteursACGeneraux.ajouterElement(new DisjoncteurACGeneral("Legrand DPX^3 630 magnétothermique", "36-100", 630, 'C', "A"));
		disjoncteursACGeneraux.ajouterElement(new DisjoncteurACGeneral("Legrand DPX^3 250 électronique", "25-70", 250, 'C', "A"));
		disjoncteursACGeneraux.ajouterElement(new DisjoncteurACGeneral("Legrand DPX^3 250 magnétothermique", "25-70", 250, 'C', "A"));
		disjoncteursACGeneraux.ajouterElement(new DisjoncteurACGeneral("Legrand DPX^3 160 magnétothermique", "16-50", 160, 'C', "A"));
		
		// Parafoudres AC | -1 = y a rien
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-385/G", "230/400 V 3-phase+N", 40, -1, 1.8, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-320/G", "230/400 V 3-phase+N", 40, -1, 1.5, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-280/G", "230/400 V 3-phase+N", 40, -1, 1.3, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-230/G", "230/400 V 3-phase+N", 40, -1, 1.25, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-120/G", "230/400 V 3-phase+N", 40, -1, 0.9, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-400", "230/400 V 3-phase+N", 160, 1.8, -1, 1.8));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-385", "230/400 V 3-phase+N", 160, 1.8, -1, 1.8));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-320", "230/400 V 3-phase+N", 160, 1.5, -1, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-280", "230/400 V 3-phase+N", 160, 1.3, -1, 1.3));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-230", "230/400 V 3-phase+N", 160, 1.25, -1, 1.25));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS44-120", "120/208 V 3-phase+N", 160, 0.9, -1, 0.9));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-120/G", "120/208 V 2-phase+N", 40, -1, 0.9, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-400", "230/400 V 3-phase", 120, 1.8, -1, -1));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-385", "230/400 V 3-phase", 120, 1.8, -1, -1));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-320", "230/400 V 3-phase", 120, 1.5, -1, -1));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-280", "230/400 V 3-phase", 120, 1.3, -1, -1));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-230", "230/400 V 3-phase", 120, 1.25, -1, -1));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS43-120", "120/208 V 3-phase", 120, 0.9, -1, -1));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-385/G", "230 V single phase", 40, -1, 1.8, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-320/G", "230 V single phase", 40, -1, 1.5, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-280/G", "230 V single phase", 40, -1, 1.3, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-230/G", "230 V single phase", 40, -1, 1.25, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-120/G", "120 V single phase", 40, -1, 0.9, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-400", "230 V single phase", 80, 1.8, -1, 1.8));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-385", "230 V single phase", 80, 1.8, -1, 1.8));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-320", "230 V single phase", 80, 1.5, -1, 1.5));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-280", "230 V single phase", 80, 1.3, -1, 1.3));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-230", "230 V single phase", 80, 1.25, -1, 1.25));
		parafoudresAC.ajouterElement(new ParafoudreAC("Citel DS42-120", "120 V single phase", 80, 0.9, -1, 0.9));
	}

    // Méthodes pour obtenir les différentes familles d'éléments

    /**
     * Obtient l'objet Fusibles qui contient tous les fusibles disponibles.
     * 
     * @return l'objet Fusibles qui contient tous les fusibles disponibles.
     */
    public static Fusibles getFusibles() {
        return fusibles;
    }

    /**
     * Obtient l'objet ParafoudresDC qui contient tous les parafoudres DC disponibles.
     * 
     * @return l'objet ParafoudresDC qui contient tous les parafoudres DC disponibles.
     */
    public static ParafoudresDC getParafoudresDC() {
        return parafoudresDC;
    }

    /**
     * Obtient l'objet InterrupteursSectionneursDC qui contient tous les interrupteurs-sectionneurs DC disponibles.
     * 
     * @return l'objet InterrupteursSectionneursDC qui contient tous les interrupteurs-sectionneurs DC disponibles.
     */
    public static InterrupteursSectionneursDC getInterrupteursSectionneursDC() {
        return interrupteursSectionneursDC;
    }

    /**
     * Obtient l'objet DisjoncteursACOnduleur qui contient tous les disjoncteurs AC pour onduleur disponibles.
     * 
     * @return l'objet DisjoncteursACOnduleur qui contient tous les disjoncteurs AC pour onduleur disponibles.
     */
    public static DisjoncteursACOnduleur getDisjoncteursACOnduleur() {
        return disjoncteursACOnduleur;
    }

    /**
     * Obtient l'objet ProtectionsDifferentielles qui contient toutes les protections différentielles disponibles.
     * 
     * @return l'objet ProtectionsDifferentielles qui contient toutes les protections différentielles disponibles.
     */
    public static ProtectionsDifferentielles getProtectionsDifferentielles() {
        return protectionsDifferentielles;
    }

    /**
     * Obtient l'objet DisjoncteursACGeneraux qui contient tous les disjoncteurs AC généraux disponibles.
     * 
     * @return l'objet DisjoncteursACGeneraux qui contient tous les disjoncteurs AC généraux disponibles.
     */
    public static DisjoncteursACGeneraux getDisjoncteursACGeneraux() {
        return disjoncteursACGeneraux;
    }

    /**
     * Obtient l'objet ParafoudresAC qui contient tous les parafoudres AC disponibles.
     * 
     * @return l'objet ParafoudresAC qui contient tous les parafoudres AC disponibles.
     */
    public static ParafoudresAC getParafoudresAC() {
        return parafoudresAC;
    }
}
