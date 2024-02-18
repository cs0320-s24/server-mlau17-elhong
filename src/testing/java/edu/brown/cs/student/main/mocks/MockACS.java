package edu.brown.cs.student.main.mocks;

/**
 * Mock ACS that would allow the user to input a mock data and will technically override the .getbroadband
 * method to return the correct output if the user inputs a specific state and county name which makes
 * it easier for testing
 **/
public class MockACS {

    private final double broadBandData;

    /**
     * Mock ACS constructor that takes in a percentage for broad band data
     **/
    public MockACS (double constantData){
        this.broadBandData = constantData;
    }

    /**
     * Mock ACS method that will override the .getBroadBand method so it only returns the correct response if
     * the input is equal to a specific state and county
     *
     * @param nameOfState name of state
     * @param nameOfCounty name of county
     **/
    public double getBroadBandData(String nameOfState, String nameOfCounty) {
        if (nameOfState.equals("Rhode Island") && nameOfCounty.equals("Kent")){
            return this.broadBandData;
        }
        return -1;
    }
}
