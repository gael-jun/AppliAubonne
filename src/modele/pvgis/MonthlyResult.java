package modele.pvgis;

/**
 * Données agrégées par mois issues de PVGIS.
 * month: 1..12
 * E_d: production moyenne journalière Wh/j
 * E_lost_d: énergie perdue Wh/j (batterie pleine, clipping...)
 * f_f: pourcentage de jours batterie pleine (%)
 * f_e: pourcentage de jours batterie vide (%)
 */
public final class MonthlyResult {
    public final int month;
    public final double E_d;
    public final double E_lost_d;
    public final double f_f;
    public final double f_e;

    public MonthlyResult(int month, double e_d, double e_lost_d, double f_f, double f_e) {
        this.month = month; this.E_d = e_d; this.E_lost_d = e_lost_d; this.f_f = f_f; this.f_e = f_e;
    }
}
