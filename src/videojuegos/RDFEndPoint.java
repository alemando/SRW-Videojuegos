package videojuegos;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

public class RDFEndPoint {
	
	public void consulta(String query) throws FileNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		InputStream archivo = FileManager.get().open("Robots.owl");
		model.read(archivo,null, "RDF/XML");
		
		StmtIterator iter = model.listStatements();
		
		while(iter.hasNext()) {
			Statement stm = iter.next();
			
			Resource s = stm.getSubject();
			Resource p = stm.getPredicate();
			RDFNode o = stm.getObject();
			
			System.out.println(s +"  -  "+ p + "  -  " + o);
		}
		
		iter.close();
	}
	
}
