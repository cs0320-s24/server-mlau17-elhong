package main.csvparser.src.main.java.edu.brown.cs.student.main;

import java.util.ArrayList;
import java.util.List;

public class IntegerCreator implements CreatorFromRow<List<Integer>> {

  /**
   * Create method that takes in a list of strings and changes it to list of integers, which allows
   * the parser to work with a variety of types
   *
   * @param row - a row of the csv which will be converted into a list of integers
   * @throws FactoryFailureException - when there is a failure creating the object
   */
  @Override
  public List<Integer> create(List<String> row) throws FactoryFailureException {
    List<Integer> convertedRow = new ArrayList<>();
    for (String eachValue : row) {
      convertedRow.add(Integer.valueOf(eachValue));
    }
    return convertedRow;
  }
}
