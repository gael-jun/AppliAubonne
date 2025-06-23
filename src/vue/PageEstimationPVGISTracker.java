package vue;

import javax.swing.*;
import java.awt.*;

public class PageEstimationPVGISTracker extends JPanel {
    public PageEstimationPVGISTracker() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Formulaire pour PV suiveur");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}
