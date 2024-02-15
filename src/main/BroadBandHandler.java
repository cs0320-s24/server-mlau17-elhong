package main;

import com.google.common.cache.Cache;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import spark.Request;
import spark.Response;
import spark.Route;

import java.awt.desktop.SystemSleepEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BroadBandHandler implements Route{

    private List<List<String>> bBD;

    public BroadBandHandler(List<List<String>> broadBandData) throws URISyntaxException, IOException, InterruptedException {
        this.bBD = broadBandData;
    }

    Cache<String, String> stateIdCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build();

    Cache<String, String> countyIdCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build();

    private void CacheStateId() throws URISyntaxException, IOException, InterruptedException {

        String url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";

        try {
            HttpRequest buildStateAPIRequest =
                    HttpRequest.newBuilder()
                            .uri(new URI(url))
                            .GET()
                            .build();

            HttpResponse<String> sentStateAPIResponse =
                    HttpClient.newBuilder()
                            .build()
                            .send(buildStateAPIRequest, HttpResponse.BodyHandlers.ofString());


            String stateData = sentStateAPIResponse.body();
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter = moshi.adapter(Types.newParameterizedType(List.class, List.class));
            List<List<String>> data = adapter.fromJson(stateData);

            if (data != null) {
                for (List<String> state : data) {
                    String name = state.get(0);
                    String stateID = state.get(1);
                    stateIdCache.put(name, stateID);
                }
            }
        } catch (URISyntaxException){
            System.err.println("Error: URI is wrong, get it together");
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

    private void findBroadBand(String stateName, String countyName) throws URISyntaxException, IOException,
            InterruptedException {

        String idOfState = stateIdCache.getIfPresent(stateName);

        if (idOfState != null) {

            String url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + idOfState;

            try {
                String countyData = sendRequest(url);
                List<List<String>> dataPackage = ACSDataSource.deserializeACSPackage(countyData);
                if (dataPackage != null) {
                    for (List<String> state : dataPackage) {
                        String name = state.get(0);
                        if (name.equals(countyName)) {
                            String countyID = state.get(2);
                            countyIdCache.put(countyName, countyID);
                        }
                    }
                }

                String idOfCounty = countyIdCache.getIfPresent(countyName);

                String finalURL = "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
                        + idOfCounty + "&in=state:" + idOfState;

                String broadBandData = sendRequest(finalURL);
                this.bBD = ACSDataSource.deserializeACSPackage(broadBandData);

            } catch (URISyntaxException) {
                System.err.println("Error: URI is wrong, get it together");
            }
        }
    }


    /**
     * This handle method needs to be filled by any class implementing Route. When the path set in
     * edu.brown.cs.examples.moshiExample.server.Server gets accessed, it will fire the handle method.
     *
     * <p>NOTE: beware this "return Object" and "throws Exception" idiom. We need to follow it because
     * the library uses it, but in general this lowers the protection of the type system.
     *
     * @param request  The request object providing information about the HTTP request
     * @param response The response object providing functionality for modifying the response
     */
    @Override
    public Object handle(Request request, Response response) {
        // If you are interested in how parameters are received, try commenting out and
        // printing these lines! Notice that requesting a specific parameter requires that parameter
        // to be fulfilled.
        // If you specify a queryParam, you can access it by appending ?parameterName=name to the
        // endpoint
        // ex. http://localhost:3232/activity?participants=num
        Set<String> params = request.queryParams();
        System.out.println(params);
        String stateName = request.queryParams("State Name");
        String countyName = request.queryParams("County Name");
        System.out.println(stateName);

        // Creates a hashmap to store the results of the request
        Map<String, Object> responseMap = new HashMap<>();
        try {
            responseMap.put("result", "success");
            responseMap.put("Percentage", this.bBD);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("result", "Exception");
        }
        return responseMap;
    }
}
