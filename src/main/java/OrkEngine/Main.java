/*
    Author: Grant Fields
    Date: 8/3/2020
 */
package OrkEngine;

import OrkEngine.graphics.VisualThread;

public class Main {

    public static int threads;

    public static void main(String[] args) {

        threads = Runtime.getRuntime().availableProcessors() / 4;

        if(threads < 1){
            threads = 2;
        }

        new VisualThread();
    }

}