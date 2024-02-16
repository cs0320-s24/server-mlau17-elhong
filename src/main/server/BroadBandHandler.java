package main.server;

import com.google.common.cache.Cache;
import spark.Request;
import spark.Response;
import spark.Route;

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

import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;

public class BroadBandHandler implements Route {

    private List<List<String>> bBD = null;

    public Cache<String,String> customizableCache(int maximumSize, int minuteDelete) {
        Cache<String,String> makeCache = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(minuteDelete, TimeUnit.MINUTES)
                .build();

        return makeCache;
    }

    Cache<String,String> stateCache = customizableCache(60, 50);
    Cache<String,String> countyCache = customizableCache(100, 50);

    private void CacheStateId() throws IOException, InterruptedException {

        String url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";

        try {
            String stateData = sendRequest(url);
            List<List<String>> dataOfStates = ACSDataSource.deserializeACSPackage(stateData);

            if (dataOfStates != null) {
                for (List<String> state : dataOfStates) {
                    String name = state.get(0);
                    String stateID = state.get(1);
                    stateCache.put(name, stateID);
                }
            }
        } catch (URISyntaxException e) {
            System.err.println("Error: URI is invalid.");
        }
    }

    private String sendRequest(String url) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest buildAPIRequest =
                HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();

        HttpResponse<String> sentAPIResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildAPIRequest, HttpResponse.BodyHandlers.ofString());

        return sentAPIResponse.body();
    }

    private void findBroadBand(String stateName, String countyName) throws IOException,
            InterruptedException {

        CacheStateId();

        String idOfState = stateCache.getIfPresent(stateName);

        if (idOfState != null) {

            String url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + idOfState;

            try {
                String countyData = sendRequest(url);
                List<List<String>> dataPackage = ACSDataSource.deserializeACSPackage(countyData);
                if (dataPackage != null) {
                    for (List<String> state : dataPackage) {
                        if (countyCache.getIfPresent(countyName) == null) {
                            String name = state.get(0);
                            if (name.equals(countyName + " County, " + stateName)) {
                                String countyID = state.get(3);
                                countyCache.put(countyName, countyID);
                            }
                        }
                    }
                }

                String idOfCounty = countyCache.getIfPresent(countyName);

                String finalURL = "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + idOfCounty + "&in=state:" + idOfState;

                String broadBandData = sendRequest(finalURL);
                this.bBD = ACSDataSource.deserializeACSPackage(broadBandData);

            } catch (URISyntaxException e) {
                System.err.println("Error: URI is wrong, get it together");
            }
        }
    }


    /**
     * This handle method needs to be filled by any class implementing Route. When the path set in
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     */
    @Override
    public Object handle(Request request, Response response) throws IOException, InterruptedException {

        String stateName = request.queryParams("State");
        String countyName = request.queryParams("County");
        Map<String, Object> responseMap = new HashMap<>();
        findBroadBand(stateName, countyName);

        try {
            responseMap.put("result", "success");
            responseMap.put("for", countyName + "in" + stateName);
            double band = Double.parseDouble(this.bBD.get(0).get(0));
            responseMap.put("Percentage", band);

            LocalDateTime todayDateTime = LocalDateTime.now();
            DateTimeFormatter dateFormatter
                    = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String theDateTime = todayDateTime.format(dateFormatter);
            responseMap.put("This data was updated on: ", theDateTime);

            return responseMap;
        } catch (Exception e) {
            responseMap.put("result", "error: problem with inputs");
        }
        return responseMap;
    }
}
