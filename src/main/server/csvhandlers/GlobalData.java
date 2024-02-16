package main.server.csvhandlers;
import java.util.ArrayList;
import java.util.List;
/**
 * Global wrapper class
 * */

public class GlobalData {
    private List<List<String>> csvData = new ArrayList<>();

    public GlobalData() {
    }


    public List<List<String>> getCsvData() {
            return csvData;
        }
    public void setCsvData(List<List<String>> newData) {
            this.csvData = newData;
        }
}
