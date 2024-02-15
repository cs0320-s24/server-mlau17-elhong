package main.server;

import spark.Spark;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static spark.Spark.after;

/**
 * Top-level class...
 * */

public class Server {

    public static void main(String[] args) {
        int port = 3232;
        Spark.port(port);

        after(
                (request, response) -> {
                    // TODO: set the * header to a list of trusted origins
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Access-Control-Allow-Methods", "*");
                });

        // Setting up the handler for the GET /broadband endpoint
        Spark.get("broadband", new BroadBandHandler());
        // one out of four endpoints^^
        Spark.init();
        Spark.awaitInitialization();

        // TODO Notice this link alone leads to a 404... Why is that?
        System.out.println("Server started at http://localhost:" + port);

    }

    private void run(){
        Scanner userInput = new Scanner(System.in);
        System.out.println("Welcome, this server can make API requests to load, view, or search the contents of a CSV " +
                "file by calling the `loadcsv`, `viewcsv` or `searchcsv` endpoints. " +
                "For `loadcsv`, please provide the path of the CSV file to load: " );
        String fileName = userInput.nextLine();

        // Reader created to scan csv data
        FileReader fr = null;
        String filepath = "data/" + fileName;
        try {
            fr = new FileReader(filepath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found, please try again.");
            System.exit(0);
            throw new RuntimeException(e);
        }
//      // Setting up the handler for the GET /loadcsv, /searchcsv, and /viewcsv endpoints
        Spark.get("loadcsv", new LoadCSVHandler(filepath));
        Spark.get("searchcsv", new SearchCSVHandler());
        Spark.get("viewcsv", new ViewCSVHandler());

        // TODO needed?? what does this do
        Spark.init();
        Spark.awaitInitialization();

    }
}
