
# Project Details
**Project name:** Server

**Team members and contributions:** elhong and mlau17

**Total estimated time it took to complete project:** -- hours

**Repo Link:**

# Design Choices
## Relationships between classes/interfaces:

Our program is divided in 3 folders: csv parser, data, and server.

The first csvparser folder contains the program to parse and search a given CSV file. This part of the program is designed around three core classes — CSVParser, Searcher, and Main — alongside the interface CreatorFromRow, to cater to diverse user needs. CSVParser focuses on parsing CSV files into a flexible List format. Searcher enables searches within the parsed data based on user inputs, supporting partial and case-insensitive matches to streamline the search process. This class serves user 1.

The Main class is the user interface of the program, facilitating input prompts for file selection, search criteria, and customization options like column identifiers and header presence. The program's architecture also includes specialized classes like IntegerCreator and StringCreator under the CreatorFromRow interface to support a variety of data types, enhancing the program's flexibility and usability.

The getHeader method in CSVParser converts headers to lowercase to minimize case sensitivity issues, streamlining data searches. To improve code clarity and search efficiency, separate methods were introduced for searching by column name or index, and for comprehensive dataset searches. This approach not only simplifies the search method. Additionally, to accommodate user input errors, the program allows users a second attempt to enter data correctly, enhancing usability by making the system more forgiving and reducing potential frustration.
 
The second data folder contains the csv data on Rhode Island income from the ACS. 

The third server folder contains the following main classes used in server:

**-> Server**
The Server class contains the high level logic of the program. It contains the main() method which starts Spark and runs the four handlers. Within main, the CSV handlers take in an instance of GlobalData called serverData representing the loaded csv data from the user input filepath, thus using dependency injection to ensure that the handlers share the same information. Moreover, the SearchCSVHandler also takes in the instance of load in order to access the CSVParser within its own class.
 
**-> BroadBandHandler**
 The BroadBandHandler class handles 
 
 **-> ACSCensusData**
The ACSCensusData class

**-> LoadCSVHandler**
 The LoadCSVHandler class handles the URL input for load csv, requesting the query parameters for filepath and header and  creating a new CSVParser with this information. It also uses Moshi to deserialize the data, which is stored as a   GlobalData global variable for the other handlers to access.
 
**-> ViewCSVHandler**
 The ViewCSVHandler class creates a response containing the data from the CSV file that was first parsed in  LoadCSVHandler,to show the viewers what the data looks like. 
 
**-> SearchCSVHandler**
 The SearchCSVHandler class handles searching for a given searchword by creating an instance of the Search class and  passing in the CSVParser created in LoadCSVHandler. The rows containing the searchword are then returned to the API user.
 
**-> GlobalData**
 The GlobalData class is a wrapper class that adds a layer of abstraction between the CSV data and the handler classes.  This way, the data obtained from LoadCSVHandler can be storied and used globally by the View and Search CSVHandler  classes.

## Data structures we used, why you created them, and other high level explanations:

## Runtime/ space optimizations:

To minimize runtime on ACS API queries, we created three Guava Cache to store (1) the state to state ID information, (2) country to country ID information, and (3)the results obtained from past queries to avoid unecessarily calling the Census API. As such, by first checking if a county or state has been called in the past and storing the information if it has not been called before, we are able to directly retrieve the data from previous requests. 

# Errors/Bugs

# Tests
 Explain the testing suites that you implemented for your program and how each test ensures that a part of the program   works. 


# How to
Run the tests you wrote/were provided
To build and run our program...

