package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe représentant une étude d'un onduleur en tenant compte des modules associés.
 * <p>
 * Cette classe calcule les caractéristiques d'un onduleur en fonction des modules photovoltaïques
 * associés, notamment le nombre maximum de modules, la plage de puissance admissible aux entrées,
 * et les courants associés aux chaînes maximales.
 * </p>
 */
public class EtudeDUnOnduleur {

    private Onduleur onduleur;
    private double UocMaxModule;
    private double ImppTmax;
    private int nbModulesMax;
    private int nbModulesPlageUMPPBasse;
    private int nbModulesPlageUMPPHaute;
    private double plagePuissanceBasse;
    private double plagePuissanceHaute;
    private List<Integer> nbChainesMaxParOnduleur;
    private List<Double> CourantAssocieANbChainesMaxParOnduleur;
    private EtudeDUnModule etudeModule;
    private List<String> nbChainesParEntreeAvecCourantAssocie;
    private String plageDePuissanceAdmissibleAuxEntrees;
    private Module moduleAssocie;
    private List<Integer> choixNbChaines;
    private double puissanceDemandee;

    /**
     * Constructeur de l'étude d'un onduleur avec un module associé.
     * 
     * @param onduleur L'onduleur à étudier.
     * @param moduleAssocie Le module photovoltaïque associé à l'étude.
     */
    public EtudeDUnOnduleur(Onduleur onduleur, Module moduleAssocie) {
        this.puissanceDemandee = 0;
        this.moduleAssocie = moduleAssocie;
        this.onduleur = onduleur;
        this.nbModulesMax = 0;
        this.plageDePuissanceAdmissibleAuxEntrees = "";
        this.choixNbChaines = new ArrayList<>();
        this.nbChainesMaxParOnduleur = new ArrayList<>();
        this.CourantAssocieANbChainesMaxParOnduleur = new ArrayList<>();
        this.nbChainesParEntreeAvecCourantAssocie = new ArrayList<>();
        this.etudeModule = new EtudeDUnModule(this.moduleAssocie, -10, 70);
        this.etudeModule.calculer();
        this.UocMaxModule = this.etudeModule.getUocTmin();
        this.ImppTmax = this.etudeModule.getImppTmax();
        postConstructeur();
    }

    /**
     * Méthode de post-construction pour initialiser les données après le constructeur.
     */
    public void postConstructeur() {
        this.nbModulesMax = (int) Math.floor(this.onduleur.getU_DCmax() / this.UocMaxModule);
        this.nbModulesPlageUMPPBasse = (int) Math.floor(this.onduleur.getPlageUMPPBasse() / this.etudeModule.getUmppTmax());
        this.nbModulesPlageUMPPHaute = (int) Math.floor(this.onduleur.getPlageUMPPHaute() / this.etudeModule.getUmppTmin());
        for (int i = 0; i < this.onduleur.getNbEntreesMPPIndependantes(); i++) {
            double courant = this.ImppTmax;
            int nbChaines = 1;
            double courantAPasDepasser = this.onduleur.getI_DCmax().get(i);
            while (courant < courantAPasDepasser && nbChaines <= this.onduleur.getChainesParEntreeMPP().get(i)) {
                nbChaines++;
                courant += this.ImppTmax;
            }
            nbChaines--;
            courant -= this.ImppTmax;
            this.nbChainesMaxParOnduleur.add(nbChaines);
            this.CourantAssocieANbChainesMaxParOnduleur.add(courant);
            this.nbChainesParEntreeAvecCourantAssocie.add(nbChaines + " (" + Calcul.arrondirDouble(courant, 2) + " A)");
        }
        this.plagePuissanceBasse = Calcul.arrondirDouble(0.9 * this.onduleur.getPDCmax(), 2);
        this.plagePuissanceHaute = Calcul.arrondirDouble(1.1 * this.onduleur.getPDCmax(), 2);
        this.plageDePuissanceAdmissibleAuxEntrees = this.plagePuissanceBasse
            + " W < " + Calcul.arrondirDouble(this.onduleur.getPDCmax(), 2)
            + " W < " + this.plagePuissanceHaute + " W";
    }

    /**
     * Obtient le nombre maximum de modules pouvant être utilisés avec l'onduleur.
     * 
     * @return Le nombre maximum de modules.
     */
    public int getNbModulesMax() {
        return this.nbModulesMax;
    }

