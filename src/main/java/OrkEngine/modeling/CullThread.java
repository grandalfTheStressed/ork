package OrkEngine.modeling;

import OrkEngine.math.vectors.Vector3d;

import java.util.ArrayList;

public class CullThread implements Runnable{

    private Thread thread;

    private ArrayList<Triangle> triangles;
    private Vector3d viewPoint;

    private boolean running;
    private int startIndex;

    final int END_INDEX;

    public CullThread(ArrayList<Triangle> triangles, Vector3d viewPoint, int startIndex, int END_INDEX){
        this.triangles = triangles;
        this.viewPoint = viewPoint;
        this.startIndex = startIndex;
        this.END_INDEX = END_INDEX;
        start();
    }

    @Override
    public void run() {
        for(; startIndex < END_INDEX && startIndex < triangles.size(); startIndex++)
            if(0 <= triangles.get(startIndex).getTriangleNormal().dotProduct(triangles.get(startIndex).getVerticesVectors()[0].sub(viewPoint)))
                triangles.get(startIndex).dontDraw();
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