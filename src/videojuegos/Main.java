package videojuegos;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;

public class Main {
	
	static JFrame frame;

	public static void main(String[] args) {
		
		frame = new FramePrincipal();
		frame.setContentPane(new InterfazPrincipal());
		
	}

}
