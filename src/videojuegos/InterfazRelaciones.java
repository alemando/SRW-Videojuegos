package videojuegos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.apache.jena.query.QuerySolution;

public class InterfazRelaciones extends Container implements ActionListener {
	
	JComboBox jcmbEntidades1, jcmbInstances1,jcmbEntidades2, jcmbInstances2;
	JLabel titulo;

    public InterfazRelaciones() {
    	setBackground(Color.WHITE);
        setLayout(null);
        
        
        jcmbEntidades1 = new JComboBox<ComboItem>(itemsListaEntidades());
        jcmbEntidades1.setBounds(50, 50, 150, 25);
        add(jcmbEntidades1);
        jcmbEntidades1.addActionListener(this);
        jcmbEntidades2 = new JComboBox<ComboItem>(itemsListaEntidades());
        jcmbEntidades2.setBounds(50, 250, 150, 25);
        add(jcmbEntidades2);
        jcmbEntidades2.addActionListener(this);
        
        jcmbInstances1 = new JComboBox<ComboItem>();
        jcmbInstances1.setBounds(250, 50, 150, 25);
        add(jcmbInstances1);
        jcmbInstances2 = new JComboBox<ComboItem>();
        jcmbInstances2.setBounds(250, 250, 150, 25);
        add(jcmbInstances2);
        
        titulo = new JLabel("<html><font size=5><center>¿relacion?</center></font></html>");
        titulo.setBounds(200, 500, 600, 50);
        add(titulo);
        
        
        JButton botonBusqueda = new JButton("Busqueda");
        botonBusqueda.setBounds(100, 100, 200, 30);
        add(botonBusqueda);
        botonBusqueda.addActionListener(this);
        
        JButton botonBusquedaIndirecta = new JButton("Busqueda Indirecta");
        botonBusquedaIndirecta.setBounds(400, 100, 200, 30);
        add(botonBusquedaIndirecta);
        botonBusquedaIndirecta.addActionListener(this);
        
        
	    
	    TableCellRenderer buttonRenderer = new JTableButtonRenderer();
	    //table.getColumn("Relaciones").setCellRenderer(buttonRenderer);
	    
        
        
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
			jcmbInstances1.removeAllItems();
			jcmbInstances2.removeAllItems();

			ComboItem[] opciones = itemsListaProperties(((ComboItem) jcmbEntidades1.getSelectedItem()).getValue());
			ComboItem[] opciones2 = itemsListaProperties(((ComboItem) jcmbEntidades2.getSelectedItem()).getValue());
			for (int i = 0; i < opciones.length; i++) {
				
				jcmbInstances1.addItem(opciones[i]);
			}
			for (int i = 0; i < opciones2.length; i++) {
				
				jcmbInstances2.addItem(opciones2[i]);
			}
			
		}
		else if(e.getActionCommand().equals("Volver")) {
			Main.frame.setContentPane(new InterfazPrincipal());
		}else if(e.getActionCommand().equals("Busqueda")) {
			
			if(((ComboItem) jcmbInstances1.getSelectedItem()).getLabel().equals("Seleccione...")){
			}else {
				busquedaDirectaRelaciones(((ComboItem) jcmbInstances1.getSelectedItem()).getValue(),((ComboItem) jcmbInstances2.getSelectedItem()).getValue());
			}
			
		}else if(e.getActionCommand().equals("Busqueda Indirecta")) {
			busquedaIndirectaRelaciones(((ComboItem) jcmbInstances1.getSelectedItem()).getValue(),((ComboItem) jcmbInstances2.getSelectedItem()).getValue());
		}
		
	}

	
