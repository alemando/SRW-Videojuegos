package videojuegos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import videojuegos.JTableModel;


public class InterfazInstancias extends Container implements ActionListener {
	
	JComboBox jcmbEntidades, jcmbProperties;
	JTextField filtro;
	JTable table;
	
    public InterfazInstancias() {
    	setBackground(Color.WHITE);
        setLayout(null);
        
        jcmbEntidades = new JComboBox<ComboItem>(itemsListaEntidades());
        jcmbEntidades.setBounds(50, 50, 150, 25);
        add(jcmbEntidades);
        jcmbEntidades.addActionListener(this);
        
        jcmbProperties = new JComboBox<ComboItem>();
        jcmbProperties.setBounds(250, 50, 150, 25);
        add(jcmbProperties);
        
        filtro = new JTextField("");
        filtro.setBounds(450, 50, 150, 25);
        add(filtro);
        
        JButton botonBusqueda = new JButton("Busqueda");
        botonBusqueda.setBounds(100, 100, 200, 30);
        add(botonBusqueda);
        botonBusqueda.addActionListener(this);
        
        JButton botonBusquedaIndirecta = new JButton("Busqueda Indirecta");
        botonBusquedaIndirecta.setBounds(400, 100, 200, 30);
        add(botonBusquedaIndirecta);
        botonBusquedaIndirecta.addActionListener(this);
        
        
		table = new JTable(); 
	    JScrollPane scrollPane = new JScrollPane(table);
	    table.setFillsViewportHeight(true);
	    
	    TableCellRenderer buttonRenderer = new JTableButtonRenderer();
	    //table.getColumn("Relaciones").setCellRenderer(buttonRenderer);
	    table.addMouseListener(new JTableButtonMouseListener(table));
	    add(scrollPane);
	    
	    scrollPane.setBounds(25, 150, 650, 450);
        
        
        JButton botonVolver = new JButton("Volver");
        botonVolver.setBounds(300, 625, 100, 25);
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
	
public ArrayList<Object[]> busquedaConFiltro(String entidad, String propiedad, String filtro) {
		
		ArrayList<String> propertiesRDFOWL = buscarProperties(entidad);
				
		ArrayList<Object[]> arreglo = new ArrayList<Object[]>();
		
		//Consulta owl
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"SELECT DISTINCT ?individual ";
		
		for (int i = 0; i < propertiesRDFOWL.size(); i++) {
			queryString += "?v"+i;
		}
		
		queryString +=" WHERE {?individual a <"+ entidad +"> .\r\n"
			+ "?individual a owl:NamedIndividual.\r\n";
		for (int i = 0; i < propertiesRDFOWL.size(); i++) {
			queryString += "?individual <"+propertiesRDFOWL.get(i)+"> ?v"+i+".\r\n";
		}
			
			queryString += "}";
		ArrayList<QuerySolution> resultados = new OWLVirtuosoEndPoint().consulta(queryString);
		for (int i = 0; i < resultados.size(); i++) {
			
			QuerySolution item = resultados.get(i);
			Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
			for (int j = 0; j < propertiesRDFOWL.size(); j++) {
				arregloA[j] = (item.get(("?v"+j))).toString();
			}
			JButton boton = new JButton("Relaciones");
			boton.setActionCommand(item.get(("?individual")).toString());
			boton.addActionListener(new controlTableButton());
			arregloA[propertiesRDFOWL.size()] = boton;
			arreglo.add(arregloA);
		}
		
		//ConsultaRDF
		try {
			String queryString2 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?individual ";
			
			for (int i = 0; i < propertiesRDFOWL.size(); i++) {
				queryString2 += "?v"+i;
			}
			
			queryString2 +=" WHERE {?individual rdfs:type <"+ entidad +"> .\r\n";
			for (int i = 0; i < propertiesRDFOWL.size(); i++) {
				queryString2 += "?individual <"+propertiesRDFOWL.get(i)+"> ?v"+i+".\r\n";
			}
				
			queryString2 += "}";
			
			ArrayList<QuerySolution> resultados2 = new RDFEndPoint().consulta(queryString2);
			for (int i = 0; i < resultados2.size(); i++) {
				
				QuerySolution item = resultados2.get(i);
				Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
				for (int j = 0; j < propertiesRDFOWL.size(); j++) {
					arregloA[j] = (item.get(("?v"+j))).toString();
				}
				JButton boton = new JButton("Relaciones");
				boton.setActionCommand(item.get(("?individual")).toString());
				boton.addActionListener(new controlTableButton());
				arregloA[propertiesRDFOWL.size()] = boton;
				arreglo.add(arregloA);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//equivalents class
		ArrayList<String> classEquivalents = buscarClassEquivalents(entidad);
		
		for (int i = 0; i < classEquivalents.size(); i++) {
			String item = classEquivalents.get(i);
			
			//Consulta D2RQ
			if(item.contains("http://localhost:2020/resource/vocab/") ) {
				ArrayList<String> propertiesD2RQ = buscarProperties(item);
				String queryString3 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
						"SELECT DISTINCT ?individual ";
				
				for (int i1 = 0; i1 < propertiesD2RQ.size(); i1++) {
					queryString3 += "?v"+i1;
				}
				
				queryString3 +=" WHERE {?individual a <"+ item +"> .\r\n";
				for (int i1 = 0; i1 < propertiesD2RQ.size(); i1++) {
					queryString3 += "?individual <"+propertiesD2RQ.get(i1)+"> ?v"+i1+".\r\n";
				}
					
				queryString3 += "}";
				ArrayList<QuerySolution> resultados3 = new D2RQEndPoint().consulta(queryString3);
				for (int i1 = 0; i1 < resultados3.size(); i1++) {
					
					QuerySolution item1 = resultados3.get(i1);
					Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
					for (int j = 0; j < propertiesRDFOWL.size(); j++) {
						arregloA[j] = (item1.get(("?v"+j))).toString();
					}
					JButton boton = new JButton("Relaciones");
					boton.setActionCommand(item1.get(("?individual")).toString());
					boton.addActionListener(new controlTableButton());
					arregloA[propertiesRDFOWL.size()] = boton;
					arreglo.add(arregloA);
				}
				
			//Consulta DBPedia
			}else if(item.contains("http://dbpedia.org/ontology/") ){
				ArrayList<String> propertiesDBPedia = buscarProperties(item);
				String queryString3 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
						"SELECT DISTINCT ?individual ";
				
				for (int i1 = 0; i1 < propertiesDBPedia.size(); i1++) {
					queryString3 += "?v"+i1;
				}
				
				queryString3 +=" WHERE {?individual a <"+ item +"> .\r\n";
				for (int i1 = 0; i1 < propertiesDBPedia.size(); i1++) {
					if(propertiesDBPedia.get(i1).equals("rdfs:label")) {
						queryString3 += "?individual "+propertiesDBPedia.get(i1)+" ?v"+i1+".\r\n";
					}else {
						queryString3 += "?individual <"+propertiesDBPedia.get(i1)+"> ?v"+i1+".\r\n";
					}
				}
					
				queryString3 += "} LIMIT 10";
				ArrayList<QuerySolution> resultados3 = new DBPediaEndPoint().consulta(queryString3);
				for (int i1 = 0; i1 < resultados3.size(); i1++) {
					
					QuerySolution item1 = resultados3.get(i1);
					Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
					for (int j = 0; j < propertiesRDFOWL.size(); j++) {
						arregloA[j] = (item1.get(("?v"+j))).toString();
					}
					JButton boton = new JButton("Relaciones");
					boton.setActionCommand(item1.get(("?individual")).toString());
					boton.addActionListener(new controlTableButton());
					arregloA[propertiesRDFOWL.size()] = boton;
					arreglo.add(arregloA);
				}
			}
		}
		
		JTableModel jtable;
		
		Object[][] tabla = new Object[arreglo.size()][propertiesRDFOWL.size()+1 ];
		
		for (int i = 0; i < arreglo.size(); i++) {
			for (int j = 0; j < arreglo.get(i).length; j++) {
				tabla[i][j] = arreglo.get(i)[j];
			}
		}
		/*
		for (int i = 0; i < tabla.length; i++) {
			for (int j = 0; j < tabla[i].length; j++) {
				System.out.println(tabla[i][j]);
			}
		}*/
		
		

		if(propertiesRDFOWL.size() == 4) {
			jtable = new JTableModel(new String[] {" ", " ", " ", " ", "Relaciones"},
					new Class<?>[] {String.class, String.class, String.class, String.class, JButton.class}, tabla);
			
		}else {
			jtable = new JTableModel(new String[] {" ", " ", " ", "Relaciones"},
					new Class<?>[] {String.class, String.class, String.class, JButton.class}, tabla);
		}
		
		table.setModel(jtable);
		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
	    table.getColumn("Relaciones").setCellRenderer(buttonRenderer);
		return arreglo;
	}
	
	public ArrayList<Object[]> busquedaSinFiltro(String entidad) {
		
		ArrayList<String> propertiesRDFOWL = buscarProperties(entidad);
				
		ArrayList<Object[]> arreglo = new ArrayList<Object[]>();
		
		//Consulta owl
		String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"SELECT DISTINCT ?individual ";
		
		for (int i = 0; i < propertiesRDFOWL.size(); i++) {
			queryString += "?v"+i;
		}
		
		queryString +=" WHERE {?individual a <"+ entidad +"> .\r\n"
			+ "?individual a owl:NamedIndividual.\r\n";
		for (int i = 0; i < propertiesRDFOWL.size(); i++) {
			queryString += "?individual <"+propertiesRDFOWL.get(i)+"> ?v"+i+".\r\n";
		}
			
			queryString += "}";
		ArrayList<QuerySolution> resultados = new OWLVirtuosoEndPoint().consulta(queryString);
		for (int i = 0; i < resultados.size(); i++) {
			
			QuerySolution item = resultados.get(i);
			Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
			for (int j = 0; j < propertiesRDFOWL.size(); j++) {
				arregloA[j] = (item.get(("?v"+j))).toString();
			}
			JButton boton = new JButton("Relaciones");
			boton.setActionCommand(item.get(("?individual")).toString());
			boton.addActionListener(new controlTableButton());
			arregloA[propertiesRDFOWL.size()] = boton;
			arreglo.add(arregloA);
		}
		
		//ConsultaRDF
		try {
			String queryString2 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?individual ";
			
			for (int i = 0; i < propertiesRDFOWL.size(); i++) {
				queryString2 += "?v"+i;
			}
			
			queryString2 +=" WHERE {?individual rdfs:type <"+ entidad +"> .\r\n";
			for (int i = 0; i < propertiesRDFOWL.size(); i++) {
				queryString2 += "?individual <"+propertiesRDFOWL.get(i)+"> ?v"+i+".\r\n";
			}
				
			queryString2 += "}";
			
			ArrayList<QuerySolution> resultados2 = new RDFEndPoint().consulta(queryString2);
			for (int i = 0; i < resultados2.size(); i++) {
				
				QuerySolution item = resultados2.get(i);
				Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
				for (int j = 0; j < propertiesRDFOWL.size(); j++) {
					arregloA[j] = (item.get(("?v"+j))).toString();
				}
				JButton boton = new JButton("Relaciones");
				boton.setActionCommand(item.get(("?individual")).toString());
				boton.addActionListener(new controlTableButton());
				arregloA[propertiesRDFOWL.size()] = boton;
				arreglo.add(arregloA);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//equivalents class
		ArrayList<String> classEquivalents = buscarClassEquivalents(entidad);
		
		for (int i = 0; i < classEquivalents.size(); i++) {
			String item = classEquivalents.get(i);
			
			//Consulta D2RQ
			if(item.contains("http://localhost:2020/resource/vocab/") ) {
				ArrayList<String> propertiesD2RQ = buscarProperties(item);
				String queryString3 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
						"SELECT DISTINCT ?individual ";
				
				for (int i1 = 0; i1 < propertiesD2RQ.size(); i1++) {
					queryString3 += "?v"+i1;
				}
				
				queryString3 +=" WHERE {?individual a <"+ item +"> .\r\n";
				for (int i1 = 0; i1 < propertiesD2RQ.size(); i1++) {
					queryString3 += "?individual <"+propertiesD2RQ.get(i1)+"> ?v"+i1+".\r\n";
				}
					
				queryString3 += "}";
				ArrayList<QuerySolution> resultados3 = new D2RQEndPoint().consulta(queryString3);
				for (int i1 = 0; i1 < resultados3.size(); i1++) {
					
					QuerySolution item1 = resultados3.get(i1);
					Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
					for (int j = 0; j < propertiesRDFOWL.size(); j++) {
						arregloA[j] = (item1.get(("?v"+j))).toString();
					}
					JButton boton = new JButton("Relaciones");
					boton.setActionCommand(item1.get(("?individual")).toString());
					boton.addActionListener(new controlTableButton());
					arregloA[propertiesRDFOWL.size()] = boton;
					arreglo.add(arregloA);
				}
				
			//Consulta DBPedia
			}else if(item.contains("http://dbpedia.org/ontology/") ){
				ArrayList<String> propertiesDBPedia = buscarProperties(item);
				String queryString3 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
						"SELECT DISTINCT ?individual ";
				
				for (int i1 = 0; i1 < propertiesDBPedia.size(); i1++) {
					queryString3 += "?v"+i1;
				}
				
				queryString3 +=" WHERE {?individual a <"+ item +"> .\r\n";
				for (int i1 = 0; i1 < propertiesDBPedia.size(); i1++) {
					if(propertiesDBPedia.get(i1).equals("rdfs:label")) {
						queryString3 += "?individual "+propertiesDBPedia.get(i1)+" ?v"+i1+".\r\n";
					}else {
						queryString3 += "?individual <"+propertiesDBPedia.get(i1)+"> ?v"+i1+".\r\n";
					}
				}
					
				queryString3 += "} LIMIT 10";
				ArrayList<QuerySolution> resultados3 = new DBPediaEndPoint().consulta(queryString3);
				for (int i1 = 0; i1 < resultados3.size(); i1++) {
					
					QuerySolution item1 = resultados3.get(i1);
					Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
					for (int j = 0; j < propertiesRDFOWL.size(); j++) {
						arregloA[j] = (item1.get(("?v"+j))).toString();
					}
					JButton boton = new JButton("Relaciones");
					boton.setActionCommand(item1.get(("?individual")).toString());
					boton.addActionListener(new controlTableButton());
					arregloA[propertiesRDFOWL.size()] = boton;
					arreglo.add(arregloA);
				}
			}
		}
		
		JTableModel jtable;
		
		Object[][] tabla = new Object[arreglo.size()][propertiesRDFOWL.size()+1 ];
		
		for (int i = 0; i < arreglo.size(); i++) {
			for (int j = 0; j < arreglo.get(i).length; j++) {
				tabla[i][j] = arreglo.get(i)[j];
			}
		}
		/*
		for (int i = 0; i < tabla.length; i++) {
			for (int j = 0; j < tabla[i].length; j++) {
				System.out.println(tabla[i][j]);
			}
		}*/
		
		

		if(propertiesRDFOWL.size() == 4) {
			jtable = new JTableModel(new String[] {" ", " ", " ", " ", "Relaciones"},
					new Class<?>[] {String.class, String.class, String.class, String.class, JButton.class}, tabla);
			
		}else {
			jtable = new JTableModel(new String[] {" ", " ", " ", "Relaciones"},
					new Class<?>[] {String.class, String.class, String.class, JButton.class}, tabla);
		}
		
		table.setModel(jtable);
		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
	    table.getColumn("Relaciones").setCellRenderer(buttonRenderer);
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
					"} ");
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
				if(item.equals("https://www.w3.org/2000/01/rdf-schema#label")) {
					arreglo.add("rdfs:label");
				}else {
					arreglo.add(item);
				}
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
