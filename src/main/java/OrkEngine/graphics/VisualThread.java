/*
    Author: Grant Fields
    Date: 8/5/2020
 */

package OrkEngine.graphics;

import OrkEngine.math.matrices.Matrix4x4;
import OrkEngine.math.matrices.PitchMatrix;
import OrkEngine.math.matrices.TranslationMatrix;
import OrkEngine.math.matrices.YawMatrix;
import OrkEngine.math.vectors.Vector3d;
import OrkEngine.modeling.Mesh;
import OrkEngine.modeling.RasterizedTriangle;
import OrkEngine.tools.Camera;
import OrkEngine.tools.MeshLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.util.Collections.sort;

public class VisualThread implements Runnable{

    //used for getting computer's width and height dimensions
    //I don't know why the dimension needs to be resized like this, but if i don't the width
    //will have gaps and the height will stretch past the the taskbar
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public final int WIDTH = (int) ((SCREEN_SIZE.getWidth() ));
    public final int HEIGHT = (int) ((SCREEN_SIZE.getHeight() ));

    private final Window WINDOW = new Window(WIDTH, HEIGHT);

    private Thread thread;
    private boolean running = false;

    //Maximum screen refresh rate
    private final double FPS_LOCK = 60;

    //Nanoseconds required to pass before rendering a new frame.
    private final long UPDATE_CAP = (long) (1000000000L / (FPS_LOCK));

    private final long START_TIME = System.nanoTime();

    //used for calculating true fps
    private long lastTime = START_TIME;

    //update time is determines how ling from now the next frame should be created
    private long updateTime = START_TIME + UPDATE_CAP;

    BufferedImage frame;

    ArrayList<Mesh> meshes = new ArrayList<>();

    Vector3d vLight = (new Vector3d(1, 1, -1f,  1).normalize());

    public VisualThread(){

        MeshLoader meshLoader = new MeshLoader();

        Mesh terrainMesh = meshLoader.loadMesh("Terrain", "terrain.txt", 1f);
        Mesh cowMesh = meshLoader.loadMesh("Cow", "lowPolyCow.txt", .08f);

        cowMesh.applyTransform(new TranslationMatrix(-500, 200,0));

        meshes.add(terrainMesh);
        meshes.add(cowMesh);

        start();
    }

    public void render(){

        Camera camera = WINDOW.getCamera();

        //These have to be applied to all meshes to make them move through world space as 3d objects
        TranslationMatrix moveMatrix = new TranslationMatrix(-camera.getCameraPos().getX(), -camera.getCameraPos().getY(), -camera.getCameraPos().getZ());
        YawMatrix ym = new YawMatrix(camera.getYaw());
        PitchMatrix pm = new PitchMatrix(camera.getPitch());
        Matrix4x4 cameraMatrix = moveMatrix.multiply(ym.multiply(pm));

        ArrayList<Mesh> meshClones = new ArrayList<>();

        ArrayList<RasterizedTriangle> rasterizedTriangles = new ArrayList<>();

        Vector3d viewPoint = camera.getCameraPos().add(camera.getCameraForward());

        IntStream.range(0, meshes.size()).forEach(i -> {

            meshClones.add(meshes.get(i).clone());

            if(WINDOW.isLighting()){

                meshClones.get(i).applyLightingAndFaceCulling(vLight, viewPoint);
            }

            meshClones.get(i).applyTransform(cameraMatrix);
            meshClones.get(i).clipTriangles();
            meshClones.get(i).projection(rasterizedTriangles);
        });

        sort(rasterizedTriangles);

        frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        setBackground(.6f, .6f, 1f, frame.getGraphics());

        for(RasterizedTriangle tri: rasterizedTriangles){

            drawTriangle(tri, frame.getGraphics());
        }
    }

    public void run(){

        while(running){

            long currentTime = System.nanoTime();

            if(currentTime >= updateTime) {

                WINDOW.render(frame);
                passDetails(((float) (currentTime - lastTime) / 1000000000L));
                lastTime = currentTime;
                updateTime = currentTime + UPDATE_CAP;
                WINDOW.updateInput();

                render();
            }
        }

        stop();
    }

    public synchronized void start(){

        if(running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop(){

        if(!running) return;

        running = false;

        try{

            thread.join();
        }catch(InterruptedException e){

            e.printStackTrace();
        }
    }

    public void setBackground(float red, float green, float blue, Graphics graphics){

        graphics.setColor(new Color(red, green, blue));

        graphics.fillRect(0, 0, WIDTH, HEIGHT);
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

    public void passDetails(float fTimeElapsed){

        WINDOW.passDetails(fTimeElapsed);
    }
}
