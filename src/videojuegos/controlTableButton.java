package videojuegos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class controlTableButton implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String id = e.getActionCommand();
        JOptionPane.showMessageDialog(null, 
               "Button clicked for row "+id);
    }
}