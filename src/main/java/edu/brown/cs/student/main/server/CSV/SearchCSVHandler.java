package edu.brown.cs.student.main.server.CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVParser.FactoryFailureException;
import edu.brown.cs.student.main.CSVParser.Search;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class handles searching for a given searchword by creating an instance of the Search class
 * and passing in the CSVParser created in LoadCSVHandler. The rows containing the searchword are
 * then returned to the API user.
 */
public class SearchCSVHandler implements Route {

  public LoadCSVHandler load;
  public GlobalData data;

  /**
   * Constructor accepts a global varibale GlobalData and the LoadCSVHandler that was previously
   * called.
   *
   * @param data global variable holding the loaded csv data
   * @param load instance of LoadCSVHandler used to load the csv file, pass in to access the parser
   */
  public SearchCSVHandler(GlobalData data, LoadCSVHandler load) {
    this.data = data;
    this.load = load;
  }

  /**
   * This method handles the searchWord query, creating a new Search with the given parameters.
   *
   * @return the response map as a JSON file
   *  */
  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    // map for the response result
    Map<String, Object> responseMap = new HashMap<>();

    try {
      String searchWord = request.queryParams("searchword");
      try {
        // if there is no searchword input
        if (searchWord.equals("null")) {}
        // if there is no load beforehand
        if (this.load.equals("null")) {}
        // if there are neither search nor load
        if (searchWord.equals("null") && this.load.equals("null")) {}
      } catch (Exception e) {
        responseMap.put("status", "error");
        responseMap.put("message", "Please load a file first and enter a valid searchword.");
        return adapter.toJson(responseMap);
      }

      // calling search class to get CSV data
      Search search = new Search(load.getParser(), searchWord);
      search.searcher();
      List<List<String>> result = search.getRowResults();

      // if search yielded results
      if (!result.isEmpty()) {
        // adding actual result data to return to web API
        responseMap.put("status", "success");
        responseMap.put("searchResults", result);
        return adapter.toJson(responseMap);
      }

    } catch (IOException e) {
      // if not, error message
      responseMap.put("status", "error");
      responseMap.put(
          "message",
          "Search word does not exist in this file or file has not been loaded, please try again.");
      return adapter.toJson(responseMap);
    } catch (FactoryFailureException e) {
      responseMap.put("status", "error");
      responseMap.put("message", "Please enter a searchword.");
      return adapter.toJson(responseMap);
    }

    responseMap.put("status", "error");
    responseMap.put("message", "Please load a file first and enter a valid searchword.");
    return adapter.toJson(responseMap);
  }
}
