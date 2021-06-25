package OrkEngine.modeling;

import OrkEngine.math.vectors.Vector3d;
import OrkEngine.math.vectors.Vertex;

public class RasterizedTriangle implements Comparable<RasterizedTriangle>{

    private Vertex[] vertices = new Vertex[3];

    private float red;
    private float green;
    private float blue;
    private float brightness;
    private float alpha;

    public RasterizedTriangle(Triangle t){

        vertices[0] = t.getVertices()[0];
        vertices[1] = t.getVertices()[1];
        vertices[2] = t.getVertices()[2];

        red = t.getRed();
        green = t.getGreen();
        blue = t.getBlue();
        brightness = t.getBrightness();
        alpha = t.getAlpha();
    }

    public RasterizedTriangle(Vertex v1, Vertex v2, Vertex v3, Triangle t){

        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;

        red = t.getRed();
        green = t.getGreen();
        blue = t.getBlue();
        brightness = t.getBrightness();
        alpha = t.getAlpha();
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

        return brightness;
    }

    public float getAlpha(){

        return alpha;
    }

    @Override
    public int compareTo(RasterizedTriangle t) {

        Vector3d[] temp = t.getVertices();
        float z1 = vertices[0].getZ() + vertices[1].getZ() + vertices[2].getZ();
        float z2 = temp[0].getZ() + temp[1].getZ() + temp[2].getZ();
        return Float.compare(z2, z1);

    }
}
