package main.server;

import main.csvparser.src.main.java.edu.brown.cs.student.main.FactoryFailureException;
import main.server.csvhandlers.GlobalData;
import main.server.csvhandlers.LoadCSVHandler;
import main.server.csvhandlers.SearchCSVHandler;
import main.server.csvhandlers.ViewCSVHandler;
import spark.Spark;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static spark.Spark.after;

/**
 * Server is the top-level class of this program...
 * */

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
        Spark.get("loadcsv", new LoadCSVHandler(serverData));
        Spark.get("viewcsv", new ViewCSVHandler(serverData));
        Spark.get("searchcsv", new SearchCSVHandler(serverData));

        Spark.init();
        Spark.awaitInitialization();

        System.out.println("Server started at http://localhost:" + port);

    }

}
