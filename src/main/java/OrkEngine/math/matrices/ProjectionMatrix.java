/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.math.matrices;

import java.awt.*;

public class ProjectionMatrix extends Matrix4x4 {

    public static final float fNear = .1f;
    public static final float fFar = 5000.0f;
    public static final float fFov = 140.0f;
    public static final float fAspectRatio = getAspectRatio();
    public static final float fFovRad = 1.0f / (float) (Math.tan(fFov * 0.5f / 100.0f * Math.PI));

    public static final float fRatio = fFar / (fFar - fNear);

    public ProjectionMatrix(){

        super();

        set(0, 0, fAspectRatio * fFovRad);
        set(1, 1, fFovRad);
        set(2, 2, fRatio);
        set(2, 3, 1.0f);
        set(3, 2, (-fRatio * fNear));
    }

    public static float getAspectRatio(){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double WIDTH = ((screenSize.getWidth() * 33.125) / 33);
        double HEIGHT = (screenSize.getHeight() * 15.5) / 16;

        return ((float) (HEIGHT / WIDTH));
    }
}
