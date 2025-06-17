package vue.cablesetprotections;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel personnalisé pour afficher une image avec mise à l'échelle.
 */
class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image image;
    private Dimension imageSize;

    /**
     * Constructeur pour initialiser le panneau avec une image.
     * 
     * @param imageIcon L'icône de l'image à afficher.
     */
    public ImagePanel(ImageIcon imageIcon) {
        this.image = imageIcon.getImage();
        this.imageSize = new Dimension(image.getWidth(null), image.getHeight(null));
        setPreferredSize(new Dimension(600, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            double imageAspectRatio = (double) imageSize.width / imageSize.height;
            double panelAspectRatio = (double) getWidth() / getHeight();
            int newWidth, newHeight;
            if (panelAspectRatio > imageAspectRatio) {
                newWidth = (int) (getHeight() * imageAspectRatio);
                newHeight = getHeight();
            } else {
                newWidth = getWidth();
                newHeight = (int) (getWidth() / imageAspectRatio);
            }
            int x = (getWidth() - newWidth) / 2;
            int y = (getHeight() - newHeight) / 2;

            // Dessiner l'image redimensionnée et centrée
            g.drawImage(image, x, y, newWidth, newHeight, this);
        }
    }
}
