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

public class InterfazEstadisticas extends Container implements ActionListener  {
	
	JComboBox atributos, entidades, opcionFiltroNumero;
	JLabel latr,lent,titulo,Lfiltro,SL1;
	JTextField T1;
	JButton botonConsultarN,botonConsultarS,botonVolver;
	
    public InterfazEstadisticas() {
        
        setBackground(Color.WHITE);
        setLayout(null);  
        
        titulo = new JLabel("<html><font size=7><center>ESTADISTICAS POR ATRIBUTO</center></font></html>");
        titulo.setBounds(80, 10, 600, 50);
        add(titulo);
	    
        lent = new JLabel("Selecciona una entidad");
        lent.setBounds(250,70 , 200, 60);
        add(lent);
        
        entidades = new JComboBox<ComboItem>(listarEntidades());
        entidades.setBounds(250,120, 200, 50);
        entidades.setSelectedIndex(-1);
        entidades.setName("entidades");
        entidades.setActionCommand("cambioEntidad");
        add(entidades);
        entidades.addActionListener(this);
        
        latr = new JLabel("Selecciona un atributo");
        latr.setBounds(250,180 , 200, 60);
        add(latr);
        
        atributos = new JComboBox<ComboItem>();
        atributos.setBounds(250,250, 200, 50);
        atributos.setSelectedIndex(-1);
        atributos.setName("atributos");
        atributos.setActionCommand("cambioAtributo");
        add(atributos);
        atributos.addActionListener(this);
        
        Lfiltro = new JLabel("Si deseas puedes filtrar la consulta");
        Lfiltro.setBounds(250,300,200,60);
        
        SL1 = new JLabel("Que contenga: ");
        SL1.setBounds(200,360,200,50);
        
        String [] opcionesFiltroNumero = {"Mayor que", "Menor que"};
        opcionFiltroNumero = new JComboBox<String>(opcionesFiltroNumero);
        opcionFiltroNumero.setBounds(150,360, 150, 50);
        opcionFiltroNumero.setSelectedIndex(-1);
        
        T1 = new JTextField("Valor del filtro");
        T1.setBounds(350, 360, 200, 50);
        
        botonConsultarN = new JButton("Consultar");
        botonConsultarN.setBounds(225, 490, 250, 50);
        botonConsultarN.setActionCommand("ConsultarN");
        botonConsultarN.addActionListener(this);
        
        botonConsultarS = new JButton("Consultar");
        botonConsultarS.setBounds(225, 490, 250, 50);
        botonConsultarS.setActionCommand("ConsultarS");
        botonConsultarS.addActionListener(this);
        
        botonVolver = new JButton("Volver");
        botonVolver.setBounds(225, 600, 250, 50);
        add(botonVolver);
        botonVolver.addActionListener(this);
        
        //Para recargar las vistas
        SwingUtilities.updateComponentTreeUI(Main.frame);

    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("cambioEntidad")) {	
			atributos.removeActionListener(this);
			atributos.removeAllItems();
			ComboItem[] opciones = listarAtributos(((ComboItem) entidades.getSelectedItem()).getValue());
			for (int i = 0; i < opciones.length; i++) {				
				atributos.addItem(opciones[i]);
			}
			atributos.setSelectedIndex(-1);
			atributos.addActionListener(this);
		}else if(e.getActionCommand().equals("cambioAtributo")) {
			String tipo = getTipo(((ComboItem) atributos.getSelectedItem()).getValue());
			if(tipo.equals("http://www.w3.org/2001/XMLSchema#dateTimeStamp")) {
				crearFiltro(0);
			}else {
				crearFiltro(1);
			}
		}else if(e.getActionCommand().equals("Volver")) {
			Main.frame.setContentPane(new InterfazPrincipal());
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
				String entidad = entidades.get(i).getResource("class").toString();
				listaEntidades[i] = new ComboItem(entidad, entidad.replace("http://www.videogames.com/", ""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return listaEntidades;
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
			listaAtributos = new ComboItem[atributos.size()];
			for (int i = 0; i < atributos.size(); i++) {
				String atributo = atributos.get(i).getResource("atr").toString();
				listaAtributos[i] = new ComboItem(atributo, atributo.replace("http://www.videogames.com/", ""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return listaAtributos;
	}
	
	public String getTipo(String atributo) {
		String tipo="";
		try {
			ArrayList<QuerySolution> type =  new RDFIntegracionEndPoint().consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																					"SELECT DISTINCT ?tipo\r\n" + 
																					"WHERE {\r\n" + 
																					"<"+atributo+"> rdfs:range ?tipo\r\n" + 												
																					"}");
			tipo = type.get(0).getResource("tipo").toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return tipo;
	}
	
	public void crearFiltro(int a) {
		add(Lfiltro);
		switch (a){
			case 0:
				remove(SL1);
				remove(botonConsultarS);
				add(opcionFiltroNumero);
				add(T1);
				add(botonConsultarN);
				this.repaint();
				break;
			case 1:
				remove(opcionFiltroNumero);
				remove(botonConsultarN);
				add(SL1);
				add(T1);
				add(botonConsultarS);
				this.repaint();
				break;
		}
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

