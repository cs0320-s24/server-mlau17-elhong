package edu.brown.cs.student.main;

import java.util.List;

public class StringCreator implements CreatorFromRow<List<String>> {

  /**
   * Create method that takes in a list of strings and changes it to list of strings, which allows
   * the parser to work with a variety of types
   *
   * @param row - a row of the csv which will be converted into a list of strings
   * @throws FactoryFailureException - when there is a failure creating the object
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
