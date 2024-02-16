package edu.brown.cs.student.main.server.csvhandlers;

import java.util.ArrayList;
import java.util.List;

/** This class serves as a wrapper class to add a layer of abstraction between the CSV data and the handler classes. This way,
* the data obtained from LoadCSVHandler can be storied and used globally by the View and Search CSVHandler classes.
*/
public class GlobalData {
  private List<List<String>> csvData = new ArrayList<>();

  public GlobalData() {}

  public List<List<String>> getCsvData() {
    return this.csvData;
  }

  public void setCsvData(List<List<String>> newData) {
    this.csvData = newData;
  }
}
