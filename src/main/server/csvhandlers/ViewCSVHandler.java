package main.server.csvhandlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import main.server.csvhandlers.LoadCSVHandler;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * This class...
 *
 *  */

public class ViewCSVHandler implements Route {

    public GlobalData data;

    /**
     * Constructor accepts a filepath
     *
     * @param data
     */
    public ViewCSVHandler(GlobalData data) {
        this.data = data;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        Moshi moshi = new Moshi.Builder().build();
        Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
        // map for the response result
        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("status", "success");
        responseMap.put("CSVdata", this.data.getCsvData().toString());

        //converting to JSON
        return adapter.toJson(responseMap);
    }

}
