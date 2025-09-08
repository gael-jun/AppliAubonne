package vue.ui;

import javax.swing.*;
import java.awt.*;
import modele.PVGISRequest;

public final class PVGISFormPanel extends JPanel {
    private final JTextField lat = new JTextField("48.989");
    private final JTextField lon = new JTextField("2.277");
    private final JComboBox<String> db = new JComboBox<>(new String[]{"PVGIS-SARAH3", "PVGIS-ERA5"});
    private final JTextField peak = new JTextField("6000");
    private final JTextField angle = new JTextField("0");
    private final JTextField aspect = new JTextField("0");
    private final JTextField battery = new JTextField("10000");
    private final JTextField cutoff = new JTextField("20");
    private final JTextField consumption = new JTextField("2000");
    private final JTextField hourly = new JTextField("");
    private final JCheckBox useHorizon = new JCheckBox("Inclure horizon", true);
    private final JTextField userHorizon = new JTextField("");
    private final JTextField output = new JTextField("json");
    private final JCheckBox browser = new JCheckBox("Browser", false);

    public PVGISFormPanel() {
        setLayout(new GridLayout(0, 2, 6, 4));
        add(new JLabel("Latitude :*")); add(lat);
        add(new JLabel("Longitude :*")); add(lon);
        add(new JLabel("Base de données :")); add(db);
        add(new JLabel("Puissance PV crête (W) :*")); add(peak);
        add(new JLabel("Inclinaison (°) :")); add(angle);
        add(new JLabel("Azimut (°) :")); add(aspect);
        add(new JLabel("Capacité batterie (Wh) :*")); add(battery);
        add(new JLabel("Limite de décharge (%) :*")); add(cutoff);
        add(new JLabel("Conso jour (Wh) :*")); add(consumption);
        add(new JLabel("Profil horaire (24 valeurs) :")); add(hourly);
        add(new JLabel("Inclure horizon :")); add(useHorizon);
        add(new JLabel("Horizon utilisateur :")); add(userHorizon);
        add(new JLabel("Format sortie :")); add(output);
        add(new JLabel("Browser :")); add(browser);
    }

    public PVGISRequest toRequest() {
        return new PVGISRequest(
                lat.getText(), lon.getText(), peak.getText(), battery.getText(),
                cutoff.getText(), consumption.getText(), angle.getText(), aspect.getText(),
                (String) db.getSelectedItem(), useHorizon.isSelected(), userHorizon.getText(),
                hourly.getText(), output.getText(), browser.isSelected()
        );
    }

    public boolean validateForm(Component parent) {
        try {
            Double.parseDouble(lat.getText());
            Double.parseDouble(lon.getText());
            Double.parseDouble(peak.getText());
            Double.parseDouble(battery.getText());
            Double.parseDouble(cutoff.getText());
            Double.parseDouble(consumption.getText());
            Double.parseDouble(angle.getText());
            Double.parseDouble(aspect.getText());
            return true;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Champs numériques invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Getters (exposés si besoin)
    public JTextField getLatField() { return lat; }
    public JTextField getLonField() { return lon; }
    public JComboBox<String> getDbCombo() { return db; }
    public JTextField getPeakField() { return peak; }
    public JTextField getAngleField() { return angle; }
    public JTextField getAspectField() { return aspect; }
    public JTextField getBatteryField() { return battery; }
    public JTextField getCutoffField() { return cutoff; }
    public JTextField getConsumptionField() { return consumption; }
    public JTextField getHourlyField() { return hourly; }
    public JCheckBox getUseHorizonCheck() { return useHorizon; }
    public JTextField getUserHorizonField() { return userHorizon; }
    public JTextField getOutputField() { return output; }
    public JCheckBox getBrowserCheck() { return browser; }
}
