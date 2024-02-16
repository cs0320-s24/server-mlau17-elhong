package main;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import org.eclipse.jetty.server.session.Session;

import java.io.IOException;
import java.util.List;

public class ACSDataSource {
    public static List<List<String>> deserializeACSPackage(String jsonPackage){
        try{
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter = moshi.adapter(Types.newParameterizedType(List.class, List.class));
            return adapter.fromJson(jsonPackage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
