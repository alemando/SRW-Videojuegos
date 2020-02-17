package videojuegos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;



public class InterfazInstanciaIgual extends Container implements ActionListener {
	
	JTextArea TA1;
	JComboBox jcmbEntidades, jcmbInstancias, jcmbProperties;
	JScrollPane JS1;
	JPanel P1;
	
	
    public InterfazInstanciaIgual() {
    	
    	setBackground(Color.WHITE);
        setLayout(null);
        
        jcmbEntidades = new JComboBox<ComboItem>(itemsListaEntidades());
        jcmbEntidades.setBounds(225, 25, 250, 50);
        add(jcmbEntidades);
        jcmbEntidades.addActionListener(this);
        
        jcmbInstancias = new JComboBox<ComboItem>();
        jcmbInstancias.setBounds(225, 100, 250, 50);
        add(jcmbInstancias);
        
        jcmbProperties = new JComboBox<ComboItem>();
        jcmbProperties.setBounds(225, 175, 250, 50);
        add(jcmbProperties);
        
        JButton botonBusqueda = new JButton("Buscar");
        botonBusqueda.setBounds(225, 250, 250, 50);
        add(botonBusqueda);
        botonBusqueda.addActionListener(this);
                
        JButton botonVolver = new JButton("Volver");
        botonVolver.setBounds(225, 600, 250, 50);
        add(botonVolver);
        botonVolver.addActionListener(this);
        
        TA1 = new JTextArea(14,56);
        JS1 = new JScrollPane(TA1);
		JS1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		P1 = new JPanel();
		P1.setBounds(1,340,700,250);
		P1.add(JS1);
		add(P1);
        TA1.setEditable(false);        
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
			
			jcmbInstancias.removeAllItems();
			ComboItem[] opcionesI;
			try {
				opcionesI = itemsInstancias(((ComboItem) jcmbEntidades.getSelectedItem()).getValue());
				for (int i= 0; i< opcionesI.length;i++) {
					
					jcmbInstancias.addItem(opcionesI[i]);
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
				
			}
			
		
		else if(e.getActionCommand().equals("Volver")) {
			Main.frame.setContentPane(new InterfazPrincipal());
		}else if(e.getActionCommand().equals("Buscar")) {
			TA1.setText("");
			ArrayList<String> salidaFinal= consultaOntologias();
			String salida ="";
			for (int i= 0; i< salidaFinal.size();i++) {
				salida += salidaFinal.get(i).toString();
				salida +="\n";
				
			}
			if(salida.isEmpty()) {
				JOptionPane.showMessageDialog(getComponent(0), " No se encontraron coincidencias");
				
			}else {
				
				TA1.setText(TA1.getText()+ salida+"\n");
			}
		}
		
		
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
				arreglo[0] = new ComboItem("null", "Propiedades...");
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
			arreglo[0] = new ComboItem("null", "Entidades...");
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
	
	public ComboItem[] itemsInstancias(String entidad) throws FileNotFoundException {
		ComboItem[] arreglo = new ComboItem[0];
		ArrayList<QuerySolution> resultados = new OWLVirtuosoEndPoint().consulta("PREFIX vdo: <http://www.videogames.com/>\r\n"+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"SELECT DISTINCT ?individual\r\n" + 
				"WHERE {\r\n" + 
				"?individual rdf:type <"+entidad+">.\r\n"+
				"}");
		arreglo = new ComboItem[resultados.size()+1];
		arreglo[0] = new ComboItem("null", "Instancias...");
		for (int i = 0; i < resultados.size(); i++) {
			
			String item = resultados.get(i).get("?individual").toString();
			arreglo[(i+1)] = new ComboItem(item, item.replace("http://www.videogames.com/", ""));
			
			
		}
		
		return arreglo;

	}
	
	public ArrayList<String> busqueda(){
		String entidad = ((ComboItem) jcmbEntidades.getSelectedItem()).getValue();
		String instancia = ((ComboItem) jcmbInstancias.getSelectedItem()).getValue();
		String propiedad = ((ComboItem) jcmbProperties.getSelectedItem()).getValue();
		String query = "PREFIX vdo: <http://www.videogames.com/>\r\n"+
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"+
						"SELECT DISTINCT ?individual ?found\r\n"+
						"WHERE {\r\n"+
							"?individual  rdf:type <"+ entidad +">.\r\n"+
							"?individual  <"+ propiedad +"> ?found.\r\n"+
						"}";
		ArrayList<QuerySolution> busquedaOWL = new OWLVirtuosoEndPoint().consulta(query);
		ArrayList<String> arreglo = new ArrayList<String>();
		for(int i = 0; i < busquedaOWL.size(); i++) {
			
			String clase = busquedaOWL.get(i).get("?individual").toString();
			if(clase.equals(instancia)) {
				String valor = busquedaOWL.get(i).get("?found").toString();
				arreglo.add(valor);
				
			}
		}
		for( int j= 0; j< arreglo.size(); j++) {
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
	
	public ArrayList<String> consultaOntologias(){
		ArrayList<String> consulta = new ArrayList<String>();
		String entidad = ((ComboItem) jcmbEntidades.getSelectedItem()).getValue();
		String instancia = ((ComboItem) jcmbInstancias.getSelectedItem()).getValue();
		String propiedad = ((ComboItem) jcmbProperties.getSelectedItem()).getValue();
		ArrayList<String> entidades = buscarClassEquivalents(entidad);
		ArrayList<String> propiedades = buscarPropertyEquivalents(entidad, propiedad);
		ArrayList<String> valores = busqueda();
		
		if(valores.isEmpty() == false) {
			for(int k =0 ; k< valores.size();k++) {
		
		String query = "PREFIX vdo: <http://www.videogames.com/>\r\n"+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"+
				"SELECT DISTINCT ?individual ?found\r\n"+
				"WHERE {\r\n"+
					"?individual  rdf:type <"+ entidad +">.\r\n"+
					"?individual  <"+ propiedad +"> ?found.\r\n"+
					"FILTER(?found='"+valores.get(k)+"')\r\n"+
				"}";
		ArrayList<QuerySolution> busquedaOWL = new OWLVirtuosoEndPoint().consulta(query);
		for(int j=0; j<busquedaOWL.size(); j++) {
			consulta.add((busquedaOWL.get(j).get("?individual")).toString());
		}
			}
		}
		if(valores.isEmpty() == false) {
			for(int k =0 ; k< valores.size();k++) {
		
		String queryR = "PREFIX vdo: <http://www.videogames.com/>\r\n"+
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n"+
				"SELECT DISTINCT ?individual ?found\r\n"+
				"WHERE {\r\n"+
					"?individual  rdf:type <"+ entidad +">.\r\n"+
					"?individual  <"+ propiedad +"> ?found.\r\n"+
					"FILTER(?found='"+valores.get(k)+"')\r\n"+
				"}";
		
		try {
			ArrayList<QuerySolution> busquedaRDF = new RDFEndPoint().consulta(queryR);
			for(int j=0; j<busquedaRDF.size(); j++) {
				consulta.add((busquedaRDF.get(j).get("?individual")).toString());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			}
		}


		for(int i = 0; i < entidades.size();i++) {
			String itemE = entidades.get(i);
			String itemP = propiedades.get(i);
			System.out.println(itemE+" "+itemP);
			
			if(itemE.contains("http://localhost:2020/resource/vocab/") && itemP.contains("http://localhost:2020/resource/vocab/")) {
				if(valores.isEmpty() == false) {
				for(int k =0 ; k< valores.size();k++) {
					
				
				String queryD2 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"SELECT DISTINCT ?individual ?found\r\n"+
						"WHERE {\r\n"+
							"?individual  a <"+ itemE +">.\r\n"+
							"?individual  <"+ itemP +"> ?found.\r\n"+
							"FILTER(?found='"+valores.get(k)+"')\r\n"+
						"}";
				ArrayList<QuerySolution> resultadosD2 = new D2RQEndPoint().consulta(queryD2);
				if(resultadosD2.isEmpty()==false) {
				for(int j=0; j< resultadosD2.size(); j++) {
					consulta.add((resultadosD2.get(j).get("?individual")).toString());
	
				}
				}
				}
				}
			}
			else if(itemE.contains("http://dbpedia.org/") && itemP.contains("http://dbpedia.org/")) {
				if(valores.isEmpty() == false) {
				for(int k =0 ; k< valores.size();k++) {					
					
					String queryD2 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
							"SELECT DISTINCT ?individual ?found\r\n"+
							"WHERE {\r\n"+
								"?individual a <"+ itemE +">.\r\n"+
								"?individual <"+ itemP +"> ?found.\r\n"+						
							"}";
					ArrayList<QuerySolution> resultadosDB = new DBPediaEndPoint().consulta(queryD2);
					
					if(resultadosDB.isEmpty()==false) {
					for(int j=0; j< resultadosDB.size(); j++) {
						String clase = resultadosDB.get(i).get("?found").toString();
						String a=clase.toLowerCase();
						String b= valores.get(k).toLowerCase();
						if(a.contains(b)) {
							
							consulta.add((resultadosDB.get(j).get("?individual")).toString());
						}			
		
					}
					}
					}
			}
				
			}
		}	
		
		return consulta;
		}
}
