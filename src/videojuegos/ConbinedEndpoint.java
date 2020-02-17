package videojuegos;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.jena.query.QuerySolution;

public class ConbinedEndpoint {
	public ArrayList<String[]> getDominio() throws FileNotFoundException{
		
			ArrayList<QuerySolution> resultados=new RDFIntegracionEndPoint().consulta(
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"SELECT DISTINCT ?class WHERE {\r\n" + 
					"{\r\n" + 
						" ?class owl:equivalentClass ?r\r\n" + 
					"}\r\n" + 
					"UNION\r\n" + 
					"{\r\n" + 
						" ?r owl:equivalentClass ?class\r\n" + 
					"}\r\n" + 					
				"}");
			ArrayList<String[]> lista=new ArrayList<String[]>();
			Iterator<QuerySolution> iterator=resultados.iterator();
			while (iterator.hasNext()) {
				String[] dominio= new String[2];
				String item = iterator.next().get("?class").toString();
				String[] bits = item.split("/");
				String last = bits[bits.length-1];
				dominio[0]=item.substring(0,item.length()-last.length());
				dominio[1]=last;
				lista.add(dominio);
			}
			return lista;

	}
	public ArrayList<QuerySolution> getUri() throws FileNotFoundException{
		
		ArrayList<QuerySolution> resultados=new RDFIntegracionEndPoint().consulta("PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
			"SELECT DISTINCT ?class WHERE {\r\n" + 
				"{\r\n" + 
					" ?class owl:equivalentClass ?r\r\n" + 
				"}\r\n" + 
				"UNION\r\n" + 
				"{\r\n" + 
					" ?r owl:equivalentClass ?class\r\n" + 
				"}\r\n" + 					
			"}");

		return resultados;

}
	ArrayList<QuerySolution> conbinedConsult(String clase){
		ArrayList<QuerySolution>total =new ArrayList<QuerySolution>();
		String[] bits=clase.split("/");
		String last = bits[bits.length-1];
		String prefix=clase.substring(0,clase.length()-last.length());
			if(clase.contains("http://www.videogames.com/")) {					
					total.addAll((new OWLVirtuosoEndPoint().consulta(
							"PREFIX : <"+prefix+">\r\n"+
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
							"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
							"SELECT DISTINCT ?instance WHERE {\r\n" + 
							" ?instance rdf:type :"+last+"\r\n" +
							"}")));
					try {
						total.addAll((new RDFEndPoint().consulta(
								"PREFIX : <"+prefix+">\r\n"+
								"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
								"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
								"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
								"SELECT DISTINCT ?instance WHERE {\r\n" + 
								" ?instance rdf:type :"+last+"\r\n" +
								"}")));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
			}
			else if(clase.contains("http://localhost:2020/resource/vocab/")) {
				total.addAll(new D2RQEndPoint().consulta(
						"PREFIX : <"+prefix+">\r\n"+
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"SELECT DISTINCT ?instance WHERE {\r\n" + 
						" ?instance rdf:type :"+last+"\r\n" +
						"}"));
			}
			else if(clase.contains("http://dbpedia.org/ontology/")) {
				if(clase.contains("VideoGame")) {			
					total.addAll(new DBPediaEndPoint().consulta(
					"PREFIX : <"+prefix+">\r\n"+
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
					"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
					"SELECT DISTINCT ?instance WHERE {\r\n" + 
					" ?instance rdf:type :"+last+"\r\n" +
					"}LIMIT 10"));
				}
				else {
					total.addAll(new DBPediaEndPoint().consulta(
							"PREFIX : <"+prefix+">\r\n"+
							"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
							"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
							"select distinct ?instance where {\r\n" + 
							"\r\n" + 
							"?a :"+last+"?instance "+
							"}LIMIT 10"));
					
				}
			}
			
				
			

		return total;
		
	}
	ArrayList<QuerySolution> allConsult(String consulta){
		ArrayList<QuerySolution>total =new ArrayList<QuerySolution>();
			if(consulta.contains("http://www.videogames.com/")) {					
					total.addAll((new OWLVirtuosoEndPoint().consulta(consulta)));
					try {
						total.addAll((new RDFEndPoint().consulta(consulta)));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
			}
			else if(consulta.contains("http://localhost:2020/resource/vocab/")) {
				total.addAll(new D2RQEndPoint().consulta(consulta));
			}
			else if(consulta.contains("http://dbpedia.org/ontology/")) {
					total.addAll(new DBPediaEndPoint().consulta(consulta));			
			

		
			}
		return total;

	}
	
	
	

}
