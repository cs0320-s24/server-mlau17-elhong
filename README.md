
# Project Details
**Project name:** Server

**Team members and contributions:** elhong and mlau17

**Total estimated time it took to complete project:** 30 hours

**Repo Link:** https://github.com/cs0320-s24/server-mlau17-elhong.git

# Design Choices
## Relationships between classes/interfaces:

Our program is divided into 3 folders: CSV parser, data, and server (API & CSV Handlers & Server).

The first CSV parser folder contains the program to parse and search a given CSV file. This part of the program is designed around three core classes — CSVParser, Searcher, and Main — alongside the interface CreatorFromRow, to cater to diverse user needs. CSVParser focuses on parsing CSV files into a flexible List format. Searcher enables searches within the parsed data based on user inputs, supporting partial and case-insensitive matches to streamline the search process. This class serves user 1.

The Main class is the user interface of the program, facilitating input prompts for file selection, search criteria, and customization options like column identifiers and header presence. The program's architecture also includes specialized classes like IntegerCreator and StringCreator under the CreatorFromRow interface to support a variety of data types, enhancing the program's flexibility and usability.

The getHeader method in CSVParser converts headers to lowercase to minimize case sensitivity issues, streamlining data searches. To improve code clarity and search efficiency, separate methods were introduced for searching by column name or index, and for comprehensive dataset searches. This approach not only simplifies the search method. Additionally, to accommodate user input errors, the program allows users a second attempt to enter data correctly, enhancing usability by making the system more forgiving and reducing potential frustration.
 
The second data folder contains the CSV data on Rhode Island income from the ACS & a JSON file which we use for testing. 

The third server folder contains the following main classes used in the server:

**-> Server**
The Server class contains the high-level logic of the program. It contains the main() method which starts Spark and runs the four handlers. Within main, the CSV handlers take in an instance of GlobalData called serverData representing the loaded CSV data from the user input file path, thus using dependency injection to ensure that the handlers share the same information. Moreover, the SearchCSVHandler also takes in the instance of load to access the CSVParser within its class. The BroadBandHandler also takes in an instance of Global Caches called ACSCacheData, which is where we store all the caches for the BroadBandHandler to use thus using dependency injection to ensure that the information does not clear every time and is usable across data structures. 
 
**-> BroadBandHandler**
 The BroadBandHandler class handles the user request of a specific state and county and finds the corresponding broadband data for that request. It contains helper methods to send the request to the ACS API and calls ACSDataSource to deserialize the JSON file that the ACS API returned to us. It also populates the caches that are contained in the ACSCacheData, which we use to stop pinging the ACS API repeatedly for the IDs or if the user already searched for those inputs. If there is an error with finding the data needed, it will output an error message that helps the user determine where the problem is. 
 
 **-> ACSDataSource**
The ACSDataSource class contains a method to help deserialize the JSON package given to us when we send a request to the ACS API that way it is easier for us to parse through it and find the data that we are looking for. The method uses Moshi to help us deserialize. It is called by BroadBandHandler after it sends the request to the ACS API. It does throw an APIException if the JSON is invalid.

**-> ACSCacheData**
The ACSCacheData class is like a global class that contains the three caches that we made and populated each time a new request comes through. Acts as a proxy between ACSCacheData and BroadBandHandler, it will search through the caches for data that has been searched before and return that value to BroadBandHandler to use instead of BroadBandHandler pinging the ACS API over and over again. It also contains a method that populates the state to state id cache so we can just search for any state instead of only the ones the user inputted.

**-> APIException**
The APIException class is an exception class that could be thrown with a customizable message anywhere the code will encounter a problem. It is used by BroadBandHandler, ACSDataSource, and ACSCacheData.

**-> LoadCSVHandler**
 The LoadCSVHandler class handles the URL input for loading a CSV, requesting the query parameters for file path and header and  creating a new CSVParser with this information. It also uses Moshi to deserialize the data, which is stored as a   GlobalData global variable for the other handlers to access.
 
**-> ViewCSVHandler**
 The ViewCSVHandler class creates a response containing the data from the CSV file that was first parsed in  LoadCSVHandler, to show the viewers what the data looks like. 
 
