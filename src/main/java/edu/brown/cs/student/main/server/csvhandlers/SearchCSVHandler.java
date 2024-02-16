package edu.brown.cs.student.main.server.csvhandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVParser.Search;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class... */
public class SearchCSVHandler implements Route {

  public LoadCSVHandler load;

  /**
   * Constructor accepts a filepath
   *
   * @param data
   */
  public SearchCSVHandler(GlobalData data, LoadCSVHandler load) {
    this.load = load;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    // map for the response result
    Map<String, Object> responseMap = new HashMap<>();

    String searchWord = request.queryParams("searchWord");
    // calling search class to get CSV data
    Search search = new Search(load.getParser(), searchWord);
    search.searcher();
    List<List<String>> result = search.getRowResults();

    // adding actual result data to return to web API
    responseMap.put("status", "success");
    responseMap.put("searchResults", result);
    return responseMap;
  }
}
