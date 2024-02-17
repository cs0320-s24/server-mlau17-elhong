package edu.brown.cs.student.main.server;

public class APIException extends Exception {

    /**
     * Sets up error message to usage/debugging easier for stakeholder
     */
    public APIException(String message) {
        super(message);
    }
}
