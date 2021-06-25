/*
    Author: Grant Fields
    Date: 8/3/2020
 */

package OrkEngine.graphics;

import OrkEngine.modeling.Mesh;
import OrkEngine.math.vectors.Vector3d;
import OrkEngine.tools.Camera;
import OrkEngine.tools.Input;

import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

class Window extends JFrame {

    private final Screen SCREEN;
    private final Input INPUT = new Input();

    public static final float fPi = (float)(Math.PI);

    private final Camera CAMERA = new Camera(new Vector3d(-400, 400, 0));
    private float fYaw = 0;
    private float fPitch = 0;

    private boolean lighting = true;

    public Window(int width, int height){

        this.setTitle("3d Demo");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(width, height);

        SCREEN = new Screen(width, height);

        this.add(SCREEN);

        this.addKeyListener(INPUT);
        this.addMouseListener(INPUT);
        this.addMouseMotionListener(INPUT);
        this.addMouseWheelListener(INPUT);

        this.setVisible(true);
    }

    public void render(BufferedImage frame){

        SCREEN.render(frame);
    }

    public void passDetails(float fElapsedTime){

        checkMovement(fElapsedTime);
        checkRotation(fElapsedTime);
        SCREEN.passDetails(fElapsedTime);
    }

    public Camera getCamera(){

        return CAMERA;
    }

    public boolean isLighting(){

        return lighting;
    }

    public void updateInput(){

        Input.update();
    }

    //W moves you forward
    //S moves you backward
    //A moves you left
    //D moves you right
    //Space increases Y axis position
    //Shift reduces Y axis position
    public void checkMovement(float fElapsedTime){

        boolean W = INPUT.isKey(KeyEvent.VK_W);
        boolean A = INPUT.isKey(KeyEvent.VK_A);
        boolean S = INPUT.isKey(KeyEvent.VK_S);
        boolean D = INPUT.isKey(KeyEvent.VK_D);
        boolean space = INPUT.isKey(KeyEvent.VK_SPACE);
        boolean shift = INPUT.isKey(KeyEvent.VK_SHIFT);
        boolean one = INPUT.isKeyDown(97);
        boolean two = INPUT.isKeyDown(98);

        float fMoveSpeed = fElapsedTime * 120;

        //This mostly works but it appears that round off errors in floating points
        //are making it possible to move backwards while looking straight down
        //by pressing W and space, or move forwards by looking straight up and pressing
        //S and space. I'm getting vectors that are mostly in one direction with some super tiny
        //component giving me movement where I shouldn't see it.
        Vector3d dir = new Vector3d(0,0,0);

        if(one){

            lighting = !lighting;
        }

        if(W){

            dir = dir.add(CAMERA.getCameraForward());
        }

        if(S){

            dir = dir.sub(CAMERA.getCameraForward());
        }

        if(A){

            dir = dir.add(CAMERA.getLeft());
        }

        if(D){

            dir = dir.add(CAMERA.getRight());
        }

        if(space){

            dir = dir.add(Mesh.Y_AXIS);
        }

        if(shift){

            dir = dir.sub(Mesh.Y_AXIS);
        }

        if(dir.length() != 0) {

            CAMERA.move(dir.normalize(), fMoveSpeed);
        }
    }

    //Q turns you left
    //E turns you right
    //Up Arrow looks up
    //Down Arrow looks down
    public void checkRotation(float fElapsedTime){

        boolean Q = INPUT.isKey(KeyEvent.VK_Q);
        boolean E = INPUT.isKey(KeyEvent.VK_E);
        boolean upArrow = INPUT.isKey(KeyEvent.VK_UP);
        boolean downArrow = INPUT.isKey(KeyEvent.VK_DOWN);

        //rotates pi radians per second
        float fRotationSpeed = (fPi * fElapsedTime);
        float fRotationSpeedScaled = fRotationSpeed * 1.5f;

        if(E){

            fYaw += fRotationSpeedScaled;
        }

        if(Q){

            fYaw -= fRotationSpeedScaled;
        }

        if(upArrow){

            fPitch -= fRotationSpeedScaled;
        }

        if(downArrow){

            fPitch += fRotationSpeedScaled;
        }

        //These stop the user from looking to far backwards. controls get inverted and unless
        //I decide to make something involving flying a ship or plane we don't need to have camera control that far
        if(! ((fPi / 2) - fPitch >= 0)){

            fPitch = fPi / 2;
        }

        if(! ((fPi / 2) + fPitch >= 0)){

            fPitch = -fPi / 2;
        }

        //this just restricts the angle to stay within tau radians
        if(! ((2 * fPi) - fYaw >= 0 && (2 * fPi) + fYaw >= 0)){

            fYaw = 0;
        }

        //Rotating the camera normals to make sure movement keys move you based on where you're looking
        CAMERA.cameraRotate(fYaw, fPitch);
    }
}
