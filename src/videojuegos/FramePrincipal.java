package videojuegos;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class FramePrincipal extends JFrame{
	
	public FramePrincipal() {
		super("E-transporte cultural");
        setSize(700,700);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
	}

}
