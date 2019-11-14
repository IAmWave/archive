/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import GUI.EscPanel;
import Scanning.ScreenScanner;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import javax.swing.JFrame;
import java.util.Timer;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * @author Vašek
 */
public class EscLogic {
    //game over

    public static final Point GAME_OVER_POS = new Point(100, 150);
    public static final Color GAME_OVER_COLOR = new Color(159, 30, 71);
    public static final int FRAME_DELAY = 5;
    public static final int DECISION_DELAY = 25;
    //environment detection
    public static final Color EYE_COLOR = new Color(255, 255, 94);
    public static final Color BG_COLOR_1 = new Color(12, 14, 18), BG_COLOR_2 = new Color(27, 36, 44);
    public static final Color SPIKE_COLOR = new Color(42, 110, 139);
    public static final int LEFT_EYE_X = 80, RIGHT_EYE_X = 398, //pozice žlutého oka na levé a pravé straně
            LEFT_SPIKE_X = 65, RIGHT_SPIKE_X = 415,
            BG_CHECK_X = 221, BG_CHECK_DIST = 102; //kontrola pozadí
    public static final int MIN_Y = 300; //před tímto Y začíná kamera jet nahoru
    public final static int WIDTH = 480, HEIGHT = 640;
    //jumps
    public static final int MIN_TIME = 30, NEUTRAL_TIME = 300, MAX_TIME = 490;
    public static final int MIN_JUMP = 20/*40*/, MAX_JUMP = 255;
    
