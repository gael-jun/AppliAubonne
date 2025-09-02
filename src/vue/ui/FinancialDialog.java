package vue.ui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Petite classe réutilisable pour construire le dialogue financier.
 */
public class FinancialDialog {
    private final JDialog dialog;
    private final JTextField investissementField = new JTextField();
    private final JTextField subventionField = new JTextField();
    private final JTextField prixVenteField = new JTextField();
    private final JTextField tauxInjectionField = new JTextField();
    private final JTextField coutAnnuelField = new JTextField();
    private final JTextField dureeField = new JTextField();
    private final JTextField tauxActualisationField = new JTextField();
    private final JTextField anneeDepartField = new JTextField();

    public FinancialDialog(Window owner) {
        dialog = new JDialog(owner, "Entrées financières", ModalityType.APPLICATION_MODAL);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Investissement initial (€) :")); panel.add(investissementField);
        panel.add(new JLabel("Subvention (€) :")); panel.add(subventionField);
        panel.add(new JLabel("Prix de vente (€/kWh) :")); panel.add(prixVenteField);
        panel.add(new JLabel("Taux d'injection/autoconsommation (0-1) :")); panel.add(tauxInjectionField);
        panel.add(new JLabel("Coût annuel d'exploitation (€) :")); panel.add(coutAnnuelField);
        panel.add(new JLabel("Durée du projet (années) :")); panel.add(dureeField);
        panel.add(new JLabel("Taux d'actualisation (%) :")); panel.add(tauxActualisationField);
        panel.add(new JLabel("Année de départ :")); panel.add(anneeDepartField);

        JButton tracerButton = new JButton("Tracer les graphes");
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(6, 12, 12, 12));
        bottomPanel.add(tracerButton);

        dialog.getContentPane().add(panel, BorderLayout.CENTER);
        dialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);

        tracerButton.addActionListener(e -> {
            // Default action: close for now. Caller can read fields via getters.
            dialog.setVisible(false);
        });
    }

    public void show() {
        dialog.setVisible(true);
    }

    public String getInvestissement() { return investissementField.getText(); }
    public String getSubvention() { return subventionField.getText(); }
    public String getPrixVente() { return prixVenteField.getText(); }
    public String getTauxInjection() { return tauxInjectionField.getText(); }
    public String getCoutAnnuel() { return coutAnnuelField.getText(); }
    public String getDuree() { return dureeField.getText(); }
    public String getTauxActualisation() { return tauxActualisationField.getText(); }
    public String getAnneeDepart() { return anneeDepartField.getText(); }
}
