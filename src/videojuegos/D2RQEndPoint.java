package videojuegos;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

public class D2RQEndPoint {
	
	public ArrayList<QuerySolution> consulta(String queryString){
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:2020/sparql", query);
		
		ResultSet results = qexec.execSelect();
		ArrayList<QuerySolution> resultados = new ArrayList<QuerySolution>();
		
		while(results.hasNext()) {
			resultados.add(results.nextSolution());
		}
		qexec.close();
		return resultados;
	}
}
