package videojuegos;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AtributosEntidad implements ItemListener{
	
		JComboBox<String> ATR;
		JPanel p2_3;
		JPanel p2_4;
		JLabel ATRIBUTOS;

		public AtributosEntidad(JComboBox<String> atr, JPanel P2_3, JPanel P2_4, JLabel atributos) {
			ATR = atr;
			p2_3 = P2_3;
			p2_4 = P2_4;
			ATRIBUTOS = atributos;
		}
		
	    public void itemStateChanged(ItemEvent event) {
	       if (event.getStateChange() == ItemEvent.SELECTED) {
	          Object item = event.getItem();
	          switch (item.toString()) {
	          case "Videogame" :
	        	  ATR.addItem("name");
	        	  ATR.addItem("release_date");
	        	  ATR.addItem("genre");
	        	  p2_3.setBounds(50, 10, 600, 100);
	        	  p2_4.setBounds(50, 10, 600, 100);
	        	  p2_3.add(ATRIBUTOS);
	        	  p2_4.add(ATR);
	        	  break;
	          case "Developer" :
	        	  ATR.addItem("name");
	        	  ATR.addItem("founder_date");
	        	  ATR.addItem("location");
	        	  ATR.addItem("founder");
	        	  p2_3.add(ATRIBUTOS);
	        	  p2_4.add(ATR);
	        	  break;
	          case "Publisher" :
	        	  ATR.addItem("name");
	        	  ATR.addItem("founder_date");
	        	  ATR.addItem("location");
	        	  ATR.addItem("founder");
	        	  p2_3.add(ATRIBUTOS);
	        	  p2_4.add(ATR);
	        	  break;
	          case "Company" :
	        	  ATR.addItem("name");
	        	  ATR.addItem("founder_date");
	        	  ATR.addItem("location");
	        	  ATR.addItem("founder");
	        	  p2_3.add(ATRIBUTOS);
	        	  p2_4.add(ATR);
	        	  break;
	          case "GameEngine" :
	        	  ATR.addItem("name");
	        	  ATR.addItem("release_date");
	        	  ATR.addItem("genre");
	        	  p2_3.add(ATRIBUTOS);
	        	  p2_4.add(ATR);
	        	  break;
	          }
	       }
	    }       
}
