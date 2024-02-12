package edu.brown.cs.student.main;

import static java.lang.System.exit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The CSV parsing tool collects and transforms data from a Reader object, with the user specifying
 * both the Reader object and the data type.
 *
 * @param <T> - object that represent the type of the data that the CSV parsing tool will extract
 *     from a Reader source.
 */
public class CSVParser<T> {
  private String fileName;
  private CreatorFromRow<T> parseType;
  private boolean hasHeader;
  private Reader parseFile;
  private List<T> data;
  private List<String> headers;
  private List<String> lowerCaseHeaders;

  /**
   * Constructor for the CSVParser class
   *
   * @param typeToParse - the type of the data that we are parsing
   * @param header - whether the file has a header or not
   * @param fileToParse - the CSV file that we are parsing
   */
  public CSVParser(CreatorFromRow<T> typeToParse, boolean header, Reader fileToParse) {
    this.parseType = typeToParse;
    this.hasHeader = header;
    this.parseFile = fileToParse;
    this.data = new ArrayList<>();
  }

  /**
   * Method that iterates through the CSV file, collecting and storing the data
   *
   * @throws IOException - when the buffered reader cannot read/find the file.
   * @throws FactoryFailureException - when the object cannot be created.
   * @return - a list of object T which we iterate in our search class to find specific values
   */
  public List<T> sortData() throws IOException, FactoryFailureException {
    final Pattern regexSplitCSVRow =
        Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
    BufferedReader reader = new BufferedReader(this.parseFile);
    try {
      String eachLine;
      if (this.hasHeader && ((eachLine = reader.readLine()) != null)) {
        this.headers = Arrays.asList(regexSplitCSVRow.split(eachLine));
      }
      while ((eachLine = reader.readLine()) != null) {
        List<String> rowData = Arrays.asList(regexSplitCSVRow.split(eachLine));
        this.data.add(this.parseType.create(rowData));
      }
    } catch (IOException e) {
      System.err.println("File is not found.");
      exit(0);
    } catch (FactoryFailureException e) {
      System.err.println("Object cannot be created.");
      exit(0);
    }
    return this.data;
  }

  /**
   * Method that changes the header names to lowercase and helps us access it
   *
   * @return - a list of the header names
   */
  public List<String> getHeaders() {
    this.lowerCaseHeaders = new ArrayList<>();
    if (this.headers == null) {
      return this.lowerCaseHeaders = null;
    } else {
      for (String eachHeader : this.headers) {
        this.lowerCaseHeaders.add(eachHeader.toLowerCase());
      }
    }
    return this.lowerCaseHeaders;
  }
}
