package fr.insee.duvel.trolls;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Pattern;

/**
 *
 */
public class DataSetModelPush {

    private static Logger logger = LogManager.getLogger(DataSetModelPush.class);

    public static void main(String[] args) throws Exception {

        String file = System.getenv("INPUT_FILE");
        String sparqlEndpoint = System.getenv("ARGS").split(Pattern.quote("|"))[0];
        String repo = System.getenv("ARGS").split(Pattern.quote("|"))[1];

        Repository db = new HTTPRepository(sparqlEndpoint, repo);

        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            try (InputStream input = new FileInputStream(file)) {
                // add the RDF data from the inputstream directly to our database
                conn.add(input, "", RDFFormat.TURTLE);
            }
        } finally {
            // Before our program exits, make sure the database is properly shut down.
            db.shutDown();
        }
    }
}
