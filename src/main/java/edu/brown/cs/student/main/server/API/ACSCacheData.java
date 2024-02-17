package edu.brown.cs.student.main.server.API;

import static java.lang.System.exit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ACSCacheData is similar to a global variable where we can store data and call it in the classes
 * we need to use the Cache data in. In this case, we mostly used it in BroadBandHandler to prevent
 * repetitive calls to the Web API. There are three specific caches: state name-state id, county
 * name-county id, and past request-past response.
 */
public class ACSCacheData {
  Cache<String, String> stateCache;
  Cache<String, String> countyCache;
  Cache<String, String> pastRequestCache;

  /**
   * The constructor for ACSCacheData where we created three custom caches with varying sizes and
   * minuteDelete. It is also where we populated the state cache as there are only around 50 key and
   * values, and it will also make it easier to find any state ids.
   */
  public ACSCacheData() {
    // Creates three caches for state-state id, for county-county id, and for past requests
    this.stateCache = this.customizableCache(100, 20);
    // Populates the Cache with all the states and its corresponding state IDs
    this.populateStateCache();
    this.countyCache = this.customizableCache(100, 20);
    this.pastRequestCache = this.customizableCache(100, 20);
  }

  /**
   * A helper method that helps create a google guava cache and makes it customizable by size and
   * when the data should be cleared in the cache.
   *
   * @param maximumSize - the size the developer wants the cache to be
   * @param minuteDelete - the amount of time that passes before the cache starts clearing info
   * @return - returns a cache which was customized by the user
   */
  public Cache<String, String> customizableCache(int maximumSize, int minuteDelete) {
    return CacheBuilder.newBuilder()
        .maximumSize(maximumSize)
        .expireAfterWrite(minuteDelete, TimeUnit.MINUTES)
        .build();
  }

  /**
   * A helper method to populate the state cache with all the states and its corresponding state
   * ids, which makes it easier to find any state id the user wants later.
   */
  private void populateStateCache() {
    String urlForStateId = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
    try {
      String stateData = sendRequest(urlForStateId);
      List<List<String>> packageOfStates = ACSDataSource.deserializeACSPackage(stateData);

      if (packageOfStates != null) {
        for (List<String> state : packageOfStates) {
          String name = state.get(0);
          String stateID = state.get(1);
          this.stateCache.put(name, stateID);
        }
      }
    } catch (APIException e) {
      exit(0);
    }
  }

  /**
   * A helper method that takes in a url and sends a request to the web API then returns its JSON
   * package
   *
   * @param url - the url that the user wants to send the request to
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
      throw new APIException("URI is incorrect");
    } catch (InterruptedException | IOException e) {
      throw new APIException("Problem with link connection");
    }
  }

  /**
   * A getter method returns the state cache
   *
   * @return - returns state cache
   */
  public Cache<String, String> getStateCache() {
    return this.stateCache;
  }

  /**
   * A getter method returns the county cache
   *
   * @return - returns county cache
   */
  public Cache<String, String> getCountyCache() {
    return this.countyCache;
  }

  /**
   * A getter method returns the pastRequest cache
   *
   * @return - returns past requests cache
   */
  public Cache<String, String> getPastRequestCache() {
    return this.pastRequestCache;
  }

  /** An add method that helps add data to the county cache
   *
   * @param countyName - the name of the county we want to add to cache
   * @param countyID - the id of the county we want to add to cache
   */
  public void addCountyCache(String countyName, String countyID) {
    this.countyCache.put(countyName, countyID);
  }

  /** An add method that helps add data to the pastRequest cache
   *
   * @param name - the name of the state and county the user looked for to add to cache
   * @param response - the response that we found to add to cache
   */
  public void addPastRequestCache(String name, String response) {
    this.pastRequestCache.put(name, response);
  }
}
