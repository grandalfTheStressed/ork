/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.vectors;

public class Vector3d implements Cloneable{

    protected float x;
    protected float y;
    protected float z;
    protected float w;

    public Vector3d(float x, float y, float z){

        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    public Vector3d(float x, float y, float z, float w){

        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float length(){

        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float getX(){

        return x;
    }

    public float getY(){

        return y;
    }

    public float getZ(){

        return z;
    }

    public float getW(){

        return w;
    }

    public Vector3d scale(float s){

     float tempX = x * s;
     float tempY = y * s;
     float tempZ = z * s;
     float tempW = w * s;

        return new Vector3d(tempX, tempY, tempZ, tempW);
    }

    public Vector3d normalize(){

        float l = length();

        return new Vector3d(x / l, y / l, z / l);
    }

    public Vector3d crossProduct(Vector3d vec){

        float fTempX = y * vec.getZ() - z * vec.getY();
        float fTempY = z * vec.getX() - x * vec.getZ();
        float fTempZ = x * vec.getY() - y * vec.getX();

        return new Vector3d(fTempX, fTempY, fTempZ).normalize();
    }

    public float dotProduct(Vector3d vec){

        return x * vec.getX() + y * vec.getY() + z * vec.getZ();
    }

    public Vector3d add(Vector3d v){

        return new Vector3d(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    public Vector3d sub(Vector3d v){

        return new Vector3d(x - v.getX(), y - v.getY(), z - v.getZ());
    }

    @Override
    public Vector3d clone(){

        try{

            super.clone();

        }catch(CloneNotSupportedException e){

            e.printStackTrace();
        }

        return new Vector3d(x, y, z, w);
    }

    @Override
    public String toString() {

        String out = "Vector Components- x: " + x + " | y: " + y + " | z: " + z;

        return out;
    }
}
