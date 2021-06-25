package OrkEngine.graphics;

import OrkEngine.math.vectors.Vector3d;
import OrkEngine.modeling.RasterizedTriangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawingThread implements Runnable{

    private Thread thread;

    private ArrayList<RasterizedTriangle> tris;

    private boolean running;
    private int startIndex;
    private BufferedImage frameChunk;
    final int WIDTH;
    final int HEIGHT;

    final int END_INDEX;

    public DrawingThread(ArrayList<RasterizedTriangle> tris, int startIndex, int END_INDEX, int WIDTH, int HEIGHT){

        this.tris = tris;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.frameChunk = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        this.startIndex = startIndex;
        this.END_INDEX = END_INDEX;
        start();
    }

    @Override
    public void run() {
        for(; startIndex < END_INDEX && startIndex < tris.size(); startIndex++)
            drawTriangle(tris.get(startIndex), frameChunk.getGraphics());
    }

    public synchronized void start(){

        if(running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized BufferedImage stop(){

        if(!running) return frameChunk;

        running = false;

        try{
            thread.join();
        }catch(InterruptedException e){

            e.printStackTrace();
        }

        return frameChunk;
    }

    public Color getColor(float red, float green, float blue, float brightness, float alpha){

        return new Color(red * brightness, green * brightness, blue * brightness, alpha);
    }

    public void drawTriangle(RasterizedTriangle t, Graphics graphics){

        Vector3d[] verts = t.getVerticesVectors();

        int[] x = new int[3];
        int[] y = new int[3];

        x[0] = (int)((verts[0].getX() + 1) * .5f * WIDTH);
        x[1] = (int)((verts[1].getX() + 1) * .5f * WIDTH);
        x[2] = (int)((verts[2].getX() + 1) * .5f * WIDTH);

        y[0] = (int)((verts[0].getY() + 1) * .5f * HEIGHT);
        y[1] = (int)((verts[1].getY() + 1) * .5f * HEIGHT);
        y[2] = (int)((verts[2].getY() + 1) * .5f * HEIGHT);

        graphics.setColor(getColor(t.getRed(), t.getGreen(), t.getBlue(), t.getBrightness(), t.getAlpha()));

        
        graphics.fillPolygon(x, y, 3);
    }
}
