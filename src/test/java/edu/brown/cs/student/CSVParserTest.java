package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.*;

import edu.brown.cs.student.main.*;
import java.io.*;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CSVParserTest {

  /**
   * Parses file with no headers
   *
   * @throws IOException - if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testNoHeader() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader("/Users/melanie/Desktop/CS320/csv-mlau998/data/census/no_header.csv");
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
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/census/dol_ri_earnings_disparity.csv");
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
        new FileReader("/Users/melanie/Desktop/CS320/csv-mlau998/data/stars/stardata.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    List<List<String>> result = parser.sortData();
    assertEquals(result.size(), 119617);
  }

  /**
   * Parses a nonexistent file
   *
   * @throws IOException - if file cannot be found
   */
  @Test
  public void testNonexistentFile() throws IOException {
    CreatorFromRow converter = new StringCreator();
    Exception e = assertThrows(IOException.class, () -> new FileReader(" "));
    String message = e.getMessage();
    System.out.println(message);
  }

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
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/malformed/malformed_signs.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    List<List<String>> result = parser.sortData();
    assertNotEquals(result.get(2).size(), parser.getHeaders().size());
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
