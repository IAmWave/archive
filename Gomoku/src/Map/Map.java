/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Map;

import java.awt.Point;

/**
 *
 * @author Vašek
 */
public class Map {

    int turn = 1;
    int[][] turnNo = new int[15][15];
    Cell[][] map = new Cell[15][15];
    boolean xMoves = true;
    int lastX = -1, lastY = -1;

    public Map(Map m) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                map[i][j] = m.map[i][j];
            }
        }
        xMoves = m.xMoves;
        turn = m.turn;
    }

    public Map() {
        for (int i = 0; i < 15; i++) {
            for (int i2 = 0; i2 < 15; i2++) {
                map[i][i2] = Cell.EMPTY;
            }
        }
    }

    public int getTurnNumber() {
        return turn;
    }

    public int getTurnNumberAt(int x, int y) {
        if (x < 0 || y < 0 || x >= 15 || y >= 15) {
            return 0;
        }
        return turnNo[x][y];
    }

    public Cell getCellAt(int x, int y) {
        if (x < 0 || y < 0 || x >= 15 || y >= 15) {
            return Cell.EMPTY;
        }
        return map[x][y];
    }

    public Cell[][] getMap() {
        return map;
    }

    public boolean attemptMove(Point p) {
        return attemptMove(p.x, p.y);
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public boolean attemptMove(int x, int y) {
        if (x < 0 || y < 0 || x >= 15 || y >= 15) {
            return false;
        }
        if (map[x][y] == Cell.EMPTY) {
            turnNo[x][y] = turn;
            turn++;
            lastX = x;
            lastY = y;

            if (xMoves) {
                map[x][y] = Cell.X;

            } else {
                map[x][y] = Cell.O;
            }
            xMoves = !xMoves;
            return true;
        }
        return false;
    }

    public boolean xMoves() {
        return xMoves;
    }

    public Cell movingPlayer() {
        if (xMoves) {
            return Cell.X;
        } else {
            return Cell.O;
        }
    }

    public Cell waitingPlayer() {
        if (xMoves) {
            return Cell.O;
        } else {
            return Cell.X;
        }
    }

    public void clear() {
        for (int i = 0; i < 15; i++) {
            for (int i2 = 0; i2 < 15; i2++) {
                map[i][i2] = Cell.EMPTY;
            }
        }
        xMoves = true;
        turn = 1;
    }

    public Cell checkWin() { //empty = není vítězství
        Cell result;
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 15; y++) {
                result = testLine(x, y, 1, 0);
                if (result != Cell.EMPTY) {
                    return result;
                }
            }
        }
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 11; y++) {
                result = testLine(x, y, 0, 1);
                if (result != Cell.EMPTY) {
                    return result;
                }
            }
        }
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                result = testLine(x, y, 1, 1);
                if (result != Cell.EMPTY) {
                    return result;
                }
            }
        }
        for (int x = 5; x < 15; x++) {
            for (int y = 0; y < 11; y++) {
                result = testLine(x, y, -1, 1);
                if (result != Cell.EMPTY) {
                    return result;
                }
            }
        }

        return Cell.EMPTY;
    }

    public Cell testLine(int x, int y, int xAdd, int yAdd) {
        Cell c;
        if (getCellAt(x, y) == Cell.EMPTY) {
            return Cell.EMPTY;
        } else {
            c = getCellAt(x, y);
        }
        int count = 0;
        Point mark = null;
        for (int i = 1; i < 5; i++) {
            if (getCellAt(x + xAdd * i, y + yAdd * i) != c) {
                return Cell.EMPTY;
            }
        }
        return c;
    }

    public static String writeTurn(int x, int y) {
        return (char) (x + 97) + String.valueOf(y + 1) + "";
    }
}
