package vue.ui.actions;

import controller.PageController;
import modele.PVGISResult;
import org.knowm.xchart.CategoryChart;
import vue.ui.GraphsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public final class ShowGraphsAction extends AbstractAction {
    private final PageController controller;
    private final PVGISResult result;
    private final GraphsPanel graphsPanel;

    public ShowGraphsAction(PageController controller, PVGISResult result, GraphsPanel graphsPanel) {
        super("Voir graphes");
        this.controller = controller;
        this.result = result;
        this.graphsPanel = graphsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<CategoryChart> charts = controller.buildPVCharts(result);
        graphsPanel.setCharts(charts);
    }
}
