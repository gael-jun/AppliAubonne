package modele;

/**
 * Paramètres financiers immuables pour le calcul (toutes unités en SI/€).
 */
public final class FinancialParams {
    public final double investissementInitial; // €
    public final double subvention;            // €
    public final double prixVente;             // €/kWh
    public final double tauxInjection;         // 0..1
    public final double coutAnnuel;            // € / an
    public final int duree;                    // années
    public final double tauxActualisation;     // 0..1
    public final int anneeDepart;              // année calendaire (ex: 2025)

    public FinancialParams(double investissementInitial, double subvention, double prixVente,
                           double tauxInjection, double coutAnnuel, int duree,
                           double tauxActualisation, int anneeDepart) {
        this.investissementInitial = investissementInitial;
        this.subvention = subvention;
        this.prixVente = prixVente;
        this.tauxInjection = tauxInjection;
        this.coutAnnuel = coutAnnuel;
        this.duree = duree;
        this.tauxActualisation = tauxActualisation;
        this.anneeDepart = anneeDepart;
    }
}
