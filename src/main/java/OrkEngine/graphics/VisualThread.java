/*
    Author: Grant Fields
    Date: 8/5/2020
 */

package OrkEngine.graphics;

import OrkEngine.Main;
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

        cowMesh.applyTransform(new TranslationMatrix(-500, 200,0), null);

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

        ArrayList<RasterizedTriangle> rasterizedTriangles = new ArrayList<>();

        Vector3d viewPoint = camera.getCameraPos().add(camera.getCameraForward());

        IntStream.range(0, meshes.size()).forEach(i -> {

            Mesh meshClone = meshes.get(i).clone();

            meshClone.faceCull(viewPoint);

            meshClone.applyTransform(cameraMatrix, vLight);
            meshClone.clipTriangles();
            meshClone.projection(rasterizedTriangles);
        });

        sort(rasterizedTriangles);

        frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

        setBackground(.6f, .6f, 1f, frame.getGraphics());

        ArrayList<DrawingThread> threads = new ArrayList<>();
        int totalThreadUsage = Main.threads;
        int blockSize = (rasterizedTriangles.size()/totalThreadUsage) + (rasterizedTriangles.size() % totalThreadUsage > 0 ? 1 : 0);

        for(int i = 0; i < totalThreadUsage; i++){
            threads.add(new DrawingThread(rasterizedTriangles, i * blockSize, (i + 1) * blockSize, WIDTH, HEIGHT));
        }
        for(DrawingThread thread: threads)
            frame.getGraphics().drawImage(thread.stop(), 0, 0, null);
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

    public void passDetails(float fTimeElapsed){

        WINDOW.passDetails(fTimeElapsed);
    }
}
