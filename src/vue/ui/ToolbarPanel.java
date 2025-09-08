package vue.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public final class ToolbarPanel extends JPanel {
    private final JButton estimateBtn = new JButton("Estimer");
    private final JButton graphsBtn = new JButton("Voir graphes");
    private final JButton exportBtn = new JButton("Export");
    private final JMenuItem exportCsv = new JMenuItem("Exporter en CSV");
    private final JMenuItem exportPdf = new JMenuItem("Exporter en PDF");

    public ToolbarPanel() {
        setLayout(new BorderLayout());
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.add(estimateBtn);
        tb.add(graphsBtn);
        tb.addSeparator();

        JPopupMenu menu = new JPopupMenu();
        menu.add(exportCsv);
        menu.add(exportPdf);
        exportBtn.addActionListener(e -> menu.show(exportBtn, 0, exportBtn.getHeight()));
        tb.add(exportBtn);

        add(tb, BorderLayout.CENTER);
        setEnabledState(false);
    }

    public void onEstimate(ActionListener al) { estimateBtn.addActionListener(al); }
    public void onGraphs(ActionListener al) { graphsBtn.addActionListener(al); }
    public void onExportCsv(ActionListener al) { exportCsv.addActionListener(al); }
    public void onExportPdf(ActionListener al) { exportPdf.addActionListener(al); }

    public void setEnabledState(boolean hasResult) {
        graphsBtn.setEnabled(hasResult);
        exportBtn.setEnabled(hasResult);
        exportCsv.setEnabled(hasResult);
        exportPdf.setEnabled(hasResult);
    }
}
