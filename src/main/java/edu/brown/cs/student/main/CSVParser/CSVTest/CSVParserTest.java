package edu.brown.cs.student.main.CSVParser.CSVTest;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import edu.brown.cs.student.main.CSVParser.CSVParser;
import edu.brown.cs.student.main.CSVParser.CreatorFromRow;
import edu.brown.cs.student.main.CSVParser.FactoryFailureException;
import edu.brown.cs.student.main.CSVParser.StringCreator;
import java.io.*;
import java.util.List;
import org.testng.annotations.Test;

public class CSVParserTest {

  /**
   * This class tests the Parser class for the following situations: - CSV data with and without
   * column headers - CSV data in different Reader types (e.g., StringReader and FileReader) - CSV
   * data with inconsistent column count - CSV data that lies outside the protected directory -
   * Using multiple CreatorFromRow classes to extract CSV data in different formats - RegEx
   * exceptions, find broken - spaces, ints
   *
   * @throws IOException - if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testNoHeader() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/main.csvparser/data/census/no_header.csv");
    CSVParser parser = new CSVParser(converter, false, reader);
    List<List<String>> result = parser.sortData();
    assertEquals(result.size(), 6);
    System.out.println(result.get(0));
  }

  /**
   * Parses file with headers
   *
   * @throws IOException - if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testHeader() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/main.csvparser/data/census/dol_ri_earnings_disparity.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    List<List<String>> result = parser.sortData();
    assertEquals(result.size(), 6);
  }

  /**
   * Parses file with many rows and header
   *
   * @throws IOException - if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testManyRows() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/main.csvparser/data/stars/stardata.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    List<List<String>> result = parser.sortData();
    assertEquals(result.size(), 119617);
  }

  /**
   * Parses a nonexistent file
   *
   * @throws IOException - if file cannot be found
   */
  /**
   * @Test public void testNonexistentFile() throws IOException { CreatorFromRow converter = new
   * StringCreator(); Exception e = assertThrows(IOException.class, () -> new FileReader("la"));
   * String message = e.getMessage(); System.out.println(message); }
   */

  /**
   * Parses a file with inconsistent column count
   *
   * @throws IOException - if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testInconsistentColumn() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/emilyhong/Desktop/cs0320/server-mlau17-elhong/src/main/main.csvparser/data/malformed/malformed_signs.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    List<List<String>> result = parser.sortData();
    assertEquals(result.get(2).size(), 2);
  }

  /**
   * CSVParser on Reader object StringReader
   *
   * @throws IOException if the file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testStringReader() throws IOException, FactoryFailureException {
    CreatorFromRow converter = new StringCreator();
    String exampleData = "John, Doe, Jane, Doe";
    StringReader reader = new StringReader(exampleData);
    CSVParser parser = new CSVParser(converter, false, reader);
    List<String> result = parser.sortData();
    System.out.println(result);
    assertTrue(result.size() == 1);
    System.out.println(result.size());
    // Result is 3 so the regex is not splitting correctly instead it is seen as one
  }

  //    /**
  //     * CSVParser on Reader object StringReader
  //     *
  //     * @throws IOException if the file cannot be found
  //     * @throws FactoryFailureException - if object cannot be created
  //     */
  //    @Test
  //    public void testStringReader2() throws IOException, FactoryFailureException {
  //        CreatorFromRow<List<String>> converter = new StringCreator();
  //        StringReader reader =
  //                new
  // StringReader("/Users/melanie/Desktop/CS320/csv-mlau998/data/stars/stardata.csv");
  //        CSVParser parser = new CSVParser(converter,true, reader);
  //        List<List<String>> result = parser.sortData();
  //        System.out.println(result);
  //        assertEquals(result.size(), 119617);
  //    }

  //    /**
  //     * Parse using CreatorFromRow class that returns a List of Integers
  //     *
  //     * @throws IOException if file cannot be found
  //     */
  //    @Test
  //    public void testIntegerCreate() throws IOException, FactoryFailureException {
  //        CreatorFromRow<List<Integer>> IntListCreate = new IntegerCreator();
  //        FileReader reader =
  //                new
  // FileReader("/Users/melanie/Desktop/CS320/csv-mlau998/data/stars/ten-star.csv");
  //        CSVParser parser = new CSVParser(IntListCreate, true, reader);
  //        List<List<Integer>> data = parser.sortData();
  //        System.out.println(data);
  //    }
}
