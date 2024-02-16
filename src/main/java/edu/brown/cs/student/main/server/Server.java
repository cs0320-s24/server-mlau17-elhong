package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.csvhandlers.GlobalData;
import edu.brown.cs.student.main.server.csvhandlers.LoadCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.SearchCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.ViewCSVHandler;
import spark.Spark;

import java.io.IOException;
import java.net.URISyntaxException;

/** Server is the top-level class of this program. It contains the main() method which starts Spark and runs the various
 * handlers (4).
 *
 * Note that the CSV handlers take in a serverData representing the loaded csv data from the user inpit filepath, using
 * depedency injenction to ensure that the endpoints share the same information. Moreover, the SearcCSVHandler also takes
 * in the instance of load in order to access the CSVParser within its own class.
 */

public class Server {

  static final int port = 3232;

  public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
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
  }
}
