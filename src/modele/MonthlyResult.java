package modele;

public final class MonthlyResult {
    public final int month; // 1..12
    public final double E_d; // Wh/day
    public final double E_lost_d; // Wh/day lost
    public final double f_f; // % days full
    public final double f_e; // % days empty

    public MonthlyResult(int month, double e_d, double e_lost_d, double f_f, double f_e) {
        this.month = month;
        this.E_d = e_d;
        this.E_lost_d = e_lost_d;
        this.f_f = f_f;
        this.f_e = f_e;
    }
}
