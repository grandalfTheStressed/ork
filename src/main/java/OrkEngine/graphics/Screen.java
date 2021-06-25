/*
    Author: Grant Fields
    Date: 8/3/2020
 */

package OrkEngine.graphics;

import OrkEngine.math.vectors.Vector3d;
import OrkEngine.modeling.RasterizedTriangle;
import OrkEngine.math.matrices.ProjectionMatrix;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class Screen extends JPanel {

    //I just didn't want to have to keeping passing in the graphics object to the methods I made
    private Graphics graphics;

    private float fFPS = 0;
    private float fDeltaT = 0;

    private final int WIDTH;
    private final int HEIGHT;

    BufferedImage frame;

    public Screen(int width, int height){

        WIDTH = width;
        HEIGHT = height;
        frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        this.setPreferredSize(new Dimension(width, height));
    }

    public void render(BufferedImage frame){

        this.frame = frame;

        repaint();
    }

    public void passDetails(float deltaT){

        this.fFPS = (float)Math.ceil((1 / (deltaT)) * 100) / 100;
        this.fDeltaT = deltaT;
    }

    //temporary graphic just to keep track of this info
    public void drawRenderInfo(int x, int y){

        graphics.setColor(Color.black);
        graphics.fillRoundRect((x - 5) , y, 125, 60, 10, 10);

        graphics.setColor(Color.darkGray);
        graphics.fillRoundRect(x, (y + 5), 115, 50, 8 ,8);

        graphics.setColor(Color.yellow);
        graphics.drawString("Delta T: " + fDeltaT, (x + 5), (y + 25));
        graphics.drawString("FPS: " + fFPS, (x + 5), (y + 45));
    }

    public void paintComponent(Graphics g){

        this.graphics = g;

        super.paintComponent(this.graphics);

        graphics.drawImage(frame, 0, 0, null);

        drawRenderInfo((int)(WIDTH * .92), 20);
    }
}
