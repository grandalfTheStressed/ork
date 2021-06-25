/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.matrices;

public class TranslationMatrix extends Matrix4x4{

    public TranslationMatrix(float x, float y, float z){

        super();

        set(0, 0, 1.0f);
        set(1, 1, 1.0f);
        set(2, 2, 1.0f);
        set(3, 3, 1.0f);
        set(3, 0, x);
        set(3, 1, y);
        set(3, 2, z);
    }
}
