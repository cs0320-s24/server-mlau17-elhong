package main.server.csvhandlers;

import main.csvparser.src.main.java.edu.brown.cs.student.main.Search;
import main.server.csvhandlers.LoadCSVHandler;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * This class...
 *
 *  */
public class SearchCSVHandler implements Route {
    public LoadCSVHandler load;

    /**
     * Constructor accepts a filepath
     *
     * @param load
     */
    public SearchCSVHandler(LoadCSVHandler load) {
        this.load = load;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String searchWord = request.queryParams("searchWord");

        // Initialize an Arraylist for our informative response.
        List<List<String>> responseList = new ArrayList<>();

        // calling search class to get CSV data
        Search results = new Search(load.getCSVparser(), searchWord);
        results.searcher();

        // adding actual result data to return to web API
        responseList.addAll(results.getRowResults());
        return responseList;
    }
}
