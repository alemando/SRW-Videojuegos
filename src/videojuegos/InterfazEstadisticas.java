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
	
	JComboBox atributos, entidades;
	
	
    public InterfazEstadisticas() {
        
        setBackground(Color.WHITE);
        setLayout(null);  
        
        JLabel titulo = new JLabel("<html><font size=7><center>ESTADISTICAS POR ATRIBUTO</center></font></html>");
        JLabel entidad = new JLabel("Selecciona una entidad");
        JLabel atributo = new JLabel("Selecciona un atributo");
        String[] entidades = { "Videogame","Developer", "Publisher","Company","GameEngine"};
        String[] atributos = {};
	
	    entidades = new JComboBox<ComboItem>();
	    entidades.setSelectedIndex(-1);

	    JComboBox<String> atr = new JComboBox<String>(atributos);

        
        
	    
        
	    //entidades.addItemListener(new AtributosEntidad(atr,P2_3,P2_4,atributo));
        
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
	
	public ComboItem[] listarEntidades {
			try {
			
			ArrayList<QuerySolution> entidades = new RDFIntegracionEndPoint.consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																	"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																	"SELECT DISTINCT ?class\r\n" + 
																	"WHERE {\r\n" + 
																	"  ?class ?r owl:Class.\r\n" + 
																	"  FILTER( STRSTARTS(STR(?class),str(vdo:)) )\r\n" + 
																	"}");
			Resource r = entidades.get(0).getResource("class");
			System.out.println(r);
			
			ArrayList<QuerySolution> atributos =  RDFiend.consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																	"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																	"SELECT DISTINCT ?atr\r\n" + 
																	"WHERE {\r\n" + 
																	"  ?atr rdfs:domain <"+r+">\r\n" + 												
																	"}");
			Resource t = atributos.get(0).getResource("atr");
			
			ArrayList<QuerySolution> resultados =  RDFiend.consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																	"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																	"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																	"SELECT DISTINCT ?atr\r\n" + 
																	"WHERE {\r\n" + 
																	"  ?atr rdfs:domain <"+r+">\r\n" + 												
																	"}");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
