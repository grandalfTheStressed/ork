package OrkEngine.modeling;

import OrkEngine.math.matrices.Matrix4x4;
import OrkEngine.math.vectors.Vector3d;
import OrkEngine.math.vectors.Vertex;

import java.util.ArrayList;

public class TransformThread implements Runnable{

    private Thread thread;

    private ArrayList<Vertex> vertices;
    private Matrix4x4 m;
    private Vector3d light;

    private boolean running;
    private int startIndex;

    final int END_INDEX;

    public TransformThread(ArrayList<Vertex> vertices, Matrix4x4 m, Vector3d light, int startIndex, int END_INDEX){

        this.vertices = vertices;
        this.m = m;
        this.light = light;
        this.startIndex = startIndex;
        this.END_INDEX = END_INDEX;
        start();
    }

    @Override
    public void run() {

        if(light != null)
            for(; startIndex < END_INDEX && startIndex < vertices.size(); startIndex++) {
                vertices.get(startIndex).calcBrightness(light);
                vertices.get(startIndex).transform(m);
            }
        else
            for(; startIndex < END_INDEX && startIndex < vertices.size(); startIndex++) {
                vertices.get(startIndex).transform(m);
            }
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
}