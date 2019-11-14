/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import Scanning.ScreenScanner;
import java.awt.Rectangle;
import java.awt.Robot;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author Vašek
 */
public class Tester {

    public static void main(String[] args) {
        while (true) {
            Timer t = new Timer();
            t.schedule(new Stopper(300), 0);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void fullScreenFPSCheck() {
        Rectangle rect = ScreenScanner.escPos;
        try {
            Robot robot = new Robot();
            int count = 1;
            long beforeTime = System.currentTimeMillis();
            while (count < 32) {
                robot.createScreenCapture(rect);
                count++;
            }
            double time = System.currentTimeMillis() - beforeTime;
            System.out.println("FPS: " + 1 / (time / 1000) * 32);
        } catch (Exception e) {
        }
    }

    public static void sleepCheck() {
        try {
            while (true) {
                long start = System.nanoTime();
                Thread.sleep(1);
                System.out.println((System.nanoTime() - start) / 1000000);
            }
        } catch (Exception e) {
        }
    }

    private static class Stopper extends TimerTask {

        int time;

        public Stopper(int time) {
            this.time = time;
        }

        @Override
        public void run() {

            long lastPressed = System.currentTimeMillis();
            try {
                //Robot robot = new Robot();
                //robot.keyPress(KeyEvent.VK_SPACE);
                //LockSupport.parkNanos((long) time * 1000000);
                LockSupport.parkUntil(System.currentTimeMillis() + time);
                /*int sleep = 5;
                 while (System.currentTimeMillis() < lastPressed + time + sleep / 2) {
                 Thread.sleep(sleep);
                 }*/
                 //robot.keyRelease(KeyEvent.VK_SPACE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            long actual = System.currentTimeMillis() - lastPressed;
            System.out.println("Intended: " + time + ". Actual: " + actual + " Difference " + (time - actual));
        }
    }
}
