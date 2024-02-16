
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

** -> Server**
 The Server class 
 
** -> BroadBandHandler**
 The BroadBandHandler class 
 
 ** -> ACSCensusData**
The ACSCensusData class

** -> LoadCSVHandler**
 The LoadCSVHandler class
 
** -> ViewCSVHandler**
 The ViewCSVHandler class
 
** -> SearchCSVHandler**
 The SearchCSVHandler class
 
** -> GlobalData**
 The GlobalData class is a wrapper class 

Data structures you used, why you created it, and other high level explanations.

Runtime/ space optimizations:
To minimize runtime on ACS API queries, we created a Guava Cache to store 

# Errors/Bugs

# Tests
 Explain the testing suites that you implemented for your program and how each test ensures that a part of the program works. 


# How to
Run the tests you wrote/were provided
Build and run your program

