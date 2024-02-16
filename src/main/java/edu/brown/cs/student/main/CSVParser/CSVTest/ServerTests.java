package edu.brown.cs.student.main.CSVParser.CSVTest;

import static org.testng.AssertJUnit.assertEquals;

import edu.brown.cs.student.main.CSVParser.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;

public class ServerTests {

  @Test
  public void testACS_CSVdata() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/data/ri_city_town_income_acs.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    Search search = new Search(parser, "Newport");
    List<Integer> rowsContaining = new ArrayList<>();
    rowsContaining.add(23);
    System.out.println(rowsContaining);
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    // asserting that the CSVParser and search work on the ACS dataset
    assertEquals(rowsContaining.get(0), val.get(0));
  }
}
