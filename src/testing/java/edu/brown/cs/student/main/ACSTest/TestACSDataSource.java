package edu.brown.cs.student.main.ACSTest;

import edu.brown.cs.student.main.server.API.ACSDataSource;
import edu.brown.cs.student.main.server.API.APIException;
import org.testng.annotations.Test;
import java.util.List;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Test suite for ACSDataSource, which contains a deserializer method to make sure it is returning a
 * List<List<String>>
 */
public class TestACSDataSource {

    /**
     * Tests the deserializer method in ACSDataSource to make sure it is turning it into a List<List<String>>
     */
    @Test
    public void testDeserializer() throws APIException {
        List<List<String>> testS = ACSDataSource.deserializeACSPackage("menu copy.json");
        assertEquals("squash", testS.get(0).get(2));
    }

    /**
     * Tests the deserializer method in ACSDataSource when the JSON does not exist, which should
     * throw an APIException
     */
    @Test
    public void testEmptyJSON(){
        assertThrows(APIException.class, () -> {
            ACSDataSource.deserializeACSPackage("");
        });
    }
}
