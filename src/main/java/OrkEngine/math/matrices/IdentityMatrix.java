/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.matrices;

public class IdentityMatrix extends Matrix4x4{

    public IdentityMatrix(){

        //This ones wrote out to use as a template for more complex matrices
        super();

        set(0, 0, 1); set(0, 1, 0); set(0, 2, 0); set(0, 3, 0);

        set(1, 0, 0); set(1, 1, 1); set(1, 2, 0); set(1, 3, 0);

        set(2, 0, 0); set(2, 1, 0); set(2, 2, 1); set(2, 3, 0);

        set(3, 0, 0); set(3, 1, 0); set(3, 2, 0); set(3, 3, 1);
    }
}
