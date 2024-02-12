package edu.brown.cs.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs.student.main.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class SearchTest {
  /**
   * Basic search example, searching file with no header and value exists
   *
   * @throws IOException if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testBasicSearch() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader("/Users/melanie/Desktop/CS320/csv-mlau998/data/census/no_header.csv");
    CSVParser parser = new CSVParser(converter, false, reader);
    Search search = new Search(parser, "black");
    List<Integer> rowsContaining = new ArrayList<>();
    rowsContaining.add(2);
    System.out.println(rowsContaining);
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    assertEquals(rowsContaining.get(0), val.get(0));
  }

  /**
   * Basic search example, searching file with header and value exists
   *
   * @throws IOException if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testBasicSearch2() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/census/dol_ri_earnings_disparity.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    Search search = new Search(parser, "black");
    List<Integer> rowsContaining = new ArrayList<>();
    rowsContaining.add(2);
    System.out.println(rowsContaining);
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    assertEquals(rowsContaining.get(0), val.get(0));
  }

  /**
   * Basic search example, searching file when value does not exist
   *
   * @throws IOException if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testDNESearch() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/census/dol_ri_earnings_disparity.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    Search search = new Search(parser, "zo");
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    assertTrue(val.isEmpty());
  }

  /**
   * Complex search example, searching file when value, header, and column identifier exists,
   * specifically uses header name
   *
   * @throws IOException if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testComplexSearch() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/census/dol_ri_earnings_disparity.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    Search search = new Search(parser, "black", "data type", "name");
    List<Integer> rowsContaining = new ArrayList<>();
    rowsContaining.add(2);
    System.out.println(rowsContaining);
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    assertEquals(rowsContaining.get(0), val.get(0));
  }

  /**
   * Complex search example, searching file when value, header, and column identifier exists,
   * specifically uses header index
   *
   * @throws IOException if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testComplexSearch2() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/census/dol_ri_earnings_disparity.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    Search search = new Search(parser, "black", "data type", "index");
    List<Integer> rowsContaining = new ArrayList<>();
    rowsContaining.add(2);
    System.out.println(rowsContaining);
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    assertEquals(rowsContaining.get(0), val.get(0));
  }

  /**
   * Complex search example, searching file when value and header exists but not column identifier,
   * specifically uses header index
   *
   * @throws IOException if file cannot be found
   * @throws FactoryFailureException - if object cannot be created
   */
  @Test
  public void testComplexSearch3() throws IOException, FactoryFailureException {
    CreatorFromRow<List<String>> converter = new StringCreator();
    FileReader reader =
        new FileReader(
            "/Users/melanie/Desktop/CS320/csv-mlau998/data/census/dol_ri_earnings_disparity.csv");
    CSVParser parser = new CSVParser(converter, true, reader);
    Search search = new Search(parser, "black", null, "index");
    List<Integer> rowsContaining = new ArrayList<>();
    rowsContaining.add(2);
    System.out.println(rowsContaining);
    search.searcher();
    List<Integer> val = search.getContainedRow();
    System.out.println(val);
    assertEquals(rowsContaining.get(0), val.get(0));
  }
}
