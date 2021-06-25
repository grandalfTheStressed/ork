/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.matrices;

public class YawMatrix extends Matrix4x4{

    public YawMatrix(float fTheta){

        super();

        set(0, 0, (float) Math.cos(fTheta));
        set(0, 2, (float) - Math.sin(fTheta));
        set(2, 0, (float) Math.sin(fTheta));
        set(1, 1, 1.0f);
        set(2, 2, (float) Math.cos(fTheta));
        set(3, 3, 1.0f);
    }
}
