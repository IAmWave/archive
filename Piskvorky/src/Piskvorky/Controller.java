package Piskvorky;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author Martin Strouhal
 */
class Controller implements ActionListener {
    Frame frame;
    public Controller(Frame frame) {
        this.frame = frame;
        this.frame.addAL(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        frame.handleAction(ae);
        /*
        Point pozice = view.zjistiCisloPole(tlacitko);

        model.setPole(pozice, model.getHrac());
        model.zmenHrace();
        model.shoda();
        view.prekresli();
        tlacitko.setEnabled(false);
        if(model.isPc() == true){
            Point s = ai.hraj();
            
            model.setPole(s, Znak.O);
            model.zmenHrace();
            model.shoda();
            view.prekresli();
            view.setEnabledTlacitko(s.x, s.y);
        }*/
    }
}
