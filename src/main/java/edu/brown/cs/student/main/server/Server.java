package edu.brown.cs.student.main.server;

import edu.brown.cs.student.main.server.API.ACSCacheData;
import edu.brown.cs.student.main.server.API.BroadBandHandler;
import edu.brown.cs.student.main.server.CSV.GlobalData;
import edu.brown.cs.student.main.server.CSV.LoadCSVHandler;
import edu.brown.cs.student.main.server.CSV.SearchCSVHandler;
import edu.brown.cs.student.main.server.CSV.ViewCSVHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import spark.Spark;

/**
 * Server is the top-level class of this program. It contains the main() method which starts Spark
 * and runs the various handlers (4).
 *
 * Note that the CSV handlers take in a serverData representing the loaded csv data from the user
 * input filepath, using dependency injection to ensure that the endpoints share the same
 * information. Moreover, the SearchCSVHandler also takes in the instance of load in order to access
 * the CSVParser within its own class.
 */
public class Server {

  public static void main(String[] args)
      throws IOException, URISyntaxException, InterruptedException {
    int port = 3232;
    Spark.port(port);

    // making a new Global variable
    GlobalData serverData = new GlobalData();
    ACSCacheData cache = new ACSCacheData();
    LoadCSVHandler load = new LoadCSVHandler(serverData);
    // Setting up the handlers for the GET /broadband, loadcsv, searchcsv, and viewcsv endpoints
    Spark.get("loadcsv", load);
    Spark.get("viewcsv", new ViewCSVHandler(serverData));
    Spark.get("searchcsv", new SearchCSVHandler(serverData, load));
    Spark.get("broadband", new BroadBandHandler(cache));

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
    System.out.println("Click link and enter your parameter values in the URL to begin!");
  }
}
