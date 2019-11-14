/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Map.Cell;
import Map.Map;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Vašek
 */
public class ForkAI implements AI {

    static final int iLoseValue = -1000;
    static final int myFiveValue = 100000;
    static final int hisFiveValue = 9999;
    static final int myFourValue = 35;
    static final int hisFourValue = 34;
    static final int myThreeValue = 10;
    static final int hisThreeValue = 9;
    static final int myTwoValue = 5;
    static final int hisTwoValue = 5;
    Value[][] values = new Value[15][15];
    boolean endMode = false;
    /*public int highestValue(Map map) {
     int[][] val = eval(map);
     int maxValue = 0;
     int x = 0, y = 0;
     for (int i = 0; i < 15; i++) {
     for (int j = 0; j < 15; j++) {
     if (val[i][j] > maxValue) {
     maxValue = val[i][j];
     x = i;
     y = j;
     }
     }
     }
     System.out.println(" Highest value at " + x + ", " + y);
     return maxValue;
     }*/

    public double[][] eval(Map map) {
        evaluate(map);
        double[][] result = new double[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                result[i][j] = values[i][j].getStrength();
            }
        }
        return result;
    }

    private ValuedPoint makeMove(Map map, int depth) {
        evaluate(map);
        return pickMove(map, depth);
    }

    public ValuedPoint makeMove(Map map) {

        evaluate(map);

        return pickMove(map, 0);

    }

    public ValuedPoint pickMove(Map map, int depth) {
        ArrayList<Point> maxPoints = new ArrayList<>();

        maxPoints.add(new Point(7, 7));
        double maxValue = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (values[i][j].getStrength() > maxValue) {
                    maxPoints.clear();
                    maxValue = values[i][j].getStrength();
                    maxPoints.add(new Point(i, j));
                } else if (maxValue == values[i][j].getStrength()) {
                    maxPoints.add(new Point(i, j));
                }
            }
        }
        /*Random random = new Random();
         if (maxPoints.size() > 1) {
         return maxPoints.get(random.nextInt(maxPoints.size() - 1));
         }*/

        if (maxValue < 2) {
            endMode = true;
            //System.out.println("endmoud");
        }
        if (endMode) {
            return new ValuedPoint(maxPoints.get(0), maxValue);
        }

        if (maxPoints.size() > 1 && depth < 10) {
            //System.out.print("c");
            if (maxValue == 9999) {
                return new ValuedPoint(maxPoints.get(0), iLoseValue);
            }
            //System.out.println("-----------Deciding for " + map.xMoves() + ": " + maxPoints);
            Map m;
            ForkAI ai = new ForkAI();
            double min = -1;
            int minIndex = 0;
            for (int i = 0; i < maxPoints.size(); i++) {
                //System.out.println("<Testing " + maxPoints.get(i).x + ", " + maxPoints.get(i).y);
                m = new Map(map);
                m.attemptMove(maxPoints.get(i));
                ValuedPoint val = ai.makeMove(m, depth + 1);
                //System.out.println(val.v + " at " + val.x + ", " + val.y + "> ");
                if (val.v < min || min == -1) {
                    min = val.v;
                    minIndex = i;
                }
            }
            //System.out.println("----------Decided for " + maxPoints.get(minIndex));
            return new ValuedPoint(maxPoints.get(minIndex), maxValue);
        }

        return new ValuedPoint(maxPoints.get(0), maxValue);
    }

    public void evaluate(Map map) {

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                values[i][j] = new Value();
            }
        }

        if (map.getCellAt(7, 7) == Cell.EMPTY) {
            values[7][7].addValue(true, 2, false);
        }

        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 15; y++) {
                testLine(x, y, 1, 0, map);
            }
        }
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 11; y++) {
                testLine(x, y, 0, 1, map);

            }
        }
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 15 - 5; y++) {
                testLine(x, y, 1, 1, map);
            }
        }
        for (int x = 4; x < 15; x++) {
            for (int y = 0; y < 11; y++) {
                testLine(x, y, -1, 1, map);
            }
        }
    }

    public void testLine(int x, int y, int xAdd, int yAdd, Map map) {
        int count = 0;
        int first = -1;
        int last = -1;
        Cell owner = Cell.EMPTY;
        for (int i = 0; i < 5; i++) {
            if (map.getCellAt(x + xAdd * i, y + yAdd * i) == Cell.X) {
                if (first == -1) {
                    first = i;
                }
                last = i;
                if (owner == Cell.O) { //vícebarvá řada
                    return;
                } else {
                    owner = Cell.X;
                    count++;
                }
            } else if (map.getCellAt(x + xAdd * i, y + yAdd * i) == Cell.O) {
                if (first == -1) {
                    first = i;
                }
                last = i;
                if (owner == Cell.X) { //vícebarvá řada
                    return;
                } else {
                    owner = Cell.O;
                    count++;
                }
            }
        }
        int spaces = last - first - count;
        if (spaces > 1) {
            count--;
        }
        for (int i = 0; i < 5; i++) {
            if (map.getCellAt(x + xAdd * i, y + yAdd * i) == Cell.EMPTY) {
                boolean diagonal = xAdd != 0 && yAdd != 0;
                values[x + xAdd * i][y + yAdd * i]
                        .addValue(owner == map.movingPlayer(), count + 1, diagonal);
            }
        }
    }
}
