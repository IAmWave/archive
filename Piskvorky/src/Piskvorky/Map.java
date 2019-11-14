/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Piskvorky;

import java.awt.Point;

/**
 *
 * @author Vašek
 */
public class Map {

    int size;
    Field[][] map;

    public Map(int size) {
        map = new Field[size][size];
        this.size = size;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                map[x][y] = Field.NONE;
            }
        }
    }

    public Field getFieldAt(int x, int y) {
        return map[x][y];
    }

    public Field getFieldAt(Point p) {
        return map[p.x][p.y];
    }

    public void setFieldAt(int x, int y, Field type) {
        map[x][y] = type;
    }
    
    public Field getWin(){
        for (int x = 0; x < size; x++) {
        for (int y = 0; y < size; y++) {
            
            if(getFormationInDirection(new Point(x, y), this, -1, 0, Field.X))
        }    
        }
        return Field.NONE;
    }
    
    private int getMaxFormation(Point p, Field field){
    int[] formations
    }
}
