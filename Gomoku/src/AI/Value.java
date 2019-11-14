/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package AI;

/**
 *
 * @author Vašek
 */
public class Value {

    static final int myFiveValue = 100000;
    static final int hisFiveValue = 9999;
    static final int myFourValue = 1;//35;
    static final int hisFourValue = 1;//34;
    static final int myThreeValue = 1;//15;
    static final int hisThreeValue = 1;//14;
    static final int myTwoValue = 1;//6;
    static final int hisTwoValue = 1;//5;
    static final double diagonal = 0.41;
    int[][][] values;

    //my    his
    //5     5
    //4     4
    //etc
    public Value() {
        values = new int[2][4][2];
    }

    @Override
    public Value clone() {
        Value v = new Value();
        v.setValues(values);
        return v;
    }

    public void setValues(int[][][] v) {
        for (int i = 0; i < 4; i++) {
            values[0][i][0] = v[0][i][0];
            values[0][i][1] = v[0][i][1];
            values[1][i][0] = v[1][i][0];
            values[1][i][1] = v[1][i][1];
        }
    }

    public void addValue(boolean my, int size, boolean diagonal) {
        if (size > 5 || size < 2) {
            return;
        }
        if (my) {
            values[0][size - 2][0]++;
            if (diagonal) {
                values[0][size - 2][1]++;
            }
        } else {
            values[1][size - 2][0]++;
            if (diagonal) {
                values[1][size - 2][1]++;
            }
        }
    }

    public int getValue(boolean my, int size, boolean diagonal) {
        if (size > 5 || size < 2) {
            return 0;
        }
        if (my) {
            if (!diagonal) {
                return values[0][size - 2][0];
            } else {
                return values[0][size - 2][1];
            }
        } else {
            if (!diagonal) {
                return values[1][size - 2][0];
            } else {
                return values[1][size - 2][1];
            }
        }
    }

    public int getValue(boolean my, int size) {
        if (size > 5 || size < 2) {
            return 0;
        }
        if (my) {
            return values[0][size - 2][0];
        } else {
            return values[1][size - 2][0];
        }
    }

    public int[][][] getValues() {
        return values.clone();
    }

    //pořadí sil: pětka, čtyřka/čtyřková vidlice, trojková vidlice, ostatní
    public double getStrength() {
        if (getValue(true, 5, false) > 0) { //dává pětku
            return myFiveValue;
        }
        if (getValue(false, 5, false) > 0) {
            return hisFiveValue;
        }
        double str = 0;
        int winPoints = 0;
        winPoints += getValue(true, 4) * 2;
        winPoints += getValue(true, 3);
        if (winPoints >= 4) { //vytváří neblokovanou čtyřku/vidličku
            str += 500;
            if (getValue(true, 4) > 0) { //čtyřkové vidličky jsou silnější
                str += 300;
            }
        }
        winPoints = 0;
        winPoints += getValue(false, 4) * 2;
        winPoints += getValue(false, 3);
        if (winPoints >= 4) { //soupeř vytváří neblokovanou čtyřku/vidličku
            str += 350;
            if (getValue(false, 4) > 0) { //čtyřkové vidličky jsou silnější
                str += 300;
            }
        }
        if (str == 0) {
            str += (getValue(true, 2) + getValue(true, 2, true) * diagonal) * myTwoValue;
            str += (getValue(false, 2) + getValue(false, 2, true) * diagonal) * hisTwoValue;
            str += (getValue(true, 3) + getValue(true, 3, true) * diagonal) * myThreeValue;
            str += (getValue(false, 3) + getValue(false, 3, true) * diagonal) * hisThreeValue;
            str += (getValue(true, 4) + getValue(true, 4, true) * diagonal) * myFourValue;
            str += (getValue(false, 4) + getValue(false, 4, true) * diagonal) * hisFourValue;

        }



        return str;
    }
}
//staré:
/*
 *      Získávání síly 1
 int str = 0;
 for (int i = 0; i < 2; i++) {
 for (int j = 0; j < 4; j++) {
 str += values[i][j] * (j + 1) - i;
 }
 }
 return str;

 */
