package edu.brown.cs.student.main.server.API;

/**
 * An exception class where we can string in our own messages and .getMessage to help
 * the stakeholders figure out where the problem is
 */
public class APIException extends Exception {

    /**
     * The constructor which takes in any message as a string which helps the stakeholders and
     * us with debugging
     *
     * @param message - the message we want to be displayed if something is unsuccessful
     */
    public APIException(String message) {
        super(message);
    }
}
