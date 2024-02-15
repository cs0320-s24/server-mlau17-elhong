package main.server;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class...
 *
 *  */
public class LoadCSVHandler implements Route {

    /**
     * Constructor accepts a filepath
     *
     * @param filepath file path of CSV
     */
    public LoadCSVHandler(String filepath){

    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
