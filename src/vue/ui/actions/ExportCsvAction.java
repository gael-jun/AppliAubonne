package vue.ui.actions;

import controller.PageController;
import export.ExportContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public final class ExportCsvAction extends AbstractAction {
    private final PageController controller;
    private final ExportContext context;
    private final java.awt.Component parent;

    public ExportCsvAction(PageController controller, ExportContext context, java.awt.Component parent) {
        super("Exporter CSV");
        this.controller = controller;
        this.context = context;
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Enregistrer le CSV");
        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith(".csv")) f = new File(f.getAbsolutePath()+".csv");
        try {
            controller.exportCsv(f, context);
            JOptionPane.showMessageDialog(parent, "CSV exporté", "OK", JOptionPane.INFORMATION_MESSAGE);
    } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Erreur export CSV", JOptionPane.ERROR_MESSAGE);
        }
    }
}
