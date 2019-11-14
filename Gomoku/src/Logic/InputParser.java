/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import AI.AI;
import AI.BlockAI;
import AI.ForkAI;
import GUI.GomokuPanel;
import Map.Cell;
import Map.Map;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;
import java.util.Timer;

/**
 *
 * @author Vašek
 */
public class InputParser implements KeyListener, MouseListener {

    int myFiveValue = 100000;
    int hisFiveValue = 9999;
    int myFourValue = 35;
    int hisFourValue = 34;
    int myThreeValue = 10;
    int hisThreeValue = 9;
    int myTwoValue = 5;
    int hisTwoValue = 5;
    Map map;
    GomokuPanel panel;
    static final int battleDelay = 1000;
    boolean battling = false;
    Timer t;
    AI ai1 = new ForkAI();
    AI ai2 = new ForkAI();//BlockAI(100000, 10000, 35, 34, 15, 14, 6, 5);

    public InputParser(Map map, GomokuPanel panel) {
        this.map = map;
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_V) {
            panel.toggleVisual();
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            if (!battling) {
                battling = true;
                t = new Timer();
                t.scheduleAtFixedRate(new RobotTurn(), battleDelay, battleDelay);
            } else {
                t.cancel();
                battling = false;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_N) {
            if (!battling) {
                battling = true;
                t = new Timer();
                t.scheduleAtFixedRate(new RobotTurn(), 10, 10);
            } else {
                t.cancel();
                battling = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int size = panel.getWidth();
        if (size > panel.getHeight()) {
            size = panel.getHeight();
        }

        if (e.getButton() == 1) {
            int x = (int) ((double) e.getX() / size * 15);
            int y = (int) ((double) e.getY() / size * 15);
            if (map.attemptMove(x, y)) {
                System.out.println(Map.writeTurn(x, y));
                Cell r = map.checkWin();
                if (r != Cell.EMPTY) {
                    System.out.println("You win");
                    map.clear();
                }
                panel.setValues(ai1.eval(map));
                map.attemptMove(ai1.makeMove(map));
                System.out.println(Map.writeTurn(map.getLastX(), map.getLastY()));
                r = map.checkWin();
                if (r != Cell.EMPTY) {
                    System.out.println("You lose");
                    map.clear();
                }
                panel.repaint();

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private class RobotTurn extends TimerTask {

        public void run() {
            //panel.setValues(ForkAI.eval(map));
            //System.out.println("Turn " + map.getTurnNumber());

            if (map.xMoves()) {
                panel.setValues(ai1.eval(map));
                map.attemptMove(ai1.makeMove(map));
            } else {
                panel.setValues(ai2.eval(map));
                map.attemptMove(ai2.makeMove(map));
            }
            panel.repaint();
            if (map.checkWin() != Cell.EMPTY) {
                if (map.checkWin() == Cell.X) {
                    System.out.println("X wins");
                } else {
                    System.out.println("O wins");
                }
                t.cancel();
            }
        }
    }
}
