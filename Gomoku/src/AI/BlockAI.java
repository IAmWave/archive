/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Map.Cell;
import Map.Map;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Vašek
 */
public class BlockAI implements AI {

    int myFiveValue = 100000;
    int hisFiveValue = 9999;
    int myFourValue = 35;
    int hisFourValue = 34;
    int myThreeValue = 10;
    int hisThreeValue = 9;
    int myTwoValue = 5;
    int hisTwoValue = 5;
    double[][] values = new double[15][15];

    public BlockAI() {
    }

    public BlockAI(int m5, int h5, int m4, int h4, int m3, int h3, int m2, int h2) {
        myFiveValue = m5;
        hisFiveValue = h5;
        myFourValue = m4;
        hisFourValue = h4;
        myThreeValue = m3;
        hisThreeValue = h3;
        myTwoValue = m2;
        hisTwoValue = h2;
    }

    public double[][] eval(Map map) {
        evaluate(map);
        return values.clone();
    }

    public Point makeMove(Map map) {

        evaluate(map);

        return pickMove();

    }

    public Point pickMove() {
        ArrayList<Point> maxPoints = new ArrayList<>();

        maxPoints.add(new Point(7, 7));
        double maxValue = 1;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (values[i][j] > maxValue) {
                    maxPoints.clear();
                    maxValue = values[i][j];
                    maxPoints.add(new Point(i, j));
                } else if (values[i][j] == maxValue) {
                    maxPoints.add(new Point(i, j));
                }
            }
        }
        /*Random random = new Random();
         if (maxPoints.size() > 1) {
         return maxPoints.get(random.nextInt(maxPoints.size() - 1));
         }*/
        return maxPoints.get(maxPoints.size() - 1);
    }

    public void evaluate(Map map) {

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                values[i][j] = 0;
            }
        }

        if (map.getCellAt(7, 7) == Cell.EMPTY) {
            values[7][7]++;
        }

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 15; y++) {
                testLine(x, y, 1, 0, map);
            }
        }
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15 - 5; y++) {
                testLine(x, y, 0, 1, map);

            }
        }
        for (int x = 0; x < 15 - 5; x++) {
            for (int y = 0; y < 15 - 5; y++) {
                testLine(x, y, 1, 1, map);
            }
        }
        for (int x = 5; x < 15; x++) {
            for (int y = 0; y < 15 - 5; y++) {
                testLine(x, y, -1, 1, map);
            }
        }
    }

    public void testLine(int x, int y, int xAdd, int yAdd, Map map) {
        int count = 0;
        Cell owner = Cell.EMPTY;
        for (int i = 0; i < 5; i++) {
            if (map.getCellAt(x + xAdd * i, y + yAdd * i) == Cell.X) {
                if (owner == Cell.O) { //vícebarvá řada
                    return;
                } else {
                    owner = Cell.X;
                    count++;
                }
            } else if (map.getCellAt(x + xAdd * i, y + yAdd * i) == Cell.O) {
                if (owner == Cell.X) { //vícebarvá řada
                    return;
                } else {
                    owner = Cell.O;
                    count++;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (map.getCellAt(x + xAdd * i, y + yAdd * i) == Cell.EMPTY) {
                values[x + xAdd * i][y + yAdd * i] += score(count, owner == map.movingPlayer());
            }
        }
    }

    public int score(int count, boolean my) {
        if (my) {
            switch (count) {
                case 4:
                    return myFiveValue;
                case 3:
                    return myFourValue;
                case 2:
                    return myThreeValue;
                case 1:
                    return myTwoValue;
                default:
                    return 0;
            }
        } else {
            switch (count) {
                case 4:
                    return hisFiveValue;
                case 3:
                    return hisFourValue;
                case 2:
                    return hisThreeValue;
                case 1:
                    return hisTwoValue;
                default:
                    return 0;
            }
        }
    }
}



/*
                 count = 0;
                 Point mark = null;
                 for (int i = 0; i < 5; i++) {
                 if (map.getCellAt(x, y + i) == map.movingPlayer() && my) {
                 count++;
                 } else if (map.getCellAt(x, y + i) == map.waitingPlayer() && !my) {
                 count++;
                 } else if (map.getCellAt(x, y + i) == Cell.EMPTY) {

                 mark = new Point(x, y + i);
                 }
                 }
                 if (count == 5 - 1 && mark != null) {
                 if (!open) {
                 result.add(mark);
                 } else if (map.getCellAt(x, y - 1) == Cell.EMPTY
                 && map.getCellAt(x, y + 5) == Cell.EMPTY) {
                 result.add(mark);
                 }
                 }*/