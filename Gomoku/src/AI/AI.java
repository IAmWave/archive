/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import Map.Map;
import java.awt.Point;

/**
 *
 * @author Vašek
 */
public interface AI {

    public double[][] eval(Map map);

    public Point makeMove(Map map);
}
