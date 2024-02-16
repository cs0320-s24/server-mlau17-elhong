package testing;


import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.csvhandlers.GlobalData;
import edu.brown.cs.student.main.server.csvhandlers.LoadCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.SearchCSVHandler;
import edu.brown.cs.student.main.server.csvhandlers.ViewCSVHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.Buffer;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCSVHandlers {

    /**
     * Creates a port when the class is run
     */
    @BeforeAll
    public static void setup_before_everything() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
     }

    /**
     * Shared state for all tests. We need to be able to mutate it but never
     * need to replace the reference itself. We clear this state out after every test runs.
     */

    final GlobalData csvData = new GlobalData();

    @BeforeEach
    public void setup() {
        // Re-initialize state, etc. for _every_ test method run
        this.csvData.clear();

    Moshi moshi = new Moshi.Builder().build();
    // moshi adapter, converts map with csv data into a Json Object
        this.mapAdapter = moshi.adapter(this.mapStringObject);


    // restart the entire Spark server for every test
        Spark.get("loadcsv", new LoadCSVHandler(this.csvData);
        Spark.get("viewcsv", new ViewCSVHandler(this.csvData));
        Spark.get("searchcsv", new SearchCSVHandler(this.csvData));
        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening

}
        @AfterEach
        public void teardown() {
        // Gracefully stop Spark listening on both endpoints after each test
        Spark.unmap("order");
        Spark.unmap("activity");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    /**
     * Tests a successful loadcsv query
     * @throws IOException
     */
    @Test
    public void testLoadCSVResponse() throws IOException {
        // URL returns an valid response code
        HttpURLConnection request = tryRequest("loadcsv?"+"filePath="+this.data1);
        assertEquals(200, request.getResponseCode());

        // returns a success status
        Map<String, Object> response = this.mapAdapter.fromJson(new Buffer().readFrom(request.getInputStream()));
        assertEquals("success", response.get("status"));


    }


}
