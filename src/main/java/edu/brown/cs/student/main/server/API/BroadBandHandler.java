package edu.brown.cs.student.main.server.API;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The BroadBandHandler handles the users request of a state name and a county name then call to the
 * web API. A successful API call will result in a response which includes the result, time, broad
 * band percentage, the county name, and the state name. If it was unsuccessful it will display a
 * helpful message notifying the user where the problem might be.
 */
public class BroadBandHandler implements Route {

  private List<List<String>> bBD = null;
  private final ACSCacheData cache;

  /**
   * This is the constructor of BroadBandHandler, which helps us access the global variable cache
   * and get the data we need instead of calling the web API repeatedly.
   *
   * @param cache - The global variable cache that is created in the Server.java
   */
  public BroadBandHandler(ACSCacheData cache) {
    this.cache = cache;
  }

  /**
   * This handle method takes in the user's request of a state and county name then checks the
   * pastRequestCache to make sure it has not been requested before calling the web API again. If it
   * has not been requested before it will call the web API and find the broadband percentage then
   * display it with the time it was requested, the county name, and the state name. If there is an
   * error in the process then it will display a helpful error message, helping the user or backend
   * developer find the problem.
   *
   * @param request The request that the user sent to our server
   * @param response The response that our server sends back to the user
   * @return - returns a hashmap that contains the results of the request
   */
  @Override
  public Object handle(Request request, Response response) {

    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);

    // retrieving query parameters
    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    Map<String, Object> responseMap = new HashMap<>();

    if (stateName == null || countyName == null) {
      // Bad request! Send an error response.
      responseMap.put("result", "failure");
      responseMap.put("Problem", "Missing Parameter");
      return adapter.toJson(responseMap);
    }

    // checking if the query has already been called before
    if (this.cache.getPastRequestCache().getIfPresent(countyName + ", " + stateName) == null) {
      // if not, retrieve and if unsuccessful print the APIException message
      try {
        this.findBroadBand(stateName, countyName);
        responseMap.put("result", "success");
        responseMap.put("for", countyName + " in " + stateName);
        double band = Double.parseDouble(this.bBD.get(1).get(1));
        responseMap.put("Percentage", band);

        LocalDateTime todayDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String theDateTime = todayDateTime.format(dateFormatter);
        responseMap.put("It was retrieved on", theDateTime);

        this.cache.addPastRequestCache(countyName + ", " + stateName, responseMap.toString());
        return adapter.toJson(responseMap);

      } catch (APIException e) {
        responseMap.put("result", "failure");
        responseMap.put("Problem", e.getMessage());
      }
    } else {
      // if it already exists in cache, just pull from cache
      return this.cache.getPastRequestCache().getIfPresent(countyName + ", " + stateName);
    }
    return adapter.toJson(responseMap);
  }

  /**
   * A helper method that takes in a url then requests data from it then takes the response and
   * return it as a string.
   *
   * @param url - the url that it should send the request to and get data from
   * @return - returns the body/JSON given to us after making a request to the web API
   * @throws APIException - an exception that allows the developer to customize the message
   */
  private String sendRequest(String url) throws APIException {
    try {
      HttpRequest buildAPIRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

      HttpResponse<String> sentAPIResponse =
          HttpClient.newBuilder()
              .build()
              .send(buildAPIRequest, HttpResponse.BodyHandlers.ofString());

      return sentAPIResponse.body();

    } catch (URISyntaxException u) {
      throw new APIException("URI syntax is incorrect, please fix in code.");
    } catch (InterruptedException | IOException e) {
      throw new APIException("Problem with URI connection, please fix in code.");
    }
  }

  /**
   * A helper method that helps find the broadband percentage by checking the caches first then
   * calling a series of web APIs if needed. This is also where the county cache is starting to be
   * populated.
   *
   * @param stateName - the name of the state that the user is looking for
   * @param countyName - the name of the county that the user is looking for
   * @throws APIException - an exception that allows the developer to customize the message
   */
  private void findBroadBand(String stateName, String countyName) throws APIException {
    // find id of the state that the user is looking for
    String idOfState = this.cache.getStateCache().getIfPresent(stateName);

    if (idOfState != null) {
      String url =
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + idOfState;

      // Checks if the county cache already contains that county and county ID and if so just pull
      // from cache
      if (this.cache.getCountyCache().getIfPresent(countyName) == null) {
        // if it does not already exist calls the API and looks for the corresponding county ID
        String countyData = sendRequest(url);
        List<List<String>> dataPackage = ACSDataSource.deserializeACSPackage(countyData);

        if (dataPackage != null) {
          for (List<String> state : dataPackage) {
            String name = state.get(0);
            if (name.equals(countyName + ", " + stateName)) {
              String countyID = state.get(2);
              this.cache.addCountyCache(countyName, countyID);
            } else if (name.equals(countyName + " County, " + stateName)) {
              String countyID = state.get(2);
              this.cache.addCountyCache(countyName, countyID);
            }
          }
        }
      }
      // retrieving county ID from existing cache
      String idOfCounty = this.cache.getCountyCache().getIfPresent(countyName);

      // if county ID is found then proceed to search in the broad band API
      if (idOfCounty != null) {
        String finalURL =
            "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                + idOfCounty
                + "&in=state:"
                + idOfState;
        String broadBandData = sendRequest(finalURL);
        this.bBD = ACSDataSource.deserializeACSPackage(broadBandData);

      } else {
        throw new APIException(
            "Error bad request: County was not Found, "
                + "please check if inputted, spelling, and capitalization.");
      }
    } else {
      throw new APIException(
          "Error bad request: State was not Found, "
              + "please check if inputted, spelling, and capitalization.");
    }
  }
}
