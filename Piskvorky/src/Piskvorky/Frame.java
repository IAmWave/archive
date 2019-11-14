package Piskvorky;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Martin Strouhal, Václav Volhejn
 */
class Frame extends JFrame {
    private static final int size = 10;
    private JButton buttons[][] = new JButton[size][size];
    private Map map;
    private AI ai;


    Frame() {
        super("Piškvorky");
        setLayout(new GridLayout(size, size));
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[x].length; y++) {
                buttons[x][y] = new JButton();
                add(buttons[x][y]);
            }
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(size*25, size*25);

        map = new Map(size);
        ai = new AI(Field.O);
        setLocationRelativeTo(null);
    }

    public void handleAction(ActionEvent ae) {
        JButton button = (JButton) ae.getSource();
        placeStone(button, Field.X);
        Point p = ai.makeTurn(map);
        placeStone(buttons[p.x][p.y], Field.O);
    }

    private void placeStone(JButton button, Field type) {
        button.setEnabled(false);
        Point pos = getButtonPosition(button);
        map.setFieldAt(pos.x, pos.y, type);
        button.setMargin(new Insets(0, 0, 0, 0));
        if (type == Field.X) {
            button.setText("X");
        } else if(type==Field.O) {
            button.setText("O");
        }

    }

    
    /*hotovo*/

    public void addAL(ActionListener al) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].addActionListener(al);
            }
        }
    }

    /*hotovo*/
    /*public void prekresli() {
    for (int i = 0; i < poleButtons.length; i++) {
    for (int j = 0; j < poleButtons[i].length; j++) {
    if (model.getPoleIndex(new Point(i, j)) == Znak.X) {
    poleButtons[i][j].setText("x");
    } else {
    if (model.getPoleIndex(new Point(i, j)) == Znak.O) {
    poleButtons[i][j].setText("o");
    }
    }
    }
    }
    }*/

    /*hotovo*/
    public Point getButtonPosition(JButton button) {
        for (int x = 0; x < buttons.length; x++) {
            for (int y = 0; y < buttons[x].length; y++) {
                if (button.equals(buttons[x][y])) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }
}
