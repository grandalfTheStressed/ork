/*
    Author: Grant Fields
    Date: 8/5/2020
 */

package OrkEngine.modeling;

import OrkEngine.math.matrices.ProjectionMatrix;
import OrkEngine.math.vectors.Vector3d;
import OrkEngine.math.vectors.Vertex;

import java.util.ArrayList;

public class Triangle implements Comparable<Triangle>, Cloneable{

    private final int[] vertKeys = new int[3];
    private final Vertex[] vertices = new Vertex[3];
    private final Vector3d normal;

    private float red = 1;
    private float green = 1;
    private float blue = 1;
    private float brightness = 1;
    private float alpha = 1;

    private boolean draw = true;

    public Triangle(int vert1, int vert2, int vert3, ArrayList<Vertex> v){

        vertKeys[0] = vert1;
        vertKeys[1] = vert2;
        vertKeys[2] = vert3;

        vertices[0] = v.get(vertKeys[0]);
        vertices[1] = v.get(vertKeys[1]);
        vertices[2] = v.get(vertKeys[2]);

        Vector3d line1 = vertices[0].sub(vertices[1]);
        Vector3d line2 = vertices[0].sub(vertices[2]);

        normal = line1.crossProduct(line2).normalize();

        vertices[0].calibrateNormal(normal);
        vertices[1].calibrateNormal(normal);
        vertices[2].calibrateNormal(normal);
    }

    private Triangle(Triangle t, ArrayList<Vertex> v){

        vertKeys[0] = t.getVertKeys()[0];
        vertKeys[1] = t.getVertKeys()[1];
        vertKeys[2] = t.getVertKeys()[2];

        vertices[0] = v.get(vertKeys[0]);
        vertices[1] = v.get(vertKeys[1]);
        vertices[2] = v.get(vertKeys[2]);

        red = t.getRed();
        green = t.getGreen();
        blue = t.getBlue();
        brightness = t.getBrightness();
        alpha = t.getAlpha();

        normal = t.getTriangleNormal();
    }

    public int[] getVertKeys(){

        return vertKeys;
    }

    public Vertex[] getVertices(){

        return vertices;
    }

    public Vector3d[] getVerticesVectors(){

        Vector3d[] temp = new Vector3d[3];

        temp[0] = vertices[0].getVector();
        temp[1] = vertices[1].getVector();
        temp[2] = vertices[2].getVector();

        return temp;
    }

    public boolean isDraw(){

        return draw;
    }

    public void dontDraw(){

        draw = false;
    }

    public float getRed(){

        return red;
    }

    public float getGreen(){

        return green;
    }

    public float getBlue(){

        return blue;
    }

    public float getBrightness(){

        return (vertices[0].getBrightnessAtrb() + vertices[1].getBrightnessAtrb() + vertices[2].getBrightnessAtrb()) / 3;
    }

    public float getAlpha(){

        return alpha;
    }

    public void setColor(float red, float green, float blue, float brightness, float alpha){

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.brightness = brightness;
        this.alpha = alpha;
    }

    public Vector3d getTriangleNormal(){

        return normal;
    }

    public boolean withinRenderDistance(){

        return vertices[0].getZ() < ProjectionMatrix.fFar;
    }

    //the triangles are using a their 3 z values to decide the order they get drawn in
    //I tried doing a z-buffer but it took to much of my cpu and this works really well for what
    //I'm trying to accomplish
    @Override
    public int compareTo(Triangle t) {

        if(!this.draw && !t.isDraw()){

            return 0;
        }
        else if(this.draw && !t.isDraw()){

            return -1;
        }
        else if(!this.draw && t.isDraw()){

            return 1;
        }
        else {

            Vector3d[] temp = t.getVertices();

            float z1 = vertices[0].getZ() + vertices[1].getZ() + vertices[2].getZ();

            float z2 = temp[0].getZ() + temp[1].getZ() + temp[2].getZ();

            return Float.compare(z2, z1);
        }
    }

    public Triangle clone(ArrayList<Vertex> v) {

        try{

            super.clone();
        }
        catch(CloneNotSupportedException e){

            e.printStackTrace();
        }

        return new Triangle(this, v);
    }

    @Override
    public String toString() {

        return "Vertex 1: " + vertices[0].getX() + " " + vertices[0].getY() + " " + vertices[0].getZ() + " \n" +
                "Vertex 2: " + vertices[1].getX() + " " + vertices[1].getY() + " " + vertices[1].getZ() + " \n" +
                "Vertex 3: " + vertices[2].getX() + " " + vertices[2].getY() + " " + vertices[2].getZ() + " \n";
    }
}
