package bank;

import com.google.gson.*;
import com.google.gson.TypeAdapterFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrivateBankSerializer implements JsonSerializer<List<Transaction> > {

    @Override
    public JsonElement serialize(List<Transaction> transactions, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray ja = new JsonArray();
       for(Transaction t : transactions){
           JsonObject obj = new JsonObject();
           obj.addProperty("CLASSNAME",t.getClass().getSimpleName());
           obj.add("INSTANCE", new Gson().toJsonTree(t));
           ja.add(obj);
       }

        return ja;

    }

}
