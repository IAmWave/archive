/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI.Minimax;

import Map.Cell;
import Map.Map;

/**
 *
 * @author Vašek
 */
public class TrivialEvaluator implements Evaluator {

    private int getLineLength(int x, int y, int xAdd, int yAdd, Map map) {
        int count = 0;
        Cell owner = Cell.EMPTY;
        Cell cur;
        for (int i = 0; i < 5; i++) {
            cur = map.getCellAt(x + xAdd * i, y + yAdd * i);
            if (cur == Cell.X) {

                if (owner == Cell.O) { //vícebarvá řada
                    return count;
                } else {
                    owner = Cell.X;
                    count++;
                }
            } else if (cur == Cell.O) {
                if (owner == Cell.X) { //vícebarvá řada
                    return count;
                } else {
                    owner = Cell.O;
                    count--;
                }
            }
        }
        return count;/*
        if (owner == Cell.X) {
            return count;
        }
        return -count;*/
    }

    @Override
    public double evaluate(Map map) {

        return getLineLength(6, 6, 0, 1, map);
    }
}
