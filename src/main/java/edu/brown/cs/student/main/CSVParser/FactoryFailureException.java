package edu.brown.cs.student.main.CSVParser;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an error provided to catch any error that may occur when you create an object from a row.
 * Feel free to expand or supplement or use it for other purposes.
 */
public class FactoryFailureException extends Exception {
  final List<String> row;

  /**
   * Constructor for the FactoryFailureException
   *
   * @param message - the message will be returned as a temporary object of the superclass
   * @param row - the row that will be made into a new Array List
   */
  public FactoryFailureException(String message, List<String> row) {
    super(message);
    this.row = new ArrayList<>(row);
  }
}
