package videojuegos;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

public class RDFIntegracionEndPoint {
	
	public ArrayList<QuerySolution> consulta(String queryString) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		InputStream archivo = FileManager.get().open("src/resources/rdfIntegracion.owl");
		model.read(archivo,null, "RDF/XML");
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		
		ResultSet results = qexec.execSelect();	
		ArrayList<QuerySolution> resultados = new ArrayList<QuerySolution>();
		
		while(results.hasNext()) {
			resultados.add(results.nextSolution());
		}
		qexec.close();
		return resultados;
		
	}

}
