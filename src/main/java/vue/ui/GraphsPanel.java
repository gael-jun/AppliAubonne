package vue.ui;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class GraphsPanel extends JPanel {
    private final List<CategoryChart> charts = new ArrayList<>();

    public GraphsPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void setCharts(List<CategoryChart> newCharts) {
        charts.clear();
        removeAll();
        if (newCharts != null) charts.addAll(newCharts);
        for (CategoryChart c : charts) {
            add(new XChartPanel<>(c));
        }
        revalidate();
        repaint();
    }

    public List<BufferedImage> getChartImages() {
        List<BufferedImage> images = new ArrayList<>();
        for (CategoryChart c : charts) {
            try {
                images.add(BitmapEncoder.getBufferedImage(c));
            } catch (RuntimeException ignore) { }
        }
        return images;
    }
}
