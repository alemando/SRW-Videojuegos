package videojuegos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.jena.query.QuerySolution;

public class controlTableButton implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String id = e.getActionCommand();
		if(id.contains("http://www.videogames.com/")) {
			JOptionPane.showMessageDialog(null, (getRelacionClass(id, relacionClass(id))).replace("http://www.videogames.com/", ""));
		}else if(id.contains("http://localhost:2020/resource/")) {
			
			String clase = buscarClassD2RQ(id).get(0);
			JOptionPane.showMessageDialog(null, (getRelacionClassD2RQ(id, relacionClassD2RQ(clase))).replace("http://localhost:2020/resource/", ""));
		}
		
		
    }
	
	
	public ArrayList<String> relacionClass(String instancia) {
		ArrayList<String> arreglo = new ArrayList<String>();
		//Cosnsulta owl
		ArrayList<QuerySolution> resultados = new OWLVirtuosoEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"SELECT DISTINCT ?re\r\n" + 
				"WHERE {\r\n" + 
				"  <"+ instancia + "> a ?class .\r\n" +
				"?re rdfs:domain ?class.\r\n" +
				"?re a owl:ObjectProperty.\r\n" +
				"}");
		for (int i = 0; i < resultados.size(); i++) {
			
			String item = resultados.get(i).get("?re").toString();
			arreglo.add(item);
			
		}
		
		return arreglo;
	}
	
	public String getRelacionClass(String instancia, ArrayList<String> relacion) {
		String mensaje = "Relaciones \n";
		//Cosnsulta owl
		for (int i = 0; i < relacion.size(); i++) {
			ArrayList<QuerySolution> resultados = new OWLVirtuosoEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?valor\r\n" + 
					"WHERE {\r\n" + 
					"<"+ instancia+ ">  <"+ relacion.get(i) + "> ?valor .\r\n" +
					"}");
			for (int i1 = 0; i1 < resultados.size(); i1++) {
				
				String item = resultados.get(i1).get("?valor").toString();
				mensaje +=instancia+"  "+ relacion.get(i) +"  "+ item +"\n";
				
			}
		}
		
		return mensaje;
	}
	
	public ArrayList<String> relacionClassD2RQ(String instancia) {
		ArrayList<String> arreglo = new ArrayList<String>();
		ArrayList<QuerySolution> resultados;
		try {
			resultados = new RDFIntegracionEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?re\r\n" + 
					"WHERE {\r\n" + 
					"<"+ instancia +"> a owl:Class .\r\n" + 
					"?re rdfs:domain <"+ instancia +">.\r\n" + 
					"?re a owl:ObjectProperty."+ 
					"}");
			for (int i = 0; i < resultados.size(); i++) {
				
				String item = resultados.get(i).get("?re").toString();
				arreglo.add(item);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return arreglo;
	}
	
	public String getRelacionClassD2RQ(String instancia, ArrayList<String> relacion) {
		String mensaje = "";
		ArrayList<QuerySolution> resultados;

		for (int i = 0; i < relacion.size(); i++) {
			resultados = new D2RQEndPoint().consulta("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"SELECT DISTINCT ?valor\r\n" + 
					"WHERE {\r\n" + 
					"<"+ instancia+ ">  <"+ relacion.get(i) + "> ?valor .\r\n" +
					"}");
			for (int i1 = 0; i1 < resultados.size(); i1++) {
				
				String item = resultados.get(i1).get("?valor").toString();
				mensaje +=instancia+"  "+ relacion.get(i) +"  "+ item +"\n";
				
			}
		}
		
		
		return mensaje;
	}
	
	public ArrayList<String> buscarClassD2RQ(String instancia) {
		ArrayList<String> arreglo = new ArrayList<String>();
		ArrayList<QuerySolution> resultados = new D2RQEndPoint().consulta("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"SELECT DISTINCT ?class\r\n" + 
				"WHERE {\r\n" + 
				"   <"+ instancia + "> rdf:type ?class.\r\n" +
				"}");
		for (int i = 0; i < resultados.size(); i++) {
			
			String item = resultados.get(i).get("?class").toString();
			arreglo.add(item);
			
		}
		
		return arreglo;
	}
}
