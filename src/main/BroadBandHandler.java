package main;

import org.testng.internal.Graph;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.security.Key;
import java.util.Map;
import java.util.Set;

public class BroadBandHandler implements Route{
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
        //     System.out.println(params);
        String stateName = request.queryParams("State Name");
        String countyName = request.queryParams("County Name");
        System.out.println(stateName);

        // Creates a hashmap to store the results of the request
        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Sends a request to the API and receives JSON back
            String broadBandPackage = this.sendRequest(stateName);
            // Deserializes JSON into an Activity
            // ACSDataSource acsData = AcsAPIUtilities.deserializeActivity(broadBandPackage);
            // Adds results to the responseMap
            responseMap.put("result", "success");
            //responseMap.put("Percentage", acsData);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            // This is a relatively unhelpful exception message. An important part of this sprint will be
            // in learning to debug correctly by creating your own informative error messages where Spark
            // falls short.
            responseMap.put("result", "Exception");
        }
        return responseMap;
    }

    private String sendRequest(String nameOfState)
            throws URISyntaxException, IOException, InterruptedException {

        HttpRequest buildAcsAPIRequest =
                HttpRequest.newBuilder()
                        .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + nameOfState))
                        .GET()
                        .build();

        // Send that API request then store the response in this variable. Note the generic type.
        HttpResponse<String> sentAcsAPIResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildAcsAPIRequest, HttpResponse.BodyHandlers.ofString());

        // What's the difference between these two lines? Why do we return the body? What is useful from
        // the raw response (hint: how can we use the status of response)?
        System.out.println(sentAcsAPIResponse);
        System.out.println(sentAcsAPIResponse.body());

        return sentAcsAPIResponse.body();
    }
}
