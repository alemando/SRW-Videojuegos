package videojuegos;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

public class RDFIntegracionEndPoint {
	
	public void consulta(String queryString) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		InputStream archivo = FileManager.get().open("src/resources/rdfIntegracion.owl");
		model.read(archivo,null, "RDF/XML");
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		
		ResultSet results = qexec.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			System.out.println(soln);
		}
		
		qexec.close();
	}

}
