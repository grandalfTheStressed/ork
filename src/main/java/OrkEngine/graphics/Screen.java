/*
    Author: Grant Fields
    Date: 8/3/2020
 */

package OrkEngine.graphics;

import OrkEngine.math.vectors.Vector3d;
import OrkEngine.modeling.RasterizedTriangle;
import OrkEngine.math.matrices.ProjectionMatrix;
import com.sun.management.OperatingSystemMXBean;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

class Screen extends JPanel {

    //I just didn't want to have to keeping passing in the graphics object to the methods I made
    private Graphics graphics;

    private float fFPS = 0;
    private float fDeltaT = 0;

    private final int WIDTH;
    private final int HEIGHT;

    ArrayList<String> renderInfo;

    BufferedImage frame;
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

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
    public void drawRenderInfo(int x, int y, ArrayList<String> renderInfo){

        graphics.setColor(Color.yellow);
        for(int i = 0; i < renderInfo.size(); i++)
            graphics.drawString(renderInfo.get(i), x, y + i * 20);
    }

    public void paintComponent(Graphics g){

        this.graphics = g;

        super.paintComponent(this.graphics);

        graphics.drawImage(frame, 0, 0, null);

        int cpuUsage = (int)(osBean.getSystemCpuLoad() * 100);
        long memTotal = (osBean.getTotalPhysicalMemorySize() / 1048576);
        long memUsage = memTotal - (osBean.getFreePhysicalMemorySize() / 1048576);

        renderInfo = new ArrayList<>();
        renderInfo.add("Delta T: " + fDeltaT);
        renderInfo.add("FPS: " + fFPS);
        renderInfo.add("CPU Usage: " + cpuUsage +"%");
        renderInfo.add("Threads: " + osBean.getAvailableProcessors());
        renderInfo.add("Memory: " + memUsage+"MB" + "/" + memTotal+"MB");

        drawRenderInfo(10, 20, renderInfo);
    }
}