**-> SearchCSVHandler**
 The SearchCSVHandler class handles searching for a given search word by creating an instance of the Search class and  passing in the CSVParser created in LoadCSVHandler. The rows containing the search word are then returned to the API user.
 
**-> GlobalData**
 The GlobalData class is a wrapper class that adds a layer of abstraction between the CSV data and the handler classes.  This way, the data obtained from LoadCSVHandler can be stored and used globally by the View and Search CSVHandler  classes.

## Data structures we used, why you created them, and other high-level explanations:

For the ACS API deserializer in ACSDataSource, I converted the JSON package into a List<List<String>> because it was similar to how the file was displayed in the ACS API and it allowed us to parse through it using for loops and find certain values using .get().

## Runtime/ space optimizations:

To minimize runtime on ACS API queries, we created three Guava Caches in the class ACSCacheData to store (1) the state to state ID information, (2) county to county ID information, and (3)the results obtained from past queries to avoid unnecessarily calling the Census API. As such, we populate the stateID cache once, fill in the countyID in the broadbandHandler, and continuously update the past queries cache by first checking if a county or state has been called in the past and storing the information if it has not been called before, thus directly retrieving the data from a list of previous requests. 

# Errors/Bugs
One of the largest problems we encountered was merging in Git Hub, for some reason it would not display where the merge conflicts were, and even with help from TAs, we could not complete merging without our project getting messed up or some GitHub error message being displayed. Unfortunately, we still cannot figure out where the problem was but to help us continue the project we tried to create multiple branches, communicate effectively when we are going to pull, and try to match up as much information as possible before pushing.

Another bug one of us encountered was with the imports so she was not able to test any of her tests, she tried to add another dependency to her pom.xml, mvn clean install, mvn package, reload project, and searched on edStem and Google. She also compared all of her files with her partner, but still, no solution was found and due to time reasons she just decided to sample write all her tests. She is hoping to inquire from the TA or Professor Nelson soon. Due to these reasons, the tests for BroadBandHandler and ACSDataSource do not pass.

Next, our code does not take into consideration that the user accidentally puts in the lowercase of the state and county names. To replicate this, just put in the lowercase of a state or county name then wait for the response, which should just say bad request and tell the user to check spelling and other things.

Finally, while our code accounts for all other cases of missing variables and calls on view and search csv without first loading, for reasons we do not yet comprehend, when we input searchcsv with an invalid search word into the URL without first loading, a 500 error code is returned. This does not occur when only inputting searchcsv or when the same input is given after first loading, but just in this specific case and order.


# Tests
Explain the testing suites that you implemented for your program and how each test ensures that a part of the program works. 

Unfortunately, due to the nature of our design, we were not able to fully use the mock data even though we created one. If I were to implement I would use mostly the same steps, but then have an interface that contains three methods — .getStateID, .getCountyID, and .getBroadBand then have the mock data override the .getBroadBand method in it. Of course, this would change our current design of the BroadBandHandler to use the interface and call .getBroadBand. This way when we are testing, after initializing the Mock data if we put in the inputs that we set with the override .getBroadBand method to mock we searched the web API, and this way we would not have to repeatedly call the web API.

The testing suite for BroadBandHandler tests a variety of cases including no parameters, one parameter, no inputs, missing inputs, invalid inputs, valid inputs, valid inputs when the user forgets to put county after the name, when the data does not exist in the ACS API, and if the date-time is being recorded.

The testing suite for ACSDataSource tests if the deserializer is working and turning the JSON file into a list of list of strings.

The testing suite for ACSCacheData tests if the state-to-state ID cache is populating correctly, and all the get and add methods. 

# How to

To run the tests you wrote/were provided: Run our tests using the large or small green buttons in Intellij, or by using mvn package.

To build and run our program:

For ACS API:
1.) Click the run button
2.) Click on the link that is displayed
3.) After 3232, add "/broadband?state=*&county=*"
4.) Replace the * with the name of the state and county you want to look for
5.) Wait and the response should be out soon!

For CSV API:
1.) Click the run button
2.) Click on the link that is displayed
3.) After 3232, to load a csv add "/loadcsv?filepath=[insert your absolute filepath here]&header=[insert Yes or No]"
4.) To view your csv, delete the past input and write "viewcsv"
5.) To search for a word in your csv, delete the past input and write "searchcsv?searchword=[insert your searchword]"


