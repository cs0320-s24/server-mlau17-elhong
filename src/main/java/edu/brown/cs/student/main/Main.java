package edu.brown.cs.student.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/** The Main class of our project. This is where execution begins. */
public final class Main {
  private String Path;
  private FileReader readTheFile;
  private boolean containHeader;
  private String value;
  private boolean isColumnIdentifierUsed;
  private String Identifier;
  private Search timetoSearch;
  private String nameOrIndex;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   * @throws IOException - when a file is not readable or found
   * @throws FactoryFailureException - when an object cannot be created f
   */
  public static void main(String[] args) throws IOException, FactoryFailureException {
    new Main(args).run();
  }

  private Main(String[] args) {}

  private void run() throws IOException, FactoryFailureException {
    /*Scanner allows us to collect and store user input.*/
    Scanner input = new Scanner(System.in);
    try {
      System.out.println(
          "Welcome User! This program can read a CSV File and find where the value you are looking for is located! "
              + "Please enter your EXACT/ABSOLUTE file path: ");
      /*Finding the path to the CSV File and then reading it using FileReader.*/
      this.Path = input.nextLine();
      this.readTheFile = new FileReader(this.Path);
      /*If the file cannot be found, print out an error message.*/
    } catch (FileNotFoundException | InputMismatchException e) {
      System.err.println(
          "Cannot find/open this file. Please check that your file path is exact and there are no extra spaces.");
      input.nextLine();
    }
    System.out.println(
        "Great, we have found the file. Does the file contain any headers? (Yes - true & No - false)");
    /*After finding the file, check if the file contains any headers.*/
    try {
      this.containHeader = input.nextBoolean();
      /*If the format is incorrect, print out an error message and prompt for another input.*/
    } catch (InputMismatchException e) {
      System.out.println("Error: Please follow the correct format.");
      input.nextLine();
    }

    /*Create the CSVParser using the user input to collect and transform the data into a List<List<String>>*/
    CSVParser parsedFile = new CSVParser(new StringCreator(), this.containHeader, this.readTheFile);
    input.nextLine();

    /*Ask about the value that the user is looking for in the CSV File.*/
    System.out.println("What value are you looking for?");
    this.value = input.nextLine();

    /*Ask if the user would like to use column identifiers.*/
    System.out.println(
        "Is there a specific column that you would like to focus on? (Yes - true, No - false)");
    try {
      this.isColumnIdentifierUsed = input.nextBoolean();
      /*If the format is incorrect, print out an error message and prompt for another input.*/
    } catch (InputMismatchException | NullPointerException e) {
      System.err.println("Error: Please follow the correct format.");
      input.nextLine();
      this.isColumnIdentifierUsed = input.nextBoolean();
    }

    /*Find what type of column identifier it is and the value of it.*/
    input.nextLine();
    if (this.isColumnIdentifierUsed) {
      if (this.containHeader) {
        System.out.println("Would you like to input column name or number?");
        this.nameOrIndex = input.nextLine();
        if (this.nameOrIndex.equals("name")) {
          System.out.println("What is the name of the column?");
        } else if (this.nameOrIndex.equals("number")) {
          System.out.println("What is the number of the column? (left-most column is 0)");
        }
      } else {
        System.out.println("What is the number of the column (left-most column is 0): ");
      }
      this.Identifier = input.nextLine();
      /*Create a search class using the user inputs as arguments to find the value the user is looking for*/
      this.timetoSearch = new Search(parsedFile, this.value, this.Identifier, this.nameOrIndex);
    } else {
      this.timetoSearch = new Search(parsedFile, this.value);
    }
    System.out.println("Searching, might take a little bit....");
    this.timetoSearch.searcher();
    /*Print the results of the search*/
    this.timetoSearch.print();
  }
}
