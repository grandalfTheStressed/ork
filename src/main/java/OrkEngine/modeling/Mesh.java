/*
    Author: Grant Fields
    Date: 8/5/2020
 */

package OrkEngine.modeling;

import OrkEngine.Main;
import OrkEngine.math.matrices.Matrix4x4;
import OrkEngine.math.matrices.ProjectionMatrix;
import OrkEngine.math.vectors.Vector3d;
import OrkEngine.math.vectors.Vertex;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.util.Collections.sort;

//Mesh stores an arraylist for triangles, the scale of the object, and the
//file destination for whatever object file you want to load in
public class Mesh implements Cloneable{

    public static final Vector3d X_AXIS = new Vector3d(1, 0, 0, 1);
    public static final Vector3d Y_AXIS = new Vector3d(0, 1, 0, 1);
    public static final Vector3d Z_AXIS = new Vector3d(0, 0, 1, 1);

    private final String sTitle;
    private final String sFilePath;
    private final float fScale;

    private final ArrayList<Triangle> triangles;
    private final ArrayList<Vertex> vertices;

    public Mesh(String sTitle, String sFilePath, float fScale, ArrayList<Vertex> vertices, ArrayList<Triangle> triangles){

        this.sTitle = sTitle;
        this.sFilePath = sFilePath;
        this.fScale = fScale;
        this.vertices = vertices;
        this.triangles = triangles;
    }

    public ArrayList<Triangle> getTriangles(){

        return triangles;
    }

    public ArrayList<Vertex> getVerts(){

        return vertices;
    }

    public String getTitle(){

        return sTitle;
    }

    public String getMeshPath(){

        return sFilePath;
    }

    public float getScale(){

        return fScale;
    }

    public void paintersAlgorithm(){

        sort(triangles);
    }

    public void faceCull(Vector3d viewPoint){

        ArrayList<CullThread> threads = new ArrayList<>();
        int totalThreadUsage = Main.threads;
        int blockSize = (triangles.size()/totalThreadUsage) + (triangles.size() % totalThreadUsage > 0 ? 1 : 0);

        for(int i = 0; i < totalThreadUsage; i++){
            threads.add(new CullThread(triangles, viewPoint,i * blockSize, (i + 1) * blockSize));
        }
        for(CullThread thread: threads)
            thread.stop();
    }

    public void applyTransform(Matrix4x4 m, Vector3d light){

        ArrayList<TransformThread> threads = new ArrayList<>();
        int totalThreadUsage = Main.threads;
        int blockSize = (vertices.size()/totalThreadUsage) + (vertices.size() % totalThreadUsage > 0 ? 1 : 0);

        for(int i = 0; i < totalThreadUsage; i++){
            threads.add(new TransformThread(vertices, m, light,i * blockSize, (i + 1) * blockSize));
        }
        for(TransformThread thread: threads)
            thread.stop();
    }

    public boolean viewPlaneCullTri(Triangle t){

        Vector3d[] verts = new Vector3d[3];

        verts[0] = t.getVerticesVectors()[0];
        verts[1] = t.getVerticesVectors()[1];
        verts[2] = t.getVerticesVectors()[2];

        if(verts[0].getX() > verts[0].getW() && verts[1].getX() > verts[1].getW() && verts[2].getX() > verts[2].getW()){
            t.dontDraw();
            return false;
        }

        if(verts[0].getX() < -verts[0].getW() && verts[1].getX() < -verts[1].getW() && verts[2].getX() < -verts[2].getW()){
            t.dontDraw();
            return false;
        }

        if(verts[0].getY() > verts[0].getW() && verts[1].getY() > verts[1].getW() && verts[2].getY() > verts[2].getW()){
            t.dontDraw();
            return false;
        }

        if(verts[0].getY() < -verts[0].getW() && verts[1].getY() < -verts[1].getW() && verts[2].getY() < -verts[2].getW()){
            t.dontDraw();
            return false;
        }

        if(verts[0].getZ() > verts[0].getW() && verts[1].getZ() > verts[1].getW() && verts[2].getZ() > verts[2].getW()){
            t.dontDraw();
            return false;
        }

        if ((verts[0].getZ() < -verts[0].getW()) && (verts[1].getZ() < -verts[1].getW()) && (verts[2].getZ() < -verts[2].getW())) {
            t.dontDraw();
            return false;
        }

        return true;
    }

    public void projection(ArrayList<RasterizedTriangle> rt){

        ProjectionMatrix project = new ProjectionMatrix();

        applyTransform(project, null);

        for(Triangle t: triangles)
            if(t.isDraw())
                if(viewPlaneCullTri(t)) {
                    finishProjection(t);
                    rt.add(new RasterizedTriangle(t.getVertices()[0], t.getVertices()[1], t.getVertices()[2], t));
                }
    }

    public void finishProjection(Triangle t){

        Vertex[] verts = t.getVertices();

        verts[0].scale(1 / verts[0].getW());
        verts[1].scale(1 / verts[1].getW());
        verts[2].scale(1 / verts[2].getW());
    }