    /**
     * Obtient la plage de nombre de modules pour les plages de tension UMPP basse et haute.
     * 
     * @return La plage de nombre de modules sous forme de chaîne de caractères.
     */
    public String getPlageModules() {
        return this.nbModulesPlageUMPPBasse + " - " + this.nbModulesPlageUMPPHaute;
    }

    /**
     * Obtient la chaîne des nombres de chaînes par entrée avec le courant associé.
     * 
     * @return Une chaîne de caractères contenant les nombres de chaînes et les courants associés.
     */
    public String getNbChainesParEntreeAvecCourantAssocie() {
        return this.nbChainesParEntreeAvecCourantAssocie.stream().collect(Collectors.joining(", "));
    }

    /**
     * Obtient la plage de puissance admissible aux entrées de l'onduleur.
     * 
     * @return La plage de puissance admissible aux entrées.
     */
    public String getPlageDePuissanceAdmissibleAuxEntrees() {
        return this.plageDePuissanceAdmissibleAuxEntrees;
    }

    /**
     * Obtient l'onduleur associé à l'étude.
     * 
     * @return L'onduleur associé.
     */
    public Onduleur getOnduleur() {
        return this.onduleur;
    }

    /**
     * Obtient l'étude du module associé à l'onduleur.
     * 
     * @return L'étude du module associé.
     */
    public EtudeDUnModule getEtudeModule() {
        return this.etudeModule;
    }

    /**
     * Obtient la puissance demandée.
     * 
     * @return La puissance demandée.
     */
    public double getPuissanceDemandee() {
        return this.puissanceDemandee;
    }

    /**
     * Calcule le bon nombre de modules en fonction du nombre de chaînes.
     * 
     * @param choixNbChaines Le nombre de chaînes choisi.
     * @return Le nombre de modules ajusté.
     */
    public int calculerBonNbModules(int choixNbChaines) {
        return this.nbModulesPlageUMPPHaute * choixNbChaines * this.moduleAssocie.getPmaxTSTC() > this.plagePuissanceHaute ? choixNbChaines - 1 : choixNbChaines;
    }

    /**
     * Obtient le module associé à l'étude.
     * 
     * @return Le module associé.
     */
    public Module getModuleAssocie() {
        return this.moduleAssocie;
    }

    /**
     * Définit la puissance demandée en fonction du choix écrit.
     * 
     * @param choixEcrit La chaîne représentant le choix de puissance.
     */
    public void setPuissance(String choixEcrit) {
        if (this.onduleur.getUniqueEntree()) {
            int nb = Integer.parseInt(choixEcrit);
            this.choixNbChaines.add(nb);
            int bonNbModules = this.nbModulesPlageUMPPHaute;
            if (this.nbModulesPlageUMPPHaute == this.nbModulesPlageUMPPBasse + 1) {
                bonNbModules = calculerBonNbModules(nb);
            }
            this.puissanceDemandee = nb * bonNbModules * this.moduleAssocie.getPmaxTSTC();
        } else {
            String[] parties = choixEcrit.split("/");
            for (String partie : parties) {
                String[] sousPartie = partie.split(":");
                int nb = Integer.parseInt(sousPartie[1].trim());
                int bonNbModules = this.nbModulesPlageUMPPHaute;
                if (this.nbModulesPlageUMPPHaute == this.nbModulesPlageUMPPBasse + 1) {
                    bonNbModules = calculerBonNbModules(nb);
                }
                this.choixNbChaines.add(nb);
                this.puissanceDemandee += nb * bonNbModules * this.moduleAssocie.getPmaxTSTC();
            }
        }
    }

    /**
     * Obtient une conclusion sur la puissance demandée par rapport à la plage admissible.
     * 
     * @return Une chaîne indiquant si la puissance demandée est acceptable ou non.
     */
    public String conclusionPuissanceDemandee() {
        boolean okOuPas = this.puissanceDemandee <= this.plagePuissanceHaute;
        return okOuPas ? this.puissanceDemandee + " <= " + this.plagePuissanceHaute + " -> OK" : this.puissanceDemandee + " > " + this.plagePuissanceHaute + " -> NON OK";
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'étude d'onduleur.
     * 
     * @return Le nom de l'onduleur associé à l'étude.
     */
    @Override
    public String toString() {
        return this.getOnduleur().getNom();
    }
}
