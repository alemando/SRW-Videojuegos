package videojuegos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class InterfazInstancias extends Container implements ActionListener {
	
	JComboBox jcmbEntidades, jcmbProperties;
	JTextField filtro;
	
    public InterfazInstancias() {
    	setBackground(Color.WHITE);
        setLayout(null);
        
        jcmbEntidades = new JComboBox<ComboItem>(itemsListaEntidades());
        jcmbEntidades.setBounds(225, 50, 250, 50);
        add(jcmbEntidades);
        jcmbEntidades.addActionListener(this);
        
        jcmbProperties = new JComboBox<ComboItem>();
        jcmbProperties.setBounds(225, 150, 250, 50);
        add(jcmbProperties);
        
        filtro = new JTextField("Valor de filtro");
        filtro.setBounds(225, 250, 250, 50);
        add(filtro);
        
        JButton botonBusqueda = new JButton("Busqueda");
        botonBusqueda.setBounds(100, 350, 200, 50);
        add(botonBusqueda);
        botonBusqueda.addActionListener(this);
        
        JButton botonBusquedaIndirecta = new JButton("Busqueda Indirecta");
        botonBusquedaIndirecta.setBounds(400, 350, 200, 50);
        add(botonBusquedaIndirecta);
        botonBusquedaIndirecta.addActionListener(this);
        
        JButton botonVolver = new JButton("Volver");
        botonVolver.setBounds(225, 600, 250, 50);
        add(botonVolver);
        botonVolver.addActionListener(this);
        
        //Para recargar las vistas
        SwingUtilities.updateComponentTreeUI(Main.frame);
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("comboBoxChanged")) {
			jcmbProperties.removeAllItems();
			ComboItem[] opciones = itemsListaProperties(((ComboItem) jcmbEntidades.getSelectedItem()).getValue());
			for (int i = 0; i < opciones.length; i++) {
				
				jcmbProperties.addItem(opciones[i]);
			}
			
		}
		else if(e.getActionCommand().equals("Volver")) {
			Main.frame.setContentPane(new InterfazPrincipal());
		}else if(e.getActionCommand().equals("Busqueda")) {
			busquedaSinFiltro(((ComboItem) jcmbEntidades.getSelectedItem()).getValue());
		}else if(e.getActionCommand().equals("Busqueda Indirecta")) {
			System.out.println("Por hacer");
		}
		
	}
	
	public ArrayList<String> busquedaSinFiltro(String entidad) {
		ArrayList<String> propertiesRDFOWL = buscarProperties(entidad);
		ArrayList<String> arreglo = new ArrayList<String>();
		
		//Consulta owl
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"SELECT DISTINCT ?individual ";
		
		for (int i = 0; i < propertiesRDFOWL.size(); i++) {
			queryString += "?v"+i;
		}
		
		queryString +="WHERE {?individual a <"+ entidad +"> .\r\n"
			+ "?individual a owl:NamedIndividual.\r\n";
		for (int i = 0; i < propertiesRDFOWL.size(); i++) {
			queryString += "?individual <"+propertiesRDFOWL.get(i)+"> ?v"+i+".\r\n";
		}
			
			queryString += "}";
		ArrayList<QuerySolution> resultados = new OWLVirtuosoEndPoint().consulta(queryString);
		for (int i = 0; i < resultados.size(); i++) {
			
			String item = resultados.get(i).toString();
			System.out.println(item);
		}
		
		//ConsultaRDF
		try {
			
			ArrayList<QuerySolution> resultados2 = new RDFEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?n ?r\r\n" + 
					"WHERE {\r\n" + 
					"  ?n ?r <"+ entidad + ">.\r\n" +

					"}");
			for (int i = 0; i < resultados2.size(); i++) {
				
				String item = resultados2.get(i).toString();
				arreglo.add(item);
				System.out.println(item);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//equivalents class
		ArrayList<String> classEquivalents = buscarClassEquivalents(entidad);
		
		for (int i = 0; i < classEquivalents.size(); i++) {
			String item = classEquivalents.get(i);
			
			if(item.contains("http://localhost:2020/resource/vocab/") ) {
				ArrayList<String> propertiesD2RQ = buscarProperties(item);
			}else if(item.contains("http://dbpedia.org/ontology/") ){
				ArrayList<String> propertiesDBPedia = buscarProperties(item);
			}
		}
		
		return arreglo;
	}
	
	public ArrayList<String> buscarClassEquivalents(String entidad) {
		ArrayList<String> arreglo = new ArrayList<String>();
		try {
			
			ArrayList<QuerySolution> resultados = new RDFIntegracionEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?class\r\n" + 
					"WHERE {?class a owl:Class.\n" + 
					" ?class owl:equivalentClass <"+ entidad +">."+
					"}");
			for (int i = 0; i < resultados.size(); i++) {
				
				String item = resultados.get(i).get("?class").toString();
				arreglo.add(item);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arreglo;
	}

	
	public ArrayList<String> buscarProperties(String entidad) {
		ArrayList<String> arreglo = new ArrayList<String>();
		try {
			
			ArrayList<QuerySolution> resultados = new RDFIntegracionEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?n\r\n" + 
					"WHERE {\r\n" + 
					"  ?n rdfs:domain <"+ entidad + ">.\r\n" +
					"  ?n a owl:DatatypeProperty .\r\n" +
					"}");
			for (int i = 0; i < resultados.size(); i++) {
				
				String item = resultados.get(i).get("?n").toString();
				arreglo.add(item);
				System.out.println(item);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arreglo;
	}
	
	public ComboItem[] itemsListaProperties(String entidad) {
		ComboItem[] arreglo = new ComboItem[0];
		try {
			
			ArrayList<QuerySolution> resultados = new RDFIntegracionEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?n\r\n" + 
					"WHERE {\r\n" + 
					"  ?n rdfs:domain <"+ entidad + ">.\r\n" +
					"  ?n a owl:DatatypeProperty .\r\n" +
					"}");
				arreglo = new ComboItem[resultados.size()+1];
				arreglo[0] = new ComboItem("null", "Seleccione...");
				for (int i = 0; i < resultados.size(); i++) {
					
				String item = resultados.get(i).get("?n").toString();
				arreglo[(i+1)] = new ComboItem(item, item.replace("http://www.videogames.com/", ""));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arreglo;
	}
	
	public ComboItem[] itemsListaEntidades() {
		ComboItem[] arreglo = new ComboItem[0];
		try {
			
			ArrayList<QuerySolution> resultados = new RDFIntegracionEndPoint().consulta("PREFIX vdo: <http://www.videogames.com/>\r\n"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?class\r\n" + 
					"WHERE {\r\n" + 
					"  ?class a owl:Class.\r\n" + 
					"  FILTER( STRSTARTS(STR(?class),str(vdo:)) )\r\n" + 
					"}");
			arreglo = new ComboItem[resultados.size()+1];
			arreglo[0] = new ComboItem("null", "Seleccione...");
			for (int i = 0; i < resultados.size(); i++) {
				
				String item = resultados.get(i).get("?class").toString();
				arreglo[(i+1)] = new ComboItem(item, item.replace("http://www.videogames.com/", ""));
				
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arreglo;
	}
}
