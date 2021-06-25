/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.matrices;

public class RollMatrix extends Matrix4x4 {

    public RollMatrix(float fTheta){

        super();

        set(0, 0, (float) (Math.cos(fTheta)));
        set(0, 1, (float) - (Math.sin(fTheta)));
        set(1, 0, (float) (Math.sin(fTheta)));
        set(1, 1, (float) (Math.cos(fTheta)));
        set(2, 2, 1.0f);
        set(3, 3, 1.0f);
    }
}
