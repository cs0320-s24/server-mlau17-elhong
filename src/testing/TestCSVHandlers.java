package testing;

public class TestCSVHandlers {

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

        @Test

}
