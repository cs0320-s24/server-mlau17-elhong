package edu.brown.cs.student.main.server.CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class creates a response containing the data from the CSV file that was first parsed in
 * LoadCSVHandler, to show the viewers what the data looks like.
 */
public class ViewCSVHandler implements Route {

  public GlobalData data;

  /**
   * Constructor accepts a GlobalData global variable
   *
   * @param data from CSV obtained in LoadCSVHandler
   */
  public ViewCSVHandler(GlobalData data) {
    this.data = data;
  }

  /**
   * This method creates a response map by accessing the CSV data from the global variable "data"
   * using a getter method and returns this data to the user.
   *
   * @return the response map as a JSON file
   */
  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    // map for the response result
    Map<String, Object> responseMap = new HashMap<>();

    // checking to see if the data was populated
    if (!this.data.isEmpty()) {
      responseMap.put("status", "success");
      responseMap.put("CSVdata", this.data.getCsvData());
    }
    if (this.data.isEmpty()) {
      responseMap.put("status", "error");
      responseMap.put("message", "Please load a csv file first before viewing.");
    }
    // converting to JSON
    return adapter.toJson(responseMap);
  }
}
