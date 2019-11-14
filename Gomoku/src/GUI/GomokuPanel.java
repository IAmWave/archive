/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import AI.Minimax.Evaluator;
import AI.Minimax.TrivialEvaluator;
import Map.Cell;
import Map.Map;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author Vašek
 */
public class GomokuPanel extends JPanel {

    Map map;
    double margin = 1;
    double[][] values = new double[15][15];
    boolean visual = true;

    public GomokuPanel(Map map) {
        this.map = map;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }

    public void toggleVisual() {
        visual = !visual;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g2) {
        
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        //g.setFont(new Font("Helvetica", Font.BOLD, 12));
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        double tileSize = (double) getWidth() / 15;
        if ((double) getHeight() / 15 < tileSize) {
            tileSize = (double) getHeight() / 15;
        }
        double maxValue = 0;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (values[i][j] > maxValue) {
                    maxValue = values[i][j];
                }
            }
        }


        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (visual) {
                    if (values[x][y] == maxValue) {
                        g.setColor(new Color(255, 255, 0));
                    } else {
                        g.setColor(new Color(255,
                                clamp(255 - (int) ((double) values[x][y] / maxValue * 128)),
                                clamp(255 - (int) ((double) values[x][y] / maxValue * 255))));
                    }
                } else {
                    g.setColor(Color.white);
                }

                g.fillRect((int) (x * tileSize + margin), (int) (y * tileSize + margin),
                        (int) (tileSize - 2 * margin), (int) (tileSize - 2 * margin));
                if (map.getCellAt(x, y) == Cell.X) {
                    g.setColor(Color.red);
                    g.fillRect((int) ((x + 0.25) * tileSize + margin), (int) ((y + 0.25) * tileSize + margin),
                            (int) (tileSize / 2 - 2 * margin), (int) (tileSize / 2 - 2 * margin));
                } else if (map.getCellAt(x, y) == Cell.O) {
                    g.setColor(Color.blue);
                    g.fillRect((int) ((x + 0.25) * tileSize + margin), (int) ((y + 0.25) * tileSize + margin),
                            (int) (tileSize / 2 - 2 * margin), (int) (tileSize / 2 - 2 * margin));
                }
                if (map.getCellAt(x, y) != Cell.EMPTY) {
                    
                    g.setColor(Color.WHITE);
                    g.drawString(map.getTurnNumberAt(x, y) + "",
                            (int) ((x + 0.25) * tileSize + margin)+4,
                            (int) ((y + 0.25) * tileSize + margin)+14);
                }

               // g.setColor(Color.black);
               //g.drawString(x + ", " + y, (int) (x * tileSize + margin) + 2, (int) (y * tileSize) + 12);
            }
        }
        Evaluator eval = new TrivialEvaluator();
        g.setColor(Color.green);
        g.setFont(new Font("Calibri", Font.BOLD, 40));
        g.drawString(eval.evaluate(map)+"", this.getWidth()/2, 50);
    }

    public int clamp(int i) {
        if (i < 0) {
            return 0;
        }
        if (i > 255) {
            return 255;
        }
        return i;
    }
}
