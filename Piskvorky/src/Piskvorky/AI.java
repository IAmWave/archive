package Piskvorky;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vašek
 */
public class AI {

    Field myField;

    public AI(Field field) {
        myField = field;
    }

    public Point makeTurn(Map map) {
        int[][] values = new int[map.size][map.size];
        for (int x = 0; x < map.size; x++) {
            for (int y = 0; y < map.size; y++) {
                values[x][y] = 0;
            }
        }
        values = block(map, values);
        return getBestField(values);
    }

    private Point getBestField(int[][] valueMap) {
        ArrayList<Point> points = new ArrayList<Point>();
        int max = 0;
        for (int x = 0; x < valueMap.length; x++) {
            for (int y = 0; y < valueMap.length; y++) {
                if (valueMap[x][y] > max) {
                    max = valueMap[x][y];
                    points.clear();
                    points.add(new Point(x, y));
                } else if (valueMap[x][y] == max) {
                    points.add(new Point(x, y));
                }
            }
        }

        return points.get(new Random().nextInt(points.size()));
    }

    private int[][] block(Map map, int[][] values) {
        System.out.println("           Formation: "
                + getFormationInDirection(new Point(0, 0), map, 0, 1, invertField(myField)));
        return values;
    }

    private static int[] getFormations(Point p, Map map, Field field) {
        int[] result = new int[8];
        getFormationInDirection(p, map, 0, -1, field);
        getFormationInDirection(p, map, 0, -1, field);
        return result;
    }

    public static int getFormationInDirection(Point p, Map map, int xDir, int yDir, Field match) {
        int result = 0;
        Point currentPos = new Point(p.x + xDir, p.y + yDir);
        boolean fieldAfterSpace = false;
        boolean spaceUsed = false;
        boolean twoSpaces = false;
        boolean inBounds = getPointInBounds(currentPos, map.size);
        while (!twoSpaces && inBounds) {
            System.out.println("Pos: " + currentPos.x + ", " + currentPos.y);
            inBounds = getPointInBounds(currentPos, map.size);
            if (inBounds) {
                if (map.getFieldAt(currentPos) == Field.NONE) {
                    if (spaceUsed) {
                        System.out.println("Two spaces! " + result);
                        twoSpaces = true;
                    } else {
                        spaceUsed = true;
                        System.out.println("One space! " + result);
                    }
                } else if (map.getFieldAt(currentPos) == match) {
                    if (spaceUsed && !fieldAfterSpace) {
                        result++;
                        fieldAfterSpace = true;
                    }
                    result++;
                    System.out.println("Matches: " + result);
                }
            }
            currentPos = new Point(currentPos.x + xDir, currentPos.y + yDir);
        }
        return result;
    }

    private static boolean getPointInBounds(Point p, int size) {
        if (p.x < size && p.x >= 0 && p.y < size && p.y >= 0) {
            return true;
        }
        return false;
    }

    public static Field invertField(Field f) {
        if (f == Field.O) {
            return Field.X;
        } else if (f == Field.X) {
            return Field.O;
        } else {
            return Field.NONE;
        }
    }
}
