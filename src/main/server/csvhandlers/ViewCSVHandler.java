package main.server.csvhandlers;

import main.server.csvhandlers.LoadCSVHandler;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class...
 *
 *  */

public class ViewCSVHandler implements Route {

    public LoadCSVHandler load;

    /**
     * Constructor accepts a filepath
     *
     * @param load
     */
    public ViewCSVHandler(LoadCSVHandler load) {
        this.load = load;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO: get to print out in webAPI
        return load.getCSVparser();
    }

}
