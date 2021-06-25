/*
    Author: Grant Fields
    Date: 8/3/2020
 */

package OrkEngine.tools;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private static final int NUM_KEYS = 256;
    private static boolean[] keys = new boolean[NUM_KEYS];
    private static boolean[] keysLast = new boolean[NUM_KEYS];

    private static final int NUM_BUTTONS = 5;
    private static boolean[] buttons = new boolean[NUM_BUTTONS];
    private static boolean[] buttonsLast = new boolean[NUM_BUTTONS];

    private static int mouseX = 0, mouseY = 0;
    private static int scroll = 0;

    public Input(){

    }

    public static void update(){

        scroll = 0;

        keysLast = keys.clone();
        buttonsLast = buttons.clone();
    }

    public void mouseWheelMoved(MouseWheelEvent mwe){

        scroll = mwe.getWheelRotation();
    }

    public void mouseDragged(MouseEvent me){

        mouseX = me.getX();
        mouseY = me.getY();
    }

    public void mouseMoved(MouseEvent me){

        mouseX = me.getX();
        mouseY = me.getY();
    }

    public void mouseClicked(MouseEvent me){


    }

    public void mouseEntered(MouseEvent me){


    }

    public void mouseExited(MouseEvent me){


    }

    //note to self:
    //left click is one
    //middle click is two
    //right click is three
    public void mousePressed(MouseEvent me){

        buttons[me.getButton()] = true;
    }

    public void mouseReleased(MouseEvent me){

        buttons[me.getButton()] = false;
    }

    public void keyPressed(KeyEvent ke){

        keys[ke.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent ke){

        keys[ke.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent ke){


    }

    public int getMouseX(){

        return mouseX;
    }

    public int getMouseY(){

        return mouseY;
    }

    public int getScroll(){

        return scroll;
    }

    public boolean isKey(int keyCode){

        return keys[keyCode];
    }

    public boolean isKeyUp(int keyCode){

        return ! keys[keyCode] && keysLast[keyCode];
    }

    public boolean isKeyDown(int keyCode){

        return keys[keyCode] && ! keysLast[keyCode];
    }

    public boolean isButton(int button){

        return buttons[button];
    }

    public boolean isButtonUp(int button){

        return ! buttons[button] && buttonsLast[button];
    }

    public boolean isButtonDown(int button){

        return buttons[button] && ! buttonsLast[button];
    }
}
