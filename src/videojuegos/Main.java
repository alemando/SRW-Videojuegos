package videojuegos;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {
		
		try {
			new RDFEndPoint().consulta("PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"+
					"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
					"SELECT DISTINCT ?p ?n ?r WHERE {\r\n" + 
					"  ?p ?n ?r\r\n" +
					"}");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*new OWLVirtuosoEndPoint().consulta("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\r\n" + 
				"SELECT ?subject ?object\r\n" + 
				"	WHERE { ?subject rdfs:subClassOf ?object }");
		
		new D2RQEndPoint().consulta("SELECT DISTINCT * WHERE {\r\n" + 
				"  ?s ?p ?o\r\n" + 
				"}");
		new DBPediaEndPoint().consulta("select distinct ?Concept where {[] a ?Concept} LIMIT 100");
*/
	}

}
