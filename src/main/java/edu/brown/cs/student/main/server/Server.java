package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.csvhandlers.GlobalData;
import edu.brown.cs.student.main.server.csvhandlers.LoadCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.SearchCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.ViewCSVHandler;
import spark.Spark;

/** Server is the top-level class of this program... */
public class Server {

  static final int port = 3232;

  public static void main(String[] args) {
    int port = 3232;
    Spark.port(port);

    after(
        (request, response) -> {
          // TODO: set the * header to a list of trusted origins
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Setting up the handlers for the GET /broadband, loadcsv, searchcsv, and viewcsv endpoints
    Spark.get("broadband", new BroadBandHandler());
    // making a new Global variable
    GlobalData serverData = new GlobalData();
    LoadCSVHandler load = new LoadCSVHandler(serverData);
    Spark.get("loadcsv", load);
    Spark.get("viewcsv", new ViewCSVHandler(serverData));
    Spark.get("searchcsv", new SearchCSVHandler(serverData, load));

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
