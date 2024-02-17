package edu.brown.cs.student.main.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.List;


/**
 * ACSDataSource helps BroadBandHandler deserialize all the JSON packages returned by the web API
 * as a List<List<String>> which makes it easier to run for loops and use .get method with.
 */
public class ACSDataSource {

  /**
   * The method that takes in a JSON package as a string and helps deserialize it using moshi and
   * adapters, turning it into a List<List<String>>.
   *
   * @param jsonPackage - the JSON package that needs to be deserialized
   */
  public static List<List<String>> deserializeACSPackage(String jsonPackage) throws APIException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<List<List<String>>> adapter =
          moshi.adapter(Types.newParameterizedType(List.class, List.class));
      return adapter.fromJson(jsonPackage);
    } catch (IOException e) {
      throw new APIException("Error Datasource");
    }
  }
}
