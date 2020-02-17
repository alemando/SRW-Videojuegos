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
	JTextArea TA1;
	JScrollPane JS1;
	JPanel P1 ;
	
    public InterfazEstadisticas() {
        
        setBackground(Color.WHITE);
        setLayout(null);  
        
        titulo = new JLabel("<html><font size=5><center>ESTADISTICAS POR ATRIBUTO</center></font></html>");
        titulo.setBounds(200, 0, 600, 50);
        add(titulo);
	    
        lent = new JLabel("Selecciona una entidad");
        lent.setBounds(250,20 , 200, 50);
        add(lent);
        
        entidades = new JComboBox<ComboItem>(listarEntidades());
        entidades.setBounds(250,60, 200, 50);
        entidades.setSelectedIndex(-1);
        entidades.setName("entidades");
        entidades.setActionCommand("cambioEntidad");
        add(entidades);
        entidades.addActionListener(this);
        
        latr = new JLabel("Selecciona un atributo");
        latr.setBounds(250,110 , 200, 50);
        add(latr);
        
        atributos = new JComboBox<ComboItem>();
        atributos.setBounds(250,150, 200, 50);
        atributos.setSelectedIndex(-1);
        atributos.setName("atributos");
        atributos.setActionCommand("cambioAtributo");
        add(atributos);
        atributos.addActionListener(this);
        
        Lfiltro = new JLabel("Si deseas puedes filtrar la consulta");
        Lfiltro.setBounds(250,200,200,40);
        
        SL1 = new JLabel("Que contenga: ");
        SL1.setBounds(200,240,200,50);
        
        String [] opcionesFiltroNumero = {"Mayor que", "Menor que"};
        opcionFiltroNumero = new JComboBox<String>(opcionesFiltroNumero);
        opcionFiltroNumero.setBounds(150,240, 150, 50);
        opcionFiltroNumero.setSelectedIndex(-1);
        
        T1 = new JTextField();
        T1.setBounds(350, 240, 200, 50);
        
        botonConsultarN = new JButton("Consultar");
        botonConsultarN.setBounds(225, 290, 250, 50);
        botonConsultarN.setActionCommand("ConsultarN");
        botonConsultarN.addActionListener(this);
        
        botonConsultarS = new JButton("Consultar");
        botonConsultarS.setBounds(225, 290, 250, 50);
        botonConsultarS.setActionCommand("ConsultarS");
        botonConsultarS.addActionListener(this);
        
        botonVolver = new JButton("Volver");
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
			if(tipo.equals("http://www.w3.org/2001/XMLSchema#dateTimeStamp")) crearFiltro(0);
			else crearFiltro(1);
		}else if(e.getActionCommand().equals("Volver")) Main.frame.setContentPane(new InterfazPrincipal());
		else if(e.getActionCommand().equals("ConsultarN")) estadisticas(0);
		else if(e.getActionCommand().equals("ConsultarS")) estadisticas(1);
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
																						"?atr rdfs:domain <"+entidad+"> ;\r\n" +
																						"a owl:DatatypeProperty \r\n"+
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
	
	public void estadisticas(int a){
		TA1.setEditable(true);
		TA1.setText(TA1.getText()+"----------------------------------------------------------------Consultando-----------------------------------------------------------------\n");
		String select="";
		String filter=""; 
		switch(a) {
		case 0:
			select = "SELECT DISTINCT (COUNT(?ins) as ?instancias) (AVG(xsd:integer(SUBSTR(REPLACE(STR(?valor),'[^0-9]',''),1,4))) as ?promedio) " +
					"(MAX(xsd:integer(SUBSTR(REPLACE(STR(?valor),'[^0-9]',''),1,4))) as ?maximo) "+
					"(MIN(xsd:integer(SUBSTR(REPLACE(STR(?valor),'[^0-9]',''),1,4))) as ?minimo)\r\n";
			if(opcionFiltroNumero.getSelectedItem()==null)filter="";
			else if(opcionFiltroNumero.getSelectedItem().toString()=="Menor que" && !(T1.getText().equals(""))) filter = "FILTER (xsd:integer(SUBSTR(REPLACE(STR(?valor),'[^0-9]',''),1,4)) < "+Integer.parseInt(T1.getText())+")";
			else if(opcionFiltroNumero.getSelectedItem().toString()=="Mayor que" && !(T1.getText().equals(""))) filter = "FILTER (xsd:integer(SUBSTR(REPLACE(STR(?valor),'[^0-9]',''),1,4)) > "+Integer.parseInt(T1.getText())+") . \r\n";
			break;
		case 1:	
			select = "SELECT DISTINCT (COUNT(?ins) as ?instancias) (AVG(STRLEN(STR(?valor))) as ?longitudPromedio)\r\n"; 
			if(T1.getText().equals("")) filter="";				
			else filter="FILTER REGEX(STR(?valor),'"+T1.getText()+"','i') .\r\n";
			break;
		}
		
		//consulta owl
		String entidad = ((ComboItem)entidades.getSelectedItem()).getValue();
		String atributo = ((ComboItem)atributos.getSelectedItem()).getValue(); 
		TA1.setText(TA1.getText()+"For OWL--------------------------------------------------------------\n");
		String qs = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n";

			qs += 	select + 
					"WHERE {\r\n" +
					"?ins a <"+entidad+"> ;\r\n"+
					"<"+atributo+"> ?valor \r\n"+ 
					filter +
					"}";
		ArrayList<QuerySolution> estadisticasOwl = new OWLVirtuosoEndPoint().consulta(qs);
		TA1.setText(TA1.getText()+estadisticasOwl+"\n");
		
		//consulta rdf
		TA1.setText(TA1.getText()+"For RDF---------------------------------------------------------------\n");
		String qs2 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n";
	
			qs2 += 	select+ 
					"WHERE {\r\n" +
					"?ins rdfs:type <"+entidad+"> ;\r\n"+
					"<"+atributo+"> ?valor \r\n"+
					filter +
					"}";
		try {
			ArrayList<QuerySolution> estadisticasRdf = new RDFEndPoint().consulta(qs2);
			TA1.setText(TA1.getText()+estadisticasRdf+"\n");
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ArrayList<String> clasesEquivalentes = buscarClasesEquivalentes(entidad);
		ArrayList<String> atributosEquivalentes = buscarPropiedadesEquivalentes(atributo);
		
		for(int i = 0;i<clasesEquivalentes.size();i++) {
			String clase = clasesEquivalentes.get(i);
			if(clase.contains("http://dbpedia.org/ontology/")){
				TA1.setText(TA1.getText()+"For DBPedia------------------------------------------------------------\n");
				String qs3 ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n"+
						select+
						"WHERE {\r\n";
				for (int j = 0; j<atributosEquivalentes.size();j++) {
					String propiedad = atributosEquivalentes.get(j);
					if(propiedad.equals("rdfs:label")) {
						qs3+="?ins a <"+clase+"> ;\r\n"+
								" "+propiedad+" ?valor \r\n"+
								filter+
								"}";
						ArrayList<QuerySolution> estadisticasDbPedia = new DBPediaEndPoint().consulta(qs3);
						TA1.setText(TA1.getText()+estadisticasDbPedia+"\n");
					}
					else if(propiedad.contains("http://dbpedia.org/")) {
						qs3+="?ins a <"+clase+"> .\r\n"+
								"?ins <"+propiedad+"> ?valor \r\n"+
								filter+
								"}";
						ArrayList<QuerySolution> estadisticasDbPedia = new DBPediaEndPoint().consulta(qs3);
						TA1.setText(TA1.getText()+estadisticasDbPedia+"\n");
					}
				}							
			}else if(clase.contains("http://localhost:2020/resource/vocab/")) {
				TA1.setText(TA1.getText()+"For D2RQ----------------------------------------------------------------\n");
				String qs4 ="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n"+
						select+
						"WHERE {\r\n";
				for (int j = 0; j<atributosEquivalentes.size();j++) {
					String propiedad = atributosEquivalentes.get(j);
					if(propiedad.contains("http://localhost:2020/resource/vocab/")) {
						String qs5 = qs4+"?ins a <"+clase+"> ;\r\n"+
								"<"+propiedad+"> ?valor \r\n"+
								filter+
								"}";
						ArrayList<QuerySolution> estadisticasD2RQ = new D2RQEndPoint().consulta(qs5);
						TA1.setText(TA1.getText()+estadisticasD2RQ+"\n");
					}
				}
			}
		}
		
		TA1.setText(TA1.getText()+"--------------------------------------------------------------------Terminado----------------------------------------------------------------------\n");
		TA1.setEditable(false);
	}
	
	public ArrayList<String> buscarClasesEquivalentes (String entidad){
		ArrayList<String> clasesEquivalentes = new ArrayList<String>();
		try {
			ArrayList<QuerySolution> clases = new RDFIntegracionEndPoint().consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																					"SELECT DISTINCT ?class\r\n" + 
																					"WHERE {\r\n" +
																					"?class a owl:Class ;"+
																					"owl:equivalentClass <"+entidad+"> \r\n" + 												
																					"}");
			for(int i=0;i<clases.size();i++) {
				clasesEquivalentes.add(clases.get(i).getResource("class").toString());
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return clasesEquivalentes;
		
	}
	
	public ArrayList<String> buscarPropiedadesEquivalentes (String atributo){
		ArrayList<String> propiedadesEquivalentes = new ArrayList<String>();
		try {
			ArrayList<QuerySolution> propiedades = new RDFIntegracionEndPoint().consulta("PREFIX vdo: <http://videogames.com/>\r\n"+
																					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
																					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n"+
																					"SELECT DISTINCT ?class\r\n" + 
																					"WHERE {\r\n" +
																					"{?class owl:equivalentProperty <"+atributo+"> }\r\n"+
																					"UNION\r\n"+
																					"{<"+atributo+"> owl:equivalentProperty ?class }\r\n"+
																					"}");
			for(int i=0;i<propiedades.size();i++) {
				if(propiedades.get(i).getResource("class").toString().equals("https://www.w3.org/2000/01/rdf-schema#label"))propiedadesEquivalentes.add("rdfs:label");
				else propiedadesEquivalentes.add(propiedades.get(i).getResource("class").toString());
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return propiedadesEquivalentes;
		
	}
}

