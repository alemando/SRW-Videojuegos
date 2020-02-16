package videojuegos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.rdf.model.Resource;

public class InterfazEstadisticas extends Container implements ActionListener {
	
	
    public InterfazEstadisticas() {
        
        setBackground(Color.WHITE);
        setLayout(null);
        
        try {
			RDFIntegracionEndPoint RDFiend = new RDFIntegracionEndPoint();
			ArrayList<QuerySolution> entidades = RDFiend.consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																	"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																	"SELECT DISTINCT ?class\r\n" + 
																	"WHERE {\r\n" + 
																	"  ?class ?r owl:Class.\r\n" + 
																	"  FILTER( STRSTARTS(STR(?class),str(vdo:)) )\r\n" + 
																	"}");
			Resource r = entidades.get(0).getResource("class");
			
			/*ArrayList<QuerySolution> atributos =  RDFiend.consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																	"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																	"SELECT DISTINCT ?atr\r\n" + 
																	"WHERE {\r\n" + 
																	"  ?atr rdfs:domain "+r+"\r\n" + 
																	"  FILTER( STRSTARTS(STR(?class),str(vdo:)) )\r\n" + 
																	"}");*/
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        JLabel titulo = new JLabel("<html><font size=7><center>ESTADISTICAS POR ATRIBUTO</center></font></html>");
        JLabel entidad = new JLabel("Selecciona una entidad");
        JLabel atributo = new JLabel("Selecciona un atributo");
        String[] entidades = { "Videogame","Developer", "Publisher","Company","GameEngine"};
        String[] atributos = {};
	
	    JComboBox<String> ent = new JComboBox<String>(entidades);
	    ent.setSelectedIndex(-1);

	    JComboBox<String> atr = new JComboBox<String>(atributos);

        
        JPanel P1 = new JPanel();
        JPanel P2 = new JPanel();
        JPanel P2_1 = new JPanel();
        JPanel P2_2 = new JPanel();
        JPanel P2_3 = new JPanel();
        JPanel P2_4 = new JPanel();
        JPanel P3 = new JPanel();
        
	    P2_1.add(entidad);
	    P2_2.add(ent);
        add(P1);
        add(P2);
        add(P3);
        
        P1.setBounds(50, 10, 600, 100);
        P1.add(titulo);   
        P2.setBounds(150, 100, 200, 100);
        P2.add(P2_1);
        P2.add(P2_2);
        P2.add(P2_3);
        P2.add(P2_4);
        
	    ent.addItemListener(new AtributosEntidad(atr,P2_3,P2_4,atributo));
        
        JButton botonVolver = new JButton("Volver");
        botonVolver.setBounds(225, 600, 250, 50);
        add(botonVolver);
        botonVolver.addActionListener(this);
        
        //Para recargar las vistas
        SwingUtilities.updateComponentTreeUI(Main.frame);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Volver")) {
			Main.frame.setContentPane(new InterfazPrincipal());
		}
		
	}
}
