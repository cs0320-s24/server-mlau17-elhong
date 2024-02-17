package edu.brown.cs.student.main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.csvhandlers.GlobalData;
import edu.brown.cs.student.main.server.csvhandlers.LoadCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.SearchCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.ViewCSVHandler;
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

public class TestViewCSV {
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
        Spark.get("viewcsv", new ViewCSVHandler(csvData));

        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening

    }

    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on all three endpoints after each test
        Spark.unmap("loadcsv");
        Spark.unmap("viewcsv");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    /**
     * Helper method to start a connection to a specific API endpoint/params
     * @param query
     * @return
     * @throws IOException
     */
    static private HttpURLConnection tryRequest(String query) throws IOException {
        // Configure the connection
        URL requestURL = new URL("http://localhost:"+ Spark.port() +"/" + query);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
        clientConnection.setRequestMethod("GET");
        clientConnection.connect();
        return clientConnection;
    }
    /**
     * Tests a successful viewcsv query
     * @throws IOException
     */
    @Test
    public void testViewCSV() throws IOException {
        // URL returns an valid response code
        HttpURLConnection request = tryRequest("viewcsv");
        assertEquals(200, request.getResponseCode());

        // returns a success status
        Map<String, Object> response = this.mapAdapter.fromJson(new Buffer().readFrom(request.getInputStream()));
        assertEquals("success", response.get("status"));
        request.disconnect();
    }

    /**
     * Tests a viewcsv query without first having a loadcsv query
     */
}
