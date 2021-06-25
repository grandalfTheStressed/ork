/*
    Author: Grant Fields
    Date: 5/30/2020
 */

package OrkEngine.tools;

import OrkEngine.math.matrices.YawMatrix;
import OrkEngine.math.matrices.Matrix4x4;
import OrkEngine.math.matrices.PitchMatrix;
import OrkEngine.math.vectors.Vector3d;

//Camera works by putting invisible unit points at the positive y and z axes
//these then get rotated by however much the current pitch and yaw for the current user are.
//I can then use them to determine the left and right using the cross product between "up" and "forward"

public class Camera {

    //these are true up and true forward, NOT what the player sees.
    public static final Vector3d fUp = new Vector3d(0, 1, 0, 1);
    public static final Vector3d fForward = new Vector3d(0, 0, 1, 1);

    //these define what the player is experiencing
    private Vector3d fCameraPos;
    private Vector3d fCameraUp;
    private Vector3d fCameraForward;

    private float fLastYaw = 0;
    private float fLastPitch = 0;

    public Camera(){

        fCameraPos = new Vector3d(0, 0, 0, 1);
        fCameraUp = fUp.clone();
        fCameraForward = fForward.clone();
    }

    public Camera(Vector3d fPos){

        this.fCameraPos = fPos;

        fCameraUp = fUp.clone();
        fCameraForward = fForward.clone();
    }

    public void move(Vector3d fDir, float fDistance){

        fCameraPos = fCameraPos.add(fDir.scale(fDistance));
    }

    public Vector3d getCameraPos(){

        return fCameraPos;
    }

    public Vector3d getCameraForward(){

        return fCameraForward;
    }

    public Vector3d getCameraUp(){

        return fCameraUp;
    }

    public Vector3d getLeft(){

        return fCameraUp.crossProduct(fCameraForward).normalize();
    }

    public Vector3d getRight(){

        return fCameraForward.crossProduct(fCameraUp).normalize();
    }

    public float getYaw(){

        return fLastYaw;
    }

    public float getPitch(){

        return fLastPitch;
    }

    public void cameraRotate(float fYaw, float fPitch){

        fLastPitch = fPitch;
        fLastYaw = fYaw;

        PitchMatrix pm;
        YawMatrix ym;
        Matrix4x4 m;

        pm = new PitchMatrix(-fPitch);
        ym = new YawMatrix(-fYaw);
        m = pm.multiply(ym);

        fCameraForward = m.multiplyVec(fForward);
        fCameraUp = m.multiplyVec(fUp);
    }
}