    public static final double JUMP_COEF = 2.7, JUMP_BASE = 370;//2.8, 340 //vyšší coef, vyšší skoky, vyšší base, nižší skoky
    public static final int CHARACTER_HEIGHT = 90;
    public static final int MAX_JUMP_PADDING = 30; //jakou výšku od maxima už radši neskáče
    public static final int FLAT_TIME_ADD = 20;
    public static final int BIG_JUMP_DECAY = 90; //jak rychle klesá bigJump (50)
    public static final int BASE_Y = 612;
    //data
    public static final int[] times = {20, 40, 60, 100, 200, 300, 400, 495};
    public static final int[] heights = {74, 106, 116, 142/*122*/, 165/*179*/, 216, 242, 257, 260};
    //debug
    public static final boolean CALIBRATION = false;
    public static final int JUMP_TIME = 400;
    //variables
    //detection
    String message = "";
    //
    Robot robot;
    //calibration/data
    double average = 0; //průměrný skok při kalibraci
    long lastPressed = 0;//
    int intendedLength = 0; //jak dlouho chtěl klávesu držet (porovnání s opravdovou hodnotou)
    EscLog log = new EscLog(10);
    //corrections
    boolean jumpInProgress = false;
    static long nextScreenshotTime = 0;
    ScreenScanner scanner;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Escaper");
        EscLogic logic = new EscLogic();
        EscPanel panel = new EscPanel(logic);
        frame.add(panel);
        frame.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setLocation(ScreenScanner.windowX + WIDTH + 10, ScreenScanner.windowY);
        frame.pack();
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        long renderTime;
        while (true) {
            renderTime = System.currentTimeMillis();
            if (System.currentTimeMillis() > nextScreenshotTime) {
                logic.scanner.updateScreenshot();
                logic.attemptJump();
                panel.repaint();
            }
            renderTime = System.currentTimeMillis() - renderTime;
            //System.out.println(renderTime);
            try {
                if (renderTime > FRAME_DELAY * 1000000) {
                    System.out.println("Render took " + renderTime / 1000000 + " ms at " + System.currentTimeMillis());
                }
                Thread.sleep(FRAME_DELAY - renderTime / 1000000); // convert to milliseconds
            } catch (final Exception ignored) {
            }
        }
    }

    public EscLogic() {
        Timer decisionTimer = new Timer();
        decisionTimer.scheduleAtFixedRate(new DecisionTask(), DECISION_DELAY * 10, DECISION_DELAY);
        try {
            robot = new Robot();
            scanner = new ScreenScanner(robot);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
        scanner.updateScreenshot();
    }

    public void attemptJump() {
        if (scanner.isInAir()) {
            return;
        }
        if (scanner.getY() < 0 || scanner.getBigJump() > 0) {
            /*if (bigJump > 0) {
             System.out.println("big: " + bigJump);
             }*/
            return;
        }
        /*if (CALIBRATION) {
         if (scanner.getY() < BASE_Y) {
         return;
         }
         if (BASE_Y - scanner.minY > 0) {
         average = ((average * jumps) + baseY - minY) / (jumps + 1);
         jumps++;
         System.out.println(JUMP_TIME + " ms, " + (baseY - minY) + " height, " + average + " average");
         }
         minY = baseY;
         jump(JUMP_TIME);
         //jumpHeight(174);
         return;
         }*/
        int spot = -1;


        boolean danger = scanner.getYSpeed() < 0;
        boolean top = true;
        for (int i = scanner.getY() - MAX_JUMP; i < scanner.getY() - MIN_JUMP; i++) {
            if (!scanner.isLeft()) {
                if (scanner.getLeftSpike(i)) {
                    danger = true;
                }
                if (!scanner.getLeftSpike(i) && scanner.getLeftSpike(i - 1)) { //spodek
                    spot = i;
                    top = false;
                    break;
                } else if (scanner.getLeftSpike(i) && !scanner.getLeftSpike(i - 1)) {
                    int temp = i - CHARACTER_HEIGHT;
                    if (temp > scanner.getY() - MAX_JUMP + MAX_JUMP_PADDING) {
                        spot = temp;
                        top = true;
                    }
                    break;
                }
            } else {
                if (scanner.getRightSpike(i)) {
                    danger = true;
                }
                if (!scanner.getRightSpike(i) && scanner.getRightSpike(i - 1)) {
                    spot = i;
                    top = false;
                    break;
                } else if (scanner.getRightSpike(i) && !scanner.getRightSpike(i - 1)) {
                    int temp = i - CHARACTER_HEIGHT;
                    if (temp > scanner.getY() - MAX_JUMP + MAX_JUMP_PADDING) {
                        spot = temp;
                        top = true;
                    }
                    break;
                }
            }
        }
        if (spot < 0) {
            if (!danger) {
                log.log("Default jump");
                message = "jumping";
                jump(NEUTRAL_TIME);
            } else {
                log.logIfDifferent("danger");
                message = "danger";
            }
        } else {
            message = (top ? "top" : "bot");
            log.log("Jumping " + message);
            if (top && noSpikesTo(spot, scanner.isLeft())) {
                log.log("Maximum jump");
                message = "maxjump";
                jump(MAX_TIME);
            }
            jumpHeight(scanner.getY() - spot);
        }
    }

    public static int heightToTime(int h) {
        if (h >= MAX_JUMP) {
            return MAX_TIME;
        } else if (h <= MIN_JUMP) {
            return MIN_TIME;
        } else {
            /*int i;
             for (i = 0; i < times.length - 1; i++) {
             if (h > heights[i]) {
             break;
             }
             }
             double k = (heights[i] - heights[i + 1]) / (times[i] - times[i + 1]);
             double a = heights[i] - k * times[i];
             //System.out.println("k=" + k + ", a=" + a);
             double result = ((h - a) / k) + FLAT_TIME_ADD;

             return (int) result;*/
            return (int) (h * JUMP_COEF - JUMP_BASE);

        }


    }

    public void jumpHeight(int h) {
        if (!CALIBRATION) {
            message = message + " " + h;
        }
        int t = heightToTime(h);
        jump(t);
    }

    public void jump(int time) {
        System.out.println(message);
        if (time > NEUTRAL_TIME) {
            scanner.setBigJump(time - NEUTRAL_TIME);
        }
        intendedLength = time;
        lastPressed = System.currentTimeMillis();
        Timer t = new Timer();
        t.schedule(new Stopper(time), 0);
        nextScreenshotTime = lastPressed + time + 5; //nedělá screenshoty při držení tlačítka kvůli přesnosti
        //inAir = true;
    }

    public boolean noSpikesTo(int to, boolean left) {
        boolean result = true;
        for (int i = 0; i < to; i++) {
            if (left ? scanner.getLeftSpike(i) == true : scanner.getRightSpike(i) == true) {
                result = false;
            }

        }
        return result;
    }

    /*public boolean getGameOver() {
     Color c = new Color(screenshot.getRGB(GAME_OVER_POS.x, GAME_OVER_POS.y));
     return c.equals(GAME_OVER_COLOR);
     }*/
    private class DecisionTask extends TimerTask { //provede se cca 50x za sekundu, rozhodovani bota

        @Override
        public void run() {
            if (System.currentTimeMillis() > nextScreenshotTime) {
                attemptJump();
            }
            scanner.approximatePosition(); //odhadne novou pozici hráče podle jeho rychlosti a podobně
        }
    }

    private class Stopper extends TimerTask { //ukončí skok

        int time = -1;

        public Stopper() {
        }

        public Stopper(int time) {
            this.time = time;
        }

        @Override
        public void run() {
            if (jumpInProgress) {
                return;
            }
            jumpInProgress = true;
            if (time > 0) {
                intendedLength = time;
                lastPressed = System.currentTimeMillis();
                robot.keyPress(KeyEvent.VK_SPACE);

                try {
                    //LockSupport.parkNanos((long) time * 1000000);
                    LockSupport.parkUntil(System.currentTimeMillis() + time);
                    /*int sleep = 5;
                     while (System.currentTimeMillis() < lastPressed + time + sleep / 2) {
                     Thread.sleep(sleep);
                     }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            robot.keyRelease(KeyEvent.VK_SPACE);
            long actual = System.currentTimeMillis() - lastPressed;
            System.out.println("Intended: " + intendedLength + ". Actual: " + actual + " Difference " + (intendedLength - actual));
            message = (intendedLength - actual) + "";
            jumpInProgress = false;
        }
    }

    public EscLog getLog() {
        return log;
    }

    public String getMessage() {
        return message;
    }

    public ScreenScanner getScreenScanner() {
        return scanner;
    }
    /*
     public int getY() {
     return y;
     }

     public int getBgY() {
     return bgY;
     }

     public boolean getLeft() {
     return left;
     }

     public boolean getInAir() {
     return inAir;
     }

     public int getYSpeed() {
     return ySpeed;
     }

     public boolean getLeftSpikeAt(int i) {
     return leftSpikes[i];
     }

     public boolean getRightSpikeAt(int i) {
     return rightSpikes[i];
     }

     public Image getScreenshot() {
     return screenshot;
     }*/

    public boolean getInAir() {
        return scanner.getY() < 0;
    }
}
