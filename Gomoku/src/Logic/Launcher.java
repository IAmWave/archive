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
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Vašek
 */
public class Launcher {

    public static void main(String args[]) {
        /*DrawFrame frame = new DrawFrame();
         frame.setVisible(true);*/
        AI ai1 = new ForkAI();
        AI ai2 = new BlockAI(100000, 10000, 35, 34, 15, 14, 6, 5);
        int xWins = 0;
        /*for (int i = 0; i < 9; i++) {
            if (play(ai1, ai2, i)) {
                xWins++;
            }
        }
        System.out.println("X won " + xWins + " times.");
*/
        Map map = new Map();

        System.out.println(Map.writeTurn(map.getLastX(), map.getLastY()));

        int config = 0;
        open(map, config);
        

        JFrame frame = new JFrame();

        frame.getContentPane().setPreferredSize(new Dimension(600, 600));
        frame.pack();
        GomokuPanel panel = new GomokuPanel(map);
        panel.setSize(600, 600);
        frame.add(panel);
        InputParser parser = new InputParser(map, panel);
        frame.addKeyListener(parser);
        frame.getContentPane().addMouseListener(parser);
        //Úpravy okna
        frame.setTitle("Gomoku");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void open(Map map, int config) {
        map.attemptMove(7, 7); //x
        switch (config) {
            case 1:
                map.attemptMove(6, 6);
                break;
            case 2:
                map.attemptMove(7, 6);
                break;
            case 3:
                map.attemptMove(8, 6);
                break;
            case 4:
                map.attemptMove(8, 7);
                break;
            case 5:
                map.attemptMove(8, 8);
                break;
            case 6:
                map.attemptMove(7, 8);
                break;
            case 7:
                map.attemptMove(6, 8);
                break;
            case 8:
                map.attemptMove(6, 7);
                break;
            case 0:
                System.out.println("zero config");
                break;
            default:
                System.out.println("oops!");
                break;
        }
    }

    public static boolean play(AI ai1, AI ai2, int config) {
        Map map = new Map();

        open(map, config);
        System.out.print(Map.writeTurn(map.getLastX(), map.getLastY()));
        while (map.checkWin() == Cell.EMPTY && map.getTurnNumber() < 200) {
            if (map.xMoves()) {
                map.attemptMove(ai1.makeMove(map));
            } else {
                map.attemptMove(ai2.makeMove(map));
            }
            System.out.print(Map.writeTurn(map.getLastX(), map.getLastY()));
        }
        System.out.println();
        System.out.println(map.checkWin() + " wins in " + map.getTurnNumber() + " turns");
        return map.checkWin() == Cell.X;
    }
}
