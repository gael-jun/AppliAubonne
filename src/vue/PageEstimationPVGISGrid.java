package vue;

import javax.swing.*;
import java.awt.*;

public class PageEstimationPVGISGrid extends JPanel {
    public PageEstimationPVGISGrid() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Formulaire pour PV couplé au réseau");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
