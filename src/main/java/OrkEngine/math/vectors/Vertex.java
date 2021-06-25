/*
    Author: Grant Fields
    Date: 8/3/2020
 */

package OrkEngine.math.vectors;

import OrkEngine.math.matrices.Matrix4x4;

public class Vertex extends Vector3d implements Cloneable{

    private Vector3d vertexNormal = new Vector3d(0, 0, 0);
    private float brightnessAtrb = 0;

    public Vertex(float x, float y, float z) {
        super(x, y, z);
    }

    public Vertex(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    public Vertex(Vector3d v){

        super(v.getX(), v.getY(), v.getZ(), v.getW());
    }

    private Vertex(Vertex v){
        super(v.getX(), v.getY(), v.getZ(), v.getW());

        vertexNormal = v.getVertexNormal();
        brightnessAtrb = v.getBrightnessAtrb();
    }

    public Vertex scale(float s){

        x *= s;
        y *= s;
        z *= s;
        w *= s;

        return this;
    }

    public Vector3d getVector(){

        return new Vector3d(this.getX(), this.getY(), this.getZ(), this.getW());
    }

    public void calibrateNormal(Vector3d triNormal){

        vertexNormal = vertexNormal.add(triNormal).normalize();
    }

    public Vector3d getVertexNormal(){

        return vertexNormal;
    }

    public void transform(Matrix4x4 m){

        Vertex temp = m.multiplyVert(this);

        x = temp.getX();
        y = temp.getY();
        z = temp.getZ();
        w = temp.getW();
    }

    public void calcBrightness(Vector3d light){

        brightnessAtrb = getVertexNormal().normalize().dotProduct(light);

        if(brightnessAtrb < 0) {
            brightnessAtrb = 0;
        }
        else if(brightnessAtrb > 1){
            brightnessAtrb = 1;
        }
    }

    public float getBrightnessAtrb(){

        return brightnessAtrb;
    }

    @Override
    public Vertex clone() {

        return new Vertex(this);
    }
}
