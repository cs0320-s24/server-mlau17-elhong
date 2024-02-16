package edu.brown.cs.student.main.server;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.awt.desktop.SystemSleepEvent;
import java.io.IOException;
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
import java.util.concurrent.TimeUnit;

import org.testng.collections.Objects;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.naming.CannotProceedException;
// import com.google.common.cache.CacheLoader;
// import com.google.common.cache.LoadingCache;

public class BroadBandHandler implements Route {

  private List<List<String>> bBD = null;
  private Cache<String, String> stateCache;
  private Cache<String, String> countyCache;
  private Cache<String, Map> pastRequestCache;

  /**
   * This handle method needs to be filled by any class implementing Route. When the path set in
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   */
  @Override
  public Object handle(Request request, Response response)
          throws IOException, InterruptedException, URISyntaxException {

    //Creates two caches one for state-state id and one for county-county id
    this.stateCache = this.customizableCache(50, 20);
    this.countyCache = this.customizableCache(100, 20);

    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    Map<String, Object> responseMap = new HashMap<>();
    this.findBroadBand(stateName, countyName);
    try {
      responseMap.put("result", "success");
      responseMap.put("for", countyName + " in " + stateName);
      double band = Double.parseDouble(this.bBD.get(1).get(1));
      System.out.println(band);
      responseMap.put("Percentage", band);

      LocalDateTime todayDateTime = LocalDateTime.now();
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      String theDateTime = todayDateTime.format(dateFormatter);
      responseMap.put("It was retrieved on", theDateTime);

      return responseMap;

    } catch (Exception e) {
      responseMap.put("result", "error: problem with inputs");
    }
    return responseMap;
  }


  public Cache<String, String> customizableCache(int maximumSize, int minuteDelete) {
    Cache<String, String> makeCache =
        CacheBuilder.newBuilder()
            .maximumSize(maximumSize)
            .expireAfterWrite(minuteDelete, TimeUnit.MINUTES)
            .build();
    return makeCache;
  }

  private String sendRequest(String url)
      throws IOException, InterruptedException, URISyntaxException {
    HttpRequest buildAPIRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();

    HttpResponse<String> sentAPIResponse =
        HttpClient.newBuilder().build().send(buildAPIRequest, HttpResponse.BodyHandlers.ofString());

    return sentAPIResponse.body();
  }

  private void findBroadBand(String stateName, String countyName) throws IOException, InterruptedException, URISyntaxException {

    //Populates the Cache with all the states and its corresponding state IDs
    this.populateStateCache(stateName);

    String idOfState = this.stateCache.getIfPresent(stateName);

    if (idOfState != null) {
      String url =
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + idOfState;

      try {
        //Checks if the county cache already contains that county and if so just pull from cache
        if (countyCache.getIfPresent(countyName) == null) {
          String countyData = sendRequest(url);
          List<List<String>> dataPackage = ACSDataSource.deserializeACSPackage(countyData);
          if (dataPackage != null) {
            for (List<String> state : dataPackage) {
              String name = state.get(0);
              if (name.equals(countyName + ", " + stateName))  {
                String countyID = state.get(2);

                countyCache.put(countyName, countyID);
              } else if (name.equals(countyName + " County, " + stateName)){
                String countyID = state.get(2);

                countyCache.put(countyName, countyID);
              }
            }
          }
        }

        String idOfCounty = countyCache.getIfPresent(countyName);

        String finalURL =
            "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                + idOfCounty
                + "&in=state:"
                + idOfState;

        String broadBandData = sendRequest(finalURL);
        System.out.println("found broad band data");
        this.bBD = ACSDataSource.deserializeACSPackage(broadBandData);
        System.out.println(this.bBD);

      } catch (URISyntaxException e) {
        System.err.println("Error: URI is wrong, get it together");
      }
    }
  }

  private void populateStateCache(String nameOfState) throws IOException, URISyntaxException, InterruptedException {
    String urlForStateId = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";

    String stateData = sendRequest(urlForStateId);
    System.out.println(stateData);
    List<List<String>> packageOfStates = ACSDataSource.deserializeACSPackage(stateData);
    System.out.println(packageOfStates);

    if (packageOfStates != null) {
      for (List<String> state : packageOfStates) {
        String name = state.get(0);
        String stateID = state.get(1);
        this.stateCache.put(name, stateID);
      }
    }
  }
}
