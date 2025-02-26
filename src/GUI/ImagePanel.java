package GUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * A JPanel that contains an Image.
 */
public class ImagePanel extends JPanel{
    private final Image image;

    /**
     * Instantiates an ImagePanel.
     *
     * @param path String with the image's path.
     */
    public ImagePanel(String path){
        image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(path))).getImage();
    }

    /**
     * Dynamically renders the image.
     *
     * @param g Graphical component of the panel.
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }
}
