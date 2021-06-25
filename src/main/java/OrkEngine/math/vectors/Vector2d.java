/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.vectors;

//this is for after a polygon has been projected from 3d to 2d space
//will be used for texturing and shading
public class Vector2d implements Comparable<Vector2d>{

    protected float u;
    protected float v;

    public Vector2d(float u, float v){

        this.u = u;
        this.v = v;
    }

    public float getU() {

        return u;
    }

    public float getV(){

        return v;
    }

    public Vector2d sub(Vector2d vec){

        float tempU = u - vec.getU();
        float tempV = v - vec.getV();

        return new Vector2d(tempU, tempV);
    }

    public Vector2d add(Vector2d vec){

        float tempU = u + vec.getU();
        float tempV = v + vec.getV();

        return new Vector2d(tempU, tempV);
    }

    public float findGradient(Vector2d vec){

        float dY = vec.getV() - v;
        float dX = vec.getU() - u;

        if( dY == 0 || dX == 0){

            return 0;
        }

        return (dY / dX);
    }

    public float length(){

        return (float)Math.sqrt(u * u + v * v);
    }

    public Vector2d normalize(){

        float l = length();

        return new Vector2d(u / l, v / l);
    }

    @Override
    public int compareTo(Vector2d vec){

        return Float.compare(vec.getV(), v);
    }
}
