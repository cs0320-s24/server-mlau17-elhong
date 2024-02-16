package edu.brown.cs.student.main.server;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ACSCacheData {
    Cache<String, String> stateCache;
    Cache<String, String> countyCache;
    Cache<String, String> pastRequestCache;

    public ACSCacheData() throws IOException, URISyntaxException, InterruptedException {
        // Creates three caches for state-state id, for county-county id, and for past requests
        this.stateCache = this.customizableCache(50, 20);
        //Populates the Cache with all the states and its corresponding state IDs
        this.populateStateCache();
        this.countyCache = this.customizableCache(100, 20);
        this.pastRequestCache = this.customizableCache(100, 20);
    }

    public Cache<String, String> customizableCache(int maximumSize, int minuteDelete) {
        Cache<String, String> makeCache = CacheBuilder.newBuilder()
                        .maximumSize(maximumSize)
                        .expireAfterWrite(minuteDelete, TimeUnit.MINUTES)
                        .build();
        return makeCache;
    }

    private void populateStateCache() throws IOException, URISyntaxException, InterruptedException {
        String urlForStateId = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";

        String stateData = sendRequest(urlForStateId);
        List<List<String>> packageOfStates = ACSDataSource.deserializeACSPackage(stateData);

        if (packageOfStates != null) {
            for (List<String> state : packageOfStates) {
                String name = state.get(0);
                String stateID = state.get(1);
                this.stateCache.put(name, stateID);
            }
        }
    }

    private String sendRequest(String url)
            throws IOException, InterruptedException, URISyntaxException {
        HttpRequest buildAPIRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
        HttpResponse<String> sentAPIResponse =
                HttpClient.newBuilder().build().send(buildAPIRequest, HttpResponse.BodyHandlers.ofString());

        return sentAPIResponse.body();
    }

    public Cache<String, String> getStateCache() {
        return this.stateCache;
    }

    public Cache<String, String> getCountyCache() {
        return this.countyCache;
    }

    public Cache<String, String> getPastRequestCache() {
        return this.pastRequestCache;
    }

    public void addCountyCache(String countyName, String countyID) {
        this.countyCache.put(countyName, countyID);

    }

    public void addPastRequestCache(String name, String response) {
        this.pastRequestCache.put(name, response);
    }

}