public void busquedaDirectaRelaciones (String instancia1, String instancia2) {
	
	String bits1[]=instancia1.split("/");
    String last1 = bits1[bits1.length-1];
    String pre1=instancia1.substring(0,instancia1.length()-last1.length());
    String bits2[]=instancia2.split("/");
    String last2 = bits2[bits2.length-1];
    String pre2=instancia2.substring(0,instancia2.length()-last2.length());

    try {
		String query="PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX pre1: <"+pre1+">\r\n" +
				"PREFIX pre2: <"+pre2+">\r\n" +
				"SELECT DISTINCT ?relation WHERE {\r\n" + 
					"{\r\n" + 
					"pre1:"+last1+" ?relation pre2:"+last2+".\r\n" + 
					"}\r\n" + 
					"UNION\r\n" + 
					"{\r\n" + 
					"pre2:"+last2+" ?relation pre1:"+last1+"\r\n" + 
					"}\r\n" + 					
				"}";
				ArrayList<QuerySolution>resultado=new ConbinedEndpoint().allConsult(query				
					);
				if(resultado.size()>0) {
					titulo.setText("hay relación");
				}
				else{
					titulo.setText("no hay relación");
				}
	} catch (Exception e) {
		titulo.setText("no hay relación");

		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	}

public void busquedaIndirectaRelaciones(String instancia1, String instancia2) {
	
	String bits1[]=instancia1.split("/");
    String last1 = bits1[bits1.length-1];
    String pre1=instancia1.substring(0,instancia1.length()-last1.length());
    String bits2[]=instancia2.split("/");
    String last2 = bits2[bits2.length-1];
    String pre2=instancia2.substring(0,instancia2.length()-last2.length());

    try {
		String query="PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX pre1: <"+pre1+">\r\n" +
				"PREFIX pre2: <"+pre2+">\r\n" +
				"SELECT DISTINCT ?relation WHERE {\r\n" + 
					"{\r\n" + 
					"pre1:"+last1+" ?relation ?x. \r\n" +
					"?x ?relation2 pre2:"+last2+".\r\n" +
					"}\r\n" + 
					"UNION\r\n" + 
					
					"{\r\n" + 
					"pre2:"+last2+" ?relation ?x. \r\n" +
					"?x ?relation2 pre1:"+last1+"\r\n" +
					"}\r\n" + 					
				"}";
				ArrayList<QuerySolution>resultado=new ConbinedEndpoint().allConsult(query				
					);

			if(resultado.size()>0) {
				titulo.setText("hay relación");
			}
			else{
				titulo.setText("no hay relación");
			}
} catch (Exception e) {
	titulo.setText("no hay relación");

	// TODO Auto-generated catch block
	e.printStackTrace();
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
			if(propertiesRDFOWL.get(i).equals(propiedad)) {
				queryString += "FILTER REGEX ( STR(?v"+i+"), \""+filtro+"\", \"i\").\r\n";
			}
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
				if(propertiesRDFOWL.get(i).equals(propiedad)) {
					queryString2 += "FILTER REGEX ( STR(?v"+i+"), \""+filtro+"\", \"i\").\r\n";
				}
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
		ArrayList<String> propertyEquivalents = buscarPropertyEquivalents(entidad, propiedad);
		for (int i = 0; i < classEquivalents.size(); i++) {
			for (int j = 0; j < propertyEquivalents.size(); j++) {
				
				String property = propertyEquivalents.get(j);
				String item = classEquivalents.get(i);
				//Consulta D2RQ
				if(item.contains("http://localhost:2020/resource/vocab/") && property.contains("http://localhost:2020/resource/vocab/")) {
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
						if(propertiesD2RQ.get(i1).equals(property)) {
							queryString3 += "FILTER REGEX ( STR(?v"+i1+"), \""+filtro+"\", \"i\").\r\n";
						}
					}
					queryString3 += "}";
					ArrayList<QuerySolution> resultados3 = new D2RQEndPoint().consulta(queryString3);
					for (int i1 = 0; i1 < resultados3.size(); i1++) {
						
						QuerySolution item1 = resultados3.get(i1);
						Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
						for (int j1 = 0; j1 < propertiesRDFOWL.size(); j1++) {
							arregloA[j1] = (item1.get(("?v"+j1))).toString();
						}
						JButton boton = new JButton("Relaciones");
						boton.setActionCommand(item1.get(("?individual")).toString());
						boton.addActionListener(new controlTableButton());
						arregloA[propertiesRDFOWL.size()] = boton;
						arreglo.add(arregloA);
					}
					
				//Consulta DBPedia
				}else if(item.contains("http://dbpedia.org/ontology/") && property.contains("http://dbpedia.org/ontology/")){
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
							if(propertiesDBPedia.get(i1).equals(property)) {
								queryString += "FILTER REGEX ( STR(?v"+i1+"), \""+filtro+"\", \"i\").\r\n";
							}
						}else {
							queryString3 += "?individual <"+propertiesDBPedia.get(i1)+"> ?v"+i1+".\r\n";
							if(propertiesDBPedia.get(i1).equals(property)) {
								queryString3 += "FILTER REGEX ( STR(?v"+i1+"), \""+filtro+"\", \"i\").\r\n";
							}
						}
					}
					queryString3 += "} LIMIT 10";
					System.out.println(queryString3);
					ArrayList<QuerySolution> resultados3 = new DBPediaEndPoint().consulta(queryString3);
					for (int i1 = 0; i1 < resultados3.size(); i1++) {
						
						QuerySolution item1 = resultados3.get(i1);
						Object [] arregloA = new Object[propertiesRDFOWL.size()+1];
						for (int j1 = 0; j1 < propertiesRDFOWL.size(); j1++) {
							arregloA[j1] = (item1.get(("?v"+j1))).toString();
						}
						JButton boton = new JButton("Relaciones");
						boton.setActionCommand(item1.get(("?individual")).toString());
						boton.addActionListener(new controlTableButton());
						arregloA[propertiesRDFOWL.size()] = boton;
						arreglo.add(arregloA);
					}
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
		
		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
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
		
		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
		return arreglo;
	}
	
	
	
	public ArrayList<Object[]> busquedaIndirecta(String entidad) {
		
		ArrayList<String> propertiesRDFOWL = buscarProperties(entidad);
				
		ArrayList<Object[]> arreglo = new ArrayList<Object[]>();
		
		ArrayList<String> clases = buscarSubClass(entidad);
		
		for (int k = 0; k < clases.size(); k++) {
			entidad = clases.get(k);
			
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
		
		TableCellRenderer buttonRenderer = new JTableButtonRenderer();
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
	
	public ArrayList<String> buscarPropertyEquivalents(String entidad, String propiedad) {
		ArrayList<String> arreglo = new ArrayList<String>();
		try {
			ArrayList<QuerySolution> resultados = new RDFIntegracionEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?property\r\n" + 
					"WHERE {?property a owl:DatatypeProperty.\n" + 
					" ?class owl:equivalentClass <"+ entidad +">."+
					"  ?property rdfs:domain ?class.\r\n" +
					" ?property owl:equivalentProperty <"+ propiedad +">."+
					"} ");
			for (int i = 0; i < resultados.size(); i++) {
				
				String item = resultados.get(i).get("?property").toString();
				arreglo.add(item);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arreglo;
	}

	public ArrayList<String> buscarSubClass(String entidad) {
		ArrayList<String> arreglo = new ArrayList<String>();
		try {
			
			ArrayList<QuerySolution> resultados = new RDFIntegracionEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?subClass\r\n" + 
					"WHERE {\r\n" + 
					"  <"+ entidad + "> a owl:Class. \r\n" +
					"  ?subClass rdfs:subClassOf <"+ entidad + ">.\r\n" +
					"}");
			for (int i = 0; i < resultados.size(); i++) {
				
				String item = resultados.get(i).get("?subClass").toString();
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
		ArrayList<QuerySolution> resultados = new ConbinedEndpoint().conbinedConsult(entidad);
		ArrayList<String> equivalentes = buscarClassEquivalents(entidad);
		Iterator<String> iterator= equivalentes.iterator();
		while (iterator.hasNext()) {
			ArrayList<QuerySolution> equivalentQueryArray=new ConbinedEndpoint().conbinedConsult(iterator.next());
			resultados.addAll(equivalentQueryArray);
		}
		
		
			arreglo = new ComboItem[resultados.size()+1];
			arreglo[0] = new ComboItem("null", "Seleccione...");
			for (int i = 0; i < resultados.size(); i++) {
				
			String item = resultados.get(i).get("?instance").toString();
			String[] bits = item.split("/");
			String last = bits[bits.length-1];

			arreglo[(i+1)] = new ComboItem(item, last);
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
