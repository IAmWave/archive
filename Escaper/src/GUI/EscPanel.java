/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Logic.EscLogic;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Vašek
 */
public class EscPanel extends JPanel {

    EscLogic logic;
    int frames = 0;
    long last;
    double average = 0;

    public EscPanel(EscLogic logic) {
        this.logic = logic;
        last = System.currentTimeMillis();
    }

    @Override
    public void paintComponent(Graphics g) {
        /*if(logic.getInAir()){
         return;
         }*/
        average = ((average * frames) + (double) (System.currentTimeMillis() - last)) / (frames + 1);
        frames++;
        last = System.currentTimeMillis();

        g.drawImage(logic.getScreenScanner().getScreenshot(), 0, 0, this);
        g.setColor(Color.GREEN);
        if (logic.getScreenScanner().isLeft()) {
            g.drawRect(0, logic.getScreenScanner().getY(), getWidth() / 2, 1);
        } else if (!logic.getInAir()) {
            g.drawRect(getWidth() / 2, logic.getScreenScanner().getY(), getWidth() / 2, 1);
        }
        int ySpeed = logic.getScreenScanner().getYSpeed();
        g.setColor(Color.yellow);
        if (ySpeed != 0) {
            g.setColor(ySpeed < 0 ? Color.green : Color.red);
        }
        //int begin = -2;
        if (logic.getScreenScanner().getY() > 300) {
            g.setColor(Color.green);

            for (int i = logic.getScreenScanner().getY() - 300; i < logic.getScreenScanner().getY() - 100; i++) {
                if (!logic.getScreenScanner().getLeftSpike(i) && logic.getScreenScanner().getLeftSpike(i - 1)) {
                    g.fillRect(0, i, 10, 10);
                } else if (logic.getScreenScanner().getLeftSpike(i) && !logic.getScreenScanner().getLeftSpike(i - 1)) {
                    g.fillRect(0, i - 30, 10, 10);
                }

                if (!logic.getScreenScanner().getRightSpike(i) && logic.getScreenScanner().getRightSpike(i - 1)) {
                    g.fillRect(EscLogic.WIDTH - 10, i, 10, 10);
                } else if (logic.getScreenScanner().getRightSpike(i) && !logic.getScreenScanner().getRightSpike(i - 1)) {
                    g.fillRect(EscLogic.WIDTH - 10, i - 30, 10, 10);
                }
            }
        }


        g.setColor(Color.CYAN);
        g.drawRect(0, logic.getScreenScanner().getBgY(), getWidth(), 1);

        g.drawString((int) (1000 / average) + " FPS", 10, 10);

        g.drawString(logic.getMessage(), 10, 30);
        g.drawString("speed " + ySpeed, 10, 50);
        for (int j = 0; j < logic.getLog().getLogSize(); j++) {
            if (logic.getLog().getLog(j) == null) {
                break;
            }
            g.drawString(logic.getLog().getLog(j), 10, 70 + 20 * j);
        }


        /*if (logic.getGameOver()) {
         g.setColor(Color.yellow);
         g.fillRect(0, 0, 50, 50);
         }*/
    }
}
