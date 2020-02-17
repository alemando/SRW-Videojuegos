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
        titulo.setBounds(80, 10, 600, 50);
        add(titulo);
	    
        JLabel lent = new JLabel("Selecciona una entidad");
        lent.setBounds(250,70 , 200, 60);
        add(lent);
        
        entidades = new JComboBox<ComboItem>(listarEntidades());
        entidades.setBounds(250,120, 200, 50);
        entidades.setSelectedIndex(-1);
        add(entidades);
        entidades.addActionListener(this);
        
        JLabel latr = new JLabel("Selecciona un atributo");
        latr.setBounds(250,180 , 200, 60);
        add(latr);
        
        atributos = new JComboBox<ComboItem>();
        atributos.setBounds(250,250, 200, 50);
        atributos.setSelectedIndex(-1);
        add(atributos);
        
        
        
        
        
        
        //entidades.addItemListener(new AtributosEntidad(atr,P2_3,P2_4,atributo));
	    
        JLabel atributo = new JLabel("Selecciona un atributo");
	
	    
	    
	    
	    
	   
        
	    
        
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
		}else if(e.getActionCommand().equals("comboBoxChanged")) {
			atributos.removeAllItems();
			ComboItem[] opciones = listarAtributos(((ComboItem) entidades.getSelectedItem()).getValue());
			for (int i = 0; i < opciones.length; i++) {				
				atributos.addItem(opciones[i]);
		}atributos.setSelectedIndex(-1);
			
		}
		
	}
	
	public ComboItem[] listarEntidades() {
		ComboItem[] listaEntidades = new ComboItem[0];
		try {

			ArrayList<QuerySolution> entidades = new RDFIntegracionEndPoint().consulta("PREFIX vdo: <http://www.videogames.com/>\r\n" + 
																						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+ 
																						"SELECT DISTINCT ?class\r\n" + "WHERE {\r\n" + "  ?class ?r owl:Class.\r\n"+ 
																						"FILTER( STRSTARTS(STR(?class),str(vdo:)) )\r\n" + "}");
			listaEntidades = new ComboItem[entidades.size()];
			for (int i = 0; i < entidades.size(); i++) {
				String entidad = entidades.get(i).getResource("?class").toString();
				listaEntidades[i] = new ComboItem(entidad, entidad.replace("http://www.videogames.com/", ""));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaEntidades;
	}
	
	public ComboItem[] listarAtributos(String entidad) {
		ComboItem[] listaAtributos = new ComboItem[0];
		try {

			ArrayList<QuerySolution> atributos =  new RDFIntegracionEndPoint().consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																						"SELECT DISTINCT ?atr\r\n" + 
																						"WHERE {\r\n" + 
																						"  ?atr rdfs:domain <"+entidad+">\r\n" + 												
																						"}");
			
			System.out.println(atributos);
			listaAtributos = new ComboItem[atributos.size()];
			for (int i = 0; i < atributos.size(); i++) {
				String atributo = atributos.get(i).getResource("atr").toString();
				listaAtributos[i] = new ComboItem(atributo, atributo.replace("http://www.videogames.com/", ""));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaAtributos;
	}
}

/*
Resource t = atributos.get(0).getResource("atr");

ArrayList<QuerySolution> resultados =  RDFiend.consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
"SELECT DISTINCT ?atr\r\n" + 
"WHERE {\r\n" + 
"  ?atr rdfs:domain <"+r+">\r\n" + 												
"}");*/

