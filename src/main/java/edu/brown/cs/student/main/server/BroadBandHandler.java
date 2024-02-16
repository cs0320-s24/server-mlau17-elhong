package edu.brown.cs.student.main.server;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadBandHandler implements Route {

  private List<List<String>> bBD = null;
  private final ACSCacheData cache;
  private boolean ifFound;

  public BroadBandHandler(ACSCacheData cache){
    this.cache = cache;
  }

  /**
   * This handle method needs to be filled by any class implementing Route. When the path set in
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   */
  @Override
  public Object handle(Request request, Response response)
          throws IOException, InterruptedException, URISyntaxException {

    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    Map<String, Object> responseMap = new HashMap<>();

    if (this.cache.getPastRequestCache().getIfPresent(countyName + ", " + stateName) ==null) {
      this.findBroadBand(stateName, countyName);
      try {
        responseMap.put("result", "success");
        responseMap.put("for", countyName + " in " + stateName);
        double band = Double.parseDouble(this.bBD.get(1).get(1));
        responseMap.put("Percentage", band);

        LocalDateTime todayDateTime = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String theDateTime = todayDateTime.format(dateFormatter);
        responseMap.put("It was retrieved on", theDateTime);

        this.cache.addPastRequestCache(countyName + ", " + stateName, responseMap.toString());

        return responseMap;

      } catch (Exception e) {
        responseMap.put("result", "error: problem with inputs");
      }
    } else {
      // getting data from past request stored in cache
      return this.cache.getPastRequestCache().getIfPresent(countyName + ", " + stateName);
    }
    return responseMap;
  }


  private String sendRequest(String url)
      throws IOException, InterruptedException, URISyntaxException {
    HttpRequest buildAPIRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
    HttpResponse<String> sentAPIResponse =
        HttpClient.newBuilder().build().send(buildAPIRequest, HttpResponse.BodyHandlers.ofString());

    return sentAPIResponse.body();
  }

  private void findBroadBand(String stateName, String countyName) throws IOException, InterruptedException, URISyntaxException {

    String idOfState = this.cache.getStateCache().getIfPresent(stateName);

    if (idOfState != null) {
      String url =
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + idOfState;

      try {
        //Checks if the county cache already contains that county and if so just pull from cache
        if (this.cache.getCountyCache().getIfPresent(countyName) == null) {
          String countyData = sendRequest(url);
          List<List<String>> dataPackage = ACSDataSource.deserializeACSPackage(countyData);
          if (dataPackage != null) {
            for (List<String> state : dataPackage) {
              String name = state.get(0);
              if (name.equals(countyName + ", " + stateName))  {
                String countyID = state.get(2);
                this.ifFound = true;
                this.cache.addCountyCache(countyName, countyID);
              } else if (name.equals(countyName + " County, " + stateName)){
                String countyID = state.get(2);
                this.ifFound = true;
                this.cache.addCountyCache(countyName, countyID);
              }
            }
          }
        }

        if(this.ifFound) {
          // retrieving county ID from existing cache
          String idOfCounty = this.cache.getCountyCache().getIfPresent(countyName);
          String finalURL =
                  "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                          + idOfCounty
                          + "&in=state:"
                          + idOfState;

          String broadBandData = sendRequest(finalURL);
          this.bBD = ACSDataSource.deserializeACSPackage(broadBandData);

        } else{
          System.err.println("Error: County Not Found. Please check it is inputted correctly.");
        }
      } catch (URISyntaxException e) {
        System.err.println("Error: API URI is wrong");
      }
    } else{
      System.err.println("Error: State Not Found. Please check it is inputted correctly.");
    }
  }

}
