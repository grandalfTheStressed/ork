/*
    Author: Grant Fields
    Date: 8/3/2020
 */

package OrkEngine.math.matrices;

import OrkEngine.math.vectors.Vector3d;
import OrkEngine.math.vectors.Vertex;

public class Matrix4x4 {

    private final float[][] matrix;

    public Matrix4x4(){

        matrix = new float[4][4];
    }

    public float get(int row, int column){

        return matrix[row][column];
    }

    public void set(int row, int column, float val){

        matrix[row][column] = val;
    }

    public Matrix4x4 multiply(Matrix4x4 m){

        Matrix4x4 temp = new Matrix4x4();

        for(int row = 0; row < 4; row++){

            for(int column = 0; column < 4; column++){

                temp.set(row, column,
                        matrix[row][0] * m.get(0, column) +
                            matrix[row][1] * m.get(1, column) +
                            matrix[row][2] * m.get(2, column) +
                            matrix[row][3] * m.get(3, column));
            }
        }

        return temp;
    }

    public Vector3d multiplyVec(Vector3d vectorIn){

        float fX;
        float fY;
        float fZ;
        float fW;

        fX = vectorIn.getX() * matrix[0][0] + vectorIn.getY() * matrix[1][0] + vectorIn.getZ() * matrix[2][0] + vectorIn.getW() * matrix[3][0];

        fY = vectorIn.getX() * matrix[0][1] + vectorIn.getY() * matrix[1][1] + vectorIn.getZ() * matrix[2][1] + vectorIn.getW() * matrix[3][1];

        fZ = vectorIn.getX() * matrix[0][2] + vectorIn.getY() * matrix[1][2] + vectorIn.getZ() * matrix[2][2] + vectorIn.getW() * matrix[3][2];

        fW = vectorIn.getX() * matrix[0][3] + vectorIn.getY() * matrix[1][3] + vectorIn.getZ() * matrix[2][3] + vectorIn.getW() * matrix[3][3];

        return new Vector3d(fX, fY, fZ, fW);
    }

    public Vertex multiplyVert(Vertex vectorIn){

        float fX;
        float fY;
        float fZ;
        float fW;

        fX = vectorIn.getX() * matrix[0][0] + vectorIn.getY() * matrix[1][0] + vectorIn.getZ() * matrix[2][0] + vectorIn.getW() * matrix[3][0];

        fY = vectorIn.getX() * matrix[0][1] + vectorIn.getY() * matrix[1][1] + vectorIn.getZ() * matrix[2][1] + vectorIn.getW() * matrix[3][1];

        fZ = vectorIn.getX() * matrix[0][2] + vectorIn.getY() * matrix[1][2] + vectorIn.getZ() * matrix[2][2] + vectorIn.getW() * matrix[3][2];

        fW = vectorIn.getX() * matrix[0][3] + vectorIn.getY() * matrix[1][3] + vectorIn.getZ() * matrix[2][3] + vectorIn.getW() * matrix[3][3];

        return new Vertex(fX, fY, fZ, fW);
    }

    @Override
    public String toString() {

        String out = "";

        out += matrix[0][0] + " " + matrix[0][1] + " " + matrix[0][2] + " " + matrix[0][3] + "\n";
        out += matrix[1][0] + " " + matrix[1][1] + " " + matrix[1][2] + " " + matrix[1][3] + "\n";
        out += matrix[2][0] + " " + matrix[2][1] + " " + matrix[2][2] + " " + matrix[2][3] + "\n";
        out += matrix[3][0] + " " + matrix[3][1] + " " + matrix[3][2] + " " + matrix[3][3] + "\n";

        return out;
    }
}

