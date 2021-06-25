/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.matrices;

public class PitchMatrix extends Matrix4x4{

    public PitchMatrix(float fTheta){

        super();

        set(0, 0, 1);
        set(1, 1, (float) (Math.cos(fTheta)));
        set(1, 2, (float) (-Math.sin(fTheta)));
        set(2, 1, (float) (Math.sin(fTheta)));
        set(2, 2, (float) (Math.cos(fTheta)));
        set(3, 3, 1.0f);
    }
}
