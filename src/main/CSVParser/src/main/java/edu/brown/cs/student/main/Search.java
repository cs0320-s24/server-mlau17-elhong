package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search takes in a variety of parameters to help the user find the value they are looking for
 * within a specific CSV file.
 */
public class Search {
  private edu.brown.cs.student.main.CSVParser csvtoparse;
  private String value;
  private String column;
  private ArrayList<Integer> containedRows;
  private String nameOrindex;

  /**
   * Constructor for the Search class.
   *
   * @param parser - the file that has been parsed.
   * @param input - the value that the user is looking for in the file.
   * @param columnIdentifier - the variable that stores the designated column for searching as
   *     requested by the user.
   * @param nameOrIndex - the variable that stores the user's preference for utilizing either header names or
   *     column indices.
   */
  public Search(edu.brown.cs.student.main.CSVParser parser, String input, String columnIdentifier, String nameOrIndex) {
    this.csvtoparse = parser;
    this.value = input;
    this.column = columnIdentifier;
    this.nameOrindex = nameOrIndex;
    this.containedRows = new ArrayList<Integer>();
  }

  /**
   * Second constructor for the Search class in case there is no column identifier.
   *
   * @param parser - the file that has been parsed;
   * @param input - the value that the user is looking for in the file.
   */
  public Search(edu.brown.cs.student.main.CSVParser parser, String input) {
    this(parser, input, null, null);
  }

  /**
   * Method that finds where the input is located using column indices.
   *
   * @throws ArrayIndexOutOfBoundsException - when the inputted column index is out of bounds of the
   *     file
   * @throws IOException - when the buffered reader cannot read/find the file.
   * @throws edu.brown.cs.student.main.FactoryFailureException - when the object cannot be created.
   */
  public void findValueIndices()
          throws IOException, edu.brown.cs.student.main.FactoryFailureException, ArrayIndexOutOfBoundsException {
    List<List<String>> fileParsed = this.csvtoparse.sortData();
    int index = Integer.parseInt(this.column);
    for (List<String> eachRow : fileParsed) {
      Integer rowIndices = fileParsed.indexOf(eachRow) + 1;
      try {
        eachRow.get(index);
      } catch (ArrayIndexOutOfBoundsException e) {
        System.err.println("The given index is out of bounds.");
      }
      if (eachRow.get(index).toLowerCase().contains(this.value.toLowerCase())
              && !this.containedRows.contains(rowIndices)) {
        this.containedRows.add(rowIndices);
      }
    }
  }


  /**
   * Method that find where the input is located by going through the entire dataset.
   *
   * @throws IOException - when the buffered reader cannot read/find the file.
   * @throws edu.brown.cs.student.main.FactoryFailureException - when the object cannot be created.
   */
  public void findEntire() throws IOException, FactoryFailureException {
    List<List<String>> fileParsed = this.csvtoparse.sortData();
    for (List<String> eachRow : fileParsed) {
      Integer rowIndices = fileParsed.indexOf(eachRow) + 1;
      if (eachRow != null) {
        for (String eachValue : eachRow) {
          if (eachValue.toLowerCase().contains(this.value.toLowerCase())
              && !this.containedRows.contains(rowIndices)) {
            this.containedRows.add(rowIndices);
          }
        }
      }
    }
  }

  /**
   * Method that finds where the input is located using column names.
   *
   * @throws IOException - when the buffered reader cannot read/find the file.
   * @throws FactoryFailureException - when the object cannot be created.
   */
  public void findValueName() throws IOException, FactoryFailureException {
    List<List<String>> fileParsed = this.csvtoparse.sortData();
    if (this.csvtoparse.getHeaders().contains(this.column)) {
      int index = this.csvtoparse.getHeaders().indexOf(this.column);
      for (List<String> eachRow : fileParsed) {
        Integer rowIndices = fileParsed.indexOf(eachRow) + 1;
        if (eachRow != null
            && eachRow.get(index).toLowerCase().contains(this.value.toLowerCase())
            && !this.containedRows.contains(rowIndices)) {
          this.containedRows.add(rowIndices);
        }
      }
    }
  }

  /**
   * Method that based on whether the CSV file contains a header and the user's choice to use column
   * identifiers, will populate the containedRows list accordingly.
   *
   * @throws ArrayIndexOutOfBoundsException - when the inputted column index is out of bounds of the
   *     file
   * @throws IOException - when the buffered reader cannot read/find the file.
   * @throws FactoryFailureException - when the object cannot be created.
   */
  public void searcher()
      throws IOException, FactoryFailureException, ArrayIndexOutOfBoundsException {
    List<List<String>> fileParsed = this.csvtoparse.sortData();
    if (this.csvtoparse.getHeaders() != null) {
      if (this.column != null && this.nameOrindex.equals("name")) {
        findValueName();
      } else if (this.column != null && this.nameOrindex.equals("number")) {
        findValueIndices();
      } else {
        findEntire();
      }
    } else if (this.csvtoparse.getHeaders() == null && this.column != null) {
      findValueIndices();
    } else {
      findEntire();
    }
  }

  /**
   * Method that while considering the presence of a header in the file, prints rows containing the
   * inputted value, accounting for an additional row if a header exists
   */
  public void print() {
    if (this.containedRows.isEmpty()) {
      System.out.println(
          "The value "
              + " '"
              + this.value
              + "' "
              + " does not exist in this file or "
              + "the column name is wrong");
    } else if (this.csvtoparse.getHeaders() != null) {
      System.out.println("Here are the rows containing '" + this.value + "' ");
      for (Integer eachNumber : this.containedRows) {
        System.out.println(eachNumber + 1);
      }
    } else {
      System.out.println("Here are the rows containing '" + this.value + "' ");
      for (Integer eachNumber : this.containedRows) {
        System.out.println(eachNumber);
      }
    }
  }

  public List<Integer> getContainedRow() {
    return this.containedRows;
  }
}
