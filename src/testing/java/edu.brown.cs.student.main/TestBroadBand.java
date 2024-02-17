package testing.java.edu.brown.cs.student.main;

import edu.brown.cs.student.main.server.API.ACSCacheData;
import edu.brown.cs.student.main.server.API.BroadBandHandler;
import okio.Buffer;
import org.testng.annotations.Test;
import spark.Spark;
import testing.java.edu.brown.cs.student.main.mocks.MockACS;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import spark.Request;
import spark.Response;
import spark.Route;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Test suite for BroadBandHandler tests, which handles the user inputs and returns a response by checking caches
 * and calling the web API.
 */
//public class TestBroadBand {
//
//    /**
//     * Selects a port for it to use
//     */
//    @BeforeAll
//    public static void setupOnce() {
//        Spark.port(3232);
//        Logger.getLogger("").setLevel(Level.WARNING);
//    }
//
//    private final Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
//    private JsonAdapter<Map<String, Object>> adapter;
//
//    /**
//     * Occurs before each test runs, creates a new BroadBandHandler and mock data source
//     */
//    @BeforeEach
//    public void setup() {
//        MockACS mockedSource = new MockACS(84.1);
//        // Understood why we need to do this, but due to the design of our code we don't actually need this
//        Spark.get("/broadband", new BroadBandHandler(new ACSCacheData()));
//        Spark.awaitInitialization();
//    }
//
//    /**
//     * Occurs after each test, and stops the spark port
//     */
//    @AfterEach
//    public void tearDown() {
//        Spark.unmap("/broadband");
//        Spark.awaitStop();
//    }
//
//    /**
//     * Helper to start a connection to a specific API endpoint/params
//     *
//     * @param apiCall - the call string, or url
//     * @return -  the connection for the given URL, just after connecting
//     * @throws IOException - if the connection fails for some reason
//     */
//    private HttpURLConnection tryRequest(String apiCall) throws IOException {
//        // Configure the connection (but don't actually send a request yet)
//        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
//        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
//        // The request body contains a Json object
//        clientConnection.setRequestProperty("Content-Type", "application/json");
//        // We're expecting a Json object in the response body
//        clientConnection.setRequestProperty("Accept", "application/json");
//
//        clientConnection.connect();
//        return clientConnection;
//    }
//
//    /**
//     * Tests that the result is successful and the output is correct if everything is valid
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestSuccess() throws IOException {
//        HttpURLConnection request = tryRequest("broadband?state=Rhode Island&county=Kent County");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("success", response.get("result"));
//        assertEquals(84.1, response.get("Percentage"));
//    }
//
//    /**
//     * Tests that the result is successful and the output is correct even if the user forgot to put
//     * county after the name of the county
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWNoCounty() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=Rhode Island&county=Kent");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("success", response.get("result"));
//        assertEquals(84.1, response.get("Percentage"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there was no county input
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWNoCountyInput() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=Rhode Island&county=");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Error bad request: County was not Found, please check if inputted, spelling, " +
//                "and capitalization.", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there was no state input
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWNoStateInput() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=&county=Kent County");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Error bad request: State was not Found, please check if " +
//                "inputted, spelling, and capitalization.", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there were no inputs for both county and state
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWNoInputs() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=&county=");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Missing Parameter", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there was only one parameter
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWOneParameter() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Missing Parameter", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there was a misspelling with the county name
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWInvalidCounty() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=Rhode Island&county=Ke");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Error bad request: County was not Found, please check if inputted, spelling, " +
//                "and capitalization.", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there was a misspelling with the state name
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWInvalidState() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=Rh&county=Kent County");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Error bad request: State was not Found, please check if inputted, spelling, " +
//                "and capitalization.", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case the data did not exist in the ACS data
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestWNonexistentData() throws IOException{
//        HttpURLConnection request = tryRequest("broadband?state=Rhode Island&county=Bristol County");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("failure", response.get("result"));
//        assertEquals("Error Datasource", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result fails and shows an error message that helps the user pinpoint the problem
//     * in the case there were no parameters
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testCensusRequestEmptyQueries() throws IOException {
//        HttpURLConnection request = tryRequest("broadband");
//        Map<String, Object> response = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assertEquals("Missing Parameter", response.get("Problem"));
//    }
//
//    /**
//     * Tests that the result, if successful, displays the date and time it was retrieved on
//     *
//     * @throws IOException - if the connection fails for some reason
//     */
//    @Test
//    public void testIfDateTimeExists() throws IOException{
//        HttpURLConnection request
//                = tryRequest("broadband?state=Rhode Island&county=Kent County");
//        Map<String, Object> response
//                = this.adapter.fromJson(new Buffer().readFrom(request.getInputStream()));
//        assert(response.get("It was retrieved on").toString().contains("2023"));
//    }
//}
