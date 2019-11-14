/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Scanning;

import static Logic.EscLogic.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

/**
 *
 * @author Vašek
 */
public class ScreenScanner {

    public static final double ACCEL = 50;
    public final static int windowX = 283, windowY = 66;
    public static Rectangle escPos = new Rectangle(windowX, windowY, WIDTH, HEIGHT);
    BufferedImage screenshot;
    boolean left = true, inAir = false;
    int y = 0;
    int ySpeed = 0;
    int bgY = 0;
    boolean[] leftSpikes = new boolean[HEIGHT], rightSpikes = new boolean[HEIGHT];
    int bigJump = 0; //při skoku, kdy jede nahoru, musí čekat, aby ztratil rychlost - bigJump se bude zmenšovat
    int minY = BASE_Y, jumps = 0;
    Robot robot;
    long lastUpdateTime;

    public ScreenScanner(Robot robot) {
        this.robot = robot;
    }

    public void updateScreenshot() {
        lastUpdateTime = System.currentTimeMillis();
        screenshot = robot.createScreenCapture(escPos);
        int lastBgY = bgY;
        int lastY = y;
        boolean wasInAir = inAir;
        y = -1;
        Color cur;
        inAir = true;
        for (int i = MIN_Y; i < HEIGHT; i++) { //zjištění pozice skokana
            cur = new Color(screenshot.getRGB(LEFT_EYE_X, i));
            if (cur.equals(EYE_COLOR)) {
                y = i;
                left = true;
                inAir = false;
                break;
            }
            cur = new Color(screenshot.getRGB(RIGHT_EYE_X, i));
            if (cur.equals(EYE_COLOR)) {
                y = i;
                left = false;
                inAir = false;
                break;
            }
        }

        bgY = -1;
        Color[] bgCheck = new Color[4];
        for (int i = 0; i < HEIGHT - BG_CHECK_DIST; i++) { //zjištění pozice pozadí
            bgCheck[0] = new Color(screenshot.getRGB(BG_CHECK_X, i));
            bgCheck[1] = new Color(screenshot.getRGB(BG_CHECK_X + 1, i));
            bgCheck[2] = new Color(screenshot.getRGB(BG_CHECK_X, i + BG_CHECK_DIST));
            bgCheck[3] = new Color(screenshot.getRGB(BG_CHECK_X + 1, i + BG_CHECK_DIST));
            if (bgCheck[0].equals(BG_COLOR_1) && bgCheck[1].equals(BG_COLOR_2)
                    && bgCheck[2].equals(BG_COLOR_1) && bgCheck[3].equals(BG_COLOR_2)) {
                bgY = i;
                break;
            }
        }

        //bodáky

        for (int i = 0; i < HEIGHT; i++) { //zjištění pozice pozadí
            cur = new Color(screenshot.getRGB(LEFT_SPIKE_X, i));
            if (cur.equals(SPIKE_COLOR)) {
                leftSpikes[i] = true;
            } else {
                leftSpikes[i] = false;
            }
            cur = new Color(screenshot.getRGB(RIGHT_SPIKE_X, i));
            if (cur.equals(SPIKE_COLOR)) {
                rightSpikes[i] = true;
            } else {
                rightSpikes[i] = false;
            }
        }

        int bgSpeed = Math.abs(bgY - lastBgY) < 50 ? bgY - lastBgY : 0;

        if (!wasInAir && !inAir) {
            ySpeed = y - lastY - bgSpeed;
        } else {
            ySpeed = 0;
        }
        if (y >= 0) {
            minY = Math.min(minY, y); //kalibrace
            bigJump = Math.max(0, bigJump - BIG_JUMP_DECAY);
        }
    }

    public void approximatePosition() {
        System.out.println("Updating after " + (System.currentTimeMillis() - lastUpdateTime));
        lastUpdateTime = System.currentTimeMillis();
    }

    public boolean isLeft() {
        return left;
    }

    public int getYSpeed() {
        return ySpeed;
    }

    public int getY() {
        return y;
    }

    public boolean getLeftSpike(int i) {
        return leftSpikes[i];
    }

    public boolean getRightSpike(int i) {
        return rightSpikes[i];
    }

    public boolean[] getLeftSpikes() {
        return leftSpikes;
    }

    public boolean[] getRightSpikes() {
        return rightSpikes;
    }

    public int getBigJump() {
        return bigJump;
    }

    public void setBigJump(int bigJump) {
        this.bigJump = bigJump;
    }

    public BufferedImage getScreenshot() {
        return screenshot;
    }

    public int getBgY() {
        return bgY;
    }
    
    public boolean isInAir(){
    return inAir;
    }
}
