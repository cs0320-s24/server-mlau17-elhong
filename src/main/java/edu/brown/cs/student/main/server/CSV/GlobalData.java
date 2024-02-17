package edu.brown.cs.student.main.server.CSV;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as a wrapper class to add a layer of abstraction between the CSV data and the
 * handler classes. This way, the data obtained from LoadCSVHandler can be storied and used globally
 * by the View and Search CSVHandler classes.
 */
public class GlobalData {
  private List<List<String>> csvData = new ArrayList<>();

  public GlobalData() {}

  /**
   * Getter method for the data
   *
   * @return the parsed csv data
   * */
  public List<List<String>> getCsvData() {
    return this.csvData;
  }

  /** Setting csv data */
  public void setCsvData(List<List<String>> newData) {
    this.csvData.clear();
    this.csvData = newData;
  }

  /** Clearing the dataset */
  public void clear() {
    this.csvData.clear();
  }

  /**
   * Checking id empty
   *
   * @return boolean is empty
   * */
  public boolean isEmpty() {
    if (this.csvData.isEmpty()) {
      return true;
    }
    return false;
  }
}
