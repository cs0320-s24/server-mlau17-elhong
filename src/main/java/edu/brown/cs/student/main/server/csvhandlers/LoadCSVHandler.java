package edu.brown.cs.student.main.server.csvhandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.StringCreator;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class handles the URL input for load csv, requesting the query parameters for filepath and
 * header and creating a new CSVParser with this information. It also uses Moshi to deserialize the
 * data, which is stored as a GlobalData global variable for the other handlers to access.
 */
public class LoadCSVHandler implements Route {

  public String filepath;
  public Boolean header;
  public GlobalData data;
  public CSVParser parser;

  /**
   * Constructor accepts a GlobalData which stores the results of load.
   *
   * @param data of the parsed CSV file
   */
  public LoadCSVHandler(GlobalData data) {
    this.data = data;
  }

  /** This method handles the */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    // map for the response result
    Map<String, Object> responseMap = new HashMap<>();

    // taking the query parameters
    String filepath = request.queryParams("filepath");
    String header = request.queryParams("header");
    this.header = this.checkHeader(header);

    System.out.println(filepath);

    if (filepath != null) {
      this.filepath = filepath;
      FileReader reader = new FileReader(filepath);
      this.parser = new CSVParser<List<String>>(new StringCreator(), this.header, reader);
      this.data.setCsvData(parser.sortData());

      // putting the results into the responseMap
      responseMap.put("status", "success");
      responseMap.put("filepath", filepath);

      // converting to JSON
    } else if (filepath.equals(null)) {
      System.out.println("No filepath");
      responseMap.put("status", "error_datasource");
      responseMap.put("message", "No filepath found. Please enter the full file path.");

    } else {
      System.out.println("No filepath");
      responseMap.put("status", "error_datasource");
      responseMap.put("message", "Invalid filepath. Please enter the full file path.");
    }
    return adapter.toJson(responseMap);
  }

  /** This method sets the header boolean according to user input */
  private Boolean checkHeader(String headerInput) {
    // setting the header to a Boolean
    if (headerInput.equals("Yes")) {
      return true;
    }
    if (headerInput.equals("No")) {
      return false;
    } else {
      System.out.println("Please try again and answer using Yes or No");
      System.exit(0);
    }
    return false;
  }

  /** This method is called in SearchCSVHandler and gets the CSVParser used in the load process. */
  public CSVParser getParser() {
    return this.parser;
  }
}
