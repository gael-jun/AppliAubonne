package modele;

import java.util.ArrayList;

/**
 * Classe représentant une étude d'un module photovoltaïque en fonction des températures
 * minimales et maximales.
 * <p>
 * Cette classe calcule les caractéristiques du module photovoltaïque à différentes températures
 * et fournit les attributs calculés pour une utilisation ultérieure.
 * </p>
 */
public class EtudeDUnModule {
    private Module module;
    private double TTmin;
    private double PmaxTmin;
    private double UocTmin;
    private double UmppTmin;
    private double IscTmin;
    private double ImppTmin;
    private double TTmax;
    private double PmaxTmax;
    private double UocTmax;
    private double UmppTmax;
    private double IscTmax;
    private double ImppTmax;
    private double UocMaxMin;

    /**
     * Constructeur de l'étude d'un module avec des températures minimales et maximales.
     * 
     * @param module Le module photovoltaïque à étudier.
     * @param TTmin La température minimale pour l'étude.
     * @param TTmax La température maximale pour l'étude.
     */
    public EtudeDUnModule(Module module, double TTmin, double TTmax) {
        this.module = module;
        this.TTmin = TTmin;
        this.PmaxTmin = 0;
        this.UocTmin = 0;
        this.UmppTmin = 0;
        this.IscTmin = 0;
        this.ImppTmin = 0;
        this.TTmax = TTmax;
        this.PmaxTmax = 0;
        this.UocTmax = 0;
        this.UmppTmax = 0;
        this.IscTmax = 0;
        this.ImppTmax = 0;
        this.UocMaxMin = 0;
    }

    /**
     * Calcule les caractéristiques du module à différentes températures (minimale et maximale).
     * Les attributs calculés sont arrondis et stockés dans les variables de la classe.
     */
    public void calculer() {
        double KpTmin = (1 + (this.module.getCTPMPP() / 100) * (this.TTmin - this.module.getTTSTC()));
        double KuTmin = (1 + (this.module.getCTUoc() / 100) * (this.TTmin - this.module.getTTSTC()));
        double KiTmin = (1 + (this.module.getCTIsc() / 100) * (this.TTmin - this.module.getTTSTC()));
        double KpTmax = (1 + (this.module.getCTPMPP() / 100) * (this.TTmax - this.module.getTTSTC()));
        double KuTmax = (1 + (this.module.getCTUoc() / 100) * (this.TTmax - this.module.getTTSTC()));
        double KiTmax = (1 + (this.module.getCTIsc() / 100) * (this.TTmax - this.module.getTTSTC()));
        
        this.PmaxTmin = Calcul.arrondirDouble(this.module.getPmaxTSTC() * KpTmin, 2);
        this.UocTmin = Calcul.arrondirDouble(this.module.getUocTSTC() * KuTmin, 2);
        this.UmppTmin = Calcul.arrondirDouble(this.module.getUmppTSTC() * KuTmin, 2);
        this.IscTmin = Calcul.arrondirDouble(this.module.getIscTSTC() * KiTmin, 2);
        this.ImppTmin = Calcul.arrondirDouble(this.module.getImppTSTC() * KiTmin, 2);
        
        this.PmaxTmax = Calcul.arrondirDouble(this.module.getPmaxTSTC() * KpTmax, 2);
        this.UocTmax = Calcul.arrondirDouble(this.module.getUocTSTC() * KuTmax, 2);
        this.UmppTmax = Calcul.arrondirDouble(this.module.getUmppTSTC() * KuTmax, 2);
        this.IscTmax = Calcul.arrondirDouble(this.module.getIscTSTC() * KiTmax, 2);
        this.ImppTmax = Calcul.arrondirDouble(this.module.getImppTSTC() * KiTmax, 2);
        
        this.UocMaxMin = Calcul.arrondirDouble(1.2 * this.module.getUocTSTC(), 2);
    }

    /**
     * Obtient une liste des attributs calculés du module et de ses caractéristiques de température.
     * 
     * @return Une liste d'objets Double représentant les attributs du module.
     */
    public ArrayList<Double> getAttributs() {
        ArrayList<Double> attributes = new ArrayList<>();
        attributes.add(this.module.getCTPMPP());
        attributes.add(this.module.getCTUoc());
        attributes.add(this.module.getCTIsc());
        attributes.add(this.module.getTTSTC());
        attributes.add(this.module.getPmaxTSTC());
        attributes.add(this.module.getUocTSTC());
        attributes.add(this.module.getUmppTSTC());
        attributes.add(this.module.getIscTSTC());
        attributes.add(this.module.getImppTSTC());
        attributes.add(this.TTmin);
        attributes.add(this.PmaxTmin);
        attributes.add(this.UocTmin);
        attributes.add(this.UmppTmin);
        attributes.add(this.IscTmin);
        attributes.add(this.ImppTmin);
        attributes.add(this.TTmax);
        attributes.add(this.PmaxTmax);
        attributes.add(this.UocTmax);
        attributes.add(this.UmppTmax);
        attributes.add(this.IscTmax);
        attributes.add(this.ImppTmax);
        attributes.add(this.UocMaxMin);
        return attributes;
    }

    /**
     * Obtient la tension à circuit ouvert minimale (Uoc) à la température minimale.
     * 
     * @return La tension à circuit ouvert minimale.
     */
    public double getUocTmin() {
        return this.UocTmin;
    }

    /**
     * Obtient la tension de puissance maximale au point de fonctionnement (Umpp) à la température maximale.
     * 
     * @return La tension de puissance maximale au point de fonctionnement.
     */
    public double getUmppTmax() {
        return this.UmppTmax;
    }

    /**
     * Obtient la tension de puissance maximale au point de fonctionnement (Umpp) à la température minimale.
     * 
     * @return La tension de puissance maximale au point de fonctionnement.
     */
    public double getUmppTmin() {
        return this.UmppTmin;
    }

    /**
     * Obtient le courant de puissance maximale au point de fonctionnement (Impp) à la température maximale.
     * 
     * @return Le courant de puissance maximale au point de fonctionnement.
     */
    public double getImppTmax() {
        return this.ImppTmax;
    }

    /**
     * Obtient le module associé à l'étude.
     * 
     * @return Le module photovoltaïque associé.
     */
    public Module getModule() {
        return this.module;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères des attributs calculés du module.
     * 
     * @return Une chaîne de caractères représentant les attributs calculés.
     */
    @Override
    public String toString() {
        return this.getAttributs().toString();
    }
}
