
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
 The BroadBandHandler class handles the user request of a specific state and county and finds the corresponding broadband data for that request. It contains helper methods to send the request to the ACS API and calls ACSCensusData to deserializes the JSON file that the ACS API returned to us. It also populates the caches that is contained in the ACSCacheData, which we use to stop pinging the ACS API repeatedly for the ids or if the user already searched for those inputs.
 
 **-> ACSCensusData**
The ACSCensusData class contains a method to help deserialize the json package given to us when we send a request to the ACS API that way it is easier for us to parse through it and find the data that we are looking for. The method uses moshi to help us deserialize. It is called by BroadBandHandler after it sends the request to the ACS API.

**-> ACSCacheData**
The ACSCacheData class is like a global class that contains the three caches that we made and populated each time a new request comes through. Basically acts as a proxy between ACSCacheData and BroadBandHandler, it will search through the caches for data that has been searched before and return that value to BroadBandHandler to use instead of BroadBandHandler pinging the ACS API over and over again. It also contains a method that populates the state to state id cache so we can just search for any state instead of only the ones the user inputted.

**-> LoadCSVHandler**
 The LoadCSVHandler class handles the URL input for load csv, requesting the query parameters for filepath and header and  creating a new CSVParser with this information. It also uses Moshi to deserialize the data, which is stored as a   GlobalData global variable for the other handlers to access.
 
**-> ViewCSVHandler**
 The ViewCSVHandler class creates a response containing the data from the CSV file that was first parsed in  LoadCSVHandler,to show the viewers what the data looks like. 
 
**-> SearchCSVHandler**
 The SearchCSVHandler class handles searching for a given searchword by creating an instance of the Search class and  passing in the CSVParser created in LoadCSVHandler. The rows containing the searchword are then returned to the API user.
 
**-> GlobalData**
 The GlobalData class is a wrapper class that adds a layer of abstraction between the CSV data and the handler classes.  This way, the data obtained from LoadCSVHandler can be storied and used globally by the View and Search CSVHandler  classes.

## Data structures we used, why you created them, and other high level explanations:

For the ACS API deserializer in ACSDataSource, I converted the JSON package into a List<List<String>> because it was similar to how the file was displayed in the ACS API and it allowed us to parse through it using for loops and find certain values using .get(). 

## Runtime/ space optimizations:

To minimize runtime on ACS API queries, we created three Guava Caches in the class ACSCacheData to store (1) the state to state ID information, (2) country to country ID information, and (3)the results obtained from past queries to avoid unecessarily calling the Census API. As such, we populate the stateID cache once, fill in the countyID in the broadbandHandler, and continuously update the past queries cache by first checking if a county or state has been called in the past and storing the information if it has not been called before, thus directly retrieving the data from a list of previous requests. 

# Errors/Bugs

One of the largest problems we encountered was merging in github, for some reason it would not display where the merge conflicts were and even with help from TAs we could not complete merging without our project getting messed up or some github error message being displayed. Unfortunately, we still cannot figure out where the problem was but to help us continue the project we 

# Tests
 Explain the testing suites that you implemented for your program and how each test ensures that a part of the program   works. 


# How to
Run the tests you wrote/were provided
To build and run our program...

