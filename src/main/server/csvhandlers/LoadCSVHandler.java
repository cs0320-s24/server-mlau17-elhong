package main.server.csvhandlers;

import main.csvparser.src.main.java.edu.brown.cs.student.main.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class...
 *
 *  */
public class LoadCSVHandler implements Route {

    public String filepath;
    public Boolean header;
    public CSVParser data;

    /**
     * Constructor accepts a filepath
     *
     * @param filepath file path of CSV
     */
    public LoadCSVHandler(String filepath) {
    this.filepath = filepath;
        }

        /**
         * If from link
         * */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        String filepath = request.queryParams("filepath");
        this.filepath = filepath;
        String header = request.queryParams("header");
        this.header = this.checkHeader(header);
        this.loadCSV();
        // TODO this is sus
        return null;
    }

    /**
     * Method that sets the header boolean according to user input
     *
     * */
    private Boolean checkHeader(String headerInput){
        // setting the header to a Boolean
        if (headerInput.equals("Yes")) {
            return true;}
        if (headerInput.equals("No")){
            return false;}
        else{
            System.out.println("Please try again and answer using Yes or No");
            System.exit(0);}
        return false;
    }

    /**
     * If directly from terminal
     * */
    private void loadCSV() throws IOException, FactoryFailureException{
        // Instantiate a Reader for the file
        Reader fileReader = new FileReader(this.filepath);
        // Instantiate a CSVParser object with appropriate parameters
        CSVParser parser = new CSVParser<>(new StringCreator(), true, fileReader);
        // Call the method to parse the CSV file
        parser.sortData();
        this.data = parser;
    }

    public CSVParser getCSVparser(){
        return this.data;
    }
}
