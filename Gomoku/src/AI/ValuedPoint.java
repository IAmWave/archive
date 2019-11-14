/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

import java.awt.Point;

/**
 *
 * @author Vašek
 */
public class ValuedPoint extends Point {

    double v;

    public ValuedPoint(int x, int y, double v){
    this.x = x;
    this.y = y;
    this.v = v;
    }
    
    public ValuedPoint(Point p, double v) {
        this.x = p.x;
        this.y = p.y;
        this.v = v;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
