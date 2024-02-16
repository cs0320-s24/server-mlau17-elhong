
# Project Details
**Project name:** Server
**Team members and contributions:** elhong and mlau17
**Total estimated time it took to complete project:** -- hours
**Repo Link:**

# Design Choices
**Relationships between classes/interfaces:**

Our program is divided in 3 folders: csv parser, data, and server.

The first csvparser folder contains the program to parse and search a given CSV file with the following classes:
 -> 
 
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

