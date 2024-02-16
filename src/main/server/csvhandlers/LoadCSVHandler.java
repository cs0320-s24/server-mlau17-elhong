package main.server.csvhandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import main.csvparser.src.main.java.edu.brown.cs.student.main.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This class...
 *
 *  */
public class LoadCSVHandler implements Route {

    public String filepath;
    public Boolean header;
    public GlobalData data;

    /**
     * Constructor accepts a filepath
     *
     * @param data path of CSV
     */
    public LoadCSVHandler(GlobalData data) {
        this.data = data;
        }

        /**
         * If from link
         * */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Moshi moshi = new Moshi.Builder().build();
        Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
        // map for the response result
        Map<String, Object> responseMap = new HashMap<>();

        String filepath = request.queryParams("filepath");
        String header = request.queryParams("header");
        this.header = this.checkHeader(header);
        if (filepath != null) {
            this.filepath = filepath;
            FileReader reader = new FileReader(filepath);
            CSVParser parser = new CSVParser<List<String>>(new StringCreator(), this.header,reader);
            this.data.setCsvData(parser.sortData());

            responseMap.put("status", "success");
            responseMap.put("filepath", filepath);

            //converting to JSON
            return adapter.toJson(responseMap);
        }
        responseMap.put("status", "error_datasource");
        return adapter.toJson(responseMap);
    }

    /**
     * Method that sets the header boolean according to user input
     *
     * */
    private Boolean checkHeader(String headerInput){
        // setting the header to a Boolean
        if (headerInput.equals("Yes")) {
            return true;}
        if (headerInput.equals("No")){
            return false;}
        else{
            System.out.println("Please try again and answer using Yes or No");
            System.exit(0);}
        return false;
    }

}
