package vue.ui.actions;

import controller.PageController;
import modele.PVGISModel;
import modele.PVGISRequest;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class EstimateAction extends AbstractAction {
    private final PageController controller;
    private final PVGISRequest request;

    public EstimateAction(PageController controller, PVGISModel model, PVGISRequest request) {
        super("Estimer");
        this.controller = controller;
        this.request = request;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.estimateAsync(request);
    }
}
