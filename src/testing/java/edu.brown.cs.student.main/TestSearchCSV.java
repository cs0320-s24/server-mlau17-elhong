package edu.brown.cs.student.main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.CSV.GlobalData;
import edu.brown.cs.student.main.server.CSV.LoadCSVHandler;
import edu.brown.cs.student.main.server.CSV.SearchCSVHandler;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the SearchCSVHandler class for the following cases:
 * - successfully searching a file having laoded first
 * - a query for search without loading first
 * - a searchcsv query with an invalid word
 * - a searchcsv query with no word
 * **/
public class TestSearchCSV {
    private JsonAdapter<Map<String, Object>> mapAdapter;
    private final Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);

    /**
     * Creates a port when the class is run
     */
    @BeforeAll
    public static void setup_before_all() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING);
    }

    /**
     * Shared state for all tests. We need to be able to mutate it but never
     * need to replace the reference itself. We clear this state out after every test runs.
     */

    @BeforeEach
    public void setup() {
        Moshi moshi = new Moshi.Builder().build();
        // moshi adapter, converts map with csv data into a Json Object
        this.mapAdapter = moshi.adapter(this.mapStringObject);

        GlobalData csvData = new GlobalData();
        // Re-initialize state, etc. for _every_ test method run
        csvData.clear();

        // restart the entire Spark server for every test
        LoadCSVHandler load = new LoadCSVHandler(csvData);
        Spark.get("loadcsv", new LoadCSVHandler(csvData));
        Spark.get("searchcsv", new SearchCSVHandler(csvData, load));

        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening

    }

    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on all three endpoints after each test
        Spark.unmap("loadcsv");
        Spark.unmap("searchcsv");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    /**
     * Helper method to start a connection to a specific API endpoint/params
     *
     * @param query
     * @return
     * @throws IOException
     */
    static private HttpURLConnection tryRequest(String query) throws IOException {
        // Configure the connection
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + query);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
        clientConnection.setRequestMethod("GET");
        clientConnection.connect();
        return clientConnection;
    }


    /**
     * Tests a successful searchcsv query
     *
     * @throws IOException
     */
    @Test
    public void testSearchCSV() throws IOException {
        // URL returns an valid response code
        HttpURLConnection request = tryRequest("loadcsv?filepath=/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/java/edu/brown/cs/student/main/data/ri_city_town_income_acs.csv&header=Yes");
        assertEquals(200, request.getResponseCode());
        tryRequest("searchcsv?searchword=East");
        assertEquals(200, request.getResponseCode());

        Map<String, Object> response = this.mapAdapter.fromJson(new Buffer().readFrom(request.getInputStream()));
        assertEquals("success", response.get("status"));
        request.disconnect();
    }

    /**
     * Tests a searchcsv query without first having a loadcsv query
     */
    @Test
    public void testSearchWithoutLoadCSV() throws IOException {
        // URL returns an invalid response code
        HttpURLConnection request = tryRequest("searchcsv?searchword=tom");
        assertEquals(500, request.getResponseCode());
        request.disconnect();
    }


    /**
     * Tests a searchcsv query with an invalid word
     */
    @Test
    public void testInvalidSearchCSV() throws IOException {
        // URL returns an valid response code
        HttpURLConnection request = tryRequest("loadcsv?filepath=/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/java/edu/brown/cs/student/main/data/ri_city_town_income_acs.csv&header=Yes");
        assertEquals(200, request.getResponseCode());
        tryRequest("searchcsv?searchword=lolol");
        assertEquals(200, request.getResponseCode());
        Map<String, Object> response = this.mapAdapter.fromJson(new Buffer().readFrom(request.getInputStream()));
        // this should pass but the mapAdapter is reading the initial request instead of the second one
        // I am not sure how to make the httpURLConnection remember that is has loaded before searching...
        assertEquals("error", response.get("status"));
        request.disconnect();
    }

    /**
     * Tests a searchcsv query with no searchword
     */
    @Test
    public void testNoWordSearchCSV() throws IOException {
        // URL returns an valid response code
        HttpURLConnection request = tryRequest("loadcsv?filepath=/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/java/edu/brown/cs/student/main/data/ri_city_town_income_acs.csv&header=Yes");
        assertEquals(200, request.getResponseCode());
        tryRequest("searchcsv");
        assertEquals(200, request.getResponseCode());
        request.disconnect();
    }
}
