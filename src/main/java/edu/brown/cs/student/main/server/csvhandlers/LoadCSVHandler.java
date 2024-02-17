package edu.brown.cs.student.main.server.csvhandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.FactoryFailureException;
import edu.brown.cs.student.main.CSVParser.StringCreator;

import java.io.FileReader;
import java.io.IOException;
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
  public Map<String, Object> responseMap;

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
    this.responseMap = new HashMap<>();

    // taking the query parameters
    String filepath = request.queryParams("filepath");
    String header = request.queryParams("header");
    System.out.println("filepath" + filepath);
    System.out.println("header" + header);

    // if there is no filepath input
    try {
      if (filepath.equals("null")) {
      }
      // if there is no header input
      if (header.equals("null")) {
      }
      // if there are neither header not filepath input
      if (filepath.equals("null") && header.equals("null")) {
      }
    } catch (Exception e) {
      responseMap.put("status", "error_datasource");
      responseMap.put("message", "Missing parameter(s). Make sure to type in the full file path and the header using Yes or No.");
      return adapter.toJson(responseMap);
    }

    // if the header input is invalid
    if (!header.equals("Yes") && !header.equals("No")) {
      this.responseMap.put("status", "error_datasource");
      this.responseMap.put("message", "Please try again and input the header using Yes or No");
      return adapter.toJson(responseMap);
    }

    this.header = this.checkHeader(header);

    try {
      // only allowing filepaths to the data folder in this program
      if (filepath.contains("/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/java/edu/brown/cs/student/main/data")) {
        this.filepath = filepath;
        FileReader reader = new FileReader(filepath);
        this.parser = new CSVParser<>(new StringCreator(), this.header, reader);
        this.data.setCsvData(this.parser.sortData());

        // putting the results into the responseMap
        this.responseMap.put("status", "success");
        this.responseMap.put("filepath", filepath);


      } else {
        // when filepath is invalid, error message
        this.responseMap.put("status", "error_datasource");
        this.responseMap.put("message", "No filepath found. Please enter the full or correct file path.");
      }
    } catch (IOException | FactoryFailureException e) {
      responseMap.put("status", "error_datasource");
      // converting to JSON
      return adapter.toJson(this.responseMap);
    }
    return adapter.toJson(this.responseMap);
  }

  /** This method sets the header boolean according to user input */
  private Boolean checkHeader(String headerInput) {
    // setting the header to a Boolean
    if (headerInput.equals("Yes")) {
      return true;
    }
    if (headerInput.equals("No")) {
      return false;
    }
    return false;
  }

  /** This method is called in SearchCSVHandler and gets the CSVParser used in the load process. */
  public CSVParser getParser() {
    return this.parser;
  }
}
