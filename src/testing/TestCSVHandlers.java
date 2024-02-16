package testing;

public class TestCSVHandlers {

/*
    @BeforeAll
    public static void setup_before_everything() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    *//**
     * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
     * need to replace the reference itself. We clear this state out after every test runs.
     *//*
    final List<String> csvData = new ArrayList<>();*/
/*
    @BeforeEach
    public void setup() {
        // Re-initialize state, etc. for _every_ test method run
        this.csvData.clear();

        // In fact, restart the entire Spark server for every test!
        Spark.get("order", new LoadCSVHandler();
        Spark.get("activity", new ActivityHandler());
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

        @Test*/

}
