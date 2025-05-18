package view;

import javax.swing.*;
import java.awt.*;

public class JPanelImage extends JPanel {
    private Image background;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public JPanelImage(String path, int width, int height) {
        this.width = width;
        this.height = height;
        setOpaque(false);
        background = new  ImageIcon(path).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, this);
    }
}