    //this is hard to explain why this works without pictures.
    //general idea is we can decide if a line is intersecting a plane, where it intersects
    //based on the the planes normal, the positions of these objects in 3d space, and how they're projecting onto each other
    public Vertex vectorIntersectPlane(Vector3d planePos, Vector3d planeNorm, Vector3d lineStart, Vector3d lineEnd){

        float planeDot = planeNorm.dotProduct(planePos);
        float startDot = lineStart.dotProduct(planeNorm);
        float endDot = lineEnd.dotProduct(planeNorm);
        float midPoint = (planeDot - startDot) / (endDot - startDot);

        Vector3d lineStartEnd = lineEnd.sub(lineStart);
        Vector3d lineToIntersect = lineStartEnd.scale(midPoint);

        return new Vertex(lineStart.add(lineToIntersect));
    }

    public float distanceFromPlane(Vector3d planePos, Vector3d planeNorm, Vector3d vert){

        return (planeNorm.dotProduct(vert) - planeNorm.dotProduct(planePos));
    }

    //When a triangle gets clipped it has 4 possible outcomes
    // 1 it doesn't actually need clipping and gets returned
    // 2 it gets clipped into 1 new triangle, for testing these are red
    // 3 it gets clipped into 2 new triangles, for testing 1 is green, and 1 is blue
    // 4 it is outside the view planes and shouldn't be rendered
    public void clipTriangles(){

        Vector3d planePos = new Vector3d(0, 0, ProjectionMatrix.fNear, 1f);
        Vector3d planeNorm = Z_AXIS.clone();

        final int length = triangles.size();

        for(int i = 0; i < length; i++) {

            Triangle t = triangles.get(i);

            if(!t.isDraw())
                continue;

            int[] insidePoint = new int[3];
            int insidePointCount = 0;

            int[] outsidePoint = new int[3];
            int outsidePointCount = 0;

            float d0 = distanceFromPlane(planePos, planeNorm, t.getVerticesVectors()[0]);
            float d1 = distanceFromPlane(planePos, planeNorm, t.getVerticesVectors()[1]);
            float d2 = distanceFromPlane(planePos, planeNorm, t.getVerticesVectors()[2]);

            //Storing distances from plane and counting inside outside points
            {
                if (d0 >= 0){

                    insidePoint[insidePointCount] = t.getVertKeys()[0];
                    insidePointCount++;
                }else{

                    outsidePoint[outsidePointCount] = t.getVertKeys()[0];
                    outsidePointCount++;
                }
                if (d1 >= 0){

                    insidePoint[insidePointCount] = t.getVertKeys()[1];
                    insidePointCount++;
                }else{

                    outsidePoint[outsidePointCount] = t.getVertKeys()[1];
                    outsidePointCount++;
                }
                if (d2 >= 0){

                    insidePoint[insidePointCount] = t.getVertKeys()[2];
                    insidePointCount++;
                }else{

                    outsidePoint[outsidePointCount] = t.getVertKeys()[2];
                }
            }

            if (insidePointCount == 1) {

                t.dontDraw();

                Vertex newVert1 = vectorIntersectPlane(planePos, planeNorm, vertices.get(outsidePoint[0]),vertices.get(insidePoint[0]));
                Vertex newVert2 = vectorIntersectPlane(planePos, planeNorm, vertices.get(outsidePoint[1]),vertices.get(insidePoint[0]));
                vertices.add(newVert1);
                vertices.add(newVert2);

                Triangle temp = new Triangle(insidePoint[0], vertices.size() - 2, vertices.size() - 1, vertices);
                temp.setColor(1,0,0, t.getBrightness(), t.getAlpha());
                triangles.add(temp);

                continue;
            }

            if (insidePointCount == 2) {

                t.dontDraw();

                Vertex newVert1 = vectorIntersectPlane(planePos, planeNorm, vertices.get(outsidePoint[0]),vertices.get(insidePoint[0]));
                Vertex newVert2 = vectorIntersectPlane(planePos, planeNorm, vertices.get(outsidePoint[0]),vertices.get(insidePoint[1]));
                vertices.add(newVert1);
                vertices.add(newVert2);

                Triangle temp = new Triangle(insidePoint[0], insidePoint[1], vertices.size() - 1, vertices);
                temp.setColor(0, 1, 0, t.getBrightness(), t.getAlpha());
                triangles.add(temp);

                temp = new Triangle(insidePoint[0], vertices.size() - 1, vertices.size() - 2, vertices);
                temp.setColor(0, 0, 1, t.getBrightness(), t.getAlpha());
                triangles.add(temp);

                continue;
            }
        }
    }

    @Override
    public Mesh clone(){

        try{

            super.clone();
        }
        catch(CloneNotSupportedException e){

            e.printStackTrace();
        }

        ArrayList<Vertex> tempVert = new ArrayList<>();

        IntStream.range(0, vertices.size()).forEach(i -> tempVert.add(vertices.get(i).clone()));

        ArrayList<Triangle> tempTris = new ArrayList<>();

        IntStream.range(0, triangles.size()).forEach(i -> {

            tempTris.add(triangles.get(i).clone(tempVert));

            tempTris.get(i).setColor(triangles.get(i).getRed(),
                                    triangles.get(i).getGreen(),
                                    triangles.get(i).getBlue(),
                                    triangles.get(i).getBrightness(),
                                    triangles.get(i).getAlpha());
        });

        return new Mesh(sTitle, sFilePath, fScale, tempVert, tempTris);
    }
}